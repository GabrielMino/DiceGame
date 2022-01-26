package com.dados.security.jwt;

import com.dados.security.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*la clase más importante del package: va a ejecutar cada petición comprobando que sea válido el token, a través del provider,
en caso de que sea válido el token va a permitir el acceso al recurso o en caso contrariolanzará la excepción
se va a ejecutar una vez por cada petición al servidor por eso se implementa OncePerRqeuestFilter
 */

public class JwtTokenFilter extends OncePerRequestFilter {

    private final static Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UserDetailsServiceImpl userDetailsService;
        
    /* doFilter(ServletRequest request, ServletResponse response)
     Causes the next filter in the chain to be invoked, or if the calling filter is the last filter in the chain,
      causes the resource at the end of the chain to be invoked.*/
    
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(req);
            //Se comprueba que existe el token y que es válido
            if(token != null && jwtProvider.validateToken(token)){
                // A partir de ese token obtenemos el usuario
            	String nombreUsuario = jwtProvider.getNombreUsuarioFromToken(token);
            	// Obtenemos el userDetail para poder obtener las autorizaciones que tiene
                UserDetails userDetails = userDetailsService.loadUserByUsername(nombreUsuario);
                
                // lo autenticamos y vemos autorizaciones
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e){
            logger.error("fail en el método doFilter " + e.getMessage());
        }
        //si no hay ninguna excepcion
        filterChain.doFilter(req, res);
    }
//Extraer el token
    private String getToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer"))
            return header.replace("Bearer ", "");
        return null;
    }
}
