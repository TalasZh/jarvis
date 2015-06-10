package org.safehaus.stash;


public class TestUtil
{
    public static final String STASH_URL = "http://test-stash.critical-factor.com";
    public static final String PROJECT_KEY = "SSH";
    public static final String STASH_REPOS_JSON =
            "{\n" + "\"size\": 1,\n" + "\"limit\": 25,\n" + "\"isLastPage\": true,\n" + "\"values\": [\n" + "{\n" +
                    "\"slug\": \"hub\",\n" + "\"id\": 3,\n" + "\"name\": \"hub\",\n" + "\"scmId\": \"git\",\n"
                    + "\"state\": \"AVAILABLE\",\n" + "\"statusMessage\": \"Available\",\n" + "\"forkable\": true,\n"
                    + "\"project\": {\n" + "\"key\": \"SSH\",\n" + "\"id\": 21,\n"
                    + "\"name\": \"Subutai Social Hub\",\n"
                    + "\"description\": \"The Subutai Social Hub enables sharing configurations and a marketplace so "
                    + "people can share resources, blueprints, templates and plugins for the Subutai platform.\",\n"
                    + "\"public\": false,\n" + "\"type\": \"NORMAL\",\n" + "\"link\": {\n" + "\"url\": \"/projects"
                    + "/SSH\",\n" + "\"rel\": \"self\"\n" + "},\n" + "\"links\": {\n" + "\"self\": [\n" + "{\n"
                    + "\"href\": \"http://test-stash.critical-factor.com/projects/SSH\"\n" + "}\n" + "]\n" + "}\n"
                    + "},\n" + "\"public\": false,\n" + "\"link\": {\n"
                    + "\"url\": \"/projects/SSH/repos/hub/browse\",\n" + "\"rel\": \"self\"\n" + "},\n"
                    + "\"cloneUrl\": \"http://daliev@test-stash.critical-factor.com/scm/ssh/hub.git\",\n"
                    + "\"links\": {\n" + "\"clone\": [\n" + "{\n"
                    + "\"href\": \"ssh://git@test-stash.critical-factor.com:7999/ssh/hub.git\",\n"
                    + "\"name\": \"ssh\"\n" + "},\n" + "{\n" + "\"href\": \"http://daliev@test-stash.critical-factor"
                    + ".com/scm/ssh/hub.git\",\n" + "\"name\": \"http\"\n" + "}\n" + "],\n" + "\"self\": [\n" + "{\n"
                    + "\"href\": \"http://test-stash.critical-factor.com/projects/SSH/repos/hub/browse\"\n" + "}\n"
                    + "]\n" + "}\n" + "}\n" + "],\n" + "\"start\": 0\n" + "}";
    public static final String REPO_SLUG = "hub";
    public static final String STASH_REPO_JSON =
            "{\n" + "\"slug\": \"hub\",\n" + "\"id\": 3,\n" + "\"name\": \"hub\",\n" + "\"scmId\": \"git\",\n" +
                    "\"state\": \"AVAILABLE\",\n" + "\"statusMessage\": \"Available\",\n" + "\"forkable\": true,\n"
                    + "\"project\": {\n" + "\"key\": \"SSH\",\n" + "\"id\": 21,\n"
                    + "\"name\": \"Subutai Social Hub\",\n"
                    + "\"description\": \"The Subutai Social Hub enables sharing configurations and a marketplace so "
                    + "people can share resources, blueprints, templates and plugins for the Subutai platform.\",\n"
                    + "\"public\": false,\n" + "\"type\": \"NORMAL\",\n" + "\"link\": {\n" + "\"url\": \"/projects"
                    + "/SSH\",\n" + "\"rel\": \"self\"\n" + "},\n" + "\"links\": {\n" + "\"self\": [\n" + "{\n"
                    + "\"href\": \"http://test-stash.critical-factor.com/projects/SSH\"\n" + "}\n" + "]\n" + "}\n"
                    + "},\n" + "\"public\": false,\n" + "\"link\": {\n"
                    + "\"url\": \"/projects/SSH/repos/hub/browse\",\n" + "\"rel\": \"self\"\n" + "},\n"
                    + "\"cloneUrl\": \"http://daliev@test-stash.critical-factor.com/scm/ssh/hub.git\",\n"
                    + "\"links\": {\n" + "\"clone\": [\n" + "{\n"
                    + "\"href\": \"ssh://git@test-stash.critical-factor.com:7999/ssh/hub.git\",\n"
                    + "\"name\": \"ssh\"\n" + "},\n" + "{\n" + "\"href\": \"http://daliev@test-stash.critical-factor"
                    + ".com/scm/ssh/hub.git\",\n" + "\"name\": \"http\"\n" + "}\n" + "],\n" + "\"self\": [\n" + "{\n"
                    + "\"href\": \"http://test-stash.critical-factor.com/projects/SSH/repos/hub/browse\"\n" + "}\n"
                    + "]\n" + "}\n" + "}";
    public static String STASH_PROJECT_JSON =
            "{\n" + "\"key\": \"SSH\",\n" + "\"id\": 21,\n" + "\"name\": \"Subutai Social Hub\",\n"
                    + "\"description\": \"The Subutai Social Hub enables sharing configurations and a marketplace so "
                    + "people can share resources, blueprints, templates and plugins for the Subutai platform.\",\n"
                    + "\"public\": false,\n" + "\"type\": \"NORMAL\",\n" + "\"link\": {\n"
                    + "\"url\": \"/projects/SSH\",\n" + "\"rel\": \"self\"\n" + "},\n" + "\"links\": {\n"
                    + "\"self\": [\n" + "{\n" + "\"href\": \"http://test-stash.critical-factor.com/projects/SSH\"\n"
                    + "}\n" + "]\n" + "}\n" + "}";
    public static String STASH_PROJECTS_JSON =
            "{\n" + "\"size\": 4,\n" + "\"limit\": 25,\n" + "\"isLastPage\": true,\n" + "\"values\": [\n" + "{\n"
                    + "\"key\": \"AUTO\",\n" + "\"id\": 62,\n" + "\"name\": \"Automation System\",\n"
                    + "\"description\": \"This is the Tulpar and Subutai Automation System\",\n"
                    + "\"public\": false,\n" + "\"type\": \"NORMAL\",\n" + "\"link\": {\n"
                    + "\"url\": \"/projects/AUTO\",\n" + "\"rel\": \"self\"\n" + "},\n" + "\"links\": {\n"
                    + "\"self\": [\n" + "{\n" + "\"href\": \"http://test-stash.critical-factor.com/projects/AUTO\"\n"
                    + "}\n" + "]\n" + "}\n" + "},\n" + "{\n" + "\"key\": \"SSH\",\n" + "\"id\": 21,\n"
                    + "\"name\": \"Subutai Social Hub\",\n"
                    + "\"description\": \"The Subutai Social Hub enables sharing configurations and a marketplace so "
                    + "people can share resources, blueprints, templates and plugins for the Subutai platform.\",\n"
                    + "\"public\": false,\n" + "\"type\": \"NORMAL\",\n" + "\"link\": {\n"
                    + "\"url\": \"/projects/SSH\",\n" + "\"rel\": \"self\"\n" + "},\n" + "\"links\": {\n"
                    + "\"self\": [\n" + "{\n" + "\"href\": \"http://test-stash.critical-factor.com/projects/SSH\"\n"
                    + "}\n" + "]\n" + "}\n" + "},\n" + "{\n" + "\"key\": \"TULPAR\",\n" + "\"id\": 41,\n"
                    + "\"name\": \"Tulpar\",\n" + "\"public\": false,\n" + "\"type\": \"NORMAL\",\n" + "\"link\": {\n"
                    + "\"url\": \"/projects/TULPAR\",\n" + "\"rel\": \"self\"\n" + "},\n" + "\"links\": {\n"
                    + "\"self\": [\n" + "{\n" + "\"href\": \"http://test-stash.critical-factor.com/projects/TULPAR\"\n"
                    + "}\n" + "]\n" + "}\n" + "},\n" + "{\n" + "\"key\": \"WAN\",\n" + "\"id\": 66,\n"
                    + "\"name\": \"WANtastic\",\n" + "\"public\": false,\n" + "\"type\": \"NORMAL\",\n"
                    + "\"link\": {\n" + "\"url\": \"/projects/WAN\",\n" + "\"rel\": \"self\"\n" + "},\n"
                    + "\"links\": {\n" + "\"self\": [\n" + "{\n"
                    + "\"href\": \"http://test-stash.critical-factor.com/projects/WAN\"\n" + "}\n" + "]\n" + "}\n"
                    + "}\n" + "],\n" + "\"start\": 0\n" + "}";

    public static String STASH_GROUP_JSON =
            "{\n" + "\"size\": 2,\n" + "\"limit\": 25,\n" + "\"isLastPage\": true,\n" + "\"values\": [\n" + "{\n"
                    + "\"group\": {\n" + "\"name\": \"hub-admins\"\n" + "},\n" + "\"permission\": \"PROJECT_ADMIN\"\n"
                    + "},\n" + "{\n" + "\"group\": {\n" + "\"name\": \"hub-team\"\n" + "},\n"
                    + "\"permission\": \"PROJECT_WRITE\"\n" + "}\n" + "],\n" + "\"start\": 0\n" + "}";
}
