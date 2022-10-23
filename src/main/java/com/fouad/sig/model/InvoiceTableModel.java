/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fouad.sig.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class InvoiceTableModel extends AbstractTableModel {

    private List<SalesInvoice> li = new ArrayList<>();
    private String[] columnNames = {"No.", "Date", "Customer", "Total"};

    public InvoiceTableModel(List<SalesInvoice> list) {
        this.li = list;
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
        SalesInvoice si = li.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return si.getNo();
            case 1:
                return si.getDate();
            case 2:
                return si.getCustomer();
            case 3:
                return si.getTotal();
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
                return LocalDate.class;
            case 2:
                return String.class;
            case 3:
                return BigDecimal.class;
        }
        return null;
    }

}
