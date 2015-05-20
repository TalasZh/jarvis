import Helpers.ApiHelper;
import Helpers.JsonHelper;
import Helpers.TemplatesHelper;
import Models.Content;
import Models.Results;
import Models.Space;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by root on 5/5/15.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Let's see some examples");

        //String res = ApiHelper.updatePage(1376259, "5th updated title", "TES", 5, false, "<p>Updated via API</p>");
        //String res = ApiHelper.createSubPage("Sub Title", "TES", "<p>Test new sub</p>", "1376259");
        //(int pageID, String pageTitle, String spaceKey, int version, boolean isMinor, String content, String ancestorID)
        //String res = ApiHelper.updateSubPage(1376262, "3rd time updated Sub page", "TES", 3, true, "<p>3 Updated space via API</p>", 1376259);

        String res = ApiHelper.addCommentToPage(1146882,"BLa bla bla");

        System.out.println(res);

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
/*
        //To query pages
        String contentsResponseJson = ApiHelper.queryContent();
        Results contentResults = JsonHelper.parseResults(contentsResponseJson);
        List<Content> contents = contentResults.fields.getArrayAsContents();
        System.out.println(contentResults.fields.getNextUrl());
        for(Iterator<Content> i = contents.iterator(); i.hasNext();){
            System.out.println(i.next().fields.getBody());
        }
*/
    }
}
