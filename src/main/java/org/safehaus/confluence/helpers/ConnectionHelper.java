package org.safehaus.confluence.helpers;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.omg.CORBA.NameValuePair;

import org.apache.commons.codec.binary.Base64;

//import org.apache.commons.httpclient.UsernamePasswordCredentials;


/**
 * Created by root on 5/8/15.
 */
public class ConnectionHelper
{

    public static HttpURLConnection buildGetRequest( String urlString, String credentials/*, String remoteAddress */) throws IOException
    {
        HttpURLConnection connection = connect( urlString, credentials );
        connection.setRequestMethod( "GET" );
//        connection.setRequestProperty( "X-Forwarded-For", remoteAddress );
//        connection.setRequestProperty( "Accept", "application/json" );
        connection.connect();
        return connection;
    }


    public static HttpURLConnection buildPostRequest( String urlString, String credentials, String content
    /*List<NameValuePair> params*/ ) throws IOException
    {
        HttpURLConnection connection = connect( urlString, credentials );
        connection.setRequestMethod( "POST" );
        connection.setRequestProperty( "Content-type", "application/json" );
        connection.setRequestProperty( "Accept", "application/json" );
        connection.setDoInput( true );
        connection.setDoOutput( true );

        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( os, "UTF-8" ) );
        //writer.write(getQuery(params));
        writer.write( content );
        writer.flush();
        writer.close();
        os.close();
        connection.connect();
        return connection;
    }


    public static HttpURLConnection buildPutRequest( String urlString, String credentials, String content
    /*List<NameValuePair> params*/ ) throws IOException
    {
        HttpURLConnection connection = connect( urlString, credentials );
        connection.setRequestMethod( "PUT" );
        connection.setRequestProperty( "Content-type", "application/json" );
        connection.setRequestProperty( "Accept", "application/json" );
        connection.setDoInput( true );
        connection.setDoOutput( true );

        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( os, "UTF-8" ) );
        //writer.write(getQuery(params));
        writer.write( content );
        writer.flush();
        writer.close();
        os.close();
        connection.connect();
        return connection;
    }


    private static HttpURLConnection connect( String urlString, String credentials ) throws IOException
    {
        URL url = new URL( urlString );
        HttpURLConnection connection = ( HttpURLConnection ) url.openConnection();

        if ( credentials.contains( ":" ) )
        {
            String encoding = new String( Base64.encodeBase64( credentials.getBytes() ) );
            connection.setRequestProperty( "Authorization", "Basic " + encoding );
        }
        else
        {
            connection.setRequestProperty( "Cookie", credentials );
        }

        connection.setDoOutput( true );

        return connection;
    }


    public static String getResponse( HttpURLConnection connection ) throws IOException
    {
        String result = "";

        InputStream content = connection.getInputStream();
        BufferedReader in = new BufferedReader( new InputStreamReader( content ) );
        String line;
        while ( ( line = in.readLine() ) != null )
        {
            result += line;
        }

        return result;
    }


    private static String getQuery( List<NameValuePair> params ) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for ( NameValuePair pair : params )
        {
            if ( first )
            {
                first = false;
            }
            else
            {
                result.append( "&" );
            }

            result.append( URLEncoder.encode( pair.id, "UTF-8" ) );
            result.append( "=" );
            result.append( URLEncoder.encode( pair.value.extract_string(), "UTF-8" ) );
        }

        return result.toString();
    }
}
