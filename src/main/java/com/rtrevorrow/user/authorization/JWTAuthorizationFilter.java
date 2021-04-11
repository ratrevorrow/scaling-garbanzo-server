package com.rtrevorrow.user.authorization;

import com.rtrevorrow.user.authentication.JWTAuthenticationFilter;
import com.rtrevorrow.user.authentication.ServerUserDetailsService;
import com.rtrevorrow.user.model.UserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    ServerUserDetailsService serverUserDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, ServerUserDetailsService serverUserDetailsService) {
        super(authenticationManager);
        this.serverUserDetailsService = serverUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JWTAuthenticationFilter.HEADER_STRING);

        if (Objects.isNull(header) || !header.startsWith(JWTAuthenticationFilter.TOKEN_PREFIX)) {
            log.info("Header = {}", header);
            chain.doFilter(request, response);
            return;
        }

        try {
            UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            try {
                chain.doFilter(request, response);
            } finally {
                SecurityContextHolder.clearContext();
            }
        } catch (ExpiredJwtException exception) {
            request.setAttribute("exception", exception);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        log.info("Authenticating user");

        String token = request
                .getHeader(JWTAuthenticationFilter.HEADER_STRING)
                .replaceAll(JWTAuthenticationFilter.TOKEN_PREFIX, "");

        log.debug("Token = {}", token);

        if (Objects.nonNull(token)) {
            Claims claims = Jwts.parser().setSigningKey(JWTAuthenticationFilter.SECRET).parseClaimsJws(token).getBody();

            log.info("Authenticated: {}", claims.getSubject());

            List<String> roles = claims.get("roles", ArrayList.class);

            List<GrantedAuthority> authorities = new ArrayList<>();

            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }

            UserDetails userDetails = serverUserDetailsService.loadUserByUsername(claims.get("username", String.class));

            return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        }

        return null;
    }
}
