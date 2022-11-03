/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fouad.sig.model;

import com.fouad.sig.util.Extentions;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class SalesInvoice {

    private int no = 0;
    private LocalDate date = null;
    private String customer = "";
    private BigDecimal total = BigDecimal.ZERO;
    private final List<SalesInvoiceLine> itemLines = new ArrayList();

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void set(SalesInvoice inv) {
        date = inv.getDate();
        customer = inv.getCustomer();
        SetItemLines(inv.getItemLines());

    }

    public BigDecimal getTotal() {

        if (itemLines.isEmpty()) {
            return BigDecimal.ZERO;
        }

        Function<SalesInvoiceLine, BigDecimal> totalMapper = invoice -> invoice.getItemTotal();
        BigDecimal total = itemLines.stream()
                .map(totalMapper)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total;
    }

    public void setTotal(BigDecimal Total) {
        this.total = Total;
    }

    public List<SalesInvoiceLine> getItemLines() {
        return itemLines;
    }

    public void SetItemLines(List<SalesInvoiceLine> lines) {

        var newLines = lines.stream()
                .filter(x -> x.getItemTotal()
                .compareTo(BigDecimal.ZERO) != 0)
                .toList();
        
        for(int i=0; i< newLines.size(); i++)
        {
            newLines.get(i).setNo(i+1);
        }

        itemLines.clear();
        itemLines.addAll(newLines);
    }

    public String ToCSVLine() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

        StringBuilder builder = new StringBuilder();

        builder.append(no).append(',');
        builder.append(formatter.format(date)).append(',');
        builder.append(customer);
        ///builder.append(String.format("%.2f", total));

        return builder.toString();
    }

    public List<String> LinesToCSVLines() {
        List<String> lines = new ArrayList<>();

        for (SalesInvoiceLine item : itemLines) {
            lines.add(item.ToCSVLine(no));
        }

        return lines;
    }

    public void AddNewItemLine(SalesInvoiceLine itemLine) {
        var maxNo = itemLines.isEmpty() ? 0 : itemLines.stream()
                .mapToInt(x -> x.no)
                .max().getAsInt();

        itemLine.setNo(maxNo);
        itemLines.add(itemLine);
    }

    public void UpdateItemLine(SalesInvoiceLine itemLine) {
        var item = itemLines.stream()
                .filter(x -> x.getNo() == itemLine.getNo())
                .findFirst()
                .orElse(null);

        if (item == null) {
            throw new NoSuchElementException();
        }

        item.Set(itemLine);
    }

    public void RemoveItem(int itemNo) {
        var item = itemLines.stream()
                .filter(x -> x.getNo() == itemNo)
                .findFirst()
                .orElse(null);

        if (item == null) {
            throw new NoSuchElementException();
        }

        itemLines.remove(item);
    }

    public static List<String> ToLines(List<SalesInvoice> invoices) {
        List<String> invLines = new ArrayList<>();

        for (SalesInvoice inv : invoices) {
            invLines.add(inv.ToCSVLine());
        }

        return invLines;
    }

    public static SalesInvoice FromLine(String[] line) throws Exception {
        var inv = new SalesInvoice();

        if (line.length != 3) {
            throw new Exception("Invalid invoice line format");
        }
        
        if (line[0].isEmpty() || line[0].isBlank()) {
            return null;
        }
        

        inv.setNo(Integer.parseInt(line[0]));

        var date = Extentions.ParseDate(line[1]);
        if (date == null) {
            throw new Exception("Invalide date format. date should be (DD/MM/YYYY)");
        }

        inv.setDate(Extentions.ParseDate(line[1]));
        inv.setCustomer(line[2]);

        return inv;
    }

    public static List<SalesInvoice> FromLines(List<String> invLines, List<String> itemLines) throws Exception {

        List<SalesInvoiceLine> allLines = new ArrayList<>();

        for (var line : itemLines) {
            String[] values = line.split(",");
            var item = SalesInvoiceLine.FromLine(values);
            if (item != null) {
                allLines.add(item);
            }
        }

        List<SalesInvoice> invoices = new ArrayList<>();
        for (var line : invLines) {
            String[] values = line.split(",");
            var inv = SalesInvoice.FromLine(values);
            if (inv != null) {
                invoices.add(inv);

                var items = allLines.stream().filter(x -> x.getInvNo() == inv.getNo()).toList();
                inv.SetItemLines(items);

            }
        }

        return invoices;
    }

    public void Print() {
        System.out.println("Invoice: " + getNo());
        System.out.println("{");
        System.out.println("[" + Extentions.DateToString(getDate()) + "]" + ", " + getCustomer());
        for (var line : this.itemLines) {
            System.out.println(line.toString());
        }
        System.out.println("}");
        System.out.println("");
    }
}
