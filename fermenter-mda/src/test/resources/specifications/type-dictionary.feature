@typeDictionary 
Feature: Specify dictionary types to support model-driven file generation 

Scenario Outline: Create a valid type dictionary file 
	Given a dictionary type described by "<name>", "<type>" 
	When dictionary type file is read 
	Then a valid dictionary type is available and can be looked up by name "<name>" 
	
	Examples: 
		| name		| type		|
		| zip_code	| string	|
		| ssn	 	| string	|
		
Scenario Outline: Dictionary Type has valid format criteria 
	Given a dictionary type described by "<name>", "<type>", "<format>" 
	When dictionary type file is read 
	Then a valid dictionary type is available and can be looked up by name "<name>" 
	
	Examples: 
		| name		| type		| format								|
		| zip_code	| string	| ^[0-9]{5}(?:-[0-9]{4})?$,^[A-Z]{5}$	|
		| someType 	| string	| 12345									|
		
Scenario Outline: Dictionary Type name is not provided 
	Given a dictionary type described by "<name>", "<type>" 
	When dictionary type file is read 
	Then the generator throws an exception about invalid dictionary type metadata 
	
	Examples: 
		| name		| type		|
		|			| string	|
		|			| boolean	|
		
Scenario Outline: Dictionary Type type is not provided 
	Given a dictionary type described by "<name>", "<type>" 
	When dictionary type file is read 
	Then the generator throws an exception about invalid dictionary type metadata 
	
	Examples: 
		| name		| type		|
		| zip_code	|			|
		| zip_code2	|			|

Scenario Outline: Dictionary Type has invalid format criteria 
	Given a dictionary type described by "<name>", "<type>", "<format>" 
	When dictionary type file is read 
	Then the generator throws an exception about invalid dictionary type metadata

	# the format value here is actually a bunch of spaces. While format is not required, but
	# if one is specified, it must be non-empty
	Examples: 
		| name		| type		| format	|
		| zip_code	| string	|           | 
