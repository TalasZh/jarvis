package org.safehaus.exceptions;


/**
 * Created by tzhamakeev on 5/5/15.
 */
public class JiraClientException extends Throwable
{
    public JiraClientException( final String message )
    {
        super( message );
    }


    public JiraClientException( final String s, final Exception e )
    {
        super( s, e );
    }
}
