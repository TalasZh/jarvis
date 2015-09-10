package org.safehaus.analysis;

import com.google.common.base.*;
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
import org.safehaus.stash.model.StashMetricIssue;
import scala.Tuple2;
import scala.Tuple3;

import java.util.*;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;
import static com.datastax.spark.connector.japi.CassandraStreamingJavaUtil.javaFunctions;

/**
 * Created by neslihan on 09.09.2015.
 */
public class StashStreamFunctions {
    private static String keyspaceName = new String("jarvis");
    private static String stashMetricTlbName = new String("user_stash_metric_info");
    private static AtomicReference<Date> mostRecentStashCommitDate = new AtomicReference<>();

    public static Function2<List<Tuple3<Integer, Integer,Integer>>, com.google.common.base.Optional<Tuple3<Integer, Integer,Integer>>, com.google.common.base.Optional<Tuple3<Integer, Integer,Integer>>> STASH_COMMIT_RUNNING_SUM =
            new Function2<List<Tuple3<Integer, Integer,Integer>>, com.google.common.base.Optional<Tuple3<Integer, Integer,Integer>>, com.google.common.base.Optional<Tuple3<Integer, Integer,Integer>>>() {
                @Override
                public com.google.common.base.Optional<Tuple3<Integer, Integer,Integer>> call(List<Tuple3<Integer, Integer,Integer>> values, com.google.common.base.Optional<Tuple3<Integer, Integer,Integer>> state) throws Exception {
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

                        if(mostRecentStashCommitDate.get() != null)
                        {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime( mostRecentStashCommitDate.get() );

                            if( cal.get( Calendar.YEAR ) > thisYear || (cal.get(Calendar.YEAR) == thisYear && cal.get(Calendar.MONTH) > thisMonth))
                            {
                                //System.out.println("Returning absent! values: "+thisMonth+"-"+thisYear);
                                return com.google.common.base.Optional.absent();
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
                    return com.google.common.base.Optional.of(newSum);
                }
            };

    public static void computeStashProductivityMetric(JavaDStream<StashMetricIssue> stashIssuesDStream)
    {
        //author,month,year - cntCommits,month, year
        JavaPairDStream<Tuple3<String, Integer, Integer>, Tuple3<Integer,Integer,Integer>> stashUserMonthlyProductivityMetrics = stashIssuesDStream.filter(new Function<StashMetricIssue, Boolean>() {
            @Override
            public Boolean call(StashMetricIssue stashMetricIssue) throws Exception {
                return (stashMetricIssue!= null && stashMetricIssue.getAuthor() != null && stashMetricIssue.getAuthor().getName() != null);
            }
        }).mapToPair(new PairFunction<StashMetricIssue, Tuple3<String, Integer, Integer>, Tuple3<Integer, Integer, Integer>>() {
            @Override
            public Tuple2<Tuple3<String, Integer, Integer>, Tuple3<Integer, Integer, Integer>> call(StashMetricIssue stashMetricIssue) throws Exception {
                Date date = new Date(stashMetricIssue.getAuthorTimestamp());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                Integer month = cal.get(Calendar.MONTH);
                Integer year = cal.get(Calendar.YEAR);

                System.out.println(stashMetricIssue.getAuthor().getName() + " - " + stashMetricIssue.getAuthor().getDisplayName() + " - "
                        + stashMetricIssue.getAuthor().getEmailAddress() + stashMetricIssue.getAuthor().getId() + "\n\n\n");

                return new Tuple2<Tuple3<String, Integer, Integer>, Tuple3<Integer, Integer, Integer>>(new Tuple3<String, Integer, Integer>(stashMetricIssue.getAuthor().getName(), month, year),
                        new Tuple3<>(1, month, year));
            }
        }).updateStateByKey(STASH_COMMIT_RUNNING_SUM);

        stashUserMonthlyProductivityMetrics.print();
        //stashUserMonthlyProductivityMetrics.count();

        // This stream keeps track of the most-recent-date at each batch of the stream
        JavaDStream<Date> stashUpdateDateStream =
                stashIssuesDStream.filter(new Function<StashMetricIssue, Boolean>() {
                    @Override
                    public Boolean call(StashMetricIssue stashMetricIssue) throws Exception {
                        return (stashMetricIssue != null);
                    }
                }).map(new Function<StashMetricIssue, Date>() {
                    @Override
                    public Date call(final StashMetricIssue stashMetricIssue)
                            throws Exception {
                        return new Date(stashMetricIssue.getAuthorTimestamp());
                    }
                });


        stashUpdateDateStream.foreachRDD(new Function<JavaRDD<Date>, Void>() {
            @Override
            public Void call(final JavaRDD<Date> dateJavaRDD) throws Exception {
                dateJavaRDD.foreach(new VoidFunction<Date>() {
                    @Override
                    public void call(final Date date) throws Exception {
                        //System.out.println( "new rdd's!!!" );
                        if (mostRecentStashCommitDate.get() == null || mostRecentStashCommitDate.get().before(date)) {
                            mostRecentStashCommitDate.set(date);
                        }
                    }
                });
                return null;
            }
        });

        JavaDStream<StashUserMetricInfo.StashUserMetricInfoInternal> metricsDStream = stashUserMonthlyProductivityMetrics.filter(
                new Function<Tuple2<Tuple3<String, Integer, Integer>, Tuple3<Integer, Integer, Integer>>, Boolean>()

                {
                    @Override
                    public Boolean call(
                            final Tuple2<Tuple3<String, Integer, Integer>, Tuple3<Integer, Integer, Integer>> tuple3Tuple3Tuple2)
                            throws Exception {

                        Integer thisMonth = tuple3Tuple3Tuple2._1()._2();
                        Integer thisYear = tuple3Tuple3Tuple2._1()._3();

                        if (mostRecentStashCommitDate.get() != null) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(mostRecentStashCommitDate.get());

                            if (cal.get(Calendar.YEAR) > thisYear || (cal.get(Calendar.YEAR) == thisYear && cal.get(Calendar.MONTH) > thisMonth)) {
                                //System.out.println("Filtering out these tuples for database! ");
                                return true;
                            }
                        }

                        return false;
                    }
                }).map(
                new Function<Tuple2<Tuple3<String, Integer, Integer>, Tuple3<Integer, Integer, Integer>>, StashUserMetricInfo.StashUserMetricInfoInternal>()

                {
                    @Override
                    public StashUserMetricInfo.StashUserMetricInfoInternal call(
                            final Tuple2<Tuple3<String, Integer, Integer>, Tuple3<Integer, Integer, Integer>> tuple3Tuple3Tuple2)
                            throws Exception {

                        Calendar cal = Calendar.getInstance();
                        cal.clear();
                        cal.set(Calendar.YEAR, tuple3Tuple3Tuple2._1()._3());
                        cal.set(Calendar.MONTH, tuple3Tuple3Tuple2._1()._2());

                        StashUserMetricInfo.StashUserMetricInfoInternal metricInfo = new StashUserMetricInfo.StashUserMetricInfoInternal();
                        metricInfo.setDeveloperId(tuple3Tuple3Tuple2._1()._1());
                        metricInfo.setStashCommitCnt(tuple3Tuple3Tuple2._2()._1());
                        metricInfo.setMetricMonthDate(cal.getTime());

                        System.out.println("STASH METRICS STREAM: " + metricInfo.getDeveloperId() + " " + metricInfo
                                .getStashCommitCnt() + " " + metricInfo.getMetricMonthDate());
                        return metricInfo;
                    }
                });

        metricsDStream.print();
        javaFunctions(metricsDStream).writerBuilder( keyspaceName, stashMetricTlbName, mapToRow(StashUserMetricInfo.StashUserMetricInfoInternal.class) ).saveToCassandra();
    }

    public static void startStashStreaming(JavaStreamingContext jssc, HashSet<String> topicsSet, HashMap<String,
            String> kafkaParams)
    {
        JavaPairInputDStream<String, StashMetricIssue> stashLogDStream = KafkaUtils.createDirectStream(jssc,
                String.class, StashMetricIssue.class, StringDecoder.class, StashMetricIssueKafkaSerializer.class,
                kafkaParams, topicsSet);

        stashLogDStream.print();

        JavaDStream<StashMetricIssue> stashIssuesDStream = stashLogDStream.map(new Function<Tuple2<String, StashMetricIssue>, StashMetricIssue>() {
            @Override
            public StashMetricIssue call(Tuple2<String, StashMetricIssue> stringStashMetricIssueTuple2) throws Exception {
                return stringStashMetricIssueTuple2._2();
            }
        });

        computeStashProductivityMetric(stashIssuesDStream);
    }


}
