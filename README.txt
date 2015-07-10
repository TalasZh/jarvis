Jarvis Core
--------------------------------------------------------------------------------
If you're reading this then you've created your new project using Maven and
jarvis.  You have only created the shell of an AppFuse Java EE
application.  The project object model (pom) is defined in the file pom.xml.
The application is ready to run as a web application. The pom.xml file is
pre-defined with Hibernate as a persistence model and Struts 2 as the web
framework.

To get started, please complete the following steps:

1. Download and install a MySQL 5.x database from 
   http://dev.mysql.com/downloads/mysql/5.0.html#downloads.

2. According to jdbc.* properties in the pom.xml, create a database and grant permissions

3. Jarvis uses Atlassian Crowd SSO to authenticate users so we also need set up 'jarvis-test.critical-factor.com'
point to localhost. Usually we can add it to /etc/hosts file.

4. Run "mvn jetty:run" and view the application at http://jarvis-test.critical-factor.com:8080.

5. More information can be found at:

   http://appfuse.org/display/APF/AppFuse+QuickStart

6. Download and install Cassandra from
   http://cassandra.apache.org/download/

   create keyspace named "jarvis"

