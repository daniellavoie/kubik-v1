package com.cspinformatique.kubik.server.domain.product.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cspinformatique.kubik.server.domain.product.repository.ProductRepository;
import com.cspinformatique.kubik.server.domain.product.repository.ProductStatsRepository;
import com.cspinformatique.kubik.server.model.product.ProductStats;

@Repository
public class ProductStatsRepositoryMysqlImpl implements ProductStatsRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ProductRepository productRepository;

	private String SQL_COUNT_PRODUCT_STATS;
	private String SQL_JOIN_PRODUCT_STATS;
	private String SQL_SELECT_PRODUCT_STATS;

	public ProductStatsRepositoryMysqlImpl() {
		SQL_JOIN_PRODUCT_STATS = "FROM\n"
				+ "	product\n"
				+ "LEFT JOIN\n"
				+ "	(	SELECT\n"
				+ "			ifNULL(sum(product_inventory.quantity_on_hand), 0) qtyOnHand,\n"
				+ "			ifNULL(sum(product_inventory.quantity_on_hold), 0) qtyOnHold,\n"
				+ "			product.id\n"
				+ "		FROM \n"
				+ "			product\n"
				+ "		JOIN\n"
				+ "			product_inventory on product_inventory.product_id = product.id\n"
				+ "		GROUP BY\n"
				+ "			product.id\n"
				+ "	) as inventory ON inventory.id = product.id\n"
				+ "LEFT JOIN\n"
				+ "	(	SELECT\n"
				+ "			ifNULL(sum(purchase_order_detail.quantity), 0) qty,\n"
				+ "			product.id\n"
				+ "		FROM \n"
				+ "			product\n"
				+ "		JOIN\n"
				+ "			purchase_order_detail on purchase_order_detail.product_id = product.id\n"
				+ "		JOIN\n"
				+ "			purchase_order_details on purchase_order_details.details_id = purchase_order_detail.id AND purchase_order_detail.purchase_order_id = purchase_order_details.purchase_order_id\n"
				+ "		JOIN\n"
				+ "			purchase_order on \n"
				+ "				purchase_order.id = purchase_order_detail.purchase_order_id AND \n"
				+ "				purchase_order.status = 'SUBMITED' AND \n"
				+ "				purchase_order.submited_date BETWEEN ? AND ?\n"
				+ "		GROUP BY\n"
				+ "			product.id\n"
				+ "	) as ordered ON ordered.id = product.id\n"
				+ "LEFT JOIN \n"
				+ "	(	SELECT\n"
				+ "			ifNULL(sum(reception_detail.quantity_received), 0) qty,\n"
				+ "			product.id\n"
				+ "		FROM \n"
				+ "			product\n"
				+ "		JOIN\n"
				+ "			reception_detail on reception_detail.product_id = product.id\n"
				+ "		JOIN\n"
				+ "			reception_details on reception_details.details_id = reception_detail.id AND reception_detail.reception_id = reception_details.reception_id\n"
				+ "		JOIN\n"
				+ "			reception on \n"
				+ "				reception.id = reception_detail.reception_id AND \n"
				+ "				reception.status = 'CLOSED' AND\n"
				+ "				reception.date_received BETWEEN ? AND ?\n"
				+ "		GROUP BY\n"
				+ "			product.id\n"
				+ "	) as received on received.id = product.id\n"
				+ "LEFT JOIN\n"
				+ "	(	SELECT\n"
				+ "			ifNULL(sum(rma_detail.quantity), 0) qty,\n"
				+ "			product.id\n"
				+ "		FROM \n"
				+ "			product\n"
				+ "		JOIN\n"
				+ "			rma_detail on rma_detail.product_id = product.id\n"
				+ "		JOIN\n"
				+ "			rma_details on rma_details.details_id = rma_detail.id AND rma_detail.rma_id = rma_details.rma_id\n"
				+ "		JOIN\n"
				+ "			rma on \n"
				+ "				rma.id = rma_detail.rma_id AND \n"
				+ "				rma.status = 'SHIPPED' AND\n"
				+ "				rma.shipped_date BETWEEN ? AND ?\n"
				+ "		GROUP BY\n"
				+ "			product.id\n"
				+ "	) as returned on returned.id = product.id\n"
				+ "LEFT JOIN\n"
				+ "	(	SELECT\n"
				+ "			ifNULL(sum(invoice_detail.quantity), 0) qty,\n"
				+ "			product.id\n"
				+ "		FROM \n"
				+ "			product\n"
				+ "		JOIN\n"
				+ "			invoice_detail on invoice_detail.product_id = product.id\n"
				+ "		JOIN\n"
				+ "			invoice_details on invoice_details.details_id = invoice_detail.id AND invoice_detail.invoice_id = invoice_details.invoice_id\n"
				+ "		JOIN\n"
				+ "			invoice on \n"
				+ "				invoice.id = invoice_detail.invoice_id AND \n"
				+ "				invoice.status_type = 'PAID' AND\n"
				+ "				invoice.paid_date BETWEEN ? AND ?\n"
				+ "		GROUP BY\n"
				+ "			product.id\n"
				+ "	) as sold on sold.id = product.id\n"
				+ "LEFT JOIN\n"
				+ "	(	SELECT\n"
				+ "			ifNULL(sum(customer_credit_detail.quantity), 0) qty,\n"
				+ "			product.id\n"
				+ "		FROM \n"
				+ "			product\n"
				+ "		JOIN\n"
				+ "			customer_credit_detail on customer_credit_detail.product_id = product.id\n"
				+ "		JOIN\n"
				+ "			customer_credit_details on customer_credit_details.details_id = customer_credit_detail.id AND customer_credit_detail.customer_credit_id = customer_credit_details.customer_credit_id\n"
				+ "		JOIN\n"
				+ "			customer_credit on \n"
				+ "				customer_credit.id = customer_credit_detail.customer_credit_id AND \n"
				+ "				customer_credit.status = 'COMPLETED' AND\n"
				+ "				customer_credit.complete_date BETWEEN ? AND ?\n"
				+ "		GROUP BY\n" + "			product.id\n"
				+ "	) as refunded ON refunded.id = product.id\n"
				+ "LEFT JOIN\n"
				+ "	(	SELECT\n"
				+ "			ifNULL(sum(quantity), 0) qty,\n"
				+ "			product_id\n"
				+ "		FROM \n"
				+ "			inventory_count\n"
				+ "		WHERE \n"
				+ "			date_counted BETWEEN ? AND ?\n"
				+ "		GROUP BY\n" + "			product_id\n"
				+ "	) as counted ON counted.product_id = product.id\n"
				+ "WHERE\n"
				+ "	(	inventory.qtyOnHand <= 0 OR inventory.qtyOnHold <= 0 OR ? = false) AND\n"
				+ "	(	ordered.qty is not null OR\n"
				+ "		received.qty is not null OR\n"
				+ "		sold.qty is not null OR\n"
				+ "		returned.qty is not null OR\n"
				+ "		refunded.qty is not null OR\n"
				+ "		counted.qty is not null)";

		this.SQL_COUNT_PRODUCT_STATS = "SELECT\n" + "	count(*)\n"
				+ SQL_JOIN_PRODUCT_STATS;

		this.SQL_SELECT_PRODUCT_STATS = "SELECT\n"
				+ "	product.id as productId,\n" 
				+ " inventory.qtyOnHand as inventory,\n "
				+ " inventory.qtyOnHold as onHold,\n "
				+ "	ordered.qty as ordered,\n"
				+ "	received.qty as received,\n" 
				+ "	sold.qty as sold,\n"
				+ "	returned.qty as returned,\n"
				+ "	refunded.qty as refunded,\n" 
				+ "	counted.qty as counted\n" 
				+ SQL_JOIN_PRODUCT_STATS;
	}

	@Override
	public Page<ProductStats> findAll(Date startDate, Date endDate, boolean withoutInventory,
			Pageable pageable) {
		String query = SQL_SELECT_PRODUCT_STATS;

		if (pageable.getSort() != null) {
			boolean hasOne = false;
			for (Order order : pageable.getSort()) {
				if (!hasOne) {
					query += "\nORDER BY ";
				} else {
					query += ", ";
				}
				query += order.getProperty();

				if (order.getDirection() != null) {
					query += " " + order.getDirection().name();
				}

				hasOne = true;
			}
		}
		
		int limitFrom = (pageable.getPageNumber() == 0) ? 0 : pageable.getPageNumber() * pageable.getPageSize() + 1;
		int limitTo = (pageable.getPageNumber() + 1) * pageable.getPageSize();
		
		query += "\nLIMIT " + limitFrom + ", " + limitTo;

		return new PageImpl<ProductStats>(this.jdbcTemplate.query(
				query,
				new RowMapper<ProductStats>() {
					@Override
					public ProductStats mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						return new ProductStats(productRepository.findOne(rs
								.getInt("productId")), rs.getDouble("ordered"),
								rs.getDouble("received"), rs.getDouble("sold"),
								rs.getDouble("returned"), rs
										.getDouble("refunded"), rs.getDouble("counted"));
					}
				}, startDate, endDate, startDate, endDate, startDate, endDate,
				startDate, endDate, startDate, endDate, startDate, endDate, withoutInventory), pageable,
				this.jdbcTemplate.queryForObject(SQL_COUNT_PRODUCT_STATS,
						Long.class, startDate, endDate, startDate, endDate,
						startDate, endDate, startDate, endDate, startDate,
						endDate, startDate, endDate, withoutInventory));
	}

	public ProductStats findByProductId(int productId, Date startDate,
			Date endDate) {
		String query = SQL_SELECT_PRODUCT_STATS + " AND\n"
				+ "	product.id = ?";

		return this.jdbcTemplate.queryForObject(
				query,
				new RowMapper<ProductStats>() {
					@Override
					public ProductStats mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						return new ProductStats(productRepository.findOne(rs
								.getInt("productId")), rs.getDouble("ordered"),
								rs.getDouble("received"), rs.getDouble("sold"),
								rs.getDouble("returned"), rs
										.getDouble("refunded"), rs.getDouble("counted"));
					}
				}, startDate, endDate, startDate, endDate, startDate, endDate,
				startDate, endDate, startDate, endDate, startDate, endDate, false, productId);
	}

}
