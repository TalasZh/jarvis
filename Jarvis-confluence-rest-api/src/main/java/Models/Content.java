package Models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by root on 5/6/15.
 */
public class Content {
    public Content(JsonObject jsonObject){
        this.jsonObject = jsonObject;
    }

    public final Fields fields = new Fields();
    JsonObject jsonObject;

    public class Fields{
        String id = "id";
        String type = "type";
        String status = "status";
        String title = "title";
        String body = "body.storage.value";

        public JsonElement getField(String fieldName){
            return jsonObject.get(fieldName);
        }

        public int getId(){
            return getField(id).getAsInt();
        }

        public String getType(){
            return getField(type).getAsString();
        }

        public String getStatus(){
            return getField(status).getAsString();
        }

        public String getTitle(){
            return getField(title).getAsString();
        }

        //TODO: use recursion instead
        public String getBody(){
            return getField("body").getAsJsonObject().get("storage").getAsJsonObject().get("value").getAsString();
        }

        public Fields(){

        }
    }
}
