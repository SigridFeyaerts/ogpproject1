package hillbillies.model;

import java.util.Random;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class of units characterised by their name, position, weight, agility,
 * strength, toughness and default behaviour. 
 * 
 * @version 1.0
 * @author Sigrid Feyaerts, Eleanor Van Looy
 * @invar  The Name of all units must be a valid name.
 * 	   | isValidName(getName())
 * @invar  The position of all units must be inside the game world.
 * 	   | getPosition()[0]<=50 && getPostion()[0]>=0 && getPosition()[1]<=50 &&  getPosition()[1]>=0 && getPosition()[2]<=50 &&  getPosition()[2]>=0
 * @invar  The weight of all units must be greater then or equal to  half  strength of the unit increased with half it's agility and smaller then or equal to 200.
 * 	   | getWeight()>=( getStrength()+getAgility())/2 && getWeight<=200
 * @invar  The toughness of all units must be smaller then or equal 200 and greater then or equal to 1.
 * 	   | getToughness()<=200 && getToughness()>=1
 * @invar  The agility of all units must be smaller then or equal 200 and greater then or equal to 1.
 * 	   | getAgility()<=200 && getAgility()>=1
 * @invar  The strength of all units must be smaller then or equal 200 and greater then or equal to 1.
 * 	   | getStrength()<=200 && getStrength()>=1
 */

public class Unit {
	private String name;
	private int strength, weight, agility, toughness, lc = 1, hitPoints, staminaPoints;
	private double attackTime, orientation, vw, vb, vs, currentSpeed, distanceToGo, workTime, restTime, minRestTime,
			sprintedTime,timeTillRest = 3*60;
	private double[] position, targetPosition, v;
	private boolean enableDefaultBehaviour, isAttacking = false, isMoving = false, isSprinting = false,
			isWorking = false, isResting = true, inMinRestTime;

	private static final char[] validChars = new char[] { ' ', '\"', '\'' };

	/**
	 * Initialize this new unit with given name, position, weight, strength,
	 * agility, toughness and state of default behaviour.
	 * 
	 * @param name
	 *            The name of the unit.
	 * @param initialPosition
	 *            The initial position of the unit, as an array with 3 elements
	 *            {x, y, z}.
	 * @param weight
	 *            The initial weight of the unit.
	 * @param agility
	 *            The initial agility of the unit.
	 * @param strength
	 *            The initial strength of the unit.
	 * @param toughness
	 *            The initial toughness of the unit.
	 * @param enableDefaultBehaviour
	 *            Whether the default behaviour of the unit is enabled.
	 * @post If the given name is valid, the name of this new unit is the same
	 *       as the given name.
	 * @post If the given initial position is valid, the initial position of
	 *       this unit it the same as the given initial position.
	 * @post If the given weight is in the range of 25 to 100, inclusively, and
	 *       at least (strength+agility)/2, the weight of this new unit is the
	 *       same as the given weight.
	 * @post If the given agility is in the range of 25 to 100, inclusively, the
	 *       agility of this new unit is the same as the given agility.
	 * @post If the given strength is in the range of 25 to 100, inclusively,
	 *       the strength of this new unit is the same as the given strength.
	 * @post If the given toughness is in the range of 25 to 100, inclusively,
	 *       the toughness of this new unit is the same as the given toughness.
	 * @post The boolean enableDefaultBehaviour of the unit will be set at enableDefaultBehaviour
	 * @throws IllegalArgumentException
	 *         A precondition was violated or an exception was thrown.
	 */
	public Unit(String name, int[] initialPosition, int weight, int agility, int strength, int toughness,
			boolean enableDefaultBehaviour) throws IllegalArgumentException {
		// set name
		setName(name);
		// set position
		setPosition(initialPosition);
		// local variables for attributes
		int minInitialValue = 25;
		int maxInitialValue = 100;
		// set agility
		if (agility < minInitialValue)
			this.agility = minInitialValue;
		else if (agility > maxInitialValue)
			this.agility = maxInitialValue;
		else
			this.agility = agility;
		// set strength
		if (strength < minInitialValue)
			this.strength = minInitialValue;
		else if (strength > maxInitialValue)
			this.strength = maxInitialValue;
		else
			this.strength = strength;
		// set weight
		int minWeight = (int) Math.ceil((this.getStrength() + this.getAgility()) / 2.0);
		if (weight < Math.max(minWeight, minInitialValue))
			this.weight = Math.max(minWeight, minInitialValue);
		else if (weight > maxInitialValue)
			this.weight = maxInitialValue;
		else
			this.weight = weight;
		// set toughness
		if (toughness < minInitialValue)
			this.toughness = minInitialValue;
		else if (toughness > maxInitialValue)
			this.toughness = maxInitialValue;
		else
			this.toughness = toughness;
		// set enableDefaultBehaviour
		setDefaultBehaviourEnabled(enableDefaultBehaviour);
		// set initial hit points
		setCurrentHitPoints(getMaxHitPoints());
		// set initial stamina points 
		setCurrentStaminaPoints(getMaxStaminaPoints());
	}

