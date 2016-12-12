package edu.sjsu.cmpe275.lms;


import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Created by dhanyaramesh on 12/6/16.
 */

@Aspect
@Component
public class loggingAspect {


    //    @AfterReturning("execution(* edu.sjsu.cmpe275.lms.controller.*(..)) && args(..)")
    public void logMethod(Joinpoint jp) {
        System.out.println("Execution of " + jp.getClass().getSimpleName() + "done");
    }

}
