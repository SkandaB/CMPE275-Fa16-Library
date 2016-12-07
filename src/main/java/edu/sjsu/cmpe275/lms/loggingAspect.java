package edu.sjsu.cmpe275.lms;

import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

/**
 * Created by dhanyaramesh on 12/6/16.
 */

@Aspect
public class loggingAspect {


    @After("execution(* edu.sjsu.cmpe275.lms.controller.*(..))")
    public void logMethod(Joinpoint jp) {
        System.out.println("Execution of " + jp.getThis().getClass().getName() + "done");
    }

}
