package com.winelab.test.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
public class DocumentService implements IDocumentService {
    public Document getJsoupDocument(String url) throws InterruptedException {
        Document doc = null;

        System.out.println(url);

        while (doc == null) {
            try {
                doc = Jsoup
                        .connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36")
                        .get(); // Берем страничку html

            } catch (Exception ex) {
                System.out.println("Couldn't get a connection to website!"); //TODO: log.error(no connection)
            } // Берем страничку html
        }
        Thread.sleep(1000);
        return doc;
    }

    public Integer pagesNumber(Document doc) {
        Element pagingElement = doc.getElementById("CatalogPagingBottom");
        String pagesNumber = pagingElement.getElementsByTag("li").last().text();
        return Integer.parseInt(pagesNumber);
    }
}
