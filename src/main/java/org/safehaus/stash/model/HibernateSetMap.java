package org.safehaus.stash.model;

import com.impetus.kundera.index.*;
import com.impetus.kundera.index.Index;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by neslihan on 03.07.2015.
 */
@Entity
@Table( name = "hibernate_set_map", schema = "jarvis@cassandra-pu" )
@IndexCollection( columns = {
        @Index( name = "id" )})
public class HibernateSetMap {

    @Id
    @TableGenerator( name = "id_gen", allocationSize = 30, initialValue = 100 )
    @GeneratedValue( generator = "id_gen", strategy = GenerationType.TABLE )
    @Column(name= "map_id")
    private long id;

    @OneToMany(fetch = FetchType.EAGER)
    @Column(name = "links_set_value")
    private Set<HibernateLinkMap> linksValue;

    public HibernateSetMap(Set<HibernateLinkMap> linksValue) {
        this.linksValue = linksValue;
    }

    public HibernateSetMap() {}

    public void setLinksValue(Set<HibernateLinkMap> linksValue)
    {
        this.linksValue = linksValue;
    }

    public Set<HibernateLinkMap> getLinksValue()
    {
        return linksValue;
    }
}
