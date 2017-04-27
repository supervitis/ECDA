package ecda.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import coopy.Table;
import ecda.experiments.size.SizeOps;
import ecda.utils.CSVUtils;
import ecda.utils.DaffUtils;

public class Folder
{
	private static final String FOLDER = "./files/output2/";
	private static final String FOLDERDAFF = "./files/daff2/";
	private static final String FOLDERDAFFOUTPUT = "./files/daffOutput2/";

	private String name;
	private File[] files;
	private long sizeOriginal;
	private long sizeDaff;
	private boolean correct;
	private long originalRows;
	private long originalColumns;

	private long addedRows;
	private long deletedRows;
	private long changedRows;

	private long addedColumns;
	private long deletedColumns;
	private long changedColumns;
	private long renamedColumns;

	private long changedCells;

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public File[] getFiles()
	{
		return this.files;
	}

	public void setFiles(File[] files)
	{
		this.files = files;
	}

	public long getSizeOriginal()
	{
		return this.sizeOriginal;
	}

	public void setSizeOriginal(long sizeOriginal)
	{
		this.sizeOriginal = sizeOriginal;
	}

	public long getSizeDaff()
	{
		return this.sizeDaff;
	}

	public void setSizeDaff(long sizeDaff)
	{
		this.sizeDaff = sizeDaff;
	}

	public boolean isCorrect()
	{
		return this.correct;
	}

	public void setCorrect(boolean correct)
	{
		this.correct = correct;
	}

	public Folder(String name) throws IOException
	{
		super();
		this.name = name;
		this.files = this.getFiles(name);

		Path path = Paths.get(FOLDER + name);
		this.sizeOriginal = SizeOps.size(path);
		Path pathdaff = Paths.get(FOLDERDAFF + name);
		this.sizeDaff = SizeOps.size(pathdaff) + this.files[0].length();
		this.correct = this.checkDaff(name);
		this.LoadValues();

	}

	private void LoadValues() throws FileNotFoundException
	{
		File folder = new File(FOLDERDAFF + this.name);
		File[] listOfFiles = folder.listFiles();

		long addedColumns = 0;
		long deletedColumns = 0;
		long renamedColumns = 0;

		long addedRows = 0;
		long deletedRows = 0;
		long changedRows = 0;

		long changedCells = 0;

		long rows = 0;
		long columns = 0;

		for (File file : listOfFiles)
		{
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext())
			{
				List<String> line = CSVUtils.parseLine(scanner.nextLine(), ';');
				// System.out.println(line.get(0));
				columns = line.size();
				rows++;
				switch (line.get(0))
				{
					case "!":
						for (Object element : line)
						{
							String string = (String) element;
							if (string.equals("+++"))
							{
								addedColumns++;
							}
							if (string.equals("---"))
							{
								deletedColumns++;
							}
							// System.out.println("!");
						}
						break;
					case "@@":
						for (Object element : line)
						{
							String string = (String) element;
							if (string.contains("->"))
							{
								renamedColumns++;
							}

						}
						// System.out.println("@@");
						break;
					case "+++":
						addedRows++;
						changedCells = changedCells + columns - 1;
						// System.out.println("+++");
						break;
					case "---":
						deletedRows++;
						changedCells = changedCells + columns - 1;
						// System.out.println("---");
						break;
					case "->":
						changedRows++;
						for (Object element : line)
						{
							String string = (String) element;
							if (string.contains("->"))
							{
								changedCells++;
							}

						}
						changedCells--;
						// System.out.println("->");
						break;
					default:
						break;
				}

				changedCells = changedCells + (addedColumns + deletedColumns) * (rows - deletedRows - addedRows - 1);
			}
			scanner.close();

		}

		this.addedColumns = addedColumns;
		this.deletedColumns = deletedColumns;
		this.changedColumns = renamedColumns;
		this.addedRows = addedRows;
		this.deletedRows = deletedRows;
		this.changedRows = changedRows;

		this.changedCells = changedCells;

		long originalRows = 0;
		long originalColumns = 0;
		Scanner scanner = new Scanner(this.files[0]);
		System.out.println(this.name);
		System.out.println(this.files[0].getName());
		List<String> line = CSVUtils.parseLine(scanner.nextLine(), ';');
		originalColumns = line.size();

		while (scanner.hasNext())
		{
			scanner.nextLine();

			originalRows++;
		}
		scanner.close();

		this.originalColumns = originalColumns;
		this.originalRows = originalRows;

	}

	private boolean checkDaff(String name) throws IOException
	{
		File folder = new File(FOLDERDAFFOUTPUT + name);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles)
		{
			Table table = DaffUtils.getTable(FOLDERDAFFOUTPUT + name + "/" + file.getName(), null);
			if (table.get_height() > 1)
			{
				return false;
			}
		}
		return true;
	}

	private File[] getFiles(String name)
	{
		File folder = new File(FOLDER + name);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> files = new ArrayList<String>();
		for (File listOfFile : listOfFiles)
		{
			files.add(listOfFile.getName());
		}
		return folder.listFiles();

	}

	public List<String> getLine(String evolution, String patch, String quality)
	{
		return Arrays.asList(this.name, this.sizeOriginal + "", this.sizeDaff + "", evolution, patch, quality, this.files.length + "", this.originalRows + "", this.originalColumns + "", this.getRatio(this.addedRows, this.originalRows), this.getRatio(this.deletedRows, this.originalRows), this.getRatio(this.changedRows, this.originalRows), this.getRatio(this.addedColumns, this.originalColumns), this.getRatio(this.deletedColumns, this.originalColumns), this.getRatio(this.renamedColumns, this.originalColumns), this.changedCells + "", this.getRatio(this.changedCells, this.originalColumns * this.originalRows));
	}

	@Override
	public String toString()
	{
		double diff = ((double) this.sizeDaff / (double) this.sizeOriginal);
		return "Folder [name=" + this.name + ", sizeOriginal=" + this.sizeOriginal + ", sizeDaff=" + this.sizeDaff + ", correct=" + this.correct + ", %=" + diff + "]";
	}

	private String getRatio(long field, long base)
	{
		return field / ((double) base * this.files.length) + "";
	}

}
