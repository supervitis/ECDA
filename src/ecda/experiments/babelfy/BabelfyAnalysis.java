package ecda.experiments.babelfy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import ecda.utils.CSVUtils;
import ecda.utils.FileUtils;
import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation;

public class BabelfyAnalysis
{

	public static void main(String[] args) throws FileNotFoundException
	{

		// String path = "./files/DATA2/98/20140905170049.csv";

		File folder = new File("./files/babelfy2/3632/");
		File[] files = folder.listFiles();
		// Babelfy bfy = new Babelfy();
		List<List<SemanticAnnotation>> lists = new ArrayList<List<SemanticAnnotation>>();

		for (File file : files)
		{
			// {
			// String test = getStringCSV(file);
			// List<SemanticAnnotation> bfyAnnotations = bfy.babelfy(test,
			// Language.EN);
			// System.out.println(bfyAnnotations.size());
			// serialize(bfyAnnotations, "./files/babelfy/98/" +
			// file.getName().split(".")[0] + ".ser");
			List<SemanticAnnotation> bfyAnnotations = (List<SemanticAnnotation>) FileUtils.deserialize("./files/babelfy2/3632/" + file.getName());
			// System.out.println(bfyAnnotations.size());
			// for (SemanticAnnotation annotation : bfyAnnotations)
			// {
			// // splitting the input text using the CharOffsetFragment start
			// // and end anchors
			// // String frag =
			// //
			// inputText.substring(annotation.getCharOffsetFragment().getStart(),
			// // annotation.getCharOffsetFragment().getEnd() + 1);
			// System.out.println("\t" + annotation.getBabelSynsetID());
			// System.out.println("\t" + annotation.getBabelNetURL());
			// System.out.println("\t" + annotation.getDBpediaURL());
			// System.out.println("\t" + annotation.getSource());
			// }

			lists.add(bfyAnnotations);

		}

		Set<SemanticAnnotation> intersection = new HashSet<>(lists.get(0));
		System.out.println("--------------");
		Set<SemanticAnnotation> totalAnnotations = new HashSet<>();
		long sizeBefore = 0;
		long sizeAfter = 0;
		for (List<SemanticAnnotation> list : lists)
		{
			sizeBefore = totalAnnotations.size();
			Set<SemanticAnnotation> newIntersection = new HashSet<>();
			totalAnnotations.addAll(list);
			sizeAfter = totalAnnotations.size();
			for (SemanticAnnotation i : list)
			{
				if (intersection.contains(i))
				{
					newIntersection.add(i);
				}
			}
			intersection = newIntersection;
			System.out.println("Iteration size " + list.size() + " / intersection " + intersection.size() + " / new ones " + (sizeAfter - sizeBefore));
		}

		System.out.println("Intersection " + intersection.size());
		System.out.println("Total entities " + totalAnnotations.size());
		for (Object element : intersection)
		{
			SemanticAnnotation annotation = (SemanticAnnotation) element;
			// annotation.getCharOffsetFragment().getEnd() + 1);
			System.out.println("\t" + annotation.getBabelSynsetID());
			System.out.println("\t" + annotation.getBabelNetURL());
			System.out.println("\t" + annotation.getDBpediaURL());
			System.out.println("\t" + annotation.getSource());
		}

	}

	private static String getHeader(File file) throws FileNotFoundException
	{
		// TODO Auto-generated method stub
		String output = "";
		Scanner scanner = new Scanner(file);
		String header = scanner.nextLine();
		char separator = getSeparator(header);

		scanner.close();

		List<String> line = CSVUtils.parseLine(header, separator);
		return concatenate(line);
	}

	private static char getSeparator(String header)
	{
		char separator = ';';
		if (!header.contains(";"))
		{
			separator = ',';
		}
		return separator;
	}

	private static String concatenate(List<String> list)
	{
		String output = "";
		for (Object element : list)
		{
			String string = (String) element;
			output = output + string + " ";

		}
		return output;
	}

	private static String getStringCSV(File file) throws FileNotFoundException
	{
		// TODO Auto-generated method stub
		String output = "";
		Scanner scanner = new Scanner(file);
		String header = scanner.nextLine();
		char separator = getSeparator(header);
		while (scanner.hasNext())
		{
			List<String> line = CSVUtils.parseLine(scanner.nextLine(), separator);
			for (Object element : line)
			{
				String string = (String) element;
				output = output + string + " ";

			}
		}
		scanner.close();
		return output;
	}

}
