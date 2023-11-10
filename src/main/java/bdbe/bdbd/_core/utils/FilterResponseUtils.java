package bdbe.bdbd._core.utils;


import bdbe.bdbd._core.exception.UnAuthorizedError;
import bdbe.bdbd._core.exception.ForbiddenError;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FilterResponseUtils {
    public static void unAuthorized(HttpServletResponse resp, UnAuthorizedError e) throws IOException {
        ObjectMapper om = new ObjectMapper();
        String responseBody = om.writeValueAsString(e.body());
        resp.getWriter().println(responseBody);
        resp.setStatus(e.getStatus().value());
        resp.setContentType("application/json; charset=utf-8");
    }

    public static void forbidden(HttpServletResponse resp, ForbiddenError e) throws IOException {
        ObjectMapper om = new ObjectMapper();
        String responseBody = om.writeValueAsString(e.body());
        resp.getWriter().println(responseBody);
        resp.setStatus(e.getStatus().value());
        resp.setContentType("application/json; charset=utf-8");
    }
}
