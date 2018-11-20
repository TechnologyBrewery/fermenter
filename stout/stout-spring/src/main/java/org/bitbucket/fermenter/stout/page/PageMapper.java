package org.bitbucket.fermenter.stout.page;

import org.springframework.data.domain.Page;

public class PageMapper<T> {

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
    
}
