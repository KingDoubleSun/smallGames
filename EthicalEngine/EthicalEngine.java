import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


import ethicalengine.Scenario;
import ethicalengine.ScenarioGenerator;
import ethicalengine.Animal;
import ethicalengine.Character;
import ethicalengine.Character.BodyType;
import ethicalengine.Character.Gender;
import ethicalengine.Person;
import ethicalengine.Person.AgeCategory;
import ethicalengine.Person.Profession;

/**
 * This is the main class of the final project with 4 static methods.
 * The main method checks the syntax of the arguments and run the program properly.
 * 
 * @author Shuwei Liu, studentID: 847046
 */
public class EthicalEngine {
	

	/**
	 * Enum Class of Decision with 2 values, PASSENGERS and PEDESTRIANS
	 */
	enum Decision {PASSENGERS, PEDESTRIANS};
	
	/**
	 * The entry point of whole program, different modes are enabled by different arguments
	 * @param args User inputs can be combination of -c -i -r -h and filePath, 
	 *        see detail by run the program with entering -h or --help
	 */
	public static void main(String[] args) 
	{
		// constants for default path names
		final String DEFAULT_SAVE_PATH = "result.log";
		final String DEFAULT_USER_PATH = "user.log";
		
		// default config file path, assume null first
		String readPath = null;
		// default save path
		String savePath = DEFAULT_SAVE_PATH;
		// assume no interactive mode first
		boolean interactiveMode = false;
		
		// CHECK ARGUMENTS SNYTAX
		for(int i = 0; i < args.length; i++) 
		{
			// check arguments one by one
			String command = args[i];
			// assume all syntax are right, no need to print help message
			boolean printHelp = false;
			
			// the situation must print help message
			if(command.equals("-h") || command.equals("--help"))
				printHelp = true;
			
			// the situation may print help message
			else if(command.equals("-c") || command.equals("--config")) 
			{
				try 
				{
					// if the next argument is command not a path, print help message
					// otherwise, save the path
					if(args[i + 1].matches("-(.*)"))
						printHelp = true;
					else
						readPath = args[i + 1];
				}
				// the next argument may not exist, print help message
				catch(IndexOutOfBoundsException e) 
				{
					printHelp = true;
				}
			}
			
			// the situation may print help message
			else if(command.equals("-r") || command.equals("--result")) 
			{
				// similar as previous one
				try 
				{
					if(args[i + 1].matches("-(.*)"))
						printHelp = true;
					else
						savePath = args[i + 1];
				}
				catch(IndexOutOfBoundsException e) 
				{
					printHelp = true;
				}
			}
			
			// the situation enables the interactive mode
			else if(command.equals("-i") || command.equals("--interactive"))
				interactiveMode = true;
			
			// print help message if necessary and exit program, no need to check next argument
			if(printHelp) 
			{
				System.out.println("EthicalEngine - COMP90041 - Final Project\n"
				         + "\nUsage: java EthicalEngine [arguments]\n"
				         + "\nArguments:\n"
				         + "   -c or --config      Optional: path to config file\n"
				         + "   -h or --help        Print Help (this message) and exit\n"
				         + "   -r or --results     Optional: path to results log file\n"
				         + "   -i or --interactive Optional: launches interactive mode");
		       System.exit(0);
			}
		}
		
		// CHECK MODE
		if(interactiveMode) 
		{
			// if save path is default result.log, change it to user.log
			if(savePath.equals(DEFAULT_SAVE_PATH))
				savePath = DEFAULT_USER_PATH;
			// --interactive mode
			iMode(readPath, savePath);
		}
		else
		{
			Audit audit;
			// --config mode
			if(readPath != null) 
			{
				
				audit = new Audit(readCSV(readPath));
				audit.setAuditType(readPath);
				audit.run();
			}
			// default mode
			else
			{
				audit = new Audit();
				audit.run(1000);
			}
			audit.printStatistic();
			audit.printToFIle(savePath);
		}
	}
	

