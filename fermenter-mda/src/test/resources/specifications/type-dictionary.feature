@typeDictionary 
Feature: Specify dictionary types to support model-driven file generation 

Scenario Outline: Create a valid type dictionary file 
	Given a dictionary type described by "<name>", "<type>" 
	When dictionary type file is read 
	Then a valid dictionary type is available and can be looked up by name "<name>" 
	
	Examples: 
		| name		| type		|
		| zip_code	| string	|
		| ssn		| string	|
		
Scenario Outline: Dictionary Type has valid format criteria 
	Given a dictionary type described by "<name>", "<type>", "<format>" 
	When dictionary type file is read 
	Then a valid dictionary type is available and can be looked up by name "<name>" 
	
	Examples: 
		| name		| type		| format								|
		| zip_code	| string	| ^[0-9]{5}(?:-[0-9]{4})?$,^[A-Z]{5}$	|
		| someType 	| string	| 12345									|
		
Scenario Outline: Dictionary Type has valid length criteria 
	Given a dictionary type described by "<name>", "<type>", "<minLength>", "<maxLength>" 
	When dictionary type file is read 
	Then a valid dictionary type is available and can be looked up by name "<name>" 
	
	Examples: 
		| name		| type		| minLength	| maxLength	|
		| someType	| string	| 1			| 5	 		|	
		| zip_code	| string	| 5			| 5			|
		| someType2	| string	| 			| 1000		|
		| someType3	| string	|500 		| 			|
		
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
		
Scenario Outline: Dictionary Type has invalid length criteria 
	Given a dictionary type described by "<name>", "<type>", "<minLength>", "<maxLength>" 
	When dictionary type file is read 
	Then the generator throws an exception about invalid dictionary type metadata 
	
	Examples: 
		| name		| type		| minLength	| maxLength	|
		| bad_zip1	| string	| 11		| 5	 		|	
		| bad_zip2	| string	| -2		| 10		|
		| bad_zip3	| string	| -5		| -1		|
		| bad_zip4	| string	| 0			| 0			|
		
Scenario Outline: Dictionary Type has invalid format criteria 
	Given a dictionary type described by "<name>", "<type>" with a blank format 
	When dictionary type file is read 
	Then the generator throws an exception about invalid dictionary type metadata 
	
	Examples: 
		| name		| type		|
		| bad_zip5	| string	|
		
Scenario: 
	Given dictionary files that are described by the following different names 
		| zip_code	|
		| ssn	 	|
		| phone		|
		| date	 	|
		| age		|
	When dictionary type file is read 
	Then there should be five dictionary types available