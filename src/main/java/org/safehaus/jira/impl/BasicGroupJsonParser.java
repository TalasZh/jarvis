package org.safehaus.jira.impl;


import java.net.URI;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;
import com.atlassian.jira.rest.client.internal.json.JsonParseUtil;


/**
 * Created by tzhamakeev on 5/20/15.
 */
public class BasicGroupJsonParser implements JsonObjectParser<BasicGroup>
{
    @Override
    public BasicGroup parse( JSONObject json ) throws JSONException
    {
        final URI selfUri = JsonParseUtil.getSelfUri( json );
        final Long id = JsonParseUtil.getOptionalLong( json, "id" );
        final String name = JsonParseUtil.getOptionalString( json, "name" );
        return new BasicGroup( selfUri, id, name );
    }
}
