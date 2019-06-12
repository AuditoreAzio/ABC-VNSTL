package demo;

class Passenger extends Participate implements Cloneable{
	String dirverID; //乘客所登记的司机的ID
	
	public Passenger()
	{
		ID="000";
		dirverID="000";
		startLoc=new Location();
		startTime=0;
		finalTime=0;
	}
	@Override
	public Object clone()
	{
		Passenger p1=null;
		try{
			p1=(Passenger)super.clone();
			
		}
		catch(CloneNotSupportedException e) {  
            e.printStackTrace();
		}
		p1.startTime=startTime;
		p1.finalTime=finalTime;
		p1.beenDriver=beenDriver;
		p1.haveCar =haveCar;
		p1.startLoc=(Location)startLoc.clone();
		p1.ID=new String(ID);
		p1.dirverID= new String(dirverID);
		return p1;
	}
	public int chosePassenger(Participate p[],int n,Passenger[] pr) //从参与者中选出乘客集合
	{
		int j=0;
		for(int i=0;i<n;i++)
		{
			if(p[i].haveCar==0)
			{
				pr[j].ID=new String(p[i].ID);
				pr[j].startLoc.copy(pr[j].startLoc,p[i].startLoc );
				pr[j].startTime=p[i].startTime;
				pr[j].finalTime=p[i].finalTime;
				pr[j].haveCar=p[i].haveCar;
				pr[j].beenDriver=p[i].beenDriver;
				j++;
			}
			else if(p[i].beenDriver!=2)
			{
				pr[j].ID=new String(p[i].ID);
				pr[j].startLoc.copy(pr[j].startLoc,p[i].startLoc );
				pr[j].startTime=p[i].startTime;
				pr[j].finalTime=p[i].finalTime;
				pr[j].haveCar=p[i].haveCar;
				pr[j].beenDriver=p[i].beenDriver;
				j++;
			}
		}
		return 1;
	}
	
	
}
