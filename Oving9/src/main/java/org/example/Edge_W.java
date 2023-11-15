package org.example;

public class Edge_W extends Edge{
    int weight;
    public Edge_W(Edge next, Node to, int time, int distance, int speed) {
            super(next, to, time, distance, speed);
            this.weight = distance;
    }
}
