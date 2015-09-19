package com.cspinformatique.kubik.domain.accounting.model;

import java.util.Comparator;

public class EntryComparator implements Comparator<Entry> {
	@Override
	public int compare(Entry o1, Entry o2) {
		if (o1.getDate().getTime() < o2.getDate().getTime()) {
			return -1;
		} else {
			return 1;
		}
	}

}
