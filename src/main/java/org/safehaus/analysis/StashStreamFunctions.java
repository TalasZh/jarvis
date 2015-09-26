package org.safehaus.analysis;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.safehaus.dao.entities.stash.StashMetricIssue;
import org.safehaus.dao.entities.stash.StashUserCollaborationMetricInfo.StashUserCollaborationMetricInfoInternal;
import org.safehaus.dao.entities.stash.StashUserMetricInfo.StashUserMetricInfoInternal;

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
public class StashStreamFunctions {
    private static String keyspaceName = new String("jarvis");
    private static String stashMetricTlbName = new String("user_stash_metric_info");
    private static String stashCollabMetricTlbName = new String("user_collaboration_metric_info");
    private static AtomicReference<Date> mostRecentStashCommitDate = new AtomicReference<>();

    public static Function2<List<Tuple3<Integer, Integer,String>>, Optional<HashSet<String>>, Optional<HashSet<String>>> STASH_COLLABORATION_PROJECTS_COUNT =
    new Function2<List<Tuple3<Integer, Integer,String>>, Optional<HashSet<String>>, Optional<HashSet<String>>>() {
        public Optional<HashSet<String>> call(List<Tuple3<Integer, Integer,String>> values, Optional<HashSet<String>> state) throws Exception {
            Integer thisMonth = -1;
            Integer thisYear = -1;

            if (values == null || values.isEmpty()) {
                return state;
            } else{
                Tuple3<Integer, Integer,String> entry = values.get(0);
                thisMonth = entry._1();
                thisYear = entry._2();
            }

            HashSet<String> projectNames = new HashSet<>();
            if(state.isPresent()){
                projectNames = state.get();
            }


            for (Tuple3<Integer, Integer,String> value : values) {
                projectNames.add(value._3());
            }

            if(mostRecentStashCommitDate.get() != null)
            {
                Calendar cal = Calendar.getInstance();
                cal.setTime( mostRecentStashCommitDate.get() );

                if( cal.get( Calendar.YEAR ) > thisYear || (cal.get(Calendar.YEAR) == thisYear && cal.get(Calendar.MONTH) > thisMonth))
                {
                    System.out.println("Returning absent! Project Names: "+thisMonth+"-"+thisYear);
                    for(String name : projectNames){
                        System.out.print(name);
                    }
                    System.out.println();
                    return Optional.absent();
                }
            }

            return Optional.of(projectNames);
        }
    };

    public static Function2<List<Tuple3<Integer, Integer,Integer>>, com.google.common.base.Optional<Tuple3<Integer, Integer,Integer>>,com.google.common.base.Optional<Tuple3<Integer, Integer,Integer>>> STASH_COMMIT_RUNNING_SUM =
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

