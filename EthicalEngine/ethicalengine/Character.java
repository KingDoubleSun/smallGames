package ethicalengine;
/**
 * The abstract class from which all character types inherit.
 * 
 * @author Shuwei Liu, studentID: 847046
 *
 */

public abstract class Character {
	/**
	 * The enum class of Gender, with 3 values FEMALE MALE UNKNOWN
	 */
	public enum Gender {FEMALE, MALE, UNKNOWN};
	/**
	 * The enum class of BodyType, with 4 values, AVERAGE ATHLETIC OVERWEIGHT UNSPECIFIED
	 */
	public enum BodyType {AVERAGE, ATHLETIC, OVERWEIGHT, UNSPECIFIED};
	
	// default attributes
	private int age = 0;
	private Gender gender = Gender.UNKNOWN;
	private BodyType bodyType = BodyType.UNSPECIFIED;
	
	/**
	 * The empty constructor, all attributes are under default
	 */
	public Character(){}
	
	/**
	 * The constructor with all attribute to be assigned.
	 * @param age the age of the Character
	 * @param gender the gender of the Character
	 * @param bodyType the bodyType of the Character
	 */
	public Character(int age, Gender gender, BodyType bodyType) 
	{
		//invariant that age must be at least 0
		this.age = age < 0 ? 0 : age;
		this.gender = gender;
		this.bodyType = bodyType;
	}
	
	/**
	 * The copy constructor, copy an exactly same character instance.
	 * @param c Another Character to be copied.
	 */
	public Character(Character c) 
	{
		this.age = c.getAge();
		this.gender = c.getGender();
		this.bodyType = c.getBodyType();
	}
	
	/**
	 * Abstract method to be inherited
	 */
	public abstract String toString();
	
	/**
	 * returns the age as an integer
	 * @return age of this instance, as a value of integer
	 */
	public int getAge() 
	{
		return this.age;
	}
	
	/**
	 * returns the gender which is a value of enum class Gender
	 * @return the gender of this instance, as a value of enum Gender
	 */
	public Gender getGender() 
	{
		return this.gender;
	}
	
	/**
	 * returns the bodyType which is a value of enum class BodyType
	 * @return the bodyType of this instance, as a value of enum BodyType
	 */
	public BodyType getBodyType() 
	{
		return this.bodyType;
	}
	
	/**
	 * changes the age to a new value 
	 * @param age the new age to be set
	 */
	public void setAge(int age) 
	{
		this.age = age;
	}
	
	/**
	 * changes the gender to a new value
	 * @param gender the new gender to be set, which should be a value of Gender
	 */
	public void setGender(Gender gender) 
	{
		this.gender = gender;
	}
	
	/**
	 * changes the bodyType to a new value
	 * @param bodyType the new bodyType to be set, which should be a value of BodyType
	 */
	public void setBodyType(BodyType bodyType) 
	{
		this.bodyType = bodyType;
	}
}
