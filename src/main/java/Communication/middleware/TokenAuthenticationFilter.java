package Communication.middleware;

import Communication.services.AuthTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 2) // Runs after CorrelationIdFilter and RateLimitingFilter
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(TokenAuthenticationFilter.class);
    private final AuthTokenService authTokenService;

    public TokenAuthenticationFilter(AuthTokenService authTokenService) {
        this.authTokenService = authTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // 1. Bypass authentication for auth endpoints and static paths
        if (path.startsWith("/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extract Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Unauthorized access attempt on {} from IP {}", path, request.getRemoteAddr());
            sendUnauthorizedResponse(response, "Missing or malformed Authorization header. Expected format: Bearer <token>");
            return;
        }

        String token = authHeader.substring(7).trim();

        // 3. Verify against the database
        if (!authTokenService.validateToken(token)) {
            log.warn("Invalid, expired, or revoked token presented for {}", path);
            sendUnauthorizedResponse(response, "Invalid or expired token");
            return;
        }

        // 4. Token valid; pass to controller
        filterChain.doFilter(request, response);
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write(String.format("""
            {
              "status": 401,
              "error": "Unauthorized",
              "message": "%s"
            }
        """, message));
    }
}