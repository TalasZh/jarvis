package org.safehaus.stash.model;

import com.impetus.kundera.index.*;

import javax.persistence.*;
import javax.persistence.Index;
import java.util.Set;

/**
 * Created by neslihan on 03.07.2015.
 */
@Entity
@Table( name = "HIBERNATE_SET_MAP", schema = "jarvis@cassandra-pu" )
@IndexCollection( columns = {
        @com.impetus.kundera.index.Index( name = "id" )})
public class HibernateSetMap {

    @Id
    @TableGenerator( name = "id_gen", allocationSize = 30, initialValue = 100 )
    @GeneratedValue( generator = "id_gen", strategy = GenerationType.TABLE )
    private long id;

    @OneToMany(fetch = FetchType.EAGER)
    @Column(name = "LINKS_SET_VALUE")
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
