package org.safehaus.jira.impl;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.atlassian.jira.rest.client.api.OptionalIterable;
import com.atlassian.jira.rest.client.api.domain.BasicComponent;
import com.atlassian.jira.rest.client.api.domain.BasicProjectRole;
import com.atlassian.jira.rest.client.api.domain.BasicUser;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.User;
import com.atlassian.jira.rest.client.api.domain.Version;
import com.atlassian.jira.rest.client.internal.json.BasicComponentJsonParser;
import com.atlassian.jira.rest.client.internal.json.BasicProjectRoleJsonParser;
import com.atlassian.jira.rest.client.internal.json.IssueTypeJsonParser;
import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;
import com.atlassian.jira.rest.client.internal.json.JsonParseUtil;
import com.atlassian.jira.rest.client.internal.json.JsonParser;
import com.atlassian.jira.rest.client.internal.json.UserJsonParser;
import com.atlassian.jira.rest.client.internal.json.VersionJsonParser;
import com.atlassian.util.concurrent.Nullable;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;


/**
 * Created by tzhamakeev on 5/20/15.
 */
public class GroupJsonParser implements JsonObjectParser<Group>
{

    private final VersionJsonParser versionJsonParser = new VersionJsonParser();
    private final BasicComponentJsonParser componentJsonParser = new BasicComponentJsonParser();
    private final IssueTypeJsonParser issueTypeJsonParser = new IssueTypeJsonParser();
    private final BasicProjectRoleJsonParser basicProjectRoleJsonParser = new BasicProjectRoleJsonParser();
    private final UserJsonParser userJsonParser = new UserJsonParser();


    static Iterable<String> parseExpandos( final JSONObject json ) throws JSONException
    {
        if ( json.has( "expand" ) )
        {
            final String expando = json.getString( "expand" );
            return Splitter.on( ',' ).split( expando );
        }
        else
        {
            return Collections.emptyList();
        }
    }


    @Override
    public Group parse( JSONObject json ) throws JSONException
    {
        System.out.println( "******************>" + json );
        URI self = JsonParseUtil.getSelfUri( json );
        final Iterable<String> expandos = parseExpandos( json );
        //        final BasicUser lead = JsonParseUtil.parseBasicUser( json.getJSONObject( "lead" ) );
        final Long id = JsonParseUtil.getOptionalLong( json, "id" );
        final String name = JsonParseUtil.getOptionalString( json, "name" );
        final String urlStr = JsonParseUtil.getOptionalString( json, "url" );
        URI uri;
        try
        {
            uri = urlStr == null || "".equals( urlStr ) ? null : new URI( urlStr );
        }
        catch ( URISyntaxException e )
        {
            uri = null;
        }
        //        String description = JsonParseUtil.getOptionalString( json, "description" );
        //        if ( "".equals( description ) )
        //        {
        //            description = null;
        //        }
        //        final Collection<Version> versions =
        //                JsonParseUtil.parseJsonArray( json.getJSONArray( "versions" ), versionJsonParser );
        //        //        JSONArray a = JsonParseUtil.getOptionalArray( json, "users" );
        //        final Collection<User> users = JsonParseUtil.parseOptionalJsonArray(
        //                parseOptionalArray( new JsonWeakParserForJsonObject<UserJsonParser>( userJsonParser ) ),
        // "users" );

        JSONObject usersObject = json.getJSONObject( "users" );
        final JSONArray usersArray = usersObject.optJSONArray( "items" );
//        System.out.println( "===================> users json array:" + usersArray );

        final OptionalIterable<User> users = JsonParseUtil.parseOptionalJsonArray( usersArray, userJsonParser );

//        System.out.println( "===================> users array:" + users );
        //        final Collection<BasicProjectRole> projectRoles =
        //                basicProjectRoleJsonParser.parse( JsonParseUtil.getOptionalJsonObject( json, "roles" ) );
        return new Group( expandos, self, id, name, Lists.newArrayList( users ) );
    }
}


