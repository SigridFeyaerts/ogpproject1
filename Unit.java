package hillbillies.model;
import be.kuleuven.cs.som.annotate.*;
import ogp.framework.util.ModelException;

/**
 * A class of units characterised by their name, position,
 * weight, agility, strength, toughness and default behaviour. TODO...(limits,...)test
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
	
	@Basic
	public String getName(){
		return this.name;
	
	}
	public void setName(Unit unit, String newname){
		
		
	}
	@Basic
	public double[] getPosition(){
		return this.position;		
	}
	public int [] getCubeCoordinates() {
		int newcoordinates[]= {(int)(Math.floor(this.getPosition()[0])),(int)(Math.floor(this.getPosition()[1])),(int)(Math.floor(this.getPosition()[2]))};
		return newcoordinates;
		
	}
	@Basic
	public int getStrength() {
		return this.strength;
		
	}
	/**
	 * 
	 * @param unit
	 * @param newstrength
	 * @post ...
	 *       | if(newstrength <=0) 
	 *       |  then  new.getStrength ==1
	 * @post ...
	 *       | if(newstength >=200)
	 *       |  then new.getStrength ==200
	 * @post ...
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
	@Basic 
	public int getAgility(){
		return this.agility;
	}
	
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
