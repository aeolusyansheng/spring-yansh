package helloworld.aop;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class MyAdvisorInterface implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation arg0) throws Throwable {

        Object[] parameters = arg0.getArguments();
        Method m = arg0.getMethod();
        Class<?>[] parameterTypes = m.getParameterTypes();
        for (Class<?> clazz : parameterTypes) {
            System.out.println(clazz.getName());
        }

        arg0.proceed();
        return null;
    }

}
