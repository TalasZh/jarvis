package org.safehaus.upsource.util;


public class TestUtil
{
    public static final String PROJECTS_JSON =
            "{\n" + "\"result\": {\n" + "\"project\": [\n" + "{\n" + "\"projectName\": \"Subutai Agent\",\n"
                    + "\"projectId\": \"subutai-agent\",\n" + "\"headHash\": \"69b6edc893967b89\",\n"
                    + "\"codeReviewIdPattern\": \"SA-CR-{}\",\n" + "\"lastCommitDate\": 1424419477000,\n"
                    + "\"lastCommitAuthorName\": \"Ozlem Ceren Sahin\",\n" + "\"projectModelType\": \"none\"\n" + "},\n"
                    + "{\n" + "\"projectName\": \"Subutai\",\n" + "\"projectId\": \"subutai\",\n"
                    + "\"headHash\": \"fe8381528f2596b7\",\n" + "\"codeReviewIdPattern\": \"SBT-CR-{}\",\n"
                    + "\"lastCommitDate\": 1434986436000,\n" + "\"lastCommitAuthorName\": \"Ozlem Ceren Sahin\",\n"
                    + "\"projectModelType\": \"maven\"\n" + "}\n" + "]\n" + "}\n" + "}";
    public static final String PROJECT_ID = "subutai";
    public static final String PROJECT_JSON =
            "{\n" + "\"result\": {\n" + "\"projectName\": \"Subutai\",\n" + "\"projectId\": \"subutai\",\n" +
                    "\"headHash\": \"fe8381528f2596b7\",\n" + "\"codeReviewIdPattern\": \"SBT-CR-{}\",\n"
                    + "\"lastCommitDate\": 1434986436000,\n" + "\"lastCommitAuthorName\": \"Ozlem Ceren Sahin\",\n"
                    + "\"projectModelType\": \"maven\"\n" + "}\n" + "}";
    public static final String REVISIONS_JSON = "{\n" + "\"result\": {\n" + "\"revision\": [\n" + "{\n"
            + "\"revisionId\": \"53066db39b844d90a6ed16675669d352a745da58\",\n" + "\"revisionDate\": 1434986436000,\n"
            + "\"effectiveRevisionDate\": 1434986436000,\n"
            + "\"revisionCommitMessage\": \"modify activemq debian creator\\ \",\n" + "\"state\": 3,\n"
            + "\"revisionIdShort\": \"53066db\",\n"
            + "\"authorId\": \"~Ozlem Ceren Sahin <ozlemcs@critical-factor.com>\",\n" + "\"branchHeadLabel\": [\n"
            + "\"keshig-integ\"\n" + "],\n" + "\"parentRevisions\": [\n"
            + "\"581a42f1813c7499277ac39d83fa4479e36d06c3\"\n" + "]\n" + "},\n" + "{\n"
            + "\"revisionId\": \"581a42f1813c7499277ac39d83fa4479e36d06c3\",\n" + "\"revisionDate\": 1434982033000,\n"
            + "\"effectiveRevisionDate\": 1434982033000,\n"
            + "\"revisionCommitMessage\": \"modify activemq debian creator\\ \",\n" + "\"state\": 3,\n"
            + "\"revisionIdShort\": \"581a42f\",\n"
            + "\"authorId\": \"~Ozlem Ceren Sahin <ozlemcs@critical-factor.com>\",\n" + "\"parentRevisions\": [\n"
            + "\"518f278a6751acd825f6327756e0ac9ae6d4f1a7\"\n" + "],\n" + "\"childRevisions\": [\n"
            + "\"53066db39b844d90a6ed16675669d352a745da58\"\n" + "]\n" + "},\n" + "{\n"
            + "\"revisionId\": \"518f278a6751acd825f6327756e0ac9ae6d4f1a7\",\n" + "\"revisionDate\": 1434972393000,\n"
            + "\"effectiveRevisionDate\": 1434972393000,\n" + "\"revisionCommitMessage\": \"test keshig build\\ \",\n"
            + "\"state\": 3,\n" + "\"revisionIdShort\": \"518f278\",\n"
            + "\"authorId\": \"~Ozlem Ceren Sahin <ozlemcs@critical-factor.com>\",\n" + "\"parentRevisions\": [\n"
            + "\"9b45d8cb6f6b9a23d4e4232ad321f56a32409a86\"\n" + "],\n" + "\"childRevisions\": [\n"
            + "\"581a42f1813c7499277ac39d83fa4479e36d06c3\"\n" + "]\n" + "}\n" + "],\n"
            + "\"headHash\": \"fe8381528f2596b7\"\n" + "}\n" + "}";
    public static final String REVISION_JSON =
            "{\n" + "\"result\": {\n" + "\"revisionId\": \"53066db39b844d90a6ed16675669d352a745da58\",\n" +
                    "\"revisionDate\": 1434986436000,\n" + "\"effectiveRevisionDate\": 1434986436000,\n"
                    + "\"revisionCommitMessage\": \"modify activemq debian creator\\ \",\n" + "\"state\": 3,\n"
                    + "\"revisionIdShort\": \"53066db\",\n"
                    + "\"authorId\": \"~Ozlem Ceren Sahin <ozlemcs@critical-factor.com>\",\n"
                    + "\"branchHeadLabel\": [\n" + "\"keshig-integ\"\n" + "],\n" + "\"parentRevisions\": [\n"
                    + "\"581a42f1813c7499277ac39d83fa4479e36d06c3\"\n" + "]\n" + "}\n" + "}";

