package org.bitbucket.fermenter.stout.page.json;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class PageMixIn<T> {

    public static final String SORT = "sort";
    public static final String CONTENT = "content";
    public static final String LAST = "last";
    public static final String NEXT_PAGE = "nextPage";
    public static final String FIRST = "first";
    public static final String PREVIOUS_PAGE = "previousPage";
    public static final String TOTAL_ELEMENTS = "totalElements";
    public static final String NUMBER_OF_ELEMENTS = "numberOfElements";
    public static final String TOTAL_PAGES = "totalPages";
    public static final String SIZE = "size";
    public static final String NUMBER = "number";
    
    @JsonProperty(NUMBER)
    abstract int getNumber();
    
    @JsonProperty(SIZE)
    abstract int getSize();
    
    @JsonProperty(TOTAL_PAGES)
    abstract int getTotalPages();
    
    @JsonProperty(NUMBER_OF_ELEMENTS)
    abstract int getNumberOfElements();
    
    @JsonProperty(TOTAL_ELEMENTS)
    abstract long getTotalElements();
    
    @JsonProperty(PREVIOUS_PAGE)
    abstract boolean getPreviousPage();
    
    @JsonProperty(FIRST)
    abstract boolean getFirst();
    
    @JsonProperty(NEXT_PAGE)
    abstract boolean getNextPage();
    
    @JsonProperty(LAST)
    abstract boolean getLast();
    
    @JsonProperty(CONTENT)
    abstract List<T> getContent();
    
    @JsonProperty(SORT)
    abstract Sort getSort();
}
