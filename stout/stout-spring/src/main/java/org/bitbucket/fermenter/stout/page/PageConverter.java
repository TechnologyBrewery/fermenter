package org.bitbucket.fermenter.stout.page;

import org.springframework.data.domain.Page;

public class PageConverter<T> {

    public PageWrapper<T> convertToPageWrapper(Page<T> page) {
        PageWrapper<T> wrapper = new PageWrapper<>();
        wrapper.setContent(page.getContent());
        wrapper.setItemsPerPage(page.getSize());
        wrapper.setFirst(page.isFirst());
        wrapper.setLast(page.isLast());
        wrapper.setStartPage(page.getNumber());
        wrapper.setNumberOfElements(page.getNumberOfElements());
        wrapper.setTotalResults(Long.valueOf(page.getTotalElements()));
        wrapper.setTotalPages(Integer.valueOf(page.getTotalPages()));
        return wrapper;
    }
}
