package edu.postech.csed332.homework2;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * An abstract test class for MutableTree with blackbox test methods
 *
 * @param <V> type of vertices
 * @param <T> type of Tree
 */
@Disabled
public abstract class AbstractMutableTreeTest<V extends Comparable<V>, T extends MutableTree<V>> {

    T tree;
    V v1, v2, v3, v4, v5, v6, v7, v8;      // vertices which are not a root of tree

    abstract boolean checkInv();    // call checkInv of tree

    @Test
    void testGetDepthRoot() {
        assertEquals(tree.getDepth(tree.getRoot()), 0);
    }

    @Test
    void testGetDepthTwo() {
        tree.addEdge(tree.getRoot(), v1);
        assertEquals(tree.getDepth(v1), 1);
    }

    @Test
    void testGetDepthNoVertex() {
        assertThrows(IllegalArgumentException.class, () -> tree.getDepth(v1));
    }

    // TODO: write black-box test cases for each method of MutableTree with respect to
    //  the specification, including the methods of Tree that MutableTree extends.
    @Test
    void testGetRoot() {
        tree.addEdge(tree.getRoot(), v1);
        tree.addEdge(v1, v2);
        tree.addEdge(tree.getRoot(), v3);

        assertFalse(List.of(v1, v2, v3, v4, v5, v6, v7, v8).contains(tree.getRoot()));

        assertTrue(checkInv());
    }

    @Test
    void testGetHeight() {
        tree.addEdge(tree.getRoot(), v1);
        tree.addEdge(v1, v2);
        tree.addEdge(tree.getRoot(), v3);

        assertEquals(tree.getHeight(), 2);

        assertTrue(checkInv());
    }

    @Test
    void testGetChildren() {
        tree.addEdge(tree.getRoot(), v1);
        tree.addEdge(v1, v2);
        tree.addEdge(tree.getRoot(), v3);
        tree.addEdge(v1, v4);

        assertEquals(Set.of(v2, v4), tree.getChildren(v1));

        assertTrue(checkInv());
    }

    @Test
    void testGetNonExistingVertexChildren() {
        tree.addEdge(tree.getRoot(), v1);
        tree.addEdge(v1, v2);
        tree.addEdge(tree.getRoot(), v3);
        tree.addEdge(v1, v4);

        assertEquals(Set.of(), tree.getChildren(v6));

        assertTrue(checkInv());
    }

    @Test
    void testGetParent() {
        tree.addEdge(tree.getRoot(), v1);
        tree.addEdge(v1, v2);
        tree.addEdge(tree.getRoot(), v3);
        tree.addEdge(v1, v4);

        assertEquals(tree.getParent(v2), Optional.of(v2));

        assertTrue(checkInv());
    }

    @Test
    void testGetParentRoot() {
        tree.addEdge(tree.getRoot(), v1);
        tree.addEdge(v1, v2);
        tree.addEdge(tree.getRoot(), v3);
        tree.addEdge(v1, v4);

        assertEquals(tree.getParent(tree.getRoot()), Optional.empty());

        assertTrue(checkInv());
    }

    @Test
    void testGetParentNonExistingVertex() {
        tree.addEdge(tree.getRoot(), v1);
        tree.addEdge(v1, v2);
        tree.addEdge(tree.getRoot(), v3);
        tree.addEdge(v1, v4);

        assertEquals(tree.getParent(v6), Optional.empty());

        assertTrue(checkInv());
    }

    @Test
    void testAddVertex() {
        tree.addEdge(tree.getRoot(), v1);
        tree.addEdge(v1, v2);
        tree.addEdge(tree.getRoot(), v3);
        tree.addEdge(v1, v4);

        assertTrue(tree.addVertex(v5));

        assertFalse(checkInv());
    }

    @Test
    void testAddDuplicateVertex() {
        tree.addEdge(tree.getRoot(), v1);
        tree.addEdge(v1, v2);
        tree.addEdge(tree.getRoot(), v3);
        tree.addEdge(v1, v4);

        assertFalse(tree.addVertex(v1));

        assertTrue(checkInv());
    }

    @Test
    void testRemoveVertexIllegalArgumentException() {
        tree.addEdge(tree.getRoot(), v1);
        tree.addEdge(v1, v2);
        tree.addEdge(tree.getRoot(), v3);
        tree.addEdge(v1, v4);

        assertThrows(IllegalArgumentException.class, () -> tree.removeVertex(tree.getRoot()));

        assertTrue(checkInv());
    }

