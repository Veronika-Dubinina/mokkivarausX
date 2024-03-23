package com.example.mokkivaraus;

import javafx.scene.control.*;

import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

public class LaskuController extends TabController<Lasku> {
    // Constructor
    public LaskuController() {
        // Set class attributes
        super("asiakkaiden_laskut", "lasku_id", Lasku.class);
        filter = "WHERE alue_id = " + SessionData.alue.getAlue_id();
    }

    // Methods
    @Override
    protected void tableMouseRightClick(TableRow<Lasku> row) {
        Lasku lasku = row.getItem();
        generatePDF(lasku);
    }

    private void generatePDF(Lasku lasku) {
        Document document = new Document();
        try {
            // Указываем путь и название для сохраняемого PDF-файла
            String fileName = "lasku_" + lasku.getLasku_id() + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // Форматируем данные из объекта Lasku и добавляем их в PDF
            String content = String.format("ID Ласку %d ID бронирования %d Asiakas %s %s Mokki %s Дата бронирования %s Сумма %.2f НДС %.2f Оплачено %s",
                    lasku.getLasku_id(), lasku.getVaraus_id(), lasku.getAsiakas_etunimi(), lasku.getAsiakas_sukunimi(),
                    lasku.getMokkinimi(), lasku.getVarattu_pvm(), lasku.getSumma(), lasku.getAlv(), (lasku.getMaksettu() == 1 ? "Да" : "Нет"));

            document.add(new Paragraph(content));

            // Закрываем документ
            document.close();

            System.out.println("PDF успешно создан: " + fileName);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
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
        return new LaskuDC("lasku", identifierKey);
    }

    @Override
    boolean getSearchConditions(Lasku lasku, String newValue) {
        // Реализуйте здесь условия поиска
        // Например:
        String searchKeyword = newValue.toLowerCase();
        if (String.valueOf(lasku.getLasku_id()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("id"))) {
            return true; // Соответствие по ID
        } else if (lasku.getAsiakas_etunimi().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("as_etunimi"))) {
            return true; // Соответствие по имени клиента
        } else if (lasku.getAsiakas_sukunimi().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("as_sukunimi"))) {
            return true; // Соответствие по фамилии клиента
        } else if (lasku.getMokkinimi().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("mökki"))) {
            return true; // Соответствие по названию мокки
        } else if (String.valueOf(lasku.getVarattu_pvm()).contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("varattu_pvm"))) {
            return true; // Соответствие по дате бронирования
        } else if (String.valueOf(lasku.getSumma()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("summa"))) {
            return true; // Соответствие по сумме
        } else if (String.valueOf(lasku.getAlv()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("alv"))) {
            return true; // Соответствие по НДС
        } else if (String.valueOf(lasku.getMaksettu()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("maksettu"))) {
            return true; // Соответствие по флагу "оплачено"
        }
        return false; // Нет совпадений
    }

    @Override
    String[] getSearchFilters() {
        return new String[]{"kaikki", "id", "as_etunimi", "as_sukunimi", "mökki", "varattu_pvm", "summa", "alv", "maksettu"};
    }
}
