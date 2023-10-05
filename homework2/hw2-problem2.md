# Problem 2-1


Let $`\mathcal{N}`$ be the set of all elements of type $`\textsf{N}`$, and $`\mathsf{null} \notin \mathcal{N}`$ be an distinguished element to represent `null`. Write formal abstract specifications of the interfaces below with respect to following abstract values:

- A graph is a pair $`G = (V, E)`$, where $`V \subseteq \mathcal{N}`$ and $`E \subseteq V \times V`$.
- A tree is a triple $`T = (V, E, \hat{v})`$, where $`(V, E)`$ is a graph and $`\hat{v} \in \mathcal{N}`$ denotes the root.

Other data types, such as `boolean`, `int`, `Set<N>`, etc. have conventional abstract values, e.g., Boolean values, integers, and subsets of $`\mathcal{N}`$, etc.


## `Graph<N>`

Let $`G_{this} = (V_{this}, E_{this})`$ be an abstract value of the current graph object. 

##### Class invariant 

```math
\forall (v, w) \in E_{this}.\ v, w \in V_{this} \land (w, v) \in E_{this}
```

##### containsVertex

```java 
boolean containsVertex(N vertex);
```

- requires: vertex is in $`\mathcal{N}`$ and not $`\mathsf{null}`$
- ensures:  
    + returns true if vertex is in $`V_{this}`$; and
    - returns false, otherwise.

##### containsEdge

```java
boolean containsEdge(N source, N target);
```

- requires:
  + `source` is in $`\mathcal{N}`$ and not $`\mathsf{null}`$
  + `target` is in $`\mathcal{N}`$ and not $`\mathsf{null}`$
- ensures:
  + returns true if $`(source,target)`$ is in $`E_{this}`$; and
  - returns false, otherwise.

##### getNeighborhood

```java
Set<N> getNeighborhood(N vertex);
```

- requires:
  + vertex is in $`\mathcal{N}`$ and not $`\mathsf{null}`$
- ensures:
  + return the set of adjacent vertices of `vertex`


## `Tree<N>`

Let $`T_{this} = (V_{this}, E_{this}, \hat{v}_{this})`$ be an abstract value of the current graph object. 

##### Class invariant 

```math
\forall (v, w) \in E_{this}.\ v, w \in V_{this} \land (w, v) \in E_{this}\\
\forall v \in V_{this},v\neq \hat{v}_{this},\texttt{there exists a unique path }P(\hat{v}_{this},v)\texttt{ between } v \texttt{ and } \hat{v}_{this}\texttt{in }T_{this}
```

##### getDepth

```java
int getDepth(N vertex);
```

- requires: 
  + vertex is in $`\mathcal{N}`$ and not $`\mathsf{null}`$.
- ensures:  
  + returns 0 if vertex is getRoot(); and
  + returns getDepth(getParent(vertex)) + 1, otherwise.

##### getHeight

```java
int getHeight();
```

- requires: true
- ensures: returns the height of $`T_{this}`$

##### getChildren

```java
Set<N> getChildren(N vertex);
```

- requires: 
  + vertex is in $`\mathcal{N}`$ and not $`\mathsf{null}`$.
- ensures:
  + returns the set of children of vertex in the tree; and
  + returns an empty set if given vertex does not have any children.

##### getParent

```java
Optional<N> getParent(N vertex);
```

- requires: vertex is in $`\mathcal{N}`$ and not $`\mathsf{null}`$.
- ensures:
  + returns the parent of vertex; and
  + returns `Optional.empty()` if given vertex does not have parent.


## `MutableGraph<N>`

Let $`G_{this} = (V_{this}, E_{this})`$ be an abstract value of the current graph object,
and $`G_{next} = (V_{next}, E_{next})`$ be an abstract value of the graph object _modified by_ the method call. 

##### Class invariant 

```math
\forall (v, w) \in E_{this}.\ v, w \in V_{this} \land (w, v) \in E_{this}
```

##### addVertex

