# Overview #
the `fermenter-mda` plugin allows the generation of code via entities, services, and enumerations.

# Configuring the fermenter-mda plugin #
There are three ways in which to leverage the `fermenter-mda` plugin:
1. With local metadata
1. With external metadata
1. With a mixture of the two.  (NOTE: in the future, they will be "mashable" in a more intelligent fashion)

## With Local Metadata  ##
In the following snippet shows the `fermenter-mda` plugin within a Maven build.  The major points are:
* configuration -> basePackage: specify the base package for generated sources.  In this case, all files generated will have a package that starts with `org.bitbucket.askllc.fermenter.cookbook.domain`
* configuration -> profile: specify the generation 'profile' that will control what exactly gets generated
* dependency -> a dependency that contains the needed profile.xml and associated targets.xml files used for `fermenter-mda` generation 
```
#!xml
	<plugin>
		<groupId>org.bitbucket.askllc.fermenter</groupId>
		<artifactId>fermenter-mda</artifactId>
		<version>${fermenter.version}</version>
		<configuration>
			<basePackage>org.bitbucket.askllc.fermenter.cookbook.domain</basePackage>
			<profile>cookbook-domain</profile>
		</configuration>
		<dependencies>
			<dependency>
				<groupId>org.bitbucket.askllc.fermenter</groupId>
				<artifactId>stout-cookbook-profile</artifactId>
				<version>${fermenter.version}</version>
			</dependency>
		</dependencies>
	</plugin>

```

## With External Metadata ##
In the following snippet shows the `fermenter-mda` plugin within a Maven build.  The major points are:
* configuration -> metadataDependencies -> metadataDependency: allows you to specify an artifactId within the plugin's dependencies that contains metadata to be loaded.  If it is in the classpath, but not specified here, the metadata will NOT be loaded.
* dependencies -> dependency -> last dependency: as noted in the prior bullet - it has to be in the **plugin's classpath** to be found and listed as a metadata dependency. 
```
#!xml
	<plugin>
		<groupId>org.bitbucket.askllc.fermenter</groupId>
		<artifactId>fermenter-mda</artifactId>
		<version>${fermenter.version}</version>
		<configuration>
			<basePackage>org.bitbucket.askllc.fermenter.cookbook.domain.client</basePackage>
			<profile>cookbook-domain-client</profile>
			<metadataDependencies>
				<metadataDependency>MAVEN ARTIFACTID OF JAR CONTAINING FERMENTER METADATA</metadataDependency>
			</metadataDependencies>			
		</configuration>
		<dependencies>
			<dependency>
				<groupId>org.bitbucket.askllc.fermenter</groupId>
				<artifactId>stout-cookbook-profile</artifactId>
				<version>${fermenter.version}</version>
			</dependency>
			<dependency>
				<groupId>some.groupId</groupId>
				<artifactId>MAVEN ARTIFACTID OF JAR CONTAINING FERMENTER METADATA</artifactId>
				<version>some-version</version>
			</dependency>
		</dependencies>
	</plugin>

```

## Mixing Local and External Metadata ##
You simply combine the two approaches - it will look like the external example, but you will mix any local metadata you have in with the external metadata.  More on how to combine these soon. 