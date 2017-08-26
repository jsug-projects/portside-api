package jsug.portside.api.config;

import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class DbConfig {
	@Bean
	public DataSource dataSource() {
		//TODO Testのとき読み込まれない?
		
		
		System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEee");
		org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();
		PoolProperties p = new PoolProperties();
		p.setUrl("jdbc:log4jdbc:h2:mem:testdb;MODE=MYSQL");
		//p.setUrl("");
		p.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
		p.setUsername("sa");
		p.setPassword("");
		p.setJmxEnabled(true);
		p.setTestWhileIdle(false);
		p.setTestOnBorrow(true);
		p.setValidationQuery("SELECT 1");
		p.setTestOnReturn(false);
		p.setValidationInterval(30000);
		p.setTimeBetweenEvictionRunsMillis(30000);
		p.setMaxActive(100);
		p.setInitialSize(10);
		p.setMaxWait(10000);
		p.setRemoveAbandonedTimeout(60);
		p.setMinEvictableIdleTimeMillis(30000);
		p.setMinIdle(10);
		p.setLogAbandoned(true);
		p.setRemoveAbandoned(true);
		p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
				+ "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
		ds.setPoolProperties(p);

		return ds;
	}

}
