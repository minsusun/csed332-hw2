package edu.postech.csed332.homework2;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An implementation of Graph with an adjacency list representation.
 * NOTE: you should NOT add more member variables to this class.
 *
 * @param <N> type of vertices, which must be immutable and comparable
 */
public class AdjacencyListGraph<N extends Comparable<N>> implements MutableGraph<N> {

    /**
     * A map from vertices to the sets of their adjacent vertices. For example, a graph
     * with vertices {v1, v2, v3, v4} and edges {(v1,v2), (v1,v3), (v2,v1), (v3,v1)} is
     * represented by the map {v1 |-> {v2, v3}, v2 |-> {v1}, v3 |-> {v1}, v4 |-> {}}.
     */
    private final @NotNull SortedMap<N, SortedSet<N>> adjMap;

    /*
     * Invariant
     *  adjMap is not null
     *  adjMap has unique keys which are in V_{this}
     *  each value of adjMap has unique elements in V_{this}
     *  for all edges (v,w) in E_{this}, (w,v) is in E_{this} and both v and w are in V_{this}
     * Abstract Function
     *  represents the graph using the adjacent list adjMap
     */

    /**
     * Creates an empty graph
     */
    public AdjacencyListGraph() {
        adjMap = new TreeMap<>();
    }

    @Override
    public boolean containsVertex(@NotNull N vertex) {
        // TODO: implement this
        return this.adjMap.containsKey(vertex);
    }

    @Override
    public boolean addVertex(@NotNull N vertex) {
        // TODO: implement this
        if(containsVertex(vertex)) {
            return false;
        }
        else {
            this.adjMap.put(vertex, new TreeSet<>());
            return true;
        }
    }

    @Override
    public boolean removeVertex(@NotNull N vertex) {
        // TODO: implement this
        if(containsVertex(vertex)) {
            this.adjMap.get(vertex).forEach(v -> this.adjMap.get(v).remove(vertex));
            this.adjMap.remove(vertex);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean containsEdge(@NotNull N source, @NotNull N target) {
        // TODO: implement this
        if(containsVertex(source)) {
            return this.adjMap.get(source).contains(target);
        }
        else {
            return false;
        }
    }

    @Override
    public boolean addEdge(@NotNull N source, @NotNull N target) {
        // TODO: implement this
        if(containsEdge(source, target)) {
            return false;
        }
        else {
            this.adjMap.computeIfAbsent(source, (vertex) -> new TreeSet<>()).add(target);
            this.adjMap.computeIfAbsent(target, (vertex) -> new TreeSet<>()).add(source);
            return true;
        }
    }

    @Override
    public boolean removeEdge(@NotNull N source, @NotNull N target) {
        // TODO: implement this
        if(containsEdge(source, target)) {
            this.adjMap.get(source).remove(target);
            this.adjMap.get(target).remove(source);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public @NotNull Set<N> getNeighborhood(@NotNull N vertex) {
        // TODO: implement this
        return Collections.unmodifiableSet(this.adjMap.getOrDefault(vertex, new TreeSet<>()));
    }

    @Override
    public @NotNull Set<N> getVertices() {
        return Collections.unmodifiableSet(adjMap.keySet());
    }

    @Override
    public @NotNull Set<Edge<N>> getEdges() {
        return adjMap.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream().map(n -> new Edge<>(entry.getKey(), n)))
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Checks if all class invariants hold for this object.
     *
     * @return true if the representation of this graph is valid
     */
    boolean checkInv() {
        // TODO: implement this
        return this.adjMap.entrySet().stream()
                .allMatch((entry) -> entry.getValue().stream()
                        .allMatch((vertex) -> this.adjMap
                                .getOrDefault(vertex, new TreeSet<>())
                                .contains(entry.getKey())
                        )
                );
    }
}
