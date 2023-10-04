package edu.postech.csed332.homework2;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A Boolean constant, either true or false.
 */
public record Constant(boolean value) implements Exp {

    @Override
    public Set<Integer> getVariables() {
        // TODO: implement this
        return Collections.emptySet();
    }

    @Override
    public Boolean evaluate(Map<Integer, Boolean> assignment) {
        // TODO: implement this
        return value;
    }

    @Override
    public Exp simplify() {
        // TODO: implement this
        return this;
    }

    @Override
    public String toPrettyString() {
        return Boolean.toString(value());
    }
}