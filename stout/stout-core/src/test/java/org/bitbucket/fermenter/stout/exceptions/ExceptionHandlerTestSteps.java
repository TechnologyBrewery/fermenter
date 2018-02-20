package org.bitbucket.fermenter.stout.exception;

import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.fail;

public class ExceptionHandlerTestSteps {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerTestSteps.class);

    private ExceptionHandler fermenterExceptionHandler = new ExceptionHandler();
    private Exception exception = null;


    @Before
    public void clearExceptions(){
        exception = null;
    }

    @Test
    @When("^given an exception of type \"([^\"]*)\" with arguments \"([^\"]*)\"$")
    public void createExceptionOfType(String exceptionType, String arguments){

        try {

            Class<?> exceptionClass = Class.forName(exceptionType);

            Constructor<?> exceptionConstructor = exceptionClass.getConstructor();

            exception = (Exception) exceptionConstructor.newInstance();

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException |
                InvocationTargetException exception){

            LOGGER.error("Test step for " + exceptionType + " failed ", exception);

            fail("Exception Handler test failed to find exception of type: " + exceptionType.trim());
        }
    }


    @Then("the exception handler should throw a RecoverableException")
    public void testRecoverableException() {

        try {
            fermenterExceptionHandler.handleException(exception);

        } catch(Exception e) {

            if(e.getClass() != RecoverableException.class) {
                fail("Recoverable exception test did not catch exception a RecoverableException");
            }
        }
    }


    @Then("the exception handler should throw an UnrecoverableException")
    public void testUnrecoverableExceptions(){

        try {
            fermenterExceptionHandler.handleException(exception);

        } catch(Exception e) {

            if(e.getClass() != UnrecoverableException.class) {
                fail("Unecoverable exception test did not catch exception a UnrecoverableException");
            }
        }
    }
}
