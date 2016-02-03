package Helpers;

/**
 * Created by won on 4/29/15.
 */
public class Session {
    public static final String serverName = "http://kazim-ubuntu:1990/confluence";
    public static final String limit = "?limit=10";
    public static final String username = "admin";
    public static final String password = "admin";
    public static final String credentials = String.format("%s:%s", username, password);
    public static final String currentDir = System.getProperty("user.dir");

    public static class RestApiUrl{
        private static final String _contentUrl = serverName + "/rest/api/content"; //GET, POST
        private static final String _spaceUrl = serverName + "/rest/api/space"; //GET, POST
        private static final String _searchUrl = serverName + "/rest/api/content/search"; //GET
        private static final String _convertBodyUrl = serverName + "/rest/api/contentbody/convert"; //POST
        private static final String _longTaskUrl = serverName + "/rest/api/longtask"; //GET

        /** GET, POST */
        public static String contentUrl(){
            return String.format("%s", _contentUrl);
        }
        /** DELETE, PUT, GET */
        public static String specificContentUrl(String contentId){
            return String.format("%s/%s", _contentUrl, contentId);
        }

        /** GET */
        public static String specificContentHistoryUrl(String contentId){
            return String.format("%s/%s/history", _contentUrl, contentId);
        }

        /** GET */
        public static String specificContentHistoryMacroUrl(String contentId, String version, String hash){
            return String.format("%s/%s/history/%s/macro/hash/%s", _contentUrl, contentId, version, hash);
        }

        /** GET */
        public static String searchUrl(String searchString){
            return String.format(_searchUrl);
        }

        /** GET */
        public static String specificContentChildUrl(String contentId){
            return String.format("%s/%s/child", _contentUrl, contentId);
        }

        /** GET */
        public static String specificContentChildTypeUrl(String contentId, String type){
            return String.format("%s/%s/child/%s", _contentUrl, contentId, type);
        }

        /** GET */
        public static String specificContentChildCommentUrl(String contentId){
            return String.format("%s/%s/child/comment", _contentUrl, contentId);
        }

        /** GET, POST */
        public static String specificContentChildAttachmentUrl(String contentId){
            return String.format("%s/%s/child/attachment", _contentUrl, contentId);
        }

        /** PUT */
        public static String specificContentChildSpecificAttachmentUrl(String contentId, String attachmentId){
            return String.format("%s/%s/child/attachment/%s", _contentUrl, contentId, attachmentId);
        }

        /** POST */
        public static String specificContentChildSpecificAttachmentDataUrl(String contentId, String attachmentId){
            return String.format("%s/%s/child/attachment/%s/data", _contentUrl, contentId, attachmentId);
        }

        /** GET */
        public static String specificContentDescendantUrl(String contentId){
            return String.format("%s/%s/descendant", _contentUrl, contentId);
        }

        /** GET */
        public static String specificContentDescendantTypeUrl(String contentid, String type){
            return String.format("%s/%s/descendant/%s", _contentUrl, contentid, type);
        }

        /** GET, POST */
        public static String specificContentLabelUrl(String contentId){
            return String.format("%s/%s/label", _contentUrl, contentId);
        }

        /** GET, POST */
        public static String specificContentPropertyUrl(String contentId){
            return String.format("%s/%s/property", _contentUrl, contentId);
        }

        /** DELETE, POST, PUT, GET */
        public static String specificContentSpecificPropertyUrl(String contentId, String propertyId){
            return String.format("%s/%s/property/%s", _contentUrl, contentId, propertyId);
        }

        //?
        public static String specificContentRestrictionUrl(String contentId){
            return String.format("%s/%s/restriction", _contentUrl, contentId);
        }

        /** GET */
        public static String specificContentRestrictionyByOperationUrl(String contentId){
            return String.format("%s/%s/restriction/byOperation", _contentUrl, contentId);
        }

        /** GET */
        public static String specificContentRestrictionBySpecificOperationUrl(String contentId, String operationKey){
            return String.format("%s/%s/restriction/byOperation/%s", _contentUrl, contentId, operationKey);
        }

        /** POST */
        public static String specificContentConvertBodyUrl(String to){
            return String.format("%s/%s", _contentUrl, to);
        }

        /** GET */
        public static String longTaskUrl(){
            return _longTaskUrl;
        }

        /** GET */
        public static String SpecificLongTaskUrl(String longtaskId){
            return String.format("%s/%s", _longTaskUrl, longtaskId);
        }

        /** GET, POST */
        public static String spaceUrl(){
            return String.format("%s", _spaceUrl);
        }

        /** POST */
        public static String spacePrivateUrl(){
            return String.format("%s/_private", _spaceUrl);
        }

        /** GET, DELETE, PUT */
        public static String specificSpaceKeyUrl(String spaceKey){
            return String.format("%s/%s", _spaceUrl, spaceKey);
        }

        /** GET */
        public static String specificSpaceContentUrl(String spaceKey){
            return String.format("%s/%s/content", _spaceUrl, spaceKey);
        }

        /** GET */
        public static String specificSpaceContentTypeUrl(String spaceKey, String type){
            return String.format("%s/%s/content/%s", _spaceUrl, spaceKey, type);
        }

        /** GET */
        public static String nextResultUrl(String nextUrl){
            return String.format("%s/%s", serverName, nextUrl);
        }


    }
}
