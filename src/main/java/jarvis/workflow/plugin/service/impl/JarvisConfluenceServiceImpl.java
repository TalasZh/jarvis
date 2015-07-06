package jarvis.workflow.plugin.service.impl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.util.json.JSONArray;
import com.atlassian.jira.util.json.JSONException;
import com.atlassian.jira.util.json.JSONObject;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jarvis.workflow.plugin.domain.ConfluenceLabels;
import jarvis.workflow.plugin.domain.ConfluencePage;
import jarvis.workflow.plugin.postfunction.JarvisConfluencePostFunctionFactory;
import jarvis.workflow.plugin.service.JarvisConfluenceService;
import jarvis.workflow.plugin.service.PluginSettingsService;


public class JarvisConfluenceServiceImpl implements JarvisConfluenceService
{
    private PluginSettingsService pluginSettingsService;
    //@formatter:off

    private final String CONFLUENCE_REST_URI          = "/rest/api";

    private final String CONFLUENCE_CONTENT_URI       = "/content";

    private final String CONFLUENCE_SPACE_URI         = "/space/";
    //@formatter:on

    CloseableHttpClient httpClient;
    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();


    private String confluenceUrl;
    private String confluenceProjectKeySpace;
    private String url;
    private static final Logger log = LoggerFactory.getLogger( JarvisConfluenceServiceImpl.class );


    public JarvisConfluenceServiceImpl( final PluginSettingsFactory pluginSettingsFactory )
    {
        this.httpClient = HttpClients.createDefault();
        this.pluginSettingsService = new PluginSettingsServiceImpl( pluginSettingsFactory );

        this.confluenceUrl = pluginSettingsService.getInfo( JarvisConfluencePostFunctionFactory.URL_FIELD );

        this.confluenceProjectKeySpace =
                pluginSettingsService.getInfo( JarvisConfluencePostFunctionFactory.PROJECT_KEY_SPACE_FIELD );
        this.url = confluenceUrl + CONFLUENCE_REST_URI + CONFLUENCE_CONTENT_URI;
    }


