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

    public static void main(String[] args) throws Exception {
        Integer startPage = 1;
        Integer endPage = Integer.MAX_VALUE;
        if(args.length>1){
            startPage = Integer.valueOf(args[1]);
        }
        if(args.length>2){
            endPage = Integer.valueOf(args[2]);
        }


        if (args.length ==0 || args.length>3) {
            System.out.print("Nieodpowiednia liczba atrybutów");
            System.exit(0);
        }

        System.out.println("startPage: " + startPage);
        System.out.println("endPage: : " + endPage);

        try {
            String basePage = args[0];
//            String basePage = "https://www.biznesfinder.pl/Wrocław;+dolnośląskie";
            List<CompanyContact> companies = parsePages(basePage, startPage, endPage);
            companies = companies.stream().filter(e -> Objects.nonNull(e.phone) && !e.phone.isEmpty()).collect(Collectors.toList());
            excel(companies);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static List<CompanyContact> parsePages(String basePageUrl, Integer startPage, Integer endPage) throws Exception{
        List<CompanyContact> result = new ArrayList<>();
        if(startPage<2) {
            result.addAll(parsePage(basePageUrl));
            System.out.println(basePageUrl);
        }
        boolean hasNext = true;
        int currentPageNo = startPage<2 ? 2:  startPage;

        while (hasNext && currentPageNo<=endPage) {
            String currentPage = basePageUrl + ",p," + currentPageNo;
            System.out.println(currentPage);
            Document page = Jsoup.connect(currentPage).get();
            Thread.sleep(500L);
            if (hasAnyResult(page)) {
                result.addAll(parsePage(page));
            } else {
                hasNext = false;
            }
            currentPageNo++;
        }
        return result;
    }

    private static boolean hasAnyResult(Document page) {
        if (!page.select("#form-recaptcha").isEmpty()) {
            System.out.println("Wykryto próbę krazieży danych");
        }
        return !page.select("#companies .company").isEmpty();
    }

    private static List<CompanyContact> parsePage(String pageUrl) throws IOException {
        return parsePage(Jsoup.connect(pageUrl).get());
    }

    private static List<CompanyContact> parsePage(Document page) throws IOException {
        Elements companies = page.select("#companies .company");

        List<CompanyContact> result = new ArrayList<>();
        for (Element company : companies) {
            String name = printIsExists(company.select(".company-name a"));
            String city = printIsExists(company.select(".list-unstyled .company-address .company-address-city"));
            String street = printIsExists(company.select(".list-unstyled .company-address .company-address-street"));
            String buildingNo = printIsExists(company.select(".list-unstyled .company-address .company-address-building"));
            String phone = printIsExists(company.select(".list-unstyled .company-phone meta"), "content");
            String email = printIsExists(company.select(".list-unstyled .company-email meta"), "content");
            String category = printIsExists(company.select(".company-category a"));
            String pageUrl = printIsExists(company.select(".company-www a"), "href");

            result.add(new CompanyContact(name, phone, email, city, street, buildingNo, category, pageUrl));
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
        row.createCell(6).setCellValue("kategoria");
        row.createCell(7).setCellValue("www");


        for (CompanyContact contact : contacts) {
            row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(contact.name);
            row.createCell(1).setCellValue(contact.city);
            row.createCell(2).setCellValue(contact.street);
            row.createCell(3).setCellValue(contact.buildingNo);
            row.createCell(4).setCellValue(contact.email);
            row.createCell(5).setCellValue(contact.phone);
            row.createCell(6).setCellValue(contact.category);
            row.createCell(7).setCellValue(contact.pageUrl);
        }
        try {
            String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
            FileOutputStream outputStream = new FileOutputStream("dane_" + fileName + ".xlsx");
            workbook.write(outputStream);
            workbook.close();
            System.out.println(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
