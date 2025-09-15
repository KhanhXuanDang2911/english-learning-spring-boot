package elearningspringboot.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExceptionHandlingFilter extends OncePerRequestFilter {

    private final CustomAuthenticationEntryPoint entryPoint;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);

        } catch (AuthenticationException ex) {
            entryPoint.commence(request, response, ex);

        } catch (RuntimeException ex) {
            entryPoint.commence(request, response,
                    new InsufficientAuthenticationException(ex.getMessage(), ex));
        }
    }

}
