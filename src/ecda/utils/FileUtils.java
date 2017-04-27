package ecda.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileUtils
{

	public static void serialize(Object object, String file)
	{
		try
		{
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(object);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in " + file);
		}
		catch (IOException i)
		{
			i.printStackTrace();
		}
	}

	public static Object deserialize(String file)
	{
		Object object;
		// System.out.println(file);
		try
		{
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			object = in.readObject();
			in.close();
			fileIn.close();
		}
		catch (IOException i)
		{
			i.printStackTrace();
			return null;
		}
		catch (ClassNotFoundException c)
		{
			System.out.println("Employee class not found");
			c.printStackTrace();
			return null;
		}
		return object;
	}

}
