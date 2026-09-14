package com.springbyexample.springexpressionlanguage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author Mujuzi Moses
 */
public class SpelApplicationTest {

    private final ExpressionParser parser = new SpelExpressionParser();

    @Test
    void shouldEvaluateLiteralExpression() {
        String result = parser.parseExpression("'Hello from SpEL!'").getValue(String.class);

        assertEquals("Hello from SpEL!", result);
    }

    @Test
    void shouldEvaluateArithmeticExpression() {
        Integer result = parser.parseExpression("10 + 20").getValue(Integer.class);

        assertEquals(30, result);
    }

    @Test
    void shouldEvaluateExpressionAgainstRootObject() {
        User user = new User("Moses");

        String result = parser.parseExpression("name").getValue(user, String.class);

        assertEquals("Moses", result);
    }

    @Test
    void shouldEvaluateExpressionUsingVariable() {
        StandardEvaluationContext context = new StandardEvaluationContext();

        context.setVariable("name", "Moses");

        String result = parser.parseExpression("#name").getValue(context, String.class);

        assertEquals("Moses", result);
    }
}