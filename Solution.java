import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
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
        for (int i=0; i<P; i++) {
            int Pi = in.nextInteger();

        }

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

                    break;

                case "M":
                    int idM = in.nextInteger();
                    int password = in.nextInteger();

                    break;

                case "J":
                    int idJ = in.nextInteger();

                    break;

                case "MOVE":
                    sofitaPosition = in.nextInteger();
            
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

        // TODO: Implement this method
        return 0;
    }

    static int M(int id, int password) {
        // 

        // TODO: Implement this method
        return 0;
    }

    static int J(int id) {
        // TODO: Implement this method
        return 0;
    }

    static class Graph {
        int size;
        List<List<Edge>> adjacencyList;  // arr[from] = arr[to] = panjangJalan
        int[] shortestCostSpanningTree;
        int[] energyNeededToTraverseAll;
        int[] energyMaxToNotTraverse;
        Map<Integer, Map<Integer, Integer>> mapMaxKotaVisited;  // Menyimpan output query R yang sudah dilakukan agar tidak double compute maxKotaVisited, id -> (energi -> maxKota)

        Graph(int V) {
            size = V;
            adjacencyList = new ArrayList<>();
            mapMaxKotaVisited = new HashMap<>();
            for (int i = 0; i < V+1; i++) {
                adjacencyList.add(new ArrayList<>());
                mapMaxKotaVisited.put(i, new HashMap<>());
            }
            energyNeededToTraverseAll = new int[V+1];
            energyMaxToNotTraverse = new int[V+1];
        }

        void addEdge(int from, int to, int panjang) {
            List<Edge> fromList = adjacencyList.get(from);
            fromList.add(new Edge(to, panjang));
            List<Edge> toList = adjacencyList.get(to);
            toList.add(new Edge(from, panjang));
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


        Long[] dijkstra(int from) {
            
        }

    }

    static class Edge {
        int toId;
        int panjangJalan;

        Edge(int to, int panjang) { 
            toId = to;
            panjangJalan = panjang;
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