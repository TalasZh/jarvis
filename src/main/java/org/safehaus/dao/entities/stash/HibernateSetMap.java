package org.safehaus.dao.entities.stash;


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;


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
