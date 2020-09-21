package com.winelab.test.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.jsoup.*;

import java.io.IOException;
import java.util.Arrays;


@Service
public class ParserHTML {
    String mainUrl = "https://spb.winestyle.ru/wine/st-petersburg/";
//    String mainUrl = "https://spb.winestyle.ru/wine/all/";

    public void winestyleParsing() throws IOException{
        //TODO: надо ещё проверить на существование страниц - как будет работать если одна страница или не одной
        // (а впрочем не надо, тут же делаем только для winestyle)
        Integer pages = getNumberOfPages(mainUrl);

        parsing(mainUrl);
        for (int i = 2; i<=pages; i++){
            parsing(mainUrl + "?page=" + i);
        }
    }

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
                .get();
        Elements wineElements = doc.getElementsByClass("item-block-content");
//        Elements listElements = wineElements.get(0).getElementsByTag("li");
//
//        System.out.println("[");
//        for (Element el: listElements){
//            entryWineInfo(el.text());
//        }
//        System.out.println("]");

        //Выводим Название
        for (Element infoBlock: wineElements){
            String title = infoBlock.getElementsByClass("text").text();
            if (title.contains("Отзыв"))
            title = title.substring(0,title.indexOf("Отзыв"));
            title = title.substring(0,title.lastIndexOf(","));
            System.out.println(title);

            //Выводим информацию
            System.out.println("[");
            for (Element el: infoBlock.getElementsByTag("li") ){
                entryWineInfo(el.text());
            }
            System.out.println("]");
        }

    }

    private void entryWineInfo(String wineInfo){
        String[] strings = wineInfo.split(":");
        System.out.println(strings[0] + " - " + strings[1]); // TODO: писать в log.info, или в БД
    }

}
