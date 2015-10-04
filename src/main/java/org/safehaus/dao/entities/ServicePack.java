package org.safehaus.dao.entities;


import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import static org.safehaus.Constants.DATABASE_SCHEMA;


/**
 * Created by talas on 10/3/15.
 */
@Entity
@Table( name = "service_pack", schema = DATABASE_SCHEMA )
@Access( AccessType.FIELD )
@IndexCollection( columns = {
        @Index( name = "jiraService" ), @Index( name = "stashService" ), @Index( name = "sonarService" ),
        @Index( name = "confluenceService" )
} )
public class ServicePack implements Serializable
{
    @Id
    @Column( name = "pack_id" )
    private String packId;

    @Embedded
    private ServiceIdentity jiraService;

    @Embedded
    private ServiceIdentity stashService;

    @Embedded
    private ServiceIdentity sonarService;

    @Embedded
    private ServiceIdentity confluenceService;


    public ServicePack()
    {
    }


    public ServicePack( final String packId, final ServiceIdentity jiraService, final ServiceIdentity stashService,
                        final ServiceIdentity sonarService, final ServiceIdentity confluenceService )
    {
        this.packId = packId;
        this.jiraService = jiraService;
        this.stashService = stashService;
        this.sonarService = sonarService;
        this.confluenceService = confluenceService;
    }


    public String getPackId()
    {
        return packId;
    }


    public void setPackId( final String packId )
    {
        this.packId = packId;
    }


    public ServiceIdentity getJiraService()
    {
        return jiraService;
    }


    public void setJiraService( final ServiceIdentity jiraService )
    {
        this.jiraService = jiraService;
    }


    public ServiceIdentity getStashService()
    {
        return stashService;
    }


    public void setStashService( final ServiceIdentity stashService )
    {
        this.stashService = stashService;
    }


    public ServiceIdentity getSonarService()
    {
        return sonarService;
    }


    public void setSonarService( final ServiceIdentity sonarService )
    {
        this.sonarService = sonarService;
    }


    public ServiceIdentity getConfluenceService()
    {
        return confluenceService;
    }


    public void setConfluenceService( final ServiceIdentity confluenceService )
    {
        this.confluenceService = confluenceService;
    }


    @Override
    public String toString()
    {
        return "ServicePack{" +
                "packId='" + packId + '\'' +
                ", jiraService=" + jiraService +
                ", stashService=" + stashService +
                ", sonarService=" + sonarService +
                ", confluenceService=" + confluenceService +
                '}';
    }
}
