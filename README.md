#Fermenter#
In brewing, a fermenter is a vessel in which unfinished ingredients become nearly finished beer. In Model Driven Architecture, Fermenter is a project that converts functional concepts into nearly finished applications. This approach allows for the quick definition and assembly of applications with the focus on functional concepts rather than technical underpinnings.

## Approach ##
The Fermenter approach is simple - it is solely intended to generate source code based on simple, developer targeted model. This is the key difference between Fermenter and other MDA tools and approaches is that it puts a premium on developer productivity rather than architectural concepts or diagrams.

![fermenter-high-level-concept.png](https://bitbucket.org/repo/rg8odx/images/2347847741-fermenter-high-level-concept.png)

### Step 1 - Define Your Model & Select a Framework ###
To get started, you define the model elements you need and select a framework to use. The model describes what business concepts you need in your application. Fermenter can model entities, services, composites, or enumerations. The framework can be anything you'd like (e.g., JEE, Spring, something home grown or legacy).

### Step 2 - Run Fermenter ###
Add our Maven plugin to your build and run your build like you normally do.

### Step 3 - Update Generated Source with Business Logic ###
Fermenter will generate source into *src/generated/<appropriate sub-folder>*, representing the concepts and framework you have configured.  Update business logic, typically in *src/main/<appropriate sub-folder>* and you're done!

## Fermenter Java ##
Fermenter's default framework, called fermenter-java, leverages JEE components.  These components use object oriented concepts to stub out locations for your business logic.  Please see *fermenter-test* sub-module for examples of all the model components in action, including the our *integration-test* maven profile that will download and execute the examples in a live Wildfly container.