    public static final String REVISION_FILTER = "author: {dilshat <daliev@critical-factor.com>}";
    public static final String REVISION_ID = "e3c136eeaf405183e634dabad2749b4c23eb3987";
    public static final String REVISION_CHANGES_JSON =
            "{\n" + "\"result\": {\n" + "\"diff\": [\n" + "{\n" + "\"projectId\": \"subutai\",\n" + "\"diffType\": 3,\n"
                    + "\"newFile\": {\n" + "\"projectId\": \"subutai\",\n"
                    + "\"revisionId\": \"e3c136eeaf405183e634dabad2749b4c23eb3987\",\n"
                    + "\"fileName\": \"/management/server/core/identity-manager/identity-manager-impl/src/main/java"
                    + "/org/safehaus/subutai/core/identity/impl/ShiroLoginModule.java\"\n" + "},\n"
                    + "\"fileIcon\": \"Class\"\n" + "}\n" + "]\n" + "}\n" + "}";
    public static final String REVISION_BRANCHES_JSON =
            "{\n" + "\"result\": {\n" + "\"branchName\": [\n" + "\"keshig-integ\",\n" + "\"karaf-from-scratch\",\n" +
                    "\"test-agent\",\n" + "\"security-enhancement-unprivileged\",\n" + "\"master\",\n"
                    + "\"security_unit_tests\",\n" + "\"unit-tests\",\n" + "\"core-security\",\n"

