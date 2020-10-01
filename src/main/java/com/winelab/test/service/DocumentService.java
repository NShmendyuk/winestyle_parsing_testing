package com.winelab.test.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DocumentService implements IDocumentService {
    public Document getJsoupDocument(String url) {
        Document doc = null;

        log.info("parsing url: {}", url);

        while (doc == null) {
            try {
                doc = Jsoup
                        .connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36")
                        .get(); // Берем страничку html

            } catch (Exception ex) {
                log.error("Couldn't get a connection to website! ", ex);
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
