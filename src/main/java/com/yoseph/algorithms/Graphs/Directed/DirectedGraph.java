package com.yoseph.algorithms.Graphs.Directed;

import com.yoseph.algorithms.Graphs.Vertex;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DirectedGraph {
    private Vertex[] vertices;
    private int currentVertex;

    public Vertex[] getVertices() {
        return vertices;
    }

    public void setVertices(Vertex[] vertices) {
        this.vertices = vertices;
        setCurrentVertex(vertices.length - 1);
    }

    public int getCurrentVertex() {
        return currentVertex;
    }

    public void setCurrentVertex(int currentVertex) {
        this.currentVertex = currentVertex;
    }

    public List<Vertex>[] getAdjacencyList() {
        return adjacencyList;
    }

    public void setAdjacencyList(List<Vertex>[] adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    private List<Vertex>[] adjacencyList;

    public DirectedGraph(int Max_Size) {
        this.vertices = new Vertex[Max_Size];
        this.currentVertex = -1;
        this.adjacencyList = new List[Max_Size];
        for (int i = 0; i < adjacencyList.length; i++) {
            List<Vertex> ll = new LinkedList<>();
            adjacencyList[i] = ll;
        }
    }

    public void addEdge(int source, int sink) {
        //find the sink vertex
        Vertex sinkV = this.vertices[sink];
        //then add it to the source vtx
        List<Vertex> theList = this.adjacencyList[source];
        theList.add(sinkV);
        this.adjacencyList[source] = theList;
    }

    public void addVertix(char label) {
        Vertex vertex = new Vertex(label);
        this.vertices[++currentVertex] = vertex;
    }

    /**
     * Topological Sorting using DAG - directed asyclic graph
     */
    public void topologicalSort() {
        Vertex[] sortedArray = new Vertex[vertices.length];
        List<Vertex> sortedList = new LinkedList<>();
        Vertex lastVertex = null;
        //check if there is a cycle - when no successor while the graph is still not empty
        while (!this.isEmpty()) {
            //find a vertex with no successor
            Vertex successor = successor();
            if (successor == null && currentVertex != 0) {
                System.out.println("Cycle detected !");
                break;
            }
            //delete from the graph
            deleteVertex(successor);
            //add it to the end a sorted array
            addToSortedArray(successor, sortedList);
            //adjacent the edge in the graph
            removeVertexFromGraph(successor);
            if ((findLastElementAfterTopSort()) != null) {
                lastVertex = findLastElementAfterTopSort();
            }
        }

        if (lastVertex != null)
            sortedList.add(0, lastVertex);

        for (Vertex vertex : sortedList) {
            System.out.print(vertex.getLabel() + "-->");
        }
    }

    private Vertex findLastElementAfterTopSort() {
        int totalCount = 0;
        Vertex oneRemainingVertex = null;
        for (int i = 0; i < adjacencyList.length; i++) {
            if (adjacencyList[i].size() == 1) {
                totalCount += 1;
                oneRemainingVertex = vertices[i];
            }
        }
        if (totalCount == 1) {
            return oneRemainingVertex;
        }
        return null;
    }

    private void removeVertexFromGraph(Vertex successor) {
        for (List<Vertex> adjList : adjacencyList) {
            Iterator<Vertex> iterator = adjList.iterator();
            int runner = 0;
            while (iterator.hasNext()) {
                if (iterator.next() == successor) {
                    adjList.remove(runner);
                    break;
                }
                runner++;
            }
        }
    }

    private int findVertexIndex(Vertex successor) {
        for (int k = 0; k < vertices.length; k++) {
            if (vertices[k] == successor) {
                return k;
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        for (int index = 0; index < vertices.length; index++) {
            List<Vertex> list = adjacencyList[index];
            if (list != null && list.size() != 0) {
                return false;
            }
        }
        return true;
    }
    //when the graph is emptied out then you can return the sorted array that would be topologically sorted

    private void addToSortedArray(Vertex successor, List<Vertex> sortedArray) {
        sortedArray.add(0, successor);
    }

    private void deleteVertex(Vertex successor) {
        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i] == successor) {
                while (i + 1 < vertices.length) {
                    vertices[i] = vertices[i + 1];
                    i++;
                }
                this.currentVertex -= 1;
            }
        }
    }

    public Vertex successor() {
        for (int k = 0; k <= getCurrentVertex(); k++) {
            if (adjacencyList[k].size() == 0) {
                return vertices[k];
            }
        }
        return null;
    }
}
