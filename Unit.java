package hillbillies.model;
import be.kuleuven.cs.som.annotate.*;
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
	private int strength,weight,agility,toughness,lc = 1,hitpoints,staminaPoints;
	private double[] position,targetposition,v;
	private double orientation,vw,vb,vs,currentSpeed,distanceToGo;
	
	/**
	 * Initialize this new unit with given name, position, weight, strength, 
	 * agility, toughness and state of default behaviour. 
	 * 
	 * @param 	name
	 * 			The name of the unit.
	 * @param 	initialPosition
	 * 			The initial position of the unit, as an array with 3 elements
	 *          {x, y, z}.
	 * @param 	weight
	 * 			The initial weight of the unit.
	 * @param 	agility
	 * 			The initial agility of the unit.
	 * @param 	strength
	 * 			The initial strength of the unit.
	 * @param 	toughness
	 * 			The initial toughness of the unit.
	 * @param 	enableDefaultBehaviour
	 * 			Whether the default behaviour of the unit is enabled.
	 * @post	
	 * @throws 	ModelException
	 *          A precondition was violated or an exception was thrown.
	 */
	public Unit(String name, int[] initialPosition, int weight, int agility, int strength, int toughness,
			boolean enableDefaultBehaviour) throws ModelException{
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
	 * Set the name of this unit to the given name.
	 * 
	 * @param 	name
	 * 			The new name of the unit.
	 * @post	The new name of this unit is equal to the given name.
	 * 			| new.getName() == name
	 * @throws 	ModelException
	 * 			The given name is not valid for any unit.
	 * 			| ! isValidName(name)
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
	 * @return ... of @Basic?
	 */
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
	 * 
	 * @param newweight
	 * 
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
	 * 	unit is given by 0.
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
	@Basic
	public int getToughness(){
		return this.toughness;
	}
	public void setToughness(int newtoughness){
		if (newtoughness <=0)
			this.toughness = 1;
		if (newtoughness >=200)
			this.toughness = 200;
		else 
			this.toughness = newtoughness;
		
	}
	@Basic
	public int getMaxHitPoints (){
		return (int)(Math.ceil(200*(this.getWeight()/100.0)*(this.getToughness()/100.0)));
		
	}
	@Basic
	public int getCurrentHitPoints(){
		return this.hitpoints;
		
	}
	@Basic
	public int getMaxStaminaPoints(){
		return (int)(Math.ceil(200*(this.getWeight()/100.0)*(this.getToughness()/100.0)));
	}
	@Basic
	public int getCurrentStaminaPoints(){
		return this.staminaPoints;
	}
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
	//advance time
	
	public void advanceTime(double dt){
		//correcte dt?
		if !(0<dt<0.2)
		   throw modelException;
		
		//snelheid bepalen 
		if (!(isMoving))
		    currentSpeed = 0;
		
		vb = 1.5 * (this.getStrength()+ this.getAgility()) / (200*this.getWeight()/100);
	    
		if ((this.getPosition()[2]- targetposition[2])==-1)
			vw = 0.5*vb;
		if ((this.getPosition()[2]- targetposition[2])==1)
			vw = 1.2*vb;
		else 
			vw = vb;
		
		if (this.isSprinting())
		    currentSpeed = 2*vw ;
		else
			currentSpeed = vw;
		
		//positie(orientatie)
		
		if (isMoving) {
			distanceToGo = Math.sqrt(Math.pow((targetposition[0]-this.getPosition()[0]),2)+ 
					       Math.pow((targetposition[1]-this.getPosition()[1]),2)
			                 +Math.pow((targetposition[2]-this.getPosition()[2]), 2));
			v = {this.getCurrentSpeed()*(targetposition[0]-this.getPosition()[0])/distanceToGo,
					this.getCurrentSpeed()*(targetposition[1]-this.getPosition()[1])/distanceToGo,
					this.getCurrentSpeed()*(targetposition[2]-this.getPosition()[2])/distanceToGo};

		    if (distanceToGo < norm(v)*dt){		
			
			this.position = {this.getPosition()[0] + v[0] * dt, this.getPosition()[1]+ v[1]*dt, this.getPosition()[2]+ v[2]*dt};
			
			this.setOrientation(Math.atan2(v[0],v[1]));
		    }
		}
	    //Stamina points
		if (isSprinting)
	    
	    
	    	
		
	}
	
	
	//moving
	
	public boolean isSprinting(){
		
		
		
	}
	public void startSprinting(){
		if (isMoving and stamina >0)
		isSprinting = True;
		
	}
	public void stopSprinting(){
		
	}
	public double getCurrentSpeed(){
		return this.currentSpeed;
		
	}
	public void moveToAdjacent(int dx, int dy, int dz){
		
		//error if not in field
		if (!(0<=this.getCubeCoordinates()[0]+dx<=49) || !(0<=this.getCubeCoordinates()[1]+dy<=49) || !(0<=this.getCubeCoordinates()[2]+dz<=49))
			throw ModelException;
		
		// variables (targetposition,)
		this.targetposition = new double[] {this.getCubeCoordinates()[0]+dx
				             + lc/2.0, this.getCubeCoordinates()[1]+dy + lc/2.0,this.getCubeCoordinates()[2]+dz + lc/2.0};
	    
		
	}
	
	public void moveTo(int[]cube){
	
		int x;
		int y;
		int z;
		
		while (this.getCubeCoordinate()!= cube); do{
			
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
			this.moveToAdjacent(x,y,z);
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
		double probabilityBlock = 0.25 * ((this.getStrength() + this.getAgility()) / attacker.getStrength() + attacker.getAgility());
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
		
		this.setHitPoints((int)(hitpoints-damage));
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

}