                    + "\"fix-agent-protobuf\",\n" + "\"core-refactor\"\n" + "]\n" + "}\n" + "}";
    public static final String FILE_NAME =
            "/management/server/core/identity-manager/identity-manager-impl/src/main/java/org/safehaus/subutai/core"
                    + "/identity/impl/ShiroLoginModule.java";
    public static final String FILE_ANNOTATION_JSON =
            "{\n" + "\"result\": {\n" + "\"retrospective\": [\n" + "{\n" + "\"startLine\": 0,\n" + "\"lineCount\": 4,\n"
                    + "\"revision\": {\n" + "\"revisionId\": \"9cdbed405931b57ee230f5efe95d39272b159da4\",\n" +
                    "\"revisionDate\": 1422012387000,\n" + "\"effectiveRevisionDate\": 1422012387000,\n"
                    + "\"revisionCommitMessage\": \"secure UI with Shiro\\ \",\n" + "\"state\": 4,\n"
                    + "\"revisionIdShort\": \"9cdbed4\",\n" + "\"authorId\": \"~Timur Zhamakeev <ztimur@gmail.com>\",\n"
                    + "\"parentRevisions\": [\n" + "\"54a3c7ada7dafd93298032c22807d436fc7bc8ed\"\n" + "],\n" +
                    "\"childRevisions\": [\n" + "\"35a89d369291641be7ce9fe1f3af17aee0e0593c\"\n" + "]\n" + "},\n"
                    + "\"filePath\": \"/management/server/core/identity-manager/identity-manager-impl/src/main/java"
                    + "/org/safehaus/subutai/core/identity/impl/ShiroLoginModule.java\"\n" + "}\n" + "]\n" + "}\n"
                    + "}";
    public static final String CONTRIBUTORS_JSON =
            "{\n" + "\"result\": {\n" + "\"authorIds\": [\n" + "\"~Timur Zhamakeev <ztimur@gmail.com>\",\n" +
                    "\"~dilshat <daliev@critical-factor.com>\",\n" + "\"03540de1-269a-4ad4-9709-18809c55f4fc\"\n"
                    + "]\n" + "}\n" + "}";
    public static final String FILE_HISTORY_JSON =
            "{\n" + "\"result\": {\n" + "\"history\": [\n" + "{\n" + "\"diffType\": 1,\n" + "\"revision\": {\n"
                    + "\"revisionId\": \"9cdbed405931b57ee230f5efe95d39272b159da4\",\n"
                    + "\"revisionDate\": 1422012387000,\n" + "\"effectiveRevisionDate\": 1422012387000,\n"
                    + "\"revisionCommitMessage\": \"secure UI with Shiro\\ \",\n" + "\"state\": 4,\n"
                    + "\"revisionIdShort\": \"9cdbed4\",\n" + "\"authorId\": \"~Timur Zhamakeev <ztimur@gmail.com>\",\n"
                    + "\"parentRevisions\": [\n" + "\"54a3c7ada7dafd93298032c22807d436fc7bc8ed\"\n" + "],\n"
                    + "\"childRevisions\": [\n" + "\"35a89d369291641be7ce9fe1f3af17aee0e0593c\"\n" + "]\n" + "},\n"
                    + "\"fileName\": \"/management/server/core/identity-manager/identity-manager-impl/src/main/java"
                    + "/org/safehaus/subutai/core/identity/impl/ShiroLoginModule.java\"\n" + "}\n" + "],\n"
                    + "\"graph\": {\n" + "\"width\": 1,\n" + "\"rows\": [\n" + "{\n" + "\"nodes\": [\n" + "{\n"
                    + "\"position\": 0,\n" + "\"color\": 1300,\n" + "\"type\": 1\n" + "}\n" + "],\n" + "\"edges\": [\n"
                    + "{\n" + "\"position\": 0,\n" + "\"toPosition\": 0,\n" + "\"isUp\": false,\n"
                    + "\"isSolid\": false,\n" + "\"color\": 1300\n" + "}\n" + "]\n" + "},\n" + "{\n" + "\"nodes\": [\n"
                    + "{\n" + "\"position\": 0,\n" + "\"color\": 1300,\n" + "\"type\": 1\n" + "}\n" + "]\n" + "}\n"
                    + "]\n" + "}\n" + "}\n" + "}";
    public static final String REVIEWS_JSON =
            "{\n" + "\"result\": {\n" + "\"reviews\": [\n" + "{\n" + "\"reviewId\": {\n"
                    + "\"projectId\": \"subutai\",\n" + "\"reviewId\": \"SBT-CR-1\"\n" + "},\n"
                    + "\"title\": \"Refactored Messenger to user reference list instead of explicit listener "
                    + "binding\",\n" + "\"participants\": [\n" + "{\n"
                    + "\"userId\": \"~dilshat <daliev@critical-factor.com>\",\n" + "\"role\": 1,\n" + "\"state\": 1\n"
                    + "},\n" + "{\n" + "\"userId\": \"e84a1a2a-21fc-42d5-a439-3851b2950eea\",\n" + "\"role\": 2,\n"
                    + "\"state\": 2\n" + "}\n" + "],\n" + "\"state\": 2,\n" + "\"unread\": false,\n"
                    + "\"priority\": 4\n" + "}\n" + "],\n" + "\"more\": false\n" + "}\n" + "}";
    public static final String REVIEW_ID = "SBT-CR-1";
    public static final String REVIEW_JSON =
            "{\n" + "\"result\": {\n" + "\"reviewId\": {\n" + "\"projectId\": \"subutai\",\n"
                    + "\"reviewId\": \"SBT-CR-1\"\n" + "},\n"
                    + "\"title\": \"Refactored Messenger to user reference list instead of explicit listener "
                    + "binding\",\n" + "\"participants\": [\n" + "{\n"
                    + "\"userId\": \"~dilshat <daliev@critical-factor.com>\",\n" + "\"role\": 1,\n" + "\"state\": 1\n"
                    + "},\n" + "{\n" + "\"userId\": \"e84a1a2a-21fc-42d5-a439-3851b2950eea\",\n" + "\"role\": 2,\n"
                    + "\"state\": 2\n" + "}\n" + "],\n" + "\"state\": 2,\n" + "\"canCreateIssue\": false\n" + "}\n"
                    + "}";
    public static final String PROJECT_ACTIVITY_JSON =
            "{\n" + "\"result\": {\n" + "\"items\": [\n" + "{\n" + "\"time\": 1378944000000,\n" + "\"value\": 50\n" +
                    "}\n" + "],\n" + "\"modules\": [\n" + "\"org.safehaus.subutai-command-executor-impl\",\n"
                    + "\"org.safehaus.subutai-subutai-common\",\n" + "\"org.safehaus.subutai-peer-manager-ui\",\n"
                    + "\"org.safehaus.subutai-plugin-manager-ui\",\n" + "\"org.safehaus.subutai-wol-manager-ui\",\n"

