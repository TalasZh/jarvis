package org.safehaus.analysis;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.safehaus.dao.entities.jira.JiraMetricIssue;

import kafka.serializer.Decoder;
import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;


/**
 * Created by neslihan on 21.07.2015.
 */
public class JiraMetricIssueKafkaSerializer implements Encoder<JiraMetricIssue>, Decoder<JiraMetricIssue>{

    public JiraMetricIssueKafkaSerializer(VerifiableProperties props) {

    }

    public byte[] toBytes(JiraMetricIssue jiraObj){
        byte[] bytes = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;

        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(jiraObj);
            bytes = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }finally
        {
            try
            {
                oos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return bytes;
    }

    public JiraMetricIssue fromBytes(byte[] bytes) {
        JiraMetricIssue obj = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;

        try
        {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            obj = (JiraMetricIssue) ois.readObject();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                ois.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return obj;
    }
}
