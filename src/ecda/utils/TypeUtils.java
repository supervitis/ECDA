package ecda.utils;

import java.util.List;
import java.util.stream.Collectors;

public class TypeUtils
{

	public static String getType(List<String> types)
	{

		String output = "";
		List<String> listDistinct = types.stream().distinct().collect(Collectors.toList());
		if (listDistinct.size() == 1)
		{
			output = listDistinct.get(0);
		}
		else
		{

			if (listDistinct.contains("string"))
			{
				output = "string";
			}
			else if (listDistinct.contains("date"))
			{
				output = "dateWrong";
			}
			else if (listDistinct.contains("double"))
			{
				output = "double";
			}
			else if (listDistinct.contains("long"))
			{
				output = "long";
			}
			else
			{
				System.out.println("Error en tipos");
				System.out.println(listDistinct.toString());
				return "";
			}
		}

		return output;
	}

}
