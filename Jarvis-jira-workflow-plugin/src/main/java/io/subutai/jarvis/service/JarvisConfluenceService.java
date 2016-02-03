package io.subutai.jarvis.service;


import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.util.json.JSONException;


public interface JarvisConfluenceService
{
    public static final String CONFLUENCE_PAGE_TYPE = "page";
    public static final String CONFLUENCE_BLOG_TYPE = "blog";


    /*
    * Create confluence page
    * */


    public boolean confluencePageExists( Issue issue );


    public String getParentPageId( Issue issue );

    public String getProjectSpaceKey();

    public String createConfluencePageForStory( Issue issue, String projectKeySpace );

    public String createConfluencePage( String phaseName, String parentId ) throws JSONException;

    public int addLabel( String contentId, String... label );
}
