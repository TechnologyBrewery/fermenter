package org.bitbucket.fermenter.stout.page;

import org.bitbucket.fermenter.stout.sort.OrderWrapper;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public class OrderMapper {

    public static final Order mapToOrder(OrderWrapper orderWrapper) {
        Direction springDirection = Direction.fromString(orderWrapper.getDirection());

        Order order = null;
        if (orderWrapper.isIgnoreCase()) {
            order = new Order(springDirection, orderWrapper.getProperty()).ignoreCase();
        } else {
            order = new Order(springDirection, orderWrapper.getProperty());
        }
        return order;
    }
}
