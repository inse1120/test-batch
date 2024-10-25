package com.temp.testBatch;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestBatchApplication {

	public static void main(String[] args) throws Exception {
		List<String> params = new ArrayList<>();
        for(String key : args){
            params.add(key);
        }
        
        params.add("requestDate="+System.currentTimeMillis());
        
        System.exit(SpringApplication.exit(SpringApplication.run(TestBatchApplication.class, params.toArray(new String[0]))));
	}

}
