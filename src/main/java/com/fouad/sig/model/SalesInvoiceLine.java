/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fouad.sig.model;

import java.math.BigDecimal;

public class SalesInvoiceLine {

    int no = 0;
    private int itemCount = 0;
    private String itemName = "";
    private BigDecimal itemPrice = BigDecimal.ZERO;
    private BigDecimal itemTotal = BigDecimal.ZERO;
    private int invNo = 0;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public BigDecimal getItemTotal() {
        return itemPrice.multiply(new BigDecimal(itemCount));
    }

    public void setItemTotal(BigDecimal itemTotal) {
        this.itemTotal = itemTotal;
    }

    public int getInvNo() {
        return invNo;
    }

    public void setInvNo(int invNo) {
        this.invNo = invNo;
    }

    public void Set(SalesInvoiceLine itemLine) {
        no = itemLine.getNo();
        itemCount = itemLine.getItemCount();
        itemName = itemLine.getItemName();
        itemPrice = itemLine.getItemPrice();
        //itemTotal = itemLine.getItemTotal();
    }

    public String ToCSVLine(int invoiceNo) {

        StringBuilder builder = new StringBuilder();

        builder.append(no).append(',');
        builder.append(itemName).append(',');
        builder.append(String.format("%.2f", itemPrice)).append(',');
        builder.append(itemCount).append(',');
        builder.append(invoiceNo);
        //builder.append(String.format("%.2f", itemTotal));

        return builder.toString();
    }

    public static SalesInvoiceLine FromLine(String[] line) {
        var invItem = new SalesInvoiceLine();

        if (line[0].isEmpty() || line[0].isBlank()) {
            return null;
        }

        invItem.setNo(Integer.parseInt(line[0]));
        invItem.setItemName(line[1]);
        invItem.setItemPrice(new BigDecimal(line[2]));
        invItem.setItemCount(Integer.parseInt(line[3]));
        invItem.setInvNo(Integer.parseInt(line[4]));

        return invItem;
    }

}
