package org.id.bankspringbatch.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BankTransaction {
	@Id
	public Long id;
	public Long accountID;
	public Date transactionDate;
	@Transient
	public String strTransactionDate;
	public String transactionType;
	public Double amount;

}
