package org.safehaus.confluence.helpers;


import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.safehaus.confluence.models.Results;
import org.safehaus.confluence.models.Space;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.io.IOUtils;


/**
 * Created by won on 4/29/15.
 */
public class ApiHelper
{
    private static Logger logger = LoggerFactory.getLogger( ApiHelper.class );


    public static String postContent( String credentials, String contentBody ) throws IOException
    {
        return postJson( Session.RestApiUrl.contentUrl(), credentials, contentBody );
    }


    public static String postContent( String credentials, int ID, String contentBody ) throws IOException
    {
        return postJson( Session.RestApiUrl.contentUrl() + '/' + ID, credentials, contentBody );
    }


    public static String putContent( String credentials, int ID, String contentBody ) throws IOException
    {
        return putJson( Session.RestApiUrl.contentUrl() + '/' + ID, credentials, contentBody );
    }


    public static String createPage( String credentials, String title, String spaceKey, String content )
            throws IOException
    {
        return ApiHelper.postContent( credentials, TemplatesHelper.pageHelper( title, spaceKey, content ) );
    }


    public static String createSubPage( String credentials, String title, String spaceKey, String content,
                                        Integer ancestorID ) throws IOException
    {
        return ApiHelper.postContent( credentials,
                TemplatesHelper.subPageHelper( title, spaceKey, content, ancestorID.toString() ) );
    }


    public static String updatePage( String credentials, Integer pageID, String pageTitle, String spaceKey, int version,
                                     boolean isMinor, String content ) throws IOException
    {
        String json = TemplatesHelper.updatePageHelper( pageID, pageTitle, spaceKey, version, isMinor, content );
        logger.debug( json );
        return ApiHelper.putContent( credentials, pageID, json );
    }


    public static String updateSubPage( String credentials, int pageID, String pageTitle, String spaceKey, int version,
                                        boolean isMinor, String content, int ancestorID ) throws IOException
    {
        String json = TemplatesHelper
                .updateSubPageHelper( pageID, pageTitle, spaceKey, version, isMinor, content, ancestorID );
        logger.debug( json );
        return ApiHelper.putContent( credentials, pageID, json );
    }


    public static String addCommentToPage( String credentials, int pageID, String commentBody ) throws IOException
    {
        return ApiHelper.postContent( credentials, TemplatesHelper.addCommentHelper( pageID, commentBody ) );
    }


    public static String querySpaces( String credentails ) throws IOException
    {
        return getJson( Session.RestApiUrl.spaceUrl(), credentails );
    }


    public static String createSpace( String credentials, String contentBody ) throws IOException
    {
        return postJson( Session.RestApiUrl.spaceUrl(), credentials, contentBody );
    }


    public static String queryContent( String credentials, String... query ) throws IOException, URISyntaxException
    {
        String result = getJson( toASCIIString( String.format( "%s%s%s", Session.RestApiUrl.contentUrl(),
                        "?expand=space,body.storage,version,container,_links", query != null ? "&" + query[0] : "" ) ),
                credentials );
        logger.debug( result );
        return result;
    }


    private static String toASCIIString( String urlString ) throws URISyntaxException, MalformedURLException
    {
        URL url = new URL( urlString );
        URI uri = new URI( url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(),
                url.getQuery(), url.getRef() );

        return uri.toASCIIString();
    }


    public static String queryPageContent( String credentials, String remoteAddress, int pageID ) throws IOException
    {
        return getJson( Session.RestApiUrl.contentUrl() + "/" + pageID
                + "?expand=space,body.storage,version,container,ancestors", credentials );
    }


    public static String queryByCustomUrl( String url, String credentials/*, String remoteAddress*/ ) throws IOException
    {
        return getJson( url, credentials );
    }


    private static String getJson( String urlString, String credentials ) throws IOException
    {
        String jsonString = "";
        HttpURLConnection connection = null;
        try
        {
            logger.debug( urlString );
            connection = ConnectionHelper.buildGetRequest( urlString, credentials );
            jsonString = ConnectionHelper.getResponse( connection );
        }
        catch ( Exception e )
        {
            //
            StringWriter sw = new StringWriter();
            IOUtils.copy( connection.getErrorStream(), sw );
            logger.debug( sw.toString() );
            logger.debug( e.getMessage(), e );
        }
        return jsonString;
    }


    private static String postJson( String urlString, String credentials, String contentBody ) throws IOException
    {
        String jsonString = "";
        try
        {
            HttpURLConnection connection = ConnectionHelper.buildPostRequest( urlString, credentials, contentBody );
            jsonString = ConnectionHelper.getResponse( connection );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return jsonString;
    }


    private static String putJson( String urlString, String credentials, String contentBody ) throws IOException
    {
        String jsonString = "";
        try
        {
            HttpURLConnection connection = ConnectionHelper.buildPutRequest( urlString, credentials, contentBody );
            jsonString = ConnectionHelper.getResponse( connection );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return jsonString;
    }


    public static Space getSpace( final String credentails, final String spaceKey ) throws IOException
    {
        String jsonString = getJson( Session.RestApiUrl.spaceUrl(), credentails );

        logger.debug( String.format( "Spaces JSON:%s", jsonString ) );
        Results results = JsonHelper.parseResults( jsonString );
        logger.debug( String.format( "Spaces Results:%s", results ) );


        List<Space> spaces = results.fields.getArrayAsSpaces();

        logger.debug( String.format( "Spaces size:%d", spaces.size() ) );

        Space result = null;
        for ( Space space : spaces )
        {
            if ( space.fields.getKey().equals( spaceKey ) )
            {
                result = space;
                break;
            }
        }

        return result;
    }
}


