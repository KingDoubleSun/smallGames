package ethicalengine;

/**
 * The class which is inherited from Character, 
 * this class represents a human in the scenarios.
 * @author Shuwei Liu, studentID: 847046
 *
 */
public class Person extends Character{
	/**
	 * The enum class of Profession, total 10 values, 
	 * default is NONE for non-ADULT and UNKNOWN for ADULT
	 */
	public enum Profession{DOCTOR, CEO, CRIMINAL, HOMELESS, ENGINEER,
		                   UNEMPLOYED, POLICE, TEACHER, UNKNOWN, NONE};
    /**
     * The enum class of AgeCategory, total 4 values, 
     * to be derived depending on the age of the person
     */
	public enum AgeCategory{BABY, CHILD, ADULT, SENIOR};
	
	private Profession profession;
	private boolean isPregnant;
	private boolean isYou = false;
	
	/**
	 * The empty constructor which every attribute is under default.
	 */
	public Person() 
	{
		super();
		this.profession = Profession.NONE;
		this.isPregnant = false;
	}
	
	/**
	 * The constructor with all attribute to be assigned.
	 * @param age The age of this person
	 * @param profession The profession of this person, if applicable
	 * @param gender The gender of this person
	 * @param bodyType The bodyType of this person
	 * @param isPregnant If this person is pregnant, if applicable
	 */
	public Person(int age, Profession profession, Gender gender, 
			      BodyType bodyType, boolean isPregnant) 
	{
		super(age, gender, bodyType);
		this.profession = profession;
		// make sure only adult female may be pregnant
		this.isPregnant = (this.getGender() == Gender.FEMALE 
		                  && this.getAgeCategory() == AgeCategory.ADULT)
			              ? isPregnant
					      : false;
	}
	
	/**
	 * The constructor for person with profession and isPregnant under default.
	 * @param age The age of this person.
	 * @param gender The gender of this person.
	 * @param bodyType The bodyType of this person.
	 */
	public Person(int age, Gender gender, BodyType bodyType) 
	{
		super(age, gender, bodyType);
		this.profession = Profession.UNKNOWN;
		this.isPregnant = false;
	}

	/**
	 * The copy constructor, which copies a same person as otherPerson.
	 * @param otherPerson the Person instance to be copied.
	 */
	public Person(Person otherPerson) 
	{
		super(otherPerson);
		this.profession = otherPerson.getProfession();
		this.isPregnant = otherPerson.isPregnant();
	}
	
	/**
	 * returns an enum value of AgeCategory depending on the person's age.
	 * @return the ageCategory of this person, in an enum value of AgeCategory.
	 */
	public AgeCategory getAgeCategory() 
	{ 
		AgeCategory ageCategory;
		
		if(0 <= this.getAge() && this.getAge() <= 4)
			ageCategory = AgeCategory.BABY;
		else if(5 <= this.getAge() && this.getAge() <= 16)
			ageCategory = AgeCategory.CHILD;
		else if (17 <= this.getAge() && this.getAge() <= 68)
			ageCategory = AgeCategory.ADULT;
		else
			ageCategory = AgeCategory.SENIOR;
		
		return ageCategory;
	}
	
	/**
	 * returns an enum value of Profession, only AgeCategory.ADULT has Profession.
	 * Other AgeCategory returns NONE, ADULT with default UNKOWN.
	 * @return the profession of this person
	 */
	public Profession getProfession() 
	{
		// Make sure non-adult have no profession
		if(this.getAgeCategory() != AgeCategory.ADULT)
			return Profession.NONE;
		else 
		{
			// Make sure adults always have profession, not NONE
			if(this.profession == Profession.NONE)
				return Profession.UNKNOWN;
			else
				return this.profession;
		}
	}
	
	/**
	 * returns a boolean indicating whether the person is pregnant. 
	 * Only ADULT FEMALE may return true, other persons always return false
	 * @return a boolean indicating whether the person is pregnant
	 */
	public boolean isPregnant() 
	{
		// make sure only adult female may be pregnant
		return (this.getGender() == Gender.FEMALE
                && this.getAgeCategory() == AgeCategory.ADULT)
	            ? this.isPregnant
                : false;
	}
	
	/**
	 * sets the value returned by isPregnant() while preventing invalid states.
	 * @param pregnant a boolean indicating whether the person is pregnant.
	 */
	public void setPregnant(boolean pregnant) 
	{
		// make sure only adult female may be pregnant
		this.isPregnant = (this.getGender() == Gender.FEMALE
		                  && this.getAgeCategory() == AgeCategory.ADULT)
			              ? pregnant
		                  : false;
	}
	
	/**
	 * returns a boolean indicating whether the person 
	 * is representative of the representative of the user.
	 * @return a boolean, true: is user, false: not user.
	 */
	public boolean isYou() 
	{
		return this.isYou;
	}
	
	/**
	 * sets the value if whether the person is representative of the user.
	 * @param isYou a boolean indicating whether the person is representative of the user
	 */
	public void setAsYou(boolean isYou) 
	{
		this.isYou = isYou;
	}
	
	/**
	 * Abstract method inherited from Character, 
	 * returns the summary of the person, which includes all applicable attributes in lower cases.
	 * @return the summary of the person, which includes all applicable attributes in lower cases.
	 */
	public String toString() 
	{
		String you = "you ";
		String bodyType = this.getBodyType().toString().toLowerCase();
		String ageCategory = this.getAgeCategory().toString().toLowerCase();
		String profession = this.getProfession().toString().toLowerCase() + " ";
		String gender = this.getGender().toString().toLowerCase();
		String pregnant = " pregnant";
		
		// make inapplicable attributes to be empty string
		if(! this.isYou())
			you = "";
		if(this.getProfession() == Profession.NONE)
			profession = "";
		if(! this.isPregnant())
			pregnant = "";
		
		return you + bodyType + " " 
		       + ageCategory + " " + profession 
		       + gender + pregnant;
	}
}
