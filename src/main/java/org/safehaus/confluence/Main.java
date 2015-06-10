package org.safehaus.confluence;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import org.safehaus.confluence.helpers.ApiHelper;
import org.safehaus.confluence.helpers.JsonHelper;
import org.safehaus.confluence.models.Content;
import org.safehaus.confluence.models.Results;


/**
 * Created by root on 5/5/15.
 */
public class Main
{
    public static void main( String[] args ) throws IOException, URISyntaxException
    {
        System.out.println( "Let's see some examples" );

        //String res = ApiHelper.updatePage(1376259, "5th updated title", "TES", 5, false, "<p>Updated via API</p>");
        //String res = ApiHelper.createSubPage("Sub Title", "TES", "<p>Test new sub</p>", "1376259");
        //String res = ApiHelper.updateSubPage(1376262, "3rd time updated Sub page", "TES", 3, true, "<p>3 Updated
        // space via API</p>", 1376259);

        //String res = ApiHelper.queryPageContent(1376259);

        String res = ApiHelper.addCommentToPage( "admin:admin", 1376259, "<p>New comment by API</p>" );
        System.out.println( res );
/*
        //To query spaces
        String spacesResponseJson = ApiHelper.querySpaces();
        Results results = JsonHelper.parseResults(spacesResponseJson);
        List<Space> spaces = results.fields.getArrayAsSpaces();
        for(Iterator<Space> i = spaces.iterator(); i.hasNext();) {
            Space space = i.next();
            System.out.println(space.fields.getKey());
            System.out.println(space.fields.getName());
            System.out.println();
        }
*/
        //To query pages
        String contentsResponseJson = ApiHelper.queryContent( "admin:admin" );
        System.out.println( contentsResponseJson );
        Results contentResults = JsonHelper.parseResults( contentsResponseJson );
        List<Content> contents = contentResults.fields.getArrayAsContents();
        System.out.println( contentResults.fields.getNextUrl() );
        for ( Iterator<Content> i = contents.iterator(); i.hasNext(); )
        {
            System.out.println( i.next().fields.getBody() );
        }
    }
}
