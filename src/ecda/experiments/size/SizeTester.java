package ecda.experiments.size;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

public class SizeTester
{

	public static void main(String[] args) throws IOException
	{
		// function1();
		SizeOps.loadSizes("./files/DATA2/");
		for (Object element : SizeOps.getSizes().keySet())
		{
			System.out.println(element + ";" + new File("./files/DATA2/" + element).listFiles().length + ";" + SizeOps.getSizes().get(element));

		}
	}

	private static void function2() throws IOException
	{
		File file = new File("./files/DATA2/");
		String[] directories = file.list((current, name) -> new File(current, name).isDirectory());

		int[] myIntArray = new int[directories.length];

		for (int i = 0; i < directories.length; i++)
		{
			myIntArray[i] = Integer.parseInt(directories[i]);
		}

		Arrays.sort(myIntArray);

		for (int i = 0; i < myIntArray.length; i++)
		{
			directories[i] = "" + myIntArray[i];
		}
		// System.out.println(Arrays.toString(directories));
		SizeOps.loadURLS();
		HashMap<Integer, String> map = SizeOps.getUrls();
		for (String string : directories)
		{
			Path path = Paths.get("./files/DATA2/" + string);

			// System.out.println(map.get(Integer.parseInt(string)) + " : " +
			// new File("./files/DATA/" + string).list().length + "/ " +
			// SizeOps.size(path));
			long sizeTotal = SizeOps.size(path);
			File folder = new File("./files/DATA2/" + string);
			File[] listOfFiles = folder.listFiles();
			// System.out.println(listOfFiles.length);

			System.out.println(string + " : " + new File("./files/DATA2/" + string).list().length + " / " + sizeTotal / (1024 * 1024) + " --- ");
		}

	}

	private static void function1() throws IOException
	{
		File file = new File("./files/DATA/");
		String[] directories = file.list((current, name) -> new File(current, name).isDirectory());
		// System.out.println(Arrays.toString(directories));
		SizeOps.loadURLS();
		HashMap<Integer, String> map = SizeOps.getUrls();
		for (String string : directories)
		{
			Path path = Paths.get("./files/DATA/" + string);
			Path pathDaff = Paths.get("./files/daff/" + string);

			// System.out.println(map.get(Integer.parseInt(string)) + " : " +
			// new File("./files/DATA/" + string).list().length + "/ " +
			// SizeOps.size(path));
			long sizeTotal = SizeOps.size(path);
			long sizeDaff = SizeOps.size(pathDaff);
			File folder = new File("./files/DATA/" + string);
			File[] listOfFiles = folder.listFiles();
			System.out.println(listOfFiles.length);
			long sizeFirstFile = listOfFiles[0].length();

			System.out.println(string + " : " + new File("./files/DATA/" + string).list().length + " / " + sizeTotal + " --- " + sizeDaff + " *** " + sizeFirstFile + " == " + (sizeTotal - sizeDaff - sizeFirstFile) + " | | " + map.get(Integer.parseInt(string)));

		}
	}

}
