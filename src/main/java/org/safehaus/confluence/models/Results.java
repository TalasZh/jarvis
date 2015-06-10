package org.safehaus.confluence.models;


import java.io.IOException;
import java.util.List;

import org.safehaus.confluence.helpers.ApiHelper;
import org.safehaus.confluence.helpers.JsonHelper;
import org.safehaus.confluence.processors.ContentProcessor;
import org.safehaus.confluence.processors.SpaceProcessor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


/**
 * Created by root on 5/6/15.
 */
public class Results {
    JsonObject jsonObject;
    JsonArray jsonArray;
    public final Fields fields = new Fields();

    public Results(JsonObject jsonObject, JsonArray jsonArray){
        this.jsonObject = jsonObject;
        this.jsonArray = jsonArray;
    }

    public Results nextResults(String credentials) throws IOException {
        String resultsJsonString = ApiHelper.queryByCustomUrl( credentials, fields.nextUrl );
        return JsonHelper.parseResults( resultsJsonString );

    }

    public class Fields {
        String start = "start";
        String limit = "limit";
        String size = "size";
        String nextUrl = "_links.next";

        public int getStart() {
            return Integer.parseInt(jsonObject.get(start).getAsString());
        }

        public int getLimit() {
            return Integer.parseInt(jsonObject.get(limit).getAsString());
        }

        public int getSize() {
            return Integer.parseInt(jsonObject.get(size).getAsString());
        }

        public String getNextUrl() {
            return jsonObject.get("_links").getAsJsonObject().get("next").getAsString();
        }


        public List<Space> getArrayAsSpaces(){
            return SpaceProcessor.processJsonArray( jsonArray );
        }

        public List<Content> getArrayAsContents(){
            return ContentProcessor.processJsonArray( jsonArray );
        }


        public JsonElement getFieldByName(String nameOfField){
            return jsonObject.get(nameOfField);
        }
    }

}
