package ecda.experiments.type;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import ecda.model.Folder;
import ecda.utils.CSVUtils;

public class MachineLearningExtractor
{
	private static final String FOLDERDAFF = "./files/daff2/";

	public static void main(String[] args) throws IOException
	{
		String csvFile = "./files/correctFinal.csv";
		String outputFile = "./files/correct3.csv";
		Scanner scanner = new Scanner(new File(csvFile));

		scanner.nextLine();
		FileWriter writer = new FileWriter(outputFile, false);
		CSVUtils.writeLine(writer, Arrays.asList("folder", "sizeOriginal", "sizeDaff", "evolution", "patch", "quality", "numberOfVersions", "originalRows", "originalColumns", "addedRows", "deletedRows", "modifiedRows", "addedColumns", "deletedColumns", "modifiedColumns", "totalChanged", "changedCells"));
		while (scanner.hasNext())
		{
			List<String> line = CSVUtils.parseLine(scanner.nextLine(), ';');
			Folder folder = new Folder(line.get(0));
			CSVUtils.writeLine(writer, folder.getLine(line.get(3), line.get(4), line.get(5)));
		}
		writer.flush();
		writer.close();
		scanner.close();
	}

	private static String getHeaderChange(String f) throws FileNotFoundException
	{
		File folder = new File(FOLDERDAFF + f);
		File[] listOfFiles = folder.listFiles();
		boolean check = true;
		System.out.println("----------------------");
		System.out.println(folder.getName());
		Scanner scanner;
		for (File listOfFile : listOfFiles)
		{
			scanner = new Scanner(listOfFile);
			List<String> line = CSVUtils.parseLine(scanner.nextLine(), ';');
			System.out.println(listOfFile.getName() + " " + line.get(0));
			if (!line.get(0).equals("@@"))
			{
				check = false;
				break;
			}
			scanner.close();

		}
		if (check)
		{
			return "1";
		}
		else
		{
			return "0";
		}
	}

}
