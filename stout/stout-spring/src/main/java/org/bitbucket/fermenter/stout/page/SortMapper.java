package org.bitbucket.fermenter.stout.page;

import java.util.ArrayList;
import java.util.List;

import org.bitbucket.fermenter.stout.sort.OrderWrapper;
import org.bitbucket.fermenter.stout.sort.SortWrapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class SortMapper {

    
    public static final Sort mapToSort(SortWrapper sortWrapper) {
        List<Order> orders = new ArrayList<>();
        for (OrderWrapper orderWrapper : sortWrapper.getOrders()) {
            orders.add(OrderMapper.mapToOrder(orderWrapper));
        }
        return new Sort(orders);
    }
}
