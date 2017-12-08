package helloworld.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class MyAdvisorAnotation {

    @Pointcut(value="execution(* helloworld.MyBean.toString2(..))")
    public void myPointcut(){

    }

    @Around(value="myPointcut()")
    public void myAdvise(ProceedingJoinPoint joinpoint){

        System.out.println("....before");
        try {
            joinpoint.proceed();
        } catch (Throwable e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        System.out.println("....after");

    }
}
