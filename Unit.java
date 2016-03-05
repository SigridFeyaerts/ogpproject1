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
	private int strength,weight,agility,toughness,lc = 1;
	private double attackTime,orientation,vw,vb,vs,currentSpeed,distanceToGo,hitpoints,staminaPoints,workTime,restTime,minRestTime;
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
	 * @throws 	ModelException
	 *      	A precondition was violated or an exception was thrown.
	 */
	public Unit(String name, int[] initialPosition, int weight, int agility, int strength, int toughness,
			boolean enableDefaultBehaviour) throws ModelException{
		//TODO: implementeren zie initiele grenzen. initial value in the range of 25 to 100, inclusively, and the weight
		// of a Unit must at all times be at least (s+a)/2
		setName(name);
		setCubeCoordinates(initialPosition);
		setWeight(weight);
		setAgility(agility);
		setStrength(strength);
		setToughness(toughness);
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
	public void setName(Unit unit, String newname){
		if (!isValidName(name))
			throw new ModelException(name);
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
	 * Set the initial position of the cube occupied by the unit to the given initial position.
	 * 
	 * @param 	initialPositionCube
	 * 			The given initial position of the cube occupied by the unit. This is given by rounding down the 
	 * 			coordinates of the unit's position to integer numbers.
	 * @post	If ... limitations. (defensively)//TODO
	 */
	 private void setCubeCoordinate(double[] initialPosition) {
		 int dim = 3;
		 int[] cubeCoordinate= new int[dim];
		 for(int i=0; i<dim; i++){
			 cubeCoordinate[i]=(int)Math.floor(initialPosition[i]);
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
	 * @param 	newstrength //Waarom niet gewoon strength?
	 * 		The new strength of the unit.
	 * @post If the given strength is an integer number with a value less than 0, the new strength of this 
	 * 	unit is given by 0.
	 *       | if(newstrength <=0) 
	 *       |  then  new.getStrength ==1
	 * @post If the given strength is an integer number with a value greater than 200, the new strength of this 
	 * 	 unit is given by 200.
	 *       | if(newstength >=200)
	 *       |  then new.getStrength ==200
	 * @post If the given strength is an integer number with a value ranging from 1 to 200, inclusively, 
	 * 	 the new strength of this unit is given by the given strength.
	 *       | if(0<newstrength<200) // moet dat niet ook = aan 0 en 200
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
	 * @param 	newweight
	 * 		The new weight of the unit.
	 * @post	If the given weight is an integer numbers with a value ranging from 1 to 200, inclusively, and at 
	 * 		least (strength+agility)/2, the new weight of this unit is given by the given weight.
	 * 
	 */
	public void setWeight(int newweight){
		if (newweight <= 0)
			this.weight = 1;
		if (newweight >=200)
			this.weight = 200;
		else
			this.weight = newweight;
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
	 * @param 	agility
	 * 		The new agility of the unit.
	 * @post If the given agility is an integer number with a value less than 0, the new agility of this 
	 * 	 unit is given by 0.
	 *       | if(newagility <=0) 
	 *       |  then  new.getAgility ==1
	 * @post If the given agility is an integer number with a value greater than 200, the new agility of this 
	 * 	 unit is given by 200.
	 *       | if(newagility >=200)
	 *       |  then new.getAgility ==200
	 * @post If the given agility is an integer number with a value ranging from 1 to 200, inclusively, 
	 * 	 the new agility of this unit is given by the given agility.
	 *       | if(0<newagility<200) // moet dat niet ook = aan 0 en 200
	 *       |  then new.getAgility == newAgility
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
	 * @param 	toughness
	 * 		The new toughness of the unit.
	 * @post	If the given toughness is an integer numbers with a value ranging from 1 to 200, inclusively, 
	 * 		the new toughness of this unit is given by the given toughness.
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
	 */
	public void setEnableDefaultBehaviour(boolean enableDefaultBehaviour) {
		this.enableDefaultBehaviour = enableDefaultBehaviour;
	}
	/**
	 * Start the default behaviour.
	 * 
	 * @effect The default behaviour state is set to true.
	 */
	public void startDefaultBehaviour(){
		
	}
	/**
	 * Stop the default behaviour.
	 * 
	 * @effect The default behaviour state is set to false.
	 */
	public void stopDefaultBehaviour(){
	/**
	 * Returns the maximum hit points of the unit.
	 * TODO @return 200*(weight/100)*(toughness/100) ofzo 
	 */	
	}
	@Basic//Is dit wel zo?
	public int getMaxHitPoints (){
		return (int)(Math.ceil(200*(this.getWeight()/100.0)*(this.getToughness()/100.0)));
	}
	/**
	 * Returns the amount of hitpoints of the unit.
	 * TODO @return ...
	 */
	@Basic
	public double getCurrentHitPoints(){
		return this.hitpoints;
	}
	/**
	 * Returns the maximum stamina points of the unit.
	 * TODO @return ...
	 */
	@Basic
	public int getMaxStaminaPoints(){
		return (int)(Math.ceil(200*(this.getWeight()/100.0)*(this.getToughness()/100.0)));
	}
	/**
	 * Returns the amount of stamina points of the unit.
	 * TODO @return ...
	 */
	@Basic
	public double getCurrentStaminaPoints(){
		return this.staminaPoints;
	}
	/* Orientation */
	/**
	 * Return the current orientation of the unit.
	 * 
	 * @param unit
	 *        The unit for which to retrieve the orientation
	 * @return The orientation of the unit, in radians.
	 * @throws ModelException
	 *          A precondition was violated or an exception was thrown.
	 */
	@Basic
	public double getOrientation(){
		return this.orientation; 
	}
	/**
	 * 
	 * @param neworientation
	 * 
	 * @Post ...
	 *       | if(neworientation >0)
	 *       |    then new.getOrientation == neworientation % 2*PI
	 * @post ...
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
	public void advanceTime(double dt) throws ModelException{
		//correcte dt?
		if (!(0<dt&& dt<0.2))
		   throw new ModelException();
		
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
		if (isSprinting)
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
			
			else if (restTime >= 0.2){
				restTime = restTime - 0.2;
				if (this.getCurrentHitPoints() < this.getMaxHitPoints()){       //check grens (niet over maxHitpoints gaan!)
					
					double extraHitPoints = this.getToughness()/200.0;
					this.hitpoints =  this.getCurrentHitPoints() +  extraHitPoints;         //sethitpoints aanmaken?                                     //TODO
				}
				else if (this.getCurrentStaminaPoints() < this.getMaxStaminaPoints()){
					double extraStaminaPoints = this.getToughness()/100.0;
					this.staminaPoints = this.getCurrentStaminaPoints() + extraStaminaPoints; 
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
	public void moveToAdjacent(int dx, int dy, int dz){
		//error if not in field
		if (!(0<=this.getCubeCoordinate()[0]+dx && this.getCubeCoordinate()[0]+dx<=49) || !(0<=this.getCubeCoordinate()[1]+dy && this.getCubeCoordinate()[1]+dy<=49) || !(0<=this.getCubeCoordinate()[2]+dz && this.getCubeCoordinate()[2]+dz<=49))
			throw new ModelException();
		//moving activeren
		this.isMoving = true;
		
		// variables (targetposition,)
		this.targetPosition = new double[] {this.getCubeCoordinates()[0]+dx
				             + lc/2.0, this.getCubeCoordinates()[1]+dy + lc/2.0,this.getCubeCoordinates()[2]+dz + lc/2.0};
	    
		
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
			
			
			while (this.getCubeCoordinates()!= cube); do{
				
				if (this.getCubeCoordinates()[0]== cube[0])
					x =0;
				else if (this.getCubeCoordinates()[0]< cube[0])
					x =1;
				else 
					x =-1;
				if (this.getCubeCoordinates()[1]==cube[1])
					y =0;
				else if (this.getCubeCoordinates()[1]<cube[1])	
					y = 1;
				else 
					y=-1;
				if (this.getCubeCoordinates()[2]==cube[2])
					z =0;
				else if (this.getCubeCoordinates()[2]<cube[2])
					z = 1;
				else 
					z = -1;
				moveToAdjacent(x,y,z);
			}
		}
				
	}	
	
	
	
	/* Attacking */
	public void attack(Unit defender){
		attackTime=1.00;
		double thetaA=Math.atan2((defender.unitPosition[1]-this.unitPosition[1]),(defender.unitPosition[0]-this.unitPosition[0]));
		double thetaD=Math.atan2((this.unitPosition[1]-defender.unitPosition[1]),(this.unitPosition[0]-defender.unitPosition[0]));		
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
		
		this.setHitPoints(hitpoints-damage);
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
	public void work(){
		//werken mogelijk?  (resting mag niet altijd onderbroken?!)
		if ((!this.isMoving) && (!this.isFighting)){
			this.isWorking = true;
			this.workTime = 500/(double)this.getStrength();       //nakijken
		}
	}
	public void rest(){
		if(!this.isFighting){
			this.isResting = true;
			this.inMinRestTime =true;
			minRestTime = 1/(this.getToughness()/200.0)*0.2;
		}
	}
		

}
