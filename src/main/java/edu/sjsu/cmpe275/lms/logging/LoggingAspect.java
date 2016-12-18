package edu.sjsu.cmpe275.lms.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Created by SkandaBhargav on 12/16/16.
 */
@Aspect
public class LoggingAspect {
    public void LogAopToFile(String message) throws IOException {
        FileWriter fileWriter = new FileWriter("aop_logging.txt", true);
        fileWriter.write(message);
        fileWriter.close();
    }

    @Pointcut("execution(* edu.sjsu.cmpe275.lms.controller.*.*(..))")
    private void logAll() {
    }

    @Before("logAll()")
    public void beforeMethod(JoinPoint joinPoint) throws IOException {
        LogAopToFile("\nTime: " + new Date() + "\t\tBefore entering method: " + joinPoint.getSignature().getName());

    }

    @AfterReturning("logAll()")
    public void afterMethodReturn(JoinPoint joinPoint) throws IOException {
        LogAopToFile("\nTime: " + new Date() + "\t\tMethod: " + joinPoint.getSignature().getName() + " returned successfully");
    }

    @AfterThrowing("logAll()")
    public void afterMethodThorw(JoinPoint joinPoint) throws IOException {
        LogAopToFile("\nTime: " + new Date() + "\t\tMethod: " + joinPoint.getSignature().getName() + " threw an exception");
    }
}

