package org.safehaus.stash.model;

import com.google.common.base.Objects;
import com.impetus.kundera.index.IndexCollection;

import javax.persistence.*;
import java.util.List;

/**
 * Created by neslihan on 07.07.2015.
 */
@Entity
@Table( name = "stash_path", schema = "jarvis@cassandra-pu" )
@IndexCollection( columns = {
        @com.impetus.kundera.index.Index( name = "id" ), @com.impetus.kundera.index.Index( name = "name" )})
public class Path {

    @Id
    @TableGenerator( name = "id_gen", allocationSize = 30, initialValue = 100 )
    @GeneratedValue( generator = "id_gen", strategy = GenerationType.TABLE )
    private long id;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @Column
    private List<String> components;

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
        return Objects.toStringHelper(this).add( "components", components ).add( "parent", parent )
                    .add( "name", name ).add( "extension", extension ).add( "toString", toString ).toString();
    }
}
