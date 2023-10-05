package edu.postech.csed332.homework2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

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
    /**
     * Test for Defining Illegal Variable
     */
    @Test
    void testVariableIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Exp var = new Variable(-1);
        });
    }

    /**
     * Test for Exp::GetVariables
     */
    @Test
    void testGetVariables() {
        String exp = "p1 || p2 && ! p3 || true";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.getVariables(), Set.of(1, 2, 3));
    }

    /**
     * Test for Exp::Evaluate
     * (1) Arbitrary Exp
     */
    @ParameterizedTest(name = "testEvaluateExp_{index}")
    @CsvSource({"true, true, true",
            "true, true, false",
            "true, false, true",
            "false, true, true",
            "true, false, false",
            "false, true, false",
            "false, false, true",
            "false, false, false"})
    void testEvaluateExp(boolean b1, boolean b2, boolean b3) {
        String exp = "p1 || p2 && ! p3 || true";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.evaluate(Map.of(1, b1, 2, b2, 3, b3)), true);
    }

    /**
     * (2) Conjunction
     */
    @Test
    void testEvaluateConjunction_1() {
        String exp = "true && true";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.evaluate(Map.of()), true);
    }

    @Test
    void testEvaluateConjunction_2() {
        String exp = "true && false";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.evaluate(Map.of()), false);
    }

    @Test
    void testEvaluateConjunction_3() {
        String exp = "false && false";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.evaluate(Map.of()), false);
    }

    @ParameterizedTest(name = "testEvaluateConjunction_4_{index}")
    @CsvSource({"true, true, true", "true, false, false", "false, false, false"})
    void testEvaluateConjunction_4(boolean b1, boolean b2, boolean b3) {
        String exp = "p1 && p2";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.evaluate(Map.of(1, b1, 2, b2)), b3);
    }

    /**
     * (3) Disjunction
     */
    @Test
    void testEvaluateDisjunction_1() {
        String exp = "true || true";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.evaluate(Map.of()), true);
    }

    @Test
    void testEvaluateDisjunction_2() {
        String exp = "true || false";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.evaluate(Map.of()), true);
    }

    @Test
    void testEvaluateDisjunction_3() {
        String exp = "false || false";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.evaluate(Map.of()), false);
    }

    @ParameterizedTest(name = "testEvaluateDisjunction_4_{index}")
    @CsvSource({"true, true, true", "true, false, true", "false, false, false"})
    void testEvaluateDisjunction_4(boolean b1, boolean b2, boolean b3) {
        String exp = "p1 || p2";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.evaluate(Map.of(1, b1, 2, b2)), b3);
    }

    /**
     * (4) Negation
     */
    @Test
    void testEvaluateNegation_1() {
        String exp = "! true";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.evaluate(Map.of()), false);
    }

    @Test
    void testEvaluateNegation_2() {
        String exp = "! false";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.evaluate(Map.of()), true);
    }

    @ParameterizedTest(name = "testEvaluateNegation_3_{index}")
    @CsvSource({"true", "false"})
    void testEvaluateNegation_3(boolean b1) {
        String exp = "! p1";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.evaluate(Map.of(1, b1)), !b1);
    }

    /**
     * (5) Variable
     */
    @ParameterizedTest(name = "testEvaluateVariable_{index}")
    @CsvSource({"true", "false"})
    void testEvaluateVariable(boolean b1) {
        String exp = "p1";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.evaluate(Map.of(1, b1)), b1);
    }

    /**
     * (6) Constant
     */
    @ParameterizedTest(name = "testEvaluateConstant_{index}")
    @CsvSource({"true", "false"})
    void testEvaluateConstant(boolean b1) {
        assertEquals(ExpParser.parse(Boolean.toString(b1)).evaluate(Map.of()), b1);
    }

    /**
     * Test for Exp::Simplify
     * (1)-1 Identity Laws
     */
    @Test
    void testSimplifyIdentityLaws_1() {
        String exp = "p1 && true";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.simplify().toPrettyString(), "p1");
    }

    @Test
    void testSimplifyIdentityLaws_2() {
        String exp = "p1 || false";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.simplify().toPrettyString(), "p1");
    }

    /**
     * (1)-2 Idempotent Laws
     */
    @Test
    void testSimplifyIdempotentLaws_1() {
        String exp = "p1 && p1";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.simplify().toPrettyString(), "p1");
    }

    @Test
    void testSimplifyIdempotentLaws_2() {
        String exp = "p1 || p1";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.simplify().toPrettyString(), "p1");
    }

    /**
     * (2)-1 Domination Laws
     */
    @Test
    void testSimplifyDominationLaws_1() {
        String exp = "p1 && false";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.simplify().evaluate(Map.of()), false);
    }

    @Test
    void testSimplifyDominationLaws_2() {
        String exp = "p1 || true";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.simplify().evaluate(Map.of()), true);
    }

    /**
     * (2)-2 Double Negation Laws
     */
    @Test
    void testSimplifyDoubleNegationLaws() {
        String exp = "! ( ! p1 )";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.simplify().toPrettyString(), "p1");
    }

    /**
     * (3) Negation Laws
     */
    @Test
    void testSimplifyNegationLaws_1() {
        String exp = "! ( true )";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.simplify().evaluate(Map.of()), false);
    }

    @Test
    void testSimplifyNegationLaws_2() {
        String exp = "! ( false )";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.simplify().evaluate(Map.of()), true);
    }

    @Test
    void testSimplifyNegationLaws_3() {
        String exp = "p1 && ! p1";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.simplify().evaluate(Map.of()), false);
    }

    @Test
    void testSimplifyNegationLaws_4() {
        String exp = "p1 || ! p1";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.simplify().evaluate(Map.of()), true);
    }

    /**
     * (4) De morgan's Laws
     */
    @Test
    void testSimplifyDeMorgansLaws_1() {
        String exp = "!(p1 || p2)";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.simplify().toPrettyString(), "((! p1) && (! p2))");
    }

    @Test
    void testSimplifyDeMorgansLaws_2() {
        String exp = "!(p1 && p2)";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.simplify().toPrettyString(), "((! p1) || (! p2))");
    }

    /**
     * (5) Absorption Laws
     */
    @Test
    void testSimplifyAbsorptionLaws_1() {
        String exp = "p1 || ( p2 && p3 )";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.simplify().toPrettyString(), "(p1 || (p2 && p3))");
    }

    @Test
    void testSimplifyAbsorptionLaws_2() {
        String exp = "p1 && ( p2 || p3 )";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.simplify().toPrettyString(), "(p1 && (p2 || p3))");
    }

    @Test
    void testSimplifyAbsorptionLaws_3() {
        String exp = "p1 || ( p1 && p2 )";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.simplify().toPrettyString(), "p1");
    }

    @Test
    void testSimplifyAbsorptionLaws_4() {
        String exp = "p1 && ( p1 || p2 )";
        Exp parsed = ExpParser.parse(exp);
        assertEquals(parsed.simplify().toPrettyString(), "p1");
    }
}
