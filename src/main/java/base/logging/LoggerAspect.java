package base.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@Aspect
public class LoggerAspect {

    private static final Logger log = LogManager.getLogger(LoggerAspect.class.getName());

    // GroupController
    // *****************************************************************************************************************
    // Варивет через задание  Pointcut+Before + After   - для всех методов класса
    @Pointcut("execution(public * base.web.GroupController.*(..))")
    public void callControllerMethodGetFilterProduct() { }

    @Before("callControllerMethodGetFilterProduct()")
    public void beforeCallAtGetFilterProduct(JoinPoint jp) {
        String args = Arrays.stream(jp.getArgs())
                .map(a -> a.toString())
                .collect(Collectors.joining(","));
        log.trace("before " + jp.getSignature().getName() /*jp.toString()*/ + ", args=[" + args + "]");
    }
    @After("callControllerMethodGetFilterProduct()")
    public void afterCallAtGetFilterProduct(JoinPoint jp) {
        log.trace("after " + jp.getSignature().getName() /*jp.toString()*/);
    }

    //AfterThrowing
    @AfterThrowing(value = "callControllerMethodGetFilterProduct()", throwing = "exception")
    public void afterThrowingCallAtGetFilterProduct(JoinPoint jp, Exception exception) {
        log.trace( jp.getSignature().getName() /*jp.toString()*/+ " вызвал exception: " + exception.toString());
    }

    //AfterReturning
    @AfterReturning(pointcut = "execution(" +
            "public * base.web.GroupController.*(..))",
            returning = "result" )
    public void logAfterReturningGroupController(JoinPoint jp,Object result) {
        log.trace( jp.getSignature().getName() /*jp.toString()*/+ " возвращенное значение: " + result.toString());
    }

    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint jp) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = jp.proceed();
        long executionTime = System.currentTimeMillis() - start;

        String args = Arrays.stream(jp.getArgs())
                .map(a -> a.toString())
                .collect(Collectors.joining(","));

        log.warn(jp.getSignature().getName() + " выполнен за " + executionTime + "мс"  + ", args=[" + args + "]");
        return proceed;
    }

}
