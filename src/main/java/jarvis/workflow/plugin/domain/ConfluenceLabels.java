package jarvis.workflow.plugin.domain;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;


public class ConfluenceLabels
{
    @Expose
    List<Map> labels = new ArrayList<>();


    public List<Map> getLabels()
    {
        return labels;
    }


    public void setLabels( final List<Map> labels )
    {
        this.labels = labels;
    }
}
