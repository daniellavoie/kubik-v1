package db.migration.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cspinformatique.kubik.server.model.product.PropertyName;
import com.cspinformatique.kubik.server.model.product.PropertyType;

public class V3_0_0_1__Online_Sales implements SpringJdbcMigration {

	@Override
	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		jdbcTemplate.query("select * from product", rs -> {
			insertProperty(jdbcTemplate, rs, rs.getInt("id"), PropertyName.AUTHOR, PropertyType.STRING,
					rs.getString("author"));
			insertProperty(jdbcTemplate, rs, rs.getInt("id"), PropertyName.PUBLISHED_DATE, PropertyType.DATE,
					rs.getDate("date_published"));
			insertProperty(jdbcTemplate, rs, rs.getInt("id"), PropertyName.COLLECTION, PropertyType.STRING,
					rs.getString("collection"));
			insertProperty(jdbcTemplate, rs, rs.getInt("id"), PropertyName.COLLECTION_REFERENCE, PropertyType.STRING,
					rs.getString("collection_reference"));
			insertProperty(jdbcTemplate, rs, rs.getInt("id"), PropertyName.EAN13, PropertyType.STRING,
					rs.getString("ean13"));
			insertProperty(jdbcTemplate, rs, rs.getInt("id"), PropertyName.EDITOR_ISBN, PropertyType.STRING,
					rs.getString("publisher_isnb"));
			insertProperty(jdbcTemplate, rs, rs.getInt("id"), PropertyName.EDITOR_NAME, PropertyType.STRING,
					rs.getString("publisher"));
			insertProperty(jdbcTemplate, rs, rs.getInt("id"), PropertyName.EDITOR_PRESENTATION, PropertyType.STRING,
					rs.getString("publisher_presentation"));
			insertProperty(jdbcTemplate, rs, rs.getInt("id"), PropertyName.ISBN, PropertyType.STRING,
					rs.getString("isbn"));
			insertProperty(jdbcTemplate, rs, rs.getInt("id"), PropertyName.THEME, PropertyType.STRING,
					rs.getString("theme"));
			insertProperty(jdbcTemplate, rs, rs.getInt("id"), PropertyName.TITLE, PropertyType.STRING,
					rs.getString("extended_label"));
		});
	}

	private void insertProperty(JdbcTemplate jdbcTemplate, ResultSet rs, int productId, PropertyName name,
			PropertyType type, Object value) throws DataAccessException, SQLException {
		if (!rs.wasNull() && value != null)
			jdbcTemplate.update("INSERT into product_property (product_id, name, type, value) VALUES (?,?,?,?)",
					productId, name.toString(), type.toString(), value.toString());
	}
}
