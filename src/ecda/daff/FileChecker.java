package ecda.daff;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import ecda.experiments.size.SizeOps;
import ecda.utils.CSVUtils;

public class FileChecker
{

	public static void main(String[] args) throws IOException
	{

		SizeOps.loadURLS();
		HashMap<Integer, String> map = SizeOps.getUrls();

		String csvFile = "./files/correct3.csv";
		Scanner scanner = new Scanner(new File(csvFile));

		scanner.nextLine();
		ArrayList<String> domains = new ArrayList<String>();
		while (scanner.hasNext())
		{
			List<String> line = CSVUtils.parseLine(scanner.nextLine(), ',');
			domains.add(map.get(Integer.parseInt(line.get(0))).split("/")[2]);
		}

		scanner.close();

		Map<String, Integer> counts = toMap(domains);
		for (String string : counts.keySet())
		{
			// System.out.println(string + ", " + counts.get(string));
		}
	}

	static public Map<String, Integer> toMap(List<String> lst)
	{

		return lst.stream().collect(HashMap<String, Integer>::new, (map, str) ->
		{
			if (!map.containsKey(str))
			{
				map.put(str, 1);
			}
			else
			{
				map.put(str, map.get(str) + 1);
			}
		}, HashMap<String, Integer>::putAll);
	}

}
