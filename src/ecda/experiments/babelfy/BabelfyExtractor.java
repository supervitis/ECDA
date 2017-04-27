package ecda.experiments.babelfy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import ecda.experiments.size.SizeOps;
import ecda.utils.CSVUtils;
import ecda.utils.FileUtils;
import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation;
import it.uniroma1.lcl.babelfy.core.Babelfy;
import it.uniroma1.lcl.babelnet.InvalidBabelSynsetIDException;
import it.uniroma1.lcl.jlt.util.Language;

public class BabelfyExtractor
{

	private static String FOLDER = "./files/output2/";
	private static String FOLDERBABELFY = "./files/babelfy3/";
	private static String FOLDERHEADER = "./files/babelfyHeader/";

	private static long counter = 0;

	public static void main(String[] args) throws IOException, InvalidBabelSynsetIDException, InterruptedException
	{
		String csvFile = "./files/correctFinal.csv";
		String classesFile = "./files/classes.csv";
		Scanner scanner = new Scanner(new File(csvFile));
		File filedaff = new File(FOLDERBABELFY);
		String[] directoriesBabelfy = filedaff.list((current, name) -> new File(current, name).isDirectory());

		SizeOps.loadURLS();
		scanner.nextLine();

		String outputFile = "./files/babelfyOutput.csv";
		// BabelNet bn = BabelNet.getInstance();

		FileWriter writer = new FileWriter(outputFile, false);
		CSVUtils.writeLine(writer, Arrays.asList("folder", "intersection", "union", "average", "intersectionHeader", "unionHeader", "averageHeader", "class"));

		Scanner scannerClasses = new Scanner(new File(classesFile));
		scannerClasses.nextLine();
		HashMap<String, String> mapClasses = new HashMap<String, String>();
		while (scannerClasses.hasNext())
		{
			List<String> line = CSVUtils.parseLine(scannerClasses.nextLine(), ',');
			mapClasses.put(line.get(0), line.get(1));

		}
		scannerClasses.close();
		while (scanner.hasNext())
		{

			List<String> line = CSVUtils.parseLine(scanner.nextLine(), ';');

			System.out.println("File " + line.get(0));
			//
			// if (!Arrays.asList(directoriesBabelfy).contains(line.get(0)))
			// {
			// if (!ExtractBabelfy(line.get(0)))
			// {
			// break;
			//
			// }
			//
			// }

			if (Arrays.asList(directoriesBabelfy).contains(line.get(0)))
			{

				// EXTRAER LAS CLASES A UN CSV, USARLO AQUI, PIMPAM

				// HACER LO MISMO PARA EL HEADER. TENER AMBOS CAMPOS Y AÑADIR LA
				// CLASE DEL EJERCICIO ANTERIOR. VER SI HAY RELACIÓN, USAR
				// MISMAS TABLAS. PIMPAMPUM

				// MISMAS GRÁFICAS

				List<String> valuesTotal = extractValues(line.get(0), FOLDERBABELFY);
				List<String> valuesHeader = extractValues(line.get(0), FOLDERHEADER);

				List<String> values = new ArrayList<String>();
				values.add(line.get(0));
				values.addAll(valuesTotal);
				values.addAll(valuesHeader);
				values.add(mapClasses.get(line.get(0)));

				CSVUtils.writeLine(writer, values);
			}

		}

		writer.close();
		scanner.close();
	}

