package org.safehaus.analysis;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import java.util.Properties;

/**
 * Created by neslihan on 22.07.2015.
 */
public class JiraMetricIssueProducer {
    private Producer<String, JiraMetricIssue> producer;
    private static String topic = "jirametricissue";

    public JiraMetricIssueProducer()
    {
        Properties props = new Properties();
        props.put("zk.connect", "127.0.0.1:2181");
        props.put("serializer.class", "org.safehaus.analysis.JiraMetricIssueKafkaSerializer");
        props.put("metadata.broker.list", "localhost:9092");
        props.put("producer.type", "sync");
        props.put("request.required.acks", "1");
        ProducerConfig config = new ProducerConfig(props);
        producer = new Producer<String, JiraMetricIssue>(config);

    }

    public void send(JiraMetricIssue jiraIssue)
    {
        producer.send(new KeyedMessage<String, JiraMetricIssue>(topic, jiraIssue));
    }

    public void close()
    {
        producer.close();
    }
}
