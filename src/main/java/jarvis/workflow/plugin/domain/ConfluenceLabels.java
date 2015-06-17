package jarvis.workflow.plugin.domain;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConfluenceLabels
{
    List<Map> lables = new ArrayList<>();


    public List<Map> getLables()
    {
        return lables;
    }


    public void setLables( final List<Map> lables )
    {
        this.lables = lables;
    }

}
