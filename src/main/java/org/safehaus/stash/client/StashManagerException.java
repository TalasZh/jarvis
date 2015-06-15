package org.safehaus.stash.client;


public class StashManagerException extends Exception
{
    public StashManagerException( final Throwable cause )
    {
        super( cause );
    }


    public StashManagerException( final String message )
    {
        super( message );
    }
}