    @Override
    public boolean confluencePageExists( final Issue issue )
    {
        String key = issue.getKey();

        String state = issue.getStatusObject().getSimpleStatus().getName();

        String url = this.url + "/search?";
        String cql = "cql=(title=%22" + key + "+" + state.toUpperCase() + "%22+and+type=page+and+space="
                + getProjectSpaceKey() + ")";
        log.debug( "Making search request with URL {}", url );

        CloseableHttpResponse response = doGet( url + cql );

        HttpEntity entity = response.getEntity();

        try
        {
            String jsonString = EntityUtils.toString( entity );
            JSONObject jsonObject = new JSONObject( jsonString );
            String title = jsonObject.getJSONArray( "results" ).getJSONObject( 0 ).get( "title" ).toString();
            log.warn( "Comparing {} to {}", title, key + " " + state );

            if ( title.equalsIgnoreCase( key + " " + state ) )
            {
                return true;
            }
            return false;
        }
        catch ( IOException | JSONException e )
        {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public String getParentPageId( Issue issue )
    {
        String url = confluenceUrl + CONFLUENCE_REST_URI + CONFLUENCE_SPACE_URI + getProjectSpaceKey();
        log.warn( "Making GET request to URL {}", url );
        CloseableHttpResponse response = doGet( url );
        HttpEntity httpEntity;
        String data = null;
        log.warn( "Response code {}", response.getStatusLine().getStatusCode() );

        httpEntity = response.getEntity();


        try
        {
            if ( httpEntity != null )
            {
                data = EntityUtils.toString( httpEntity );
                JSONObject jsonObject = new JSONObject( data );
                String homePage = jsonObject.getJSONObject( "_expandable" ).get( "homepage" ).toString();
                EntityUtils.consume( httpEntity );
                String projectId =
                        homePage.subSequence( homePage.lastIndexOf( '/' ) + 1, homePage.length() ).toString();
                log.warn( "Project space id {}", projectId );
                return projectId;
            }
        }
        catch ( IOException | JSONException e )
        {
            e.printStackTrace();
            log.error( "Error happened during GET request to server {} with httpEntity {}", url, data );
        }

        return null;
    }


    @Override
    public String getProjectSpaceKey()
    {
        return confluenceProjectKeySpace;
    }


    @Override
    public String createConfluencePageForStory( Issue issue, final String projectKeySpace )
    {
        ConfluencePage page = buildPage( issue, getProjectSpaceKey(), getParentPageId( issue ) );

        String url = this.url;

        HttpEntity entity;

        log.warn( "POST Url {}", url );
        HttpPost post = new HttpPost( url );

        String encoding = "amFydmlzYm90OjZ2Q0Y2WWlCRjJvSA";
        post.setHeader( "Authorization", "Basic " + encoding );

        post.setHeader( "Content-Type", "application/json;charset=UTF-8" );
        CloseableHttpResponse response = null;
        String jsonString = gson.toJson( page );
        log.warn( "JSON String {}", jsonString );

        try
        {
            StringEntity stringEntity = new StringEntity( jsonString, ContentType.create( "application/json" ) );

            post.setEntity( stringEntity );

            response = httpClient.execute( post );

            log.warn( "Status code {}", response.getStatusLine().getStatusCode() );
            entity = response.getEntity();
            String data = EntityUtils.toString( entity );
            log.warn( "Response : {}", data );
            JSONObject jsonObject = new JSONObject( data );
            String id = jsonObject.getString( "id" );
            EntityUtils.consume( entity );
            return id;
        }
        catch ( IOException | JSONException e )
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                response.close();
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }

        return null;
    }


    public boolean confluencePageExists( final String id )
    {
        String url = this.url + "/" + id;

        CloseableHttpResponse response = doGet( url );

        if ( response.getStatusLine().getStatusCode() == 200 )
        {
            return true;
        }

        return false;
    }


    @Override
    public String createConfluencePage( final String phaseName, final String parentId ) throws JSONException
    {
        ConfluencePage page;
        page = buildPage( phaseName, getProjectSpaceKey(), parentId );

        CloseableHttpResponse response = null;

        log.warn( "POST Url {}", url );

        HttpPost post = new HttpPost( url );

        String encoding = "amFydmlzYm90OjZ2Q0Y2WWlCRjJvSA";
        post.setHeader( "Authorization", "Basic " + encoding );

        post.setHeader( "Content-Type", "application/json;charset=UTF-8" );

        String jsonString = gson.toJson( page );

        try
        {
            StringEntity entity = new StringEntity( jsonString, ContentType.create( "application/json" ) );

            post.setEntity( entity );

            response = httpClient.execute( post );

            log.warn( "Status code {}", response.getStatusLine().getStatusCode() );
            EntityUtils.consume( entity );
            return new JSONObject( EntityUtils.toString( response.getEntity() ) ).getString( "id" );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                response.close();
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    public int addLabel( final String contentId, final String... labels )
    {
        String url = this.url + "/" + contentId + "/label";

        HashMap<String, String> map = new HashMap<>();
        JSONArray jsonArray = null;

        for ( String label : labels )
        {
            map.put( "prefix", "global" );
            map.put( "name", label );
        }

        ConfluenceLabels confluenceLabels = new ConfluenceLabels();
        confluenceLabels.getLabels().add( map );

        String json = gson.toJson( confluenceLabels );

        try
        {
            jsonArray = new JSONObject( json ).getJSONArray( "labels" );
        }
        catch ( JSONException e )
        {
            e.printStackTrace();
        }

        HttpPost post = new HttpPost( url );
        String encoding = "amFydmlzYm90OjZ2Q0Y2WWlCRjJvSA";
        post.setHeader( "Authorization", "Basic " + encoding );
        post.setHeader( "Content-Type", "application/json;charset=UTF-8" );

        CloseableHttpResponse response = null;

        try
        {
            StringEntity entity = new StringEntity( jsonArray.toString(), ContentType.create( "application/json" ) );

            post.setEntity( entity );

            response = httpClient.execute( post );

            EntityUtils.consume( entity );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                response.close();
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }

        return response.getStatusLine().getStatusCode();
    }


    private CloseableHttpResponse doGet( String url )
    {
        log.warn( "URL {}", url );

        HttpGet get = new HttpGet( url );
        String encoding = "amFydmlzYm90OjZ2Q0Y2WWlCRjJvSA";
        get.setHeader( "Authorization", "Basic " + encoding );

        CloseableHttpResponse response = null;
        try
        {
            response = httpClient.execute( get );

            return response;
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }


        return null;
    }


    private ConfluencePage buildPage( Issue issue, String projectKeySpace, String parentId )
    {
        return buildPage( issue.getKey(), projectKeySpace, parentId );
    }


    private ConfluencePage buildPage( String title, String projectKeySpace, String parentId )
    {
        Map<String, String> space = new HashMap<String, String>();
        space.put( "key", projectKeySpace );

        List<Map> an = new ArrayList<>();

        Map<String, String> ancestors = new HashMap<String, String>();
        ancestors.put( "id", parentId );
        ancestors.put( "type", CONFLUENCE_PAGE_TYPE );
        an.add( ancestors );


        ConfluencePage page = new ConfluencePage();
        page.setType( CONFLUENCE_PAGE_TYPE );
        page.setTitle( title );
        page.setSpace( space );
        page.setAncestors( an );

        ConfluencePage.Body body = new ConfluencePage.Body();
        Map<String, String> storage = new HashMap<String, String>();
        storage.put( "value", String.format( " %s ", title ) );
        storage.put( "representation", "storage" );

        body.setStorage( storage );
        page.setBody( body );

        return page;
    }
}
