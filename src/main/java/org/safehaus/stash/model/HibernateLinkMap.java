package org.safehaus.stash.model;

import com.impetus.kundera.index.IndexCollection;

import javax.persistence.*;
import java.util.Map;

/**
 * Created by neslihan on 03.07.2015.
 */
@Entity
@Table( name = "HIBERNATE_LINK_MAP", schema = "jarvis@cassandra-pu" )
@IndexCollection( columns = {
        @com.impetus.kundera.index.Index( name = "id" )})
public class HibernateLinkMap {

    @Id
    @TableGenerator( name = "id_gen", allocationSize = 30, initialValue = 100 )
    @GeneratedValue( generator = "id_gen", strategy = GenerationType.TABLE )
    private long id;

    @ElementCollection
    @Column(name = "LINKS_MAP_VALUE")
    private Map<String,String> linksMap;

    public HibernateLinkMap(Map<String,String> linksMap)
    {
        this.linksMap = linksMap;
    }

    public HibernateLinkMap(){}

    public void setLinksMap(Map<String,String> linksMap)
    {
        this.linksMap = linksMap;
    }

    public Map<String,String> getLinksMap()
    {
        return linksMap;
    }
}
