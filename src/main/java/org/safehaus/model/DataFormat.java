package org.safehaus.model;


public enum DataFormat
{
    XLS, XLSX, PDF, DOC, DOCX, AVI;


    /**
     * If provided protocol is within allowed protocol.
     *
     * @param format format
     *
     * @return true, if it is in allowed protocol.
     */
    public static boolean isValidFormat( String format )
    {
        try
        {
            DataFormat.valueOf( format.toUpperCase() );
            return true;
        }
        catch ( IllegalArgumentException iex )
        {
            return false;
        }
    }
}
