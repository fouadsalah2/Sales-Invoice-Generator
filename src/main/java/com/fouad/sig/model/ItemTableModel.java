/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fouad.sig.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ItemTableModel extends AbstractTableModel {

    private List<SalesInvoiceLine> li = new ArrayList<>();
    private String[] columnNames = {"No.", "Item Name", "Price", "Count", "Item Total"};

    public ItemTableModel(List<SalesInvoiceLine> list) {
        this.li = list;
    }

    public void CreateNewRow() {
        if (li.isEmpty()) {
            li.add(new SalesInvoiceLine());
            return;
        }

        var row = li.get(li.size() - 1);

        if (row.getItemCount() > 0 && row.getItemPrice().compareTo(BigDecimal.ZERO) != 0) {
            li.add(new SalesInvoiceLine());
        }

    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public int getRowCount() {
        return li.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SalesInvoiceLine si = li.get(rowIndex);
        switch (columnIndex) {
            case 0 -> {
                return si.getNo();
            }
            case 1 -> {
                return si.getItemName();
            }
            case 2 -> {
                return si.getItemPrice();
            }
            case 3 -> {
                return si.getItemCount();
            }
            case 4 -> {
                return si.getItemTotal();
            }
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0 -> {
                return Integer.class;
            }
            case 1 -> {
                return String.class;
            }
            case 2 -> {
                return BigDecimal.class;
            }
            case 3 -> {
                return Integer.class;
            }
            case 4 -> {
                return BigDecimal.class;
            }

        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        SalesInvoiceLine row = li.get(rowIndex);

        switch (columnIndex) {
            case 0:
                row.setNo((Integer) aValue);
                break;
            case 1:
                row.setItemName((String) aValue);
                break;
            case 2:
                row.setItemPrice((BigDecimal) aValue);
                break;
            case 3:
                row.setItemCount((Integer) aValue);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        if (columnIndex == 4) {
            return false;
        }

        return true;
    }

}
