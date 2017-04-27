package ecda.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.commons.text.similarity.JaccardSimilarity;
import org.apache.commons.text.similarity.LevenshteinDistance;

import com.wanasit.chrono.Chrono;

import ecda.utils.TypeUtils;

public class Cell implements Serializable
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<String> values;

	public Cell(ArrayList<String> values)
	{
		super();
		this.values = values;
	}

	public Cell()
	{
		this.values = new ArrayList<String>();
	}

	public void addValue(String string)
	{
		this.values.add(string);
	}

	public ArrayList<String> getValues()
	{
		return this.values;
	}

	public double getJaccard()
	{

		// Function to get the average jaccard similarity
		JaccardSimilarity similarity = new JaccardSimilarity();
		double aggregate = 0;
		for (int i = 0; i < this.values.size(); i++)
		{
			double sumSimilarity = 0;
			String value = this.values.get(0);
			for (int j = 0; j < this.values.size(); j++)
			{
				if (i == j)
				{
					continue;
				}
				sumSimilarity = sumSimilarity + similarity.apply(value, this.values.get(j));

			}
			sumSimilarity = sumSimilarity / (this.values.size() - 1);
			aggregate = aggregate + sumSimilarity;
		}
		return aggregate / this.values.size();
	}

	public double getLehvenshtein()
	{
		LevenshteinDistance distance = new LevenshteinDistance();
		double aggregate = 0;
		for (int i = 0; i < this.values.size() - 1; i++)
		{
			String value = this.values.get(0);
			aggregate = aggregate + distance.apply(value, this.values.get(i + 1));

		}

		return aggregate / (this.values.size() - 1);
	}

	public double getJaccardsimple()
	{

		// Function to get the average jaccard similarity
		JaccardSimilarity similarity = new JaccardSimilarity();
		double aggregate = 0;
		for (int i = 0; i < this.values.size() - 1; i++)
		{
			String value = this.values.get(0);
			aggregate = aggregate + similarity.apply(value, this.values.get(i + 1));

		}

		return aggregate / (this.values.size() - 1);
	}

	public double getAverageSize()
	{
		double size = 0;
		for (Object element : this.values)
		{
			String string = (String) element;
			size = size + string.length();
		}
		return size / this.values.size();

	}

	@Override
	public String toString()
	{

		String output = "";

		List<String> listDistinct = this.values.stream().distinct().collect(Collectors.toList());

		// Display them to terminal using stream::collect with a build in
		// Collector.
		// String collectDistinct =
		// listDistinct.stream().collect(Collectors.joining(", "));

		String collectDistinct = this.values.size() + " : " + this.values.get(0);
		// System.out.println(collectDistinct);

		// for (Object element : this.values)
		// {
		// String string = (String) element;
		// output = output + "->" + string;
		//
		// }
		return collectDistinct;
	}

	public String getType()
	{
		Scanner sc;

		// Parser dateParser = new Parser();

		ArrayList<String> types = new ArrayList<>();

		for (Object element : this.values)
		{
			String highType = "";
			String type = "";
			String string = (String) element;
			sc = new Scanner(string).useLocale(Locale.ITALIAN);

			sc.useDelimiter(";");
			type = sc.hasNextInt() ? "int" : sc.hasNextLong() ? "long" : sc.hasNextDouble() ? "double" : sc.hasNextBoolean() ? "boolean" : "string";

			if ((type == "int" || type == "long") && highType == "double")
			{
				type = "double";
			}

			if (type == "int" && highType == "long")
			{
				type = "long";
			}

			String next = "";
			try
			{
				next = sc.next();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				// System.out.println("aqui es");
				type = "string";
			}
			sc.close();
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

			types.add(type);

		}
		String output = "";
		if (types.size() != 0)
		{

			if (types.stream().allMatch(s -> s.equals(types.get(0))))
			{
				output = types.get(0);
			}
			else
			{
				output = TypeUtils.getType(types);
			}
		}
		else
		{
			output = "";
		}
		return output;

	}

	public int getChanges()
	{
		// TODO Auto-generated method stub
		return this.values.stream().distinct().collect(Collectors.toList()).size();
	}

}
