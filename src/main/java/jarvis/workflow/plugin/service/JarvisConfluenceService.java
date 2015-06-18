package jarvis.workflow.plugin.service;


import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.util.json.JSONException;

import jarvis.workflow.plugin.domain.ConfluencePage;


public interface JarvisConfluenceService
{
    public static final String CONFLUENCE_PAGE_TYPE = "page";
    public static final String CONFLUENCE_BLOG_TYPE = "blog";


    /*
    * Create confluence page
    * */

    public String createConfluencePage( Issue issue );

    public boolean confluencePageExists( Issue issue );

    public ConfluencePage getConfluencePage();

    public ConfluencePage findByTitleAndSpaceKey();

    public void updateConfluencePage();

    public String getParentPageId( Issue issue );

    public String getProjectSpaceKey();

    public String createConfluencePageForStory( Issue issue, String projectKeySpace );

    public boolean confluencePageExists( String id );

    public String createConfluencePage( String phaseName, String parentId ) throws JSONException;

    public int addLabel( String contentId, String... label );
}
