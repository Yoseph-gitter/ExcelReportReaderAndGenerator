package com.yoseph.algorithms.Graphs;

public enum VertexStates {
    UNVISITED(-1),
    VISITED(0),
    DISCARDED(1);

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    private int value;

    VertexStates(int value) {
        this.value = value;
    }
}
