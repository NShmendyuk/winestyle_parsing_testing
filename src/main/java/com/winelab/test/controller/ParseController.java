package com.winelab.test.controller;

import com.winelab.test.controller.exception.ServiceIsBusyException;
import com.winelab.test.service.IParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/parse")
public class ParseController {
    private final IParserService parserService;

    @Autowired
    public ParseController(IParserService parserService) {
        this.parserService = parserService;
    }

    @RequestMapping(value = "{alcohol}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> startParsing(@PathVariable String alcohol) throws ServiceIsBusyException {
        parserService.startParsingJob(alcohol);
        return new ResponseEntity<>("Parsing job was successfully launched.", HttpStatus.OK);
    }
}
