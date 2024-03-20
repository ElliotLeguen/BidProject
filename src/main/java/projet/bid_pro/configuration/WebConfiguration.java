package projet.bid_pro.configuration;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebConfiguration implements WebMvcConfigurer {
	@Bean
	LocaleResolver localeResolver() {
		System.out.println("localeResolver");
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(new Locale("fr"));
		return slr;
	}

	@Bean
	LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("language");
		return localeChangeInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	@Bean
	UserDetailsManager userDetailsManager(DataSource datasource) {
		var jdbcUserDetailsManager = new JdbcUserDetailsManager(datasource);
		jdbcUserDetailsManager.setUsersByUsernameQuery("select email, mot_de_passe, 1 from UTILISATEURS where email=?");
		jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select email, administrateur from UTILISATEURS where email=?");

		return jdbcUserDetailsManager;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http ) throws Exception
	{
		http.authorizeHttpRequests(auth->{
			auth.requestMatchers(HttpMethod.GET,"/login").anonymous();
			auth.requestMatchers(HttpMethod.GET,"/register").permitAll();
			auth.requestMatchers(HttpMethod.POST,"/register/save").permitAll();
			//auth.requestMatchers(HttpMethod.GET,"/encheres").hasRole("ADMIN");
			auth.requestMatchers(HttpMethod.GET,"/encheres").fullyAuthenticated();
			auth.requestMatchers(HttpMethod.GET,"/").permitAll();
			auth.requestMatchers(HttpMethod.GET,"/encheres/detail").hasRole("1");
			auth.requestMatchers(HttpMethod.GET,"/encheres/creer").hasRole("0");
			auth.requestMatchers("/css/*").permitAll();
			auth.requestMatchers("/images/*").permitAll();
			auth.requestMatchers("/*").permitAll();// C'est la fête, tout le monde à le droit

			auth.anyRequest().denyAll();// Seulement les utilisateurs non connectés ont accès
		}).csrf(AbstractHttpConfigurer::disable);

		//http.formLogin(Customizer.withDefaults());
		http
				.formLogin(form -> form
						.loginPage("/login")
						.successHandler(new SimpleUrlAuthenticationSuccessHandler("/loginSuccessHandler"))
						.failureUrl("/login-error"))
				.logout(logout -> logout
						.logoutSuccessUrl("/")
						.deleteCookies("JSESSIONID"));

		return http.build();
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
