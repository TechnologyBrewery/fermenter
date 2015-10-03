# Fermenter Test #

The following project tests Fermenter by using out Java framework to create a deployable and runnable set of services. The modules works as follows:

* *fermenter-test-profile*: defines a profile of templates, which is the set of templates that will be run when the profile is referenced in a the Maven Fermenter MDA plugin
* *fermenter-test-domain*: wires up the Fermenter MDA Maven plugin with the profile defined above to generate the source code needed to deploy an application.
* *fermenter-test-webapp*: a manually deployable web application (i.e., war) that contains the fermenter-test-domain