package org.safehaus.analysis;

import com.google.common.base.Optional;
import kafka.serializer.StringDecoder;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import scala.Tuple2;
import scala.Tuple3;
import scala.Tuple4;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;
import static com.datastax.spark.connector.japi.CassandraStreamingJavaUtil.javaFunctions;

/**
 * Created by neslihan on 08.09.2015.
 */
public class JiraStreamFunctions {
    private static String jiraMetricTblName = new String("user_jira_metric_info");
    private static AtomicReference<Date> mostRecentJiraTaskDate = new AtomicReference<>(  );
    private static String keyspaceName = new String("jarvis");

    public static void computeJiraProductivityMetric(JavaPairDStream<String, JiraMetricIssue> jiraAuthorIssuesDStream)
    {

        final Function2<List<Tuple4<Integer, Integer, Integer,Integer>>, Optional<Tuple4<Integer, Integer, Integer,Integer>>, Optional<Tuple4<Integer, Integer, Integer,Integer>>> updateOrSaveStaleFunction =
                new Function2<List<Tuple4<Integer, Integer, Integer,Integer>>, Optional<Tuple4<Integer, Integer, Integer,Integer>>, Optional<Tuple4<Integer, Integer, Integer,Integer>>>() {
                    @Override
                    public Optional<Tuple4<Integer, Integer, Integer,Integer>> call(List<Tuple4<Integer, Integer, Integer,Integer>> values, Optional<Tuple4<Integer, Integer, Integer,Integer>> state) {
                        Tuple4<Integer, Integer, Integer,Integer> newSum = state.or(
                                new Tuple4<Integer, Integer, Integer, Integer>( 0, 0, 0, 0 ) );
                        Integer finishedCount = newSum._1();
                        Integer allCount = newSum._2();

                        Integer thisMonth= -1;
                        Integer thisYear = -1;
                        boolean firstTime; // = false;

                        if(state.isPresent())
                        {
                            firstTime = false;
                            thisMonth = newSum._3();
                            thisYear = newSum._4();

                            //System.out.println("State present checks! "+thisMonth+"-"+thisYear);

                            if(mostRecentJiraTaskDate.get() != null)
                            {
                                Calendar cal = Calendar.getInstance();
                                cal.setTime( mostRecentJiraTaskDate.get() );

                                if( cal.get( Calendar.YEAR ) > thisYear || (cal.get(Calendar.YEAR) == thisYear && cal.get(Calendar.MONTH) > thisMonth))
                                {
                                    //System.out.println("Returning absent! values: "+thisMonth+"-"+thisYear);
                                    return Optional.absent();
                                }
                            }

                        } else
                        {
                            firstTime = true;
                        }

                        for (Tuple4<Integer, Integer, Integer,Integer> value : values) {
                            finishedCount += value._1();
                            allCount += value._2();

                            if(firstTime){
                                thisMonth = value._3();
                                thisYear = value._4();
                                firstTime = false;
                            }
                        }


                        newSum = new Tuple4<Integer, Integer, Integer, Integer>(finishedCount, allCount, thisMonth, thisYear);
                        return Optional.of(newSum);
                    }
                };

        JavaPairDStream<String,JiraMetricIssue> jiraCleanFilteredIssuesDStream = jiraAuthorIssuesDStream.filter(new Function<Tuple2<String, JiraMetricIssue>, Boolean>() {
            public Boolean call(Tuple2<String, JiraMetricIssue> jiraMetricIssueTuple2) throws Exception {
                return (jiraMetricIssueTuple2._2().getUpdateDate() != null && jiraMetricIssueTuple2._2().getStatus() != null);
            }
        });
        JavaDStream<Date> jiraUpdateDateStream =
                jiraCleanFilteredIssuesDStream.map( new Function<Tuple2<String, JiraMetricIssue>, Date>()
                {
                    @Override
                    public Date call( final Tuple2<String, JiraMetricIssue> stringJiraMetricIssueTuple2 )
                            throws Exception
                    {
                        return stringJiraMetricIssueTuple2._2().getUpdateDate();
                    }
                } );


        jiraUpdateDateStream.foreachRDD( new Function<JavaRDD<Date>, Void>()
        {
            @Override
            public Void call( final JavaRDD<Date> dateJavaRDD ) throws Exception
            {
                dateJavaRDD.foreach( new VoidFunction<Date>()
                {
                    @Override
                    public void call( final Date date ) throws Exception
                    {
                        //System.out.println( "new rdd's!!!" );
                        if ( mostRecentJiraTaskDate.get() == null || mostRecentJiraTaskDate.get().before( date ) )
                        {
                            mostRecentJiraTaskDate.set( date );
                        }
                    }
                } );

                //System.out.println( "Most recent update date: " + mostRecentJiraTaskDate.get() );
                return null;
            }
        } );

        // author-month-year,finishedCnt-totalCnt-jiraMetricIssue
        JavaPairDStream<Tuple3<String, Integer, Integer>, Tuple4<Integer, Integer, Integer,Integer>> jiraUserProductivityMetric =
                jiraCleanFilteredIssuesDStream.mapToPair(new PairFunction<Tuple2<String, JiraMetricIssue>, Tuple3<String, Integer, Integer>,Tuple4<Integer, Integer, Integer,Integer>>() {
                    public Tuple2<Tuple3<String, Integer, Integer>, Tuple4<Integer, Integer, Integer,Integer>> call(Tuple2<String, JiraMetricIssue> stringJiraMetricIssueTuple2) throws Exception {
                        Integer month, year;
                        Date updateDt = stringJiraMetricIssueTuple2._2().getUpdateDate();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(updateDt);

                        month = cal.get(Calendar.MONTH);
                        year = cal.get(Calendar.YEAR) ;

                        if (stringJiraMetricIssueTuple2._2().getStatus().compareToIgnoreCase("Closed") == 0 || stringJiraMetricIssueTuple2._2().getStatus().compareToIgnoreCase("Resolved") == 0) {
                            return new Tuple2<Tuple3<String, Integer, Integer>, Tuple4<Integer, Integer, Integer,Integer>>(
                                    new Tuple3<String, Integer, Integer>(stringJiraMetricIssueTuple2._1(), month, year),
                                    new Tuple4<Integer, Integer, Integer,Integer>(1, 1, month, year));
                        } else {
                            return new Tuple2<Tuple3<String, Integer, Integer>, Tuple4<Integer, Integer, Integer,Integer>>(
                                    new Tuple3<String, Integer, Integer>(stringJiraMetricIssueTuple2._1(), month, year),
                                    new Tuple4<Integer, Integer, Integer,Integer>(0, 1, month, year));
                        }
                    }
                } ).updateStateByKey(updateOrSaveStaleFunction);

        jiraUserProductivityMetric.print();

        //Save only those stream who has finalized records
        JavaDStream<UserMetricInfo.UserMetricInfoInternal> metricsDStream = jiraUserProductivityMetric.filter(
                new Function<Tuple2<Tuple3<String, Integer, Integer>, Tuple4<Integer, Integer, Integer, Integer>>, Boolean>()

                {
                    @Override
                    public Boolean call(
                            final Tuple2<Tuple3<String, Integer, Integer>, Tuple4<Integer, Integer, Integer,
                                    Integer>> tuple3Tuple4Tuple2 )
                            throws Exception
                    {

                        Integer thisMonth = tuple3Tuple4Tuple2._1()._2();
                        Integer thisYear = tuple3Tuple4Tuple2._1()._3();

                        if(mostRecentJiraTaskDate.get() != null)
                        {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime( mostRecentJiraTaskDate.get() );

                            if( cal.get( Calendar.YEAR ) > thisYear || (cal.get(Calendar.YEAR) == thisYear && cal.get(Calendar.MONTH) > thisMonth))
                            {
                                //System.out.println("Filtering out these tuples for database! ");
                                return true;
                            }
                        }

                        return false;
                    }
                } ).map(
                new Function<Tuple2<Tuple3<String, Integer, Integer>, Tuple4<Integer, Integer, Integer,
                        Integer>>, UserMetricInfo.UserMetricInfoInternal>()

                {
                    @Override
                    public UserMetricInfo.UserMetricInfoInternal call(
                            final Tuple2<Tuple3<String, Integer, Integer>, Tuple4<Integer, Integer, Integer,
                                    Integer>> tuple3Tuple4Tuple2 )
                            throws Exception
                    {

                        Calendar cal = Calendar.getInstance();
                        cal.clear();
                        cal.set( Calendar.YEAR, tuple3Tuple4Tuple2._1()._3() );
                        cal.set(Calendar.MONTH, tuple3Tuple4Tuple2._1()._2());

                        UserMetricInfo.UserMetricInfoInternal metricInfo = new UserMetricInfo.UserMetricInfoInternal();
                        metricInfo.setDeveloperId(tuple3Tuple4Tuple2._1()._1());
                        metricInfo.setJiraProductivity(
                                ( ( double ) tuple3Tuple4Tuple2._2()._1() / ( double ) tuple3Tuple4Tuple2._2()._2() )
                                        * 100.0 );
                        metricInfo.setMetricMonthDate(cal.getTime());

                        return metricInfo;
                    }
                } );

        metricsDStream.print();

        javaFunctions(metricsDStream).writerBuilder( keyspaceName, jiraMetricTblName, mapToRow(UserMetricInfo.UserMetricInfoInternal.class) ).saveToCassandra();
    }

