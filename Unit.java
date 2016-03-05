package hillbillies.model;

import be.kuleuven.cs.som.annotate.*;

import java.util.Arrays.*;

import ogp.framework.util.ModelException;

/**
 * A class of units characterised by their name, position,
 * weight, agility, strength, toughness and default behaviour. TODO...(limits,...)
 * 
 * @version 1.0
 * @author Sigrid Feyaerts, Eleanor Van Looy
 */
 
public class Unit {
	private String name;
	private int strength,weight,agility,toughness,lc = 1,hitPoints,staminaPoints;
	private double attackTime,orientation,vw,vb,vs,currentSpeed,distanceToGo,workTime,restTime,minRestTime,sprintedTime;
	private double[] position,targetPosition,v,endTargetPosition;
	private boolean enableDefaultBehaviour, isAttacking=false,isMoving=false,isSprinting=false,isWorking=false,isResting=true,inMinRestTime;;
    
    
	private static final char [] validChars = new char[]{' ','\"','\''};
	
	/**
	 * Initialize this new unit with given name, position, weight, strength, 
	 * agility, toughness and state of default behaviour. 
	 * 
	 * @param 	name
	 * 		The name of the unit.
	 * @param 	initialPosition
	 * 		The initial position of the unit, as an array with 3 elements
	 *          	{x, y, z}.
	 * @param 	weight
	 * 		The initial weight of the unit.
	 * @param 	agility
	 * 		The initial agility of the unit.
	 * @param 	strength
	 * 		The initial strength of the unit.
	 * @param 	toughness
	 * 		The initial toughness of the unit.
	 * @param 	enableDefaultBehaviour
	 * 		Whether the default behaviour of the unit is enabled.
	 * @post	If the given name is valid, the name of this new unit is the same as the given name.
	 * @post	If the given initial position is valid, the initial position of this unit it the same 
	 * 		as the given initial position.
	 * @post	If the given weight is in the range of 25 to 100, inclusively, and at least 
	 * 		(strength+agility)/2, the weight of this new unit is the same as the given weight.
	 * @post	If the given agility is in the range of 25 to 100, inclusively, the agility of this new
	 * 		unit is the same as the given agility.
	 * @post	If the given strength is in the range of 25 to 100, inclusively, the strength of this new
	 * 		unit is the same as the given strength.
	 * @post	If the given toughness is in the range of 25 to 100, inclusively, the toughness of this new
	 * 		unit is the same as the given toughness.
	 * @post	enableDefaultBehaviour TODO
	 * @throws 	IllegalArgumentException
	 *      	A precondition was violated or an exception was thrown.
	 */
	public Unit(String name, int[] initialPosition, int weight, int agility, int strength, int toughness,
			boolean enableDefaultBehaviour) throws IllegalArgumentException{
		 
		int minInitialValue = 25;
		int maxInitialValue = 100;
		setName(name);
		setPosition(initialPosition);
		
		if (agility < minInitialValue)
			agility = minInitialValue;
		if (agility > maxInitialValue)
			agility = maxInitialValue;
		setAgility(agility);		
		if (strength < minInitialValue)
			strength = minInitialValue;
		if (strength > maxInitialValue)
			strength = maxInitialValue;
		setStrength(strength);
		if (toughness < minInitialValue)
			toughness = minInitialValue;
		if (toughness > maxInitialValue)
			toughness = maxInitialValue;
		setToughness(toughness);
		setWeight(weight);
		setEnableDefaultBehaviour(enableDefaultBehaviour);
		
		
	}
	/**
	 * Return the name of the unit.
	 */
	@Basic
	public String getName(){
		return this.name;
	
	}
	/**
	 * Check whether the given name is a valid name for all units.
	 * @param 	name
	 * 		The name to check.
	 * @return	True if and only if the given name is at least two characters long, starts with an uppercase letter and only uses
	 * 		letters (both uppercase and lowercase), quotes (both single and double) and spaces.
	 * 		|...TODO
	 */
	public static boolean isValidName(String name){
		if(name.length()>=2 && Character.isUpperCase(name.charAt(0))){
			for(int i=1;i<name.length();i++){
				char c = name.charAt(i);
				if(!(Character.isLetter(c) || validCharContains(c)))
					return false;
			}
			return true;
		}
		return false;
	}
	/**
	 * TODO
	 * @param character
	 * @return
	 */
	public static boolean validCharContains(char character){
		for(int i =0;i<validChars.length;i++){
			if(validChars[i]==character) return true;
		}
		return false;
	}
	/**
	 * Set the name of this unit to the given name.
	 * 
	 * @param 	name
	 * 		The new name of the unit.
	 * @post	The new name of this unit is equal to the given name.
	 * 		| new.getName() == name
	 * @throws 	ModelException
	 * 		The given name is not valid for any unit.
	 * 		| ! isValidName(name)
	 */
	public void setName(String newname)throws IllegalArgumentException{
		if (!isValidName(name))
			throw new IllegalArgumentException(name);
		this.name = name;
		
	}
	/**
	 * Returns the initial position of the unit.
	 */
	@Basic
	public double[] getPosition(){
		return this.position;		
	}
	/**
	 * Returns the initial position of the cube occupied by the unit.
	 */
	 @Basic
	public int [] getCubeCoordinate() {
		int newcoordinates[]= {(int)(Math.floor(this.getPosition()[0])),(int)(Math.floor(this.getPosition()[1])),(int)(Math.floor(this.getPosition()[2]))};
		return newcoordinates;
		
	}
	/**
	 * set the position of the unit to the center of the given cube.
	 * @param initialPosition
	 * 
	 * @post the position of the unit is the center of the cube with coordinate intitialPosition.
	 *       | for (int i=0;i<3;i++)
	 *       |     new.position[i] = initialPositon[i]+lc/2.0
	 */
	private void setPosition(int[]initialPosition){
		int dim = 3;
		for(int i=0; i<dim; i++){
			 this.position[i]= initialPosition[i]+ lc/2.0;
		}
				
	}
	/**
	 * Returns the strength of the unit.
	 */
	@Basic
	public int getStrength() {
		return this.strength;
		
	}
	/**
	 * Set the strength of the unit to the given strength.
	 * @param 	newstrength 
	 * 		The new strength of the unit.
	 * @post If the given strength is an integer number with a value less than or equal to 0, the new strength of this 
	 * 	unit is given by 1.
	 *       | if(newstrength <=0) 
	 *       |  then  new.getStrength ==1
	 * @post If the given strength is an integer number with a value greater than 200, the new strength of this 
	 * 	 unit is given by 200.
	 *       | if(newstength >=200)
	 *       |  then new.getStrength ==200
	 * @post If the given strength is an integer number with a value ranging from 1 to 200, inclusively, 
	 * 	 the new strength of this unit is given by the given strength.
	 *       | if(0<newstrength<200) 
	 *       |  then new.getStrength == newstrength
	 *     
	 */
	public void setStrength(int newstrength ){
		if (newstrength <= 0)
			this.strength = 1;
		if (newstrength >= 200)
			this.strength = 200;
		else
			this.strength = newstrength;
		
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
	 * @param 	newWeight
	 * 		The new weight of the unit.
	 * @post	If the given weight is an integer numbers with a value ranging from (strength+agility)/2 to 200, inclusively,
	 *           the new weight of this unit is given by the given weight.
	 *           | if((newWeight>(this.getStrengt()+this.getAgility())/2.0)&& (newWeight <= 200))
	 *           |    then new.getWeight() =  newWeight
	 * @post	If the given weight is greater then 200 the weight of the unit will be set at 200.
	 * 			| if(newWeight > 200)
	 * 			|   then new.getWeight() = newWeight
	 * @Post    If the given weight is smaller (strength+agility)/2, the weight of the unit will be set at
	 * 			 (strength+agility)/2 rounded up to the next integer.
	 * 			| if(newWeight < (this.getStrength() +this.getAgility())/2.0)
	 *          |   then new.getWeigth() = (int)Math.ceil((this.getStrength() +this.getAgility())/2.0)
	 * 
	 * 
	 */
	public void setWeight(int newWeight){
		double minWeight = (this.getStrength()+ this.getAgility())/2.0;
		if (newWeight <= minWeight)
			this.weight = (int) Math.ceil(minWeight);
		if (newWeight >=200)
			this.weight = 200;
		else
			this.weight = newWeight;
	}
	/**
	 * Returns the agility of the unit.
	 */
	@Basic 
	public int getAgility(){
		return this.agility;
	}
	/**
	 * Set the agility of the unit to the given agility.
	 * @param 	newagility
	 * 		The new agility of the unit.
	 * @post If the given agility is an integer number with a value less than  or equal to 0, the new agility of this 
	 * 	 unit is given by 1.
	 *       | if(newagility <=0) 
	 *       |  then  new.getAgility ==1
	 * @post If the given agility is an integer number with a value greater than 200, the new agility of this 
	 * 	 unit is given by 200.
	 *       | if(newagility >=200)
	 *       |  then new.getAgility ==200
	 * @post If the given agility is an integer number with a value ranging from 1 to 200, inclusively, 
	 * 	 the new agility of this unit is given by the given agility.
	 *       | if(0<newagility<200) 
	 *       |  then new.getAgility == newagility
	 */
	public void setAgility(int newagility){
		if (newagility <=0)
			this.agility = 1;
		if (newagility >= 200)
			this.agility = 200;
		else
			this.agility = newagility;
	}
	/**
	 * Returns the toughness of the unit.
	 */
	@Basic
	public int getToughness(){
		return this.toughness;
	}
	/**
	 * Set the toughness of the unit to the given toughness.
	 * @param 	newtoughness
	 * 		The new toughness of the unit.
	 * @post If the given toughness is an integer number with a value less than or equal to 0, the new toughness of this 
	 * 		unit is given by 1.
	 *       | if(newtoughness <=0) 
	 *       |  then  new.getToughness ==1
	 * @post If the given toughness is an integer number with a value greater than 200, the new toughness of this 
	 * 	 unit is given by 200.
	 *       | if(newtoughness >=200)
	 *       |  then new.getToughness ==200
	 * @post If the given toughness is an integer number with a value ranging from 1 to 200, inclusively, 
	 * 	 the new toughness of this unit is given by the given toughness.
	 *       | if(0<newtoughness<200) 
	 *       |  then new.getToughness == newtoughness
	 *     
	 */
	
	public void setToughness(int newtoughness){
		if (newtoughness <=0)
			this.toughness = 1;
		if (newtoughness >=200)
			this.toughness = 200;
		else 
			this.toughness = newtoughness;
		
	}
	/**
	 * Returns whether the default behaviour is enabled or not.
	 * 	The default behaviour consists of choosing activities at random. The activity can be (a) move to a random 
	 * 	position within the game world, (b) conduct a work task, or (c) rest until it has fully recovered hitpoints 
	 * 	and stamina points. 
	 */
	@Basic
	public boolean isEnableDefaultBehaviour() {
		return enableDefaultBehaviour;
	}
	/**
	 * Set the default behaviour of this unit according to the given flag.
	 * 	
	 * @param 	enableDefaultBehaviour
	 * 			The new default behaviour state for this unit.
	 * @post	The new default behaviour state of this unit is equal to the given flag.
	 *          | new.isEnableDefaultBehaviour = enableDefaultBehaviour
	 */
	public void setEnableDefaultBehaviour(boolean enableDefaultBehaviour) {
		this.enableDefaultBehaviour = enableDefaultBehaviour;
	}
	/**
	 * Start the default behaviour.
	 * 
	 * @effect The default behaviour state is set to true.
	 *         | new.isEnableDefaultBehaviour = true
	 */
	public void startDefaultBehaviour(){
		this.enableDefaultBehaviour = true;
	}
	/**
	 * Stop the default behaviour.
	 * 
	 * @effect The default behaviour state is set to false.
	 *  		| new.isEnableDefaultBehaviour = false
	 */
	public void stopDefaultBehaviour(){
		this.enableDefaultBehaviour = false;
	}
	/**
	 * Returns the maximum hit points the unit could have.
	 * @return 200*(weight/100)*(toughness/100) rounded up to the next integer.
	 */	
	
	
	public int getMaxHitPoints (){
		return (int)(Math.ceil(200*(this.getWeight()/100.0)*(this.getToughness()/100.0)));
	}
	/**
	 * Returns the amount of hitpoints of the unit.
	 * 
	 */
	@Basic
	public int getCurrentHitPoints(){
		return this.hitPoints;
	}
	/**
	 * Sets the hitpoints of the unit to the given amount, if the given amount is greater or equal to 0 and smaller then
	 *  the Maximum hitpoints the unit could have.
	 * @param newHitPoints
	 * 		The given amount of hitpoints.
	 * 
	 * @post  the hitpoints of the unit are set at the given value.
	 * 			| new.hitPoints = newHitPoints
	 * @throws IllegalArgumentException
	 * 			If the given hitpoints are greater then the maximum amount the unit can have or smaller then 0.
	 *          
	 */
	public void setCurrentHitPoints(int newHitPoints)throws IllegalArgumentException{
		if (newHitPoints<0 || newHitPoints > this.getMaxHitPoints()){
			throw new IllegalArgumentException();
		}
		this.hitPoints = newHitPoints;
	}
	/**
	 * Returns the maximum stamina points of the unit.
	 * @return 200*(weight/100)*(toughness/100) rounded up to the next integer.
	 */
	
	public int getMaxStaminaPoints(){
		return (int)(Math.ceil(200*(this.getWeight()/100.0)*(this.getToughness()/100.0)));
	}
	/**
	 * Returns the amount of stamina points of the unit.
	 * 
	 */
	@Basic
	public int getCurrentStaminaPoints(){
		return this.staminaPoints;
	}
	/**
	 * Sets the stamina points of the unit at the given amount.
	 * 
	 * @param newStaminaPoints
	 * 		The given amount of stamina points.
	 * @post the stamina points of the unit are equal to the given amount.
	 *       | new.getStaminaPoints() =  newStaminaPoints
	 * @throws IllegalArgumentException
	 *		If the given amount of stamina points is greater then the maximum amount the unit is able to have or
	 *		if the amount is smaller then 0.
	 */
	public void setCurrentStaminaPoints(int newStaminaPoints)throws IllegalArgumentException{
		if (newStaminaPoints<0 || newStaminaPoints>this.getMaxStaminaPoints()){
			throw new IllegalArgumentException();
		}
		this.staminaPoints = newStaminaPoints;
	}
	/* Orientation */
	/**
	 * Return the current orientation of the unit.
	 * 
	 * @param unit
	 *        The unit for which to retrieve the orientation
	 * @return The orientation of the unit, in radians.
	 * 
	 */
	@Basic
	public double getOrientation(){
		return this.orientation; 
	}
	/**
	 * Sets the orientation of the unit to the angel between 0 and 2*Pi that equals the given angle.
	 * @param neworientation
	 * 		The given angel for the unit.
	 * 
	 * @Post If the given orientation is greater then or equal to 0, the orientation of the unit is set at 
	 * 		neworientation modulo 2 times Pi.
	 *       | if(neworientation >=0)
	 *       |    then new.getOrientation == neworientation % 2*PI
	 * @post If the given orientation is negative, the orientation of the unit will be equal to the neworientation modulo 2 times Pi,
	 * 		increased with 2 times Pi.
	 *       | if(neworientation<0)
	 *       |    then new.getOrientation ==  (neworientation % 2*PI) +2*PI
	 */
	public void setOrientation(double neworientation){
		
		neworientation = neworientation % (2*Math.PI);
		if (neworientation < 0)
			neworientation = neworientation + 2*Math.PI;
		this.orientation = neworientation;
	}
	/*advance time*/
	/**
	 * Advance the state of the given unit by the given time period.
	 * 
	 * @param 	dt
	 *          The time period, in seconds, by which to advance the unit's
	 *          state.
	 * @post	If the unit is moving, the unit's position and speed gets updated every dt seconds.
	 * @post	If the unit is sprinting, the unit's position, stamina points and speed gets updated every dt seconds.
	 * @post	If the unit is working, the unit's position is given and speed is set to zero.
	 * @post	If the unit is fighting, after one second stop fighting. 			
	 * @throws 	ModelException
	 *          A precondition was violated or an exception was thrown.
	 */
	public void advanceTime(double dt) throws IllegalArgumentException{
		//correcte dt?
		if (!(0<dt&& dt<0.2))
		   throw new IllegalArgumentException();
		
		//snelheid bepalen 
		if (!(isMoving))
		    currentSpeed = 0;
		
		vb = 1.5 * (this.getStrength()+ this.getAgility()) / (200*this.getWeight()/100.0);
	    
		if ((this.getPosition()[2]- targetPosition[2])==-1)
			vw = 0.5*vb;
		if ((this.getPosition()[2]- targetPosition[2])==1)
			vw = 1.2*vb;
		else 
			vw = vb;
		
		if (this.isSprinting)
			currentSpeed = 2*vw ;
		else
			currentSpeed = vw;
		
		//positie(orientatie)
		
		if (isMoving) {
			distanceToGo = Math.sqrt(Math.pow((targetPosition[0]-this.getPosition()[0]),2)+ 
					       Math.pow((targetPosition[1]-this.getPosition()[1]),2)
			                 +Math.pow((targetPosition[2]-this.getPosition()[2]), 2));
			v = new double[]{this.getCurrentSpeed()*(targetPosition[0]-this.getPosition()[0])/distanceToGo,
					this.getCurrentSpeed()*(targetPosition[1]-this.getPosition()[1])/distanceToGo,
					this.getCurrentSpeed()*(targetPosition[2]-this.getPosition()[2])/distanceToGo};

			if (distanceToGo > this.getCurrentSpeed()*dt){		
				this.position = new double[]{this.getPosition()[0] + v[0] * dt, this.getPosition()[1]+ v[1]*dt, this.getPosition()[2]+ v[2]*dt};
				this.setOrientation(Math.atan2(v[0],v[1]));
		    }
		    else 
		    	this.position = this.targetPosition;
		        if (this.endTargetPosition == null)
		        	this.isMoving = false;
		    if (this.position == this.endTargetPosition){
		    	this.isMoving = false;
		    	this.endTargetPosition = null;
		    }
		}
		//Stamina points
		if (isSprinting){
			sprintedTime += dt;
			while(sprintedTime >= 0.1){
				sprintedTime -= 0.1;
				if (this.getCurrentStaminaPoints()<=1){
					this.setCurrentStaminaPoints(0);
					this.isSprinting = false;
				}
				else
					this.setCurrentStaminaPoints(this.getCurrentStaminaPoints()-1);				
			}
		}
		//working
		if (this.isWorking){
			if ((this.workTime - dt)>0){
				this.workTime = this.workTime - dt;
			}
			else{
				this.isWorking = false;
				this.workTime = 0;
			}
		}
		//resting in 3 min 
		double timeTillRest=0;
		if (!this.isResting){
			if (timeTillRest + dt >= 3*60){
				this.isResting = true;
				timeTillRest = 0;
			}
			else 
				timeTillRest += dt;
				
		}
		//in rust (hitpoints, staminapoints of beeindigen, in minimum rusttijd?) 
		if (this.isResting){
			if (this.inMinRestTime){
				if ((minRestTime - dt) <= 0){
					this.inMinRestTime = false;
					minRestTime = 0;
				}
				
				else 
					minRestTime -= dt;
					
				
			}
			
			restTime += dt;
			
			if (this.getCurrentHitPoints()==this.getMaxHitPoints() && this.getCurrentStaminaPoints()==this.getMaxStaminaPoints()){
				this.isResting = false;
				restTime = 0.0;
			}
			else if (this.getCurrentHitPoints()<this.getMaxHitPoints()){
				double timeToRecoverHitPoint = (1/(this.getToughness()/200.0)*0.2);
			
				if (restTime > timeToRecoverHitPoint ){
					this.setCurrentHitPoints(this.getCurrentHitPoints()+1);
					restTime -= timeToRecoverHitPoint;
				}
				
			}
			else if (this.getCurrentStaminaPoints()<this.getMaxStaminaPoints()){
				double timeToRecoverStaminaPoint = (1/(this.getToughness()/100.0)*0.2);
				if (restTime > timeToRecoverStaminaPoint){
					this.setCurrentStaminaPoints(this.getCurrentStaminaPoints()+1);
					restTime -= timeToRecoverStaminaPoint;
				}
			}
			
		}
		//fighting
		if (isAttacking) {
			attackTime -= dt;
			if (attackTime <= 0)
				isAttacking = false;
			}
	    	//default behaviour
		
	}
	
	
	/* moving*/
	/**
	 * Enable sprinting mode for the given unit.
	 * 
	 * @param unit
	 *        The unit which should start sprinting.
	 */
	public void startSprinting(){
		if (this.isMoving && this.getCurrentStaminaPoints() >0)
		this.isSprinting = true;
		
	}
	/**
	 * Disable sprinting mode for the given unit.
	 * 
	 * @param unit
	 *            The unit which should stop sprinting.
	 */
	public void stopSprinting(){
		this.isSprinting = false;		
	}
	/**
	 * Return the current speed of the given unit.
	 * 
	 * @param unit
	 *            The unit for which to retrieve the speed.
	 * @return The speed of the given unit.
	 */
	public double getCurrentSpeed(){
		return this.currentSpeed;
		
	}
	/**
	* Move the given unit to an adjacent cube.
	*
	* @param unit
	*            The unit to move
	* @param dx
	*            The amount of cubes to move in the x-direction; should be -1,
	*            0 or 1.
	* @param dy
	*            The amount of cubes to move in the y-direction; should be -1,
	*            0 or 1.
	* @param dz
	*            The amount of cubes to move in the z-direction; should be -1,
	*            0 or 1.
	*/	
	public void moveToAdjacent(int dx, int dy, int dz)throws IllegalArgumentException{
		//error if not in field
		if (!(0<=this.getCubeCoordinate()[0]+dx && this.getCubeCoordinate()[0]+dx<=49) || !(0<=this.getCubeCoordinate()[1]+dy && this.getCubeCoordinate()[1]+dy<=49) || !(0<=this.getCubeCoordinate()[2]+dz && this.getCubeCoordinate()[2]+dz<=49))
			throw new IllegalArgumentException();
		//moving activeren
		this.isMoving = true;
		
		// variables (targetposition,)
		this.targetPosition = new double[] {this.getCubeCoordinate()[0]+dx
				             + lc/2.0, this.getCubeCoordinate()[1]+dy + lc/2.0,this.getCubeCoordinate()[2]+dz + lc/2.0};
	    
		
	}
	/**
	 * Start moving the given unit to the given cube.
	 * 
	 * @param unit
	 *            The unit that should start moving
	 * @param cube
	 *            The coordinate of the cube to move to, as an array of integers
	 *            {x, y, z}.
	 */
	public void moveTo(int[]cube){
		if (!this.isWorking){
		
	
			int x;
			int y;
			int z;
			this.isMoving = true;
			this.endTargetPosition = new double []{cube[0]+lc/2.0 , cube[1]+lc/2.0, cube[2]+lc/2.0};
			
			
			while (this.getCubeCoordinate()!= cube){
				
				if (this.getCubeCoordinate()[0]== cube[0])
					x =0;
				else if (this.getCubeCoordinate()[0]< cube[0])
					x =1;
				else 
					x =-1;
				if (this.getCubeCoordinate()[1]==cube[1])
					y =0;
				else if (this.getCubeCoordinate()[1]<cube[1])	
					y = 1;
				else 
					y=-1;
				if (this.getCubeCoordinate()[2]==cube[2])
					z =0;
				else if (this.getCubeCoordinate()[2]<cube[2])
					z = 1;
				else 
					z = -1;
				moveToAdjacent(x,y,z);
			}
		}
				
	}	
	/**
	 * Return whether the given unit is currently moving.
	 * 
	 * @param unit
	 *            The unit for which to retrieve the state.
	 * @return true if the unit is currently moving; false otherwise
	 */
	public boolean isMoving(){
		return isMoving;
	}
	/**
	 * Return whether the given unit is currently sprinting.
	 * 
	 * @param unit
	 *            The unit for which to retrieve the state.
	 * @return true if the unit is currently sprinting; false otherwise
	 * @throws ModelException
	 *             A precondition was violated or an exception was thrown.
	 */
	public boolean isSprinting(){
		return isSprinting;
	}
	
	/* Attacking */
	public void attack(Unit defender){
		attackTime=1.00;
		double thetaA=Math.atan2((defender.position[1]-this.position[1]),(defender.position[0]-this.position[0]));
		double thetaD=Math.atan2((this.position[1]-defender.position[1]),(this.position[0]-defender.position[0]));		
		this.setOrientation(thetaA);
		defender.setOrientation(thetaD);
		isAttacking = true;
		//
		if(defender.defended(this)){
			return;
		} else {
			defender.takeDamage(this);
		}
		
	}
	/**
	 * Make the unit defend itself from the given unit.
	 * 
	 * @param attacker
	 */
	private boolean defended(Unit attacker) {
		// dodge
		double probabilityDodge = 0.20 * (this.getAgility() / attacker.getAgility());
		if (success(probabilityDodge)){
			//random position in game world
			return true;

		}
		// block
		double probabilityBlock = 0.25 * ((this.getStrength() + this.getAgility()) / (attacker.getStrength() + attacker.getAgility()));
		if (success(probabilityBlock))
			return true;

		return false;
	}
	
	private boolean success(double probability){
		// 
		return probability>Math.random();
		
	}
	private void takeDamage(Unit attacker){
		double damage =attacker.getStrength()/10.0;
		
		this.setCurrentHitPoints((int)Math.round(this.getCurrentHitPoints()-damage)); // is dit zo dan ok???(stond op een double)
	}
	
	/**
	 * Return whether this unit is currently attacking another unit.
	 * 
	 * @return true if the unit is currently attacking another unit; false
	 *         otherwise.
	 */
	public boolean isAttacking(){
		return isAttacking;
	}
	/* Working */
	/**
	 * Make the given unit start working.
	 * 
	 * @param unit
	 *      The unit that should start working
	 */
	public void work(){
		//werken mogelijk?  (resting mag niet altijd onderbroken?!)
		if ((!this.isMoving) && (!this.isAttacking)){
			this.isWorking = true;
			this.workTime = 500/(double)this.getStrength();       
		}
	}
	/**
	 * Return whether the given unit is currently working.
	 * 
	 * @param unit
	 *            The unit for which to retrieve the state
	 * @return true if the unit is currently working; false otherwise
	 */
	public boolean isWorking(){
		return isWorking;
	}
	/* Resting */

	/**
	 * Make the given unit rest.
	 * 
	 * @param unit
	 *            The unit that should start resting
	 */
	public void rest(){
		if(!this.isAttacking){
			this.isResting = true;
			this.inMinRestTime =true;
			minRestTime = 1/(this.getToughness()/200.0)*0.2;
		}
	}
	/**
	 * Return whether the given unit is currently resting.
	 * 
	 * @param unit
	 *            The unit for which to retrieve the atate
	 * @return true if the unit is currently resting; false otherwise
	 */
	public boolean isResting(){
		return isResting;
	}
}
