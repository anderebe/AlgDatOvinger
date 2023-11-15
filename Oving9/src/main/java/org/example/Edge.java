package org.example;

public class Edge {
    Edge next;
    Node to;
    int time;
    int distance;
    int speed;


    public Edge(Edge next, Node to, int time, int distance, int speed) {
        this.next = next;
        this.to = to;
        this.time = time;
        this.distance = distance;
        this.speed = speed;

    }



}

