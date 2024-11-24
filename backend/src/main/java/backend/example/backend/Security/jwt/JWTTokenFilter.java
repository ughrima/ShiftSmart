
package backend.example.backend.Security.jwt;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import backend.example.backend.Service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTTokenFilter extends OncePerRequestFilter {

    private final JWTTokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    public JWTTokenFilter(JWTTokenProvider tokenProvider, CustomUserDetailsService customUserDetailsService) {
        this.tokenProvider = tokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    // @Override
    // protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    //         throws IOException, ServletException {
    //     String token = getJwtFromRequest(request);

    //     if (token != null && tokenProvider.validateToken(token)) {
    //         String username = tokenProvider.getUsernameFromJWT(token);

    //         if (username != null) {
    //             UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
    //             UsernamePasswordAuthenticationToken authentication =
    //                     new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    //             authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    //             SecurityContextHolder.getContext().setAuthentication(authentication);
    //         }
    //     }

    //     filterChain.doFilter(request, response);
    // }

    // private String getJwtFromRequest(HttpServletRequest request) {
    //     String bearerToken = request.getHeader("Authorization");
    //     return (bearerToken != null && bearerToken.startsWith("Bearer ")) ? bearerToken.substring(7) : null;
    // }
    @Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {
    String token = getJwtFromRequest(request);

    if (token != null && tokenProvider.validateToken(token)) {
        String username = tokenProvider.getUsernameFromJWT(token);
        List<String> roles = tokenProvider.getRolesFromToken(token); // Extract roles

        if (username != null) {
            // Map roles to GrantedAuthority
            List<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // Create authentication object
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Set authentication in the context
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    filterChain.doFilter(request, response);
}
private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    return (bearerToken != null && bearerToken.startsWith("Bearer ")) ? bearerToken.substring(7) : null;
}

}
