package ecda.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import coopy.Csv;
import coopy.JavaTableView;
import coopy.Table;

public class FileChecker
{

	private static final String FOLDER = "./files/zamg/";
	// private static final String FOLDER = "./files/daff/";

	public static void main(String[] args) throws IOException
	{

		File folder = new File(FOLDER);
		File[] listOfFiles = folder.listFiles();
		ArrayList<ArrayList<String>> totalList = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < listOfFiles.length - 1; i++)
		{
			File file1 = listOfFiles[i];
			File file2 = listOfFiles[i + 1];
			Table table1 = getTable(FOLDER + file1.getName());
			Table table2 = getTable(FOLDER + file2.getName());

			Table table3 = getComparison(table1, table2);
			// System.out.println(Size of table 1);

			storeCSV(table3, "./files/daff/" + file1.getName() + "_" + file2.getName());

			ArrayList<String> headers = new ArrayList<String>();

			for (int j = 0; j < table3.get_width(); j++)
			{

				if (table3.getCell(0, 0) == "!")
				{

				}
				headers.add((String) table3.getCell(0, j));

			}
			totalList.add(headers);
		}

		for (int i = 0; i < totalList.size(); i++)
		{
			ArrayList<String> arrayList = totalList.get(i);
			if (arrayList.get(0) == "!")
			{
				for (String string : arrayList)
				{

				}

				// /System.out.println("Change of header in " + listOfFiles[i]);
			}

		}

	}

	private static void storeCSV(Table table3, String string) throws IOException
	{
		Csv csv = new Csv(";", null);
		String output = csv.renderTable(table3);
		PrintWriter pr = new PrintWriter(new FileWriter(new File(string), false));
		// String arr[] = output.split("\\n");
		pr.print(output);
		pr.close();
	}

	public static Table getComparison(Table table1, Table table2)
	{
		coopy.Alignment alignment = coopy.Coopy.compareTables(table1, table2, null).align();
		// System.out.println(table1.get_height() + " " + alignment.count());
		JavaTableView table_diff = new JavaTableView();
		coopy.CompareFlags flags = new coopy.CompareFlags();
		flags.show_unchanged = true;
		flags.show_unchanged_columns = true;

		coopy.TableDiff highlighter = new coopy.TableDiff(alignment, flags);
		highlighter.hilite(table_diff);
		return table_diff;
	}

	public static Table getTable(String string) throws IOException
	{
		// TODO Auto-generated method stub

		Csv csv = new Csv(";", null);

		BufferedReader reader = new BufferedReader(new FileReader(string));

		// read file line by line
		String line = null;
		String output = "";
		while ((line = reader.readLine()) != null)
		{
			// System.out.println(line);
			output = output + line + "\n";
		}

		// close reader
		reader.close();
		return csv.makeTable(output);

	}

}
