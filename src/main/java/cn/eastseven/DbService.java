package cn.eastseven;

import java.util.List;
import cn.eastseven.model.*;

public interface DbService {

	public void test();
	
	public List<Table> getTablesFromJdbc();
}
