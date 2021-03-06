package Processors;

import Models.Content;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by root on 5/8/15.
 */
public class ContentProcessor {
    public static List<Content> processJsonArray(JsonArray jsonArray){
        List<Content> result = new ArrayList<Content>();
        for(Iterator<JsonElement> i = jsonArray.iterator(); i.hasNext();){
            Content content = new Content(i.next().getAsJsonObject());
            result.add(content);
        }

        return result;
    }
}
