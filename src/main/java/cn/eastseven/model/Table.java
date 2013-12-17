package cn.eastseven.model;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * TABLE_CAT String => table catalog (may be null)
TABLE_SCHEM String => table schema (may be null)
TABLE_NAME String => table name
TABLE_TYPE String => table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
REMARKS String => explanatory comment on the table
TYPE_CAT String => the types catalog (may be null)
TYPE_SCHEM String => the types schema (may be null)
TYPE_NAME String => type name (may be null)
SELF_REFERENCING_COL_NAME String => name of the designated "identifier" column of a typed table (may be null)
REF_GENERATION String => specifies how values in SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER", "DERIVED". (may be null)
 * @author eastseven
 *
 */
public class Table {

	public static final String TABLE_CAT                 = "TABLE_CAT";
	public static final String TABLE_SCHEM               = "TABLE_SCHEM";
	public static final String TABLE_NAME                = "TABLE_NAME";
	public static final String TABLE_TYPE                = "TABLE_TYPE";
	public static final String REMARKS                   = "REMARKS";
	public static final String TYPE_CAT                  = "TYPE_CAT";
	public static final String TYPE_SCHEM                = "TYPE_SCHEM";
	public static final String TYPE_NAME                 = "TYPE_NAME";
	public static final String SELF_REFERENCING_COL_NAME = "SELF_REFERENCING_COL_NAME";
	public static final String REF_GENERATION            = "REF_GENERATION";
	
	private String catalog;

	private String schema;

	private String name;

	private String remarks;

	private List<Column> columns = Lists.newArrayList();

	public Table(String catalog, String schema, String name, String remarks) {
		super();
		this.catalog = catalog;
		this.schema  = schema;
		this.name    = name;
		this.remarks = remarks;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	@Override
	public String toString() {
		return "Table [catalog=" + catalog + ", schema=" + schema + ", name=" + name + ", remarks=" + remarks + ", columns=" + columns + "]";
	}

}