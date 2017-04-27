package ecda.experiments.quality;

import java.io.File;
import java.io.IOException;

import coopy.Table;
import ecda.experiments.size.SizeOps;
import ecda.utils.CSVUtils;
import ecda.utils.DaffUtils;

public class Patcher
{

	private static final String FOLDER = "./files/DATA2/";
	private static final String FOLDERDAFF = "./files/daff2/";
	private static final String OUTPUT = "./files/output2/";

	public static void main(String[] args) throws IOException
	{

		File file = new File(FOLDER);
		String[] directories = file.list((current, name) -> new File(current, name).isDirectory());
		SizeOps.loadSizes(FOLDER);

		for (String directorie : directories)
		{
			if (directorie == "2550")
			{
				break;
			}
			if (SizeOps.getAverage(directorie) < 1048576)
			{
				getPatched(directorie);
			}

		}

	}

	static void getPatched(String f) throws IOException
	{
		File folder = new File(FOLDER + f);
		File[] listOfFiles = folder.listFiles();

		File dafffolder = new File(FOLDERDAFF + f);
		File[] listOfDaffs = dafffolder.listFiles();

		new File(OUTPUT + f).mkdirs();

		Table table1 = DaffUtils.getTable(FOLDER + f + "/" + listOfFiles[0].getName(), null);

		CSVUtils.storeCSV(table1, OUTPUT + f + "/" + listOfFiles[0].getName());

		for (File daff : listOfDaffs)
		{
			String table2Name = daff.getName();

			Table table2 = DaffUtils.getTable(FOLDERDAFF + f + "/" + table2Name, ";");
			DaffUtils.patch(table1, table2);
			// Now the table1 is updated
			CSVUtils.storeCSV(table1, OUTPUT + f + "/" + table2Name.split("_")[1]);
		}

	}
}
