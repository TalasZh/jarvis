package org.safehaus.timeline.model;


import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;

import com.google.common.collect.Maps;


/**
 * Created by talas on 10/4/15.
 */

public class UserInfo
{
    @Id
    @Column( name = "user_id" )
    private String userId;

    @Column( name = "username" )
    private String username;

    @Column( name = "email" )
    private String email;

    @Column( name = "display_name" )
    private String displayName;

    @ElementCollection
    @MapKeyColumn( name = "issuesSolved" )
    @Column( name = "totalSolved" )
    @CollectionTable( name = "resolvedIssues", joinColumns = @JoinColumn( name = "solved_id" ) )
    private Map<String, Long> totalIssuesSolved = Maps.newHashMap();

    @Embedded
    private ProgressStatus openStatus;

    @Embedded
    private ProgressStatus inProgressStatus;

    @Embedded
    private ProgressStatus doneStatus;
}
