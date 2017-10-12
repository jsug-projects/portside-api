package jsug.portside.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@Profile("basic")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${admin.user}")
	private String adminUser;
	@Value("${admin.pass}")
	private String adminPass;
		
	@Override
	protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().authorizeRequests()
		.anyRequest().authenticated()
        .and().httpBasic()
		;
	}
	
	//authorizeRequests()でやりたかったけど、permitAllでもBasic認証が要求されるので、ignoring()で対応
	//https://stackoverflow.com/questions/30366405/how-to-disable-spring-security-for-particular-url
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
        .antMatchers(HttpMethod.OPTIONS, "/**")//To allow Pre-flight [OPTIONS] request from browser
        .antMatchers(HttpMethod.GET, "/sessions")
        .regexMatchers(HttpMethod.GET, "/sessions/[0-9a-fA-Z]{8}-.+")
		.regexMatchers(HttpMethod.GET, "/attendees/.+/sessions")
		.antMatchers(HttpMethod.POST, "/attendees")
		.regexMatchers(HttpMethod.PUT, "/attendees/[0-9a-fA-Z]{8}-.+")
		.regexMatchers(HttpMethod.GET, "/speakers/.+/image")
		;
    }
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser(adminUser).password(adminPass).roles("ADMIN");
	}
}
