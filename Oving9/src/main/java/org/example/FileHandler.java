package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class FileHandler {
    public Graph readFile(String node_filename, String edge_filename) throws IOException {
        Graph graph = new Graph();

        BufferedReader brNodes = new BufferedReader(new FileReader(node_filename));
        BufferedReader brEdges = new BufferedReader(new FileReader(edge_filename));
        readNodes(brNodes, graph);
        readEdges(brEdges, graph);

        return graph;
    }

    private void readNodes(BufferedReader brNodes, Graph graph) throws IOException{
        StringTokenizer st = new StringTokenizer(brNodes.readLine());

        //Read the total amount of nodes and create an empty array with that size
        graph.N = Integer.parseInt(st.nextToken());
        graph.nodes = new Node[graph.N];

        //initialize the nodes
        for(int i = 0; i < graph.N; ++i){

            //Gets the line to be read
            st = new StringTokenizer(brNodes.readLine());

            //Gathers the node variables on the line
            int id = Integer.parseInt(st.nextToken());
            double laptitude = Double.parseDouble(st.nextToken());
            double longitude = Double.parseDouble(st.nextToken());

            //Creates the new node with the variables
            graph.nodes[i] = new Node(id, laptitude, longitude);

        }

        //Initalize the first predecessor
        graph.initPredecessors(graph.nodes[0]);
    }

    private void readEdges(BufferedReader brEdges, Graph graph) throws IOException{
        StringTokenizer st = new StringTokenizer(brEdges.readLine());

        //Read the total amount of edges
        graph.E = Integer.parseInt(st.nextToken());

        //initialize the edges and adds them to the nodes
        for(int i = 0; i < graph.E; ++i){
            st = new StringTokenizer(brEdges.readLine());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());

            int time = Integer.parseInt(st.nextToken());
            int distance = Integer.parseInt(st.nextToken());
            int speed = Integer.parseInt(st.nextToken());
            graph.nodes[from].edge1 = new Edge_W(graph.nodes[from].edge1, graph.nodes[to], time, distance, speed);
        }
    }
}
