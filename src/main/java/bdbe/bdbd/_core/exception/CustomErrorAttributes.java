package bdbe.bdbd._core.exception;

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

        // 성공 여부를 설정합니다. 에러가 발생했으면 false, 아니면 true입니다.
        customErrorAttributes.put("success", !errorOccurred);

        // 에러 발생시에만 'error' 키를 포함시키고, 'response'를 null로 설정합니다.
        if (errorOccurred) {
            Map<String, Object> errorDetails = new LinkedHashMap<>();

            // 404 에러일 경우 메시지를 "Not Found"로 설정합니다.
            String errorMessage = (status == 404) ? "Not Found" : (String) defaultErrorAttributes.get("message");
            errorDetails.put("message", errorMessage);
            errorDetails.put("status", status);

            customErrorAttributes.put("response", null); // 에러 발생 시 response는 null
            customErrorAttributes.put("error", errorDetails);
        } else {
            customErrorAttributes.put("response", null); // 에러가 없을 때도 response는 null
            customErrorAttributes.put("error", null);
        }

        return customErrorAttributes;
    }
}
