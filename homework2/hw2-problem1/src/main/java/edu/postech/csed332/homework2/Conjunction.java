package edu.postech.csed332.homework2;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A Boolean expression whose top-level operator is && (and).
 */
public record Conjunction(Exp... subExps) implements Exp {

    @Override
    public Set<Integer> getVariables() {
        // TODO: implement this
        return Arrays.stream(this.subExps).map(Exp::getVariables).flatMap(Set::stream).collect(Collectors.toSet());
    }

    @Override
    public Boolean evaluate(Map<Integer, Boolean> assignment) {
        // TODO: implement this
        return Arrays.stream(this.subExps).allMatch(exp -> exp.evaluate(assignment));
    }

    @Override
    public Exp simplify() {
        // TODO: implement this
        // (0) Simplify subExps
        List<Exp> simplified = Arrays.stream(this.subExps).map(Exp::simplify).toList();
        // (1) Identity and idempotent laws
        simplified = simplified.stream().filter(exp -> !(exp instanceof  Constant && ((Constant) exp).value())).distinct().toList();
        // (2)-1 Domination law
        if(simplified.stream().anyMatch(exp -> Objects.equals(exp, new Constant(false))))
            return new Constant(false);
        // (3) Negation laws
        List<Exp> negationSubExps = simplified.stream().filter(exp -> exp instanceof Negation).map(negation -> ((Negation) negation).subExp()).toList();
        if(simplified.stream().anyMatch(negationSubExps::contains))
            return new Constant(false);
        // (5) Absorption laws
        // .toList().toArray(Exp[]::new) instead of (Exp[]) ~.toArray()
        // : Causing cast exception due to un-castable from Object[] to Exp[]
        List<Exp> nonDisjunctions = simplified.stream().filter(exp -> !(exp instanceof Disjunction)).toList();
        List<Exp> exps = simplified.stream().filter(exp -> {
            if(exp instanceof Disjunction) {
                for(Exp e: ((Disjunction) exp).subExps()) {
                    if(nonDisjunctions.contains(e))
                        return false;
                }
            }
            return true;
        }).toList();
        if(exps.size() == 1)
            return exps.get(0);
        return new Conjunction(exps.toArray(Exp[]::new));
    }

    @Override
    public String toPrettyString() {
        return "(" + Arrays.stream(subExps()).map(Exp::toPrettyString)
                .collect(Collectors.joining(" && ")) + ")";
    }
}