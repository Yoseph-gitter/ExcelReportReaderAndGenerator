package com.yoseph.algorithms.Graphs;

public class Vertex {
    private char label;

    public char getLabel() {
        return label;
    }

    public void setLabel(char label) {
        this.label = label;
    }

    public VertexStates getSTATE() {
        return STATE;
    }

    public void setSTATE(VertexStates STATE) {
        this.STATE = STATE;
    }

    private VertexStates STATE;

    public Vertex(char label) {
        this.label = label;
        this.STATE = VertexStates.UNVISITED;
    }
}
