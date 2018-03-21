package jsug.portside.api.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


//@ConditionalOnProperty(matchIfMissing=true, value={ "admin.user", "admin.pass" })//プロパティが指定されてもマッチしてしまうなー。
@ConditionalOnMissingBean(SecurityConfig.class)//とりいそぎ
@Configuration
public class NoSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring().antMatchers("/**");
	}
}
