package org.example;

import java.util.Arrays;

public class Graph {

    int N, E;
    Node[] nodes;


    public void set_nodes(Node[] nodes){
        this.nodes = nodes;
    }
    public void set_N(int N){
        this.N = N;
    }
    public void set_E(int E){
        this.E = E;
    }
    public Node[] get_nodes(){
        return nodes;
    }

    public void initPredecessors(Node source) {
        for (int i = 0; i < N; i++) {
            nodes[i].prev = new Previous();
        }
        source.prev.distance = 0;
    }

    int over(int i){return (i - 1) >> 1;}
    int left(int i){return (i << 1) + 1;}
    int right(int i){return (i + 1) << 1;}


    public void set_in(Node node){
        Node[] copy = new Node[N + 1];
        System.arraycopy(this.nodes, 0, copy, 0, N);
        copy[N] = node;
        this.N = copy.length;
        this.nodes = copy;
    }

    public Graph prio_opp(Graph graph, int i, int x){
        int f;
        graph.nodes[i].prev.distance += x;
        while(i > 0 && graph.nodes[i].prev.distance > graph.nodes[f=over(i)].prev.distance){
            Node t = graph.nodes[i];
            graph.nodes[i] = graph.nodes[f];
            graph.nodes[f] = t;
            i = f;
        }
        return graph;
    }


    public void djikstra(Node s){
        initPredecessors(s);
        Node []pri = new Node[N];
        make_pri(pri);
        for(int i = N; i > 1; --i){
            Node n = get_min(i, pri);
            Edge_W k;
            for (k = (Edge_W) n.edge1; k != null; k = (Edge_W) k.next) {shorten(n, k);}
        }
    }
    public void make_pri(Node[] pri){
        if (N >= 0) System.arraycopy(nodes, 0, pri, 0, N);
    }
    public void shorten(Node n, Edge_W k){
        Previous nd = (Previous) n.prev, md = (Previous) k.to.prev;
        if(md.distance > nd.distance + k.weight){
            md.distance = nd.distance + k.weight;
            md.previous = n;
        }
    }
    public Node get_min(int x, Node[] pri) {
        int min = 0;

        for (int i = 1; i < x; i++) {
            if (pri[i].prev.distance < pri[min].prev.distance) {
                min = i;
            }
        }
        Node minNode = pri[min];

        // Remove the minimum node from the priority list
        for (int i = min; i < x - 1; i++) {
            pri[i] = pri[i + 1];
        }

        return minNode;
    }
}
