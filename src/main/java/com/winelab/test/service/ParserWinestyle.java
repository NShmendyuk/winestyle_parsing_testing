package com.winelab.test.service;

import com.winelab.test.dto.WineDto;
import com.winelab.test.model.Wine;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jsoup.*;

import java.io.IOException;
import java.util.ArrayList;


@Service
public class ParserWinestyle{
    private IWineService iWineService;

    String mainUrl = "https://spb.winestyle.ru";
//    String winePages = "/wine/st-petersburg/";
    String winePages = "/wine/wines_ll/";

    public ParserWinestyle(IWineService iWineService){
        this.iWineService= iWineService;
    }

    @Autowired
    public void winestyleParsingPages() throws IOException, InterruptedException {
        int pages = getNumberOfPages(mainUrl + winePages);

        parsing(mainUrl + winePages); //TODO: i = 2
        for (int i = 2; i<=pages; i++){
//            System.out.println("Next page " + i); //TODO: log.info(page);
            parsing(mainUrl + winePages + "?page=" + i);
            Thread.sleep(100);
        }
        iWineService.toCsvFile();
    }

//    Здесь всё норм - просто пробегаем по страничкам интернет-магазина
    private Integer getNumberOfPages(String url) throws IOException {
        Document doc = null;

        while(doc == null)
            try{
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36")
                        .get(); // Берем страничку html

            } catch (Exception ex){
                System.out.println("not connected to pages!!!!!!"); //TODO: log.error(no connection)
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        Element winePagesElement = doc.getElementById("CatalogPagingBottom");
        String pages = winePagesElement.getElementsByTag("ul").text();
        return Integer.parseInt(pages.substring(pages.lastIndexOf(" ")+1));
    }

    private void parsing(String url) throws IOException {
        Document doc = null;

        while(doc == null) {
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36")
                        .get(); // Берем страничку html

            } catch (Exception ex) {
                System.out.println("not connected to pages!!!!!!"); //TODO: log.error(no connection)
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } // Берем страничку html
        }
        Elements wineElements = doc.getElementsByClass("item-block-content");

        //Переход на личную страницу продукции
        for (Element infoBlock: wineElements) {
            Elements els = infoBlock.getElementsByClass("title");
            for (Element el: els){
                String urlToProductPage = el.getElementsByAttribute("href").toString();
                urlToProductPage = urlToProductPage.
                        substring(urlToProductPage.
                                indexOf("<a href=\"")+9, urlToProductPage.indexOf("\">"));
                parsePage(urlToProductPage);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void parsePage(String wineUrlPage) throws IOException {
        String color = "noColor";
        String price = "noPrice";
        String name = "noName";
        ArrayList<String> values;
        Document doc = null;

        while(doc == null)
        try{
            doc = Jsoup.connect(mainUrl + wineUrlPage)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36")
                    .get(); // Берем страничку html

        } catch (Exception ex){
            System.out.println("not connected to wine page!!!!!!"); //TODO: log.error(no connection)
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Название
        Elements mainHeader = doc.getElementsByClass("main-header");

        //вино, регион, производитель, бренд, крепость, объем, виноград
        Elements mainInfo = doc.getElementsByClass("main-info");

        boolean checkStock = true;
        for (Element el: mainInfo){
            //Товар в наличии или нет, если нет, то price надо по-другому парсить (из main-info, а не right-info)
            if (el.getElementsByClass("stock").hasText()) checkStock = false;
        }
        // цена
        Elements rightInfo = doc.getElementsByClass("right-info");

        Elements colorTastingInfo = doc.getElementsByClass("item-content item-content_second item-content_no-js");

        name = parseHeader(mainHeader);
        values = new ArrayList<>(parseMainInfo(mainInfo));

        //TODO: Double.parseDouble(price)
        if (checkStock){
            price = parsePrice(rightInfo);
        } else {
            price = parsePriceFromMainInfo(mainInfo);
        }

        color = parseColorTastingInfo(colorTastingInfo); //TODO: можно ли изменить строку так, чтобы остался только цвет

        createWine(name, price, color, values);

         }

    /**
     *
     * @param header
     * @return wine name
     */
    private String parseHeader(Elements header){
        String name = "noHeaderName";
        for (Element head: header){
            name = head.getElementsByClass("text").toString();
            name = name.substring(name.indexOf("<span class=\"text\">")+19, name.lastIndexOf(",")); //TODO: имя вина
        }
        return name;
    }

    /**
     *
     * @param rightInfo
     * @return String price
     */
    private String parsePrice(Elements rightInfo){
        String price = "noPrice";
        for (Element el: rightInfo){
            price = el.getElementsByClass("price").toString();
            price = price.substring(price.indexOf("<div class=\"price \">")+22, price.indexOf("<span>")-1);
        }
        return price;
    }

    private String parsePriceFromMainInfo(Elements mainInfo){
        String price = "noPrice_noStock";
        for (Element el: mainInfo){
            price = el.getElementsByClass("price").text();
        }
        return price;
    }

    private ArrayList<String> parseMainInfo(Elements info){
        ArrayList<String> arrInfo = new ArrayList<>();
        for (Element el: info){
            Elements liElements = el.getElementsByTag("li");
            for (Element li: liElements){
                if (li.text().length() == 0) continue;
                arrInfo.add(li.text());
            }
        }
        return arrInfo;
    }

    private String parseColorTastingInfo(Elements tastingInfo){
        String colorDescription = "noColor";
        for (Element el: tastingInfo){
            Elements elements = el.getElementsByClass("description-block");
            for (Element description: elements){
                if (description.text().contains("Цвет")){
                    colorDescription = description.text().replaceAll("Цвет", "");
                }
            }
        }
        return colorDescription;
    }

    private Wine createWine(String name, String price, String colorDescription, ArrayList<String> values){
        WineDto wineDto = new WineDto();
        wineDto.setName(name);
        wineDto.setPrice(price.replaceAll("руб", "").replaceAll(".",""));
        wineDto.setColor(colorDescription);
        values.forEach(value -> {

            if (value.contains("Регион")){
                wineDto.setRegion(value.replaceAll("Регион", "").replaceAll(" ",""));
            }
//            if (value.contains("Производитель")){}
            if (value.contains("Бренд")){
                wineDto.setBrand( value.replaceAll("Бренд", "").replaceAll(" ",""));
            }
            if (value.contains("Крепость")){
                wineDto.setStrength( value.replaceAll("Крепость", "").replaceAll(" ",""));
            }
            if (value.contains("Объем")){
                wineDto.setVolume( value.replaceAll("Объем", "").replaceAll(" ",""));
            }
            if (value.contains("Виноград")){
                wineDto.setGrape( value.replaceAll("Виноград", "").replaceAll(" ","") );
                if(value.contains("Бел")){
                    wineDto.setColor("Белое");
                }
            }
            if (value.contains("Вино:")){
                wineDto.setSugar( value.substring(value.indexOf(",")+2) );

                //TODO: всё перевести в lower case
                if(value.contains("Бел"))
                {
                    wineDto.setColor("Белое");
                }
                if(value.contains("Оранж")){
                    wineDto.setColor("Оранжевое");
                }
                if(value.contains("Розов")){
                    wineDto.setColor("Розовое");
                }
                if(value.contains("Красн")){
                    wineDto.setColor("Красное");
                }

            }
        });
        if (wineDto.getBrand() == null){
            try{
                if (name.contains("\"")) wineDto.setBrand(name.substring(name.indexOf("\"")+1, name.lastIndexOf("\"")));
                else wineDto.setBrand("noBrand");
            } catch (Exception ex) {
                System.out.println(name + "  !!!!!!Exception"); //TODO: log.error
                wineDto.setBrand("noBrand");
            }
        }
        return iWineService.add(wineDto);
    }
}
