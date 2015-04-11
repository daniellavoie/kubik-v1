package com.cspinformatique.kubik.domain.dilicom.batch.separator;

import org.springframework.batch.item.file.separator.RecordSeparatorPolicy;

public class ShippingNoticeRecordSeparator implements RecordSeparatorPolicy {

	@Override
	public boolean isEndOfRecord(String record) {
		return false;
	}

	@Override
	public String postProcess(String record) {
		return record;
	}

	@Override
	public String preProcess(String record) {
		return record;
	}

}