	/* Position */
	/**
	 * Returns the initial position of the unit.
	 */
	@Basic
	public double[] getPosition() {
		return this.position;
	}

	/**
	 * Set the position of the unit to the center of the given cube.
	 * 
	 * @param initialPosition
	 *            The given initial position of the unit.
	 * @post If each number in the given array is a positive number and less
	 *       than or equal to 50,
	 */
	public void setPosition(int[] initialPosition) {
		int dim = 3;
		if (position == null)
			position = new double[dim];
		for (int i = 0; i < dim; i++) {
			this.position[i] = initialPosition[i] + lc / 2.0;
		}

	}

	/**
	 * Returns the initial position of the cube occupied by the unit.
	 * @return Returns an array which contains the position of the unit rounded down to an integer.
	 *         | int[] ==  {(int)(Math.floor(this.getPosition()[0])),(int)(Math.floor(this.getPosition()[1])),(int)(Math.floor(this.getPosition()[2]))}

	 */
	public int[] getCubeCoordinate() {
		int[] cubeCoordinate = { (int) Math.floor(position[0]), (int) Math.floor(position[1]),
				(int) Math.floor(position[2]) };
		return cubeCoordinate;
	}

	/* Name */
	/**
	 * Return the name of the unit.
	 */
	@Basic
	public String getName() {
		return this.name;
	}

