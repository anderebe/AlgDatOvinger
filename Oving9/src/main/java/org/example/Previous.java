package org.example;

public class Previous {
    int distance;

    Node previous;

    static final int INFINITY = 1000000000;

    public int getDistance() {
        return distance;
    }

    public Node getPrevious() {
        return previous;
    }

    public Previous(){
        this.distance = INFINITY;
    }
}
