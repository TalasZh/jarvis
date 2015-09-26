package org.safehaus.analysis;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.safehaus.confluence.model.ConfluenceMetric;
import org.safehaus.dao.entities.ConfluenceMetricInfo;

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

import com.google.common.base.Optional;

import kafka.serializer.StringDecoder;
import scala.Tuple2;
import scala.Tuple3;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;
import static com.datastax.spark.connector.japi.CassandraStreamingJavaUtil.javaFunctions;

/**
 * Created by neslihan on 09.09.2015.
 */
public class ConfluenceStreamFunctions {
    private static String confluenceMetricTlbName = new String("user_confluence_metric_info");
    private static AtomicReference<Date> mostRecentConfluenceActivityDate = new AtomicReference<>();
    private static String keyspaceName = new String("jarvis");

    public static Function2<List<Tuple3<Integer, Integer,Integer>>, Optional<Tuple3<Integer, Integer,Integer>>, Optional<Tuple3<Integer, Integer,Integer>>> CONFLUENCE_ACTIVITY_RUNNING_SUM =
            new Function2<List<Tuple3<Integer, Integer,Integer>>, Optional<Tuple3<Integer, Integer,Integer>>, Optional<Tuple3<Integer, Integer,Integer>>>() {
                @Override
                public Optional<Tuple3<Integer, Integer,Integer>> call(List<Tuple3<Integer, Integer,Integer>> values, Optional<Tuple3<Integer, Integer,Integer>> state) throws Exception {
                    Tuple3<Integer, Integer,Integer> newSum = state.or(new Tuple3<Integer, Integer,Integer>(0, 0, 0));
                    Integer allCommitsCnt = newSum._1();

                    Integer thisMonth = -1;
                    Integer thisYear = -1;
                    boolean firstTime;

                    if(state.isPresent())
                    {
                        firstTime = false;
                        thisMonth = newSum._2();
                        thisYear = newSum._3();

                        //System.out.println("State present checks! "+thisMonth+"-"+thisYear);

                        if(mostRecentConfluenceActivityDate.get() != null)
                        {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime( mostRecentConfluenceActivityDate.get() );

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

                    for (Tuple3<Integer, Integer,Integer> value : values) {
                        allCommitsCnt += value._1();

                        if(firstTime){
                            thisMonth = value._2();
                            thisYear = value._3();
                            firstTime = false;
                        }
                    }


                    newSum = new Tuple3<Integer, Integer, Integer>(allCommitsCnt, thisMonth, thisYear);
                    return Optional.of(newSum);
                }
            };


    public static void computeConfluenceCollaborationMetric( JavaDStream<ConfluenceMetric> confluenceIssuesDStream)
    {
        //author,month,year - cntCommits,month, year
        JavaPairDStream<Tuple3<String, Integer, Integer>, Tuple3<Integer,Integer,Integer>> confluenceUserMonthlyProductivityMetrics = confluenceIssuesDStream.filter(new Function<ConfluenceMetric, Boolean>() {
            @Override
            public Boolean call(ConfluenceMetric confluenceMetric) throws Exception {

                return (confluenceMetric!= null && confluenceMetric.getAuthorUsername() != null );
            }
        }).mapToPair(new PairFunction<ConfluenceMetric, Tuple3<String, Integer, Integer>, Tuple3<Integer, Integer, Integer>>() {
            @Override
            public Tuple2<Tuple3<String, Integer, Integer>, Tuple3<Integer, Integer, Integer>> call(ConfluenceMetric confluenceMetric) throws Exception {
                Date date = confluenceMetric.getWhen();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                Integer month = cal.get(Calendar.MONTH);
                Integer year = cal.get(Calendar.YEAR);

                System.out.println(confluenceMetric.getAuthorUsername() + " - " + confluenceMetric.getAuthorDisplayName() + " - "
                        + confluenceMetric.getTitle() + confluenceMetric.getVersionNumber() + "\n\n\n");

                return new Tuple2<Tuple3<String, Integer, Integer>, Tuple3<Integer, Integer, Integer>>(new Tuple3<String, Integer, Integer>(confluenceMetric.getAuthorUsername(), month, year),
                        new Tuple3<>(1, month, year));
            }
        }).updateStateByKey(CONFLUENCE_ACTIVITY_RUNNING_SUM);

        confluenceUserMonthlyProductivityMetrics.print();

        // This stream keeps track of the most-recent-date at each batch of the stream
        JavaDStream<Date> confluenceUpdateDateStream =
                confluenceIssuesDStream.filter(new Function<ConfluenceMetric, Boolean>() {
                    @Override
                    public Boolean call(ConfluenceMetric confluenceMetricIssue) throws Exception {
                        return (confluenceMetricIssue != null);
                    }
                }).map(new Function<ConfluenceMetric, Date>() {
                    @Override
                    public Date call(final ConfluenceMetric confluenceMetricIssue)
                            throws Exception {
                        return confluenceMetricIssue.getWhen();
                    }
                });


        confluenceUpdateDateStream.foreachRDD(new Function<JavaRDD<Date>, Void>() {
            @Override
            public Void call(final JavaRDD<Date> dateJavaRDD) throws Exception {
                dateJavaRDD.foreach(new VoidFunction<Date>() {
                    @Override
                    public void call(final Date date) throws Exception {
                        //System.out.println( "new rdd's!!!" );
                        if (mostRecentConfluenceActivityDate.get() == null || mostRecentConfluenceActivityDate.get().before(date)) {
                            mostRecentConfluenceActivityDate.set(date);
                        }
                    }
                });
                return null;
            }
        });

        JavaDStream<ConfluenceMetricInfo.ConfluenceMetricInfoInternal> metricsDStream = confluenceUserMonthlyProductivityMetrics.filter(
                new Function<Tuple2<Tuple3<String, Integer, Integer>, Tuple3<Integer, Integer, Integer>>, Boolean>()

                {
                    @Override
                    public Boolean call(
                            final Tuple2<Tuple3<String, Integer, Integer>, Tuple3<Integer, Integer, Integer>> tuple3Tuple3Tuple2)
                            throws Exception {

                        Integer thisMonth = tuple3Tuple3Tuple2._1()._2();
                        Integer thisYear = tuple3Tuple3Tuple2._1()._3();

                        if (mostRecentConfluenceActivityDate.get() != null) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(mostRecentConfluenceActivityDate.get());

                            if (cal.get(Calendar.YEAR) > thisYear || (cal.get(Calendar.YEAR) == thisYear && cal.get(Calendar.MONTH) > thisMonth)) {
                                //System.out.println("Filtering out these tuples for database! ");
                                return true;
                            }
                        }

                        return false;
                    }
                }).map(
                new Function<Tuple2<Tuple3<String, Integer, Integer>, Tuple3<Integer, Integer, Integer>>, ConfluenceMetricInfo.ConfluenceMetricInfoInternal>()

                {
                    @Override
                    public ConfluenceMetricInfo.ConfluenceMetricInfoInternal call(
                            final Tuple2<Tuple3<String, Integer, Integer>, Tuple3<Integer, Integer, Integer>> tuple3Tuple3Tuple2)
                            throws Exception {

                        Calendar cal = Calendar.getInstance();
                        cal.clear();
                        cal.set(Calendar.YEAR, tuple3Tuple3Tuple2._1()._3());
                        cal.set(Calendar.MONTH, tuple3Tuple3Tuple2._1()._2());

                        ConfluenceMetricInfo.ConfluenceMetricInfoInternal metricInfo = new ConfluenceMetricInfo.ConfluenceMetricInfoInternal();
                        metricInfo.setDeveloperId(tuple3Tuple3Tuple2._1()._1());
                        metricInfo.setConfluenceActivityCount(tuple3Tuple3Tuple2._2()._1());
                        metricInfo.setMetricMonthTimestamp(cal.getTimeInMillis());

                        System.out.println("CONFLUENCE METRICS STREAM: " + metricInfo.getDeveloperId() + " " + metricInfo
                                .getConfluenceActivityCount() + " " + metricInfo.getMetricMonthTimestamp());
                        return metricInfo;
                    }
                });

        metricsDStream.print();
        javaFunctions(metricsDStream).writerBuilder( keyspaceName, confluenceMetricTlbName, mapToRow(ConfluenceMetricInfo.ConfluenceMetricInfoInternal.class) ).saveToCassandra();
    }

    public static void startConfluenceStreaming(JavaStreamingContext jssc, HashSet<String> topicsSet, HashMap<String, String> kafkaParams)
    {
        JavaPairInputDStream<String, ConfluenceMetric> confluenceLogDStream = KafkaUtils.createDirectStream(jssc,
                String.class, ConfluenceMetric.class, StringDecoder.class, ConfluenceMetricKafkaSerializer.class,
                kafkaParams, topicsSet);

        confluenceLogDStream.print();

        JavaDStream<ConfluenceMetric> confluenceIssuesDStream = confluenceLogDStream.map(new Function<Tuple2<String, ConfluenceMetric>, ConfluenceMetric>() {
            @Override
            public ConfluenceMetric call(Tuple2<String, ConfluenceMetric> stringConfluenceMetricIssueTuple2) throws Exception {
                return stringConfluenceMetricIssueTuple2._2();
            }
        });

        computeConfluenceCollaborationMetric(confluenceIssuesDStream);
    }

}
