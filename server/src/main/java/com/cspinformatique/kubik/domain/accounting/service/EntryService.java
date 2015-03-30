package com.cspinformatique.kubik.domain.accounting.service;

import java.util.Date;
import java.util.List;

import com.cspinformatique.kubik.domain.accounting.model.Entry;

public interface EntryService {
	List<Entry> generateSaleJournalEntries(Date startDate, Date endDate);
	
	List<Entry> generateTreasuryJournalEntries(Date startDate, Date endDate);
}
