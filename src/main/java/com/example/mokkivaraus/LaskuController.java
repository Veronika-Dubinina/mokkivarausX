package com.example.mokkivaraus;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPTable;
import javafx.scene.control.*;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.io.IOException;
import java.util.ArrayList;

public class LaskuController extends TabController<Lasku> {
    // Attributes
    private static final String PDF_FOLDER_PATH = "src/main/pdf/"; // Путь к папке для сохранения PDF-файлов
    private static final String BLANK_PDF_PATH = "src/main/pdf/table_data.pdf"; // Путь к файлу-болванке


    // Constructor
    public LaskuController() {
        // Set class attributes
        super(Lasku.class);
    }

    // Methods
    @Override
    protected void tableMouseRightClick(TableRow<Lasku> row) {
        Lasku lasku = row.getItem();
        generatePDF(lasku);
    }

    private void generatePDF(Lasku lasku) {
        String fileName = "lasku_" + lasku.getLasku_id() + ".pdf";
        String dest = PDF_FOLDER_PATH + fileName;

        try {
            PdfDocument pdf = new PdfDocument(new PdfReader(BLANK_PDF_PATH), new PdfWriter(dest));
            Document document = new Document(pdf);

            // Set margins
            document.setMargins(100, 50, 50, 100);

            // Format the data from the Lasku object
            String content = String.format("ID Lasku: %d\nID Varaus: %d\nAsiakas: %s %s\nMokki: %s\nVarauksen data: %s\nSumma: %.2f €\nALV: %.2f %%\nMaksettu: %s",
                    lasku.getLasku_id(), lasku.getVaraus_id(), lasku.getAsiakas_etunimi(), lasku.getAsiakas_sukunimi(),
                    lasku.getMokkinimi(), lasku.getVarattu_pvm(), lasku.getSumma(), lasku.getAlv(), (lasku.getMaksettu() == 1 ? "joo" : "ei"));

            // Add content to the PDF at a fixed position
            Paragraph paragraph = new Paragraph(content);
            paragraph.setFixedPosition(100, 350, 400); // (x, y, width)
            document.add(paragraph);

            // Close the document
            document.close();

            // Show info dialog
            showInfoDialog(dest);

            System.out.println("PDF успешно создан: " + dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
// информируем о создании счета
    private void showInfoDialog(String filePath) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tieto");
        alert.setHeaderText(null);
        alert.setContentText("Lasku on tallennettu: " + filePath);
        alert.showAndWait();
    }
    @Override
    ArrayList<String[]> getColToAttr() {
        String[] cols = new String[]{"id", "asiakas_etunimi", "asiakas_sukunimi", "mokki", "varattu_pvm", "summa", "alv", "maksettu"};
        String[] attrs = new String[]{"lasku_id", "asiakas_etunimi", "asiakas_sukunimi", "mokkinimi", "varattu_pvm", "summa", "alv", "maksettu"};
        ArrayList<String[]> colToAttr = new ArrayList<>();
        colToAttr.add(cols);
        colToAttr.add(attrs);
        return colToAttr;
    }

    @Override
    DialogController getController() {
        return new LaskuDC("lasku", "lasku_id");
    }

    @Override
    boolean getSearchConditions(Lasku lasku, String newValue) {
        // Implement search conditions here
        // For example:
        String searchKeyword = newValue.toLowerCase();
        if (String.valueOf(lasku.getLasku_id()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("id"))) {
            return true; // Match by ID
        } else if (lasku.getAsiakas_etunimi().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("as_etunimi"))) {
            return true; // Match by customer's first name
        } else if (lasku.getAsiakas_sukunimi().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("as_sukunimi"))) {
            return true; // Match by customer's last name
        } else if (lasku.getMokkinimi().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("mökki"))) {
            return true; // Match by cottage name
        } else if (String.valueOf(lasku.getVarattu_pvm()).contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("varattu_pvm"))) {
            return true; // Match by booking date
        } else if (String.valueOf(lasku.getSumma()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("summa"))) {
            return true; // Match by amount
        } else if (String.valueOf(lasku.getAlv()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("alv"))) {
            return true; // Match by VAT
        } else if (String.valueOf(lasku.getMaksettu()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("maksettu"))) {
            return true; // Match by "paid" flag
        }
        return false; // No match
    }

    @Override
    String[] getSearchFilters() {
        return new String[]{"kaikki", "id", "as_etunimi", "as_sukunimi", "mökki", "varattu_pvm", "summa", "alv", "maksettu"};
    }
}
