package org.technologybrewery.fermenter.stout.authz;

import org.technologybrewery.fermenter.stout.authz.json.PolicyRequest;

public class TokenDataInput extends PolicyRequest {

    private String subject;
    private String audience;
    private String result;
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getAudience() {
        return audience;
    }
    
    public void setAudience(String audience) {
        this.audience = audience;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
}
