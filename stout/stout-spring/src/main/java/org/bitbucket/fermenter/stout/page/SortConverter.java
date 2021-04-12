package org.bitbucket.fermenter.stout.page;

import java.util.ArrayList;
import java.util.List;

import org.bitbucket.fermenter.stout.sort.OrderWrapper;
import org.bitbucket.fermenter.stout.sort.SortWrapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class SortConverter {

    
    public static final Sort convertToSpringSort(SortWrapper sortWrapper) {
        List<Order> orders = new ArrayList<>();
        for (OrderWrapper orderWrapper : sortWrapper.getOrders()) {
            orders.add(OrderConverter.convertToSpringOrder(orderWrapper));
        }
        return Sort.by(orders);
    }
}
