# must derive from base image or FROM SCRATCH
FROM tomcat:8-jdk8

# Add the Microsoft default fonts
RUN apt-get update && apt-get install -y vim

# Sets up debugging on Tomcat. If you are debugging an startup issue and need the server to wait for you while you connect, change to suspend=y, but do not check in
RUN echo 'JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,address=8787,server=y,suspend=n -DCoordinatorEnvironmentBean.defaultTimeout=600"' >> /usr/local/tomcat/bin/setenv.sh

RUN rm -Rf /usr/local/tomcat/webapps/examples
RUN rm -Rf /usr/local/tomcat/webapps/docs

COPY tomcat /usr/local/tomcat
COPY webapps /usr/local/tomcat/webapps

