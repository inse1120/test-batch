package com.temp.testBatch.service;

import org.springframework.transaction.annotation.Transactional;
//import com.temp.testBatch.service.TestBatchMapper;

public class TestBatchService {
	
	private TestBatchMapper testBatchMapper;
	
	@Transactional(rollbackFor = Exception.class)
    public void saveTemp() {
		System.out.println("db insert=====>");
		testBatchMapper.insertTemp();
    }
}
