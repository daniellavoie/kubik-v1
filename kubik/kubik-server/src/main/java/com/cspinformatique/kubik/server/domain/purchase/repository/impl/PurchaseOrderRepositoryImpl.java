package com.cspinformatique.kubik.server.domain.purchase.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cspinformatique.kubik.server.domain.purchase.repository.PurchaseOrderRepositoryCustom;

@Repository
public class PurchaseOrderRepositoryImpl implements PurchaseOrderRepositoryCustom {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Long> findIdsWithDilicomOrders() {
		return jdbcTemplate.query("SELECT id FROM purchase_order WHERE sent_to_dilicom = true and dilicom_order_id IS NULL", new RowMapper<Long>() {
			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong("id");
			}
		});
	}

}
