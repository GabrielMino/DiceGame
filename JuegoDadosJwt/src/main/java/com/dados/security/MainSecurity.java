package com.dados.security;

import com.dados.security.jwt.JwtEntryPoint;
import com.dados.security.jwt.JwtTokenFilter;
import com.dados.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)// para indicar a que métodos solo puede acceder el administrador
public class MainSecurity extends WebSecurityConfigurerAdapter {
	
	//Se utliza para adaptar a la clase que utliza Spring Security
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    //Simplemente corrobora que el token sea valido
    @Autowired
    JwtEntryPoint jwtEntryPoint;
    
    
    @Bean
    public JwtTokenFilter jwtTokenFilter(){
        return new JwtTokenFilter();
    }

    //Service interface for encoding passwords. The preferred implementation is BCryptPasswordEncoder.
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //obtener el usuario y cifrar la contraseña
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    //elemento mas imortante
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
    	http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()//aqui puede acceder cualquiera
                .anyRequest().authenticated() // para esto hay que estar autenticado
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtEntryPoint) //y los errores de autorizacion se manejan de esta forma
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//sin cookies
        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class); // pasa el usuario por el proceso de autenticación
    }
}

/*
 .cors()
 
- El Intercambio de Recursos de Origen Cruzado (CORS (en-US)) es un mecanismo que utiliza cabeceras HTTP adicionales
 para permitir que un user agent (en-US) obtenga permiso para acceder a recursos seleccionados desde un servidor,
 en un origen distinto (dominio) al que pertenece. Un agente crea una petición HTTP de origen cruzado cuando solicita
  un recurso desde un dominio distinto, un protocolo o un puerto diferente al del documento que lo generó.
 
 csrf().disable()
 
 Los token CSRF permiten prevenir un frecuente agujero de seguridad de las aplicaciones web llamado "Cross Site Request Forgery". 
 En español sería algo como "falsificación de petición en sitios cruzados" o simplemente falsificación de solicitud entre sitios.

El token CSRF mejora la seguridad porque permite validar que las solicitudes son generadas desde el sitio web autorizado y no desde otras fuentes. 
Para ello se genera una cadena aleatoria y encriptada, que es capaz de ofrecer información solamente al servidor que la ha generado,
 que una vez procesada sirve para validar la procedencia de la solicitud y el usuario que la ha realizado. 
En los formularios se entrega generalmente ese token mediante un input hidden, con lo cual el servidor puede validar el token antes de procesar cualquier solicitud.
 
 The reason to disable CSRF is that the spring boot application is open to the public or it is cumbersome when you are in under development or testing phase.
 
 
 */

