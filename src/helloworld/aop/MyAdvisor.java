package helloworld.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;

import helloworld.MyBean;

public class MyAdvisor {

    public void setBefore() {
        System.out.println("do before........");
    }

    public void setBeforeWithpara(ProceedingJoinPoint joinPoint) {

        String sex = ((MyBean) joinPoint.getTarget()).getSex();
        Method[] targetMs = joinPoint.getTarget().getClass().getMethods();
        System.out.println("target");
        for (Method m : targetMs) {
            System.out.println(m.getName());
        }

        Method[] proxyMs = joinPoint.getThis().getClass().getMethods();
        System.out.println("proxy");
        for (Method m1 : proxyMs) {
            System.out.println(m1.getName());
        }

        Object[] proxyObject = joinPoint.getArgs();
        Object ob = joinPoint.getThis();
        System.out.println("proxyObject........" + proxyObject);
        System.out.println("joinPoint........" + joinPoint);
        System.out.println("ob........" + ob);
        try {
            joinPoint.proceed();
        } catch (Throwable e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }

    public void setAfterReturn() {
        System.out.println("do afterReturn........");
    }

    public void setAfter() {
        System.out.println("do after finally........");
    }

    public void setAfterException() {
        System.out.println("do after exception........");
    }

    public void setAround(ProceedingJoinPoint joinPoint) {

        System.out.println("do before around........");
        try {
            joinPoint.proceed();
        } catch (Throwable e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        System.out.println("do after around........");
    }
}
