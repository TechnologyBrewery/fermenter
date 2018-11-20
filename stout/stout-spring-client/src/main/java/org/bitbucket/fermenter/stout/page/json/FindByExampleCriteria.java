package org.bitbucket.fermenter.stout.page.json;

import java.io.Serializable;

import org.bitbucket.fermenter.stout.sort.SortWrapper;

public class FindByExampleCriteria<T> implements Serializable {
    
    private static final long serialVersionUID = -5495995215177860703L;

    private T probe;
    private Integer page;
    private Integer size;
    private SortWrapper sortWrapper;

    public FindByExampleCriteria(T probe, Integer page, Integer size, SortWrapper sortWrapper) {
        this.probe = probe;
        this.page = page;
        this.size = size;
        this.sortWrapper = sortWrapper;
    }
    
    public FindByExampleCriteria(T probe, int page, int size, SortWrapper sortWrapper) {
        this.probe = probe;
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
