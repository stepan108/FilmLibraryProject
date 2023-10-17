//package ru.stephen.filmlibrary.library.config.jwt;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import ru.stephen.filmlibrary.library.service.userdetails.CustomUserDetails;
//import ru.stephen.filmlibrary.library.service.userdetails.CustomUserDetailsService;
//
//import java.io.IOException;
//
//@Component
//public class JWTTokenFilter
//        extends OncePerRequestFilter {
//    private final JWTTokenUtil jwtTokenUtil;
//    private final CustomUserDetailsService customUserDetailsService;
//
//    public JWTTokenFilter(JWTTokenUtil jwtTokenUtil, CustomUserDetailsService customUserDetailsService) {
//        this.jwtTokenUtil = jwtTokenUtil;
//        this.customUserDetailsService = customUserDetailsService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        String token = null;
//        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
//        if (header == null || header.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        token = header.split(" ")[1].trim();
//        UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtTokenUtil.getUserNameFromToken(token));
//
//        if (!jwtTokenUtil.validateToken(token, userDetails)) {
//          filterChain.doFilter(request, response);
//          return;
//        }
//
//        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                userDetails,
//                null,
//                userDetails.getAuthorities()
//        );
//
//        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        filterChain.doFilter(request, response);
//    }
//}
