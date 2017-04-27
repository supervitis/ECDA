package ecda.extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.commons.io.FileUtils;

import ecda.experiments.size.SizeOps;
import ecda.utils.CSVUtils;

public class DataMonitorExtractor
{

	public static void main(String[] args) throws IOException, InterruptedException
	{

		BufferedReader reader = new BufferedReader(new FileReader("./files/csv_urls.csv"));

		// read file line by line
		String line = null;
		Scanner scanner = null;
		int i = 0;
		String csvFile = "./files/files.csv";
		FileWriter writer = new FileWriter(csvFile);
		CSVUtils.writeLine(writer, Arrays.asList("url", "versionsDB", "versionsJSON", "versions"));

		reader.readLine();
		while ((line = reader.readLine()) != null)
		{

			i++;
			scanner = new Scanner(line);
			scanner.useDelimiter(",");
			int index = 0;
			String url = "";
			String versionsDB = "";
			int versionsJSON = 0;
			while (scanner.hasNext())
			{

				String data = scanner.next();
				if (index == 0)
				{
					url = data;
				}
				else if (index == 1)
				{
					versionsDB = data;
				}
				else
				{
					System.out.println("invalid data::" + data);
				}
				index++;
			}

			// System.out.println("Url: " + url);
			ArrayList<String> fileVersions = getVersionsFile(url);

			versionsJSON = fileVersions.size();
			int versions = 0;

			// ARCHIVO 463 XML

			if (fileVersions.size() != 0 && !url.contains("genesis."))
			{
				new File("./files/DATA2/" + i).mkdirs();
				for (Object element : fileVersions)
				{
					String string = (String) element;
					if (getCsvFile(string, "./files/DATA2/" + i + "/" + string.substring(53, 67) + ".csv"))
					{
						versions++;
					}
					Thread.sleep(100);
				}
				// Max size for folder: 100MB
				if (SizeOps.size(Paths.get("./files/DATA2/" + i)) > 104857600)
				{
					FileUtils.deleteDirectory(new File("./files/DATA2/" + i));
				}

			}
			else
			{
				Thread.sleep(100);

			}

			CSVUtils.writeLine(writer, Arrays.asList(url, versionsDB, versionsJSON + "", versions + ""));

		}

		// close reader
		reader.close();
		writer.flush();
		writer.close();

	}

	private static ArrayList<String> getVersionsFile(String testFile) throws IOException
	{
		System.out.println("Accessing file..." + testFile);
		ArrayList<String> versions = new ArrayList<>();
		URL url = new URL("http://csvengine.ai.wu.ac.at/datamonitor/api/v1/memento/timemap/json/" + testFile);
		URLConnection conn = url.openConnection();
		conn.setConnectTimeout(30000);
		try (InputStream is = conn.getInputStream(); JsonReader rdr = Json.createReader(is))
		{

			JsonObject obj = rdr.readObject();
			JsonObject mementos = obj.getJsonObject("mementos");
			JsonArray results = mementos.getJsonArray("list");
			String test = "";
			for (JsonObject result : results.getValuesAs(JsonObject.class))
			{
				test = result.getString("uri", "");
				String replaced = test.replaceAll("localhost:5001", "csvengine.ai.wu.ac.at");
				versions.add(replaced);
			}
			rdr.close();
			is.close();

		}
		catch (Exception e)
		{
			System.out.println(e);
			return versions;
		}
		return versions;
	}

	private static boolean getCsvFile(String replaced, String file)
	{
		try
		{
			URL url2;

			url2 = new URL(replaced);
			// System.out.println(replaced);
			HttpURLConnection con = (HttpURLConnection) url2.openConnection();
			con.setRequestProperty("Accept-Encoding", "gzip");
			// System.out.println("Length : " + con.getContentLength());
			if (con.getContentLength() == -1 || con.getContentLength() == 0)
			{
				return false;
			}

			GZIPInputStream gis = new GZIPInputStream(con.getInputStream());
			FileOutputStream fos = new FileOutputStream(file, true);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = gis.read(buffer)) != -1)
			{
				fos.write(buffer, 0, len);
			}
			// close resources
			fos.close();
			gis.close();

		}
		catch (MalformedURLException e)
		{
			// System.out.println("catch!");
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			// System.out.println("catch!" + e);
			return false;
		}
		catch (IOException e)
		{
			// System.out.println("catch!");
			return false;
		}
		return true;

	}

	private static void decompressGzipFile(String gzipFile, String newFile)
	{
		try
		{
			FileInputStream fis = new FileInputStream(gzipFile);
			GZIPInputStream gis = new GZIPInputStream(fis);
			FileOutputStream fos = new FileOutputStream(newFile);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = gis.read(buffer)) != -1)
			{
				fos.write(buffer, 0, len);
			}
			// close resources
			fos.close();
			gis.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	private static void compressGzipFile(String file, String gzipFile)
	{
		try
		{
			FileInputStream fis = new FileInputStream(file);
			FileOutputStream fos = new FileOutputStream(gzipFile);
			GZIPOutputStream gzipOS = new GZIPOutputStream(fos);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = fis.read(buffer)) != -1)
			{
				gzipOS.write(buffer, 0, len);
			}
			// close resources
			gzipOS.close();
			fos.close();
			fis.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

}
