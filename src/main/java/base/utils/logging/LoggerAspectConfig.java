package base.utils.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@Aspect
public class LoggerAspectConfig {

    private static final Logger log = LogManager.getLogger(LoggerAspectConfig.class.getName());

    // GroupController
    // *****************************************************************************************************************
    // Варивет через задание  Pointcut+Before + After   - для всех методов класса
    @Pointcut("execution(public * base.web.GroupController.*(..))")
    public void callControllerMethodGroup() { }

    // Варивет через задание  Pointcut+Before + After   - для всех методов класса
    @Pointcut("execution(public * base.web.StudentController.*(..))")
    public void callControllerMethodStudent() { }

    @Before("callControllerMethodGroup()")
    public void beforeCallAtGetFilterProduct(JoinPoint jp) {
        String args = Arrays.stream(jp.getArgs())
                .map(a -> a.toString())
                .collect(Collectors.joining(","));
        log.info(logCurrentUserDetails());
        log.info("before " + jp.getSignature().getName() /*jp.toString()*/ + ", args=[" + args + "]");
    }
    @After("callControllerMethodGroup()")
    public void afterCallAtGetFilterProduct(JoinPoint jp) {
        log.info(logCurrentUserDetails());
        log.info("after " + jp.getSignature().getName() /*jp.toString()*/);
    }

    //AfterThrowing
    @AfterThrowing(value = "callControllerMethodGroup()", throwing = "exception")
    public void afterThrowingCallAtGetFilterProduct(JoinPoint jp, Exception exception) {
        log.info(logCurrentUserDetails());
        log.info( jp.getSignature().getName() /*jp.toString()*/+ " вызвал exception: " + exception.toString());
    }

    //AfterReturning
    @AfterReturning(pointcut = "execution(" +
            "public * base.web.GroupController.*(..))",
            returning = "result" )
    public void logAfterReturningGroupController(JoinPoint jp,Object result) {
        log.info(logCurrentUserDetails());
        log.info( jp.getSignature().getName() /*jp.toString()*/+ " возвращенное значение: " + result.toString());
    }

    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint jp) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = jp.proceed();
        long executionTime = System.currentTimeMillis() - start;

        String args = Arrays.stream(jp.getArgs())
                .map(a -> a.toString())
                .collect(Collectors.joining(","));

        log.info(logCurrentUserDetails());
        log.info(jp.getSignature().getName() + " выполнен за " + executionTime + "мс"  + ", args=[" + args + "]");
        return proceed;
    }

    private String logCurrentUserDetails() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();
        return user.toString();
    }

}
