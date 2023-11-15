package org.example;

public class Node {
    int id;
    double latitude, longitude;
    Edge edge1;
    Previous prev;
    public Node(int id, double laptitude, double longitude) {
        this.id = id;
        this.latitude = laptitude;
        this.longitude = longitude;
    }
}
