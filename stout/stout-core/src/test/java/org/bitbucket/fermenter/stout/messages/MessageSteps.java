package org.bitbucket.fermenter.stout.messages;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bitbucket.fermenter.stout.exception.UnrecoverableException;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MessageSteps {

	private Message messageOne;
	private Message messageTwo;
	private Boolean areEqual;
	private Boolean hasSameHashCode;

	@Given("^the following messages:$")
	public void the_following_messages(List<MessageInput> messageInputs) throws Throwable {
		if (messageInputs.size() > 2) {
			throw new UnrecoverableException("Only designed to handle two messages!");
		}		
		
		for (MessageInput messageInput : messageInputs) {
		    MockMetaMessage metaMessage = new MockMetaMessage(messageInput.messageName, null);
		    
			Message message = new Message(metaMessage);
			message.addInsert(messageInput.insertName, messageInput.insertValue);
			
			if (messageInput.severity == null) {
				message.setSeverity(Severity.ERROR);
				
			} else {
				message.setSeverity(Severity.valueOf(messageInput.severity));
				
			}

			if (messageOne == null) {
				messageOne = message;
			} else {
				messageTwo = message;
			}
		}
	}

	@When("^the messages are compared for equality$")
	public void the_messages_are_compared_for_equality() throws Throwable {
		areEqual = messageOne.equals(messageTwo);
	}
	
	@When("^the messages hash codes are compared$")
	public void the_messages_hash_codes_are_compared() throws Throwable {
		hasSameHashCode = messageOne.hashCode() == messageTwo.hashCode();
	}	

	@Then("^they are equal$")
	public void they_are_equal() throws Throwable {
		assertTrue("Messages SHOULD have been equal!", areEqual);
	}

	@Then("^they are not equal$")
	public void they_are_not_equal() throws Throwable {
		assertFalse("Messages SHOULD NOT have been equal!", areEqual);
	}
	
	@Then("^they have identical hash codes$")
	public void they_have_identical_hash_codes() throws Throwable {
		assertTrue("Messages SHOULD have the same hash code!", hasSameHashCode);
	}

	@Then("^they have different hash codes$")
	public void they_have_different_hash_codes() throws Throwable {
		assertFalse("Messages SHOULD NOT have the same hash code!", hasSameHashCode);
	}	

	public class MessageInput {
		public String messageName;
		public String insertName;
		public String insertValue;
		public String severity;

	}
	
	public class MockMetaMessage implements MetaMessage {
	    
	    private String name;
	    private String text;
	    
	    public MockMetaMessage(String name, String text) {
	        this.name = name;
	        this.text = text;
	    }
	    
        @Override
        public String getText() {
            return text;
        }
        
        @Override
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            return EqualsBuilder.reflectionEquals(this, o, true);

        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this, true);
        }
        
        
	    
	}

}
