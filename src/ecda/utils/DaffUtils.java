package ecda.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import coopy.Csv;
import coopy.JavaTableView;
import coopy.Table;

public class DaffUtils
{
	public static Table getTable(String string, String s) throws IOException
	{

		BufferedReader reader = new BufferedReader(new FileReader(string));

		// read file line by line
		String line = null;
		String output = "";
		int row = 0;
		String separator = "";
		while ((line = reader.readLine()) != null)
		{
			if (s == null)
			{
				if (row == 0)
				{
					if (line.contains(";"))
					{
						separator = ";";
					}
					else
					{
						separator = ",";
					}
				}
			}
			else
			{
				separator = s;
			}

			row++;
			// System.out.println(line);
			output = output + line + "\n";
		}
		Csv csv = new Csv(separator, null);

		// close reader
		reader.close();
		return csv.makeTable(output);

	}

	public static Table getComparison(Table table1, Table table2, boolean total)
	{
		coopy.Alignment alignment = coopy.Coopy.compareTables(table1, table2, null).align();

		// System.out.println(table1.get_height() + " " + alignment.count());
		JavaTableView table_diff = new JavaTableView();
		coopy.CompareFlags flags = new coopy.CompareFlags();

		// CHANGE FOR THE OTHER COMPARISON
		if (total)
		{
			flags.show_unchanged = true;
			flags.show_unchanged_columns = true;
		}

		flags.ordered = false;

		coopy.TableDiff highlighter = new coopy.TableDiff(alignment, flags);
		highlighter.hilite(table_diff);
		// table_diff.get
		// System.out.println(highlighter.hasDifference());
		return table_diff;
	}

	public static void patch(Table table1, Table table2)
	{

		coopy.CompareFlags flags = new coopy.CompareFlags();
		// flags.ordered = false;
		coopy.Coopy.patch(table1, table2, flags);
	}
}
