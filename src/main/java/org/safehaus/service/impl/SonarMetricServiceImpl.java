package org.safehaus.service.impl;


import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.codehaus.jackson.map.annotate.JsonView;
import org.safehaus.dao.Dao;
import org.safehaus.dao.entities.sonar.SonarMetricIssue;
import org.safehaus.model.Views;
import org.safehaus.service.api.SonarMetricService;
import org.safehaus.service.rest.SonarMetricsRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;


/**
 * Created by ermek on 10/1/15.
 */
@Service( "sonarMetricsManager" )
@WebService( serviceName = "SonarMetricServiceImpl",
        endpointInterface = "org.safehaus.service.rest.SonarMetricsRestService" )

public class SonarMetricServiceImpl implements SonarMetricService, SonarMetricsRestService
{
    private static final Logger log = LoggerFactory.getLogger( SonarMetricServiceImpl.class );

    @Autowired
    private Dao dao;


    @Override
    public void insertSonarMetricIssue( final SonarMetricIssue sonarMetricIssue )
    {
        log.info( "Inserting new StashMetricIssue" );
        dao.insert( sonarMetricIssue );
    }


    @Override
    public SonarMetricIssue getSonarMetricIssueByName( final String projectName )
    {

        String parameter = "projectName";
        String query = String.format( "SELECT sp FROM %s sp WHERE sp.projectName = :%s",
                SonarMetricIssue.class.getSimpleName(), projectName );

        Map<String, Object> params = Maps.newHashMap();
        params.put( parameter, projectName );

        List<SonarMetricIssue> results = dao.findByQuery( SonarMetricIssue.class, query, params );

        if ( results.size() == 0 )
        {
            return null;
        }
        else
        {
            return results.iterator().next();
        }
    }


    @Override
    @JsonView( Views.CompleteView.class )
    public SonarMetricIssue findSonarMetricIssueByProjectId( final String id )
    {
        log.info( "Finding SonarMetricIssue by id: {}", id );
        return dao.findById( SonarMetricIssue.class, id );
    }
}
