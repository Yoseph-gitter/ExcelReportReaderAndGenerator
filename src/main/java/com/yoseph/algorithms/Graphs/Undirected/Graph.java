package com.yoseph.algorithms.Graphs.Undirected;

import com.yoseph.algorithms.Graphs.Vertex;
import com.yoseph.algorithms.Graphs.VertexStates;

import java.util.*;

/**
 * Array Impl of Undirected graph
 */
public class Graph implements IGraph {
    public Vertex[] getVertices() {
        return vertices;
    }

    public void setVertices(Vertex[] vertices) {
        this.vertices = vertices;
    }

    public int[][] getAdjMatrix() {
        return adjMatrix;
    }

    public void setAdjMatrix(int[][] adjMatrix) {
        this.adjMatrix = adjMatrix;
    }

    private Vertex[] vertices;
    private int MAX_SIZE;
    private int[][] adjMatrix;
    private int currentVertix;

    public Graph(int max_size) {
        this.MAX_SIZE = max_size;
        this.vertices = new Vertex[this.MAX_SIZE];
        this.adjMatrix = new int[MAX_SIZE][MAX_SIZE];
        this.currentVertix = 0;
    }

    public void addVertix(char label) {
        Vertex vertex = new Vertex(label);
        this.vertices[this.currentVertix++] = vertex;
    }

    public void addEdge(int source, int sync) {
        this.adjMatrix[source][sync] = 1;
        this.adjMatrix[sync][source] = 1;
    }

    public void isGraphCyclicUsingBfs() {
        Vertex rootVertex = vertices[0];
        int[] parent = new int[vertices.length];
        rootVertex.setSTATE(VertexStates.VISITED);
        Queue<Vertex> queue = new LinkedList<>();
        queue.add(rootVertex);
        parent[0] = -1;
        Set<Vertex> cycleDetectedVertices = new HashSet<>();
        cyclicBfs(parent, queue, cycleDetectedVertices);
        for (Vertex V : cycleDetectedVertices) {
            System.out.println("Cycle detected at vertics BFS: " + V.getLabel());
        }
    }

    private void cyclicBfs(int[] parent, Queue<Vertex> queue, Set<Vertex> cycleDetectedVertices) {
        if (queue.isEmpty()) {
            return;
        }
        Vertex rootVertex = queue.remove();
        int index = findVertexIndex(rootVertex);
        List<Vertex> allAdjacentVertices = findAllAdjacentVertices(rootVertex);
        for (Vertex adjVertex : allAdjacentVertices) {
            int adjIndex = findVertexIndex(adjVertex);
            if (adjVertex.getSTATE() == VertexStates.UNVISITED) {
                parent[adjIndex] = index;
                adjVertex.setSTATE(VertexStates.VISITED);
                queue.add(adjVertex);
            } else {
                //if already visited
                if (parent[adjIndex] == parent[index]) {
                    cycleDetectedVertices.add(adjVertex);
                    //System.out.println("Cycle detected at vertex : " + adjVertex.getLabel());
                }
            }
        }
        cyclicBfs(parent, queue, cycleDetectedVertices);
    }

    public void isGraphCyclicUsingDfs() {
        Map<Integer, Integer> vertixRank = new HashMap<>();
        List<Vertex> cycleDetectedLocs = new LinkedList<>();
        int RANK = 0;
        Stack<Vertex> stack = new Stack<>();
        Vertex rootVertex = vertices[0];
        rootVertex.setSTATE(VertexStates.VISITED);
        vertixRank.put(RANK, RANK);
        stack.push(rootVertex);

        while (!stack.isEmpty()) {
            Vertex top = stack.peek();
            Vertex adjacentVtx = findAdjacentUnvisitedVertex(top);
            if (adjacentVtx != null) {
                adjacentVtx.setSTATE(VertexStates.VISITED);
                int adjIndex = findVertexIndex(adjacentVtx);
                vertixRank.put(adjIndex, ++RANK);
                stack.push(adjacentVtx);
            } else {
                //if there is no adjacent vertices ..check for cycle
                if (isCycle(top, vertixRank)) {
                    cycleDetectedLocs.add(top);
                }
                top.setSTATE(VertexStates.DISCARDED);
                stack.pop();
            }
        }
        for (Vertex V : cycleDetectedLocs) {
            System.out.println("Graph Cycle detected using DFS : " + V.getLabel());
        }
        return;
    }


    private boolean isCycle(Vertex top, Map<Integer, Integer> vertixRank) {
        List<Vertex> adjacents = findAllAdjacentVertices(top);
        int topIndex = findVertexIndex(top);
        for (Vertex vertex : adjacents) {
            int index = findVertexIndex(vertex);
            if (index != -1 && adjacents.size() != 1
                    && (vertixRank.get(index) < vertixRank.get(topIndex) - 1)) {
                return true;
            }
        }
        return false;
    }


    public void bridthFirstSearchIteratively() {
        Vertex vertex = vertices[0];
        Queue<Vertex> queue = new LinkedList<>();
        vertex.setSTATE(VertexStates.VISITED);
        queue.add(vertex);
        while (!queue.isEmpty()) {
            printIterableStructure(queue);
            Vertex first = findAdjacentUnvisitedVertex(queue.peek());
            if (first == null) {
                queue.remove();
            } else {
                queue.add(first);
            }
        }
    }

    public void bfs() {
        Vertex entryVertix = vertices[0];
        Queue<Vertex> queue = new LinkedList<>();
        entryVertix.setSTATE(VertexStates.VISITED);
        queue.add(entryVertix);
        bfsRecrsive(queue);
    }

