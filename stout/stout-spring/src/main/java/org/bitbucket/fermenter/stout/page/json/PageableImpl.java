package org.bitbucket.fermenter.stout.page.json;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableImpl implements Pageable {

    private int pageNumber;
    private int pageSize;
    private int offset;
    private Sort sort;
    private Pageable next;
    private Pageable previousOrFirst;
    private Pageable first;
    private boolean previous;
    
    public PageableImpl(int pageNumber, int pageSize, int offset, boolean previous, Sort sort) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.offset = offset;
        this.previous = previous;
        this.sort = sort;
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return next;
    }

    @Override
    public Pageable previousOrFirst() {
        return previousOrFirst;
    }

    @Override
    public Pageable first() {
        return first;
    }

    @Override
    public boolean hasPrevious() {
        return previous;
    }

}
