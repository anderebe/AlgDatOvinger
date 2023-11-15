package org.example;

public class Graph {

    int N, E;
    Node[] nodes;

    int over(int i){return (i - 1) >> 1;}
    int left(int i){return (i << 1) + 1;}
    int right(int i){return (i + 1) << 1;}


    public void initPredecessors(Node source) {
        for (int i = 0; i < N; i++) {
            nodes[i].prev = new Previous();
        }
        source.prev.distance = 0;
    }

}
