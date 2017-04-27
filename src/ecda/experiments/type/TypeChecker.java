package ecda.experiments.type;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.wanasit.chrono.Chrono;

public class TypeChecker
{

	// private static final String FOLDER = "./files/DATA2/516/";

	public static ArrayList<String> getFolderTypes(String f) throws IOException
	{

		ArrayList<ArrayList<String>> listFileTypes = new ArrayList<ArrayList<String>>();

		File folder = new File(f);
		File[] listOfFiles = folder.listFiles();
		ArrayList<ArrayList<String>> totalList = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < listOfFiles.length - 1; i++)
		{
			File file = listOfFiles[i];
			listFileTypes.add(getTypes(f + file.getName()));
		}
		// System.out.println(listFileTypes.toString());
		ArrayList<String> evolution = new ArrayList<String>();
		for (int i = 0; i < listFileTypes.get(8).size(); i++)
		{
			String previous = "";
			String output = "";
			for (int j = 8; j < listFileTypes.size(); j++)
			{
				// System.out.println();
				String type;
				try
				{
					type = listFileTypes.get(j).get(i);
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					continue;
				}
				if (type != previous)
				{
					// output = output + "/" + type;
					output = output + type;
					previous = type;
				}
				// System.out.println(listFileTypes.get(j));
			}
			evolution.add(output);
		}
		// System.out.println(evolution.toString());
		// System.out.println(getTypes("./files/zamg/zamg_20160612220027.csv").toString());
		return evolution;

	}

	private static ArrayList<String> getTypes(String string) throws IOException
	{
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(new FileReader(string));

		// read file line by line
		String line = null;
		Scanner sc = null;
		int index = 0;

		line = reader.readLine();
		if (line == null)
		{
			return null;
		}

		ArrayList<ArrayList<String>> columns = new ArrayList<ArrayList<String>>();

		sc = new Scanner(line).useLocale(Locale.ITALIAN);
		sc.useDelimiter(";");

		while (sc.hasNext())
		{
			sc.next();
			columns.add(new ArrayList<String>());
		}

		while ((line = reader.readLine()) != null)
		{
			int i = 0;
			sc = new Scanner(line).useLocale(Locale.ITALIAN);
			sc.useDelimiter(";");
			String highType = "";
			String type = "";
			// Parser dateParser = new Parser();
			while (sc.hasNext())
			{

				type = sc.hasNextInt() ? "int" : sc.hasNextLong() ? "long" : sc.hasNextDouble() ? "double" : sc.hasNextBoolean() ? "boolean" : "string";

				if ((type == "int" || type == "long") && highType == "double")
				{
					type = "double";
				}

				if (type == "int" && highType == "long")
				{
					type = "long";
				}

				String next = sc.next();

				if (type == "string")
				{
					if (Chrono.Parse(next).size() == 1)
					{
						type = "date";
					}
				}

				if (next.isEmpty())
				{
					type = highType;
				}
				highType = type;
				try
				{
					columns.get(i).add(type);

				}
				catch (Exception e)
				{
					break;
				}
				i++;
				// if (i == 16)
				// {
				// break;
				// }
			}
		}
		// close reader
		reader.close();

		ArrayList<String> types = new ArrayList<String>();

		for (Object element : columns)
		{
			ArrayList<String> arrayList = (ArrayList<String>) element;
			if (arrayList.size() != 0)
			{

				if (arrayList.stream().allMatch(s -> s.equals(arrayList.get(0))))
				{
					types.add(arrayList.get(0));
				}
				else
				{
					String output = "";
					List<String> listDistinct = arrayList.stream().distinct().collect(Collectors.toList());
					String highType = "";
					for (Object element2 : listDistinct)
					{
						String type = (String) element2;
						if ((type == "int" || type == "long") && highType == "double")
						{
							type = "double";
						}

						if (type == "int" && highType == "long")
						{
							type = "long";
						}
						highType = type;
						output = output + (String) element2;

					}
					types.add(highType);
				}
			}
		}
		// System.out.println(types.toString());
		return types;
	}

}
