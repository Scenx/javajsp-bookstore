package com.scen.bookstore.domain;

/**
 * @author Scen
 * @date 2017/11/21
 */
public class OrderItem {
    /**
     * 订单
     */
    private Order order;
    /**
     * 商品
     */
    private Product p;
    /**
     * 购物数量
     */
    private int buynum;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getP() {
        return p;
    }

    public void setP(Product p) {
        this.p = p;
    }

    public int getBuynum() {
        return buynum;
    }

    public void setBuynum(int buynum) {
        this.buynum = buynum;
    }

}
