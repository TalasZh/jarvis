package jarvis.workflow.plugin.domain;


public class ConfluencePagesHierarchy
{

    private String storyId;
    private String requirementsId;
    private String designId;
    private String playbookId;


    @Override
    public String toString()
    {
        return "ConfluencePagesHierarchy{" +
                "storyId='" + storyId + '\'' +
                ", requirementsId='" + requirementsId + '\'' +
                ", designId='" + designId + '\'' +
                ", playbookId='" + playbookId + '\'' +
                '}';
    }


    public String getStoryId()
    {
        return storyId;
    }


    public void setStoryId( final String storyId )
    {
        this.storyId = storyId;
    }


    public String getRequirementsId()
    {
        return requirementsId;
    }


    public void setRequirementsId( final String requirementsId )
    {
        this.requirementsId = requirementsId;
    }


    public String getDesignId()
    {
        return designId;
    }


    public void setDesignId( final String designId )
    {
        this.designId = designId;
    }


    public String getPlaybookId()
    {
        return playbookId;
    }


    public void setPlaybookId( final String playbookId )
    {
        this.playbookId = playbookId;
    }
}
