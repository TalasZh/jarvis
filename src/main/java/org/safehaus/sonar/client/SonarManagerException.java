package org.safehaus.sonar.client;


public class SonarManagerException extends Exception
{
    public SonarManagerException( final Throwable cause )
    {
        super( cause );
    }


    public SonarManagerException( final String message )
    {
        super( message );
    }
}
