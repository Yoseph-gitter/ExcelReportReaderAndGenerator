//package com.yoseph.algorithms.Graphs;
//
//import com.yoseph.algorithms.Graphs.Directed.DirectedGraph;
//
//import java.util.Random;
//
//public class GraphDriver implements IGraph{
//    public static void main(String[] args) {
//        Graph graph1 = graphGenerator();
//        printGraph(graph1);
//        graph1.depthFirstSearchIteratively();
//        initializeGraph(graph1);
//        graph1.cycleDetectorDfs_1();
//
//    }
//
//    private static void initializeGraph(Graph graph) {
//        for (Vertex vertex : graph.getVertices()) {
//            vertex.setSTATE(VertexStates.UNVISITED);
//        }
//    }
//
//    private static Graph generateGraph() {
//        char[] vertices = {'A', 'B', 'C', 'D'};
//        Vertex[] vertices1 = new Vertex[vertices.length];
//
//        for (int k = 0; k < vertices.length; k++) {
//            vertices1[k] = new Vertex(vertices[k]);
//        }
//        int[][] edges = {{0, 1}, {1, 2}, {2,}, {2, 3}};
//
//        Graph graph = new Graph(edges.length);
//        graph.setVertices(vertices1);
//        for (int[] edge : edges) {
//            graph.getAdjMatrix()[edge[0]][edge[1]] = 1;
//        }
//        return graph;
//    }
//
//    private static DirectedGraph graphGenerator() {
//        char[] labels = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'};
//        int graphLength = 2 + new Random().nextInt(labels.length - 2) ;
//        DirectedGraph graph = new DirectedGraph(graphLength);
//
//        for (int j = 0; j < graphLength; j++) {
//            graph.addVertix(labels[j]);
//        }
//        //randomly generating a graph
//        int[][] adjMatrix = graph.getAdjMatrix();
//        for (int k = 0; k < graphLength; k++) {
//            for (int j = 0; j < graphLength; j++) {
//                if (k == j) {
//                    adjMatrix[j][k] = 0;
//                } else {
//                    int row = new Random().nextInt(graphLength);
//                    int col = new Random().nextInt(graphLength);
//                    if(row != col){
//                        adjMatrix[row][col] = 1;
//                        adjMatrix[col][row] = 1;
//                    }
//                }
//            }
//        }
//        return graph;
//    }
//    private static void printGraph(Graph graph ){
//        int[][] adjMatrix = graph.getAdjMatrix() ;
//        Vertex[] vertices = graph.getVertices() ;
//        System.out.print("  ") ;
//        for(Vertex V : vertices){
//            System.out.format("%s%s ---" , " ",V.getLabel());
//        }
//        System.out.println();
//        for (int k = 0; k < vertices.length ; k++) {
//            System.out.print(vertices[k].getLabel() + "  ");
//            for (int j = 0; j < vertices.length - 1; j++) {
//                System.out.format("%s --> ", adjMatrix[k][j]);
//            }
//            System.out.format("%s", adjMatrix[k][vertices.length - 1]);
//            System.out.println();
//        }
//    }
//}
//
