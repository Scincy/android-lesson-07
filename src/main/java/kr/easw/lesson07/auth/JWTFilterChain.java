package kr.easw.lesson07.auth;

import kr.easw.lesson07.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JWTFilterChain extends OncePerRequestFilter {
    private final JwtService jwtService;

    private final JPAUserDetailService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getHeader("Authorization") == null) 
        {
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader("Authorization");
        switch (jwtService.validate(token)) {
            case VALID:
                String userName = jwtService.extractUsername(token);
                UserDetails details = userDetailsService.loadUserByUsername(userName);
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                        details,
                        details.getPassword(),
                        details.getAuthorities()
                ));
                filterChain.doFilter(request, response);
                return;
            case EXPIRED:
                response.sendError(401, "만료된 토큰");
                break;
            case UNSUPPORTED:
                response.sendError(401, "지원 안되는 토큰");
                break;
            case INVALID:
                response.sendError(401, "이상한 토큰");
                break;
        }
    }
}