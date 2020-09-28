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
    String winePages = "/wine/all/";

    public ParserWinestyle(IWineService iWineService){
        this.iWineService= iWineService;
    }

    @Autowired
    public void winestyleParsingPages() throws IOException{
        int pages = getNumberOfPages(mainUrl + winePages);

        parsing(mainUrl + winePages);
        for (int i = 2; i<=pages; i++){
            parsing(mainUrl + winePages + "?page=" + i);
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
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void parsePage(String wineUrlPage) throws IOException {
        String color = "";
        String price = "";
        String name = "";
        ArrayList<String> values;

        Document doc = Jsoup.connect(mainUrl + wineUrlPage)
                .userAgent("Chrome/85.0.4183.102")
                .get(); // Берем страничку html

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

        color = parseColorTastingInfo(colorTastingInfo);

        Wine wine = createWine(name, price, color, values);

        System.out.println(wine.getColor()); //TODO: можно ли изменить строку так, чтобы остался только цвет
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
            name = name.substring(name.indexOf("<span class=\"text\">")+19, name.lastIndexOf(","));
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
                    colorDescription = description.text().substring(5);
                }
            }
        }

        return colorDescription;
    }

    private Wine createWine(String name, String price, String color, ArrayList<String> values){
        WineDto wineDto = new WineDto();
        wineDto.setName(name);
        wineDto.setPrice(price);
        wineDto.setColor(color);
        values.forEach(value -> {
            if (value.contains("Вино:")){
                wineDto.setSugar( value.substring(value.indexOf(",")+2) );
            }
            if (value.contains("Регион")){
                wineDto.setRegion( value.substring(value.indexOf(":")+2) );
            }
//            if (value.contains("Производитель")){}
            if (value.contains("Бренд")){
                wineDto.setBrand( value.substring(value.indexOf(":")+2) );
            }
            if (value.contains("Крепость")){
                wineDto.setStrength( value.substring(value.indexOf("Крепость")+9) );
            }
            if (value.contains("Объем")){
                wineDto.setVolume( value.substring(value.indexOf(":")+2) );
            }
            if (value.contains("Виноград")){
                wineDto.setGrape( value.substring(value.indexOf(":")+2) );
            }
        });
        if (wineDto.getBrand() == null){
            if (name.contains("\"")) wineDto.setBrand(name.substring(name.indexOf("\"")+1, name.lastIndexOf("\"")));
            else wineDto.setBrand("noBrand");
        }
        return iWineService.add(wineDto);
    }
}
