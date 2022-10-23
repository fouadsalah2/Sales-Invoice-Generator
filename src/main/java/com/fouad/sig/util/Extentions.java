/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fouad.sig.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author John
 */
public class Extentions {

    public static LocalDate ParseDate(String dateTxt) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
            //convert String to LocalDate
            LocalDate localDate = LocalDate.parse(dateTxt, formatter);

            return localDate;
        } catch (Throwable t) {
            return null;
        }
    }

    public static String DateToString(LocalDate date) {
        if (date == null) {
            return "";
        }

        return DateTimeFormatter.ofPattern("d/MM/yyyy").format(date);
    }
}
