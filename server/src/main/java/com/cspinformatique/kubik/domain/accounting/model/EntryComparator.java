package com.cspinformatique.kubik.domain.accounting.model;

import java.util.Comparator;

public class EntryComparator implements Comparator<Entry> {
	@Override
	public int compare(Entry o1, Entry o2) {
		Long invoiceNumber1 = Long.parseLong(o1.getInvoiceNumber());
		Long invoiceNumber2 = Long.parseLong(o2.getInvoiceNumber());

		if (invoiceNumber1 < invoiceNumber2) {
			return -1;
		} else if (invoiceNumber1 == invoiceNumber2) {
			if (o1.getDate().before(o2.getDate())) {
				return -1;
			} else {
				return 1;
			}
		} else {
			return 1;
		}
	}

}
