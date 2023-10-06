package edu.postech.csed332.homework2;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An implementation of Tree that delegates to a given instance of Graph. This class
 * is a wrapper of a MutableGraph instance that enforces the Tree invariant.
 * NOTE: you should NOT add more member variables to this class.
 *
 * @param <N> type of vertices, which must be immutable and comparable
 */
public class DelegateTree<N extends Comparable<N>> implements MutableTree<N> {

    /**
     * A root vertex of this tree.
     */
    private final @NotNull N root;

    /**
     * The underlying graph of this tree
     */
    private final @NotNull MutableGraph<N> delegate;

    /**
     * A map assigning a depth to each vertex in this tree
     */
    private final @NotNull Map<N, Integer> depthMap;

    /*
     * Invariant
     *  root is not null
     *  delegate is not null
     *  delegate contains all vertices and edges information
     *  depthMap is not null
     *  depthMap has unique keys which are in V_{this}
     *  each value of depthMap indicates its depth
     *  for all edges (v,w) in E_{this}, (w,v) is in E_{this} and both v and w are in V_{this}
     *  for all vertices v in V_{this} except root, has only unique Path(from root to v)
     * Abstract Function
     *  represents the tree
     */

    /**
     * Creates a tree that delegates to a given graph.
     *
     * @param emptyGraph an empty graph
     * @param vertex     the root of the tree
     * @throws IllegalArgumentException if {@code emptyGraph} is not empty
     */
    public DelegateTree(@NotNull MutableGraph<N> emptyGraph, @NotNull N vertex) {
        if (!emptyGraph.getVertices().isEmpty())
            throw new IllegalArgumentException();

        delegate = emptyGraph;
        depthMap = new HashMap<>();
        root = vertex;

        delegate.addVertex(root);
        depthMap.put(root, 0);
    }

    @Override
    public @NotNull N getRoot() {
        return root;
    }

    @Override
    public int getDepth(@NotNull N vertex) {
        // TODO: implement this
        if(containsVertex(vertex)) {
            return this.depthMap.get(vertex);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int getHeight() {
        // TODO: implement this
        return Collections.max(this.depthMap.values().stream().toList());
    }

    @Override
    public @NotNull Set<N> getChildren(@NotNull N vertex) {
        // TODO: implement this
        if(containsVertex(vertex)) {
            return delegate.getNeighborhood(vertex).stream().filter((neighbor) -> depthMap.get(neighbor) > depthMap.get(vertex)).collect(Collectors.toUnmodifiableSet());
        }
        else {
            return Set.of();
        }
    }

    @Override
    public @NotNull Optional<N> getParent(@NotNull N vertex) {
        // TODO: implement this
        if(containsVertex(vertex)) {
            if(vertex == getRoot()) {
                return Optional.empty();
            }
            else {
                return Optional.of(delegate.getNeighborhood(vertex).stream().filter((neighbor) -> depthMap.get(neighbor) < depthMap.get(vertex)).toList().get(0));
            }
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public boolean containsVertex(@NotNull N vertex) {
        // TODO: implement this
        return delegate.containsVertex(vertex);
    }

    @Override
    public boolean addVertex(@NotNull N vertex) {
        if(containsVertex(vertex)) {
            return false;
        }
        else {
            this.depthMap.put(vertex, 0);
            return delegate.addVertex(vertex);
        }
    }

    @Override
    public boolean removeVertex(@NotNull N vertex) {
        // TODO: implement this
        if(vertex == getRoot()) {
            throw new IllegalArgumentException();
        }
        else if(containsVertex(vertex)) {
            getChildren(vertex).forEach(this::removeVertex);
            depthMap.remove(vertex);
            delegate.removeVertex(vertex);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean containsEdge(@NotNull N source, @NotNull N target) {
        // TODO: implement this
        return delegate.containsEdge(source, target);
    }

    @Override
    public boolean addEdge(@NotNull N source, @NotNull N target) {
        // TODO: implement this
        if(containsVertex(source) && !containsVertex(target)) {
            this.depthMap.put(target, this.depthMap.get(source) + 1);
            return delegate.addEdge(source, target);
        }
        else {
            return false;
        }
    }

    @Override
    public boolean removeEdge(@NotNull N source, @NotNull N target) {
        // TODO: implement this
        if(containsEdge(source, target)) {
            if(getParent(target).equals(Optional.of(source))) {
                removeVertex(target);
            }
            else {
                removeVertex(source);
            }
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public @NotNull Set<N> getNeighborhood(@NotNull N vertex) {
        // TODO: implement this
        return delegate.getNeighborhood(vertex);
    }

    @Override
    public @NotNull Set<N> getVertices() {
        // TODO: implement this
        return delegate.getVertices();
    }

    @Override
    public @NotNull Set<Edge<N>> getEdges() {
        // TODO: implement this
        return delegate.getEdges();
    }

    /**
     * Checks if all class invariants hold for this object
     *
     * @return true if the representation of this tree is valid
     */
    boolean checkInv() {
        // TODO: implement this
        return findReachableVertices(getRoot()).equals(getVertices());
    }
}
