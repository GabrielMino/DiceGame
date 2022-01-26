package com.dados.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


 //Esta clase combrobara si el token es válido y en caso que no, devolverá una respuesta 401 NO AUTORIZADO


@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

    private final static Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException, ServletException {
        logger.error("Fail in commence method.");
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "no autorizado");
    }
}

/*
 AuthenticationEntryPoint:
 It is an interface implemented by ExceptionTranslationFilter, basically a filter which is the first point of entry for Spring Security. 
 It is the entry point to check if a user is authenticated and logs the person in or throws exception (unauthorized). 
 Usually the class can be used like that in simple applications but when using Spring security in REST, JWT etc one will have to extend it to provide better Spring Security filter chain management.
 */
