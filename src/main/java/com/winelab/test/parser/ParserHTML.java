package com.winelab.test.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.jsoup.*;

import java.io.IOException;


@Service
public class ParserHTML {
    String mainUrl = "https://spb.winestyle.ru/wine/st-petersburg/";

    public void winestyleParsing() throws IOException{
        parsing(mainUrl);
        parsing(mainUrl + "?page=2");
    }

    public void parsing(String url) throws IOException {
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
        System.out.println(strings[0] + " - " + strings[1]);
    }

}
