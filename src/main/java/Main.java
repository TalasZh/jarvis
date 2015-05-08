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

        //To create new page
        String res = ApiHelper.postContent(TemplatesHelper.pageHelper(
                "TESTPAGE",
                "~bnuriddin",
                "Some test text right here"
        ));
        System.out.print(res);

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

        //To query pages
        String contentsResponseJson = ApiHelper.queryContent();
        Results contentResults = JsonHelper.parseResults(contentsResponseJson);
        List<Content> contents = contentResults.fields.getArrayAsContents();
        System.out.println(contentResults.fields.getNextUrl());
        for(Iterator<Content> i = contents.iterator(); i.hasNext();){
            System.out.println(i.next().fields.getBody());
        }

    }
}
