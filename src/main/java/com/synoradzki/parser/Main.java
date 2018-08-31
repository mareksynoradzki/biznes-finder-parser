package com.synoradzki.parser;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.print("Nieodpowiednia liczba atrybutów");
            System.exit(0);
        }
        try {
            String basePage = args[0];
            List<CompanyContact> companies = parsePages(basePage);
            companies = companies.stream().filter(e -> Objects.nonNull(e.phone) && !e.phone.isEmpty()).collect(Collectors.toList());
            excel(companies);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static List<CompanyContact> parsePages(String basePageUrl) throws IOException {
        List<CompanyContact> result = new ArrayList<>();
        result.addAll(parsePage(basePageUrl));
        boolean hasNext = true;
        int currentPageNo = 2;
        while (hasNext) {
            String currentPage = basePageUrl + ",p," + currentPageNo;
            System.out.println("strona " + currentPageNo);
            if (hasAnyResult(currentPage)) {
                result.addAll(parsePage(currentPage));
            } else {
                hasNext = false;
            }
            currentPageNo++;
        }
        return result;
    }

    private static boolean hasAnyResult(String pageUrl) throws IOException {
        if (!Jsoup.connect(pageUrl).get().select("#form-recaptcha").isEmpty()) {
            System.out.println("Wykryto próbę krazieży danych");
        }
        return !Jsoup.connect(pageUrl).get().select("#companies .company").isEmpty();
    }

    private static List<CompanyContact> parsePage(String pageUrl) throws IOException {
        Document doc = Jsoup.connect(pageUrl).get();
        Elements companies = doc.select("#companies .company");

        List<CompanyContact> result = new ArrayList<>();
        for (Element company : companies) {
            String name = printIsExists(company.select(".company-name a"));
            String city = printIsExists(company.select(".list-unstyled .company-address .company-address-city"));
            String street = printIsExists(company.select(".list-unstyled .company-address .company-address-street"));
            String buildingNo = printIsExists(company.select(".list-unstyled .company-address .company-address-building"));
            String phone = printIsExists(company.select(".list-unstyled .company-phone meta"), "content");
            String email = printIsExists(company.select(".list-unstyled .company-email meta"), "content");
            result.add(new CompanyContact(name, phone, email, city, street, buildingNo));
        }
        return result;
    }

    private static String printIsExists(Elements elements) {
        return printIsExists(elements, null);
    }

    private static String printIsExists(Elements elements, String attr) {
        if (!elements.isEmpty()) {
            if (Objects.isNull(attr)) {
                return elements.get(0).text();
            } else {
                return elements.get(0).attr(attr);

            }
        }
        return null;
    }

    private static void excel(List<CompanyContact> contacts) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Datatypes in Java");
        int rowNum = 0;

        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Nazwa");
        row.createCell(1).setCellValue("Miasto");
        row.createCell(2).setCellValue("Ulica");
        row.createCell(3).setCellValue("Numer bundynku");
        row.createCell(4).setCellValue("email");
        row.createCell(5).setCellValue("telefon");


        for (CompanyContact contact : contacts) {
            row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(contact.name);
            row.createCell(1).setCellValue(contact.city);
            row.createCell(2).setCellValue(contact.street);
            row.createCell(3).setCellValue(contact.buildingNo);
            row.createCell(4).setCellValue(contact.email);
            row.createCell(5).setCellValue(contact.phone);
        }
        try {
            String fileName = new SimpleDateFormat("yyyyy-mm-dd-hh-mm").format(new Date());
            FileOutputStream outputStream = new FileOutputStream("dane_" + fileName + ".xlsx");
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
