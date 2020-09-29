package com.winelab.test.service;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ProxyService implements IProxyService {
    private final String proxyApiURL = "http://pubproxy.com/api/proxy";

    //    public JSONObject proxyConnectionInfo(){
//
//    }
    public void connectToProxy() throws IOException {
        Jsoup.connect("https://spring.io/blog")
                .userAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html) " +
                        "Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko; compatible; Googlebot/2.1; +http://www.google.com/bot.html) " +
                        "Chrome/W.X.Y.Z‡ Safari/537.36")
                .proxy("187.111.160.6", 8080)
                .get();
    }
}
