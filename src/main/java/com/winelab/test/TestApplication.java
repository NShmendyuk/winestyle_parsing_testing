package com.winelab.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.winelab.test.parser.ParserHTML;

import java.io.IOException;

@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(TestApplication.class, args);

        ParserHTML parserHTML = new ParserHTML();
        parserHTML.winestyleParsing();
    }

}
