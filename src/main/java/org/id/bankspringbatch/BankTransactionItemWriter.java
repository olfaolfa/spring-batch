package org.id.bankspringbatch;

import java.util.List;

import org.id.bankspringbatch.dao.BankTransactionRepository;
import org.id.bankspringbatch.entity.BankTransaction;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BankTransactionItemWriter implements ItemWriter<BankTransaction> {
	@Autowired
	private BankTransactionRepository bankTransactionRepository;

	@Override
	public void write(List<? extends BankTransaction> items) throws Exception {
		bankTransactionRepository.saveAll(items);
	}

}
