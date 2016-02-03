package org.safehaus.confluence.helpers;

import org.safehaus.confluence.models.Results;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by won on 4/28/15.
 */

public class JsonHelper {

    public static Results parseResults(String jsonString){
        JsonObject rootObj = new JsonParser().parse(jsonString).getAsJsonObject();
        JsonElement fieldResults = rootObj.get("results");
        JsonObject resultSecData = new JsonParser().parse(jsonString.replace(fieldResults.toString(), "\"\"")).getAsJsonObject();
        Results result = new Results(
               resultSecData, fieldResults.getAsJsonArray());

        return result;
    }
}
