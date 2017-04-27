package ecda.experiments.quality;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import coopy.Table;
import ecda.daff.EvolutionChecker;
import ecda.experiments.size.SizeOps;
import ecda.model.Folder;
import ecda.utils.CSVUtils;
import ecda.utils.DaffUtils;

public class QualityChecker
{

	private static final String FOLDER = "./files/DATA2/";
	private static final String FOLDERDAFF = "./files/daff2/";
	private static final String OUTPUT = "./files/output2/";
	private static final String FOLDERDAFF2 = "./files/daffOutput2/";

	public static void main(String[] args) throws IOException
	{

		File file = new File(FOLDER);
		String[] directories = file.list((current, name) -> new File(current, name).isDirectory());
		File filedaff = new File(FOLDERDAFF);
		String[] directoriesdaff = filedaff.list((current, name) -> new File(current, name).isDirectory());
		SizeOps.loadSizes(FOLDER);
		ArrayList<Folder> list = new ArrayList<Folder>();
		String tempoFile = "./files/tempo.csv";

		FileWriter tempowriter = new FileWriter(tempoFile, true);
		CSVUtils.writeLine(tempowriter, Arrays.asList("name", "evolution", "patch", "quality"));

		for (String directorie : directories)
		{

			if (directorie.equals("1950") || Arrays.asList(directoriesdaff).contains(directorie))
			{
				continue;
			}

			if (SizeOps.getAverage(directorie) < 1048576)
			{

				long actual = System.currentTimeMillis();
				EvolutionChecker.getEvolution(directorie);
				long postEvolution = System.currentTimeMillis();
				Patcher.getPatched(directorie);
				long postPatcher = System.currentTimeMillis();
				getQuality(directorie);
				long postQuality = System.currentTimeMillis();
				// System.out.println(directorie);
				list.add(new Folder(directorie));
				CSVUtils.writeLine(tempowriter, Arrays.asList(directorie, (postEvolution - actual) + "", (postPatcher - postEvolution) + "", (postQuality - postPatcher) + ""));
			}
		}
		int i = 57;
		String csvFile = "./files/correct.csv";
		FileWriter writer = new FileWriter(csvFile, true);

		// CSVUtils.writeLine(writer, Arrays.asList("folder", "sizeOriginal",
		// "sizeDaff"));
		for (Object element : list)
		{
			Folder folder = (Folder) element;
			System.out.println(folder.toString());
			if (folder.isCorrect())
			{
				CSVUtils.writeLine(writer, Arrays.asList(folder.getName(), folder.getSizeOriginal() + "", folder.getSizeDaff() + ""));
				i++;
			}
		}

		tempowriter.flush();
		tempowriter.close();

		writer.flush();
		writer.close();
		System.out.println("-------------------");
		System.out.println(list.size() + " : " + i);

	}

	private static void getQuality(String f) throws IOException
	{

		File folder = new File(FOLDER + f);
		File[] listOfFiles = folder.listFiles();
		new File(FOLDERDAFF2 + f).mkdirs();

		for (int i = 0; i < listOfFiles.length - 1; i++)
		{

			File file1 = listOfFiles[i];
			Table table1 = DaffUtils.getTable(FOLDER + f + "/" + file1.getName(), null);
			Table table2 = DaffUtils.getTable(OUTPUT + f + "/" + file1.getName(), null);

			Table table3 = DaffUtils.getComparison(table1, table2, false);
			CSVUtils.storeCSV(table3, FOLDERDAFF2 + f + "/" + file1.getName());
		}
	}

}
