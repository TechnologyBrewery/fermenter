package org.technologybrewery.fermenter.stout.page.json;

import java.io.Serializable;

import org.technologybrewery.fermenter.stout.sort.SortWrapper;

public class FindByExampleCriteria<T> implements Serializable {
    
    private static final long serialVersionUID = -5495995215177860703L;

    private T probe;
    private Integer startPage;
    private Integer count;
    private SortWrapper sortWrapper;
    private Boolean containsMatch = false;
    
    public FindByExampleCriteria(T probe, boolean containsMatch, int startPage, int count, SortWrapper sortWrapper) {
        init(probe, containsMatch, startPage, count, sortWrapper);
    }
    
    public FindByExampleCriteria(T probe, int startPage, int count, SortWrapper sortWrapper) {
        init(probe, false, startPage, count, sortWrapper);
    }
    
    public FindByExampleCriteria(int startPage, int count, SortWrapper sortWrapper) {
        init(null, false, startPage, count, sortWrapper);
    }

    private void init(T probe, boolean containsMatch, int startPage, int count, SortWrapper sortWrapper) {
        this.probe = probe;
        this.containsMatch = containsMatch;
        this.startPage = Integer.valueOf(startPage);
        this.count = Integer.valueOf(count);
        this.sortWrapper = sortWrapper;
    }

    public FindByExampleCriteria() {
    }

    public T getProbe() {
        return probe;
    }

    public void setProbe(T probe) {
        this.probe = probe;
    }
    
    public Boolean getContainsMatch() {
        return containsMatch;
    }
    
    public void setContainsMatch(Boolean containsMatch) {
        this.containsMatch = containsMatch;
    }
    
    public Integer getStartPage() {
        return startPage;
    }

    public void setStartPage(Integer startPage) {
        this.startPage = startPage;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public SortWrapper getSortWrapper() {
        return sortWrapper;
    }
    
    public void setSortWrapper(SortWrapper sortWrapper) {
        this.sortWrapper = sortWrapper;
    }
}
