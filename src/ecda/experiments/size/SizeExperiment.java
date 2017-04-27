package ecda.experiments.size;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import ecda.utils.CSVUtils;

public class SizeExperiment
{

	public static void main(String[] args) throws FileNotFoundException
	{
		String csvFile = "./files/correct.csv";

		Scanner scanner = new Scanner(new File(csvFile));
		scanner.nextLine();
		int i = 0, j = 0;

		while (scanner.hasNext())
		{
			List<String> line = CSVUtils.parseLine(scanner.nextLine());
			long size = Long.parseLong(line.get(1));
			long sizeDaff = Long.parseLong(line.get(2));
			long difference = size - sizeDaff;
			System.out.println("Folder [name= " + line.get(0) + ", size= " + line.get(1) + " , sizeDaff=" + line.get(2) + ",diff " + difference + "]");

			if (difference > 0)
			{
				i++;
			}
			else
			{
				j++;
			}
		}
		System.out.println("Smaller daff: " + i + " Bigger: " + j);
		scanner.close();
	}

}
