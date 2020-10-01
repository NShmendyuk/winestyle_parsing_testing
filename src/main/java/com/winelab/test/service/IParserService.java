package com.winelab.test.service;

import com.winelab.test.controller.exception.ServiceIsBusyException;

public interface IParserService {
    void startParsingJob(String alcoholType) throws ServiceIsBusyException;
}
