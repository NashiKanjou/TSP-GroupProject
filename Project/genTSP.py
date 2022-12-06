import argparse
import numpy as np
import random
import copy


###########################################################
#  genTSP.py
#
# Purpose:
#   generate random Traveling Sales Person problems
#
# Parameters:
#   -n  --  number of locations
#   -k  --  number of distinct distance values to use
#   -u  --  mean of normal distribution for distances
#   -v  --  variance (standard deviation) of normal distribution for distances
#   -p  --  number of problem instances to generate
#
# Dependencies:
#   (1) Python 2.7+ & pip
#   (2) pip install argparse
#   (3) pip install numpy
#
# Output: [p] files
#   each file has name:
#       tsp-problem-{n}-{k}-{u}-{v}-{i}.txt
#         (where {n}, {k}, {u}, {v} are the corresponding parameters,
#          and {i} is the index (1~p) of the problem instance)
#   each file follows format:
#       - first row  -> {n}
#       - all other (n) rows compose the adjacency matrix for a complete graph,
#           row i are writen with distances from location i to all other locations,
#           distances are delimited by whitespace.
#
# Author:
#   Qiushi Bai (qbai1@uci.edu)
#
# Version:
#   Nov-29-2020
#
###########################################################


def gen_tsp(n, k, u, v, p):
    print("generating " + str(p) + " problems with parameters: N=" + str(n) +
          ", K=" + str(k) + ", U=" + str(u) + ", V=" + str(v) + "...")
    filename_prefix = "tsp-problem-" + str(n) + "-" + str(k) + "-" + str(u) + "-" + str(v) + "-"
    # loop problem instances
    for i in range(1, p + 1):
        filename = filename_prefix + str(i) + ".txt"
        outfile = open(filename, "w")
        outfile.write(str(n) + "\n")
        # generate k distinct distance values following N(u,v) normal distribution
        distinct_dists = np.random.normal(loc=u, scale=v, size=k).tolist()
        # enforce positive distance values
        # of course, it might violate the distinct and normal distribution intention,
        # but for now, it is good enough for our purpose of controlling the hardness
        # of generated problems
        distinct_dists = [abs(x) for x in distinct_dists]
        # all possible edges in the complete graph matrix
        all_edges = []
        for x in range(0, n):
            for y in range(x + 1, n):
                edge = (x, y)
                all_edges.append(edge)
        # randomly pick k edges from all_edges for those distinct distance values
        distinct_edges = random.sample(all_edges, k)

        # init the graph matrix
        graph = np.zeros(shape=(n, n))

        # loop edges to assign distance values
        distinct_dists_disposable = copy.deepcopy(distinct_dists)
        for x in range(0, n):
            for y in range(x + 1, n):
                # this edge should have a distinct distance
                if (x, y) in distinct_edges:
                    graph[x, y] = distinct_dists_disposable.pop(0)
                    graph[y, x] = graph[x, y]
                # this edge random pick one distance from the k distinct distances
                else:
                    graph[x, y] = random.choice(distinct_dists)
                    graph[y, x] = graph[x, y]

        # write the graph matrix to outfile
        for x in range(0, n):
            outfile.write(" ".join(str(x) for x in graph[x, :].tolist()) + "\n")
        outfile.close()
    print("generation is done.")


if __name__ == "__main__":

    # parse parameters
    parser = argparse.ArgumentParser(description="Generate random Traveling Sales Person problems.")
    parser.add_argument("-n", "--n", help="N: number of locations", required=True, type=int)
    parser.add_argument("-k", "--k", help="K: number of distinct distance values to use", required=True, type=int)
    parser.add_argument("-u", "--u", help="U: mean of normal distribution for distances", required=True, type=int)
    parser.add_argument("-v", "--v", help="V: variance of normal distribution for distances", required=True, type=int)
    parser.add_argument("-p", "--p", help="P: number of problem instances to generate (default: 1)", required=False, type=int, default=1)
    args = parser.parse_args()

    n = args.n
    k = args.k
    u = args.u
    v = args.v
    p = args.p

    if n < 1:
        print("[Error parameters] N can NOT be < 1. Exit.")
        exit(0)

    if k < 1:
        print("[Error parameters] K can NOT be < 1. Exit.")
        exit(0)

    if u <= 0:
        print("[Error parameters] U can NOT be <= 0. Exit.")
        exit(0)

    if v < 0:
        print("[Error parameters] V can NOT be < 0. Exit.")
        exit(0)

    if p < 1:
        print("[Error parameters] P can NOT be < 1. Exit.")
        exit(0)

    if n*(n-1)/2 < k:
        print("[Warning] K > C(2,N)(Choose 2 from N), which means all distance values will be distinct.")

    gen_tsp(n, k, u, v, p)

