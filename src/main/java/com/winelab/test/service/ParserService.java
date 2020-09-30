package com.winelab.test.service;

import com.winelab.test.dto.WineDto;
import com.winelab.test.model.Wine;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;


@Service
public class ParserService {
    private final IWineService wineService;
    private final IDocumentService documentService;

    String mainUrl = "https://spb.winestyle.ru";
    //    String winePages = "/wine/st-petersburg/";
    String winePages = "/wine/wines_ll/";

    public ParserService(IWineService wineService, IDocumentService documentService) {
        this.wineService = wineService;
        this.documentService = documentService;
    }

    //TODO: принимать любой относительный путь
    //public void parseByPages(String relativeUrl)

    // Пробегаем по страничкам интернет-магазина
    @Autowired
    public void parseByPages() throws IOException, InterruptedException {
        String relativeUrl = this.winePages;
        Document parsePageDoc = documentService.getJsoupDocument(mainUrl + relativeUrl);

        Document productDoc;

        int pages = documentService.pagesNumber(parsePageDoc);

        for (int i = 2; i <= pages; i++) {
            //TODO: log.info(page);
            Elements wineElements = parsePageDoc.getElementsByClass("item-block-content");

            for (Element infoBlock : wineElements) {
                Elements els = infoBlock.getElementsByClass("title");
                for (Element el : els) {
                    String urlToProductPage = el.getElementsByAttribute("href").toString();
                    urlToProductPage = urlToProductPage.
                            substring(urlToProductPage.
                                    indexOf("<a href=\"") + 9, urlToProductPage.indexOf("\">"));
                    productDoc = documentService.getJsoupDocument(mainUrl + urlToProductPage);
                    parsePage(productDoc, urlToProductPage);
                    Thread.sleep(2000);
                }
            } //Переход на страницу продукции

            Thread.sleep(2000);
            parsePageDoc = documentService.getJsoupDocument(mainUrl + relativeUrl + "?page=" + i);
        }
    }

    public void parsePage(Document doc, String urlToProductPage) {
        String price = "noPrice";
        String name = "noName";
        ArrayList<String> values;

        //Название
        Elements mainHeader = doc.getElementsByClass("main-header");

        //вино, регион, производитель, бренд, крепость, объем, виноград
        Elements mainInfo = doc.getElementsByClass("main-info");

        Element aImage = doc.select("a[itemprop=image]").first();
        String urlImage = aImage.getElementsByAttribute("href").toString();
        urlImage = urlImage.
                substring(urlImage.
                        indexOf("\" href=\"") + 8, urlImage.indexOf("\" title"));

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


        createWine(name, price, values, urlToProductPage, urlImage);
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

            Element meta = el.select("meta[itemprop=releaseDate]").first();
            String year = meta.attr("content");
            arrInfo.add("Год: " + year);
        }
        return arrInfo;
    }

    private String parseTastingInfo(Elements tastingInfo){ //TODO: tasting info
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

    private Wine createWine(String name, String price, ArrayList<String> values, String urlToProductPage, String urlImage){
        WineDto wineDto = new WineDto();
        wineDto.setName(name);
        wineDto.setUrl(urlToProductPage);
        wineDto.setPrice(price.replaceAll("руб", "").replaceAll("\\.", ""));
        wineDto.setImageUrl(urlImage);
        values.forEach(value -> {

            if (value.contains("Год:")){
                wineDto.setYear(Long.parseLong((value.replaceAll("Год: ", ""))));
            }
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
        return wineService.add(wineDto);
    }
}
