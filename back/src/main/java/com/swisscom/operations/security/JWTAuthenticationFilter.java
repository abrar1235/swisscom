package com.swisscom.operations.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.swisscom.operations.util.AppUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static com.swisscom.operations.constant.Keys.ROLE;
import static com.swisscom.operations.constant.Keys.USER_ID;

@Order(99)
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private final AppUtil appUtil;
    private final String[] secureLinks;

    public JWTAuthenticationFilter(AppUtil appUtil, String[] secureLinks){
        this.appUtil = appUtil;
        this.secureLinks = secureLinks;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            String uri = request.getRequestURI();
            if(Stream.of(secureLinks).noneMatch(link -> link.equalsIgnoreCase(uri))) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = request.getHeader(AUTHORIZATION).replace("Bearer ", "");
            DecodedJWT decodedJWT = appUtil.parseToken(token);
            String userId = decodedJWT.getClaim(USER_ID).asString();
            List<GrantedAuthority> roles = decodedJWT.getClaim(ROLE).asList(GrantedAuthority.class);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, roles);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("an error occurred while authenticating token", e);
            JSONObject error = new JSONObject();
            error.put("error", "unauthorized");
            error.put("message", "Token Expired/Invalid Token");
            error.put("timestamp", new Date().getTime());
            error.put("path", request.getRequestURI());
            PrintWriter out = response.getWriter();
            out.print(error);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
