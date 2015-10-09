package org.safehaus.exceptions;


/**
 * Created by talas on 10/8/15.
 */
public class InvalidIssueKeyException extends Exception
{
    /**
     * Construct a new invalid issue key exception.
     *
     * @param issueKey The invalid issue key value.
     * @param cause The cause.
     */
    public InvalidIssueKeyException( String issueKey, Throwable cause )
    {
        super( "Invalid issue key format '" + issueKey + "'", cause );
    }


    /**
     * Construct a new invalid issue key exception.
     *
     * @param issueKey The invalid issue key value.
     */
    public InvalidIssueKeyException( String issueKey )
    {
        this( issueKey, null );
    }
}
