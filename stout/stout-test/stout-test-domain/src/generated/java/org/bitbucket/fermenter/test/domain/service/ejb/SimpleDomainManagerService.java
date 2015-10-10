package org.bitbucket.fermenter.test.domain.service.ejb;

import java.lang.Integer;
import java.lang.String;
import java.util.Collection;
import java.util.List;
import org.bitbucket.fermenter.test.domain.transfer.SimpleDomain;

import org.bitbucket.fermenter.test.domain.transfer.*;

import javax.ejb.Local;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.bitbucket.fermenter.service.ValueServiceResponse;
import org.bitbucket.fermenter.service.VoidServiceResponse;

/**
 * Interface for the SimpleDomainManager service.
 * 
 * Generated Code - DO NOT MODIFY
 */
@Local
@Path("/")
public interface SimpleDomainManagerService {

	/**
	 * Execute the GetSimpleDomainCount operation.
	 * 
	 * @return A service response-wrapped instance of {@link Long}
	 */
	@GET
	@Path("/SimpleDomainManagerService/getSimpleDomainCount")
	@Produces(MediaType.APPLICATION_JSON)
	ValueServiceResponse<Long> getSimpleDomainCount();

	/**
	 * Execute the DoSomething operation.
	 * 
	 * @return An empty service response
	 */
	@POST
	@Path("/SimpleDomainManagerService/doSomething")
	@Produces(MediaType.APPLICATION_JSON)
	public VoidServiceResponse doSomething();

	/**
	 * Returns your string concatenated with another, perhaps cooler, one
	 * 
	 * @param echoRoot
	 * @return A service response-wrapped instance of {@link String}
	 */
	@POST
	@Path("/SimpleDomainManagerService/echoPlusWazzup")
	@Produces(MediaType.APPLICATION_JSON)
	ValueServiceResponse<String> echoPlusWazzup(@QueryParam("echoRoot") String echoRoot);

	/**
	 * Execute the CreateAndPropagateErrorMessages operation.
	 * 
	 * @param numErrorMessagesToGenerate
	 * @return An empty service response
	 */
	@POST
	@Path("/SimpleDomainManagerService/createAndPropagateErrorMessages")
	@Produces(MediaType.APPLICATION_JSON)
	public VoidServiceResponse createAndPropagateErrorMessages(Integer numErrorMessagesToGenerate);

	/**
	 * Execute the SaveSimpleDomainEntityAndPropagateErrorMessages operation.
	 * 
	 * @param targetNameValue
	 * @param numErrorMessagesToGenerate
	 * @return A service response-wrapped instance of {@link SimpleDomain}
	 */
	@POST
	@Path("/SimpleDomainManagerService/saveSimpleDomainEntityAndPropagateErrorMessages")
	@Produces(MediaType.APPLICATION_JSON)
	ValueServiceResponse<SimpleDomain> saveSimpleDomainEntityAndPropagateErrorMessages(
			@QueryParam("targetNameValue") String targetNameValue,
			@QueryParam("numErrorMessagesToGenerate") Integer numErrorMessagesToGenerate);

	/**
	 * An operation that demonstrates some important business function. Returns the altered domain..
	 * 
	 * @param someBusinessEntity
	 *            The entity on which we should perform some business operation
	 * @param otherImportantData
	 *            Critical data to supplement the business entity
	 * @return A service response-wrapped instance of {@link SimpleDomain}
	 */
	@POST
	@Path("/SimpleDomainManagerService/someBusinessOperation")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	ValueServiceResponse<SimpleDomain> someBusinessOperation(SimpleDomain someBusinessEntity,
			@QueryParam("otherImportantData") String otherImportantData);

	/**
	 * Execute the SelectAllSimpleDomains operation.
	 * 
	 * @return A service response-wrapped {@link Collection>} of {@link SimpleDomain}
	 */
	@GET
	@Path("/SimpleDomainManagerService/selectAllSimpleDomains")
	@Produces(MediaType.APPLICATION_JSON)
	ValueServiceResponse<Collection<SimpleDomain>> selectAllSimpleDomains();

	/**
	 * Execute the DoSomethingAndReturnACharacter operation.
	 * 
	 * @return A service response-wrapped instance of {@link String}
	 */
	@POST
	@Path("/SimpleDomainManagerService/doSomethingAndReturnACharacter")
	@Produces(MediaType.APPLICATION_JSON)
	ValueServiceResponse<String> doSomethingAndReturnACharacter();

	/**
	 * Execute the Count operation.
	 * 
	 * @param input
	 * @return A service response-wrapped instance of {@link Integer}
	 */
	@POST
	@Path("/SimpleDomainManagerService/count")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	ValueServiceResponse<Integer> count(List<SimpleDomain> input);

	/**
	 * Execute the ReturnSingleSimpleDomain operation.
	 * 
	 * @return A service response-wrapped instance of {@link SimpleDomain}
	 */
	@GET
	@Path("/SimpleDomainManagerService/returnSingleSimpleDomain")
	@Produces(MediaType.APPLICATION_JSON)
	ValueServiceResponse<SimpleDomain> returnSingleSimpleDomain();

	/**
	 * Execute the SelectAllSimpleDomainsByType operation.
	 * 
	 * @param type
	 * @return A service response-wrapped {@link Collection>} of {@link SimpleDomain}
	 */
	@GET
	@Path("/SimpleDomainManagerService/selectAllSimpleDomainsByType")
	@Produces(MediaType.APPLICATION_JSON)
	ValueServiceResponse<Collection<SimpleDomain>> selectAllSimpleDomainsByType(@QueryParam("type") String type);

	/**
	 * Execute the ReturnNullEntity operation.
	 * 
	 * @return A service response-wrapped instance of {@link SimpleDomain}
	 */
	@POST
	@Path("/SimpleDomainManagerService/returnNullEntity")
	@Produces(MediaType.APPLICATION_JSON)
	ValueServiceResponse<SimpleDomain> returnNullEntity();

}