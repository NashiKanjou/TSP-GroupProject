package cs271project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DFS_H {

	public static double upper_bound = Double.MAX_VALUE;
	// public static long time_dfs = 0;
	// public static long time_hx = 0;
	public static long total_t = 0;
	public static List<Integer> p = new ArrayList<Integer>();
	public static List<Integer> temp_p = new ArrayList<Integer>();
	public static int hx_steps = 4;

	public static void DFS(int startnode, double[][] adj_matrix, int node, double path, List<Integer> visited) {
		/* timing */
		// long t_msec = System.nanoTime();
		total_t++;
		// System.out.println(total_t + ": " + visited.size());
		if (Main.time < System.currentTimeMillis()) {
			return;
		}
		temp_p.add(node);
		visited.add(node);

		/* visited last node,return the path if better */
		if (visited.size() == heuristic.len_Graph) {
			double new_distance = path + adj_matrix[node][startnode];
			if (new_distance <= upper_bound) {
				upper_bound = new_distance;
				p.clear();
				for (int i : temp_p) {
					p.add(i);
				}
			}
			temp_p.remove((Object) node);
			visited.remove((Object) node);
			return;
		}

		// long te_msec = System.nanoTime();
		// time_dfs += te_msec - t_msec;
		// t_msec = System.nanoTime();

		double[] hx = heuristic.fun_heuristic(upper_bound - path, adj_matrix, node, visited, hx_steps);

		// te_msec = System.nanoTime();
		// time_hx += te_msec - t_msec;
		// t_msec = System.nanoTime();

		List<Double> hxc = new ArrayList<Double>(heuristic.len_Graph);
		for (double d : hx) {
			/* break condition */
			if (d <= 0 || path + d >= upper_bound) {
				continue;
			}
			hxc.add(d);
		}

		if (hxc.size() == 0) {
			temp_p.remove((Object) node);
			visited.remove((Object) node);
			return;
		}

		Collections.sort(hxc);

		List<Integer> sorted_need = new ArrayList<Integer>(heuristic.len_Graph);
		List<Integer> skip = new ArrayList<Integer>(heuristic.len_Graph);
		int l = hxc.size();
		int count = 0;
		while (count < l) {
			double d = hxc.get(count);
			for (int i = 0; i < heuristic.len_Graph; i++) {
				if (hx[i] == d) {
					if (skip.contains(i)) {
						continue;
					}
					skip.add(i);
					sorted_need.add(i);
					count += 1;
					break;
				}
			}
		}
		// te_msec = System.nanoTime();
		// time_dfs += te_msec - t_msec;
		for (int i : sorted_need) {
			DFS(startnode, adj_matrix, i, path + adj_matrix[node][i], visited);
		}
		temp_p.remove((Object) node);
		visited.remove((Object) node);
		return;
	}
}