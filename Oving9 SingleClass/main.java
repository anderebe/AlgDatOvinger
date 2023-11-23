import java.io.*;
import java.util.*;

//** Created by Anders Emil Bergan **//
public class main{
    public static void main(String[] args){

        Graph g = new Graph();

        try{

            System.out.print("Checking files...");
            BufferedReader nReader = new BufferedReader(new FileReader("norden/noder.txt"));
            BufferedReader eReader = new BufferedReader(new FileReader("norden/kanter.txt"));
            BufferedReader iReader = new BufferedReader(new FileReader("norden/interessepkt.txt"));
            System.out.println("Done!");

            System.out.print("Reading nodes...");
            g.readNodes(nReader);
            System.out.println("Done!");

            System.out.print("Reading edges...");
            g.readEdges(eReader);
            System.out.println("Done!");

            System.out.print("Reading interest points...");
            g.readIP(iReader);
            System.out.println("Done!\n");

            Node start = g.node[3168086];
            Node end = g.node[6441311];

            Node orkanger = g.node[2266026];
            Node trondheimCamping = g.node[3005466];
            Node hotellOstersund = g.node[3240367];

            Node trondheim = g.node[7826348];
            Node selbustrand = g.node[5009309];
            Node greenstarHotelLahti = g.node[999080];

            int ladestasjon = 4;
            int spisested = 8;
            int drikkested = 16;
            int numberOfPoints = 5;

            //Norden
            String[] landmarks = {"Nordkapp", "Kristiansand", "Krakow", "Bremen", "Joensuu"};

            long startTime;
            long endTime;

            String mapFileName = "PreProcessedMap2.txt";
            if(!new File(mapFileName).exists()) {
                System.out.println("Preprocessing of map not done!\n");
                System.out.println("Starting preprocessing of map...");
                g.createPreProcessMap(landmarks, mapFileName);
            }
            System.out.print("Reading preprocessed map...");
            g.readPreProcessMap(mapFileName);
            System.out.println("Done!\n");



            System.out.print("Starting dijkstra...");
            startTime = System.currentTimeMillis();
            g.dijkstra(start, end);
            endTime = System.currentTimeMillis();
            System.out.println("Done!");

            System.out.println("Time spent on dijkstra: "+(endTime-startTime) + " ms");
            System.out.println("Nodes on the road: " + g.shortestPathDijkstra.size());
            System.out.println("Total nodes visited: " + g.visitedNodesDijkstra.size());
            System.out.println("Time used from start to end: "+ formatTime(((Prev)g.node[end.id].d).dist/100) + "\n");

            System.out.print("Starting ALT...");
            startTime = System.currentTimeMillis();
            g.ALT(start,end);
            endTime = System.currentTimeMillis();
            System.out.println("Done!");

            System.out.println("Time spent on alt algorithm: "+(endTime-startTime) + "ms");
            System.out.println("Nodes on the road: " + g.shortestPathAlt.size());
            System.out.println("Total nodes visited: " + g.visitedNodesAlt.size());
            System.out.println("Time used from start to end: "+formatTime(g.node[end.id].d.dist/100) + "\n");



            Node[] ladestasjoner = g.dijkstra_IP(orkanger,ladestasjon,numberOfPoints);
            Node[] drikkesteder = g.dijkstra_IP(trondheimCamping,drikkested,numberOfPoints);
            Node[] spisesteder = g.dijkstra_IP(hotellOstersund,spisested,numberOfPoints);

            System.out.println("Charging stations the closest to Orkanger:");
            Arrays.stream(ladestasjoner).forEach(s -> System.out.println(" - " + s.name + ": " + formatTime(s.interestTime)));
            System.out.print("\n\nDrinking places the closest to Trondheim Camping: ");
            Arrays.stream(drikkesteder).forEach(s -> System.out.println(" - " + s.name + ": " + formatTime(s.interestTime)));
            System.out.print("\n\nEating places the closest to Hotell Östersund: ");
            Arrays.stream(spisesteder).forEach(s -> System.out.println(" - " + s.name + ": " + formatTime(s.interestTime)));

        }  catch (IOException e){
            e.printStackTrace();
        }

    }
    private static String formatTime(int i) {
        int hours = i / 3600;
        int remainder = i % 3600;
        int minutes = remainder / 60;
        int seconds = remainder % 60;

        return hours + " hours " + minutes + " minutes " + seconds + " seconds";
    }
    static class Node{
        int id;
        Double longitude;
        Double latitude;
        Edge edge1;