                    + "\"org.safehaus.subutai-channel-manager-ui\",\n"
                    + "\"org.safehaus.subutai-subutai-common-datasource\",\n"
                    + "\"org.safehaus.subutai-peer-manager-api\",\n"
                    + "\"org.safehaus.subutai-template-registry-impl\",\n"
                    + "\"org.safehaus.subutai-network-manager-api\",\n"
                    + "\"org.safehaus.subutai-network-manager-impl\",\n" + "\"org.safehaus.subutai-env-manager-cli\",\n"
                    + "\"org.safehaus.subutai-template-registry-ui\",\n" + "\"org.safehaus.subutai-repository-manager"
                    + "-impl\",\n" + "\"org.safehaus.subutai-broker-api\"\n" + "],\n" + "\"stats\": {\n"
                    + "\"minCommitTime\": 1376866982000,\n" + "\"maxCommitTime\": 1435137675000,\n"
                    + "\"totalCommits\": 13475,\n" + "\"minIndexedCommitTime\": 1417893496000,\n"
                    + "\"maxIndexedCommitTime\": 1435135468000,\n" + "\"totalIndexedCommits\": 275,\n"
                    + "\"projectModelKnown\": true\n" + "}\n" + "}\n" + "}";
    public static final String RESPONSIBILITY_DIST_JSON =
            "{\n" + "\"result\": {\n" + "\"items\": [\n" + "{\n" + "\"committer\": \"emininal <emininal@gmail.com>\"\n"
                    + "}\n" + "],\n" + "\"users\": [\n" + "{\n" + "\"committer\": \"skardan <kardan38@gmail.com>\",\n"
                    + "\"userId\": \"~skardan <kardan38@gmail.com>\"\n" + "}\n" + "],\n" + "\"modules\": [ … ],\n"
                    + "\"stats\": {\n" + "\"minCommitTime\": 1376866982000,\n" + "\"maxCommitTime\": 1435137675000,\n"
                    + "\"totalCommits\": 13475,\n" + "\"minIndexedCommitTime\": 1417893496000,\n"
                    + "\"maxIndexedCommitTime\": 1435135468000,\n" + "\"totalIndexedCommits\": 275,\n"
                    + "\"projectModelKnown\": true\n" + "}\n" + "}\n" + "}";
    public static final String COMMITERS_JSON = "{\n" + "  \"result\": {\n" + "    \"users\": [\n" + "      {\n"
            + "        \"committer\": \"skardan <kardan38@gmail.com>\",\n"
            + "        \"userId\": \"~skardan <kardan38@gmail.com>\"\n" + "      },\n" + "      {\n"
            + "        \"committer\": \"damir aralbaev <daralbaev@critical-factor.com>\",\n"
            + "        \"userId\": \"5e6472c3-27df-40e2-b279-1a96e84b643a\"\n" + "      }\n" + "    ]\n" + "  }\n" +
            "}";
    public static final String USER_ACTIVITY_JSON =
            "{\n" + "  \"result\": {\n" + "    \"items\": [\n" + "      {\n" + "        \"time\": 1378944000000,\n" +
                    "        \"value\": 0\n" + "      },\n" + "      {\n" + "        \"time\": 1381536000000,\n"
                    + "        \"value\": 0\n" + "      }\n" + "    ],\n" + "    \"stats\": {\n"

