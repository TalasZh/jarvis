package Models;

import Helpers.ApiHelper;
import Helpers.JsonHelper;
import Processors.ContentProcessor;
import Processors.SpaceProcessor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

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

    public Results nextResults() throws IOException {
        String resultsJsonString = ApiHelper.queryByCustomUrl(fields.nextUrl);
        return JsonHelper.parseResults(resultsJsonString);

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
            return SpaceProcessor.processJsonArray(jsonArray);
        }

        public List<Content> getArrayAsContents(){
            return ContentProcessor.processJsonArray(jsonArray);
        }


        public JsonElement getFieldByName(String nameOfField){
            return jsonObject.get(nameOfField);
        }
    }

}
