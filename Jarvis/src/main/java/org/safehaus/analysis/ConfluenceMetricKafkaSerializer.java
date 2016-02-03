package org.safehaus.analysis;

import kafka.serializer.Decoder;
import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;
import org.safehaus.confluence.model.ConfluenceMetric;

import java.io.*;

/**
 * Created by neslihan on 27.08.2015.
 */
public class ConfluenceMetricKafkaSerializer implements Encoder<ConfluenceMetric>, Decoder<ConfluenceMetric> {

    public ConfluenceMetricKafkaSerializer(VerifiableProperties props) {}

    @Override
    public ConfluenceMetric fromBytes(byte[] bytes) {
        ConfluenceMetric obj = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;

        try
        {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            obj = (ConfluenceMetric) ois.readObject();
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

    @Override
    public byte[] toBytes(ConfluenceMetric confluenceMetricObj) {
        byte[] bytes = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;

        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(confluenceMetricObj);
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
}