	/**
	 * Decide algorithm, return who will survive.
	 * Decide who survive by credit system (integer).
	 * Both group has a credit which is influenced by different characteristics.
	 * The group with higher credit survives, if equal then pedestrians will survive.
	 * @param scenario The scenario to be decided by this method
	 * @return The final decision for input scenario, as data type of Decision
	 */
	public static Decision decide(Scenario scenario) 
	{
		// constants for credit values of different characteristics
		final int IS_RED = -30;       // legalCrossing: false
		final int IS_PREGNANT = 15;   // isPregnant: true
		final int IS_PERSON = 5;      // instanceOf Person: true
		final int IS_CRIMINAL = -15;  // Profession is CRIMINAL
		final int IS_CHILD = 15;      // AgeCategory is BABY or CHILD
		
		// Both credits start with 0
		int passengerCredit = 0;
		int pedestrianCredit = 0;
		
		// get both groups of Characters
		Character[] passengers = scenario.getPassengers();
		Character[] pedestrians = scenario.getPedestrians();
		
		// check if isLegalCrossing
		if(! scenario.isLegalCrossing())
			pedestrianCredit += IS_RED;
		
		// count credits for passengers
		for(Character passenger: passengers) 
		{
			if(passenger instanceof Person) 
			{
				// add credits for the passenger group for being a Person
				passengerCredit += IS_PERSON;
				
				// add credits for being pregnant
				if(((Person) passenger).isPregnant())
					passengerCredit += IS_PREGNANT;
				
				// add credits for being CHILD or BABY
				if(((Person) passenger).getAgeCategory().equals(AgeCategory.CHILD)
				   || ((Person) passenger).getAgeCategory().equals(AgeCategory.BABY))
					passengerCredit += IS_CHILD;
				
				// add negative credits for being CRIMINAL
				if(((Person) passenger).getProfession().equals(Profession.CRIMINAL))
					passengerCredit += IS_CRIMINAL;
			}
		}
		
		// count credits for pedestrians, similar pattern as above
		for(Character pedestrian: pedestrians) 
		{
			if(pedestrian instanceof Person) 
			{
				pedestrianCredit += IS_PERSON;
				
				if(((Person) pedestrian).isPregnant())
					pedestrianCredit += IS_PREGNANT;
				
				if(((Person) pedestrian).getAgeCategory().equals(AgeCategory.CHILD)
				   || ((Person) pedestrian).getAgeCategory().equals(AgeCategory.BABY))
					pedestrianCredit += IS_CHILD;
				
				if(((Person) pedestrian).getProfession().equals(Profession.CRIMINAL))
					pedestrianCredit += IS_CRIMINAL;
			}
		}
		
		return passengerCredit > pedestrianCredit
			   ? Decision.PASSENGERS 
			   : Decision.PEDESTRIANS;
	}
	
