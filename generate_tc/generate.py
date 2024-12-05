import random

for i in range(100):
    with open(f"testcase/M/{i}.txt", "w") as f:
        # V is the number of vertices
        V = random.randint(1, 100)
        # Ensure E is at least V-1 (minimum for a connected graph) and respects the edge limits
        E = random.randint(V - 1, min(100, V * (V - 1) // 2))
        f.write(f"{V} {E}\n")

        edges = {}  # Dictionary to store unique edges with weights
        connected = set(range(1, V + 1))  # Track vertices

        # Step 1: Create a spanning tree to ensure all vertices are connected
        vertices = list(connected)
        random.shuffle(vertices)
        for i in range(1, V):
            Vi = vertices[i - 1]
            Vj = vertices[i]
            Li = random.randint(1, 100)
            edge = (min(Vi, Vj), max(Vi, Vj))  # Ensure undirected edge format
            edges[edge] = Li  # Store edge with its weight

        # Step 2: Add additional random edges
        while len(edges) < E:
            Vi = random.randint(1, V)
            Vj = random.randint(1, V)
            while Vi == Vj:
                Vj = random.randint(1, V)
            Li = random.randint(1, 100)
            edge = (min(Vi, Vj), max(Vi, Vj))  # Ensure undirected edge format
            if edge in edges:
                continue  # Skip duplicates
            edges[edge] = Li  # Add the new edge with its weight

        # Write edges to file
        for (Vi, Vj), Li in edges.items():
            f.write(f"{Vi} {Vj} {Li}\n")

        P = random.randint(1, 100)
        f.write(f"{P}\n") 

        password = []
        while len(password) < P:
            Pi = random.randint(1, 10000)
            if Pi in password:
                continue
            password.append(Pi)

        for p in password:
            f.write(f"{p}\n")

        # Generate random queries
        Q = random.randint(1, 100)
        f.write(f"{Q}\n")
        for _ in range(Q):
            j = random.randint(1, V)
            f.write(f"J {j}\n")
