package cn.eastseven;

import java.util.List;
import cn.eastseven.model.*;

public interface DbService {

	public static final String DELIMITER = " ";
	
	public void test();
	
	public List<Table> getTablesFromJdbc();
}
