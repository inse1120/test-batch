package com.temp.testBatch.service;

import com.temp.testBatch.db.PrimaryConnection;

@PrimaryConnection
public interface TestBatchMapper {
    void insertTemp();
}