package bdbe.bdbd._core.handler;


import bdbe.bdbd._core.exception.BadRequestError;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Aspect
@Component
public class GlobalValidationHandler {
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping() {
    }

    @Before("postMapping()")
    public void validationAdvice(JoinPoint jp) {
        Object[] args = jp.getArgs();
        for (Object arg : args) {
            if (arg instanceof Errors) {
                Errors errors = (Errors) arg;

                if (errors.hasErrors()) { // 검증 오류 발생시 400
                    throw new BadRequestError(
                            String.format("%s:%s",
                                    errors.getFieldErrors().get(0).getDefaultMessage(),
                                    errors.getFieldErrors().get(0).getField())
                    );
                }
            }
        }
    }
}