	/**
	 *   The method for interactive mode.
	 *   User input occurs here.
	 *   If readPath exits, use readCSV(filePath) method extracts scenarios from the file,
	 *   show at most 3 of them each time, keep showing when user choose to continue.
	 *   Otherwise, use ScenarioGenerator generate random scenarios, generate 3 instances
	 *   each time, repeat when user choose to continue.
	 * @param readPath The file path of custom scenarios, 
	 *                 if this is null means it needs to generate random scenarios.
	 * @param savePath The file path to save(if user allowed) the results.
	 */
	private static void iMode(String readPath, String savePath) 
	{
		// Try read CSV first if it exists
		// Otherwise create empty list for saving random scenarios
		List<Scenario> scenarios = new ArrayList<Scenario>();
	    if(readPath != null) 
	    	scenarios = Arrays.asList(readCSV(readPath)); // convert from array to arrayList
	    
	    
		/** WELCOME SECTION **/
	    // read and print welcome.ascii
		try 
		{
			BufferedReader inputStream = 
					new BufferedReader(new FileReader("welcome.ascii"));
			String line = inputStream.readLine();
			while(line != null) 
			{
				System.out.println(line);
				line = inputStream.readLine();
			}
			inputStream.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("ERROR: could not find welcome file");
			System.exit(0);
		}
		catch (IOException e) {System.exit(0);}
		
		/** CONSENT TO SAVE STAT SECTION **/
		Scanner scanner = new Scanner(System.in);
		System.out.println("Do you consent to have your decisions saved to a file? (yes/no)");
		String response = scanner.next();
		while(! response.equals("yes") && ! response.equals("no")) 
		{
			System.out.println("Invalid response. "
					+ "Do you consent to have your decisions saved to a file? (yes/no)");
			response = scanner.next();
		}
		// true: allow to save results, false: do not save results
		boolean saveResult = response.equals("yes")
				             ? true
				             : false;
		
		/** DISPLAY SCENARIOS SECTION **/
		// initialize an user audit for saving statistics
		Audit audit = new Audit();
		audit.setAuditType("User");
		
		int index = 0;   // the index of next scenario to be displayed in the list
		
		// ends when user do not continue or all custom scenarios are shown
		while(true) 
		{	
			// if there is no config file, add 3 new random scenarios
			if(readPath == null) 
			{
				ScenarioGenerator generator = new ScenarioGenerator();
				for(int i = 0; i < 3; i++) 
					scenarios.add(generator.generate());
			}
			
			// show scenario one by one, and read user decision
			// break if all scenarios are showed or 3 scenarios are showed
			while(index < scenarios.size()) 
			{
				/** DECIDE SECTION **/
				System.out.println(scenarios.get(index).toString());
				System.out.println("Who should be saved? "
						+ "(passenger(s) [1] or pedestrian(s) [2])");
				
				String decision = scanner.next();
				if(decision.equals("passenger") 
				   || decision.equals("passengers")
				   || decision.equals("1")) 
				{
					decision = "PASSENGERS";
				}
				else if(decision.equals("pedestrian") 
						|| decision.equals("pedestrians")
						|| decision.equals("2")) 
				{
					decision = "PEDESTRIANS";
				}
				
				// record on audit
				audit.singleRun(scenarios.get(index), decision);
				index++;
				
				// break if 3 scenarios have been showed
				if(index % 3 == 0)
					break;
			}
			
			// print and save(if allowed) statistics
			audit.printStatistic();
			if(saveResult)
				audit.printToFIle(savePath);
			
			// break if scenarios in the file are all read
			if(readPath != null && index == scenarios.size())
				break;
			
			/** CONTINUE SECTION **/
			System.out.println("Would you like to continue? (yes/no)");
			String choice = scanner.next();
			if(choice.equals("yes"))
				continue;
			else if(choice.equals("no"))
				break;
		}
		
		/** QUIT SECTION **/
		System.out.println("That's all. Press Enter to quit.");
		// consume previous enter key
		scanner.nextLine();
		// press enter to quit
		scanner.nextLine();
		scanner.close();
		System.exit(0);
	}
	