```java
boolean addVertex(N vertex);
```

- requires: vertex is in $`\mathcal{N}`$ and not $`\mathsf{null}`$
- ensures:  
  + $`V_{next} = V_{this} \cup \{\texttt{vertex}\}`$; 
  + $`E_{next} = E_{this}`$ (the edges are not modified)
  + If $`G_{this}`$ satisfies the class invariant, $`G_{next}`$ also satisfies the class invariant; and
  + returns true if and only if $`\texttt{vertex} \notin V_{this}`$.

##### removeVertex

```java
boolean removeVertex(N vertex);
```

- requires: vertex is in $`\mathcal{N}`$ and not $`\mathsf{null}`$
- ensures:
  + $`V_{next}=V_{this}-\{\texttt{vertex}\}`$
  + $`E_{next} = E_{this}-\{(u,\texttt{vertex})|\forall u \in V_{this}\}\cup \{(\texttt{vertex},u)|\forall u \in V_{this}\}`$
  + If $`G_{this}`$ satisfies the class invariant, $`G_{next}`$ also satisfies the class invariant; and
  + returns true if and only if $`\texttt{vertex} \in V_{this}`$.

##### addEdge

```java
boolean addEdge(N source, N target);
```

- requires:
  + source is in $`\mathcal{N}`$ and not $`\mathsf{null}`$
  + target is in $`\mathcal{N}`$ and not $`\mathsf{null}`$
- ensures:
  + $`V_{next}=V_{this}\cup \{\texttt{source,target}\}`$
  + $`E_{next}=E_{this}\cup \{\texttt{(source, target), (target,source)}\}`$
  + If $`G_{this}`$ satisfies the class invariant, $`G_{next}`$ also satisfies the class invariant; and
  + returns true if and only if $`\texttt{(source,target)} \notin E_{this}\land \texttt{(target,source)} \notin E_{this}`$.

##### removeEdge

```java
boolean removeEdge(N source, N target);
```

- requires:
  + source is in $`\mathcal{N}`$ and not $`\mathsf{null}`$
  + target is in $`\mathcal{N}`$ and not $`\mathsf{null}`$
- ensures:
  + $`V_{next}=V_{this}`$ (the vertices are not modified)
  + $`E_{next}=E_{this}-\{\texttt{(source, target), (target,source)}\}`$
  + If $`G_{this}`$ satisfies the class invariant, $`G_{next}`$ also satisfies the class invariant; and
  + returns true if and only if $`\texttt{(source,target)} \in E_{this}\land \texttt{(target,source)} \in E_{this}`$.


## `MutableTree<N>`

Let $`T_{this} = (V_{this}, E_{this}, \hat{v}_{this})`$ be an abstract value of the current tree object,
and $`T_{next} = (V_{next}, E_{next}, \hat{v}_{next})`$ be an abstract value of the tree object _modified by_ the method call. 

##### Class invariant 

```math
\forall (v, w) \in E_{this}.\ v, w \in V_{this} \land (w, v) \in E_{this}\\
```

##### addVertex

```java
boolean addVertex(N vertex);
```

- requires: vertex is in $`\mathcal{N}`$ and not $`\mathsf{null}`$
- ensures:  
  + $`V_{next} = V_{this} \cup \{\texttt{vertex}\}`$; 
  + $`E_{next} = E_{this}`$ (the edges are not modified)
  + $`T_{next}`$ will not satisfies the class invariant if $`\texttt{vertex} \notin V_{this}`$
  + returns true if and only if $`\texttt{vertex} \notin V_{this}`$.

##### removeVertex

```java
boolean removeVertex(N vertex);
```

