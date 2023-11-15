package org.example;

public class Node {
    int id;
    double laptitude, longitude;
    Edge edge1;
    Previous prev;
    public Node(int id, double laptitude, double longitude) {
        this.id = id;
        this.laptitude = laptitude;
        this.longitude = longitude;
    }
}
