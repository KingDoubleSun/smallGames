package ethicalengine;

/**
 * The class which is inherited from Character,
 * this class represents animals in the scenarios.
 * @author Shuwei Liu, studentID: 847046
 *
 */
public class Animal extends Character{
	/**
	 * The enum class of 4 species, 
	 * this class will be used to generate random animal.
	 */
	enum Species{BIRD, CAT, DOG, PIG};
	
	// default attributes
	private String species = "unspecified";
	private boolean isPet = false;
	
	/**
	 * The empty constructor, all attributes under default.
	 */
	public Animal() 
	{
		super();
	}
	
	/**
	 * The constructor to create a animal as a specified species.
	 * @param species the name of the species.
	 */
	public Animal(String species) 
	{
		this.species = species;
	}
	
	/**
	 * The copy constructor to copy a same Animal as otherAnimal.
	 * @param otherAnimal the instance of an Animal to be copied.
	 */
	public Animal(Animal otherAnimal) 
	{
		this.species = otherAnimal.getSpecies();
		this.isPet = otherAnimal.isPet();
	}
	
	/**
	 * returns a String indicating what types of species the animal represents.
	 * @return a String indicating what types of species the animal represents.
	 */
	public String getSpecies() 
	{
		return this.species.toLowerCase();
	}
	
	/**
	 * sets the value returned by isPet().
	 * @param species the new species type of the animal to be set.
	 */
	public void setSpecies(String species) 
	{
		this.species = species;
	}
	
	/**
	 * returns a boolean value depending whether the animal is a pet or wild animal.
	 * @return a boolean indicating whether the animal is a pet or wild animal.
	 */
	public boolean isPet() 
	{
		return this.isPet;
	}
	
	/**
	 * sets the value returned by isPet()
	 * @param isPet a boolean indicating whether the animal is a pet or wild animal.
	 */
	public void setPet(boolean isPet) 
	{
		this.isPet = isPet;
	}

	/**
	 * Abstract method inherited from Character, 
	 * returns the summary of the animal, which includes all applicable attributes in lower cases.
	 * @return the summary of the animal, which includes all applicable attributes in lower cases.
	 */
	public String toString() {
		String isPet = " is pet";
		if(! this.isPet())
			isPet = "";
		return this.getSpecies() + isPet;
	}
}
