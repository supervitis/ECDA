package ecda.daff;

import java.io.File;
import java.io.IOException;

import coopy.Table;
import ecda.experiments.size.SizeOps;
import ecda.utils.CSVUtils;
import ecda.utils.DaffUtils;

public class EvolutionChecker
{

	private static final String FOLDER = "./files/DATA2/";
	private static final String FOLDERDAFF = "./files/daff2/";

	// private static final String FOLDER = "./files/daff/";

	public static void main(String[] args) throws IOException
	{

		File file = new File(FOLDER);
		String[] directories = file.list((current, name) -> new File(current, name).isDirectory());
		SizeOps.loadSizes(FOLDER);

		for (String directorie : directories)
		{

			// 2550

			if (SizeOps.getAverage(directorie) < 1048576)
			{
				getEvolution(directorie);
			}

		}

	}

	public static void getEvolution(String f) throws IOException
	{
		File folder = new File(FOLDER + f);
		File[] listOfFiles = folder.listFiles();
		// ArrayList<ArrayList<String>> totalList = new
		// ArrayList<ArrayList<String>>();
		new File(FOLDERDAFF + f).mkdirs();

		for (int i = 0; i < listOfFiles.length - 1; i++)
		{

			File file1 = listOfFiles[i];
			File file2 = listOfFiles[i + 1];
			Table table1 = DaffUtils.getTable(FOLDER + f + "/" + file1.getName(), null);
			Table table2 = DaffUtils.getTable(FOLDER + f + "/" + file2.getName(), null);

			Table table3 = DaffUtils.getComparison(table1, table2, false);
			// System.out.println(Size of table 1);

			CSVUtils.storeCSV(table3, FOLDERDAFF + f + "/" + file1.getName() + "_" + file2.getName());

			// ArrayList<String> headers = new ArrayList<String>();
			//
			// for (int j = 0; j < table3.get_width(); j++)
			// {
			//
			// if (table3.getCell(0, 0) == "!")
			// {
			//
			// }
			// headers.add((String) table3.getCell(0, j));
			//
			// }
			// totalList.add(headers);
		}

		// for (int i = 0; i < totalList.size(); i++)
		// {
		// ArrayList<String> arrayList = totalList.get(i);
		// if (arrayList.get(0) == "!")
		// {
		// for (String string : arrayList)
		// {
		//
		// }
		//
		// // /System.out.println("Change of header in " + listOfFiles[i]);
		// }
		//
		// }
	}

}
