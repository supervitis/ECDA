package ecda.experiments.distance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import coopy.Table;
import ecda.experiments.size.SizeOps;
import ecda.model.Cell;
import ecda.model.Key;
import ecda.utils.CSVUtils;
import ecda.utils.DaffUtils;
import ecda.utils.TypeUtils;

public class PatterncheckerIndividual
{

	private static final String FOLDER = "./files/output2/";
	// private static final String FOLDERDISTANCE = "./files/distance/516/";

	public static void main(String[] args) throws IOException
	{

		String outputFile = "./files/distanceOutputCells2.csv";

		FileWriter writer = new FileWriter(outputFile, false);
		CSVUtils.writeLine(writer, Arrays.asList("folder", "column", "row", "versions", "simpleJaccard", "completeJaccard", "average", "dataType", "lehvenshtein", "changes"));

		String csvFile = "./files/static.csv";
		Scanner scanner = new Scanner(new File(csvFile));

		SizeOps.loadURLS();
		scanner.nextLine();
		while (scanner.hasNext())
		{

			List<String> line = CSVUtils.parseLine(scanner.nextLine(), ';');
			addCells(line.get(0), writer);
		}
		scanner.close();
		writer.close();

	}

	private static void addCells(String file, FileWriter writer) throws IOException
	{
		System.out.println(FOLDER + file);
		File folder = new File(FOLDER + file);
		File[] listOfFiles = folder.listFiles();
		Map<Key, Cell> map = new HashMap<Key, Cell>();
		Table table1 = DaffUtils.getTable(FOLDER + file + "/" + listOfFiles[0].getName(), null);

		addFirstVersion(table1, map);

		// System.out.println(map.keySet().toString());

		// System.out.println(table1.toString());
		// System.out.println(TypeChecker.getFolderTypes(FOLDER));
		for (int i = 0; i < listOfFiles.length - 1; i++)
		{
			File file2 = listOfFiles[i + 1];
			Table table2 = DaffUtils.getTable(FOLDER + file + "/" + file2.getName(), null);
			Table table3 = DaffUtils.getComparison(table1, table2, true);
			// System.out.println(table3.toString());

			addVersion(table3, map);
			// System.out.println(Size of table 1);

			// storeCSV(table3, "./files/type/" + file1.getName() + "_" +
			// file2.getName());

		}

		// for (int y = 1; y < table.get_height(); y++)
		// {
		// String output = "";
		//
		// for (int x = 1; x < table.get_width(); x++)
		// {
		// // output = output + map.get(new Key(x, y)).getValues().get(0) +
		// // "," + map.get(new Key(x, y)).getJaccard() + "," + map.get(new
		// // Key(x, y)).getValues().size() + "|";
		// output = output + map.get(new Key(x, y)).getJaccardsimple() + ",";
		// }
		// System.out.println(output);
		// }

		// System.out.println("Inicio" + " " + file);

		for (int x = 1; x < table1.get_width() + 1; x++)
		{
			for (int y = 1; y < table1.get_height(); y++)
			{
				CSVUtils.writeLine(writer, getCell(x, y, table1, map, file));
				writer.flush();

			}
			System.out.println("columna");
		}

		// for (int y = 1; y < table.get_height(); y++)
		// {
		// serialize(map, FOLDERDISTANCE + "serial.ser");
		// }
	}

	private static List<String> getCell(int x, int y, Table table1, Map<Key, Cell> map, String file)
	{
		// TODO Auto-generated method stub
		// System.out.println("Columna " + x + "F");
		// TODO Auto-generated method stub
		double simpleJaccard = 0;
		double completeJaccard = 0;
		long versions = 0;
		double size = 0;
		double lehvenshtein = 0;
		int changes = 0;
		String type = "";

		Cell cell = map.get(new Key(x, y));

		simpleJaccard = cell.getJaccardsimple();
		completeJaccard = cell.getJaccard();
		versions = cell.getValues().size();
		size = cell.getAverageSize();
		type = cell.getType();
		lehvenshtein = cell.getLehvenshtein();
		changes = cell.getChanges();

		return Arrays.asList(file, x + "", y + "", versions + "", simpleJaccard + "", completeJaccard + "", size + "", type + "", lehvenshtein + "", changes + "");
	}

