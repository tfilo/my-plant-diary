package sk.filo.plantdiary.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sk.filo.plantdiary.jwt.AuthEntryPoint;
import sk.filo.plantdiary.jwt.AuthFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class
SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private AuthFilter authFilter;

    private AuthEntryPoint authEntryPoint;

    @Autowired
    public void setAuthFilter(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Autowired
    public void setAuthEntryPoint(AuthEntryPoint authEntryPoint) {
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/auth").permitAll() // allow everyone to authenticate
                .antMatchers("/api/user/register").permitAll() // allow everyone to register
                .antMatchers("/api/user/activate").permitAll() // allow everyone to activate account
                .antMatchers("/api/**").authenticated() // protect rest api
                .anyRequest().permitAll() // allow everyone static content
                .and()
                .exceptionHandling().authenticationEntryPoint(authEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
