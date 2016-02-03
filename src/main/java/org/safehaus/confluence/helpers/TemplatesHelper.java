package org.safehaus.confluence.helpers;


import java.io.IOException;


/**
 * Created by root on 5/8/15.
 */
public class TemplatesHelper
{


    public static String pageHelper( String title, String spaceKey, String content ) throws IOException
    {
        String templateContent = new FilesHelper().ReadFile( "templates/contentTemplate" );
        String result = templateContent.replace( "[TITLE]", title ).replace( "[SPACE_KEY]", spaceKey )
                                       .replace( "[BODY_STORAGE_VALUE]", content );
        return result;
    }


    public static String subPageHelper( String title, String spaceKey, String content, String ancestorID )
            throws IOException
    {
        String templateContent = new FilesHelper().ReadFile( "templates/subPageTemplate" );
        String result = templateContent.replace( "[TITLE]", title ).replace( "[SPACE_KEY]", spaceKey )
                                       .replace( "[BODY_STORAGE_VALUE]", content )
                                       .replace( "[ANCESTOR_ID]", ancestorID );
        return result;
    }


    public static String updatePageHelper( int pageID, String pageTitle, String spaceKey, int version, boolean isMinor,
                                           String content ) throws IOException
    {
        String templateContent = new FilesHelper().ReadFile( String.format( "templates/updatePageTemplate" ) );
        String result =
                templateContent.replace( "[PAGE_ID]", Integer.toString( pageID ) ).replace( "[PAGE_TITLE]", pageTitle )
                               .replace( "[SPACE_KEY]", spaceKey ).replace( "[VERSION]", Integer.toString( version ) )
                               .replace( "[IS_MINOR]", Boolean.toString( isMinor ) )
                               .replace( "[BODY_STORAGE_VALUE]", content );
        return result;
    }


    public static String updateSubPageHelper( int pageID, String pageTitle, String spaceKey, int version,
                                              boolean isMinor, String content, int ancestorID ) throws IOException
    {
        String templateContent = new FilesHelper().ReadFile( String.format( "templates/updateSubPageTemplate" ) );
        String result =
                templateContent.replace( "[PAGE_ID]", Integer.toString( pageID ) ).replace( "[PAGE_TITLE]", pageTitle )
                               .replace( "[SPACE_KEY]", spaceKey ).replace( "[VERSION]", Integer.toString( version ) )
                               .replace( "[IS_MINOR]", Boolean.toString( isMinor ) )
                               .replace( "[BODY_STORAGE_VALUE]", content )
                               .replace( "[ANCESTOR_ID]", Integer.toString( ancestorID ) );
        return result;
    }


    public static String addCommentHelper( int pageID, String commentBody ) throws IOException
    {
        String templateContent = new FilesHelper().ReadFile( String.format( "templates/commentTemplate" ) );
        String result = templateContent.replace( "[PARENT_ID]", Integer.toString( pageID ) )
                                       .replace( "[COMMENT_BODY]", commentBody );
        return result;
    }
}
