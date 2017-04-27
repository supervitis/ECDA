package ecda.experiments.size;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

public class SizeOps
{

	private static HashMap<Integer, String> urls;
	private static HashMap<String, Long> sizes;
	private static String FOLDER;

	public static double getAverage(String folder)
	{
		double sizeFolder = sizes.get(folder);
		return sizeFolder / new File(FOLDER + folder).listFiles().length;

	}

	public static long size(Path path)
	{

		final AtomicLong size = new AtomicLong(0);

		try
		{
			Files.walkFileTree(path, new SimpleFileVisitor<Path>()
			{
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
				{

					size.addAndGet(attrs.size());
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc)
				{

					System.out.println("skipped: " + file + " (" + exc + ")");
					// Skip folders that can't be traversed
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc)
				{

					if (exc != null)
					{
						System.out.println("had trouble traversing: " + dir + " (" + exc + ")");
					}
					// Ignore errors traversing a folder
					return FileVisitResult.CONTINUE;
				}
			});
		}
		catch (IOException e)
		{
			throw new AssertionError("walkFileTree will not throw IOException if the FileVisitor does not");
		}

		return size.get();
	}

	public static void loadSizes(String path)
	{
		FOLDER = path;

		setSizes(getMapSizeFolder(path));
	}

	public static void loadURLS() throws IOException
	{
		// TODO Auto-generated method stub

		BufferedReader reader = new BufferedReader(new FileReader("./files/csv_urls.csv"));
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		String line = null;
		String output = "";
		Scanner scanner = null;
		int i = 0;
		reader.readLine();
		while ((line = reader.readLine()) != null)
		{
			i++;
			scanner = new Scanner(line);
			scanner.useDelimiter(",");
			int index = 0;
			String url = "";
			while (scanner.hasNext())
			{

				String data = scanner.next();
				if (index == 0)
				{
					url = data;
				}
				else if (index == 1)
				{
					// int count = Integer.parseInt(data);
				}
				else
				{
					System.out.println("invalid data::" + data);
				}
				index++;
			}
			map.put(i, url);
		}
		urls = map;
		reader.close();
	}

	public static HashMap<Integer, String> getUrls()
	{
		return urls;
	}

	public static void setUrls(HashMap<Integer, String> urls)
	{
		SizeOps.urls = urls;
	}

	private static HashMap<String, Long> getMapSizeFolder(String location)
	{
		HashMap<String, Long> map = new HashMap<String, Long>();
		File file = new File(location);

		String[] directories = getOrderedFolders(file);
		for (String string : directories)
		{
			Path path = Paths.get(location + string);
			long sizeTotal = SizeOps.size(path);
			map.put(string, sizeTotal);
		}

		return map;

	}

	private static String[] getOrderedFolders(File file)
	{
		// TODO Auto-generated method stub
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
		return directories;
	}

	public static HashMap<String, Long> getSizes()
	{
		return sizes;
	}

	private static void setSizes(HashMap<String, Long> sizes)
	{
		SizeOps.sizes = sizes;
	}

}
