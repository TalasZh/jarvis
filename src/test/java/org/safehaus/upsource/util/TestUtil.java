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
                    + "/org/safehaus/subutai/core/identity/impl/ShiroLoginModule.java\"\n"
                    + "},\n" + "\"fileIcon\": \"Class\"\n" + "}\n" + "]\n" + "}\n" + "}";
}
