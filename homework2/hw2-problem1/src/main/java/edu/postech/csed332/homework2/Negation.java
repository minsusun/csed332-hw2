package edu.postech.csed332.homework2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A Boolean expression whose top-level operator is ! (not).
 */
public record Negation(Exp subExp) implements Exp {

    @Override
    public Set<Integer> getVariables() {
        // TODO: implement this
        return new HashSet<>(this.subExp.getVariables());
    }

    @Override
    public Boolean evaluate(Map<Integer, Boolean> assignment) {
        // TODO: implement this
        return !this.subExp.evaluate(assignment);
    }

    @Override
    public Exp simplify() {
        // TODO: implement this
        // (0) Simplify subExp
        Exp simplified = this.subExp.simplify();
        // (2)-2 Double negation laws
        if(this.subExp instanceof Negation) return ((Negation) this.subExp).subExp;
        // (3) Negation laws
        if(this.subExp instanceof Constant) return new Constant(!((Constant) this.subExp).value());
        // (4) De Morgan's laws
        // .toList().toArray(Exp[]::new) instead of (Exp[]) ~.toArray()
        // : Causing cast exception due to un-castable from Object[] to Exp[]
        if(this.subExp instanceof Conjunction) return new Disjunction(Arrays.stream(((Conjunction) this.subExp).subExps()).map(exp -> new Negation(exp).simplify()).toList().toArray(Exp[]::new));
        if(this.subExp instanceof Disjunction) return new Conjunction(Arrays.stream(((Disjunction) this.subExp).subExps()).map(exp -> new Negation(exp).simplify()).toList().toArray(Exp[]::new));
        return new Negation(simplified);
    }

    @Override
    public String toPrettyString() {
        return "(! " + subExp().toPrettyString() + ")";
    }
}
