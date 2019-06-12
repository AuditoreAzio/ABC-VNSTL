package demo;
import java.util.Random;

public class Driver extends Participate implements Cloneable{
	int capacity; //�ó����صĳ˿�����
	Passenger[] pd; //�ó����صĳ˿͵�����
	float distance; //������ʻ�ľ���
	float velocity; //�������ٶ�
	float realtime; //������ʱ������ʱ��
	Location dest;
	Location nowLoc;
	public Driver()
	{
		pd=new Passenger[4];
		capacity=0;
		distance=0;
		velocity=60;
		realtime=0;
		haveCar=1;
		nowLoc=new Location();
		nowLoc.copy(nowLoc, startLoc);
		dest=new Location();
		dest.setLocation(9, 11);
		
		for(int f=0;f<4;f++)
			pd[f]=new Passenger();
	}
	@Override
	public Object clone()
	{
		Driver d1=null;
		try{
			d1=(Driver)super.clone();
			
		}
		catch(CloneNotSupportedException e) {  
            e.printStackTrace();
		}
		//for(int f=0;f<4;f++)
			//d1.pd[f]=(Passenger)pd[f].clone();
		d1.pd=(Passenger[])pd.clone();
		d1.startLoc=(Location)startLoc.clone();
		d1.nowLoc=(Location)nowLoc.clone();
		d1.ID=new String(ID);
		d1.startTime=startTime;
		d1.finalTime=finalTime;
		d1.beenDriver=d1.beenDriver;
		d1.capacity=capacity;
		d1.realtime=realtime;
		d1.distance=distance;
		d1.velocity=velocity;
		d1.dest=(Location)dest.clone();
		return d1;
	}
	public int register(Driver d, Passenger p) //���˿�p�Ǽǵ�˾��d
	{
		int i;
		for(i=0;i<d.capacity;i++);
		if(i<=3)
		{
			d.pd[i]=(Passenger)p.clone();
			d.pd[i].dirverID=d.ID;
			d.capacity=d.capacity+1;
			d.distance=d.distance+countdis(d.nowLoc,p.startLoc); 
			d.realtime=countTime(countdis(d.nowLoc,p.startLoc),d.velocity,d.realtime,p.startTime,p.finalTime);
			d.nowLoc=(Location)p.startLoc.clone();
			return 1;
		}
		else return 0;
	}
	
	public int ChoseDeiver(Participate p[],int n,int d,Driver[] dr) //��n����������ѡ��d��˾��
	{
		int x;
		for(x=0;x<n;x++)
		{
			if(p[x].haveCar==1&&p[x].beenDriver==0)
			{
				d=d-1;
				dr[d].ID=new String(p[x].ID);
				dr[d].startLoc.copy(dr[d].startLoc, p[x].startLoc);
				nowLoc.copy(nowLoc, p[x].startLoc);
				dr[d].velocity=60;
				dr[d].startTime=p[x].startTime;
				dr[d].finalTime=p[x].finalTime;
				p[x].beenDriver=2;
				if(d==0) break;
			}
			
		}
		/*for(int i=0;i<n;i++)
		{ 
			
			if(p[i].beenDriver!=0) p[i].beenDriver--;
		}*/
		return 1;
		//��һ�������߱�ѡΪ˾��ʱ��beenDriver����������Ϊ3
		//֮��ÿһ��ѡ��˾��ʱ��beenDriver��һ������0����ٴα�ѡ��
	}
	
	public  void initialization(Driver[][] dr,int fn,int n,Passenger[] pr,int m) //������ʼ��
	{
		for(int d=0;d<fn;d++)
		{
			for(int i=0;i<m;i++)
			{
				int j;
				for(;;)
				{
					j=(int)(Math.random()*n);
					if(countTime(countdis(dr[d][j].startLoc,pr[i].startLoc),dr[d][j].velocity ,dr[d][j].realtime,pr[i].startTime,pr[i].finalTime)!=0&&dr[d][j].capacity<4)
					{
							break;	
					}
				}
				//dr[d][j].realtime=countTime(countdis(dr[d][j].nowLoc,pr[i].startLoc),dr[d][j].velocity,dr[d][j].realtime,pr[i].startTime,pr[i].finalTime);
				register(dr[d][j],pr[i]);
			}
			for(int x=0;x<n;x++) dr[d][x].distance+=dr[d][x].countdis(dr[d][x].nowLoc, dr[d][x].dest);
		}
		 
		
		
	}
	
	public float  countdis(Location s, Location l)//���������ڵ��ľ���
	{
		float d,dx,dy;
		dx=Math.abs(l.x-s.x)*Math.abs(l.x-s.x);
		dy=Math.abs(l.y-s.y)*Math.abs(l.y-s.y);
		d=(float)Math.sqrt((dx+dy));
		return d;
	}
	

	public float countTime(float l,float v,float rt,float t2,float t3)//������ʻ����l���õ�ʱ��
	{
		float t1=rt;
		float td=(l/v);
		if((t1+td)<=t2)
		{
			return t2;
		}
		else if((t1+td)<=t3)
		{
			t1=t1+td;
			return t1;
		}
		else return 0;
	}
	
	

}







