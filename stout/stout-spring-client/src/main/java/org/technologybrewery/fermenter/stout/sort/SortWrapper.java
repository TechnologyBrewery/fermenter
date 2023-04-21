package org.technologybrewery.fermenter.stout.sort;

import java.util.ArrayList;
import java.util.List;

public class SortWrapper {

    private List<OrderWrapper> orders;

    private SortWrapper() {
        // needed for resteasy
    }

    public SortWrapper(String direction, String property) {
        orders = new ArrayList<>();
        orders.add(new OrderWrapper(direction, property));
    }

    public SortWrapper(String property) {
        orders = new ArrayList<>();
        orders.add(new OrderWrapper(property));
    }

    public SortWrapper(OrderWrapper order) {
        orders = new ArrayList<>();
        orders.add(order);
    }

    public SortWrapper(List<OrderWrapper> orders) {
        this.orders = orders;
    }

    public List<OrderWrapper> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderWrapper> orders) {
        this.orders = orders;
    }

}
