package org.bitbucket.askllc.fermenter.cookbook.domain.service.rest;

import java.lang.Integer;
import java.lang.String;
import java.util.Collection;
import java.util.List;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainBO;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.bitbucket.fermenter.stout.service.ValueServiceResponse;
import org.bitbucket.fermenter.stout.service.VoidServiceResponse;

/**
 * Interface for the SimpleDomainManager service.
 * 
 * Generated Code - DO NOT MODIFY
 */
@Path("/")
public interface SimpleDomainManagerBaseService {

	/**
	 * Execute the DeleteAllSimpleDomains operation.
	 * 
	 * @return An empty service response
	 */
	@POST
	@Path("/SimpleDomainManagerService/deleteAllSimpleDomains")
	@Produces("application/json;charset=UTF-8")
	public VoidServiceResponse deleteAllSimpleDomains();

	/**
	 * Returns the number of persistent SimpleDomains that exist
	 * 
	 * @return A service response-wrapped instance of {@link Integer}
	 */
	@GET
	@Path("/SimpleDomainManagerService/getSimpleDomainCount")
	@Produces("application/json;charset=UTF-8")
	ValueServiceResponse<Integer> getSimpleDomainCount();

	/**
	 * Returns your string concatenated with another, perhaps cooler, one
	 * 
	 * @param echoRoot
	 * @return A service response-wrapped instance of {@link String}
	 */
	@POST
	@Path("/SimpleDomainManagerService/echoPlusWazzup")
	@Produces("application/json;charset=UTF-8")
	ValueServiceResponse<String> echoPlusWazzup(@QueryParam("echoRoot") String echoRoot);

	/**
	 * Execute the CreateAndPropagateErrorMessages operation.
	 * 
	 * @param numErrorMessagesToGenerate
	 * @return An empty service response
	 */
	@POST
	@Path("/SimpleDomainManagerService/createAndPropagateErrorMessages")
	@Produces("application/json;charset=UTF-8")
	public VoidServiceResponse createAndPropagateErrorMessages(
			@QueryParam("numErrorMessagesToGenerate") Integer numErrorMessagesToGenerate);

	/**
	 * Execute the SaveSimpleDomainEntityAndPropagateErrorMessages operation.
	 * 
	 * @param targetNameValue
	 * @param numErrorMessagesToGenerate
	 * @return A service response-wrapped instance of {@link SimpleDomainBO}
	 */
	@POST
	@Path("/SimpleDomainManagerService/saveSimpleDomainEntityAndPropagateErrorMessages")
	@Produces("application/json;charset=UTF-8")
	ValueServiceResponse<SimpleDomainBO> saveSimpleDomainEntityAndPropagateErrorMessages(
			@QueryParam("targetNameValue") String targetNameValue,
			@QueryParam("numErrorMessagesToGenerate") Integer numErrorMessagesToGenerate);

	/**
	 * An operation that demonstrates some important business function. Returns the altered domain..
	 * 
	 * @param someBusinessEntity
	 *            The entity on which we should perform some business operation
	 * @param otherImportantData
	 *            Critical data to supplement the business entity
	 * @return A service response-wrapped instance of {@link SimpleDomainBO}
	 */
	@POST
	@Path("/SimpleDomainManagerService/someBusinessOperation")
	@Produces("application/json;charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	ValueServiceResponse<SimpleDomainBO> someBusinessOperation(SimpleDomainBO someBusinessEntity,
			@QueryParam("otherImportantData") String otherImportantData);

	/**
	 * Returns the number of provided SimpleDomain entities
	 * 
	 * @param input
	 * @return A service response-wrapped instance of {@link Integer}
	 */
	@POST
	@Path("/SimpleDomainManagerService/countNumInputs")
	@Produces("application/json;charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	ValueServiceResponse<Integer> countNumInputs(List<SimpleDomainBO> input);

	/**
	 * Execute the SelectAllSimpleDomains operation.
	 * 
	 * @return A service response-wrapped {@link Collection>} of {@link SimpleDomainBO}
	 */
	@GET
	@Path("/SimpleDomainManagerService/selectAllSimpleDomains")
	@Produces("application/json;charset=UTF-8")
	ValueServiceResponse<Collection<SimpleDomainBO>> selectAllSimpleDomains();

	/**
	 * Execute the SelectAllSimpleDomainsWithPaging operation.
	 * 
	 * @param startPage
	 *            0-based start page
	 * @param pageSize
	 *            Maximum number of results to return
	 * @return A service response-wrapped {@link Collection>} of {@link SimpleDomainBO}
	 */
	@GET
	@Path("/SimpleDomainManagerService/selectAllSimpleDomainsWithPaging")
	@Produces("application/json;charset=UTF-8")
	ValueServiceResponse<Collection<SimpleDomainBO>> selectAllSimpleDomainsWithPaging(
			@QueryParam("startPage") Integer startPage, @QueryParam("pageSize") Integer pageSize);

	/**
	 * Execute the SelectAllSimpleDomainsByType operation.
	 * 
	 * @param type
	 * @return A service response-wrapped {@link Collection>} of {@link SimpleDomainBO}
	 */
	@GET
	@Path("/SimpleDomainManagerService/selectAllSimpleDomainsByType")
	@Produces("application/json;charset=UTF-8")
	ValueServiceResponse<Collection<SimpleDomainBO>> selectAllSimpleDomainsByType(@QueryParam("type") String type);

	/**
	 * Execute the ReturnNullEntity operation.
	 * 
	 * @return A service response-wrapped instance of {@link SimpleDomainBO}
	 */
	@POST
	@Path("/SimpleDomainManagerService/returnNullEntity")
	@Produces("application/json;charset=UTF-8")
	ValueServiceResponse<SimpleDomainBO> returnNullEntity();

}