	private static void addColumns(String file, FileWriter writer) throws IOException
	{
		System.out.println(FOLDER + file);
		File folder = new File(FOLDER + file);
		File[] listOfFiles = folder.listFiles();
		Map<Key, Cell> map = new HashMap<Key, Cell>();
		Table table1 = DaffUtils.getTable(FOLDER + file + "/" + listOfFiles[0].getName(), null);

		addFirstVersion(table1, map);

		System.out.println(map.keySet().toString());

		// System.out.println(table1.toString());
		// System.out.println(TypeChecker.getFolderTypes(FOLDER));
		for (int i = 0; i < listOfFiles.length - 1; i++)
		{
			File file2 = listOfFiles[i + 1];
			Table table2 = DaffUtils.getTable(FOLDER + file + "/" + file2.getName(), null);
			Table table3 = DaffUtils.getComparison(table1, table2, true);
			// System.out.println(table3.toString());

			addVersion(table3, map);
			// System.out.println(Size of table 1);

			// storeCSV(table3, "./files/type/" + file1.getName() + "_" +
			// file2.getName());

		}

		// for (int y = 1; y < table.get_height(); y++)
		// {
		// String output = "";
		//
		// for (int x = 1; x < table.get_width(); x++)
		// {
		// // output = output + map.get(new Key(x, y)).getValues().get(0) +
		// // "," + map.get(new Key(x, y)).getJaccard() + "," + map.get(new
		// // Key(x, y)).getValues().size() + "|";
		// output = output + map.get(new Key(x, y)).getJaccardsimple() + ",";
		// }
		// System.out.println(output);
		// }

		System.out.println("Inicio" + " " + table1.get_width());

		for (int x = 1; x < table1.get_width() + 1; x++)
		{

			CSVUtils.writeLine(writer, getColumn(x, table1, map, file));
			writer.flush();

		}

		// for (int y = 1; y < table.get_height(); y++)
		// {
		// serialize(map, FOLDERDISTANCE + "serial.ser");
		// }
	}

	private static List<String> getColumn(int x, Table table, Map<Key, Cell> map, String file)
	{

		System.out.println("Columna " + x);
		// TODO Auto-generated method stub
		double simpleJaccard = 0;
		double completeJaccard = 0;
		double lehvenshtein = 0;
		long versions = 0;
		long rows = 0;
		double size = 0;
		String type = "";

		ArrayList<String> types = new ArrayList<String>();

		for (int y = 1; y < table.get_height(); y++)

		{

			Cell cell = map.get(new Key(x, y));

			simpleJaccard = simpleJaccard + cell.getJaccardsimple();
			completeJaccard = completeJaccard + cell.getJaccard();
			versions = cell.getValues().size();
			size = size + cell.getAverageSize();
			types.add(cell.getType());
		}

		rows = table.get_height() - 1;
		simpleJaccard = simpleJaccard / rows;
		completeJaccard = completeJaccard / rows;
		size = size / rows;
		type = TypeUtils.getType(types);

		// CONSEGUIR DE ALGUNA MANERA EL TIPO Y RESTO DE DATOS

		return Arrays.asList(file, x + "", versions + "", rows + "", simpleJaccard + "", completeJaccard + "", size + "", type + "", +lehvenshtein + "");
	}

	private static void serialize(Object object, String file)
	{
		try
		{
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(object);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in " + file);
		}
		catch (IOException i)
		{
			i.printStackTrace();
		}
	}

	private static void addVersion(Table table, Map<Key, Cell> map)
	{
		for (int y = 1; y < table.get_height(); y++)
		{

			for (int x = 1; x < table.get_width(); x++)
			{
				Key key = new Key(x, y);

				String content;
				String value = "";

				try
				{
					content = table.getCell(x, y).toString();
					if (content.contains("->"))
					{
						value = content.split("->")[1];
					}
					else
					{
						value = content;
					}
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					ArrayList<String> values = map.get(key).getValues();
					if (values.size() == 0)
					{
						value = "";
					}
					else
					{
						value = values.get(values.size() - 1);

					}
				}

				map.get(key).addValue(value);

			}

		}
	}

	private static void addFirstVersion(Table table, Map<Key, Cell> map)
	{
		System.out.println("w: " + table.get_width());
		for (int y = 1; y < table.get_height() + 1; y++)
		{

			for (int x = 1; x < table.get_width() + 1; x++)
			{

				Key key = new Key(x, y);
				map.put(key, new Cell());

			}
		}

	}

}
