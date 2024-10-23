 package com.temp.testBatch.job;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.batch.MyBatisBatchItemWriter;
//import org.mybatis.spring.batch.MyBatisCursorItemReader;
//import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.batch.core.step.tasklet.Tasklet;
//import org.springframework.batch.repeat.RepeatStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//import com.temp.testBatch.service.TestBatchService;

 @Slf4j
 @Configuration
 @RequiredArgsConstructor
 public class TestBatchJobConfig {
	 
	//private final TestBatchService testBatchService;
	private final DataSource dataSource;
//	private final SqlSessionFactory sqlSessionFactory;

 	@Bean
 	public Job testJob(JobRepository jobRepository,PlatformTransactionManager transactionManager, JdbcCursorItemReader<TempData> reader, JdbcBatchItemWriter<TempData> writer){
        Job job = new JobBuilder("testBatchJob",jobRepository)
                .start(testStep(jobRepository,transactionManager,reader,writer))
                .build();
        return job;
    }
 	
// 	@Bean
// 	public Job testJob(JobRepository jobRepository,PlatformTransactionManager transactionManager, JdbcCursorItemReader<TempData> reader, MyBatisBatchItemWriter<TempData> writer){
//        Job job = new JobBuilder("testBatch1Job",jobRepository)
//                .start(testStep(jobRepository,transactionManager,reader,writer))
//                .build();
//        return job;
//    }

 	@Bean
 	public Step testStep(JobRepository jobRepository,PlatformTransactionManager transactionManager, JdbcCursorItemReader<TempData> reader, JdbcBatchItemWriter<TempData> writer){
        Step step = new StepBuilder("testStep",jobRepository)
        	.<TempData, TempData> chunk(1, transactionManager)
    		.reader(reader)
    		.writer(writer)
            .build();
        return step;
    }
 	
// 	@Bean
// 	public Step testStep(JobRepository jobRepository,PlatformTransactionManager transactionManager, JdbcCursorItemReader<TempData> reader, MyBatisBatchItemWriter<TempData> writer){
//        Step step = new StepBuilder("testStep",jobRepository)
//        	.<TempData, TempData> chunk(1, transactionManager)
//    		.reader(reader)
//    		.writer(writer)
//            .build();
//        return step;
//    }
	
 	@Bean
 	public JdbcCursorItemReader<TempData> reader() {
 		return new JdbcCursorItemReaderBuilder<TempData>()
	        .fetchSize(1)
	        .dataSource(dataSource)
	        .beanRowMapper(TempData.class)
	        .sql("SELECT NVL(MAX(TEMP_NO), 0) + 1 AS tempNo, TO_CHAR(NVL(MAX(TEMP_NO), 0) + 1) || '_TEST' AS tempNm FROM T_TEMP")
	        .name("tempDataReader")
	        .build();
 	}
 	
 	@Bean
	public JdbcBatchItemWriter<TempData> writer() {
 		System.out.println("***** Test JdbcBatchItemWriter! *****");
		return new JdbcBatchItemWriterBuilder<TempData>()
			.sql("INSERT INTO T_TEMP (TEMP_NO, TEMP_NM, TEMP_DTTM) VALUES (:tempNo, :tempNm, SYSDATE)")
			.dataSource(dataSource)
			.beanMapped()
			.build();
	}
 	
// 	@Bean
// 	public MyBatisBatchItemWriter<TempData> writer() {    
// 		return new MyBatisBatchItemWriterBuilder<TempData>()
// 			.sqlSessionFactory(sqlSessionFactory)
// 			.statementId("TestBatchMapper.insertTemp")
// 			.build();
// 	} 	

// 	@Bean
// 	public Job testJob(JobRepository jobRepository,PlatformTransactionManager transactionManager){
//        Job job = new JobBuilder("testJob",jobRepository)
//                .start(testStep(jobRepository,transactionManager))
//                .build();
//        return job;
//     }
// 	
// 	@Bean
// 	public Step testStep(JobRepository jobRepository,PlatformTransactionManager transactionManager){
//        Step step = new StepBuilder("testStep",jobRepository)
//                .tasklet(testTasklet(),transactionManager)
//                .build();
//        return step;
//    }
// 	
// 	public Tasklet testTasklet(){ 	
//        return ((contribution, chunkContext) -> {
//              System.out.println("***** hello batch! *****");
//              // 원하는 비지니스 로직 작성    
//              testBatchService.saveTemp();
//              return RepeatStatus.FINISHED;
//        });
//    }

 }
