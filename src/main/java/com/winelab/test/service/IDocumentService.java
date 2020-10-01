package com.winelab.test.service;

import org.jsoup.nodes.Document;

public interface IDocumentService {
    Document getJsoupDocument(String url) throws InterruptedException;

    Integer pagesNumber(Document doc);
}
