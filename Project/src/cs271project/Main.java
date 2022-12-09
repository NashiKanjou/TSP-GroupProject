package cs271project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static File csv;
	public static boolean isAuto = false;
	public static long time;
	public static List<String[]> data = new ArrayList<String[]>();
	public static String[] part_data;

	private static void listFilesForFolder(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				if (fileEntry.getName().endsWith(".txt") || fileEntry.getName().endsWith(".out")) {
					double[][] graph = input_trans.getInput(fileEntry);
					part_data = new String[9];
					part_data[0] = fileEntry.getName();
					System.out.println("Running DFS for: " + fileEntry.getName());
					dfs(graph);
					System.out.println("Running SLS for: " + fileEntry.getName());
					sls(graph);
					data.add(part_data);
					System.out.println("Ended");
					System.out.println();
				}
			}
		}
	}

	public static void auto_dir(String filepath) {
		// copied auto but added input to filepath I want to check.
		isAuto = true;
		data.clear();
		csv = new File("output.csv");
		if (!csv.exists()) {
			try {
				csv.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		listFilesForFolder(new File("."));
		part_data = new String[9];
		part_data[0] = "File";
		part_data[1] = "DFS Runtime (ms)";
		part_data[2] = "SLS Runtime (ms)";
		part_data[3] = "DFS Solution Quality";
		part_data[4] = "SLS Solution Quality";
		part_data[5] = "DFS Number Nodes Expanded";
		part_data[6] = "SLS Number Iterations";
		part_data[7] = "DFS Solution";
		part_data[8] = "SLS Solution";
		data.add(part_data);
		listFilesForFolder(new File(filepath));
		try {
			writeToFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void Auto() {
		isAuto = true;
		data.clear();
		csv = new File("output.csv");
		if (!csv.exists()) {
			try {
				csv.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		part_data = new String[9];
		part_data[0] = "File";
		part_data[1] = "DFS Runtime (ms)";
		part_data[2] = "SLS Runtime (ms)";
		part_data[3] = "DFS Solution Quality";
		part_data[4] = "SLS Solution Quality";
		part_data[5] = "DFS Number Nodes Expanded";
		part_data[6] = "SLS Number Iterations";
		part_data[7] = "DFS Solution";
		part_data[8] = "SLS Solution";
		data.add(part_data);
		listFilesForFolder(new File("."));
		try {
			writeToFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeToFile() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(csv));
		for (String[] raw : data) {
			String line = "";
			for (String str : raw) {
				line += str + ",";
			}
			bw.newLine();
			bw.write(line);
		}
		bw.close();
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please input the filename of the graph, or input \"auto\" to run all txt and out files:");
		String filename = sc.nextLine();
		if (filename.equals("auto_dir")) {
			auto_dir("test_data"); // This should probably be input from next line but I don't have time to try
									// currently
			sc.close();
			return;
		}
		if (filename.equals("auto")) {
			Auto();
			sc.close();
			return;
		}
		File file_input = new File(filename);
		if (!file_input.exists()) {
			System.out.println("File not found, exited");
			sc.close();
			return;
		}
		double[][] graph = new double[0][0];
		try {
			graph = input_trans.getInput(file_input);
		} catch (Exception e) {
			System.out.println("File incorrect, exited");
			sc.close();
			return;
		}

		System.out.println("Input 1 to do DFS, 2 for SLS, 3 for path verify: ");
		int i = Integer.parseInt(sc.nextLine());
		if (i == 1) {
			dfs(graph);
		} else if (i == 2) {
			sls(graph);
		} else {
			checkpath(graph);
		}
		sc.close();
	}

	public static void checkpath(double[][] graph) {
		System.out.println("Path verify..");
		Scanner sc = new Scanner(System.in);
		System.out.println("please input path or 'exit'");
		String raw = sc.next();
		List<Integer> list = new ArrayList<Integer>();
		while (!raw.equals("exit")) {
			list.clear();
			for (String str : raw.split("->")) {
				list.add(Integer.parseInt(str));
			}
			System.out.println("Cost: "+LocalSearch.calc_cost(list, graph));
			System.out.println("please input path or 'exit'");
			raw = sc.next();
		}
		sc.close();
	}

	public static void sls(double[][] graph) {
		LocalSearch.t = BigInteger.valueOf(0);
		LocalSearch.start = System.currentTimeMillis() + 600000;
		int N = graph.length;
		List<Integer> best_overall = LocalSearch.random_permutation(N);
		double best_overall_cost = LocalSearch.calc_cost(best_overall, graph);
		List<String> list_searched = new LinkedList<String>();

		int count = 500;
		while (LocalSearch.start > System.currentTimeMillis() && count > 0) {
			List<Integer> path_random = LocalSearch.random_permutation(N);
			boolean not_converged = true;
			if (list_searched.contains(path_random.toString())) {
				continue;
			}
			list_searched.add(path_random.toString());
			if (list_searched.size() > LocalSearch.max_size) {
				list_searched.remove(0);
			}
			while (not_converged) {
				List<Integer> best_solution = LocalSearch.SLS(path_random, graph);
				if (best_solution.toString().equals(path_random.toString())) {
					not_converged = false;
				}
				path_random = best_solution;
				double newcost = LocalSearch.calc_cost(best_solution, graph);
				if (newcost < best_overall_cost) {
					best_overall = best_solution;
					best_overall.add(best_overall.get(0));
					best_overall_cost = newcost;

				}
				count -= 1;
			}
		}

		long end = System.currentTimeMillis();
		if (isAuto) {
			part_data[2] = "" + (end - (LocalSearch.start - 600000));
			part_data[4] = "" + (best_overall_cost);
			part_data[6] = "" + (LocalSearch.t.toString());
			String path = "";
			for (int i = 0; i < best_overall.size() - 1; i++) {
				path += best_overall.get(i) + "->";
			}
			path += best_overall.get(best_overall.size() - 1);
			part_data[8] = "" + (path);
			return;
		}
		System.out.println(best_overall);
		System.out.println(best_overall_cost);
	}

	public static void dfs(double[][] graph) {
		heuristic.init(graph);

		if (heuristic.len_Graph < 50) {
			DFS_H.hx_steps = 4;
		} else if (heuristic.len_Graph <= 100) {
			DFS_H.hx_steps = 3;
		} else {
			DFS_H.hx_steps = 2;
		}

		time = System.currentTimeMillis() + 600000;
		DFS_H.upper_bound = Double.MAX_VALUE;
		DFS_H.total_t = BigInteger.valueOf(0);
		int size = (int) (heuristic.len_Graph / 0.75 + 1);
		DFS_H.p = new ArrayList<Integer>(size);
		DFS_H.temp_p = new ArrayList<Integer>(size);

		int Start_node = 0;
		List<Integer> v_visited = new ArrayList<Integer>(size);

		long time_msec = System.currentTimeMillis();

		DFS_H.DFS(Start_node, graph, Start_node, 0, v_visited);

		long time_msec_end = System.currentTimeMillis();

		DFS_H.p.add(Start_node);

		if (isAuto) {
			part_data[1] = "" + (time_msec_end - time_msec);
			part_data[3] = "" + (DFS_H.upper_bound);
			part_data[5] = "" + (DFS_H.total_t.toString());
			String path = "";
			for (int i = 0; i < DFS_H.p.size() - 1; i++) {
				path += DFS_H.p.get(i) + "->";
			}
			path += DFS_H.p.get(DFS_H.p.size() - 1);
			part_data[7] = "" + (path);
			return;
		}

		System.out.println("path: " + DFS_H.p);
		/* verfiy */
		/*
		 * double distance_verify = 0; for (int i = 0; i < p.size() - 1; i++) {
		 * distance_verify += graph[p.get(i)][p.get(i + 1)]; }
		 * System.out.println("path_verify:" + distance_verify);
		 */
		System.out.println("shortest path cost: " + DFS_H.upper_bound);
		System.out.println("cost of search: " + DFS_H.total_t.toString());

		// long t = 1;
		// for (int i = 1; i < heuristic.len_Graph; i++) {
		// t *= i;
		// }

		// System.out.println("cost of search without heuristic/prune: " + t);
		// System.out.println("dfs ns time: " + time_dfs);
		// System.out.println("hx ns time: " + time_hx);
		System.out.println("total ms time: " + (time_msec_end - time_msec));
	}
}
