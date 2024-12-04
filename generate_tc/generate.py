import random

with open("1.txt", "w") as f:

    V = random.randint(2, 1000)
    E = random.randint(V, min(10000, V*(V-1)/2))
    f.write(f"{V} {E}\n")


    for i in range(E):
        Vi = random.randint(1, V)
        Vj = random.randint(1, V)
        Li = random.randint(1, 100)
        while Vi == Vj:
            Vj = random.randint(1, V)

        f.write(f"{Vi} {Vj} {Li}\n")

    f.write(f"0\n")


    Q = random.randint(1, 100)
    f.write(f"{Q}\n")

    for _ in range(Q):
        j = random.randint(1, V)
        f.write(f"J {j}\n")