        Prev d;
        int classification;
        String name;
        int interestTime;

        public Node(int id){
            this.id = id;
        }

        public Node(int id, Double latitude, Double longitude){
            this.id = id;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        public String toString(){return "Node: " + id + ", " + latitude + ", " + longitude;

        }

    }
    static class Edge{
        Node to;
        Edge next;

        public Edge(Node n, Edge next){
            this.to = n;
            this.next = next;
        }
    }
    static class Edge_W extends Edge{
        int weight;
        int distance;

        public Edge_W(Node n, Edge next, int weight, int distance){
            super(n, next);
            this.weight = weight;
            this.distance = distance;
        }
    }
    static class Prev{
        int dist;
        int estimate;
        Node prev;
        static final int inf = 1000000000;

        public Prev(){
            dist = inf;
            estimate = 0;
        }

        public int getDist(){
            return this.dist + this.estimate;
        }
    }
    static class Graph{
        int N, E, I;
        Node[] node;
        Node[] transposed;
        boolean[] visited;
        boolean[] found;
        PriorityQueue<Node> pq;
        HashMap<String, Node> interestPoints;
        List<Node> visitedNodesDijkstra;
        List<Node> shortestPathDijkstra;

        int[] landmarks;
        int[][] fromLandmark;
        int[][] toLandmark;
        List<Node> visitedNodesAlt;
        List<Node> shortestPathAlt;


        public Graph(){
            visitedNodesDijkstra = new ArrayList<>();
            shortestPathDijkstra = new ArrayList<>();
            visitedNodesAlt = new ArrayList<>();
            shortestPathAlt = new ArrayList<>();
        }

        void readNodes(BufferedReader n) throws IOException{
            StringTokenizer st = new StringTokenizer(n.readLine());
            N = Integer.parseInt(st.nextToken());
            node = new Node[N];
            transposed = new Node[N];
            for (int i = 0; i < N; i++){
                st = new StringTokenizer(n.readLine());
                int id = Integer.parseInt(st.nextToken());
                double latitude = Double.parseDouble(st.nextToken());
                double longitude = Double.parseDouble(st.nextToken());
                node[i] = new Node(id, latitude, longitude);
                transposed[i] = new Node(id, latitude, longitude);
            }
        }
        void readEdges(BufferedReader e) throws IOException{
            StringTokenizer st = new StringTokenizer(e.readLine());
            E = Integer.parseInt(st.nextToken());
            for (int i = 0; i < E; i++){
                st = new StringTokenizer(e.readLine());
                int from = Integer.parseInt(st.nextToken());
                int to = Integer.parseInt(st.nextToken());
                int weight = Integer.parseInt(st.nextToken());
                int distance = Integer.parseInt(st.nextToken());
                Edge_W w = new Edge_W(node[to], node[from].edge1, weight, distance);
                Edge_W w2 = new Edge_W(transposed[from], transposed[to].edge1, weight, distance);
                node[from].edge1 = w;
                transposed[to].edge1 = w2;
            }
        }
        void readIP(BufferedReader ip) throws IOException{
            interestPoints = new HashMap<>();
            StringTokenizer st = new StringTokenizer(ip.readLine());
            I = Integer.parseInt(st.nextToken());
            for (int i = 0; i < I; i++){
                st = new StringTokenizer(ip.readLine());
                int id = Integer.parseInt(st.nextToken());
                int classification = Integer.parseInt(st.nextToken());
                StringBuilder name = new StringBuilder(st.nextToken());
                while (st.hasMoreTokens()){
                    name.append(" ").append(st.nextToken());
                }
                name = new StringBuilder(name.toString().replaceAll(String.valueOf('"'), ""));
                node[id].classification = classification;
                node[id].name = name.toString();
                interestPoints.put(name.toString(), node[id]);
            }
        }

