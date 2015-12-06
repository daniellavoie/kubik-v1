package com.cspinformatique.kubik.server.domain.accounting.model;

import java.util.Comparator;

public class EntryComparator implements Comparator<Entry> {
	@Override
	public int compare(Entry o1, Entry o2) {
		try {
			if (o1 != null && o2 != null && o1.getDate().getTime() < o2.getDate().getTime()) {
				return -1;
			} else {
				return 1;
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