- requires: vertex is in $`\mathcal{N}`$ and not $`\mathsf{null}`$
- ensures:
  + throws `IllegalArgumentException`, if $`\texttt{vertex}=\hat{v}_{this}`$
  + otherwise
    + Let $`V=\{\texttt{vertex}\}\cup\{v\in V_{this}|v \texttt{ is descendant of vertex}\}`$
    + $`V_{next}=V_{this}-V`$
    + $`E_{next} = E_{this}-\{(u,v)|\forall u \in V_{this}.\forall v\in V\}\cup \{(v,u)|\forall v\in V.\forall u \in V_{this}\}`$
    + If $`T_{this}`$ satisfies the class invariant, $`T_{next}`$ also satisfies the class invariant; and
    + returns true if and only if $`\texttt{vertex} \in V_{this}`$.

##### addEdge

```java
boolean addEdge(N source, N target);
```

- requires:
  + source is in $`\mathcal{N}`$ and not $`\mathsf{null}`$
  + target is in $`\mathcal{N}`$ and not $`\mathsf{null}`$
- ensures:
  + $`V_{next}=V_{this}`$
  + $`E_{next}=E_{this}\cup \{\texttt{(source, target), (target,source)}\}`$
  + returns true if $`\texttt{(source,target)} \notin E_{this}\land \texttt{(target,source)} \notin E_{this}`$ or $`\texttt{source}\in V_{this}\land \texttt{target}\notin V_{this}`$.

##### removeEdge

```java
boolean removeEdge(N source, N target);
```

- requires:
  + source is in $`\mathcal{N}`$ and not $`\mathsf{null}`$
  + target is in $`\mathcal{N}`$ and not $`\mathsf{null}`$
- ensures:
  + Let $`\texttt{children}=child(source,target)`$
  + Let $`\texttt{parent}=parent(source,target)`$
  + Let $`V=\{\texttt{children}\}\cup\{v\in V_{this}|v \texttt{ is descendant of children}\}`$
  + $`V_{next}=V_{this}-V`$
  + $`E_{next}=E_{this}-\{(u,v)|\forall u \in V_{this}.\forall v\in V\}\cup \{(v,u)|\forall v\in V.\forall u \in V_{this}\}`$
  + If $`T_{this}`$ satisfies the class invariant, $`T_{next}`$ also satisfies the class invariant; and
  + returns true if and only if $`\texttt{(source,target)} \in E_{this}\land \texttt{(target,source)} \in E_{this}`$.


# Problem 2-2

Identify whether the abstract interfaces satisfy the Liskov substitution principle.
For each question, explain your reasoning _using the abstract specifications that you have defined in Problem 1_. 


##### `Tree<N>` and `Graph<N>`

* Is `Tree<N>` a subtype of `Graph<N>`?
  * Answer : Yes
  * Reasons
    * Since performing the methods of `Graph<N>` on `Tree<N>` works same; and
    * class invariants of `Tree<N>` is stronger than `Graph<N>`'s.

##### `MutableGraph<N>` and `Graph<N>`

* Is `MutableGraph<N>` a subtype of `Graph<N>`?
  * Answer : Yes
  * Reason
    * After executing `removeVertex` from `MutableGraph<N>` does not gaurantee that the result of executing `containsVertex` on both `Graph<N>` and `MutableGraph<N>` are same.

##### `MutableTree<N>` and `Tree<N>`

* Is `MutableTree<N>` a subtype of `Tree<N>`?
  * Answer : No
  * Reason
    * `int getDepth(@NotNull N vertex)` : If a vertex added to `MutableTree<N>` by `boolean addVertex(@NotNull N vertex)` with vertex not connected to any vertex, calling `getDepth(vertex)` cannot perform well since the vertex added by `addVertex` does not have path from root to given vertex.

##### `MutableTree<N>` and `MutableGraph<N>`

* Is `MutableTree<N>` a subtype of `MutableGraph<N>`?
  * Answer : No
  * Reason
    * `boolean addEdge(@NotNull N source, @NotNull N target)` : If a edge between a vertex in $V_{this}$ and another vertex not in $V_{this}$ is added to `MutableTree<N>` by `addEdge`, `MutableTree<N>` does not add a new vertex connected by a added edge, but `MutableGraph<N>` should.
