package org.bitbucket.fermenter.stout.page;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageWrapper<T> {
//    public Integer getSize();
//    public Integer getNumber();
//    public Integer getTotalPages();
//    
//    public Integer getNumberOfElements();    
//    public Long getTotalElements();
//    
//    public Boolean getFirst();
//    public Boolean getLast();
//    public List<T> getContent();
    
    public PageWrapper<T> mapToPageWrapper(Page page) {
        PageWrapper<T> wrapper = new PageWrapper<T>();
        wrapper.setContent(page.getContent());
        wrapper.setSize(page.getSize());
        wrapper.setFirst(page.isFirst());
        wrapper.setLast(page.isLast());
        wrapper.setNumber(page.getNumber());
        wrapper.setNumberOfElements(page.getNumberOfElements());
        wrapper.setTotalElements(Long.valueOf(page.getTotalElements()));
        wrapper.setTotalPages(Integer.valueOf(page.getTotalPages()));
        return wrapper;
    }

    private List<T> content = new ArrayList<T>();
    private Integer size;
    private Integer number;
    private Integer totalPages;
    private Long totalElements;
    private Boolean isFirst;
    private Boolean isLast;
    private Integer numberOfElements;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Boolean getFirst() {
        return isFirst;
    }

    public void setFirst(Boolean first) {
        isFirst = first;
    }

    public Boolean getLast() {
        return isLast;
    }

    public void setLast(Boolean last) {
        isLast = last;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public void setNumberOfElements(Integer numberOfElements) {
        this.numberOfElements = numberOfElements;
    }
    
    public Integer getNumberOfElements() {
        return numberOfElements;
    }
}
