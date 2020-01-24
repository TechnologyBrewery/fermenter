@localInvocationOfRemoteMaintenanceService
Feature: Local Invocation of Remote Maintenance Service
  
  Tests the ability to have a local call binding for maintenance delegates that allows two domains projects to be co-deployed in order to short-circuit remoting

  # Most, if not all, scenarios in this file should also be added to stout-cookbook-domain-client's invocation-of-remote-maintenance-service.feature
  
  Scenario: test local optimization of a create and retrieve call
    When a create service is invoked
    Then the newly created instance can be retrieved

  Scenario: test local optimization of an update
    Given an existing instance of an entity
    When the entity is updated via the maintainence service
    Then the update can be retrieved

  Scenario: test local optimization of a delete
    Given an existing instance of an entity
    When the entity is deleted via the maintainence service
    Then the entity can not longer be retrieved

  Scenario: test local optimization of a bulk create and find by example call
    When a bulk create service is invoked
    Then the newly created instances can be retrieved via find by example

  Scenario: test local optimization of a bulk update
    Given existing instances of an entity
    When the entities are updated via the bulk maintainence service
    Then the updates can be retrieved

  Scenario: test local optimization of a bulk delete
    Given existing instances of an entity
    When the entities are deleted via the bulk maintainence service
    Then then none of the entities can be retrieved
