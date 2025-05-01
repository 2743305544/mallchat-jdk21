package com.shiyi.mallchat.common.utils;

import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Optional;

public class SpElUtils {

    private static final ExpressionParser PARSER = new SpelExpressionParser();
    private static final DefaultParameterNameDiscoverer DISCOVERER = new DefaultParameterNameDiscoverer();
    public static String getMethodKey(Method method) {
        return method.getDeclaringClass() + "#" + method.getName();
    }

    public static String parseSpEL(MethodSignature signature, Object[] args, String key) {
        String[] strings = Optional.ofNullable(DISCOVERER.getParameterNames(signature.getMethod())).orElse(new String[]{});
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < strings.length; i++) {
            context.setVariable(strings[i], args[i]);
        }
        return PARSER.parseExpression(key).getValue(context, String.class);
    }
}
