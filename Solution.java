import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;

public class Solution {
    private static InputReader in;


    public static void main(String[] args) {

        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        // OutputStream outputStream = System.out;
        // out = new PrintWriter(outputStream);
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
                    Set<Integer> maxKotaVisited = graph.maximumKotaVisited(sofitaPosition, energi);
                    if (maxKotaVisited.isEmpty()) System.out.println(-1);
                    else System.out.println(maxKotaVisited.size());
                    // System.out.println(maxKotaVisited.toString());
                    
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
    }

    static int R(int energi) {
        // Cetak jumlah kota maksimum yang dapat dikunjungi dengan energi sebanyak [ENERGI]
        // Setiap sampai di satu kota, sofita akan mengisi ulang energi
        // TODO: Implement this method
        return 0;
    }

    static int F(int tujuan) {
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
        Map<Integer, List<Integer[]>> adjacencyList;  // fromId -> (toId, panjangJalan)

        Graph(int V) {
            size = V;
            adjacencyList = new HashMap<>();
            for (int i=1; i<=V; i++) {
                adjacencyList.put(i, new ArrayList<Integer[]>());
            }
        }

        void addEdge(int from, int to, int panjang) {
            List<Integer[]> fromList = adjacencyList.get(from);
            fromList.add(new Integer[]{to, panjang});
            List<Integer[]> toList = adjacencyList.get(to);
            toList.add(new Integer[]{from, panjang});
        }

        Set<Integer> maximumKotaVisited(int from, int energi) {
            Set<Integer> visited = new HashSet<>();
            Queue<Integer> queue = new LinkedList<>();

            // Doing breadth first search
            queue.add(from);

            while(!queue.isEmpty()) {
                int explore = queue.poll();
                
                List<Integer[]> jalanan = adjacencyList.get(explore);
                for (Integer[] jalan : jalanan) {
                    int toId = jalan[0];
                    int panjangJalan = jalan[1];
                    if (!visited.contains(toId) && energi >= panjangJalan && toId != from) {
                        visited.add(toId);
                        queue.add(toId);
                    }
                }

            }
            return visited;
        }

        Long[] dijkstra(int from) {
            return new Long[1];
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