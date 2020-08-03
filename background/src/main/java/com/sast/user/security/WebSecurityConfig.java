package com.sast.user.security;

import com.sast.user.security.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;
    @Resource
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Resource
    private MyAccessDeniedHandler myAccessDeniedHandler;
    @Resource
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Resource
    private MyLogoutSuccessHandler myLogoutSuccessHandler;
    private CustomUserDetailsService userDetailsService;
    private DataSource dataSource;
    private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    public void setAuthenticationDetailsSource(AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource) {
        this.authenticationDetailsSource = authenticationDetailsSource;
    }

    @Autowired
    public void setUserDetailsService(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    public void setCustomAuthenticationProvider(CustomAuthenticationProvider customAuthenticationProvider) {
        this.customAuthenticationProvider = customAuthenticationProvider;
    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic().authenticationEntryPoint(myAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                // 如果有允许匿名的url，填在下面
                .antMatchers("/getVerifyCode").permitAll()
                .antMatchers("/register/*").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/druid/*").permitAll()
                .and()
                // 设置登陆页
                .formLogin().loginPage("/login")
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHandler)
                .permitAll()
                // 指定authenticationDetailsSource，增加登陆时所需的参数
                .authenticationDetailsSource(authenticationDetailsSource)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(myLogoutSuccessHandler)
                .permitAll()
                .and().rememberMe()
                .userDetailsService(userDetailsService);

        http.rememberMe().rememberMeParameter("remember-me")
                .userDetailsService(userDetailsService).tokenValiditySeconds(1000);

        http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler); // 无权访问 JSON 格式的数据

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 设置拦截忽略文件夹，可以对静态资源放行
        web.ignoring().antMatchers("/css/**", "/js/**");
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        String separator = System.lineSeparator();

        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ROOT > ROLE_ADMIN " + separator + " ROLE_ADMIN > ROLE_NORMAL";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }
}
