package com.springbyexample.springexpressionlanguage;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @author Mujuzi Moses
 */
public class SpelApplication {

    public static void main(String[] args) {
        ExpressionParser parser = new SpelExpressionParser();

        Expression expression = parser.parseExpression("'Hello from SpEL!'");
        String message = expression.getValue(String.class);

        System.out.println(message);

        Integer result = parser.parseExpression("10 + 20").getValue(Integer.class);

        System.out.println("10 + 20 = " + result);
    }
}