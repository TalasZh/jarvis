package org.safehaus.confluence.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by root on 5/6/15.
 */
public class Space {
    public Space(JsonObject jsonObject){
        this.jsonObject = jsonObject;
    }

    public final Fields fields = new Fields();
    JsonObject jsonObject;

    public class Fields{
        String id = "id";
        String key = "key";
        String name = "name";
        String type = "type";
        String description = "description";
        String homepageUrl = "homepage";

        public int getId(){
            return getFieldByName(id).getAsInt();
        }

        public String getKey(){
            return getFieldByName(key).getAsString();
        }

        public String getName(){
            return getFieldByName(name).getAsString();
        }

        public String getType(){
            return getFieldByName(type).getAsString();
        }

        public String getDescription(){
            return getFieldByName(description).getAsString();
        }

        public String getHomepageUrl(){
            return getFieldByName(homepageUrl).getAsString();
        }

        public JsonElement getFieldByName(String fieldName){
            return jsonObject.get(fieldName);
        }

        public Fields(){

        }
    }
}
