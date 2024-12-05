import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Solution {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {

        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);
        int sofitaPosition = 1;

        int V = in.nextInteger();
        int E = in.nextInteger();

        Graph graph = new Graph(V);

        for (int i=0; i<E; i++) {
            int Vi = in.nextInteger();
            int Vj = in.nextInteger();
            int Li = in.nextInteger();
            graph.addEdge(Vi, Vj, Li);
        }

        int P = in.nextInteger();
        int[] guesses = new int[P];
        for (int i=0; i<P; i++) {
            int Pi = in.nextInteger();
            guesses[i] = Pi;
        }
        PasswordGraph pwGraph = new PasswordGraph(guesses);

        int Q = in.nextInteger();
        for (int i=0; i<Q; i++) {
            String Qi = in.next();

            switch (Qi) {
                case "R":
                    int energi = in.nextInteger();

                    out.println(R(graph, sofitaPosition, energi));
                    break;

                case "F":
                    int tujuan = in.nextInteger();
                    out.println(F(graph, sofitaPosition, tujuan));

                    break;

                case "M":
                    int idM = in.nextInteger();
                    int password = in.nextInteger();

                    sofitaPosition = idM;
                    out.println(M(pwGraph, password));

                    break;

                case "J":
                    int idJ = in.nextInteger();
                    out.println(J(graph, idJ));

                    break;

                case "MOVE":
                    sofitaPosition = in.nextInteger();
                    break;
            
                default:
                    break;
            }
        }
        out.close();
    }

    static int R(Graph graph, int sofitaPosition, int energi) {
        // Cetak jumlah kota maksimum yang dapat dikunjungi dengan energi sebanyak [ENERGI]
        // Setiap sampai di satu kota, sofita akan mengisi ulang energi

        if (energi >= 100) return graph.size - 1;

        Integer energyMinToTraverse = graph.energyNeededToTraverseAll[sofitaPosition];
        if (energyMinToTraverse != null && energyMinToTraverse != 0 && energi >= energyMinToTraverse) {
            // System.out.println("Get min energy ");
            // System.out.println("Energy min: " + energyMinToTraverse);
            return graph.size - 1;
        }

        Integer energyMaxToNotTraverse = graph.energyMaxToNotTraverse[sofitaPosition];
        if (energyMaxToNotTraverse != null && energyMaxToNotTraverse != 0 && energi <= energyMaxToNotTraverse) return -1;

        Integer getPrevOutput = graph.mapMaxKotaVisited.get(sofitaPosition).get(energi);
        if (getPrevOutput != null) {
            // System.out.println("Getprev");
            return getPrevOutput;
        }

        int res = graph.computeMaxKotaVisited(sofitaPosition, energi);
        // System.out.println("Res");
        return res > 0 ? res : -1;
    }

    static int F(Graph graph, int sofitaPosition, int tujuan) {
        // Cetak jarak terpendek ke kota dengan id [TUJUAN]

        int res = graph.getShortest(sofitaPosition, tujuan);
        return res;
    }

    static int M(PasswordGraph pwGraph, int password) {
        if (pwGraph.currentPassword == password) return 0;
        return pwGraph.getSpt(password);
    }

    static int J(Graph graph, int id) {
        return graph.getMinSpanTree(id);
    }

    static class Graph {
        int size;
        List<List<Edge>> adjacencyList;  // arr[from] = arr[to] = panjangJalan
        int[] minCostSpanningTreeArr;
        int[] energyNeededToTraverseAll;
        int[] energyMaxToNotTraverse;
        Map<Integer, Map<Integer, Integer>> mapMaxKotaVisited;  // Menyimpan output query R yang sudah dilakukan agar tidak double compute maxKotaVisited, id -> (energi -> maxKota)
        int[][] shortestArr;
        int password;

        Graph(int V) {
            size = V;
            adjacencyList = new ArrayList<>();
            mapMaxKotaVisited = new HashMap<>();
            shortestArr = new int[V+1][V+1];
            for (int i = 0; i < V+1; i++) {
                adjacencyList.add(new ArrayList<>());
                mapMaxKotaVisited.put(i, new HashMap<>());
                shortestArr[i] = null;
            }
            energyNeededToTraverseAll = new int[V+1];
            energyMaxToNotTraverse = new int[V+1];
            minCostSpanningTreeArr = new int[V+1];
            password = 0;
        }

        void addEdge(int from, int to, int panjang) {
            List<Edge> fromList = adjacencyList.get(from);
            fromList.add(new Edge(from, to, panjang));
            List<Edge> toList = adjacencyList.get(to);
            toList.add(new Edge(to, from, panjang));
        }

        int computeMaxKotaVisited(int from, int energi) {
            Queue<Integer> queue = new LinkedList<>();
            boolean[] visitedKota = new boolean[size+1];

            int visited = 0;
            visitedKota[from] = true;

            // Doing breadth first search
            queue.add(from);

            while(!queue.isEmpty()) {
                int explore = queue.poll();
                
                List<Edge> jalanan = adjacencyList.get(explore);
                for (Edge jalan : jalanan) {
                    int toId = jalan.toId;
                    int panjangJalan = jalan.panjangJalan;
                    if (!visitedKota[toId] && energi >= panjangJalan && toId != from) {
                        visitedKota[toId] = true;
                        queue.offer(toId);
                        visited++;
                    }
                }
            }

            if (visited == (size-1)) energyNeededToTraverseAll[from] = energi;
            else if (visited == 0) energyMaxToNotTraverse[from] = energi;

            mapMaxKotaVisited.get(from).put(energi, visited);
            
            return visited;
        }

        void printPQ(PriorityQueue<Edge> pq) {
            for (Edge jalan : pq) {
                jalan.print();
            }
        }


        int[] dijkstra(int from) {
            PriorityQueue<Edge> pq = new PriorityQueue<>((a,b) -> Integer.compare(a.panjangJalan, b.panjangJalan)); 
            int[] shortest = new int[size+1];
            boolean[] visited = new boolean[size+1];

            Arrays.fill(shortest, Integer.MAX_VALUE);
            
            visited[from] = true;
            shortest[from] = 0;

            for (Edge jalan : adjacencyList.get(from)) {
                pq.offer(jalan);
                shortest[jalan.toId] = jalan.panjangJalan;
            }

            // for (Edge jalan : pq) {
            //     System.out.print("Edge: " + jalan.fromId + " to : " + jalan.toId + " (" + jalan.panjangJalan + ")");
            //     System.out.println();
            // }

            while (!pq.isEmpty()) {
                // System.out.println("Belum kosong");
                Edge jalan = pq.poll();
                // System.out.print("Exploring: ");
                // jalan.print();

                // System.out.println("======================");
                // System.out.println("======================");
                // System.out.println("Sebelum explore: ");
                // for (Edge j : pq) {
                //     System.out.print("Edge: " + j.fromId + " to : " + j.toId + " (" + j.panjangJalan + ")");
                //     System.out.println();
                // }
                // System.out.println("======================");
                // System.out.println("======================");

                int fromId = jalan.fromId;
                int toId = jalan.toId;

                visited[fromId] = true;

                // System.out.println("======================");
                // System.out.println("Visited: " + Arrays.toString(visited));
                // System.out.println("Shortest: " + Arrays.toString(shortest));
                // System.out.println("======================");

                for (Edge e : adjacencyList.get(toId)) {
                    int totalPanjangJalan = shortest[e.fromId] + e.panjangJalan;
                    // System.out.println("======================");
                    // System.out.print("See edge: ");
                    // e.print();

                    // System.out.println("Total Panjang Jalan: " + shortest[e.fromId] + "(shortest[fromId]) + " + e.panjangJalan + "(e.panjangJalan)");
                    // System.out.println("Total Panjang Jalan: " + totalPanjangJalan);

                    // System.out.println("======================");
                    if (!visited[e.toId] && totalPanjangJalan < shortest[e.toId]) {
                        shortest[e.toId] = totalPanjangJalan;
                        pq.offer(new Edge(e.fromId, e.toId, totalPanjangJalan));
                    }
                }

                // System.out.println("======================");
                // System.out.println("======================");

                // System.out.println("Setelah explore: ");
                // for (Edge j : pq) {
                //     System.out.print("Edge: " + j.fromId + " to : " + j.toId + " (" + j.panjangJalan + ")");
                //     System.out.println();
                // }
                // System.out.println("======================");
                // System.out.println("======================");


            }

            // System.out.println("From: " + from + ". Array: " + Arrays.toString(shortest));

            shortestArr[from] = shortest;
            return shortest;
        }

        int getShortest(int from, int to) {
            if (shortestArr[from] == null) {
                return dijkstra(from)[to];
            }
            return shortestArr[from][to];
        }

        int minSpanTree(int from) {
            PriorityQueue<Edge> pq = new PriorityQueue<>((a,b) -> Integer.compare(a.panjangJalan, b.panjangJalan));
            boolean[] connected = new boolean[size+1];

            int[] minCostArr = new int[size+1];
            int minCost = 0;
            connected[from] = true;

            for (Edge e1 : adjacencyList.get(from)) {
                for (Edge e2 : adjacencyList.get(e1.toId)) {
                    if (!connected[e2.toId]) pq.offer(e2);
                }
                // if (!connected[e1.toId]) {
                minCost += e1.panjangJalan;
                connected[e1.toId] = true;
                minCostArr[e1.toId] = e1.panjangJalan;
                // } else {
                //     if (e1.panjangJalan < minCostArr[e1.toId]) {
                //         minCost -= minCostArr[e1.toId];
                //         minCostArr[e1.toId] = e1.panjangJalan;
                //         minCost += e1.panjangJalan;
                //     }
                // }
            }   

            while (!pq.isEmpty()) {
                Edge jalan = pq.poll();
                int explore = jalan.toId;
                int panjangJalan = jalan.panjangJalan;

                if (connected[explore]) continue;

                // Connect edge ke spanning tree
                connected[explore] = true;
                minCost += panjangJalan;

                for (Edge e : adjacencyList.get(explore)) {
                    if (!connected[e.toId]) pq.offer(e);
                }
            }
            minCostSpanningTreeArr[from] = minCost;

            return minCost;
        }

        int getMinSpanTree(int from) {
            if (minCostSpanningTreeArr[from] == 0) {
                return minSpanTree(from);
            }
            return minCostSpanningTreeArr[from];
        }

    }

    static class Edge {
        int fromId;
        int toId;
        int panjangJalan;

        Edge(int from, int to, int panjang) { 
            fromId = from;
            toId = to;
            panjangJalan = panjang;
        }

        void print() {
            System.out.println("Edge: " + fromId + " to " + toId + "(" + panjangJalan + ")");
        }
    }


    static class PasswordGraph {
        List<List<PasswordEdge>> adjacencyList;
        boolean[] connected;
        int currentPassword;
        int[][] shortestArr;
        int[] guesses;

        PasswordGraph(int[] g) {
            adjacencyList = new ArrayList<>();
            shortestArr = new int[10000][10000];
            for (int i = 0; i <= 9999; i++) {
                adjacencyList.add(new ArrayList<>());
                shortestArr[i] = null; 
            }
            connected = new boolean[10000];
            Arrays.fill(connected, false);
            currentPassword = 0; 
            guesses = g;
        }

        void addEdge(int from, int to) {
            List<PasswordEdge> fromList = adjacencyList.get(from);
            fromList.add(new PasswordEdge(from, to, 1));
        }

        int operate(int a, int b) {
            int d0 = ((a / 1000) + (b / 1000)) % 10;
            int d1 = ((a / 100) + (b / 100)) % 10;
            int d2 = ((a / 10) + (b / 10)) % 10;
            int d3 = (a + b) % 10;
            int res = (d0 * 1000) + (d1 * 100) + (d2 * 10) + d3;
            return res;
        }

        void operateEdges(int source) {
            if (connected[source]) return;

            // System.out.println("Operating edges...");
            // System.out.println("===========================");
            // System.out.println("===========================");


            for (int guess : guesses) {
                int result = operate(source, guess);

                // System.out.println("===========================");
                // System.out.println("Guess = " + guess + ", source =  " + source);
                // System.out.println("result = " + result);
                // System.out.println("===========================");

                addEdge(source, result);
            }
            connected[source] = true;
        }

        void printPQ(PriorityQueue<PasswordEdge> pq) {
            System.out.println("PQ=PQ=PQ=PQ=PQ=PQ=PQ=PQ");
            for (PasswordEdge e : pq) {
                e.print();
            }
            System.out.println("PQ=PQ=PQ=PQ=PQ=PQ=PQ=PQ");
        }

        void build() {
            if (!connected[currentPassword]) operateEdges(currentPassword); 

            PriorityQueue<PasswordEdge> pq = new PriorityQueue<>((a,b) -> Integer.compare(a.weight, b.weight));
            boolean[] visited = new boolean[10000];
            int[] shortestPath = new int[10000];
            Arrays.fill(shortestPath, Integer.MAX_VALUE);

            visited[currentPassword] = true;
            shortestPath[currentPassword] = 0;

            for (PasswordEdge edge : adjacencyList.get(currentPassword)) {
                shortestPath[edge.toId] = edge.weight;
                pq.offer(edge);
            }



            while (!pq.isEmpty()) {
                PasswordEdge edge = pq.poll();
                int toId = edge.toId;
                int fromId = edge.fromId;
                
                // System.out.println("Sebelum explore: ");
                // printPQ(pq);

                visited[fromId] = true;
                if (!connected[toId]) operateEdges(toId);

                for (PasswordEdge e : adjacencyList.get(toId)) {
                    int totalWeight = shortestPath[e.fromId] + e.weight;
                    if (!visited[e.toId] && totalWeight < shortestPath[e.toId]) {
                        shortestPath[e.toId] = totalWeight;
                        pq.offer(new PasswordEdge(e.fromId, e.toId, totalWeight));
                    }
                }
                // System.out.println("Setelah explore: ");
                // printPQ(pq);
            }
            shortestArr[currentPassword] = shortestPath;

        }

        int getSpt(int search) {
            int get;

            if (shortestArr[currentPassword] == null) {
                build();
            }
            get = shortestArr[currentPassword][search];
            
            if (get == Integer.MAX_VALUE) return -1;
            currentPassword = search;
            return get;
        }
    }

    static class PasswordEdge {
        int fromId;
        int toId;
        int weight;
        
        PasswordEdge (int from, int to, int w) {
            fromId = from;
            toId = to;
            weight = w;
        }

        void print() {
            System.out.println("Edge = " + fromId + " to " + toId + "(" + weight + ")");
        }
    }


    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInteger() {
            return Integer.parseInt(next());
        }
    }

}