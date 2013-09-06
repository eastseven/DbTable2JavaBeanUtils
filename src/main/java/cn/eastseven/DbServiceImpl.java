package cn.eastseven;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.eastseven.model.Column;
import cn.eastseven.model.Table;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class DbServiceImpl implements DbService {

	static Logger logger = Logger.getLogger(DbServiceImpl.class);

	private DatabaseMetaData databaseMetaData;

	public void test() {}

	public List<Table> getTablesFromJdbc() {
		List<Table> tables = Lists.newArrayList();
		
		String[] types = new String[] { "TABLE"/*, "VIEW"*/ };
		String catalog = null;
		String schemaPattern = null;
		String tableNamePattern = null;

		Connection conn = null;
		ResultSet rs = null;
		
		try {

			PropertiesConfiguration conf = new PropertiesConfiguration("src/main/resources/jdbc.properties");
			
			catalog          = StringUtils.isBlank(conf.getString("db.catalog")) ? null : conf.getString("db.catalog");
			schemaPattern    = StringUtils.isBlank(conf.getString("db.schema.pattern")) ? null : conf.getString("db.schema.pattern");
			tableNamePattern = StringUtils.isBlank(conf.getString("db.table.name.pattern")) ? null : conf.getString("db.table.name.pattern");
			
			Class.forName(conf.getString("db.driver"));
			conn = DriverManager.getConnection(conf.getString("db.url"), conf.getString("db.username"), conf.getString("db.password"));
			
			databaseMetaData = conn.getMetaData();
			rs = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types);
			printDatabaseMetaData(databaseMetaData);
			
			while(rs.next()) {
				Table table = new Table(rs.getString(Table.TABLE_CAT), rs.getString(Table.TABLE_SCHEM), rs.getString(Table.TABLE_NAME), rs.getString(Table.REMARKS));
				logger.info(table);
				ResultSet _rs = databaseMetaData.getColumns(catalog, schemaPattern, table.getName(), null);
				//printResultSet(_rs);
				Set<Column> columns = Sets.newHashSet();
				while(_rs.next()) {
					Column column = new Column();
					column.setDataType(_rs.getInt(Column.DATA_TYPE));
					column.setDefaultValue(_rs.getString(Column.COLUMN_DEF));
					column.setName(_rs.getString(Column.COLUMN_NAME));
					column.setNullable("YES".equals(_rs.getString(Column.IS_NULLABLE)));
					column.setOrdinalPosition(_rs.getInt(Column.ORDINAL_POSITION));
					column.setRemarks(_rs.getString(Column.REMARKS));
					column.setSize(_rs.getInt(Column.COLUMN_SIZE));
					column.setTypeName(_rs.getString(Column.TYPE_NAME));
					columns.add(column);
				}
				
				table.setColumns(columns);
				tables.add(table);
				logger.debug(table);
				_rs.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			databaseMetaData = null;
		}
		
		return tables;
	}

	private void printDatabaseMetaData(DatabaseMetaData databaseMetaData) {
		ResultSet catalogs = null;
		ResultSet schemas  = null;
		try {
			
			catalogs = databaseMetaData.getCatalogs();
			schemas  = databaseMetaData.getSchemas();
			
			logger.debug("----- Catalogs -----");
			while(catalogs.next()) {
				logger.debug(catalogs.getObject(1));
			}
			
			logger.debug("----- Schemas -----");
			while(schemas.next()) {
				logger.debug(schemas.getObject(1));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(catalogs != null) {
				try {
					catalogs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (schemas != null) {
				try {
					schemas.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void printResultSet(ResultSet rs) throws SQLException {
		logger.info("----------");
		ResultSetMetaData rsmd = rs.getMetaData();
		final int count = rsmd.getColumnCount();
		while(rs.next()) {
			for(int index = 1; index <= count; index++) {
				logger.debug(rsmd.getColumnName(index) + ": " + rs.getObject(index));
			}
		}
		//rs.first();
	}
}
