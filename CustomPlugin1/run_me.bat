@echo run me first to build this little gradle plugin and deploy it to local maven repo (USER_HOME/.m2)
@echo Upon successful execution, we will see
@echo Uploading: com/vogella/gradle/exampleplugin/maven-metadata.xml to repository remote 
@echo at file:/C:/Users/xxx/.m2/repository/

gradle clean uploadArchives --info