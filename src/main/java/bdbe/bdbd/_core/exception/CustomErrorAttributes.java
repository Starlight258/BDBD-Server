package bdbe.bdbd._core.exception;

import org.apache.http.HttpStatus;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(
            WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> defaultErrorAttributes = super.getErrorAttributes(webRequest, options);

        Map<String, Object> customErrorAttributes = new LinkedHashMap<>();
        boolean errorOccurred = !defaultErrorAttributes.get("status").equals(200);
        int status = (int) defaultErrorAttributes.get("status");

        customErrorAttributes.put("success", !errorOccurred);

        if (errorOccurred) {
            Map<String, Object> errorDetails = new LinkedHashMap<>();

            String errorMessage = (status == HttpStatus.SC_NOT_FOUND) ? NotFoundError.ErrorCode.RESOURCE_NOT_FOUND.getMessage() : (String) defaultErrorAttributes.get("message");
            errorDetails.put("status", String.valueOf(status));
            errorDetails.put("code", String.valueOf(NotFoundError.ErrorCode.RESOURCE_NOT_FOUND.getCode()));
            errorDetails.put("message", errorMessage);

            customErrorAttributes.put("response", null); // 에러 발생 시 response는 null
            customErrorAttributes.put("error", errorDetails);
        } else {
            customErrorAttributes.put("response", null); // 에러가 없을 때도 response는 null
            customErrorAttributes.put("error", null);
        }

        return customErrorAttributes;
    }
}
