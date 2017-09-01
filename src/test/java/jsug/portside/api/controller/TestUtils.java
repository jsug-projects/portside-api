package jsug.portside.api.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class TestUtils {

	public static void resetDb(DataSource ds) {
		JdbcTemplate jt = new JdbcTemplate(ds);
		jt.update("SET REFERENTIAL_INTEGRITY FALSE");
		List<Map<String, Object>> tables = jt.queryForList("show tables");
				
		for (Map table : tables) {
			jt.update("delete from " + table.get("TABLE_NAME"));
		}
		jt.update("SET REFERENTIAL_INTEGRITY TRUE");
		
	}
	
	
	public static UUID getFromLocation(String location) {
		return UUID.fromString(location.substring(location.lastIndexOf('/')+1));
	}
}
