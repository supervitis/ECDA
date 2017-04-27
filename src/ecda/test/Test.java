package ecda.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class Test
{

	public static void main(String[] args) throws IOException, InterruptedException
	{

		BufferedReader reader = new BufferedReader(new FileReader("./files/csv_urls.csv"));

		// read file line by line
		String line = null;
		String output = "";
		Scanner scanner = null;
		int i = 0;
		reader.readLine();
		while ((line = reader.readLine()) != null)
		{
			i++;
			if (i < 135)
			{
				continue;
			}
			scanner = new Scanner(line);
			scanner.useDelimiter(",");
			int index = 0;
			String url = "";
			String versionsDB = "";
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

			System.out.println("Url: " + url);
			ArrayList<String> fileVersions = getVersionsFile(url);
			System.out.println("Versions: " + fileVersions.size());
			if (fileVersions.size() != 0)
			{
				new File("./files/DATA/" + i).mkdirs();
				for (Object element : fileVersions)
				{
					String string = (String) element;
					getCsvFile(string, "./files/DATA/" + i + "/" + string.substring(53, 67) + ".csv");
					Thread.sleep(500);
				}

			}
			else
			{
				Thread.sleep(500);

			}

			if (i == 137)
			{
				break;
			}
		}

		// close reader
		reader.close();

		// String testFile =
		// "http://data.wien.gv.at/daten/geo?service=WFS&request=GetFeature&version=1.1.0&typeName=ogdwien:LUFTGUETENETZOGD&srsName=EPSG:4326&outputFormat=csv";
		//
		// URL url = new
		// URL("http://csvengine.ai.wu.ac.at/datamonitor/api/v1/memento/timemap/json/"
		// + testFile);
		// try (InputStream is = url.openStream(); JsonReader rdr =
		// Json.createReader(is))
		// {
		//
		// JsonObject obj = rdr.readObject();
		// // System.out.println(obj.toString());
		// JsonObject mementos = obj.getJsonObject("mementos");
		// JsonArray results = mementos.getJsonArray("list");
		// String test = "";
		// for (JsonObject result : results.getValuesAs(JsonObject.class))
		// {
		// test = result.getString("uri", "");
		//
		// String replaced = test.replaceAll("localhost:5001",
		// "csvengine.ai.wu.ac.at");
		// getCsvFile(replaced, "./files/weather/" + "weather" + "_" +
		// test.substring(46, 60) + ".csv");
		// }
		//
		// }

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

	private static void getCsvFile(String replaced, String file)
	{
		try
		{
			URL url2;

			url2 = new URL(replaced);

			HttpURLConnection con = (HttpURLConnection) url2.openConnection();
			con.setRequestProperty("Accept-Encoding", "gzip");
			System.out.println("Length : " + con.getContentLength());
			if (con.getContentLength() == -1)
			{
				return;
			}
			if ("gzip".equals(con.getContentEncoding()))
			{
				FileOutputStream fos = new FileOutputStream(file, true);
				GZIPInputStream gis = new GZIPInputStream(con.getInputStream());
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
			else
			{
				FileOutputStream fos = new FileOutputStream(file);
				GZIPInputStream gis = new GZIPInputStream(con.getInputStream());
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
		}
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			return;
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			return;
		}

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
