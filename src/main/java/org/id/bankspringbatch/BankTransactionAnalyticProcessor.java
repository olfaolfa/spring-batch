package org.id.bankspringbatch;

import org.id.bankspringbatch.entity.BankTransaction;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class BankTransactionAnalyticProcessor implements ItemProcessor<BankTransaction, BankTransaction> {
	@Getter
	private double totalDebit;
	@Getter
	private double totalCredit;

	@Override
	public BankTransaction process(BankTransaction bankItem) throws Exception {
		if ("D".equals(bankItem.getTransactionType()))
			totalDebit += bankItem.getAmount();
		else
			totalCredit += bankItem.getAmount();
		return bankItem;
	}
}
