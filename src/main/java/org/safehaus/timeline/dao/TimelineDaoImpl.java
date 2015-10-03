package org.safehaus.timeline.dao;


import java.util.List;
import java.util.Map;

import org.safehaus.dao.Dao;
import org.safehaus.timeline.model.StructuredIssue;
import org.safehaus.timeline.model.StructuredProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;


/**
 * Created by talas on 9/29/15.
 */
@Service( "timelineService" )
public class TimelineDaoImpl implements TimelineDao
{
    @Autowired
    private Dao daoManager;


    public void setDaoManager( final Dao daoManager )
    {
        this.daoManager = daoManager;
    }


    public void insertProject( StructuredProject project )
    {
        daoManager.insert( project );
    }


    public void updateProject( StructuredProject project )
    {
        daoManager.merge( project );
        for ( final StructuredIssue structuredIssue : project.getIssues() )
        {
            updateStructuredIssue( structuredIssue );
        }
    }


    public void deleteProject( StructuredProject project )
    {
        daoManager.remove( project );
    }


    public StructuredProject getProject( String id )
    {
        return daoManager.findById( StructuredProject.class, id );
    }


    public StructuredProject getProjectByKey( String projectKey )
    {
        String query = String.format( "select t from %s t where t.key = :projectKey",
                StructuredProject.class.getSimpleName() );
        List<StructuredProject> results =
                ( List<StructuredProject> ) daoManager.findByQuery( query, "projectKey", projectKey );
        if ( results.size() == 0 )
        {
            return null;
        }
        else
        {
            return results.iterator().next();
        }
    }


    public List<StructuredProject> getAllProjects()
    {
        return daoManager.getAll( StructuredProject.class );
    }


    public void insertStructuredIssue( StructuredIssue issue )
    {
        daoManager.insert( issue );
    }


    public void updateStructuredIssue( StructuredIssue issue )
    {
        daoManager.merge( issue );
        for ( final StructuredIssue structuredIssue : issue.getIssues() )
        {
            updateStructuredIssue( structuredIssue );
        }
    }


    public void deleteStructuredIssue( StructuredIssue issue )
    {
        daoManager.remove( issue );
    }


    public StructuredIssue getStructuredIssue( String id )
    {
        return daoManager.findById( StructuredIssue.class, id );
    }


    public StructuredIssue getStructuredIssueByKey( String key )
    {
        String parameter = "key";
        String query = String.format( "select s from %s s where s.key = :%s", StructuredIssue.class.getSimpleName(),
                parameter );
        Map<String, Object> params = Maps.newHashMap();
        params.put( parameter, key );

        List<StructuredIssue> results = daoManager.findByQuery( StructuredIssue.class, query, params );
        if ( results.size() == 0 )
        {
            return null;
        }
        else
        {
            StructuredIssue issue = results.iterator().next();
            for ( final String issueKey : issue.getIssuesKeys() )
            {
                StructuredIssue structuredIssue = getStructuredIssueByKey( issueKey );
                issue.addIssue( structuredIssue );
            }
            return issue;
        }
    }


    public List<StructuredIssue> getAllStructuredISsues()
    {
        return daoManager.getAll( StructuredIssue.class );
    }
}
