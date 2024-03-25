package projet.bid_pro.configuration;
import java.io.IOException;
import java.util.Locale;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
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
			auth.requestMatchers(HttpMethod.POST,"/register").permitAll();
			//auth.requestMatchers(HttpMethod.GET,"/encheres").hasRole("ADMIN");
			auth.requestMatchers(HttpMethod.GET,"/encheres/*").fullyAuthenticated();
			auth.requestMatchers(HttpMethod.POST,"/encheres/*").fullyAuthenticated();
			auth.requestMatchers(HttpMethod.GET,"/gestionUtilisateur").hasAnyRole("ADMIN");
			auth.requestMatchers(HttpMethod.GET,"/utilisateurEtat").hasAnyRole("ADMIN");;
			auth.requestMatchers(HttpMethod.GET,"/article/*").fullyAuthenticated();
			auth.requestMatchers(HttpMethod.GET,"/profilEnchere").fullyAuthenticated();
			auth.requestMatchers(HttpMethod.GET,"/").permitAll();
			auth.requestMatchers("/css/*").permitAll();
			auth.requestMatchers("/images/*").permitAll();
			auth.requestMatchers("/*").permitAll();// C'est la fête, tout le monde à le droit

			auth.anyRequest().denyAll();// Seulement les utilisateurs non connectés ont accès
		}).csrf(AbstractHttpConfigurer::disable);

		//http.formLogin(Customizer.withDefaults());
		http
				.formLogin(form -> form
						.loginPage("/login")
						.defaultSuccessUrl("/verifActif")
						.failureUrl("/login?loginError=true"))
				.logout(logout -> logout
						.logoutSuccessUrl("/")
						.deleteCookies("JSESSIONID"));

		return http.build();
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {
		// Rediriger l'utilisateur vers la route /verifEtat après une connexion réussie
		response.sendRedirect("/verifEtat");
	}

}
