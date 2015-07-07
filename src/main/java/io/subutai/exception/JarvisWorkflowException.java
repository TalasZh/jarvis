package io.subutai.exception;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.workflow.WorkflowException;


public class JarvisWorkflowException extends WorkflowException
{
    private List errorsList = new ArrayList<>();
    private Map errorsMap = new HashMap();


    public JarvisWorkflowException( final String string )
    {
        super( string );
        this.addError( string );
    }


    public JarvisWorkflowException( final Throwable throwable )
    {
        super( throwable );
    }


    public JarvisWorkflowException( final String string, final Throwable throwable )
    {
        super( string, throwable );
    }


    public void addError( String error )
    {
        this.errorsList.add( error );
    }


    public void addError( String name, String error )
    {
        this.errorsMap.put( name, error );
    }


    public List getErrorsList()
    {
        return errorsList;
    }


    public void setErrorsList( final List errorsList )
    {
        this.errorsList = errorsList;
    }


    public Map getErrorsMap()
    {
        return errorsMap;
    }


    public void setErrorsMap( final Map errorsMap )
    {
        this.errorsMap = errorsMap;
    }
}
