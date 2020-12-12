package filter;

import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserSessionFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            super.doFilter(request, response, chain);
        } else {
            String url = request.getRequestURL().toString();
            String resource = url.substring(url.lastIndexOf("/"));
            if (resource.equals("/catalog") || resource.equals("/orders") || resource.equals("/addTour") ||
                    resource.equals("/editTour")) {
                response.sendRedirect(request.getContextPath());
            } else {
                super.doFilter(request, response, chain);
            }
        }
    }
}

