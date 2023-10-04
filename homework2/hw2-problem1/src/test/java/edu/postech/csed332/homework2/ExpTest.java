package edu.postech.csed332.homework2;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ExpTest {

    @Test
    void testParserOK() {
        String expStr = "((p1 || (p2 && (! p3))) || true)";
        Exp exp = ExpParser.parse(expStr);
        assertEquals(expStr, exp.toPrettyString());
    }

    @Test
    void testParserError() {
        assertThrows(IllegalStateException.class, () -> {
            Exp exp = ExpParser.parse("p1 || p2 && ! p0 || true");
        });
    }

    /*
     * TODO: add  test methods to achieve at least 80% branch coverage of the classes:
     *  Conjunction, Constant, Disjunction, Negation, Variable.
     * Each test method should have appropriate JUnit assertions to test a single behavior.
     * You should not add arbitrary code to test methods to just increase coverage.
     */
    @Test
    void testVariableIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Exp var = new Variable(-1);
        });
    }

    @Test
    void testGetVariables() {
        String exp = "p1 || p2 && ! p3 || true";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "((p1 || (p2 && (! p3))) || true)");
        assertEquals(parsed.getVariables(), Set.of(1, 2, 3));
    }

    /**
     * Test for Exp::Evaluate
     * (1) Arbitrary Exp
     */
    @Test
    void testEvaluateExp() {
        String exp = "p1 || p2 && ! p3 || true";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "((p1 || (p2 && (! p3))) || true)");
        assertEquals(parsed.evaluate(Map.of(1, true, 2, true, 3, true)), true);
        assertEquals(parsed.evaluate(Map.of(1, false, 2, true, 3, true)), true);
        assertEquals(parsed.evaluate(Map.of(1, true, 2, false, 3, true)), true);
        assertEquals(parsed.evaluate(Map.of(1, true, 2, true, 3, false)), true);
        assertEquals(parsed.evaluate(Map.of(1, false, 2, false, 3, true)), true);
        assertEquals(parsed.evaluate(Map.of(1, false, 2, true, 3, false)), true);
        assertEquals(parsed.evaluate(Map.of(1, true, 2, false, 3, false)), true);
        assertEquals(parsed.evaluate(Map.of(1, false, 2, false, 3, false)), true);
    }

    /**
     * (2) Conjunction
     */
    @Test
    void testEvaluateConjunction_1() {
        String exp = "true && true";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(true && true)");
        assertEquals(parsed.evaluate(Map.of()), true);
    }

    @Test
    void testEvaluateConjunction_2() {
        String exp = "true && false";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(true && false)");
        assertEquals(parsed.evaluate(Map.of()), false);
    }

    @Test
    void testEvaluateConjunction_3() {
        String exp = "false && false";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(false && false)");
        assertEquals(parsed.evaluate(Map.of()), false);
    }

    @Test
    void testEvaluateConjunction_4() {
        String exp = "p1 && p2";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(p1 && p2)");
        assertEquals(parsed.evaluate(Map.of(1, true, 2, true)), true);
        assertEquals(parsed.evaluate(Map.of(1, true, 2, false)), false);
        assertEquals(parsed.evaluate(Map.of(1, false, 2, false)), false);
    }

    /**
     * (3) Disjunction
     */
    @Test
    void testEvaluateDisjunction_1() {
        String exp = "true || true";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(true || true)");
        assertEquals(parsed.evaluate(Map.of()), true);
    }

    @Test
    void testEvaluateDisjunction_2() {
        String exp = "true || false";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(true || false)");
        assertEquals(parsed.evaluate(Map.of()), true);
    }

    @Test
    void testEvaluateDisjunction_3() {
        String exp = "false || false";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(false || false)");
        assertEquals(parsed.evaluate(Map.of()), false);
    }

    @Test
    void testEvaluateDisjunction_4() {
        String exp = "p1 || p2";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(p1 || p2)");
        assertEquals(parsed.evaluate(Map.of(1, true, 2, true)), true);
        assertEquals(parsed.evaluate(Map.of(1, true, 2, false)), true);
        assertEquals(parsed.evaluate(Map.of(1, false, 2, false)), false);
    }

    /**
     * (4) Negation
     */
    @Test
    void testEvaluateNegation_1() {
        String exp = "! true";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(! true)");
        assertEquals(parsed.evaluate(Map.of()), false);
    }

    @Test
    void testEvaluateNegation_2() {
        String exp = "! false";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(! false)");
        assertEquals(parsed.evaluate(Map.of()), true);
    }

    @Test
    void testEvaluateNegation_3() {
        String exp = "! p1";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(! p1)");
        assertEquals(parsed.evaluate(Map.of(1, true)), false);
        assertEquals(parsed.evaluate(Map.of(1, false)), true);
    }

    /**
     * (5) Variable
     */
    @Test
    void testEvaluateVariable() {
        String exp = "p1";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "p1");
        assertEquals(parsed.evaluate(Map.of(1, true)), true);
        assertEquals(parsed.evaluate(Map.of(1, false)), false);
    }

    /**
     * (6) Constant
     */
    @Test
    void testEvaluateConstant() {
        assertEquals(ExpParser.parse("true").evaluate(Map.of()), true);
        assertEquals(ExpParser.parse("false").evaluate(Map.of()), false);
    }

    /**
     * Test for Exp::Simplify
     * (1)-1 Identity Laws
     */
    @Test
    void testSimplifyIdentityLaws_1() {
        String exp = "p1 && true";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(p1 && true)");
        assertEquals(parsed.simplify().toPrettyString(), "p1");
    }

    @Test
    void testSimplifyIdentityLaws_2() {
        String exp = "p1 || false";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(p1 || false)");
        assertEquals(parsed.simplify().toPrettyString(), "p1");
    }

    /**
     * (1)-2 Idempotent Laws
     */
    @Test
    void testSimplifyIdempotentLaws_1() {
        String exp = "p1 && p1";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(p1 && p1)");
        assertEquals(parsed.simplify().toPrettyString(), "p1");
    }

    @Test
    void testSimplifyIdempotentLaws_2() {
        String exp = "p1 || p1";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(p1 || p1)");
        assertEquals(parsed.simplify().toPrettyString(), "p1");
    }

    /**
     * (2)-1 Domination Laws
     */
    @Test
    void testSimplifyDominationLaws_1() {
        String exp = "p1 && false";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(p1 && false)");
        assertEquals(parsed.simplify().evaluate(Map.of()), false);
        assertEquals(parsed.simplify().toPrettyString(), "false");
    }

    @Test
    void testSimplifyDominationLaws_2() {
        String exp = "p1 || true";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(p1 || true)");
        assertEquals(parsed.simplify().evaluate(Map.of()), true);
        assertEquals(parsed.simplify().toPrettyString(), "true");
    }

    /**
     * (2)-2 Double Negation Laws
     */
    @Test
    void testSimplifyDoubleNegationLaws() {
        String exp = "! ( ! p1 )";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(! (! p1))");
        assertEquals(parsed.simplify().toPrettyString(), "p1");
    }

    /**
     * (3) Negation Laws
     */
    @Test
    void testSimplifyNegationLaws_1() {
        String exp = "! ( true )";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(! true)");
        assertEquals(parsed.simplify().evaluate(Map.of()), false);
        assertEquals(parsed.simplify().toPrettyString(), "false");
    }

    @Test
    void testSimplifyNegationLaws_2() {
        String exp = "! ( false )";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(! false)");
        assertEquals(parsed.simplify().evaluate(Map.of()), true);
        assertEquals(parsed.simplify().toPrettyString(), "true");
    }

    @Test
    void testSimplifyNegationLaws_3() {
        String exp = "p1 && ! p1";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(p1 && (! p1))");
        assertEquals(parsed.simplify().evaluate(Map.of()), false);
        assertEquals(parsed.simplify().toPrettyString(), "false");
    }

    @Test
    void testSimplifyNegationLaws_4() {
        String exp = "p1 || ! p1";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(p1 || (! p1))");
        assertEquals(parsed.simplify().evaluate(Map.of()), true);
        assertEquals(parsed.simplify().toPrettyString(), "true");
    }

    /**
     * (4) De morgan's Laws
     */
    @Test
    void testSimplifyDeMorgansLaws_1() {
        String exp = "!(p1 || p2)";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(! (p1 || p2))");
        assertEquals(parsed.simplify().toPrettyString(), "((! p1) && (! p2))");
    }

    @Test
    void testSimplifyDeMorgansLaws_2() {
        String exp = "!(p1 && p2)";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(! (p1 && p2))");
        assertEquals(parsed.simplify().toPrettyString(), "((! p1) || (! p2))");
    }

    /**
     * (5) Absorption Laws
     */
    @Test
    void testSimplifyAbsorptionLaws_1() {
        String exp = "p1 || ( p2 && p3 )";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(p1 || (p2 && p3))");
        assertEquals(parsed.simplify().toPrettyString(), "(p1 || (p2 && p3))");
    }

    @Test
    void testSimplifyAbsorptionLaws_2() {
        String exp = "p1 && ( p2 || p3 )";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(p1 && (p2 || p3))");
        assertEquals(parsed.simplify().toPrettyString(), "(p1 && (p2 || p3))");
    }

    @Test
    void testSimplifyAbsorptionLaws_3() {
        String exp = "p1 || ( p1 && p2 )";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(p1 || (p1 && p2))");
        assertEquals(parsed.simplify().toPrettyString(), "p1");
    }

    @Test
    void testSimplifyAbsorptionLaws_4() {
        String exp = "p1 && ( p1 || p2 )";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.toPrettyString(), "(p1 && (p1 || p2))");
        assertEquals(parsed.simplify().toPrettyString(), "p1");
    }
}
