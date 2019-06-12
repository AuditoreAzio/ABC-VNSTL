package demo;

class Participate implements Cloneable{
	String ID; //参与者的ID
	int haveCar; //是否有车
	int beenDriver; //3天内是否曾被选作司机
	float startTime; //时间窗起始时间
	float finalTime; //时间窗结束时间
	Location startLoc;//参与者的起始位置 
	
	public Participate()
	{
		ID="000";
		haveCar=0;
		beenDriver=0;
		startLoc=new Location();
		
	}
	
}