    public static void startJiraStream(JavaStreamingContext jssc, HashSet<String> topicsSet, HashMap<String, String> kafkaParams)
    {

        JavaPairInputDStream<String, JiraMetricIssue> jiraLogDStream = KafkaUtils
                .createDirectStream(jssc, String.class, JiraMetricIssue.class, StringDecoder.class,
                        JiraMetricIssueKafkaSerializer.class, kafkaParams, topicsSet);

        JavaDStream<JiraMetricIssue> jiraIssuesDStream = jiraLogDStream.map(
                new Function<Tuple2<String, JiraMetricIssue>, JiraMetricIssue>() {
                    public JiraMetricIssue call(Tuple2<String, JiraMetricIssue> tuple2) {
                        return tuple2._2();
                    }
                });

        JavaPairDStream<String, JiraMetricIssue> jiraAuthorIssuesDStream = jiraIssuesDStream.filter(new Function<JiraMetricIssue, Boolean>() {
            @Override
            public Boolean call(JiraMetricIssue jiraMetricIssue) throws Exception {
                return jiraMetricIssue.getAssigneeName()!=null;
            }
        }).mapToPair(
                new PairFunction<JiraMetricIssue, String, JiraMetricIssue>() {
                    public Tuple2<String, JiraMetricIssue> call(JiraMetricIssue jiraMetricIssue) throws Exception {
                        return new Tuple2<String, JiraMetricIssue>(jiraMetricIssue.getAssigneeName(),
                                jiraMetricIssue);
                    }
                });

        JavaPairDStream<String, Integer> jiraAuthorIssueCountDStream = jiraAuthorIssuesDStream.mapToPair(
                new PairFunction<Tuple2<String, JiraMetricIssue>, String, Integer>() {
                    public Tuple2<String, Integer> call(Tuple2<String, JiraMetricIssue> JiraMetricIssueTuple2)
                            throws Exception {
                        return new Tuple2<String, Integer>(JiraMetricIssueTuple2._1(), 1);
                    }
                }).reduceByKey(new Function2<Integer, Integer, Integer>() {
            public Integer call(Integer i1, Integer i2) throws Exception {
                return i1 + i2;
            }
        });

        jiraAuthorIssueCountDStream.print();

        computeJiraProductivityMetric(jiraAuthorIssuesDStream);
    }

}
