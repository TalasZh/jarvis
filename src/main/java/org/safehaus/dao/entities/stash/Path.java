package org.safehaus.dao.entities.stash;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.google.common.base.Objects;
import com.impetus.kundera.index.IndexCollection;


/**
 * Created by neslihan on 07.07.2015.
 */
@Entity
@Table( name = "stash_path", schema = "jarvis@cassandra-pu" )
@IndexCollection( columns = {
        @com.impetus.kundera.index.Index( name = "id" ), @com.impetus.kundera.index.Index( name = "name" )
} )
public class Path implements Serializable
{

    @Id
    @TableGenerator( name = "id_gen", allocationSize = 30, initialValue = 100 )
    @GeneratedValue( generator = "id_gen", strategy = GenerationType.TABLE )
    private long id;

    @ElementCollection( targetClass = String.class, fetch = FetchType.EAGER )
    @Column
    private List<String> components = new ArrayList<>();

    @Column
    private String parent;

    @Column
    private String name;

    @Column
    private String extension;

    @Column
    private String toString;


    public List<String> getComponents()
    {
        return components;
    }


    public String getParent()
    {
        return parent;
    }


    public String getName()
    {
        return name;
    }


    public String getExtension()
    {
        return extension;
    }


    public String getToString()
    {
        return toString;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "components", components ).add( "parent", parent )
                      .add( "name", name ).add( "extension", extension ).add( "toString", toString ).toString();
    }


    public void setId( long id )
    {
        this.id = id;
    }


    public void setComponents( List<String> components )
    {
        this.components = components;
    }


    public void setParent( String parent )
    {
        this.parent = parent;
    }


    public void setName( String name )
    {
        this.name = name;
    }


    public void setExtension( String extension )
    {
        this.extension = extension;
    }


    public void setToString( String toString )
    {
        this.toString = toString;
    }
}
