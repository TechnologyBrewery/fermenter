package org.bitbucket.fermenter.mda.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;

/**
 * Simple tracking class to allow bulk collection and reporting of error messages during the generation
 * process.  This allows us to collect a maximum number of issues without stopping the generation process
 * and forcing multiple executions to work through errors one at a time.
 */
public class MessageTracker {

    private static MessageTracker singletonInstance = new MessageTracker();
    
    boolean hasErrors;
    private List<Message> orderedMessages = new ArrayList<>();
    
    
    private MessageTracker() {
        //private constructor to prevent instantiation
    }
    
    /**
     * Returns the singleton instance of this class.
     * @return 
     */
    public static MessageTracker getInstance() {
        return singletonInstance;
    }
    
    /**
     * Clears the contents of the tracker.
     */
    public void clear() {
        hasErrors = false;
        orderedMessages.clear();
    }

    /**
     * Adds an error message to the tracker.
     * @param errorMessage The message to add
     */
    public void addErrorMessage(String errorMessage) {
        orderedMessages.add(new ErrorMessage(errorMessage));
        hasErrors = true;
    }
    
    /**
     * Adds a warning message to the tracker.
     * @param warningMessage The message to add
     */
    public void addWarningMessage(String warningMessage) {
        orderedMessages.add(new WarningMessage(warningMessage));
    }    
    
    /**
     * Returns whether or not errors have been added to the tracker.
     * @return true if error exist
     */
    public boolean hasErrors() {
        return hasErrors;
    }
    
    /**
     * Logs all messages that have been encountered. 
     */
    public void emitMessages(Log log) {
        for (Message message : orderedMessages) {
            message.log(log);
        }
    }
    
    abstract class Message {
        
        protected String value;
        
        public Message(String value) {
            this.value = value;
        }
        
        abstract void log(Log log);
        
    }
    
    class WarningMessage extends Message {
        
        public WarningMessage(String value) {
            super(value);
        }
        
       void log(Log log) {
           log.warn(value);
       }
        
    }
    
    class ErrorMessage extends Message {
        
        public ErrorMessage(String value) {
            super(value);
        }
        
       void log(Log log) {
           log.error(value);
       }
        
    }    
    
}
