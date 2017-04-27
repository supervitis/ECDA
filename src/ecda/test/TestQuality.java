package ecda.test;

import java.io.IOException;

import coopy.Table;
import ecda.utils.CSVUtils;
import ecda.utils.DaffUtils;

public class TestQuality
{

	public static void main(String[] args) throws IOException
	{

		Table table1 = DaffUtils.getTable("./files/DATA/62/20140812120302.csv", null);
		Table table2 = DaffUtils.getTable("./files/DATA/62/20140822230142.csv", null);

		Table table3 = DaffUtils.getComparison(table1, table2, true);
		// System.out.println(table3.toString());
		// System.out.println(table1.toString());

		DaffUtils.patch(table1, table3);

		CSVUtils.storeCSV(table1, "./files/output/62/20140822230142.csv");

	}

}
