package cs271project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class input_trans {
	public static double[][] getInput(File file) {
		Scanner scanner;
		try {
			scanner = new Scanner(file);

			int count = 0;
			int num_loc = Integer.parseInt(scanner.nextLine());
			double[][] graphmap = new double[num_loc][];
			for (int i = 0; i < num_loc; i++) {
				double[] arr_row = new double[num_loc];
				String str = scanner.nextLine();
				int c = 0;
				for (String d : str.split(" ")) {
					arr_row[c] = Double.parseDouble(d);
					c++;
				}
				graphmap[count] = arr_row;
				count++;
			}
			scanner.close();

			return graphmap;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
