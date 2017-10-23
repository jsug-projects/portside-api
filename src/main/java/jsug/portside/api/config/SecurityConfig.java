package jsug.portside.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

@ConditionalOnProperty({ "admin.user", "admin.pass" })
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${admin.user}")
	private String adminUser;
	@Value("${admin.pass}")
	private String adminPass;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests() //
				.mvcMatchers("/cloudfoundryapplication/**").permitAll() //
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll() //
				.mvcMatchers(HttpMethod.GET, "/sessions").permitAll() //
				.mvcMatchers(HttpMethod.GET, "/sessions/withAttendeeCount")
				.authenticated() //
				.mvcMatchers(HttpMethod.GET, "/sessions/{id}").permitAll() //
				.mvcMatchers(HttpMethod.GET, "/attendees/{id}/sessions").permitAll() //
				.mvcMatchers(HttpMethod.POST, "/attendees").permitAll() //
				.mvcMatchers(HttpMethod.PUT, "/attendees/{id}").permitAll() //
				.mvcMatchers(HttpMethod.GET, "/speakers/{id}/image").permitAll() //
				.anyRequest().authenticated() //
				.and() //
				.exceptionHandling()
				.authenticationEntryPoint(new Http403ForbiddenEntryPoint()) // To prevent popup when basic authentication fails
				.and() //
				.httpBasic() //
				.and() //
				.csrf().disable() //
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication() //
				.withUser(adminUser).password(adminPass).roles("ADMIN");
	}
}
