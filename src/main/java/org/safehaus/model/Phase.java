package org.safehaus.model;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Created by tzhamakeev on 5/15/15.
 */
/*@Entity
@Table( name = "app_phase" )*/
@XmlRootElement
public class Phase
{
    private Long id;
    private JarvisProject project;
    private String phase;
    private Date created;
    private Date updated;
    private Set<JarvisIssue> issue = new HashSet();


    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    public Long getId()
    {
        return id;
    }


    public void setId( final Long id )
    {
        this.id = id;
    }


    @XmlTransient
    @JsonIgnore
    @ManyToOne
    public JarvisProject getProject()
    {
        return project;
    }


    public void setProject( final JarvisProject project )
    {
        this.project = project;
    }


    @XmlTransient
    @JsonIgnore
    @OneToMany( mappedBy = "phase", cascade = CascadeType.ALL )
    public Set<JarvisIssue> getIssue()
    {
        return issue;
    }


    public void setIssue( final Set<JarvisIssue> issue )
    {
        this.issue = issue;
    }


    public String getPhase()
    {
        return phase;
    }


    public void setPhase( final String phase )
    {
        this.phase = phase;
    }


    public Date getCreated()
    {
        return created;
    }


    public void setCreated( final Date created )
    {
        this.created = created;
    }


    public Date getUpdated()
    {
        return updated;
    }


    public void setUpdated( final Date updated )
    {
        this.updated = updated;
    }
}
