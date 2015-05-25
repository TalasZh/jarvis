package org.safehaus.jira.impl;


import java.net.URI;
import java.util.EnumSet;

import javax.ws.rs.core.UriBuilder;

import org.safehaus.jira.api.GroupRestClient;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.internal.async.AbstractAsynchronousRestClient;
import com.atlassian.jira.rest.client.internal.json.BasicProjectsJsonParser;
import com.atlassian.util.concurrent.Promise;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;


/**
 * Created by tzhamakeev on 5/20/15.
 */
public class AsynchronousGroupRestClient extends AbstractAsynchronousRestClient implements GroupRestClient
{
    private static final EnumSet<GroupRestClient.Expandos> DEFAULT_EXPANDS = EnumSet.noneOf( Expandos.class );
    private static final Function<GroupRestClient.Expandos, String> EXPANDO_TO_PARAM =
            new Function<GroupRestClient.Expandos, String>()
            {
                @Override
                public String apply( GroupRestClient.Expandos from )
                {
                    return from.name().toLowerCase();
                }
            };
    private static final String GROUP_URI_PREFIX = "group";
    private final GroupJsonParser groupJsonParser = new GroupJsonParser();
    private final BasicProjectsJsonParser basicProjectsJsonParser = new BasicProjectsJsonParser();

    private final URI baseUri;


    public AsynchronousGroupRestClient( final URI baseUri, final HttpClient client )
    {
        super( client );
        this.baseUri = baseUri;
    }


    @Override
    public Promise<Group> getGroup( final String groupname )
    {
        URI uri = UriBuilder.fromUri( baseUri ).path( GROUP_URI_PREFIX ).queryParam( "groupname", groupname ).build();

        return getAndParse( uri, groupJsonParser );
    }


    @Override
    public Promise<Group> getGroup( final String groupname, final Iterable<Expandos> expand )
    {
        final UriBuilder uriBuilder = UriBuilder.fromUri( baseUri );
        final Iterable<GroupRestClient.Expandos> expands = Iterables.concat( DEFAULT_EXPANDS, expand );
        uriBuilder.path( GROUP_URI_PREFIX ).queryParam( "groupname", groupname )
                  .queryParam( "expand", Joiner.on( ',' ).join( Iterables.transform( expands, EXPANDO_TO_PARAM ) ) );
        System.out.println(uriBuilder.build().toString());
        return getAndParse( uriBuilder.build(), groupJsonParser );
    }

    //	@Override
    //	public Promise<Group> getGroup( final URI groupUri ) {
    //		return getAndParse(groupUri, groupJsonParser );
    //	}

    //	@Override
    //	public Promise<Iterable<BasicProject>> getAllProjects() {
    //		final URI uri = UriBuilder.fromUri(baseUri).path( GROUP_URI_PREFIX ).build();
    //		return getAndParse(uri, basicProjectsJsonParser);
    //	}
}