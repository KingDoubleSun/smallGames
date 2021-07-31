package ethicalengine;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class is the basis of simulation 
 * and shall be used to create a variety of scenarios.
 * @author Shuwei Liu, studentID: 847046
 *
 */
public class ScenarioGenerator {
	// a random number generator
	private Random rnd = new Random();
	// default attributes for the amount to generate
	private int passengerCountMinimum = 1;
	private int passengerCountMaximum = 5;
	private int pedestrianCountMinimum = 1;
	private int pedestrianCountMaximum = 5;
	
	/**
	 * the empty constructor, the random generator is truly random.
	 */
	public ScenarioGenerator(){}
	
	/**
	 * the constructor with seed is set
	 * @param seed a long predefined value for random generator to set.
	 */
	public ScenarioGenerator(long seed) 
	{
		this.rnd.setSeed(seed);
	}
	
	/**
	 * the constructor sets the seed as well as the minimum and maximum number 
	 * for both passengers and pedestrians with predefined values. 
	 * If minimum > maximum, minimum will be default 1
	 * If maximum < 1, maximum will be default 5
	 * @param seed a long predefined value for random generator to set.
	 * @param passengerCountMinimum the minimum integer value to create passengers.
	 * @param passengerCountMaximum the maximum integer value to create passengers.
	 * @param pedestrianCountMinimum the minimum integer value to create pedestrians.
	 * @param pedestrianCountMaximum the maximum integer value to create pedestrians.
	 */
	public ScenarioGenerator(long seed, int passengerCountMinimum, int passengerCountMaximum, 
			                 int pedestrianCountMinimum, int pedestrianCountMaximum) 
	{
		this.rnd.setSeed(seed);
		// make sure minimum <= maximum
		this.passengerCountMinimum = passengerCountMinimum <= passengerCountMaximum
				                     ? passengerCountMinimum
				                     : 1;
		// make sure maximum always >= minimum
		this.passengerCountMaximum = passengerCountMaximum >= 1
		                             ? passengerCountMaximum
		                             : 5;
		// make sure minimum <= maximum
		this.pedestrianCountMinimum = pedestrianCountMinimum <= pedestrianCountMaximum
				                      ? pedestrianCountMinimum
				                      : 1;
		// make sure maximum always >= minimum
		this.pedestrianCountMaximum = pedestrianCountMaximum >= 1
				                      ? passengerCountMaximum
				                      : 5;
	}
	
	/**
	 * sets the minimum number of car passengers for each scenario
	 * if new min > max, this will stay unchanged.
	 * @param min an integer number of car passengers at least be
	 */
	public void setPassengerCountMin(int min) 
	{
		// make sure minimum <= maximum
		this.passengerCountMinimum = min <= this.passengerCountMaximum
						             ? min
						             : this.passengerCountMinimum;
	}
	
	/**
	 * sets the maximum number of car passengers for each scenario
	 * if new max < min, this will stay unchanged.
	 * @param max an integer number of car passengers at most be.
	 */
	public void setPassengerCountMax(int max) 
	{
		// make sure minimum <= maximum
		this.passengerCountMaximum = max >= this.passengerCountMinimum
								     ? max
								     : this.passengerCountMaximum;
	}
	
	/**
	 * sets the minimum number of pedestrians for each scenario
	 * if new min > max, this will stay unchanged.
	 * @param min an integer number of pedestrians at least be
	 */
	public void setPedestrianCountMin(int min) 
	{
		// make sure minimum <= maximum
		this.pedestrianCountMinimum = min <= this.pedestrianCountMinimum
								      ? min
								      : this.pedestrianCountMinimum;
	}
	
	/**
	 * sets the maximum number of pedestrians for each scenario
	 * if new max < min, this will stay unchanged.
	 * @param max an integer number of pedestrians at most be.
	 */
	public void setPedestrianCountMax(int max) 
	{
		// make sure minimum <= maximum
		this.pedestrianCountMaximum = max >= this.pedestrianCountMaximum
									  ? max
								      : this.pedestrianCountMaximum;
	}
	
