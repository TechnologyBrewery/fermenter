package org.bitbucket.fermenter.stout.sort;

import java.util.ArrayList;
import java.util.List;

public class SortWrapper {

    private List<OrderWrapper> orders;

    public SortWrapper(String direction, String property) {
        orders = new ArrayList<>();
        orders.add(new OrderWrapper(direction, property));
    }

    public List<OrderWrapper> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderWrapper> orders) {
        this.orders = orders;
    }

}
