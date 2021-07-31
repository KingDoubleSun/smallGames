import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import ethicalengine.Person;
import ethicalengine.Scenario;
import ethicalengine.ScenarioGenerator;
import ethicalengine.Animal;
import ethicalengine.Character;
/**
 * This is the class of Audit, where scenarios are decided and statistics
 * are recorded, calculated and printed, saved.
 * 
 * @author Shuwei Liu, studentID: 847046
 */
public class Audit {
	
	private String auditType = "Unspecified";  // audit name
	private int runs = 0;                      // total runs have been run
	private int survivalTotalAge = 0;          // total age of Person survived of all runs
	private int survivedPersons = 0;           // total amount of survived Person of all runs
	private Scenario[] scenarios;              // custom scenarios from file
	
	/*** record statistics ***/
	// key: characteristics, value:[TotalAmount, survivedAmount]
	private HashMap<String, int[]> statistics = new HashMap<String, int[]>();

	
	/**
	 * empty constructor, everything in default;
	 */
	public Audit() {}
	
	/**
	 * Constructor which allow custom scenarios
	 * @param scenarios custom scenarios in Class of Scenario, should be read from a file
	 */
	public Audit(Scenario[] scenarios) 
	{
		this.scenarios = scenarios;
	}
	
	/**
	 * The method for the audit with custom scenarios.
	 * Use Decide to make decision for each scenario and
	 * use singleRun() to record statistics
	 */
	public void run() 
	{
		for(Scenario s: scenarios) 
		{
			String decision = EthicalEngine.decide(s).toString();
			singleRun(s, decision);
		}
	}
	
	/**
	 * The method can be used for all types of audit.
	 * Generate random scenario by ScenasrioGenerator, make decision by Decide,
	 * and use singleRun to record statistics of it,
	 * repeat for number of runs.
	 * @param runs how many times to generate random scenario and make decision.
	 */
	public void run(int runs) 
	{
		ScenarioGenerator generator = new ScenarioGenerator();
		for(int i = 0; i < runs; i++) 
		{
			Scenario scenario = generator.generate();
			String decision = EthicalEngine.decide(scenario).toString();
			singleRun(scenario, decision);
		}
	}
	
	/**
	 * This methods record statistics of a single scenario to this.statistics
	 * @param scenario the detail of decided scenario in Class of Scenario
	 * @param decision the final decision(who survive) for this scenario
	 */
	public void singleRun(Scenario scenario, String decision) 
	{
		Character[] passengers = scenario.getPassengers();
		Character[] pedestrians = scenario.getPedestrians();
		// characteristics as green or red
		String light = scenario.isLegalCrossing()
			           ? "green"
			           : "red";
		
		// check characteristics of each passenger
		for(Character passenger: passengers) 
		{
			String[] characteristics;
			// AS Person use toString and add person and light to split to an array
			if(passenger instanceof Person)
				characteristics = (passenger.toString() + " person" + " " + light).split(" ");
			// AS Animal, if pet add species, animal, pet to array, otherwise no pet.
			else 
			    characteristics = ((Animal) passenger).isPet() 
			                      ? new String[]{((Animal) passenger).getSpecies(), "animal", "pet", light}
			                      : new String[]{((Animal) passenger).getSpecies(), "animal", light};
			
			// make recording for each characteristic
			for(String c: characteristics) 
			{
				if(statistics.containsKey(c))
					statistics.get(c)[0] += 1;
				else
					statistics.put(c, new int[] {1, 0});
				
				if(decision.equals("PASSENGERS"))
					statistics.get(c)[1] += 1;
			}
			
			// add age statistics
			if(decision.equals("PASSENGERS") && passenger instanceof Person)
			{
				this.survivalTotalAge += passenger.getAge();
			    this.survivedPersons += 1;
			}
		}
		
		// same as above for pedestrians
		for(Character pedestrian: pedestrians) 
		{
			String[] characteristics;
			if(pedestrian instanceof Person)
				characteristics = (pedestrian.toString() + " person" + " " + light).split(" ");
			else 
			    characteristics = ((Animal) pedestrian).isPet() 
			                      ? new String[]{((Animal) pedestrian).getSpecies(), "animal", "pet", light}
			                      : new String[]{((Animal) pedestrian).getSpecies(), "animal", light};
			
			for(String c: characteristics) 
			{
				if(statistics.containsKey(c))
					statistics.get(c)[0] += 1;
				else
					statistics.put(c, new int[] {1, 0});
				
				if(decision.equals("PEDESTRIANS"))
					statistics.get(c)[1] += 1;
			}
			if(decision.equals("PEDESTRIANS") && pedestrian instanceof Person)
			{
				this.survivalTotalAge += pedestrian.getAge();
			    this.survivedPersons += 1;
			}
		}
		// this scenario finish, accumulate runs by 1
		this.runs += 1;
	}
	
	/**
	 * Sets the name of the audit type
	 * @param name new name for this audit
	 */
	public void setAuditType(String name) 
	{
		this.auditType = name;
	}
	
	/**
	 * returns the name of the audit
	 * @return the name of this audit
	 */
	public String getAuditType() 
	{
		return this.auditType;
	}
	
	
	/**
	 * returns certain format of the summary.
	 * @return a summary of the simulation, if no simulation has been run, 
	 *         returns "no audit available"
	 */
	public String toString() 
	{
		// if no simulation has been run
		if(statistics.isEmpty())
			return "no audit available";
		
		// header
		String header = "";
		header += "======================================";
		header += "\n# " + this.auditType +  " Audit\n";
		header += "======================================";
		
		// calculate and save result for each characteristic
		ArrayList<String[]> results = new ArrayList<String[]>();
		for(String i: statistics.keySet()) 
		{
			double survivalRate = (double) statistics.get(i)[1] / statistics.get(i)[0];
			// each result, a String array[0] = characteristic name, [1] = survival rate
			results.add(new String[] {i, Double.toString(survivalRate)});
		}
		// sort descending by survival rate
		results.sort((String[] result1, String[] result2) 
				      -> result2[1].compareTo(result1[1]));
		
		// result of all characteristics
		String result = "";
		for(String[] i: results) 
		{
			result += "\n" + i[0] + ": " + i[1].substring(0, 3);
		}
		
		return header 
			   + "\n- % SAVED AFTER " + runs + " RUNS" 
			   + result
			   + "\n--"
			   + "\naverage age: " + 
			     Math.floor((double) this.survivalTotalAge / this.survivedPersons * 10) / 10;
	}
	
	/**
	 * prints the summary returned by the toSrting method to the command line
	 */
	public void printStatistic() 
	{
		System.out.println(this.toString());
	}
	
	/**
	 * prints the results of the toString to a target file.
	 * if file exits, append the result. Otherwise, create the file and print.
	 * @param filepath the file path for the file to save the result
	 */
	public void printToFIle(String filepath) 
	{
		PrintWriter outputStream = null;
		try 
		{
			outputStream = new PrintWriter(new FileOutputStream(filepath, true));
		}
		catch(FileNotFoundException e) 
		{
			System.out.println("ERROR: "
					+ "could not print results. Target directory doese not exist.");
			System.exit(0);
		}
		outputStream.println(this.toString());
		outputStream.close();
	}
}
