package edu.postech.csed332.homework2;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * An abstract test class for MutableGraph with blackbox test methods
 *
 * @param <V> type of vertices
 * @param <G> type of Graph
 */
@Disabled
public abstract class AbstractMutableGraphTest<V extends Comparable<V>, G extends MutableGraph<V>> {

    G graph;
    V v1, v2, v3, v4, v5, v6, v7, v8;

    abstract boolean checkInv();    // call checkInv of graph

    @Test
    void testAddVertex() {
        assertTrue(graph.addVertex(v1));
        assertTrue(graph.containsVertex(v1));
        assertTrue(checkInv());
    }

    @Test
    void testAddDuplicateVertices() {
        assertTrue(graph.addVertex(v6));
        assertTrue(graph.addVertex(v7));
        assertFalse(graph.addVertex(v6));
        assertTrue(graph.containsVertex(v6));
        assertTrue(graph.containsVertex(v7));
        assertTrue(checkInv());
    }

    @Test
    void testFindReachableVertices() {
        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        graph.addVertex(v4);

        assertEquals(Set.of(v1, v2, v3), graph.findReachableVertices(v1));
        assertEquals(Set.of(v1, v2, v3), graph.findReachableVertices(v2));
        assertEquals(Set.of(v1, v2, v3), graph.findReachableVertices(v3));
        assertEquals(Set.of(v4), graph.findReachableVertices(v4));
        assertTrue(graph.findReachableVertices(v5).isEmpty());
        assertTrue(checkInv());
    }

    // TODO: write black-box test cases for each method of MutableGraph with respect to
    //  the specification, including the methods of Graph that MutableGraph extends.

    @Test
    void testAddEdgeSourceInVTargetInV() {
        graph.addVertex(v1);
        graph.addVertex(v2);
        assertTrue(graph.addEdge(v1, v2));

        assertTrue(graph.containsEdge(v1, v2));
        assertTrue(graph.containsEdge(v2, v1));

        assertTrue(checkInv());
    }

    @Test
    void testAddEdgeSourceInVTargetNotInV() {
        graph.addVertex(v1);
        assertTrue(graph.addEdge(v1, v2));

        assertTrue(graph.containsEdge(v1, v2));
        assertTrue(graph.containsEdge(v2, v1));
        assertTrue(graph.containsVertex(v2));

        assertTrue(checkInv());
    }

    @Test
    void testAddEdgeSourceNotInVTargetInV() {
        graph.addVertex(v2);
        assertTrue(graph.addEdge(v1, v2));

        assertTrue(graph.containsEdge(v1, v2));
        assertTrue(graph.containsEdge(v2, v1));
        assertTrue(graph.containsVertex(v1));

        assertTrue(checkInv());
    }

    @Test
    void testAddEdgeSourceNotInVTargetNotInV() {
        assertTrue(graph.addEdge(v1, v2));

        assertTrue(graph.containsEdge(v1, v2));
        assertTrue(graph.containsEdge(v2, v1));
        assertTrue(graph.containsVertex(v1));
        assertTrue(graph.containsVertex(v2));

        assertTrue(checkInv());
    }

    @Test
    void testAddDuplicateEdge() {
        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        graph.addEdge(v4, v3);

        assertFalse(graph.addEdge(v1, v2));

        assertTrue(checkInv());
    }

    @Test
    void testRemoveVertex() {
        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        graph.addEdge(v4, v3);

        assertTrue(graph.removeVertex(v1));
        assertFalse(graph.containsVertex(v1));
        assertFalse(graph.containsEdge(v1, v2));
        assertFalse(graph.containsEdge(v3, v1));

        assertTrue(checkInv());
    }

    @Test
    void testRemoveNonExistingVertex() {
        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        graph.addEdge(v4, v3);

        assertFalse(graph.removeVertex(v8));

        assertTrue(checkInv());
    }

    @Test
    void testRemoveEdge() {
        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        graph.addEdge(v4, v3);

        assertTrue(graph.removeEdge(v3, v4));
        assertFalse(graph.containsEdge(v3, v4));
        assertFalse(graph.containsEdge(v4, v3));

        assertTrue(checkInv());
    }

    @Test
    void testNonExistingEdge() {
        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        graph.addEdge(v4, v3);

        assertFalse(graph.removeEdge(v1, v6));

        assertTrue(checkInv());
    }

    @Test
    void testGetVertices() {
        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        graph.addEdge(v4, v3);
        graph.addVertex(v7);

        assertEquals(Set.of(v1, v2, v3, v4, v7), graph.getVertices());

        assertTrue(checkInv());
    }

    @Test
    void testGetEdges() {
        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        graph.addEdge(v4, v3);
        graph.addVertex(v7);

        assertEquals(Set.of(new Edge<>(v1, v2), new Edge<>(v2, v1), new Edge<>(v1, v3), new Edge<>(v3, v1), new Edge<>(v3, v4), new Edge<>(v4, v3)), graph.getEdges());

        assertTrue(checkInv());
    }

    @Test
    void testGetNeighbors() {
        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        graph.addEdge(v4, v3);
        graph.addVertex(v7);

        assertEquals(Set.of(v2, v3), graph.getNeighborhood(v1));
        assertEquals(Set.of(), graph.getNeighborhood(v7));

        assertTrue(checkInv());
    }

    @Test
    void testGetNonExistingVertexNeighbors() {
        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        graph.addEdge(v4, v3);
        graph.addVertex(v7);

        assertEquals(Set.of(), graph.getNeighborhood(v8));

        assertTrue(checkInv());
    }
}
