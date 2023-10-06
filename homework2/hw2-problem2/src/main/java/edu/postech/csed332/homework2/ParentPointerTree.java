package edu.postech.csed332.homework2;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An implementation of Tree, where each vertex has a reference to its parent node but
 * no references to its children.
 *
 * @param <N> type of vertices, which must be immutable and comparable
 */
public class ParentPointerTree<N extends Comparable<N>> implements MutableTree<N> {

    private record Node<V>(@Nullable V parent, int depth) {
    }

    /**
     * A root vertex of this tree.
     */
    private final @NotNull N root;

    /**
     * A map assigning to each vertex a pair of a parent reference and a depth. The parent
     * of the root is {@code null}. For example, a tree with  vertices {v1, v2, v3, v4} edges
     * {(v1,v2), (v1,v3), (v2,v1), (v2,v4), (v3,v1), (v4,v2)}, where v1 is the root, is
     * represented by the map {v1 |-> (null,0), v2 |-> (v1,1), v3 |-> (v1,1), v4 |-> (v2,2)}.
     */
    private final @NotNull SortedMap<N, Node<N>> nodeMap;

    /*
     * Invariant
     *  root is not null
     *  nodeMap has unique keys which are in V_{this}
     *  each value of nodeMap has Node<V> datatype with indicating the unique parent and its depth
     *  for all edges (v,w) in E_{this}, (w,v) is in E_{this} and both v and w are in V_{this}
     *  for all vertices v in V_{this} except root, has a only unique Path(from root to v)
     * Abstract Function
     *  represents the tree with child-parent map
     */

    /**
     * Create a parent pointer tree with a given root vertex.
     *
     * @param vertex the root of the tree
     */
    public ParentPointerTree(@NotNull N vertex) {
        root = vertex;
        nodeMap = new TreeMap<>();
        nodeMap.put(root, new Node<>(null, 0));
    }

    @Override
    public @NotNull N getRoot() {
        return root;
    }

    @Override
    public int getDepth(@NotNull N vertex) {
        if(containsVertex(vertex)) {
            return nodeMap.get(vertex).depth();
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int getHeight() {
        // TODO: implement this
        return Collections.max(this.nodeMap.values().stream().map(Node::depth).toList());
    }

    @Override
    public @NotNull Set<N> getChildren(@NotNull N vertex) {
        // TODO: implement this
        if(containsVertex(vertex)) {
            return this.nodeMap.entrySet().stream()
                    .filter((entry) -> entry.getValue().parent() == vertex).map(Map.Entry::getKey)
                    .collect(Collectors.toUnmodifiableSet());
        }
        else {
            return Set.of();
        }
    }

    @Override
    public @NotNull Optional<N> getParent(@NotNull N vertex) {
        // TODO: implement this
        if(containsVertex(vertex)) {
            return Optional.ofNullable(this.nodeMap.get(vertex).parent());
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public boolean containsVertex(@NotNull N vertex) {
        // TODO: implement this
        return this.nodeMap.containsKey(vertex);
    }

    @Override
    public boolean addVertex(@NotNull N vertex) {
        if(containsVertex(vertex)) {
            return false;
        }
        else {
            this.nodeMap.put(vertex, new Node<>(null, 0));
            return true;
        }
    }

    @Override
    public boolean removeVertex(@NotNull N vertex) {
        // TODO: implement this
        if(getRoot() == vertex) {
            throw new IllegalArgumentException();
        }
        else if(containsVertex(vertex)) {
            // using ~.toList() in order to prevent concurrent modifying exception
            this.nodeMap.entrySet().stream()
                    .filter((entry) -> entry.getValue().parent() == vertex).map(Map.Entry::getKey)
                    .toList().forEach(this::removeVertex);
            this.nodeMap.remove(vertex);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean containsEdge(@NotNull N source, @NotNull N target) {
        // TODO: implement this
        return getEdges().stream().anyMatch((edge) -> edge.compareTo(new Edge<>(source, target)) == 0);
    }

    @Override
    public boolean addEdge(@NotNull N source, @NotNull N target) {
        // TODO: implement this
        if(containsVertex(source) && !containsVertex(target)) {
            this.nodeMap.put(target, new Node<>(source, getDepth(source) + 1));
            return true;
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
        if(containsVertex(vertex)) {
            return this.nodeMap.keySet().stream()
                    .filter((node) -> this.nodeMap.get(node).parent() == vertex || this.nodeMap.get(vertex).parent() == node)
                    .collect(Collectors.toUnmodifiableSet());
        }
        else {
            return Set.of();
        }
    }

    @Override
    public @NotNull Set<N> getVertices() {
        // TODO: implement this
        return Collections.unmodifiableSet(this.nodeMap.keySet());
    }

    @Override
    public @NotNull Set<Edge<N>> getEdges() {
        // TODO: implement this
        return this.nodeMap.keySet().stream()
                .map((node) -> getNeighborhood(node).stream()
                        .map((neighbor) -> List.of(new Edge<>(node, neighbor), new Edge<>(neighbor, node)))
                .flatMap(List::stream).toList()).flatMap(List::stream).collect(Collectors.toUnmodifiableSet());
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