	/**
	 * Check whether the given name is a valid name for all units.
	 * 
	 * @param name
	 *            The name to check.
	 * @return True if and only if the given name is at least two characters
	 *         long, starts with an uppercase letter and only uses letters (both
	 *         uppercase and lowercase), quotes (both single and double) and spaces. 
	 *          |if((name.length() >= 2) && (Character.isUpperCase(name.charAt(0)) &&
	 *			|  (for(int i=1;i<name.length();i++){ Character.isLetter(name.charAt(i)|| validCharContains(name.charAt(i)}))
	 *      	|   then true 
	 *      	| else false
	 */
	private static boolean isValidName(String name) {
		if (name.length() >= 2 && Character.isUpperCase(name.charAt(0))) {
			for (int i = 1; i < name.length(); i++) {
				char c = name.charAt(i);
				if (!(Character.isLetter(c) || validCharContains(c)))
					return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * Checks or the given character is character may be used in a name.
	 * @param 	character
	 * 			The given character to check.
	 * @return True if and only if character is a part of validChars.
	 *         | for(i=0; i<validChars.length){
	 *         |   if (validChars[i] == character)
	 *         |      then return true 
	 *         | else false 
	 *         
	 */
	private static boolean validCharContains(char character) {
		for (int i = 0; i < validChars.length; i++) {
			if (validChars[i] == character)
				return true;
		}
		return false;
	}

	/**
	 * Set the name of this unit to the given name. 
	 * is
	 * 
	 * @param name
	 *            The new name of the unit.
	 * @post The new name of this unit is equal to the given name. |
	 *       new.getName() == name
	 * @throws IllegalArgumentException
	 *             The given name is not valid for any unit. | !
	 *             isValidName(name)
	 */
	public void setName(String name) throws IllegalArgumentException {
		if (!isValidName(name))
			throw new IllegalArgumentException(name);
		this.name = name;

	}

	/* Attributes */
	/**
	 * Returns the strength of the unit.
	 */
	@Basic
	public int getStrength() {
		return this.strength;
	}

	/**
	 * Set the strength of the unit to the given strength.
	 * 
	 * @param newStrength
	 *            The new strength of the unit.
	 * @post If the given strength is in the range 1..200, the strength of this
	 *       unit is equal to the given strength. If the given strength exceeds
	 *       200, the strength of this unit is equal to 200. If the given
	 *       strength is less than 1, the strength for this unit is equal to 1.
	 *       |if ((newStrength >= 1) && (newStrength <= 200)) 
	 *       | then new.getStrength() == newStrength 
	 *       |else if (newStrength > 200) 
	 *       | then new.getStrength() == 200 
	 *       |else if (newStrength < 1) 
	 *       | then new.getStrength() == 1
	 */
	public void setStrength(int newStrength) {
		if (newStrength <= 0)
			this.strength = 1;
		else if (newStrength >= 200)
			this.strength = 200;
		else
			this.strength = newStrength;
	}

	/**
	 * Returns the weight of the unit.
	 */
	@Basic
	public int getWeight() {
		return this.weight;
	}

	/**
	 * Set the weight of the unit to the given weight.
	 * 
	 * @param newWeight
	 *       The new weight of the unit.
	 * @post If the given weight is an integer numbers with a value ranging from
	 *       1 to 200, inclusively, and at least (strength+agility)/2, the new
	 *       weight of this unit is given by the given weight.
	 * @post If the given weight is in the range ((strength+agility)/2)..200,
	 *       the weight of this unit is equal to the given weight. If the given
	 *       weight exceeds 200, the weight of this unit is equal to 200. If the
	 *       given weight is less than (strength+agility)/2, the weight for this
	 *       unit is equal to (strength+agility)/2. 
	 *       |if ((newWeight >=(getStrength()+getAgility()/2.0)) && (newWeight <= 200)) 
	 *       | then new.getWeight() == newWeight 
	 *       |else if (newWeight > 200) 
	 *       | then new.getWeight() == 200 
	 *       |else if (newWeight <(getStrength()+getAgility()/2.0)) 
	 *       | then new.getWeight() == (getStrength()+getAgility()/2.0)
	 */
	public void setWeight(int newWeight) {
		int minWeight = (int) Math.ceil((this.getStrength() + this.getAgility()) / 2.0);
		if (newWeight <= minWeight)
			this.weight = minWeight;
		else if (newWeight >= 200)
			this.weight = 200;
		else
			this.weight = newWeight;
	}

	/**
	 * Returns the agility of the unit.
	 */
	@Basic
	public int getAgility() {
		return this.agility;
	}

	/**
	 * Set the agility of the unit to the given agility.
	 * 
	 * @param newAgility
	 *            The new agility of the unit.
	 * @post If the given agility is in the range 1..200, the agility of this
	 *       unit is equal to the given agility. If the given agility exceeds
	 *       200, the agility of this unit is equal to 200. If the given agility
	 *       is less than 1, the agility for this unit is equal to 1. 
	 *       |if ((newAgility >= 1) && (newAgility <= 200)) 
	 *       | then new.getAgility() == newAgility 
	 *       |else if (newAgility > 200) 
	 *       | then new.getAgility()== 200 
	 *       |else if (newAgility < 1) 
	 *       | then new.getAgility() == 1
	 */
	public void setAgility(int newAgility) {
		if (newAgility <= 0)
			this.agility = 1;
		else if (newAgility >= 200)
			this.agility = 200;
		else
			this.agility = newAgility;
	}

	/**
	 * Returns the toughness of the unit.
	 */
	@Basic
	public int getToughness() {
		return this.toughness;
	}

	/**
	 * Set the toughness of the unit to the given toughness.
	 * 
	 * @param newToughness
	 *            The new toughness of the unit.
	 * @post If the given toughness is in the range 1..200, the toughness of
	 *       this unit is equal to the given toughness. If the given toughness
	 *       exceeds 200, the toughness of this unit is equal to 200. If the
	 *       given toughness is less than 1, the toughness for this unit is
	 *       equal to 1. 
	 *       |if ((newToughness >= 1) && (newToughness <= 200)) 
	 *       | then new.getToughness() == newToughness 
	 *       |else if (newToughness > 200) 
	 *       | then new.getToughness() == 200 
	 *       |else if (newToughness < 1)
	 *       | then new.getToughness() == 1
	 */
	public void setToughness(int newToughness) {
		if (newToughness <= 0)
			this.toughness = 1;
		else if (newToughness >= 200)
			this.toughness = 200;
		else
			this.toughness = newToughness;
	}

	/**
	 * Returns the maximum amount of hit points the unit can have.
	 * 
	 * @return  200 times the weight of the unit divided by 100 times the toughness of the unit divided by 100,
	 * 		rounded up to the next integer.
	 * 		| result == (int)Math.ceil(200*(getWeight()/100.0)*(getToughness()/100.0))
	 */
	public int getMaxHitPoints() {
		return (int) (Math.ceil(200.0 * (this.getWeight() / 100.0) * (this.getToughness() / 100.0)));
	}

	/**
	 * Returns the amount of hit points of the unit.
	 */
	@Basic
	public int getCurrentHitPoints() {
		return this.hitPoints;
	}

	/**
	 * Set the amount of hit points of this unit. 
	 * 
	 * @param newHitPoints
	 *            The new amount of hit points of this unit.
	 * @pre newHitPoints must be greater then or equal to 0 and smaller or equal to the maximum hit points the unit is able to have.
	 * 		| (newHitpoints<=0)&&(newHitPoints>=this.getMaxHitPoints())
	 * @post the hitpoints of the unit are set at the given value. 
	 * 		| new.hitPoints = newHitPoints
	 */
	private void setCurrentHitPoints(int newHitPoints){
		assert newHitPoints >=0;
		assert newHitPoints <= this.getMaxHitPoints();
		this.hitPoints = newHitPoints;
	}

	/**
	 * Returns the maximum stamina points of the unit.
	 * 
	 * @return 200*(getWeight()/100.0)*(getToughness()/100.0) rounded up to the
	 *         next integer
	 */
	public int getMaxStaminaPoints() {
		return (int) (Math.ceil(200.0 * (this.getWeight() / 100.0) * (this.getToughness() / 100.0)));
	}

	/**
	 * Returns the amount of stamina points of the unit.
	 */
	@Basic
	public int getCurrentStaminaPoints() {
		return this.staminaPoints;
	}

	/**
	 * Sets the stamina points of the unit at the given amount.
	 * 
	 * @param newStaminaPoints
	 * 		The given amount of stamina points.
	 * @pre The newStaminaPoints must be greater then or equal to 0 and smaller or equal to
	 * 		 the maximum stamina points the unit is able to have.
	 *		| (newStaminaPoints>=0 && newStaminaPoints<=this.getMaxStaminaPoints())
	 * 
	 * @post the stamina points of the unit are equal to the given amount.
	 *       | new.getStaminaPoints() =  newStaminaPoints
	 */
	private void setCurrentStaminaPoints(int newStaminaPoints){
		assert newStaminaPoints >=0;
		assert newStaminaPoints <= this.getMaxStaminaPoints();
		
		this.staminaPoints = newStaminaPoints;
	}
	/* Orientation */

	/**
	 * Return the current orientation of the unit.
	 * 
	 * @param unit
	 *            The unit for which to retrieve the orientation
	 * @return The orientation of the unit, in radians.
	 */
	@Basic
	public double getOrientation() {
		return this.orientation;
	}

	/**
	 * Sets the orientation of the unit to the angel between 0 and 2*Pi that
	 * equals the given angle.
	 * 
	 * @param newOrientation
	 *            The given angel for the unit.
	 * 
	 * @post If the given orientation is greater then or equal to 0, the
	 *       orientation of the unit is set at neworientation modulo 2 times Pi.
	 *       | if(neworientation >=0) | then new.getOrientation ==
	 *       neworientation % 2*PI
	 * @post If the given orientation is negative, the orientation of the unit
	 *       will be equal to the neworientation modulo 2 times Pi, increased
	 *       with 2 times Pi. | if(neworientation<0) | then new.getOrientation
	 *       == (neworientation % 2*PI) +2*PI
	 */
	public void setOrientation(double newOrientation) {
		newOrientation = newOrientation % (2 * Math.PI);
		while (newOrientation < 0)
			newOrientation = newOrientation + 2 * Math.PI;
		this.orientation = newOrientation;
	}

	/* Time */
	/**
	 * Advance the state of the given unit by the given time period.
	 * 
	 * @param dt
	 *            The time period, in seconds, by which to advance the unit's
	 *            state.
	 * @post If the unit is moving, the unit's position and speed gets updated
	 *       every dt seconds.
	 * @post If the unit is sprinting, the unit's position, stamina points and
	 *       speed gets updated every dt seconds.
	 * @post If the unit is working, the unit's position is given and speed is
	 *       set to zero.
	 * @post If the unit is fighting, after one second stop fighting.
	 * @throws IllegalArgumentException
	 *         A precondition was violated or an exception was thrown.
	 *         | if(!(0<dt && dt <=0.2))
	 */
	public void advanceTime(double dt) throws IllegalArgumentException {
		// correcte dt?
		if (!(0 < dt && dt <= 0.2))
			throw new IllegalArgumentException();

		setCurrentSpeed();
		if (isMoving) {
			moving(dt);
		}
		
		else if (isWorking) {
			working(dt);
		}
		
		else if(isResting){
			resting(dt);

		}
		
		else if (isAttacking) {
			attacking(dt);
		} else if(isDefaultBehaviourEnabled()){
			startDefaultBehaviour();
		}
		
		
		// resting in 3 min
		if (!isResting) {
			if (timeTillRest <= 0) {
				this.isResting = true;
				timeTillRest = 3 * 60;
			} else
				timeTillRest -= dt;

		}

	}

	/* Moving */
	/**
	 * Enable sprinting mode for this unit.
	 * 
	 * @post The unit is sprinting, isSprinting is true.
	 *       | new.isSprinting = true
	 */
	public void startSprinting() {
		if (this.isMoving && this.getCurrentStaminaPoints() > 0){
			sprintedTime=0;
			this.isSprinting = true;
		}
	}

	/**
	 * Disable sprinting mode for this unit.
	 * @post The unit stopped sprinting, isSprinting is false.
	 *       | new.isSprinting = false 
	 */
	public void stopSprinting() {
		this.isSprinting = false;
	}

	/**
	 * Return the current speed of this unit.
	 * 
	 */
	@Basic
	public double getCurrentSpeed() {
		return this.currentSpeed;
	}
	/**
	 * Sets the current speed of this unit.
	 * 
	 * @post if the unit is not moving, the speed is 0.
	 * @post if the unit is moving in the x and/or y direction the speed is vb.
	 * @post if the unit is moving in the negative z-direction, the speed is 1.2*vb.
	 * @post if the unit is moving in the negative z-direction, the speed is 0.5*vb.
	 * 
	 */
	private void setCurrentSpeed() {
		if (!(isMoving)) {
			currentSpeed = 0;
		} else {
			vb = 1.5 * (this.getStrength() + this.getAgility()) / (200.0 * this.getWeight() / 100.0);

			if ((this.getPosition()[2] < targetPosition[2]))
				vw = 0.5 * vb;
			else if ((this.getPosition()[2] > targetPosition[2]))
				vw = 1.2 * vb;
			else
				vw = vb;
			vs = 2 * vw;
			if (this.isSprinting)
				currentSpeed = vs;
			else
				currentSpeed = vw;

		}
	}

	/**
	 * Move this unit to an adjacent cube.
	 *
	 * @param dx
	 *            The amount of cubes to move in the x-direction; should be -1,
	 *            0 or 1.
	 * @param dy
	 *            The amount of cubes to move in the y-direction; should be -1,
	 *            0 or 1.
	 * @param dz
	 *            The amount of cubes to move in the z-direction; should be -1,
	 *            0 or 1.
	 * @throws IllegalArgumentException
	 *         If the coordinate of the given cube aren't in the range of the gameworld. 
	 */

	public void moveToAdjacent(int dx, int dy, int dz) throws IllegalArgumentException {
		// error if not in field

		int[]cubeCoordinates=this.getCubeCoordinate();
				if (!(0 <= cubeCoordinates[0] + dx &&cubeCoordinates[0] + dx <= 49)
				|| !(0 <= cubeCoordinates[1] + dy && cubeCoordinates[1] + dy <= 49)
				|| !(0 <= cubeCoordinates[2] + dz && cubeCoordinates[2] + dz <= 49))
			throw new IllegalArgumentException();

		// variables (targetposition,)
		this.targetPosition = new double[] { cubeCoordinates[0] + dx + lc / 2.0,
				cubeCoordinates[1] + dy + lc / 2.0, cubeCoordinates[2] + dz + lc / 2.0 };
		isMoving=true;
	}

	/**
	 * Start moving this unit to the given cube.
	 * 
	 * @param cube
	 *         The coordinate of the cube to move to, as an array of integers
	 *         {x, y, z}.
	 * @post The endTargetPosition of the unit will be set at the center of the given cube if the unit isn't working or attacking
	 * 		and isMoving will be true.
	 *       | if ((!this.isWorking) && (!this.isAttacking))
	 *       |  then new.endTargetPosition[i]  = cube[i]+lc/2.0
	 *       |  then this.isMoving = true
	 */
	public void moveTo(int[] cube) {
		isWorking=false;isResting=false;isAttacking=false;
		this.targetPosition= new double[]{cube[0]+lc/2.0,cube[1]+lc/2.0,cube[2]+lc/2.0};
		isMoving=true;
		if (enableDefaultBehaviour&&Math.random()>=0.5)
			isSprinting=true;
	}

	/**
	 * Return whether this unit is currently moving.
	 * 
	 * @return true if the unit is currently moving; false otherwise
	 */
	public boolean isMoving() {
		return isMoving;
	}

	/**
	 * Return whether this unit is currently sprinting.
	 * 
	 * @return true if the unit is currently sprinting; false otherwise
	 */
	public boolean isSprinting() {
		return isSprinting;
	}
	private void moving(double dt) {
		distanceToGo = Math.sqrt(Math.pow((targetPosition[0] - this.getPosition()[0]), 2)
				+ Math.pow((targetPosition[1] - this.getPosition()[1]), 2)
				+ Math.pow((targetPosition[2] - this.getPosition()[2]), 2));
		v = new double[] { this.getCurrentSpeed() * (targetPosition[0] - this.getPosition()[0]) / distanceToGo,
				this.getCurrentSpeed() * (targetPosition[1] - this.getPosition()[1]) / distanceToGo,
				this.getCurrentSpeed() * (targetPosition[2] - this.getPosition()[2]) / distanceToGo };
		double speed = Math.sqrt(Math.pow(v[0], 2) + Math.pow(v[1], 2) + Math.pow(v[2], 2));
		if (distanceToGo > speed * dt) {
			this.position = new double[] { this.getPosition()[0] + v[0] * dt, this.getPosition()[1] + v[1] * dt,
					this.getPosition()[2] + v[2] * dt };
			this.setOrientation(Math.atan2(v[1], v[0]));
		} else{
			this.position = this.targetPosition;
			sprintedTime=0;
			this.isMoving = false;
			isSprinting=false;
		}
		if (isSprinting) {
			sprintedTime += dt;
			while (sprintedTime >= 0.1) {
				sprintedTime -= 0.1;
				if (this.getCurrentStaminaPoints() <= 1) {
					this.setCurrentStaminaPoints(0);
					this.isSprinting = false;
					sprintedTime=0;
				} else
					this.setCurrentStaminaPoints(this.getCurrentStaminaPoints() - 1);
			}
		}
	}


	/* Attacking */
	/**
	 * Makes the unit attack the given unit(defender).
	 * @param defender
	 *      The unit that should be attacked.
	 * @post The orientation of both units will be set facing each other.
	 *       
	 * @post If the attacked unit was unable to defend itself its hit points will be lowered.
	 * 
	 * @post IsAttacking will be set to true.
	 * 
	 */
	public void attack(Unit defender) {
		isMoving=false;isWorking=false;isResting=false;
		if (!isAdjacent(defender) || defender==this)
			return ;		
		this.getCubeCoordinate();
		attackTime = 1.00;
		double thetaA = Math.atan2((defender.position[1] - this.position[1]),
				(defender.position[0] - this.position[0]));
		double thetaD = Math.atan2((this.position[1] - defender.position[1]),
				(this.position[0] - defender.position[0]));
		this.setOrientation(thetaA);
		defender.setOrientation(thetaD);
		isAttacking = true;
		//
		if (defender.defended(this)) {
			return;
		} else {
			defender.takeDamage(this);
		}

	}
	/**
	 * Checks if a different unit is adjacent to this unit
	 * @param unit
	 * 		the unit to check if it is adjacent to this unit.
	 * @return true if the given unit is adjacent to this unit, otherwise false.
	 */
	private boolean isAdjacent(Unit unit){
		int[] cubeCoordinate = this.getCubeCoordinate();
		int[] otherCubeCoordinate = unit.getCubeCoordinate();

		for(int i=0;i<cubeCoordinate.length;i++){
			if (Math.abs(cubeCoordinate[i]- otherCubeCoordinate[i])>1)
				return false;
		}
		// If x,y,z of other cube are within range -1...1, ok, otherwise, not OK
		return true;
	}
	/**
	 * Make the unit defend itself from the given unit.
	 * 
	 * @param attacker
	 * @return true if success of the probability that dodging succeeds is true or success of the probability of Blocking is true, otherwise if both fail false will be returned.
	 * 	   | if (success(0.2*(this.getAgility() /attacker.getAgility())))
	 *         |  then true 
	 *	   | if (success( 0.25 * ((this.getStrength() + this.getAgility()) / (attacker.getStrength() + attacker.getAgility()))))
	 * 	   |  then true 
	 * 	   | else false
	 */
	private boolean defended(Unit attacker) {
		isResting=false;
		// dodge
		double probabilityDodge = 0.20 * (this.getAgility() / attacker.getAgility());
		if (success(probabilityDodge)) {
			Random randomGenerator = new Random();
			this.moveTo(new int[]{randomGenerator.nextInt(49),randomGenerator.nextInt(49),randomGenerator.nextInt(49)});
			return true;

		}
		// block
		double probabilityBlock = 0.25* ((this.getStrength() + this.getAgility()) / (attacker.getStrength() + attacker.getAgility()));
		if (success(probabilityBlock))
			return true;

		return false;
	}
	/**
	 * Returns or the probability is high enough to succeed.
	 * @param probability
	 *        The given probability 
	 * @return Returns true if the given probability is higher then a random double between 0 and 1.
	 * 	  | (probability>Math.random())
	 */
	private boolean success(double probability) {
		return probability > Math.random();

	}
	/**
	 * decreases the hit points of the unit according to the strength of the given strength.
	 * @param attacker
	 *        The unit that attacks this unit.
	 * @post The hit points of the unit will be decreased by the strenth of the attacker divided by 10.
	 *       | new.getCurrentHitPoints() = this.getCurrentHitPoints() - attacker.getStrength()/10
	 */
	private void takeDamage(Unit attacker) {
		double damage = attacker.getStrength() / 10.0;

		if ((hitPoints - damage)>0)
			this.setCurrentHitPoints((int) (hitPoints - damage));
		else
			this.setCurrentHitPoints(0);
	}

	/**
	 * Return whether this unit is currently attacking another unit.
	 * 
	 * @return true if the unit is currently attacking another unit; false
	 *         otherwise.
	 */
	public boolean isAttacking() {
		return isAttacking;
	}
	/**
	 * Decreases the attack time.
	 * @param  	dt
	 * 			The given time.
	 */
	private void attacking(double dt) {
		attackTime -= dt;
		if (attackTime <= 0)
			isAttacking = false;
	}
	/* Working */

	/**
	 * Make the given unit start working.
	 * @post If the unit isn't attacking or moving, the boolean isWorking will be set to true 
	 * 		and workTime will be set at 500 divided by the strength of the unit.
	 * 		| if((!this.isMoving)&&(!this.isAttacking)
	 * 		|  then new.isWorking = true
	 *		|       new.workTime = 500/(double)this.getStrength()
	 *
	 */
	public void work() {
		if ((!this.isMoving) && (!this.isAttacking)) {
			isMoving=false;isResting=false;isAttacking=false;
			this.isWorking = true;
			this.workTime = 500 / (double) this.getStrength();
		}
	}

	/**
	 * Return whether the given unit is currently working.
	 * 
	 * @return true if the unit is currently working; false otherwise
	 */
	public boolean isWorking() {
		return isWorking;
	}
	/**
	 * decreases the working time every dt
	 * @param dt
	 * 			the given time
	 */
	private void working(double dt) {
		if ((this.workTime - dt) > 0) {
			this.workTime = this.workTime - dt;
		} else {
			this.isWorking = false;
			this.workTime = 0;
		}
	}
	/* Resting */

	/**
	 * Make this unit rest.
	 * @post If the unit isn't attacking, the booleans isResting and inMinRestTime will be set to true and
	 * 		minRestTime will be set to the time the unit needs to recover 1 hit point.
	 *      | if(!this.isAttacking)
	 *      |  then this.isResting = true
	 *      |       this.inMinRestTime =true
	 *      |       this.minRestTime = 1/(this.getToughness()/200.0)*0.2
	 */
	public void rest() {
		if (!this.isAttacking) {
			isMoving=false;isSprinting=false;isWorking=false;isAttacking=false;
			this.isResting = true;
			this.inMinRestTime = true;
			minRestTime = 1 / (this.getToughness() / 200.0) * 0.2;
		}
	}

	/**
	 * Return whether this unit is currently resting.
	 * 
	 * @return true if the unit is currently resting; false otherwise
	 */
	public boolean isResting() {
		return isResting;
	}
	/**
	 * adds hitpoint and staminapoint when resting
	 * @param dt
	 * 		The given time.
	 */
	private void resting(double dt) {
		if (this.inMinRestTime) {
			if ((minRestTime - dt) <= 0) {
				this.inMinRestTime = false;
				minRestTime = 0;
			}

			else
				minRestTime -= dt;

		}

		restTime += dt;

		if (this.getCurrentHitPoints() == this.getMaxHitPoints()
				&& this.getCurrentStaminaPoints() == this.getMaxStaminaPoints()) {
			if (this.enableDefaultBehaviour)
				startDefaultBehaviour();
			this.isResting = false;
			restTime = 0.0;
		} else if (this.getCurrentHitPoints() < this.getMaxHitPoints()) {
			double timeToRecoverHitPoint = (1 / (this.getToughness() / 200.0) * 0.2);

			if (restTime > timeToRecoverHitPoint) {
				this.setCurrentHitPoints(this.getCurrentHitPoints() + 1);
				restTime -= timeToRecoverHitPoint;
			}

		} else if (this.getCurrentStaminaPoints() < this.getMaxStaminaPoints()) {
			double timeToRecoverStaminaPoint = (1 / (this.getToughness() / 100.0) * 0.2);
			if (restTime > timeToRecoverStaminaPoint) {
				this.setCurrentStaminaPoints(this.getCurrentStaminaPoints() + 1);
				restTime -= timeToRecoverStaminaPoint;
			}
		}
	}

	/* Default behaviour */
	/**
	 * Returns whether the default behaviour is enabled or not. The default
	 * behaviour consists of choosing activities at random. The activity can be
	 * (a) move to a random position within the game world, (b) conduct a work
	 * task, or (c) rest until it has fully recovered hit points and stamina
	 * points.
	 * 
	 * @return true if the default behaviour is enabled; false otherwise
	 */
	@Basic
	public boolean isDefaultBehaviourEnabled() {
		return enableDefaultBehaviour;
	}

	/**
	 * Set the default behaviour of this unit according to the given flag.
	 * 
	 * @param enableDefaultBehaviour
	 *            The new default behaviour state for this unit: true if the
	 *            default behaviour should be enabled; false otherwise
	 * @post The new default behaviour state of this unit is equal to the given
	 *       flag. | new.isDefaultBehaviourEnabled() = enableDefaultBehaviour
	 */
	public void setDefaultBehaviourEnabled(boolean enableDefaultBehaviour) {
		this.enableDefaultBehaviour = enableDefaultBehaviour;
		if (enableDefaultBehaviour)
			startDefaultBehaviour();
	}

	/**
	 * Start the default behaviour.
	 * 
	 * @effect The default behaviour state is set to true. 
	 *         |new.isDefaultBehaviourEnabled() = true
	 */
	private void startDefaultBehaviour() {
		this.enableDefaultBehaviour = true;
		Random randomGenerator = new Random();
		int randomBehaviour =randomGenerator.nextInt(3);
		if (randomBehaviour==0)
			rest();
		else if (randomBehaviour==1){
			moveTo(new int[]{randomGenerator.nextInt(49),randomGenerator.nextInt(49),randomGenerator.nextInt(49)});
		}
		else if (randomBehaviour==2)
			work();
		
	}

	/**
	 * Stop the default behaviour.
	 * 
	 * @effect The default behaviour state is set to false. |
	 *         new.isDefaultBehaviourEnabled() = false
	 */
	private void stopDefaultBehaviour() {
		this.enableDefaultBehaviour = false;
	}
}
