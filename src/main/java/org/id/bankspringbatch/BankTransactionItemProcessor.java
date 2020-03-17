package org.id.bankspringbatch;

import java.text.SimpleDateFormat;

import org.id.bankspringbatch.entity.BankTransaction;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class BankTransactionItemProcessor implements ItemProcessor<BankTransaction, BankTransaction> {
	private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy-HH:mm");

	@Override
	public BankTransaction process(BankTransaction bankItem) throws Exception {
		bankItem.setTransactionDate(format.parse(bankItem.getStrTransactionDate()));
		return bankItem;
	}

}
