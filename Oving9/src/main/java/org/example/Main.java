package org.example;

public class Main {

    static String nodeFileName = "src/main/resources/noder.txt";
    static String edgeFileName = "src/main/resources/kanter.txt";
    static FileHandler handler = new FileHandler();

    public static void main(String[] args){
        Graph graph = handler.readFile(nodeFileName, edgeFileName);
    }
}