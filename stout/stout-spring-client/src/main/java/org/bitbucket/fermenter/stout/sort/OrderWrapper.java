package org.bitbucket.fermenter.stout.sort;

public class OrderWrapper {

    public static final String ASC = "asc";
    public static final String DESC = "desc";
    private String property;
    private String direction;
    private Boolean ignoreCase;
    
    public OrderWrapper(String direction, String property) {
        this.direction = direction;
        this.property = property;
    }
    
    public String getProperty() {
        return property;
    }
    public void setProperty(String property) {
        this.property = property;
    }
    public String getDirection() {
        return direction;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }
    
    public void setIgnoreCase(Boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }
}
