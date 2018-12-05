package org.bitbucket.askllc.fermenter.stout;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.bitbucket.fermenter.stout.messages.Messages;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class BatchMessageHandlingSteps {
	
	private MockBatchProcessor batchProcessor = new MockBatchProcessor();
	private Messages postErrorStepMessages = null;
	private Messages postSuccessStepMessages = null;

	@When("^a batch step that results in an error message is executed$")
	public void a_batch_step_that_results_in_an_error_message_is_executed() throws Throwable {
		batchProcessor.runBatchStepThatProducesAndError();
		postErrorStepMessages = MessageManager.getMessages();
		batchProcessor.moveToBatchJobMessageTrackingAndClearForNextBatchItem();
		
	}
	
	@When("^a batch step that result has no errors is executed$")
	public void a_batch_step_that_result_has_no_errors_is_executed() throws Throwable {
		batchProcessor.runBatchStepThatSucceeds();
		postSuccessStepMessages = MessageManager.getMessages();
		batchProcessor.moveToBatchJobMessageTrackingAndClearForNextBatchItem();
	}
	
	@Then("^the error step should process with an error message returned$")
	public void the_error_step_should_process_with_an_error_message_returned() throws Throwable {
	    assertTrue(postErrorStepMessages.hasErrorMessages());
	}

	@Then("^the success step should process without an error message returned$")
	public void the_success_step_should_process_without_an_error_message_returned() throws Throwable {
	    assertFalse(postSuccessStepMessages.hasErrorMessages());
	}	
	
}
