package nus.iss.chatapp.com.server.configs;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class JwtSecurityConfig {

	public static final String[] ENDPOINTS_WHITELIST = {
		"/manifest.json",
		"/**favicon.ico",
		"/runtime.**.js",
		"/polyfills.**.js",
		"/main.**.js",
		"/**.css",
		"/assets/**",
		"/",
		"/index.html",
		"/api/log-in**",
		"/api/sign-up**",
		"/api/auth**",
		"/api/email",
		"/chat",
		"/error/**"
	};

    @Autowired
    private DataSource dataSource;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception  {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder())
            .dataSource(dataSource)
            .usersByUsernameQuery("select email, password, enabled from user_profiles where email=?")
            .authoritiesByUsernameQuery("select email, role from user_profiles where email=?")
        ;
    }
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(
						auth -> {
                            auth.requestMatchers(ENDPOINTS_WHITELIST).permitAll()
							//							"/chat", //websocket
							// 							"/api/email/**", // send email
														// "/api/sign-up", // register
														// "/api/log-in", // login
							// 							"/api/auth", // check email exist
							// 							"/"
							// 							// "/api/chat/**",
							// 							// "/api/profiles/**"
														// ).permitAll()
							.anyRequest().authenticated();
						});
		
		http.sessionManagement(
						session -> 
							session.sessionCreationPolicy(
									SessionCreationPolicy.STATELESS)
						);
		
		http.httpBasic().disable();
		http.csrf().disable();
		http.headers().frameOptions().sameOrigin();
		http.oauth2ResourceServer().jwt();
		
		return http.build();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public KeyPair keyPair() {
		try {
			var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			return keyPairGenerator.generateKeyPair();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	@Bean
	public RSAKey rsaKey(KeyPair keyPair) {
		
		return new RSAKey
				.Builder((RSAPublicKey)keyPair.getPublic())
				.privateKey(keyPair.getPrivate())
				.keyID(UUID.randomUUID().toString())
				.build();
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
		var jwkSet = new JWKSet(rsaKey);
		
		return (jwkSelector, context) ->  jwkSelector.select(jwkSet);
		
	}
	
	@Bean
	public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
		return NimbusJwtDecoder
				.withPublicKey(rsaKey.toRSAPublicKey())
				.build();
		
	}
	
	@Bean
	public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
		return new NimbusJwtEncoder(jwkSource);
	}
}
