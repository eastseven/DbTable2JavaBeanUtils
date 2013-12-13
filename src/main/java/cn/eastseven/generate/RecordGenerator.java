package cn.eastseven.generate;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import cn.eastseven.App;
import cn.eastseven.DbService;
import cn.eastseven.DbServiceImpl;
import cn.eastseven.model.Column;
import cn.eastseven.model.Table;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class RecordGenerator {

	private static final Logger logger = Logger.getLogger(RecordGenerator.class);
	
	public static void main(String[] args) throws Exception {
		
		PropertiesConfiguration conf = new PropertiesConfiguration("src/main/resources/generator.properties");
		String path = conf.getString("dest.dir") + "/";
		String classPackage = conf.getString("class.package");
		
		DbService gen = new DbServiceImpl();
		
		List<Table> tables = gen.getTablesFromJdbc();
		Writer out = new OutputStreamWriter(System.out);
		for (Table table : tables) {
			freemarker.template.Configuration cfg = new freemarker.template.Configuration(); 
			cfg.setDirectoryForTemplateLoading(new File("src/main/resources/freemarker"));
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			Template temp = cfg.getTemplate("recordbean.ftl");
			Map<String, Object> root = Maps.newHashMap();

			String tableName = table.getName();
			String className = App.getName(tableName);
			
			root.put("package", classPackage);
			root.put("className", className);
			root.put("tableName", tableName);
			
			List<Map<String, Object>> properties = Lists.newArrayList();
			StringBuilder sb = new StringBuilder();
			for(Column c : table.getColumns()) {
				Map<String, Object> e = Maps.newHashMap();
				
				String columnName = c.getName();
				columnName = columnName.substring(0,1).toLowerCase() + columnName.substring(1, columnName.length());
				e.put("name", columnName);
				e.put("type", getType(c.getDataType()));
				
				logger.debug(c);
				properties.add(e);
				
				sb.append(",").append(columnName);
			}
			root.put("properties", properties);
			root.put("propertyNames", sb.toString().replaceFirst(",", ""));
			
			temp.process(root, out);
			out.flush();
			
			FileWriter writer = new FileWriter(new File(path + className+".java"));
			temp.process(root, writer);
			writer.flush();
			writer.close();
		}
		out.close();
	}

	//BYTE, INT, LONG, FLOAT, STRING, DATE
	private static String getType(int type) {
		switch (type) {
		// String
		case Types.CHAR:
			return "STRING";
		case Types.VARCHAR:
			return "STRING";
		case Types.LONGNVARCHAR:
			return "STRING";
		case Types.LONGVARCHAR:
			return "STRING";
			
		// Number
		case Types.INTEGER:
			return "INT";
		case Types.TINYINT:
			return "BYTE";
		case Types.SMALLINT:
			return "BYTE";
		case Types.DECIMAL:
			return "LONG";
		case Types.DOUBLE:
			return "FLOAT";
		case Types.FLOAT:
			return "FLOAT";
		case Types.REAL:
			return "FLOAT"; 
			
		//	Date
		case Types.DATE:
			return "DATE";
		case Types.TIME:
			return "DATE";
		case Types.TIMESTAMP:
			return "DATE";
		
		// LOB
		case Types.BLOB:
			return Object.class.getName();
		case Types.CLOB:
			return "STRING";
		case Types.LONGVARBINARY:
			return "STRING";
		case Types.VARBINARY:
			return Object.class.getName();
			
		// Other
		case Types.BOOLEAN:
			return Boolean.class.getName();
		case Types.BIT:
			return Boolean.class.getName();
			
		default:
			logger.warn("type = " + type);
			return "";
		}
	}
}
