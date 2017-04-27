package ecda.experiments.size;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import ecda.utils.CSVUtils;

public class ZipChecker
{
	private static final String OUTPUT_ZIP = "./files/zipDaff/";

	public static void main(String[] args) throws IOException
	{
		String csvFile = "./files/correct3.csv";
		Scanner scanner = new Scanner(new File(csvFile));
		SizeOps.loadURLS();
		scanner.nextLine();
		// BabelNet bn = BabelNet.getInstance();
		String outputFile = "./files/zipOutputDaff.csv";
		FileWriter writer = new FileWriter(outputFile, false);
		CSVUtils.writeLine(writer, Arrays.asList("folder", "size", "versions"));
		while (scanner.hasNext())
		{
			List<String> line = CSVUtils.parseLine(scanner.nextLine(), ',');
			File file = new File(OUTPUT_ZIP + line.get(0) + ".zip");
			CSVUtils.writeLine(writer, Arrays.asList(line.get(0), file.length() + "", line.get(6)));

		}
		writer.flush();
		writer.close();
		scanner.close();
	}

}
