package com.fouad.sig.model;

import com.fouad.sig.Frams.InvoicesFram;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileOperations {

    private InvoicesFram _frame;

    private static String sPath = "";

    public FileOperations(InvoicesFram frame) {
        _frame = frame;
    }

    public List<SalesInvoice> ReadFile() throws FileNotFoundException, Exception {

        int result;
        String invoiceHeaderFilePath = "", invoiceLineFilePath = "";

        _frame.setTitle("No file loaded");

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Please select the Invoices header csv file");

        if (!sPath.isBlank()) {
            fileChooser.setCurrentDirectory(new File(sPath));
        }

        result = fileChooser.showOpenDialog(_frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            sPath = selectedFile.getParent();
            invoiceHeaderFilePath = selectedFile.getAbsolutePath();
            System.out.println("Invoice Header File: " + selectedFile.getAbsolutePath());
        } else {
            throw new Exception("Operation canceled");
        }

        // lines
        fileChooser.setDialogTitle("Please select the Invoice lines csv file");
        result = fileChooser.showOpenDialog(_frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            invoiceLineFilePath = selectedFile.getAbsolutePath();
            System.out.println("Invoice Line File: " + selectedFile.getAbsolutePath());
        } else {
            throw new Exception("Operation canceled");
        }

        var invLines = ReadCSVFile(invoiceHeaderFilePath);
        var invLineItems = ReadCSVFile(invoiceLineFilePath);

        var invoices = SalesInvoice.FromLines(invLines, invLineItems);
        PrintInvoices(invoices);

        _frame.setTitle("Current file: " + invoiceHeaderFilePath);
        return invoices;
    }

    public void SaveFile(List<SalesInvoice> headers) throws Exception {
        int result;
        String invoiceHeaderFilePath = "", invoiceLineFilePath = "";
        FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("csv files (*.csv)", "csv");

        JFileChooser fileChooser = new JFileChooser() {
            @Override
            public void approveSelection() {
                if (getSelectedFile().exists()) {
                    int n = JOptionPane.showConfirmDialog(
                            this,
                            "Do You Want to Overwrite File?",
                            "Confirm Overwrite",
                            JOptionPane.YES_NO_OPTION);

                    if (n == JOptionPane.YES_OPTION) {
                        super.approveSelection();
                    }

                } else {
                    super.approveSelection();
                }
            }
        };

        fileChooser.addChoosableFileFilter(csvFilter);
        fileChooser.setDialogTitle("Please Save the Invoices header csv file");
        result = fileChooser.showSaveDialog(_frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            invoiceHeaderFilePath = selectedFile.getAbsolutePath();
            System.out.println("Invoice Header File: " + selectedFile.getAbsolutePath());
        } else {
            throw new Exception("Operation canceled");
        }
        
        if (invoiceHeaderFilePath == null || invoiceHeaderFilePath.isBlank() || !invoiceHeaderFilePath.toLowerCase().endsWith(".csv")) {
            throw new Exception("Invalid invoice header file format");
        }

        fileChooser.addChoosableFileFilter(csvFilter);
        fileChooser.setDialogTitle("Please Save the Invoices Line csv file");
        result = fileChooser.showSaveDialog(_frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            invoiceLineFilePath = selectedFile.getAbsolutePath();
            System.out.println("Invoice Line File: " + selectedFile.getAbsolutePath());
        } else {
            throw new Exception("Operation canceled");
        }
        
        if (invoiceLineFilePath == null || invoiceLineFilePath.isBlank() || !invoiceLineFilePath.toLowerCase().endsWith(".csv")) {
            throw new Exception("Invalid invoice header file format, not csv");
        }
        
        if(invoiceLineFilePath.equals(invoiceHeaderFilePath))
        {
            throw new Exception("Please choose different file name for invoices header and item line. File must be csv");
        }

        WriteToCSV(invoiceHeaderFilePath, headers);
        WriteLinesToCSV(invoiceLineFilePath, headers);

    }

    private List<String> ReadCSVFile(String fileName) throws Exception {

        if (fileName == null || fileName.isBlank() || !fileName.toLowerCase().endsWith(".csv")) {
            throw new Exception("Invalid file format");
        }

        try ( BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            List<String> lines = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            return lines;

        } catch (UnsupportedEncodingException e) {
            throw new Exception("Invalid file format");
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Invalid file format");
        } catch (IOException e) {
            throw new Exception("Invalid file format");
        }
    }

    private void WriteToCSV(String fileName, List<SalesInvoice> invoices) throws UnsupportedEncodingException, FileNotFoundException, IOException, Exception {
        
         if (fileName == null || fileName.isBlank() || !fileName.toLowerCase().endsWith(".csv")) {
            throw new Exception("Invalid file format");
        }
        
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
            for (SalesInvoice inv : invoices) {
                bw.write(inv.ToCSVLine());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (UnsupportedEncodingException e) {
            throw e;
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }

    private void WriteLinesToCSV(String fileName, List<SalesInvoice> invoices) throws UnsupportedEncodingException, FileNotFoundException, IOException {

        ArrayList<String> lines = new ArrayList<>();
        for (SalesInvoice inv : invoices) {
            var invLines = inv.getItemLines();
            for (SalesInvoiceLine line : invLines) {
                lines.add(line.ToCSVLine(inv.getNo()));
            }
        }

        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (UnsupportedEncodingException e) {
            throw e;
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }

    private void PrintInvoices(List<SalesInvoice> invoices) {
        for (var inv : invoices) {
            inv.Print();
        }
    }

}
