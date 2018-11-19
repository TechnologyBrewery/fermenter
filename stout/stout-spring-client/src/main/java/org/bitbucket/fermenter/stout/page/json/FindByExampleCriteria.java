package org.bitbucket.fermenter.stout.page.json;

import java.io.Serializable;

import org.springframework.data.domain.Sort;

public class FindByExampleCriteria<T> implements Serializable {
    
    private static final long serialVersionUID = -5495995215177860703L;

    private T probe;
    private Integer page;
    private Integer size;
    private Sort sort;

    public FindByExampleCriteria(T probe, Integer page, Integer size, Sort sort) {
        this.probe = probe;
        this.page = page;
        this.size = size;
        this.sort = sort;
    }
    
    public FindByExampleCriteria(T probe, int page, int size, Sort sort) {
        this.probe = probe;
        this.page = new Integer(page);
        this.size = new Integer(size);
        this.sort = sort;
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

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }
}