        public void dijkstra(Node start, Node end) {
            visited = new boolean[N];
            found = new boolean[N];
            visitedNodesDijkstra.clear();
            shortestPathDijkstra.clear();

            initPrev(start);
            pq = makePrio(start);
            found[start.id] = true;

            while(!visited[end.id]){
                Node n = pq.poll();
                assert n != null;
                visited[n.id] = true;
                visitedNodesDijkstra.add(n);

                for(Edge_W w = (Edge_W)n.edge1; w != null; w=(Edge_W)w.next){
                    shorten(n, w);
                }
            }

            Node n = end;
            while(n != null){
                shortestPathDijkstra.add(n);
                n = ((Prev)n.d).prev;
            }
            Collections.reverse(shortestPathDijkstra);
        }
        public void dijkstra(Node start){
            visited = new boolean[N];
            found = new boolean[N];
            initPrev(start);
            pq = makePrio(start);
            found[start.id] = true;
            while(!pq.isEmpty()){
                Node n = pq.poll();
                visited[n.id]=true;
                for(Edge_W w = (Edge_W)n.edge1; w!= null; w=(Edge_W) w.next){
                    shorten(n,w);
                }
            }
        }
        public void dijkstra_T(Node start){
            visited = new boolean[N];
            found = new boolean[N];
            initPrev_T(start);
            pq = makePrio(start);
            while(!pq.isEmpty()){
                Node n = pq.poll();
                visited[n.id]=true;
                for(Edge_W w = (Edge_W)n.edge1; w!= null; w=(Edge_W) w.next){
                    shorten(n,w);
                }
            }
        }
        public Node[] dijkstra_IP(Node s, int type, int numberOfPoints) {
            Node[] interestPoints = new Node[numberOfPoints + 1];
            interestPoints[0] = s;
            int counter = 1;
            visited = new boolean[N];
            found = new boolean[N];
            initPrev(s);
            pq = makePrio(s);
            found[s.id] = true;

            while (counter < interestPoints.length) {
                Node n = pq.poll();
                assert n != null;

                // Check if the node is an interest point of the specified type
                if (n != s && (n.classification & type) == type) {
                    interestPoints[counter++] = n;

                    // Calculate and store the time to reach the interest point
                    interestPoints[counter-1].interestTime = ((Prev) n.d).dist/100;;
                }

                for (Edge_W w = (Edge_W) n.edge1; w != null; w = (Edge_W) w.next) {
                    shorten(n, w);
                }
            }
            return interestPoints;
        }
        private void shorten(Node n, Edge_W w) {
            if(visited[w.to.id]) return;

            Prev nd = (Prev)n.d;
            Prev mtd = (Prev)w.to.d;

            if(!found[w.to.id]){
                pq.add(w.to);
                found[w.to.id] = true;
            }

            if(mtd.dist>nd.dist+w.weight){
                mtd.dist = nd.dist + w.weight;
                mtd.prev = n;
                pq.remove(w.to);
                pq.add(w.to);
            }
        }
        private PriorityQueue<Node> makePrio(Node start) {
            PriorityQueue<Node> pq = new PriorityQueue<>(N, Comparator.comparingInt(a -> ((Prev) a.d).getDist()));
            pq.add(start);
            return pq;
        }
        private void initPrev(Node start) {
            for(int i = N; i-->0;){
                node[i].d= new Prev();
            }
            ((Prev)start.d).dist = 0;
        }
        private void initPrev_T(Node start) {
            for(int i = N; i-->0;){
                transposed[i].d= new Prev();
            }
            ((Prev)start.d).dist = 0;
        }

        public void ALT(Node start, Node end) {
            visited = new boolean[N];
            found = new boolean[N];
            visitedNodesAlt.clear();
            shortestPathAlt.clear();
            initPrev(start);
            pq = makePrio(start);
            while (!visited[end.id]){
                Node n = pq.poll();
                assert n != null;
                visited[n.id] = true;
                visitedNodesAlt.add(n);

                for(Edge_W w = (Edge_W)n.edge1; w != null; w=(Edge_W)w.next){
                    shorten_Alt(n, end, w);
                }
            }
            Node n = end;
            while (n != null) {
                shortestPathAlt.add(n);
                n = ((Prev) n.d).prev;
            }
            Collections.reverse(shortestPathAlt);
        }