    public static void computeStashCollabMetric(final JavaDStream<StashMetricIssue> stashIssuesDStream)
    {
        //author,month,year - month,year,numofDifferentProjects
        JavaPairDStream<Tuple3<String, Integer, Integer>, HashSet<String>> stashUserMonthlyCollaborationMetrics = stashIssuesDStream.filter(new Function<StashMetricIssue, Boolean>() {
            @Override
            public Boolean call(StashMetricIssue stashMetricIssue) throws Exception {
                return (stashMetricIssue!= null && stashMetricIssue.getAuthor() != null && stashMetricIssue.getAuthor().getName() != null && stashMetricIssue.getProjectName() != null);
            }
        }).mapToPair(new PairFunction<StashMetricIssue, Tuple3<String, Integer, Integer>, Tuple3<Integer, Integer, String>>() {
            @Override
            public Tuple2<Tuple3<String, Integer, Integer>, Tuple3<Integer, Integer, String>> call(StashMetricIssue stashMetricIssue) throws Exception {
                Date date = new Date(stashMetricIssue.getAuthorTimestamp());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                Integer month = cal.get(Calendar.MONTH);
                Integer year = cal.get(Calendar.YEAR);
                String projectName = stashMetricIssue.getProjectName();
                String author = stashMetricIssue.getAuthor().getName();

                return new Tuple2<Tuple3<String, Integer, Integer>, Tuple3<Integer, Integer, String>>(new Tuple3<String, Integer, Integer>(author, month, year),
                        new Tuple3<Integer, Integer, String>(month, year, projectName));

            }
        }).updateStateByKey(STASH_COLLABORATION_PROJECTS_COUNT);

        stashUserMonthlyCollaborationMetrics.print();

        JavaDStream<StashUserCollaborationMetricInfoInternal> collabMetricsDStream = stashUserMonthlyCollaborationMetrics.filter(new Function<Tuple2<Tuple3<String, Integer, Integer>, HashSet<String>>, Boolean>() {
            @Override
            public Boolean call(Tuple2<Tuple3<String, Integer, Integer>, HashSet<String>> tuple3HashSetTuple2) throws Exception {
                Integer thisMonth = tuple3HashSetTuple2._1()._2();
                Integer thisYear = tuple3HashSetTuple2._1()._3();

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
        }).map(new Function<Tuple2<Tuple3<String, Integer, Integer>, HashSet<String>>, StashUserCollaborationMetricInfoInternal>() {
            @Override
            public StashUserCollaborationMetricInfoInternal call(Tuple2<Tuple3<String, Integer, Integer>, HashSet<String>> tuple3HashSetTuple2) throws Exception {

                Calendar cal = Calendar.getInstance();
                cal.clear();
                cal.set(Calendar.YEAR, tuple3HashSetTuple2._1()._3());
                cal.set(Calendar.MONTH, tuple3HashSetTuple2._1()._2());

                StashUserCollaborationMetricInfoInternal collabMetricInfo = new StashUserCollaborationMetricInfoInternal();
                collabMetricInfo.setDeveloperId(tuple3HashSetTuple2._1()._1());
                collabMetricInfo.setStashCollaborationCount(tuple3HashSetTuple2._2().size());
                collabMetricInfo.setMetricMonthTimestamp(cal.getTimeInMillis());

                System.out.println("STASH COLLABORATION STREAM: " + collabMetricInfo.getDeveloperId() + " " + collabMetricInfo
                        .getStashCollaborationCount() + " " + collabMetricInfo.getMetricMonthTimestamp());
                return collabMetricInfo;

            }
        });

        collabMetricsDStream.print();
        javaFunctions(collabMetricsDStream).writerBuilder( keyspaceName, stashCollabMetricTlbName, mapToRow(StashUserCollaborationMetricInfoInternal.class) ).saveToCassandra();
    }

    public static void computeStashProductivityMetric(final JavaDStream<StashMetricIssue> stashIssuesDStream)
    {
        //Compute multiple projects collaboration metric
        computeStashCollabMetric(stashIssuesDStream);

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
                String author = stashMetricIssue.getAuthor().getName();

                System.out.println(stashMetricIssue.getAuthor().getName() + " - " + stashMetricIssue.getAuthor().getDisplayName() + " - "
                        + stashMetricIssue.getAuthor().getEmailAddress() + stashMetricIssue.getAuthor().getId() + "\n\n\n");

                return new Tuple2<Tuple3<String, Integer, Integer>, Tuple3<Integer, Integer, Integer>>(new Tuple3<String, Integer, Integer>(author, month, year),
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

        JavaDStream<StashUserMetricInfoInternal> metricsDStream = stashUserMonthlyProductivityMetrics.filter(
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
                new Function<Tuple2<Tuple3<String, Integer, Integer>, Tuple3<Integer, Integer, Integer>>, StashUserMetricInfoInternal>()

                {
                    @Override
                    public StashUserMetricInfoInternal call(
                            final Tuple2<Tuple3<String, Integer, Integer>, Tuple3<Integer, Integer, Integer>> tuple3Tuple3Tuple2)
                            throws Exception {

                        Calendar cal = Calendar.getInstance();
                        cal.clear();
                        cal.set(Calendar.YEAR, tuple3Tuple3Tuple2._1()._3());
                        cal.set(Calendar.MONTH, tuple3Tuple3Tuple2._1()._2());

                        StashUserMetricInfoInternal metricInfo = new StashUserMetricInfoInternal();
                        metricInfo.setDeveloperId(tuple3Tuple3Tuple2._1()._1());
                        metricInfo.setStashCommitCnt(tuple3Tuple3Tuple2._2()._1());
                        metricInfo.setMetricMonthTimestamp(cal.getTimeInMillis());

                        System.out.println("STASH METRICS STREAM: " + metricInfo.getDeveloperId() + " " + metricInfo
                                .getStashCommitCnt() + " " + metricInfo.getMetricMonthTimestamp());
                        return metricInfo;
                    }
                });

        metricsDStream.print();
        javaFunctions(metricsDStream).writerBuilder( keyspaceName, stashMetricTlbName, mapToRow(StashUserMetricInfoInternal.class) ).saveToCassandra();
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
