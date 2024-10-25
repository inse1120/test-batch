 package com.temp.testBatch.job;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import org.springframework.batch.core.configuration.annotation.StepScope;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

 @Slf4j
 @Configuration
 @RequiredArgsConstructor
 public class TestBatchJobConfig {
	 
	private final SqlSessionFactory sqlSessionFactory;
 	
	@Bean
 	public Job testJob(JobRepository jobRepository,PlatformTransactionManager transactionManager, MyBatisCursorItemReader<TempData> reader, MyBatisBatchItemWriter<TempData> writer){
        Job job = new JobBuilder("testBatch22Job",jobRepository)
                .start(testStep(jobRepository,transactionManager,reader,writer))
                .build();
        return job;
    }
 	
	@Bean
 	public Step testStep(JobRepository jobRepository,PlatformTransactionManager transactionManager, MyBatisCursorItemReader<TempData> reader, MyBatisBatchItemWriter<TempData> writer){
        Step step = new StepBuilder("testStep",jobRepository)
        	.<TempData, TempData> chunk(1, transactionManager)
    		.reader(reader)
    		.writer(writer)
            .build();
        return step;
    }
 	
 	@Bean
 	@StepScope
 	public MyBatisCursorItemReader<TempData> reader() { 		
 		return new MyBatisCursorItemReaderBuilder<TempData>()
 			.sqlSessionFactory(sqlSessionFactory)
 			.queryId("com.temp.testBatch.service.SecondaryMapper.selectTemp")
 			.build(); 			
 	}
	 	
 	@Bean
 	@StepScope
 	public MyBatisBatchItemWriter<TempData> writer() {   
 		return new MyBatisBatchItemWriterBuilder<TempData>()
 			.sqlSessionFactory(sqlSessionFactory)
 			.statementId("com.temp.testBatch.service.PrimaryMapper.insertTemp")
 			.build();
 	}

 }