        private void shorten_Alt(Node n, Node e, Edge_W w) {
            if(visited[w.to.id]) return;
            Prev nd = (Prev)n.d;
            Prev wtd = (Prev)w.to.d;

            if(!found[w.to.id]){
                calculateEstimate(w.to, e);
                pq.add(w.to);
                found[w.to.id] = true;
            }

            if(wtd.dist>nd.dist+w.weight){
                wtd.dist = nd.dist + w.weight;
                wtd.prev = n;
                pq.remove(w.to);
                pq.add(w.to);
            }
        }

        private void calculateEstimate(Node w, Node end) {
            int largestEstimate = 0;
            int previous = -1;
            for (int i = 0; i < landmarks.length; i++){
                int estimateFromLandmark = fromLandmark[i][end.id] + toLandmark[i][w.id];
                int estimateToLandmark = toLandmark[i][w.id] + fromLandmark[i][end.id];
                largestEstimate = Math.max(estimateToLandmark, estimateFromLandmark);
                if (previous > largestEstimate) largestEstimate = previous;
                previous = largestEstimate;
            }
            if(largestEstimate > 0)((Prev)w.d).estimate = largestEstimate;
        }

        public void createPreProcessMap(String[] landmarks, String mapFileName) throws IOException {
            System.out.print(" - Dijkstra...");
            int[][] dijkstraLengths = new int[landmarks.length][N];
            for (int i = 0; i < landmarks.length; i++) {
                dijkstra(findInterestPoints(landmarks[i]));
                for (int j = 0; j < N; j++) {
                    dijkstraLengths[i][j] = node[j].d.dist;
                }
            }
            System.out.println("Done!");

            System.out.print(" - Dijkstra transposed...");
            int[][] dijkstraLengthsTransposed = new int[landmarks.length][N];
            for (int i = 0; i < landmarks.length; i++) {
                dijkstra_T(findTransposedInterestPoint(landmarks[i]));
                for (int j = 0; j < N; j++) {
                    dijkstraLengthsTransposed[i][j] = ((Prev)transposed[j].d).dist;
                }
            }
            System.out.println("Done!");

            System.out.print(" - Writing out landmarks...");
            FileWriter fw = new FileWriter(mapFileName);
            for (int i = 0; i < landmarks.length; i++) {
                fw.write(String.valueOf(findInterestPoints(landmarks[i]).id));
                if(i+1<landmarks.length) fw.write(" ");
            }
            fw.write("\n");
            System.out.println("Done!");

            System.out.print(" - Writing out dijkstra...");
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < landmarks.length; j++) {
                    fw.write(String.valueOf(dijkstraLengths[j][i]));
                    if(j+1<landmarks.length) fw.write(" ");
                }
                fw.write("\n");
            }
            System.out.println("Done!");

            System.out.print(" - Writing out dijkstra transposed...");
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < landmarks.length; j++) {
                    fw.write(String.valueOf(dijkstraLengthsTransposed[j][i]));
                    if(j+1<landmarks.length) fw.write(" ");
                }
                if(i+1<N) fw.write("\n");
            }
            System.out.println("Done!");
            fw.close();
        }
        public void readPreProcessMap(String mapFileName) throws IOException {
            BufferedReader br = new BufferedReader(new FileReader(mapFileName));
            StringTokenizer str = new StringTokenizer(br.readLine());
            final int size = str.countTokens();
            this.landmarks = new int[size];
            this.fromLandmark = new int[size][N];
            this.toLandmark = new int[size][N];
            for (int i = 0; i < landmarks.length; i++){
                this.landmarks[i] = Integer.parseInt(str.nextToken());
            }
            for (int i = 0; i < N; i++) {
                str = new StringTokenizer(br.readLine());
                for (int j = 0; j < size; j++) {
                    fromLandmark[j][i] = Integer.parseInt(str.nextToken());
                }
            }
            for (int i = 0; i < N; i++) {
                str = new StringTokenizer(br.readLine());
                for (int j = 0; j < size; j++) {
                    toLandmark[j][i] = Integer.parseInt(str.nextToken());
                }
            }
        }
        public Node findInterestPoints(String s){
            return interestPoints.get(s);
        }
        public Node findTransposedInterestPoint(String s){
            return transposed[interestPoints.get(s).id];
        }
    }
}