package com.cspinformatique.kubik.server.domain.accounting.service;

import java.util.Date;
import java.util.List;

import com.cspinformatique.kubik.server.domain.accounting.model.Account;
import com.cspinformatique.kubik.server.domain.accounting.model.Entry;

public interface EntryService {
	List<Account> generateAccounts();
	
	List<Entry> generateSaleJournalEntries(Date startDate, Date endDate);
	
	List<Entry> generateTreasuryJournalEntries(Date startDate, Date endDate);
}
