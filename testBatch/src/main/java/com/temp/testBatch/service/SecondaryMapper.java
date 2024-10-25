package com.temp.testBatch.service;

import com.temp.testBatch.db.SecondaryConnection;

@SecondaryConnection
public interface SecondaryMapper {
    void selectTemp();
    
    void selectSecondary();
    
    void updateSecondaryStart();
    
    void updateSecondaryEnd();
}