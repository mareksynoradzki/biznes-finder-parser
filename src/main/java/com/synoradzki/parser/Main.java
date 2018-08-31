package com.synoradzki.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.synoradzki.parser.CSVUtils.writeLine;

public class Main {

    public static void main(String[] args) throws IOException {
        List<CompanyContact> companies = Arrays.asList(
                new CompanyContact("Jowita", "6897 8484", "marek.synnoradzki", "Wrocław", "jakielońska", "26"),
                new CompanyContact(null, "6897 8484", "marek.synnoradzki", "Wrocłąw", "jakielońska", "26")
        );
            saveToFile(companies);
//        try {
//            String basePage = "https://www.biznesfinder.pl/Wroc%C5%82aw;+dolno%C5%9Bl%C4%85skie,s,Biura+projektowe";
//            List<CompanyContact> companies = parsePages(basePage);
//            saveToFile(companies);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    private static List<CompanyContact> parsePages(String basePageUrl) throws IOException {
        List<CompanyContact> result = new ArrayList<>();
        result.addAll(parsePage(basePageUrl));
        boolean hasNext = true;
        int currentPageNo = 2;
        while (hasNext) {
            String currentPage = basePageUrl + ",p," + currentPageNo;
            System.out.println("aktualna strona " + currentPageNo);
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

    private static void saveToFile(List<CompanyContact> contacts) throws IOException {

        String csvFile = "result.csv";
//        FileWriter writer = new FileWriter(csvFile);
        Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(csvFile), "UTF8"));

        contacts.forEach(contact->{
            try {
                writeLine(writer, contact.propertiesToList());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writer.flush();
        writer.close();


    }
}