	/**
	 *  The method which reads CSV file with scenarios.
	 *  This method will print warn message for invalid data 
	 *  and replace invalid data with default value.
	 * @param filePath The path of the file to be read
	 * @return An array of scenarios of class Scenario,
	 *         all of them are from the file which is specified by the filePath
	 */
	private static Scenario[] readCSV(String filePath) 
	{
		// use arrayList to save first, 
		// since list is flexible, we do not know how many scenarios are in the file
		ArrayList<Scenario> scenarios = new ArrayList<Scenario>();
		
		// Exceptions for file reading
		try 
		{
			BufferedReader inputStream = 
					new BufferedReader(new FileReader(filePath));
			// consume the header
			String line = inputStream.readLine();
			// read first line 
			line = inputStream.readLine();
			// count which line is reading now, header is line 1 which has been already read
			int lineCount = 2;
			
			// passengers and pedestrians list for this scenario
			ArrayList<Character> passengers = new ArrayList<Character>();
			ArrayList<Character> pedestrians = new ArrayList<Character>();
			// if this scenario is first one in the file
			boolean first = true;
			// if this scenario is legal crossing
			boolean isLegalCrossing = true;
			
			while(line != null) 
			{
				// DataFormat Exception
				try 
				{
					// split the line to 10 attributes
					String[] characteristics = line.split(",", -1);
					// throw exception if attribute missing, catch will skip current line
					if(characteristics.length != 10) 
						throw new InvalidDataFormatException();
					
					/** CHECKING WHETHER NEW SCENARIO **/
					// check if this line is first scenario of the file
					if(characteristics[0].matches("scenario(.*)") && first)
					{
						// set isLegalCrosing for this scenario
						if(characteristics[0].substring(9).equals("red"))
							isLegalCrossing = false;
						first = false;
						
						// read next line
						line = inputStream.readLine();
						lineCount ++;
						continue;
					}
					// check if this line is new scenario
					else if(characteristics[0].matches("scenario(.*)"))
					{
						// convert Character arrayLists to arrays 
						// and add previous scenario to scenarios list
						scenarios.add
							(new Scenario(passengers.toArray(new Character[passengers.size()]), 
								          pedestrians.toArray(new Character[pedestrians.size()]), 
								          isLegalCrossing));
						
						// refresh
						isLegalCrossing = true;
						if(characteristics[0].substring(9).equals("red"))
							isLegalCrossing = false;
						
						// refresh passengers and pedestrians lists
						passengers = new ArrayList<Character>();
						pedestrians = new ArrayList<Character>();
						
						// read next line
						line = inputStream.readLine();
						lineCount ++;
						continue;
					}
					
					/** READING ATTRIBUTES **/
					// read person info
					if(characteristics[0].equals("person"))
					{
						// InvalidCharacteristicException may happen for following variables
						// If happens assign the default value, and print error message
						Gender gender;
						BodyType bodytype;
						Profession profession;
						String message = String.format("WARNING: "
								+ "invalid characteristic in config file in line %d", lineCount);
						try {gender = Gender.valueOf(characteristics[1].toUpperCase());}
						catch(IllegalArgumentException e)
						{
							gender = Gender.UNKNOWN;
							System.out.println(message);
						}
						try {bodytype = BodyType.valueOf(characteristics[3].toUpperCase());}
						catch(IllegalArgumentException e)
						{
							bodytype = BodyType.UNSPECIFIED;
							System.out.println(message);
						}
						try 
						{
							// empty Profession means NONE
							
								profession = characteristics[4].equals("")
										     ? Profession.NONE
								             : Profession.valueOf(characteristics[4].toUpperCase());
						}
						catch(IllegalArgumentException e)
						{
							profession = Profession.UNKNOWN;
							System.out.println(message);
						}
						
						// NumberFormatException may happen for the following variable
						int age;
						try {age = Integer.valueOf(characteristics[2]);}
						catch(NumberFormatException e) 
						{
							System.out.printf
							("WARNING: invalid number format in config file in line %d%n", lineCount);
							age = 0;
						}
						
						boolean pregnant = Boolean.valueOf(characteristics[5].toLowerCase());
						boolean isYou = Boolean.valueOf(characteristics[6].toLowerCase());
						String role = characteristics[9].toUpperCase();
						
						// create the instance of Person
						Person person = new Person(age, profession, gender, 
								                   bodytype, pregnant);
						person.setAsYou(isYou);
						
						// add it to the list which it belongs to
						if(role.equals("PASSENGER"))
							passengers.add(person);
						else if(role.equals("PEDESTRIAN"))
							pedestrians.add(person);
					}
					// read animal info
					else
					{
						String species = characteristics[7];
						boolean isPet = Boolean.valueOf(characteristics[8].toLowerCase());
						String role = characteristics[9].toUpperCase();
						
						// create an animal instance
						Animal animal = new Animal(species);
						animal.setPet(isPet);
						
						// add into where it belongs to
						if(role.equals("PASSENGER"))
							passengers.add(animal);
						else if(role.equals("PEDESTRIAN"))
							pedestrians.add(animal);
					}
				}
				catch(InvalidDataFormatException e)
				{
					// print error message and continue(skip current line)
					System.out.println(e.getMessage() + lineCount);
				}
				
				// read next line
				line = inputStream.readLine();
				lineCount ++;
				
				// if reading is finished, add the final scenario to scenarios list
				if(line == null)
					scenarios.add
					(new Scenario(passengers.toArray(new Character[passengers.size()]), 
			                      pedestrians.toArray(new Character[pedestrians.size()]), 
			                      isLegalCrossing));
			}
			// finish reading
			inputStream.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("ERROR: could not find config file");
			System.exit(0);
		} 
		catch (IOException e) {System.exit(0);}
		
		// convert to array and return
		return scenarios.toArray(new Scenario[scenarios.size()]);
	}
}

