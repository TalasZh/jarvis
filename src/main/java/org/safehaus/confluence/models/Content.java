package org.safehaus.confluence.models;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * Created by root on 5/6/15.
 */
public class Content
{
    public Content( JsonObject jsonObject )
    {
        this.jsonObject = jsonObject;
    }


    public Content( String content )
    {
        this.jsonObject = new JsonParser().parse( content ).getAsJsonObject();
    }


    public final Fields fields = new Fields();
    JsonObject jsonObject;


    public class Fields
    {
        String id = "id";
        String type = "type";
        String status = "status";
        String title = "title";
        String ancestors = "ancestors";
        String version = "version";
        String body = "body.storage.value";


        public JsonElement getField( String fieldName )
        {
            return jsonObject.get( fieldName );
        }


        public Integer getId()
        {
            return getField( id ) == null ? null : getField( id ).getAsInt();
        }


        public int getAncestorId()
        {
            return getField( ancestors ).getAsJsonObject().get( "id" ).getAsInt();
        }


        public int getVersion()
        {
            return getField( version ).getAsJsonObject().get( "number" ).getAsInt();
        }


        public String getType()
        {
            return getField( type ).getAsString();
        }


        public String getWebui()
        {
            return getField( "_links" ).getAsJsonObject().get( "webui" ).getAsString();
        }


        public String getStatus()
        {
            return getField( status ).getAsString();
        }


        public String getTitle()
        {
            return getField( title ).getAsString();
        }


        //TODO: use recursion instead
        public String getBody()
        {
            return getField( "body" ).getAsJsonObject().get( "storage" ).getAsJsonObject().get( "value" ).getAsString();
        }


        public Fields()
        {

        }
    }
}
