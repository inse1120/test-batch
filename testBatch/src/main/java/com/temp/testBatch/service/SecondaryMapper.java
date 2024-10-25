package com.temp.testBatch.service;

import com.temp.testBatch.db.SecondaryConnection;

@SecondaryConnection
public interface SecondaryMapper {
    void selectTemp();
}