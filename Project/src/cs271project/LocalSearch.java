package cs271project;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class LocalSearch {
	public static Random r = new Random();
	public static final int max_size = 5000;

	public static List<Integer> random_permutation(int N) {
		List<Integer> list = new ArrayList<Integer>();
		List<Integer> permutation = new ArrayList<Integer>();
		for (int i = 0; i < N; i++) {
			list.add(i);
		}
		while (list.size() > 0) {
			int node = list.get(r.nextInt(list.size()));
			list.remove((Object) node);
			permutation.add(node);
		}
		return permutation;
	}

	public static double calc_cost(List<Integer> path, double[][] adj_matrix) {
		double cost = 0;
		int N = adj_matrix.length;
		for (int i = 0; i < N - 1; i++) {
			cost += adj_matrix[path.get(i)][path.get(i + 1)];
		}
		cost += adj_matrix[path.get(N - 1)][path.get(0)];
		return cost;

	}

	public static void swap(List<Integer> path, int idx_node1, int idx_node2) {
		int temp = path.get(idx_node1);
		path.set(idx_node1, path.get(idx_node2));
		path.set(idx_node2, temp);
		return;
	}

	public static boolean Prob(int p) {
		return r.nextInt(100) < p;
	}

	public static List<Integer> SLS(List<Integer> path, double[][] adj_matrix) {
		int N = adj_matrix.length;
		List<Integer> best_solution = new LinkedList<Integer>(path);
		double cost = calc_cost(best_solution, adj_matrix);
		// double cost = N * 5000;
		int p = 1;
		boolean search = true;
		while (search) {
			search = false;
			for (int node1 = 0; node1 < N; node1++) {
				for (int node2 = 0; node2 < N; node2++) {
					if (node1 == node2) {
						continue;
					}
					swap(path, node1, node2);
					double new_cost = calc_cost(path, adj_matrix);
					if (new_cost < cost || Prob(p)) {
						cost = new_cost;
						best_solution = new LinkedList<Integer>(path);
						search = true;
					} else {
						swap(path, node1, node2);
					}
				}
			}
		}
		return best_solution;
	}

	/*
	 * public static void main(String args[]) { Scanner sc = new Scanner(System.in);
	 * System.out.println("Please input the filename of the graph:"); String
	 * filename = sc.nextLine(); File file_input = new File(filename); double[][]
	 * graph = input_trans.getInput(file_input); int N = graph.length; List<Integer>
	 * best_overall = null; double best_overall_cost = N * 5000; List<String>
	 * list_searched = new LinkedList<String>(); int count = 500; while (count > 0)
	 * { List<Integer> path_random = random_permutation(N); boolean not_converged =
	 * true; if (list_searched.contains(path_random.toString())) { continue; }
	 * list_searched.add(path_random.toString()); if (list_searched.size() >
	 * max_size) { list_searched.remove(0); } while (not_converged) { List<Integer>
	 * best_solution = SLS(path_random, graph); if
	 * (best_solution.toString().equals(path_random.toString())) { not_converged =
	 * false; } path_random = best_solution; double newcost =
	 * calc_cost(best_solution, graph); if (newcost < best_overall_cost) {
	 * best_overall = best_solution; best_overall.add(best_overall.get(0));
	 * best_overall_cost = newcost;
	 * 
	 * } count -= 1; } } sc.close(); System.out.println(best_overall);
	 * System.out.println(best_overall_cost); }
	 */
}
