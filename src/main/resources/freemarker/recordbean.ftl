package ${package};

import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.silio.db.DBOperate;

import com.zobio.comm.Field;
import com.zobio.comm.Record;
import com.zobio.comm.Table;

public class ${className} extends Record<Object> {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(${className}.class);
	
	private static Table records  = null;
	private static Field[] fields = null;
	
	public static enum F {
		${propertyNames} ;
	}
	
	public static void init() {
		if (fields == null) {
			fields = new Field[] {
				<#list properties as property>
				new Field(Field.Type.${property.type}, F.${property.name}.name()),
				</#list>
			};
			records = new Table(fields);
			records.setUpdateTable("${tableName}");
		}

		if (records != null && records.size() > 0)
			records.clear();

		DBOperate db = DBOperate.getDBOperate("game_db");
		${className} record = null;
		try {
			String sql = "select";
			sql += " ${propertyNames} ";
			sql += " from ${tableName} ";
			Statement st = db.statement();
			ResultSet rs = st.executeQuery(sql);

			String id = null;
			while (rs.next()) {
				id = rs.getString(1);
				record = new ${className}();
				records.addRecord(id, record);
				record.setValue(fields[0].name(), id);

				for (int i = 1; i < fields.length; i++) {
					record.setValue(fields[i].name(), rs.getString(i + 1));
				}
				record.setStatus(false);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection();
		}

		log.debug("init ${tableName}:" + records.size());

	}

}