package bdbe.bdbd._core.utils;


import bdbe.bdbd._core.exception.UnAuthorizedError;
import bdbe.bdbd._core.exception.ForbiddenError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FilterResponseUtils {

    private static final ObjectMapper om = new ObjectMapper(); // ObjectMapper는 재사용 가능하므로 클래스 변수로 선언

    public static void unAuthorized(HttpServletResponse resp, UnAuthorizedError e) throws IOException {
        String responseBody = om.writeValueAsString(ApiUtils.error(
                String.valueOf(e.getStatus().value()),
                String.valueOf(e.getErrorCode().getCode()),
                e.getErrors()
        ));
        sendResponse(resp, responseBody, e.getStatus());
    }

    public static void forbidden(HttpServletResponse resp, ForbiddenError e) throws IOException {
        String responseBody = om.writeValueAsString(ApiUtils.error(
                String.valueOf(e.getStatus().value()),
                String.valueOf(e.getErrorCode().getCode()),
                e.getErrors()
        ));
        sendResponse(resp, responseBody, e.getStatus());
    }

    private static void sendResponse(HttpServletResponse resp, String responseBody, HttpStatus status) throws IOException {
        resp.getWriter().println(responseBody);
        resp.setStatus(status.value());
        resp.setContentType("application/json; charset=utf-8");
    }
}
