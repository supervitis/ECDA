package ecda.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import coopy.Csv;
import coopy.JavaTableView;
import coopy.Table;

class Example
{

	public static void main(String[] args) throws IOException
	{

		Object[][] data1 = { { "Country", "Capital" }, { "Ireland", "Dublin" }, { "France", "Paris" }, { "Spain", "Barcelona" } };

		Object[][] data2 = { { "Country", "Code", "Capital" }, { "Ireland", "ie", "Dublin" }, { "France", "fr", "Paris" }, { "Spain", "es", "Madrid" }, { "Germany", "de", "Berlin" } };

		Csv csv = new Csv(";", null);

		Table table3 = csv.makeTable("");

		System.out.println(table3.toString());

		JavaTableView table1 = new JavaTableView(data1);
		JavaTableView table2 = new JavaTableView(data2);

		Table table_diff = getComparison(table1, table2);
		System.out.println(table_diff.toString());

		coopy.DiffRender diff2html = new coopy.DiffRender();
		diff2html.usePrettyArrows(false);
		diff2html.render(table_diff);
		String table_diff_html = diff2html.html();
		System.out.print(table_diff_html);
		System.out.println(csv.renderTable(table1));

		Table table4 = getTable("files/zamg_20140715030002.csv");
		Table table5 = getTable("files/zamg_20140717030003.csv");

		System.out.println(getComparison(table4, table5).toString());

		// System.out.println(csv.renderTable(table4));

	}

	public static Table getComparison(Table table1, Table table2)
	{
		coopy.Alignment alignment = coopy.Coopy.compareTables(table1, table2, null).align();
		JavaTableView table_diff = new JavaTableView();
		coopy.CompareFlags flags = new coopy.CompareFlags();
		coopy.TableDiff highlighter = new coopy.TableDiff(alignment, flags);
		highlighter.hilite(table_diff);
		return table_diff;
	}

	public static Table getTable(String string) throws IOException
	{
		// TODO Auto-generated method stub

		BufferedReader reader = new BufferedReader(new FileReader(string));

		// read file line by line
		String line = null;
		String output = "";
		while ((line = reader.readLine()) != null)
		{
			// System.out.println(line);
			output = output + line + "\n";
		}
		Csv csv = new Csv(";", null);

		// close reader
		reader.close();
		return csv.makeTable(output);

	}

}
