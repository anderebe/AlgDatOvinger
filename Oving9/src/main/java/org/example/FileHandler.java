package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class FileHandler {
    public Graph readFile(String node_filename, String edge_filename){
        Graph graph = new Graph();

        try {
            System.out.printf("Reading '%s'...", node_filename);
            BufferedReader brNodes = new BufferedReader(new FileReader(node_filename));
            System.out.println("Done!");

            System.out.printf("Reading '%s'...", edge_filename);
            BufferedReader brEdges = new BufferedReader(new FileReader(edge_filename));
            System.out.println("Done!");

            System.out.print("Reading nodes...");
            readNodes(brNodes, graph);

            System.out.print("Reading edges...");
            readEdges(brEdges, graph);


        } catch (IOException | NumberFormatException e) {
            // Handle exceptions by printing "Failed!" and the exception details
            System.out.println("Failed! \nException: " + e.getMessage());
        }

        return graph;
    }

    private void readNodes(BufferedReader brNodes, Graph graph){
        try{
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
                double latitude = Double.parseDouble(st.nextToken());
                double longitude = Double.parseDouble(st.nextToken());

                //Creates the new node with the variables
                graph.nodes[i] = new Node(id, latitude, longitude);

            }

            //Initialize the first predecessor
            graph.initPredecessors(graph.nodes[0]);

            System.out.println("Done!");

        } catch (IOException | NumberFormatException| NullPointerException e) {
            // Handle exceptions by printing "Failed!" and the exception details
            System.out.println("Failed! \nException: " + e.getMessage());
        }
    }

    private void readEdges(BufferedReader brEdges, Graph graph){
        try{
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

            System.out.println("Done!");

        } catch (IOException | NumberFormatException | NullPointerException e) {
            // Handle exceptions by printing "Failed!" and the exception details
            System.out.println("Failed! \nException: " + e.getMessage());
        }
    }
}
