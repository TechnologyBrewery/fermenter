package org.bitbucket.fermenter.stout.page.json;

import java.io.Serializable;

import org.bitbucket.fermenter.stout.sort.SortWrapper;

public class FindByExampleCriteria<T> implements Serializable {
    
    private static final long serialVersionUID = -5495995215177860703L;

    private T probe;
    private Integer page;
    private Integer size;
    private SortWrapper sortWrapper;
    private Boolean containsMatch = false;
    
    public FindByExampleCriteria(T probe, boolean containsMatch, int page, int size, SortWrapper sortWrapper) {
        init(probe, containsMatch, page, size, sortWrapper);
    }
    
    public FindByExampleCriteria(T probe, int page, int size, SortWrapper sortWrapper) {
        init(probe, false, page, size, sortWrapper);
    }
    
    public FindByExampleCriteria(int page, int size, SortWrapper sortWrapper) {
        init(null, false, page, size, sortWrapper);
    }

    private void init(T probe, boolean containsMatch, int page, int size, SortWrapper sortWrapper) {
        this.probe = probe;
        this.containsMatch = containsMatch;
        this.page = new Integer(page);
        this.size = new Integer(size);
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

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public SortWrapper getSortWrapper() {
        return sortWrapper;
    }
    
    public void setSortWrapper(SortWrapper sortWrapper) {
        this.sortWrapper = sortWrapper;
    }
}
