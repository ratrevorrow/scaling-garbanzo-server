package com.rtrevorrow.user.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtrevorrow.user.model.LoginCredentials;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    // TODO: create secure key and store in environment variable
    public static final String SECRET = "SECRET";
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

//    AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
        super(new AntPathRequestMatcher("/login"));
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
//        try {
            InputStream inputStream = request.getInputStream();
            LoginCredentials credentials = new ObjectMapper().readValue(inputStream, LoginCredentials.class);

            return this.getAuthenticationManager()
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword(),
                            new ArrayList<>()));
//        } catch (IOException exception) {
//            throw new RuntimeException(exception);
//        } catch (BadCredentialsException exception) {
//            log.error("Bad username or password");
//            throw new BadCredentialsException("Bad username or password");
//        }
    }

//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request,
//                                              HttpServletResponse response,
//                                              AuthenticationException exception) throws IOException, ServletException {
//        log.error("Authorization failed");
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed");
//        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
//        response.getWriter().print(exception.getMessage());
//        response.getWriter().flush();
//    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        Date expiration = new Date(System.currentTimeMillis() + (long) 15 * 1000 * 60L);

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = new ArrayList<>();

        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            roles.add(authority.getAuthority());
        }

        claims.put("username", userDetails.getUsername());
        claims.put("roles", roles);

        String token = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setExpiration(expiration)
                .compact();
        response.setHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}