    @Test
    void testRemoveVertex() {
        tree.addEdge(tree.getRoot(), v1);
        tree.addEdge(v1, v2);
        tree.addEdge(tree.getRoot(), v3);
        tree.addEdge(v1, v4);

        assertTrue(tree.removeVertex(v1));
        assertFalse(tree.containsVertex(v2));
        assertFalse(tree.containsVertex(v4));
        assertFalse(tree.containsEdge(v1, v2));
        assertFalse(tree.containsEdge(v4, v1));
        assertFalse(tree.containsEdge(tree.getRoot(), v1));

        assertTrue(checkInv());
    }

    @Test
    void testRemoveNonExistingVertex() {
        tree.addEdge(tree.getRoot(), v1);
        tree.addEdge(v1, v2);
        tree.addEdge(tree.getRoot(), v3);
        tree.addEdge(v1, v4);

        assertFalse(tree.removeVertex(v6));

        assertTrue(checkInv());
    }

    @Test
    void testAddEdgeSourceInVTargetInV() {
        tree.addEdge(tree.getRoot(), v1);
        tree.addEdge(v1, v2);
        tree.addEdge(tree.getRoot(), v3);
        tree.addEdge(v1, v4);

        assertFalse(tree.addEdge(v1,v4));
        assertFalse(tree.containsEdge(v4, v1));
        assertFalse(tree.containsEdge(v1, v4));

        assertTrue(checkInv());
    }

    @Test
    void testAddEdgeSourceInVTargetNotInV() {
        tree.addEdge(tree.getRoot(), v1);
        tree.addEdge(v1, v2);
        tree.addEdge(tree.getRoot(), v3);
        tree.addEdge(v1, v4);

        assertTrue(tree.addEdge(v4, v6));
        assertTrue(tree.containsVertex(v6));
        assertTrue(tree.containsEdge(v4, v6));
        assertTrue(tree.containsEdge(v6, v4));

        assertTrue(checkInv());
    }

    @Test
    void testAddEdgeSourceNotInVTargetInV() {
        tree.addEdge(tree.getRoot(), v1);
        tree.addEdge(v1, v2);
        tree.addEdge(tree.getRoot(), v3);
        tree.addEdge(v1, v4);

        assertFalse(tree.addEdge(v6,v4));
        assertFalse(tree.containsVertex(v6));
        assertFalse(tree.containsEdge(v4, v6));
        assertFalse(tree.containsEdge(v6, v4));

        assertTrue(checkInv());
    }

    @Test
    void testAddEdgeSourceNotInVTargetNotInV() {
        tree.addEdge(tree.getRoot(), v1);
        tree.addEdge(v1, v2);
        tree.addEdge(tree.getRoot(), v3);
        tree.addEdge(v1, v4);

        assertFalse(tree.addEdge(v6,v5));
        assertFalse(tree.containsVertex(v6));
        assertFalse(tree.containsVertex(v5));
        assertFalse(tree.containsEdge(v5, v6));
        assertFalse(tree.containsEdge(v6, v5));

        assertTrue(checkInv());
    }

    @Test
    void testRemoveEdge() {
        tree.addEdge(tree.getRoot(), v1);
        tree.addEdge(v1, v2);
        tree.addEdge(tree.getRoot(), v3);
        tree.addEdge(v1, v4);

        assertTrue(tree.removeEdge(tree.getRoot(), v1));
        assertFalse(tree.containsVertex(v1));
        assertFalse(tree.containsVertex(v2));
        assertFalse(tree.containsVertex(v4));
        assertFalse(tree.containsEdge(tree.getRoot(), v1));
        assertFalse(tree.containsEdge(v1, v2));
        assertFalse(tree.containsEdge(v1, v4));

        assertTrue(checkInv());
    }

    @Test
    void testRemoveNonExistingEdge() {
        tree.addEdge(tree.getRoot(), v1);
        tree.addEdge(v1, v2);
        tree.addEdge(tree.getRoot(), v3);
        tree.addEdge(v1, v4);

        assertFalse(tree.removeEdge(v6, v8));

        assertTrue(checkInv());
    }
}