package org.id.bankspringbatch.controller;

import java.util.HashMap;
import java.util.Map;

import org.id.bankspringbatch.BankTransactionAnalyticProcessor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankTransactionController {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	@Autowired
	private BankTransactionAnalyticProcessor bankTransactionAnalyticProcessor;

	@RequestMapping("/startJob")
	public BatchStatus launchJob() throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		Map<String, JobParameter> params = new HashMap<>();
		params.put("time", new JobParameter(System.currentTimeMillis()));
		JobParameters jobParameter = new JobParameters(params);
		JobExecution jobExecution = jobLauncher.run(job, jobParameter);
		while (jobExecution.isRunning()) {
			System.out.println("......");
		}
		return jobExecution.getStatus();
	}

	@RequestMapping("/analytics")
	public Map<String, Double> analytics() {
		Map<String, Double> map = new HashMap<>();
		// je dois accéder au 2éme process
		map.put("totalCredit", bankTransactionAnalyticProcessor.getTotalCredit());
		map.put("totalDebit", bankTransactionAnalyticProcessor.getTotalDebit());
		return map;
	}

}
