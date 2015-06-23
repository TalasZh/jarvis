package org.safehaus.upsource.client;


import java.util.Map;
import java.util.Set;

import org.safehaus.upsource.model.Project;
import org.safehaus.util.JsonUtil;
import org.safehaus.util.RestUtil;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;


public class UpsourceManagerImpl implements UpsourceManager
{

    private final String baseUrl;

    protected RestUtil restUtil;

    protected JsonUtil jsonUtil = new JsonUtil();


    public UpsourceManagerImpl( final String baseUrl, final String username, final String password )
    {
        Preconditions.checkArgument( !Strings.isNullOrEmpty( baseUrl ) );
        Preconditions.checkArgument( !Strings.isNullOrEmpty( username ) );
        Preconditions.checkArgument( !Strings.isNullOrEmpty( password ) );

        this.baseUrl = baseUrl;
        this.restUtil = new RestUtil( username, password );
    }


    protected JsonElement get( String apiPath, Map<String, String> urlParams, String elementName )
            throws RestUtil.RestException
    {
        String response = restUtil.get( String.format( "%s/~rpc/%s", baseUrl, apiPath ), urlParams );
        JsonObject result = ( JsonObject ) jsonUtil.from( response, JsonObject.class ).get( "result" );

        return result.get( elementName );
    }


    @Override
    public Set<Project> getAllProjects() throws UpsourceManagerException
    {
        try
        {
            return jsonUtil.from( get( "getAllProjects", null, "project" ).toString(), new TypeToken<Set<Project>>()
            {}.getType() );
        }
        catch ( Exception e )
        {
            throw new UpsourceManagerException( e );
        }
    }
}
