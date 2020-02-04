package me.emicsto.flashcards.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import me.emicsto.flashcards.user.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthorizationFilter extends GenericFilterBean {
    private final CustomUserDetailsService userDetailsService;
    private final TokenProvider tokenProvider;

    private static final String HEADER_STRING = "Authorization";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String token = httpRequest.getHeader(HEADER_STRING);

        if (token != null) {
            try {
                Authentication authentication = getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (InvalidTokenException ex) {
                httpResponse.setStatus(HttpStatus.SC_UNAUTHORIZED);
                return;
            }
        }

        chain.doFilter(httpRequest, httpResponse);
    }

    private Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUsername(String token) {
        DecodedJWT decodedToken = tokenProvider.getDecodedToken(token);
        return decodedToken.getSubject();
    }
}
