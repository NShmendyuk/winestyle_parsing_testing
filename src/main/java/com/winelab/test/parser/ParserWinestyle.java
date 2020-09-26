package com.winelab.test.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jsoup.*;

import java.io.IOException;


@Service
public class ParserWinestyle{
    String mainUrl = "https://spb.winestyle.ru";
    String wineSpbPage = "/wine/st-petersburg/";

    @Autowired
    public void winestyleParsingPages() throws IOException{
        int pages = getNumberOfPages(mainUrl + wineSpbPage);

        parsing(mainUrl + wineSpbPage);
        for (int i = 2; i<=pages; i++){
            parsing(mainUrl + wineSpbPage + "?page=" + i);
        }
    }

//    Здесь всё норм - просто пробегаем по страничкам интернет-магазина
    private Integer getNumberOfPages(String url) throws IOException {
        Document doc = Jsoup.connect(url)
                .userAgent("Chrome/85.0.4183.102")
                .get();
        Element winePagesElement = doc.getElementById("CatalogPagingBottom");
        String pages = winePagesElement.getElementsByTag("ul").text();
        return Integer.parseInt(pages.substring(pages.lastIndexOf(" ")+1));
    }

    private void parsing(String url) throws IOException {
        Document doc = Jsoup.connect(url)
                .userAgent("Chrome/85.0.4183.102")
                .get(); // Берем страничку html

        Elements wineElements = doc.getElementsByClass("item-block-content");

        for (Element infoBlock: wineElements) {
            Elements els = infoBlock.getElementsByClass("title");
            for (Element el: els){
                String urlToProductPage = el.getElementsByAttribute("href").toString();
                urlToProductPage = urlToProductPage.
                        substring(urlToProductPage.
                                indexOf("<a href=\"")+9, urlToProductPage.indexOf("\">"));
                parsePage(urlToProductPage);
            }
        }
    }

    private void parsePage(String wineUrlPage){

    }

}
