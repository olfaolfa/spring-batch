package org.id.bankspringbatch;

import java.util.ArrayList;
import java.util.List;

import org.id.bankspringbatch.entity.BankTransaction;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Autowired
	private ItemReader<BankTransaction> bankTransactionItemReader;
	@Autowired
	private ItemWriter<BankTransaction> bankTransactionItemWriter;
	@Autowired
	private ItemProcessor<BankTransaction, BankTransaction> bankTransactionItemProcessor;
	@Autowired
	private ItemProcessor<BankTransaction, BankTransaction> bankTransactionAnalyticProcessor;
	

	//@Autowired
	// private ItemProcessor<BankTransaction, BankTransaction>
	// bankTransactionItemProcessor;

	@Bean
	public Job bankJob() {
		Step step1 = stepBuilderFactory.get("step1").<BankTransaction, BankTransaction>chunk(100)
				.reader(bankTransactionItemReader)
				// .processor(bankTransactionItemProcessor)
				.processor(compositeItemProcessor()) // compositee iteem
														// proceessor
				.writer(bankTransactionItemWriter).build();
		return jobBuilderFactory.get("bank-data-loardeer-job").start(step1).build();

	}

	@Bean
	public CompositeItemProcessor<BankTransaction, BankTransaction> compositeItemProcessor() {
		CompositeItemProcessor<BankTransaction, BankTransaction> compositeOfItemProcessor = new CompositeItemProcessor<>();
		List<ItemProcessor<BankTransaction, BankTransaction>> listOfItemsProcessor = new ArrayList<>();
		listOfItemsProcessor.add(bankTransactionItemProcessor);
		listOfItemsProcessor.add(bankTransactionAnalyticProcessor);
		compositeOfItemProcessor.setDelegates(listOfItemsProcessor);
		return compositeOfItemProcessor;

	}

	@Bean
	public FlatFileItemReader<BankTransaction> fileItemReader(@Value("${inputFile}") Resource inputFile) {
		FlatFileItemReader<BankTransaction> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setName("CSV-READER");
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setResource(inputFile);
		flatFileItemReader.setLineMapper(lineMapper());
		return flatFileItemReader;

	}

	@Bean
	public LineMapper<BankTransaction> lineMapper() {
		DefaultLineMapper<BankTransaction> lineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");// le séparateur
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames("id", "accountID", "strTransactionDate", "transactionType", "amount");
		lineMapper.setLineTokenizer(lineTokenizer);
		BeanWrapperFieldSetMapper<BankTransaction> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(BankTransaction.class);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		return lineMapper;
	}

}
