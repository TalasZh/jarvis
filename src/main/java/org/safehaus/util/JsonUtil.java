package org.safehaus.util;


/**
 * Created by tzhamakeev on 6/1/15.
 */
public class JsonUtil
{
    /**
     * Escapes symbols in json string
     *
     * @param s - string to escape
     *
     * @return escaped json string
     */
    public static String escape( String s )
    {
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < s.length(); i++ )
        {
            char ch = s.charAt( i );
            switch ( ch )
            {
                case '"':
                    sb.append( "\"" );
                    break;
                case '\\':
                    sb.append( "\\" );
                    break;
                case '\b':
//                    sb.append( "\b" );
                    break;
                case '\f':
//                    sb.append( "\f" );
                    break;
                case '\n':
//                    sb.append( "\n" );
                    break;
                case '\r':
//                    sb.append( "\r" );
                    break;
                case '\t':
//                    sb.append( "\t" );
                    break;
//                case '/':
//                    sb.append( "\\/" );
//                    break;
                default:
                    sb.append( processDefaultCase( ch ) );
            }
        }
        return sb.toString();
    }


    private static String processDefaultCase( char ch )
    {
        StringBuilder sb = new StringBuilder();
        if ( ( ch >= '\u0000' && ch <= '\u001F' ) || ( ch >= '\u007F' && ch <= '\u009F' ) || ( ch >= '\u2000'
                && ch <= '\u20FF' ) )
        {
            String ss = Integer.toHexString( ch );
            sb.append( "\\u" );
            for ( int k = 0; k < 4 - ss.length(); k++ )
            {
                sb.append( '0' );
            }
            sb.append( ss.toUpperCase() );
        }
        else
        {
            sb.append( ch );
        }
        return sb.toString();
    }
}
