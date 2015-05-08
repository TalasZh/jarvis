package Helpers;

import org.apache.commons.codec.binary.Base64;
import org.omg.CORBA.NameValuePair;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

//import org.apache.commons.httpclient.UsernamePasswordCredentials;

/**
 * Created by root on 5/8/15.
 */
public class ConnectionHelper {

    public static HttpURLConnection buildGetRequest(String urlString, String credentials) throws IOException {
        HttpURLConnection connection = connectBasicAuth(urlString, credentials);
        connection.setRequestMethod("GET");

        return connection;
    }

    public static HttpURLConnection buildPostRequest(String urlString, String credentials, String content /*List<NameValuePair> params*/) throws IOException {
        HttpURLConnection connection = connectBasicAuth(urlString, credentials);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-type", "application/json");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        //writer.write(getQuery(params));
        writer.write(content);
        writer.flush();
        writer.close();
        os.close();
        connection.connect();
        return connection;
    }

    private static HttpURLConnection connectBasicAuth(String urlString, String credentials) throws IOException {
        URL url = new URL(urlString);
        String encoding = new String(Base64.encodeBase64(credentials.getBytes()));

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Basic " + encoding);

        return connection;
    }

    public static String getResponse(HttpURLConnection connection) throws IOException {
        String result = "";

        InputStream content = connection.getInputStream();
        BufferedReader in =
                new BufferedReader(new InputStreamReader(content));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }

        return result;
    }

    private static String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.id, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.value.extract_string(), "UTF-8"));
        }

        return result.toString();
    }
}