    public void bfsRecrsive(Queue<Vertex> queue) {
        if (queue.isEmpty()) {
            return;
        }
        printIterableStructure(queue);
        Vertex top = queue.remove();
        List<Vertex> adjacentVerex = findAdjacentVtsxs(top);
        for (Vertex vertex : adjacentVerex) {
            vertex.setSTATE(VertexStates.VISITED);
            queue.add(vertex);
        }
        bfsRecrsive(queue);
    }

    public void dfs() {
        Vertex startVtx = vertices[0];
        startVtx.setSTATE(VertexStates.VISITED);
        List<Vertex> vetxs = findAdjacentVtsxs(startVtx);
        for (Vertex vertex : vetxs) {
            System.out.print(startVtx.getLabel());
            dfsRecursive(vertex);
        }
    }

    public void dfsRecursive(Vertex sourceVertex) {
        if (sourceVertex == null) {
            System.out.println();
            return;
        }
        System.out.print(sourceVertex.getLabel());
        Vertex adjacentVertex = findAdjacentUnvisitedVertex(sourceVertex);
        dfsRecursive(adjacentVertex);
    }

    /**
     * This dfs method also does the task of printing the edges while pushing a new vertex
     * which we call it finding the minimum spanning tree. This could be extended to storing all the edges
     * in some data structure and do whatever we want. we can also call this method from each vertics and find out
     * all the possible MSTs.
     */
    public void depthFirstSearchIteratively() {
        Vertex startVtx = vertices[0];
        Stack<Vertex> stk = new Stack<>();
        stk.push(startVtx);
        startVtx.setSTATE(VertexStates.VISITED);
        while (!stk.isEmpty()) {
            Vertex top = stk.peek();
            // printIterableStructure(stk);
            Vertex adjacentVertex = findAdjacentUnvisitedVertex(top);
            if (adjacentVertex != null) {
                printVertex(top);
                printVertex(adjacentVertex);
                adjacentVertex.setSTATE(VertexStates.VISITED);
                stk.push(adjacentVertex);
            } else {
                printVertex(top);
                stk.pop();
                if (!stk.isEmpty())
                    printVertex(stk.peek());
            }
            System.out.print(" ");
        }
    }

    private void printVertex(Vertex vertex) {
        System.out.print(vertex.getLabel());
    }

    private void printIterableStructure(Iterable<Vertex> iterable) {
        Iterator<Vertex> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next().getLabel());
        }
        System.out.println();
    }

    private List<Vertex> findAdjacentVtsxs(Vertex vertex) {
        List<Vertex> neighbours = new LinkedList<>();
        int row = findVertexIndex(vertex);
        for (int j = 0; j < this.vertices.length && row != -1; j++) {
            if (adjMatrix[row][j] != 0 && vertices[j].getSTATE() != VertexStates.UNVISITED) {
                vertices[j].setSTATE(VertexStates.VISITED);
                neighbours.add(vertices[j]);
            }
        }
        return neighbours;
    }

    private List<Vertex> findAllAdjacentVertices(Vertex vertex) {
        List<Vertex> neighbours = new LinkedList<>();
        int row = findVertexIndex(vertex);
        for (int j = 0; j < this.vertices.length && row != -1; j++) {
            if (adjMatrix[row][j] != 0) {
                neighbours.add(vertices[j]);
            }
        }
        return neighbours;
    }

    private Vertex findAdjacentUnvisitedVertex(Vertex vertex) {
        int row = findVertexIndex(vertex);
        for (int j = 0; j < this.vertices.length && row != -1; j++) {
            if (adjMatrix[row][j] != 0 && vertices[j].getSTATE() == VertexStates.UNVISITED) {
                //vertices[j].setSTATE(VertexStates.VISITED);
                return vertices[j];
            }
        }
        return null;
    }

    private int findVertexIndex(Vertex vertex) {
        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i].getLabel() == vertex.getLabel()) {
                return i;
            }
        }
        return -1;
    }

    public void cycleDetectorDfs_1() {
        List<Vertex> cycleVerties = new LinkedList<>();
        Stack<Vertex> stack = new Stack<>();
        Vertex rootVertex = vertices[0];
        Map<Integer, Integer> parent = new HashMap<>();
        parent.put(rootVertex.hashCode(), Integer.MIN_VALUE);
        rootVertex.setSTATE(VertexStates.VISITED);
        stack.push(rootVertex);
        while (!stack.isEmpty()) {
            Vertex top = stack.peek();
            Vertex adjacentVertex = findAdjacentUnvisitedVertex(top);
            if (adjacentVertex != null) {
                adjacentVertex.setSTATE(VertexStates.VISITED);
                stack.push(adjacentVertex);
                parent.put(adjacentVertex.hashCode(), top.hashCode());
            } else {
                if (isCycleDetected(top, parent)) {
                    cycleVerties.add(top);
                }
                top.setSTATE(VertexStates.DISCARDED);
                stack.pop();
            }
        }
        for (Vertex V : cycleVerties) {
            System.out.println("\nGraph Cycle detected using DFS with 3 state Impl: " + V.getLabel());
        }
    }

    private boolean isCycleDetected(Vertex top, Map<Integer, Integer> parent) {
        List<Vertex> adjacency = findAllAdjacentVertices(top);
        for (Vertex V : adjacency) {
            if (V.getSTATE() == VertexStates.VISITED) {
                if (adjacency.size() != 1 && parent.get(V.hashCode()) != top.hashCode())
                    return true;
            }
        }
        return false;
    }

}
