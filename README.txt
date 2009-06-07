HOW TO BUILD THE PLUGIN

ATM the POM has this dependency

<dependency>
   <groupId>org.scala-tools</groupId>
   <artifactId>javautils</artifactId>
   <version>2.7.4-0.2-SNAPSHOT</version>
</dependency>

which is not published to any public repo. The easiest way to get this is
to check it out and install it in to your own local repo. It's a github project
so just clone it from here:

git://github.com/jorgeortiz85/scala-javautils.git


Building with your plugin with Maven is really easy:

- Run 'mvn compile' to compile the plugin.
- Run 'mvn test' to run the unit tests.
- Run 'mvn integration-test' to run the integration tests.
- Run 'mvn package' to produce the JAR.

For the above commands to skip the various test phases add one of the following options to the command:

    -Dmaven.test.skip=true - skips both unit and integration tests
    -Dmaven.test.unit.skip=true - skips unit tests
    -Dmaven.test.it.skip=true - skips integration tests

XML data that is usually imported into the JIRA instance by the tests should be placed in:

    src/test/xml/ directory provided
    

The following system properties are available to control the behaviour of the integration test harness:

		tomcat.installer.url - path to the tomcat zip file to install. Default: http://repository.atlassian.com/maven2/org/apache/tomcat/apache-tomcat/5.5.20/apache-tomcat-5.5.20-jdk14.zip
		
		cargo.wait - if this is set o true - tomcat will be started up and then cargo would suspend allowing you to manually access this JIRA from the browser.
		
		jira.version - version of JIRA to compile and test against. default is 3.10.2
		
		jira.data.version - version of the test resource bundle that contains the bre basic JIRA configuration data for the integration test environment. These versions mimic the actual JIRA versions. However we might only modify and release this project for the reasons of non-backwards compatibility of the new versions of JIRA. Therefore not every version of JIRA will have a corresponding version of the resource bundle. By default this is set to 3.10 which should be migrated and work correctly with the newer versions. 
		
		jira.test-lib.version - version of jira-func-tests JAR to compile and test against. default is 3.10-DEV
		
		http.port - port on which this instance of JIRA will be accessible on. Default: 8989
		
		jira.url - base URL for this instance of JIRA. Default: http://localhost:{http.port}/jira/
		rmi.port
