package com.winelab.test.service;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface IDocumentService {
    Document getJsoupDocument(String url) throws IOException;

    Integer pagesNumber(Document doc) throws IOException;
}