                    + "      \"minCommitTime\": 1376866982000,\n" + "      \"maxCommitTime\": 1435311078000,\n"
                    + "      \"totalCommits\": 13491,\n" + "      \"minIndexedCommitTime\": 1417893496000,\n"
                    + "      \"maxIndexedCommitTime\": 1435135468000,\n" + "      \"totalIndexedCommits\": 275,\n"
                    + "      \"projectModelKnown\": true\n" + "    }\n" + "  }\n" + "}";
    public static final String REVIEW_STATS_JSON =
            "{\n" + "  \"result\": {\n" + "    \"openReviews\": 0,\n" + "    \"closedReviews\": 1,\n" + "    "
                    + "\"allRevisions\": 13514,\n" + "    \"revisionsCoveredByOpenReviews\": 0,\n"
                    + "    \"revisionsCoveredByClosedReviews\": 0,\n" + "    \"authorStatsByReviews\": [\n"
                    + "      {\n" + "        \"userId\": \"~dilshat <daliev@critical-factor.com>\",\n"
                    + "        \"value\": 1\n" + "      }\n" + "    ],\n" + "    \"reviewerStatsByReviews\": [\n"
                    + "      {\n" + "        \"userId\": \"e84a1a2a-21fc-42d5-a439-3851b2950eea\",\n"
                    + "        \"value\": 1\n" + "      }\n" + "    ],\n" + "    \"authorStatsByRevisions\": [\n"
                    + "      {\n"

                    + "        \"userId\": \"~dilshat <daliev@critical-factor.com>\",\n" + "        \"value\": 0\n"
                    + "      }\n" + "    ],\n" + "    \"reviewerStatsByRevisions\": [\n" + "      {\n"
                    + "        \"userId\": \"e84a1a2a-21fc-42d5-a439-3851b2950eea\",\n" + "        \"value\": 0\n"
                    + "      }\n" + "    ]\n" + "  }\n" + "}";
    public static final String REVIEW_COVERAGE_JSON =
            "{\n" + "  \"result\": {\n" + "    \"allRevisions\": [\n" + "      {\n"
                    + "        \"time\": 1384128000000,\n" + "        \"value\": 229\n" + "      }\n" + "    ],\n"
                    + "    \"coveredRevisions\": [\n" + "      {\n" + "        \"time\": 1384128000000,\n"
                    + "        \"value\": 0\n" + "      }\n" + "    ]\n" + "  }\n" + "}";
}