	/**
	 * returns a newly created instance of Person 
	 * with random age, gender, bodyType, profession and state of pregnancy.
	 * @return a totally random instance of Person.
	 */
	public Person getRandomPerson() 
	{
		// max age assumption
		final int MAX_AGE = 80;
		// get random age
		int age = rnd.nextInt(MAX_AGE);
		// get random index of class Profession
		int professionIndex = rnd.nextInt(Person.Profession.values().length);
		// get random index of class Gender
		int genderIndex = rnd.nextInt(Character.Gender.values().length);
		// get random index of class BodyType
		int bodytypeIndex = rnd.nextInt(Character.BodyType.values().length);
		// get random state of pregnancy
		boolean isPregnant = rnd.nextBoolean();
		
		// create and return random person instance
		return new Person(age, Person.Profession.values()[professionIndex], 
				               Character.Gender.values()[genderIndex],
				               Character.BodyType.values()[bodytypeIndex],
				               isPregnant);
	}
	
	/**
	 * returns a newly created instance of Animal
	 * with random species and whether it is pet or not and default age, gender, bodyType,
	 * since age, gender, bodyType is not essential for the animal.
	 * @return a random instance of Animal.
	 */
	public Animal getRandomAnimal()
	{
		int speciesIndex = rnd.nextInt(Animal.Species.values().length);
		boolean isPet = rnd.nextBoolean();
		Animal animal = new Animal(Animal.Species.values()[speciesIndex].toString());
		animal.setPet(isPet);
		return animal;
	}
	
	/**
	 * returns a newly created instance of Scenario containing \
	 * a random number of passengers and pedestrians with random characteristics 
	 * as well as randomly red or green light condition 
	 * with you(the user) being either in the car, on the street, or absent.
	 * @return
	 */
	public Scenario generate() 
	{
		// random amount of passengers, derived by min and max
		int passengerAmount = rnd.nextInt(passengerCountMaximum - passengerCountMinimum + 1) 
				              + passengerCountMinimum;
		// random amount of pedestrians, derived by min and max
		int pedestrianAmount = rnd.nextInt(pedestrianCountMaximum - pedestrianCountMinimum + 1) 
				               + pedestrianCountMinimum;
		// empty list to store passengers
		ArrayList<Character> passengers = new ArrayList<Character>();
		// empty list to store pedestrians
		ArrayList<Character> pedestrians = new ArrayList<Character>();
		//randomly generate whether pedestrians are legal crossing
		boolean isLegalCrossing = rnd.nextBoolean();
		
		//randomly add characters to passenger arrayList
		for(int i = 0; i < passengerAmount; i++) 
		{
			// 1/10 probability to add an Animal, 9/10 probability to add a Person
			boolean isAnimal = rnd.nextInt(10) == 0;
			if(isAnimal)
				passengers.add(getRandomAnimal());
			else
				passengers.add(getRandomPerson());
		}
		
		//randomly add characters to pedestrian arrayList
		for(int i = 0; i < pedestrianAmount; i++) 
		{
			// 1/10 probability to add an Animal, 9/10 probability to add a Person
			boolean isAnimal = rnd.nextInt(10) == 0;
			if(isAnimal)
				pedestrians.add(getRandomAnimal());
			else
				pedestrians.add(getRandomPerson());
		}
		
		// check both if passengers and pedestrians contains person
		// avoid no person can be set as you and program run into endless loop.
		boolean passengerHavePerson = false;
		boolean pedestrianHavePerson = false;
		for(Character p: passengers) 
		{
			if(p instanceof Person)
			{
				passengerHavePerson = true;
				break;
			}
		}
		for(Character p: pedestrians)
		{
			if(p instanceof Person)
			{
				pedestrianHavePerson = true;
				break;
			}
		}
		
		// Randomly set a Person be you
		int you = rnd.nextInt(3); // 0 absent, 1 in car, 2 on the street
		boolean setYou = false;   // if you has been set
		// if you in car
		if(you == 1)
			// break when one person is set as you
			while(! setYou && passengerHavePerson) 
			{
				// use mutable attribute of Character, 
				// set a random person from passengers or pedestrians be you
				Character randomPassenger = passengers.get(rnd.nextInt(passengers.size()));
				if(randomPassenger instanceof Person)
				{
					((Person) randomPassenger).setAsYou(true);
					setYou = true;
				}
			}
		// if you in lane
		else if(you == 2)
			while(! setYou && pedestrianHavePerson) 
			{
				// same technique
				Character randomPedestrian = pedestrians.get(rnd.nextInt(pedestrians.size()));
				if(randomPedestrian instanceof Person)
				{
					((Person) randomPedestrian).setAsYou(true);
					setYou = true;
				}
			}
			
		// convert ArrayLists of passenger and pedestrian to Arrays
		Character[] p1 = passengers.toArray(new Character[passengers.size()]);
		Character[] p2 = pedestrians.toArray(new Character[pedestrians.size()]);
		
		return new Scenario(p1, p2, isLegalCrossing);
	}
}
