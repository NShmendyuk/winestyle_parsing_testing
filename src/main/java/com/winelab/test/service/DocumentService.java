package com.winelab.test.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
public class DocumentService implements IDocumentService {
    public Document getJsoupDocument(String url) {
        Document doc = null;

        System.out.println(url);

        while (doc == null) {
            try {
                doc = Jsoup
                        .connect(url)
                        .get(); // Берем страничку html

            } catch (Exception ex) {
                System.out.println("Couldn't get a connection to website!"); //TODO: log.error(no connection)
            } // Берем страничку html
        }
        return doc;
    }

    public Integer pagesNumber(Document doc) {
        Element pagingElement = doc.getElementById("CatalogPagingBottom");
        String pagesNumber = pagingElement.getElementsByTag("li").last().text();
        return Integer.parseInt(pagesNumber);
    }
}