	private static List<String> extractValues(String string, String folder)
	{
		// TODO Auto-generated method stub
		List<List<SemanticAnnotation>> listAnnotations = GetBabelfy(string, folder);

		Set<SemanticAnnotation> intersection = null;
		try
		{
			intersection = new HashSet<>(listAnnotations.get(0));
		}
		catch (IndexOutOfBoundsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			intersection = new HashSet<>();

		}
		System.out.println("--------------");
		Set<SemanticAnnotation> totalAnnotations = new HashSet<>();
		long sizeBefore = 0;
		long sizeAfter = 0;
		long sizeSum = 0;
		for (List<SemanticAnnotation> list : listAnnotations)
		{
			sizeBefore = totalAnnotations.size();
			Set<SemanticAnnotation> newIntersection = new HashSet<>();
			try
			{
				totalAnnotations.addAll(list);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			sizeAfter = totalAnnotations.size();
			for (SemanticAnnotation i : list)
			{
				if (intersection.contains(i))
				{
					newIntersection.add(i);
				}
			}
			intersection = newIntersection;
			// System.out.println("Iteration size " + list.size() + " /
			// intersection " + intersection.size() + " / new ones " +
			// (sizeAfter - sizeBefore));
			sizeSum = sizeAfter - sizeBefore + sizeSum;
		}
		return Arrays.asList(intersection.size() + "", totalAnnotations.size() + "", sizeSum / listAnnotations.size() + "");
	}

	private static List<List<SemanticAnnotation>> GetBabelfy(String string, String f)
	{
		List<List<SemanticAnnotation>> list = new ArrayList<List<SemanticAnnotation>>();
		File folder = new File(f + string);
		File[] files = folder.listFiles();
		for (File file : files)
		{
			List<SemanticAnnotation> annotations = (List<SemanticAnnotation>) FileUtils.deserialize(f + string + "/" + file.getName());
			list.add(annotations);
		}
		return list;

	}

	private static char getSeparator(String line)
	{
		if (line.contains(";"))
		{
			return ';';

		}
		else
		{
			return ',';
		}
	}

	private static boolean ExtractBabelfy(String string) throws FileNotFoundException
	{
		File folder = new File(FOLDER + string);
		File[] files = folder.listFiles();
		Babelfy bfy = new Babelfy();
		// List<List<SemanticAnnotation>> lists = new
		// ArrayList<List<SemanticAnnotation>>();
		boolean correct = true;
		new File(FOLDERBABELFY + string).mkdirs();

		for (File file : files)
		{
			// {
			// String test = getStringCSV(file);
			String test = getHeaderCSV(file);
			String domain = SizeOps.getUrls().get(Integer.parseInt(string)).split("/")[2];
			System.out.println(domain);
			List<SemanticAnnotation> bfyAnnotations = null;
			try
			{
				if (domain.equals("www.landesdatenbank.nrw.de"))
				{
					bfyAnnotations = bfy.babelfy(test, Language.DE);
				}
				else
				{
					bfyAnnotations = bfy.babelfy(test, Language.EN);
				}
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				continue;
			}

			FileUtils.serialize(bfyAnnotations, FOLDERBABELFY + string + "/" + file.getName() + ".ser");

			counter++;
			if (counter == 9500)
			{
				correct = false;
				System.out.println("Folder: " + string + " file: " + file.getName());
				break;
			}

			// List<SemanticAnnotation> bfyAnnotations =
			// deserialize("./files/babelfy/98/" + file.getName());
			// System.out.println(bfyAnnotations.size());
			// for (SemanticAnnotation annotation : bfyAnnotations)
			// {
			// // splitting the input text using the CharOffsetFragment start
			// // and end anchors
			// // String frag =
			// //
			// //
			// inputText.substring(annotation.getCharOffsetFragment().getStart(),
			// // annotation.getCharOffsetFragment().getEnd() + 1);
			// System.out.println("\t" + annotation.getBabelSynsetID());
			// System.out.println("\t" + annotation.getBabelNetURL());
			// System.out.println("\t" + annotation.getDBpediaURL());
			// System.out.println("\t" + annotation.getSource());
			// }
		}
		// Set<SemanticAnnotation> intersection = new HashSet<>(lists.get(0));
		// System.out.println("--------------");
		// Set<SemanticAnnotation> totalAnnotations = new HashSet<>();
		// long sizeBefore = 0;
		// long sizeAfter = 0;
		// for (List<SemanticAnnotation> list : lists)
		// {
		// sizeBefore = totalAnnotations.size();
		// Set<SemanticAnnotation> newIntersection = new HashSet<>();
		// totalAnnotations.addAll(list);
		// sizeAfter = totalAnnotations.size();
		// for (SemanticAnnotation i : list)
		// {
		// if (intersection.contains(i))
		// {
		// newIntersection.add(i);
		// }
		// }
		// intersection = newIntersection;
		// System.out.println("Iteration size " + list.size() + " / intersection
		// " + intersection.size() + " / new ones " + (sizeAfter - sizeBefore));
		// }
		//
		// System.out.println("Intersection " + intersection.size());
		// System.out.println("Total entities " + totalAnnotations.size());
		// for (Object element : intersection)
		// {
		// SemanticAnnotation annotation = (SemanticAnnotation) element;
		// // annotation.getCharOffsetFragment().getEnd() + 1);
		// System.out.println("\t" + annotation.getBabelSynsetID());
		// System.out.println("\t" + annotation.getBabelNetURL());
		// System.out.println("\t" + annotation.getDBpediaURL());
		// System.out.println("\t" + annotation.getSource());
		// }
		return correct;

	}

	private static String getHeaderCSV(File file) throws FileNotFoundException
	{
		String output = "";
		Scanner scanner = new Scanner(file);
		String header;
		try
		{
			header = scanner.nextLine();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			header = scanner.nextLine();
		}
		char separator = getSeparator(header);
		// FUCKING METHOD TO GET SEPARATOR IS FUCKING NECESSARY IF WE WANT
		// THE FUCKING ENTITIES. THAT IS WHY IT FAILS

		List<String> line = CSVUtils.parseLine(header, separator);
		for (Object element : line)
		{
			String string = (String) element;
			output = output + string + " ";

		}
		scanner.close();
		return output;
	}

	private static String getStringCSV(File file) throws FileNotFoundException
	{
		// TODO Auto-generated method stub
		String output = "";
		Scanner scanner = new Scanner(file);
		char separator;
		try
		{
			separator = getSeparator(scanner.nextLine());
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			scanner.close();
			return "";
		}
		while (scanner.hasNext())
		{
			// FUCKING METHOD TO GET SEPARATOR IS FUCKING NECESSARY IF WE WANT
			// THE FUCKING ENTITIES. THAT IS WHY IT FAILS

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
