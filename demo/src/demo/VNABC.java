package demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.Writer;

class VNABC{
	public static void main(String args[]) throws Exception
	{
		long start=System.currentTimeMillis();
		VNABC a=new VNABC();
		float all=106; //总参与人数
		int n=(int)Math.ceil(all/5); //司机数量
		int m = (int)all-n; //乘客数量
		int foodNum=10; 
		int tl =2*n;
		int day=10;
		int bestS[]=new int[2];
		bestS[0]=bestS[1]=0;
		float bestFit=0;
		Node tb[]=new Node[foodNum];
		for(int f=0;f<foodNum;f++)
			tb[f]=new Node();
		int flag=0;
		File file = new File("test.txt");
		String Id[] = new String[m+n];
		int hc[] = new int[m+n];
		float lx[] = new float[m+n];
		float ly[] = new float[m+n];
		int t1[] = new int[m+n];
		int t2[] = new int[m+n];
		readdate(file,Id,hc,lx,ly,t1,t2);
		
		Participate[] p=new Participate[m+n];
		for(int i=0;i<m+n;i++)
			p[i]=new Participate();
		
		for(int i=0;i<m+n;i++)
		{
			p[i].ID=Id[i];
			p[i].haveCar=hc[i];
			p[i].startLoc.setLocation(lx[i], ly[i]);
			p[i].startTime=1;
			p[i].finalTime=100;
			
		}
		PrintStream ps = new PrintStream("result.txt");
		 System.setOut(ps);
		for(int r=0;r<day;r++)
		{
		int mark[]=new int[foodNum];
		for(int i=0;i<foodNum;i++) mark[i]=0;
		Driver[] cd =new Driver[n];
		Driver[][] dr=new Driver[foodNum][n];
		Driver[][] db=new Driver[foodNum][n];
		
		Passenger[] pr = new Passenger[m];
		for(int i=0;i<n;i++)
		cd[i]=new Driver();
		
		for(int i=0;i<foodNum;i++)
		{
			for(int j=0;j<n;j++)
				{dr[i][j]=new Driver();
				db[i][j]=new Driver();}
		}
		for(int i=0;i<m;i++)
			pr[i]=new Passenger();
		cd[0].ChoseDeiver(p, m+n, n, cd);
		pr[0].chosePassenger(p, m+n, pr);
		
		
		for(int i=0;i<foodNum;i++)
		{
			for(int j=0;j<n;j++)
			dr[i][j]=(Driver)cd[j].clone();
		}
		dr[0][0].initialization(dr,foodNum,n,pr,m); //生成初始解
		/*for(int i=0;i<foodNum;i++)
		{
			for(int j=0;j<n;j++)
			dr[i][j].realtime=a.countRealTime(dr[i][j]);
		}*/
		//db=(Driver[][])dr.clone();
		bestFit=a.countfitness(dr,0,n);
		if(r==0)
		System.out.println(bestFit);
		for(int i=0;i<m+n;i++)
		{
			if(p[i].beenDriver==2) p[i].beenDriver=0;
		}
		for(int i=0;i<foodNum;i++)
		tb[i].next=null;
		//for(int i=0;i<foodNum;i++)
			//for(int j=0;j<n;j++) tb[i].in(dr[i][j]);
		for(int i=0;i<foodNum;i++)
		{
			for(int j=0;j<n;j++)
			db[i][j]=(Driver)dr[i][j].clone();
		}
		
		for(int i=0;i<5000;i++)//开始迭代
		{
			
			if(flag==0)
			{
				long estart=System.currentTimeMillis();
				//dr=(Driver[][])db.clone();
				for(int l=0;l<foodNum;l++)
				{
					for(int j=0;j<n;j++)
					dr[l][j]=(Driver)db[l][j].clone();
				}
				
				bestS[flag]=a.employedBee(dr,foodNum,n,tb);
				
				
				bestS[flag]=a.onlookerBee(dr,foodNum, n,tb);
				
				bestFit=a.countfitness(dr,bestS[flag],n);
				//System.out.println(bestFit);
				for(int f=0;f<foodNum;f++)
				{
				
				for(int x=0;x<foodNum;x++)
					for(int y=0;y<n;y++)
						tb[f].in(dr[x][y]);
				while(tb[f].length()>=tl) tb[f].out();
				}
				
				a.scoutBee(db, dr, foodNum, n, p, m,bestS[flag],mark);
				long eend=System.currentTimeMillis();
				//System.out.println("一次迭代时间:"+(eend-estart)+"ms");
				flag=1;
			}
			else
			{
				//db=(Driver[][])dr.clone();
				for(int l=0;l<foodNum;l++)
				{
					for(int j=0;j<n;j++)
					db[l][j]=(Driver)dr[l][j].clone();
				}
				
				bestS[flag]=a.employedBee(db,foodNum,n,tb);
				
				long ostart=System.currentTimeMillis();
				bestS[flag]=a.onlookerBee(db,foodNum, n,tb);
				long oend=System.currentTimeMillis();
				//System.out.println("onlookerBee:"+(oend-ostart)+"ms");
				bestFit=a.countfitness(db,bestS[flag],n);
				//System.out.println(bestFit);
				for(int f=0;f<foodNum;f++)
				{
				for(int x=0;x<foodNum;x++)
					for(int y=0;y<n;y++)
						tb[f].in(db[x][y]);
				while(tb[f].length()>=tl) tb[f].out();
				}
				long sstart=System.currentTimeMillis();
				a.scoutBee(dr, db, foodNum, n, p, m,bestS[flag],mark);
				long send=System.currentTimeMillis();
				//System.out.println("scoutBee:"+(send-sstart)+"ms");
				flag=0;
			}
			
		}
		
		if(flag==1)
		{
			bestFit=a.countfitness(dr,bestS[0],n);
			/*System.out.println("Day:"+(r+1));
			for(int i=0;i<n;i++)
			{
				System.out.println("Driver:");
				System.out.println(dr[bestS[0]][i].ID);
				System.out.println("Passenger:");
				for(int j=0;j<dr[bestS[0]][i].capacity;j++)
					System.out.println(dr[bestS[0]][i].pd[j].ID);
				dr[bestS[0]][i].realtime=a.countRealTime(dr[bestS[0]][i]);
				//System.out.println(dr[bestS[0]][i].realtime);
			}*/
			System.out.println(bestFit);
			//System.out.println(a.sumdistance(dr, bestS[0], n));
			for(int x=0;x<m+n;x++)
			{
				for(int y=0;y<n;y++)
				{
					if(p[x].ID.equals(dr[bestS[0]][y].ID)) 
						{p[x].beenDriver=2;break;}
					
				}
				if(p[x].beenDriver!=0) p[x].beenDriver--;
				int f=0;
				for(f=0;f<n;f++)
					if(p[x].ID.equals(dr[bestS[0]][f].ID)) break;
				if(f==n&&p[x].beenDriver==2) p[x].beenDriver=0;
			}
		}
		else
		{
			bestFit=a.countfitness(db,bestS[1],n);
			/*System.out.println("Day:"+(r+1));
			for(int i=0;i<n;i++)
			{
				System.out.println("Driver:");
				System.out.println(db[bestS[1]][i].ID);
				System.out.println("Passenger:");
				for(int j=0;j<db[bestS[1]][i].capacity;j++)
					System.out.println(db[bestS[1]][i].pd[j].ID);
				db[bestS[1]][i].realtime=a.countRealTime(db[bestS[1]][i]);
				//System.out.println(db[bestS[1]][i].realtime);
			}*/
			System.out.println(bestFit);
			//System.out.println(a.sumdistance(db, bestS[1], n));
			for(int x=0;x<m+n;x++)
			{
				for(int y=0;y<n;y++)
				{
					if(p[x].ID.equals(db[bestS[1]][y].ID)) 
						{p[x].beenDriver=2;break;}
					
				}
				if(p[x].beenDriver!=0) p[x].beenDriver--;
				//int f=0;
				//for(f=0;f<n;f++)
					//if(p[x].ID.equals(db[bestS[1]][f].ID)) break;
				//if(f==n&&p[x].beenDriver==2) p[x].beenDriver=0;
			}
		}
		}
		long end = System.currentTimeMillis();
		System.out.println((end-start)+"ms"); 
		
	}
	//从文件中获取测试数据
	public static void readdate(File file,String cid[],int hc[],float lx[],float ly[],int t1[],int t2[]){
        //StringBuilder result = new StringBuilder();
        String clx=null,cly=null;
        int c=0,h=0,x=0,y=0,ts=0,te=0;
        int i=0;
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
            	cid[c]=(String) s.subSequence(0, 3);
            	c++;
            	hc[h++]=Integer.parseInt((String)s.substring(4, 5));
            	for(i=0;i<s.length();i++)
            		if(s.charAt(i)=='(') break;
            	int b=i+1;
            	for(;i<s.length();i++)
            		if(s.charAt(i)==',') break;
            	clx=(String)s.substring(b, i);
            	lx[x++]=Float.parseFloat(clx);
            	b=i+1;
            	for(;i<s.length();i++)
            		if(s.charAt(i)==')') break;
            	cly=(String)s.substring(b, i);
            	ly[y++]=Float.parseFloat(cly);
            	for(;i<s.length();i++)
            		if(s.charAt(i)=='(') break;
            	b=i+1;
            	for(;i<s.length();i++)
            		if(s.charAt(i)==':') break;
            	clx=(String)s.substring(b, i);
            	t1[ts++]=Integer.parseInt(clx);
            	b=i+1;
            	for(;i<s.length();i++)
            		if(s.charAt(i)==')') break;
            	clx=(String)s.substring(b, i);
            	t2[te++]=Integer.parseInt(clx);
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
	/*public int Chose(float d1,float d2)
	{
		if(d2-d1<(d1/2))
		{
		double r1=Math.exp((double)(d1-d2)*2);
		double x=Math.random();
		if(x<=r1) return 1;
		//else if(x<r2) return 1;
		else return 0;
		}
		else
		{
			double r2=Math.exp((double)(d1*d2)/(d1-d2));
			double y=Math.random();
			if(y<=r2) return 1;
			else return 0;
		}
		
	}*/
	/*public float countRealTime(Driver dr)
	{
		float time=0;
		time+=(dr.countdis(dr.startLoc, dr.pd[0].startLoc)/dr.velocity);
		for(int i=0;i<dr.capacity-1;i++)
			time+=(dr.countdis(dr.pd[i].startLoc, dr.pd[i+1].startLoc)/dr.velocity);
		time+=(dr.countdis(dr.pd[dr.capacity-1].startLoc, dr.dest)/dr.velocity);
		return time;
	}*/

	public int changeP(Driver [][] dr,int k,int n,Node tb[])//change one passenger
	{
		
		Passenger p;
		float t1=0;
		float t2=0;
		int i=0;
		
		int x1=(int)(Math.random()*n);
		int x2=(int)(Math.random()*n);
		while(dr[k][x1].capacity==0) x1=(int)(Math.random()*n);
		while(x1==x2||dr[k][x2].capacity==0)
			x2=(int)(Math.random()*n);
		Driver d1=new Driver();
		Driver d2=new Driver();
		d1=(Driver)dr[k][x1].clone();
		d2=(Driver)dr[k][x2].clone();
		int p1=(int)(Math.random()*d1.capacity);
		int p2=(int)(Math.random()*d2.capacity);
		Location l1=new Location();
		Location l2=new Location();
		l1.copy(l1, d1.startLoc);
		l2.copy(l2, d2.startLoc);
		
		for(i=0;i<p1;i++)
		{
			t1=d1.countTime(d1.countdis(l1, d1.pd[i].startLoc), d1.velocity, t1, d1.pd[i].startTime, d1.pd[i].finalTime);
			l1.copy(l1, d1.pd[i].startLoc);
		}
		for(i=0;i<p2;i++)
		{
			t2=d2.countTime(d2.countdis(l2, d2.pd[i].startLoc), d2.velocity, t2, d2.pd[i].startTime, d2.pd[i].finalTime);
			l2.copy(l2, d2.pd[i].startLoc);
		}
		if(d1.countTime(d1.countdis(l1,d2.pd[p2].startLoc), d1.velocity,t1,d2.pd[p2].startTime,d2.pd[p2].finalTime)>0&&d2.countTime(d1.countdis(l2,d1.pd[p1].startLoc), d2.velocity,t2,d1.pd[p1].startTime,d1.pd[p1].finalTime)>0)
		{
			p=(Passenger)d1.pd[p1].clone();
			d1.pd[p1]=(Passenger)d2.pd[p2].clone();
			d2.pd[p2]=p;
			d1.distance=drdistance(d1);
			d2.distance=drdistance(d2);
			d1.nowLoc=(Location)d1.pd[d1.capacity-1].startLoc;
			d2.nowLoc=(Location)d2.pd[d2.capacity-1].startLoc;
			d1.realtime=d1.countTime(d1.countdis(d1.startLoc, d1.pd[0].startLoc),d1.velocity,d1.realtime,d1.pd[0].startTime,d1.pd[0].finalTime);
			for(i=0;i<(d1.capacity-1);i++)
			{
				d1.realtime=d1.countTime(d1.countdis(d1.pd[i].startLoc, d1.pd[i+1].startLoc),d1.velocity,d1.realtime,d1.pd[i+1].startTime,d1.pd[i+1].finalTime);
			}
			
			d2.realtime=d2.countTime(d2.countdis(d2.startLoc, d2.pd[0].startLoc),d2.velocity,d2.realtime,d2.pd[0].startTime,d2.pd[0].finalTime);
			for(i=0;i<(d1.capacity-1);i++)
			{
				d2.realtime=d2.countTime(d2.countdis(d2.pd[i].startLoc, d2.pd[i+1].startLoc),d2.velocity,d2.realtime,d2.pd[i+1].startTime,d2.pd[i+1].finalTime);
			}
			if(tb[k].compare(d1)==1||tb[k].compare(d2)==1)
			return 1;
				//changeP(dr,k,n,tb);
			
			if(d1.distance+d2.distance<dr[k][x1].distance+dr[k][x2].distance)
			{
				dr[k][x1]=(Driver)d1.clone();
				dr[k][x2]=(Driver)d2.clone();
				//dr[k][x1].realtime=countRealTime(dr[k][x1]);
				//dr[k][x2].realtime=countRealTime(dr[k][x1]);
				//while(tb.length()>2*n) tb.out();
				//tb.in(d1);
				//tb.in(d2);
				return 1;
			}
			/*else if(Chose(dr[k][x1].distance+dr[k][x2].distance,d1.distance+d2.distance)==1)
			{
				dr[k][x1]=(Driver)d1.clone();
				dr[k][x2]=(Driver)d2.clone();
				return 1;
			}*/
			else
				return 0;
			
		}
		return 0;
	}
	public int changeP2(Driver[][] dr,int k,int n,Node tb[]) //change all passenger
	{
		
		 int x1=(int)(Math.random()*n);
		 int x2=(int)(Math.random()*n);
		 Passenger[] pd=new Passenger[4];
		 int c;
		 for(int i=0;i<4;i++) pd[i]=new Passenger();
		 while(dr[k][x1].capacity==0)
			 x1=(int)(Math.random()*n);
		 while(x1==x2||dr[k][x2].capacity==0||dr[k][x1].countTime(dr[k][x2].countdis(dr[k][x2].startLoc,dr[k][x1].pd[0].startLoc),dr[k][x2].velocity,dr[k][x1].realtime,dr[k][x1].pd[0].startTime,dr[k][x1].pd[0].finalTime)==0)
			 x2=(int)(Math.random()*n);
		 Driver d1=(Driver)dr[k][x1].clone();
		 Driver d2=(Driver)dr[k][x2].clone();
		 pd=(Passenger[])dr[k][x1].pd.clone();
		 c=dr[k][x1].capacity;
		 dr[k][x1].pd=(Passenger[])dr[k][x2].pd.clone();
		 dr[k][x1].capacity=dr[k][x2].capacity;
		 dr[k][x2].pd=(Passenger[])pd.clone();
		 dr[k][x2].capacity=c;
		 dr[k][x1].distance=drdistance(dr[k][x1]);
		 dr[k][x2].distance=drdistance(dr[k][x2]);
		 /*if(tb[k].compare(dr[k][x1])==1||tb[k].compare(dr[k][x2])==1)
		 {
			 dr[k][x1]=(Driver)d1.clone();
			 dr[k][x2]=(Driver)d2.clone();
			 changeP2(dr,k,n,tb);
		 }*/
		 if(dr[k][x1].distance+dr[k][x2].distance<d1.distance+d2.distance)
		 {
			 /*while(tb.length()>6) tb.out();
			 tb.in(dr[k][x1]);
			 tb.in(dr[k][x2]);*/
			 //dr[k][x1].realtime=countRealTime(dr[k][x1]);
			 //dr[k][x2].realtime=countRealTime(dr[k][x1]);
			 return 1;
		 }
		 //else if(Chose(d1.distance+d2.distance,dr[k][x1].distance+dr[k][x2].distance)==1)
			 //return 1;
		 else
		 {
			 dr[k][x1]=(Driver)d1.clone();
			 dr[k][x2]=(Driver)d2.clone();
			 return 0;
		 }
		 
	}
	public int changeP3(Driver[][] dr,int k,int n,Node tb[])//add random number passenger
	{
		int c=0;
		for(c=0;c<n;c++)
			if(dr[k][c].capacity!=4) break;
		if(c==n) return 0;
		int x1=(int)(Math.random()*n);
		while(dr[k][x1].capacity<2)
			x1=(int)(Math.random()*n);
		int x2=(int)(Math.random()*n);
		while(x2==x1||dr[k][x2].capacity==4)
			x2=(int)(Math.random()*n);
		int y1=(int)(Math.random()*dr[k][x1].capacity);
		Driver d1=(Driver)dr[k][x1].clone();
		Driver d2=(Driver)dr[k][x2].clone();
		if(dr[k][x2].countTime(dr[k][x2].countdis(dr[k][x2].pd[dr[k][x1].capacity-1].startLoc, dr[k][x1].pd[y1].startLoc), dr[k][x2].velocity, dr[k][x2].realtime, dr[k][x1].pd[y1].startTime, dr[k][x1].pd[y1].finalTime)>0)
		{
			dr[k][x2].register(dr[k][x2], dr[k][x1].pd[y1]);
			for(int i=y1;i<dr[k][x1].capacity-1;i++)
				dr[k][x1].pd[i]=(Passenger)dr[k][x1].pd[i+1].clone();
			dr[k][x1].capacity=dr[k][x1].capacity-1;
			dr[k][x1].distance=drdistance(dr[k][x1]);
			dr[k][x2].distance=drdistance(dr[k][x2]);
			
			
			dr[k][x1].nowLoc.copy(dr[k][x1].nowLoc, dr[k][x1].pd[dr[k][x1].capacity-1].startLoc);
			dr[k][x1].realtime=dr[k][x1].countTime(dr[k][x1].countdis(dr[k][x1].startLoc, dr[k][x1].pd[0].startLoc), dr[k][x1].velocity, dr[k][x1].realtime, dr[k][x1].pd[0].startTime, dr[k][x1].pd[0].finalTime);
			for(int i=0;i<dr[k][x1].capacity-1;i++)
				dr[k][x1].realtime=dr[k][x1].countTime(dr[k][x1].countdis(dr[k][x1].pd[i].startLoc, dr[k][x1].pd[i+1].startLoc), dr[k][x1].velocity, dr[k][x1].realtime, dr[k][x1].pd[i+1].startTime, dr[k][x1].pd[i+1].finalTime);
			if(tb[k].compare(dr[k][x1])==1||tb[k].compare(dr[k][x2])==1)
			{
				 dr[k][x1]=(Driver)d1.clone();
				 dr[k][x2]=(Driver)d2.clone();
				 return 1;
			}
			if(dr[k][x1].distance+dr[k][x2].distance<d1.distance+d2.distance) 
			{
				//while(tb.length()>2*n) tb.out();
				// tb.in(dr[k][x1]);
				// tb.in(dr[k][x2]);
				//dr[k][x1].realtime=countRealTime(dr[k][x1]);
				//dr[k][x2].realtime=countRealTime(dr[k][x1]);
				return 1;
			}
			//else if(Chose(d1.distance+d2.distance,dr[k][x1].distance+dr[k][x2].distance)==1)
				//return 1;
			else
			{
				dr[k][x1]=(Driver)d1.clone();
				dr[k][x2]=(Driver)d2.clone();
				return 0;
			}
		}
		return 0;
	}
	public int changeP4(Driver dr[][],int k,int n) //单个司机最短路径
	{
		for(int i=0;i<n;i++)
		{
			if(dr[k][i].capacity==1||dr[k][i].capacity==0) continue;
			else if(dr[k][i].capacity==2)
			{
				float dis=dr[k][i].countdis(dr[k][i].startLoc, dr[k][i].pd[1].startLoc)+dr[k][i].countdis(dr[k][i].pd[1].startLoc, dr[k][i].pd[0].startLoc)+dr[k][i].countdis(dr[k][i].pd[0].startLoc,dr[k][i].dest);
				if(dis<dr[k][i].distance)
				{
				Passenger p1=(Passenger)dr[k][i].pd[0].clone();
				dr[k][i].pd[0]=(Passenger)dr[k][i].pd[1].clone();
				dr[k][i].pd[1]=(Passenger)p1.clone();
				dr[k][i].distance=dis;
				dr[k][i].realtime=dr[k][i].countTime(dr[k][i].countdis(dr[k][i].startLoc, dr[k][i].pd[0].startLoc),dr[k][i].velocity, dr[k][i].startTime, dr[k][i].pd[0].startTime, dr[k][i].pd[0].finalTime);
				dr[k][i].realtime=dr[k][i].countTime(dr[k][i].countdis(dr[k][i].pd[0].startLoc, dr[k][i].pd[1].startLoc),dr[k][i].velocity, dr[k][i].realtime, dr[k][i].pd[1].startTime, dr[k][i].pd[1].finalTime);
				}
			}
			else if(dr[k][i].capacity==3)
			{
				Passenger p[]=new Passenger[3];
				for(int x=0;x<3;x++)
					p[x]=new Passenger();
				p[0]=(Passenger)dr[k][i].pd[0].clone();
				p[1]=(Passenger)dr[k][i].pd[1].clone();
				p[2]=(Passenger)dr[k][i].pd[2].clone();
				float dis[]=new float[5];
				int flag[][]=new int[5][3];
				flag[0][0]=0;flag[0][1]=2;flag[0][2]=1;
				flag[1][0]=1;flag[1][1]=0;flag[1][2]=2;
				flag[2][0]=1;flag[2][1]=2;flag[2][2]=0;
				flag[3][0]=2;flag[3][1]=0;flag[3][2]=1;
				flag[4][0]=2;flag[4][1]=1;flag[4][2]=0;
				for(int x=0;x<5;x++)
				{
					dis[x]=dr[k][i].countdis(dr[k][i].startLoc, p[flag[x][0]].startLoc);
					for(int y=1;y<3;y++)
						dis[x]+=dr[k][i].countdis(p[flag[x][y-1]].startLoc, p[flag[x][y]].startLoc);
					dis[x]+=dr[k][i].countdis(p[flag[x][2]].startLoc, dr[k][i].dest);
				}
				int best=0;
				for(int x=0;x<5;x++)
				{
					if(dis[x]<dis[best]) best=x;
				}
				if(dis[best]<dr[k][i].distance) 
				{
					dr[k][i].distance=0;
					dr[k][i].realtime=0;
					dr[k][i].capacity=0;
					dr[k][i].nowLoc=(Location)dr[k][i].startLoc.clone();
					for(int y=0;y<4;y++)
						dr[k][i].pd[y]=new Passenger();
					for(int y=0;y<3;y++)
					dr[k][i].register(dr[k][i], p[flag[best][y]]);
					dr[k][i].distance=dis[best];
				}
				else continue;			
				
			
			}
			else
			{
				Passenger p[]=new Passenger[4];
				for(int x=0;x<4;x++)
					p[x]=new Passenger();
				p[0]=(Passenger)dr[k][i].pd[0].clone();
				p[1]=(Passenger)dr[k][i].pd[1].clone();
				p[2]=(Passenger)dr[k][i].pd[2].clone();
				p[3]=(Passenger)dr[k][i].pd[3].clone();
				float dis[]=new float[23];
				int flag[][]=new int[23][4];
				flag[0][0]=0;flag[0][1]=1;flag[0][2]=3;flag[0][3]=2;
				flag[1][0]=0;flag[1][1]=2;flag[1][2]=1;flag[1][3]=3;
				flag[2][0]=0;flag[2][1]=2;flag[2][2]=3;flag[2][3]=1;
				flag[3][0]=0;flag[3][1]=3;flag[3][2]=1;flag[3][3]=2;
				flag[4][0]=0;flag[4][1]=3;flag[4][2]=2;flag[4][3]=1;
				flag[5][0]=1;flag[5][1]=0;flag[5][2]=2;flag[5][3]=3;
				flag[6][0]=1;flag[6][1]=0;flag[6][2]=3;flag[6][3]=2;
				flag[7][0]=1;flag[7][1]=2;flag[7][2]=0;flag[7][3]=3;
				flag[8][0]=1;flag[8][1]=2;flag[8][2]=3;flag[8][3]=0;
				flag[9][0]=1;flag[9][1]=3;flag[9][2]=0;flag[9][3]=2;
				flag[10][0]=1;flag[10][1]=3;flag[10][2]=2;flag[10][3]=0;
				flag[11][0]=2;flag[11][1]=0;flag[11][2]=1;flag[11][3]=3;
				flag[12][0]=2;flag[12][1]=0;flag[12][2]=3;flag[12][3]=1;
				flag[13][0]=2;flag[13][1]=1;flag[13][2]=0;flag[13][3]=3;
				flag[14][0]=2;flag[14][1]=1;flag[14][2]=3;flag[14][3]=0;
				flag[15][0]=2;flag[15][1]=3;flag[15][2]=0;flag[15][3]=1;
				flag[16][0]=2;flag[16][1]=3;flag[16][2]=1;flag[16][3]=0;
				flag[17][0]=3;flag[17][1]=0;flag[17][2]=1;flag[17][3]=2;
				flag[18][0]=3;flag[18][1]=0;flag[18][2]=2;flag[18][3]=1;
				flag[19][0]=3;flag[19][1]=1;flag[19][2]=0;flag[19][3]=2;
				flag[20][0]=3;flag[20][1]=1;flag[20][2]=2;flag[20][3]=0;
				flag[21][0]=3;flag[21][1]=2;flag[21][2]=0;flag[21][3]=1;
				flag[22][0]=3;flag[22][1]=2;flag[22][2]=1;flag[22][3]=0;
				for(int x=0;x<23;x++)
				{
					dis[x]=dr[k][i].countdis(dr[k][i].startLoc, p[flag[x][0]].startLoc);
					for(int y=1;y<4;y++)
						dis[x]+=dr[k][i].countdis(p[flag[x][y-1]].startLoc, p[flag[x][y]].startLoc);
					dis[x]+=dis[x]+=dr[k][i].countdis(p[flag[x][3]].startLoc, dr[k][i].dest);
				}
				int best =0;
				for(int x=0;x<23;x++)
				{
					if(dis[x]<dis[best]) best=x;
				}
				if(dis[best]<dr[k][i].distance) 
				{
					dr[k][i].distance=0;
					dr[k][i].realtime=0;
					dr[k][i].capacity=0;
					dr[k][i].nowLoc=(Location)dr[k][i].startLoc.clone();
					for(int y=0;y<4;y++)
						dr[k][i].pd[y]=new Passenger();
					for(int y=0;y<4;y++)
					dr[k][i].register(dr[k][i], p[flag[best][y]]);
					dr[k][i].distance=dis[best];
					//dr[k][i].realtime=countRealTime(dr[k][i]);
				}
				
				
				else continue;
			}
			
		}
		return 1;
	}
	
	public int changeP5(Driver[][] dr,int k,int n,Node tb[]) //司机与乘客交换
	{
		for(int i=0;i<n;i++)
		{
			Driver[] dc=new Driver[4];
			for(int x=0;x<4;x++) dc[x]=new Driver();
			int f=0;
			for(int j=0;j<dr[k][i].capacity;j++)
			{
				if(dr[k][i].pd[j].haveCar==1&&dr[k][i].pd[j].beenDriver==0)
				{
					dc[f].ID=new String(dr[k][i].pd[j].ID);
					dc[f].startLoc=(Location)dr[k][i].pd[j].startLoc.clone();
					dc[f].nowLoc=(Location)dc[f].startLoc.clone();
					dc[f].startTime=dr[k][i].pd[j].startTime;
					dc[f].finalTime=dr[k][i].pd[j].finalTime;
					f++;
				}
			}
			if(f>0)
			{
				Passenger p=new Passenger();
				p.ID=new String(dr[k][i].ID);
				p.haveCar=1;
				p.beenDriver=dr[k][i].beenDriver;
				p.startLoc=(Location)dr[k][i].startLoc.clone();
				p.startTime=dr[k][i].startTime;
				p.finalTime=dr[k][i].finalTime;
				int best=0;
				for(int l=0;l<f;l++)
				{
					dc[l].register(dc[l], p);
					for(int y=0;y<dr[k][i].capacity;y++)
						if(dr[k][i].pd[y].ID.equals(dc[l].ID)==false) dc[l].register(dc[l], dr[k][i].pd[y]);
					dc[l].distance+=dc[l].countdis(dc[l].nowLoc,dc[l].dest);
					if(dc[l].distance<dc[best].distance) best=l;
					
				}
				//if(tb[k].compare(dc[best])==1)
					//continue;
				if(dc[best].distance<dr[k][i].distance) 
				{
					
					
					dr[k][i]=(Driver)dc[best].clone();
					//dr[k][i].realtime=countRealTime(dr[k][i]);
					//while(tb.length()>2*n) tb.out();
					 //tb.in(dr[k][i]);
				}
				//else if(Chose(dr[k][i].distance,dc[best].distance)==1)
					//dr[k][i]=(Driver)dc[best].clone();
				
			}
		}
		return 0;
	}
	
	public int changeP6(Driver[][] dr,int k,int n,Node tb[],int foodNum) //两个解之间交换
	{
		if(k==foodNum-1) return 0;
		int x1=(int)(Math.random()*n);
		int x2=(int)(Math.random()*n);
		while(x1==x2) x2=(int)(Math.random()*n);
		Driver d1=(Driver)dr[k][x1].clone();
		Driver d2=(Driver)dr[k][x2].clone();
		int m=dr[k][x1].capacity+dr[k][x2].capacity;
		Passenger[] pp=new Passenger[m];
		for(int i=0;i<m;i++) pp[i]=new Passenger();
		int y=0;
		for(int i=0;i<dr[k][x1].capacity;i++)
		{
			pp[y]=(Passenger)dr[k][x1].pd[i].clone();
			y++;
		}
		for(int i=0;i<dr[k][x2].capacity;i++)
		{
			pp[y]=(Passenger)dr[k][x2].pd[i].clone();
			y++;
		}
		for(int i=0;i<m;i++)
			pp[i].dirverID=new String("000");
		for(int i=0;i<dr[k+1][x1].capacity;i++)
		{
			for(int j=0;j<m;j++)
				if(dr[k+1][x1].pd[i].ID.equals(pp[j].ID)) pp[j].dirverID=new String(dr[k+1][x1].ID);
		}
		for(int i=0;i<dr[k+1][x2].capacity;i++)
		{
			for(int j=0;j<m;j++)
				if(dr[k+1][x2].pd[i].ID.equals(pp[j].ID)) pp[j].dirverID=new String(dr[k+1][x2].ID);
		}
		dr[k][x1].capacity=0;
		dr[k][x1].distance=0;
		dr[k][x1].realtime=0;
		dr[k][x1].nowLoc=(Location)dr[k][x1].startLoc.clone();
		for(int c=0;c<4;c++) dr[k][x1].pd[c]=new Passenger();
		dr[k][x2].capacity=0;
		dr[k][x2].distance=0;
		dr[k][x2].realtime=0;
		dr[k][x2].nowLoc=(Location)dr[k][x2].startLoc.clone();
		for(int c=0;c<4;c++) dr[k][x2].pd[c]=new Passenger();
		for(int i=0;i<m;i++)
		{
			if(pp[i].dirverID.equals(dr[k][x1].ID)) 
				{
				  dr[k][x1].register(dr[k][x1], pp[i]);
				  pp[i].ID=new String("000");
				}
			if(pp[i].dirverID.equals(dr[k][x2].ID)) 
				{
				  dr[k][x2].register(dr[k][x2], pp[i]);
				  pp[i].ID=new String("000");
				}
		}
		for(int i=0;i<m;i++)
		{
			if(pp[i].ID.equals("000")) continue;
			else
			{
				if(dr[k][x1].register(dr[k][x1], pp[i])==0)
					dr[k][x2].register(dr[k][x2], pp[i]);
			}
		}
		dr[k][x1].distance+=dr[k][x1].countdis(dr[k][x1].nowLoc, dr[k][x1].dest);
		dr[k][x2].distance+=dr[k][x2].countdis(dr[k][x2].nowLoc, dr[k][x2].dest);
		/*if(tb[k].compare(dr[k][x1])==1||tb[k].compare(dr[k][x2])==1)
		{
			dr[k][x1]=(Driver)d1.clone();
			dr[k][x2]=(Driver)d2.clone();
			changeP6(dr,k,n,tb);
		}*/
		if(dr[k][x1].distance+dr[k][x2].distance<d1.distance+d2.distance)
		{
			//while(tb.length()>2*n) tb.out();
			 //tb.in(dr[k][x1]);
			 //tb.in(dr[k][x2]);
			//dr[k][x1].realtime=countRealTime(dr[k][x1]);
			//dr[k][x2].realtime=countRealTime(dr[k][x2]);
		    return 1;
		}
		//else if(Chose(d1.distance+d2.distance,dr[k][x1].distance+dr[k][x2].distance)==1)
			//return 1;
		else
		{
			dr[k][x1]=(Driver)d1.clone();
			dr[k][x2]=(Driver)d2.clone();
			return 0;
		}
	}
	public float drdistance(Driver dr)
	{
		float dis;
		if(dr.capacity!=0)
		{
			dis=dr.countdis(dr.startLoc, dr.pd[0].startLoc);
			for(int j=0;j<dr.capacity-1;j++)
				dis+=dr.countdis(dr.pd[j].startLoc, dr.pd[j+1].startLoc);
			dis+=dr.countdis(dr.pd[dr.capacity-1].startLoc, dr.dest);
		}
		else
			dis=dr.countdis(dr.startLoc, dr.dest);
		return dis;
	}
	public float sumdistance(Driver[][] dr,int k,int n)
	{
		float[] dis=new float[n];
		for(int i=0;i<n;i++) dis[i]=0;
		for(int i=0;i<n;i++)
		{
			if(dr[k][i].capacity!=0)
			{
				dis[i]=dr[k][i].countdis(dr[k][i].startLoc, dr[k][i].pd[0].startLoc);
				for(int j=0;j<dr[k][i].capacity-1;j++)
					dis[i]+=dr[k][i].countdis(dr[k][i].pd[j].startLoc, dr[k][i].pd[j+1].startLoc);
				dis[i]+=dr[k][i].countdis(dr[k][i].pd[dr[k][i].capacity-1].startLoc, dr[k][i].dest);
			}
			else
			{
				dis[i]=dr[k][i].countdis(dr[k][i].startLoc, dr[k][i].dest);
			}
			
		}
		float sum=0;
		for(int i=0;i<n;i++)
			sum+=dis[i];
		return sum;
	}
	public float countfitness(Driver[][] dr,int k,int n)
	{
		float fitness=0;
		for(int i=0;i<n;i++)
		{
			fitness=fitness+dr[k][i].distance;
		}
		return fitness;
	}
	
	public int rouletterwheel(Driver[][] dr,int m,int n)
	{
		int i=0;
		float[] fit=new float[m];
		float x=(float)(Math.random());
		float total=0;
		for(i=0;i<m;i++)
		{
			fit[i]=countfitness(dr,i,n);
		}
		float maxf=fit[0];
		for(i=0;i<m;i++)
		{
			if(fit[i]>maxf) maxf=fit[i];
		}
		for(i=0;i<m;i++)
		{
			fit[i]=maxf-fit[i];
			total+=fit[i];
		}
		for(i=0;i<m-1;i++)
		{
			for(int j=0;j<m-1;j++)
			{
				if(fit[j]<fit[j+1])
				{
					float temp=fit[j+1];
					fit[j+1]=fit[j];
					fit[j]=temp;
				}
			}
				
		}
		for(i=0;i<m;i++)
			fit[i]=fit[i]/total;
		for(i=0;i<m-1;i++)
		{
			fit[i+1]+=fit[i];
		}
		for(i=0;i<m;i++)
		{
			if(x<fit[i]) return i;
		}
		return -1;
	}
	
	public int employedBee(Driver[][] dr,int foodNum,int n,Node tb[])
	{
		int bestS = 0;
		
		for(int i=0;i<foodNum;i++)
		{
			if(countfitness(dr,i,n)<countfitness(dr,bestS,n)) bestS=i;
		}
		for(int i=0;i<foodNum;i++)
		{
			 
			
			 while(changeP3(dr,i,n,tb)==1);
			 
			 while(changeP2(dr,i,n,tb)==1);
			 
			 while(changeP(dr,i,n,tb)==1);
			 
			 while(changeP6(dr,i,n,tb,foodNum)==1);
			 
			 changeP5(dr,i,n,tb);
			 
			 changeP4(dr,i,n);			
			 
		}
		
		for(int i=0;i<foodNum;i++)
		{
			if(countfitness(dr,i,n)<countfitness(dr,bestS,n)) bestS=i;
		}
		return bestS;
	}
	public int onlookerBee(Driver[][] dr,int foodNum,int n,Node tb[])
	{
		int bestS=0;
		for(int i=0;i<foodNum;i++)
		{
			if(countfitness(dr,i,n)<countfitness(dr,bestS,n)) bestS=i;
		}
		for(int i=0;i<foodNum;i++)
		{
			int x=rouletterwheel(dr,foodNum,n);
			
			while(changeP3(dr,x,n,tb)==1);
			
			 while(changeP2(dr,x,n,tb)==1);
			 
			 while(changeP(dr,x,n,tb)==1);
			
			 while(changeP6(dr,x,n,tb,foodNum)==1);
			
			 changeP5(dr,x,n,tb);
			 
			 changeP4(dr,x,n);
			
		}
		for(int i=0;i<foodNum;i++)
		{
			if(countfitness(dr,i,n)<countfitness(dr,bestS,n)) bestS=i;
		}
		return bestS;
	}
	public int countSimilarity(Driver[][] dr, Driver[] nd, int n,int foodNum)
	{
		int s[]=new int[foodNum];
		int sum=n;
		int counter=0;
		for(int i=0;i<n;i++)
			sum+=nd[i].capacity;
		for(int i=0;i<foodNum;i++)
		{
			s[i]=0;
			for(int j=0;j<n;j++)
				for(int x=0;x<n;x++)
					if(nd[j].ID.equals(dr[i][x].ID)) 
						{
						  s[i]+=1;
						  for(int y=0;y<nd[j].capacity;y++)
							  for(int k=0;k<dr[i][x].capacity;k++)
								  if(nd[j].pd[y].ID.equals(dr[i][x].pd[k].ID)) s[i]+=1;
						}
			
		}
		for(int i=0;i<foodNum;i++)
		{
			if(s[i]==sum) return 0;
			if(s[i]<sum/4) counter++;
		}
		if(counter==(foodNum)) return 2;
		else return 1;
	}
	public int scoutBee(Driver[][] d1,Driver[][] d2,int foodNum,int n,Participate[] p,int m,int best,int mark[])
	{
		int i=0;
		for(i=0;i<foodNum;i++)
		{
			if(mark[i]!=0) continue;
			if(i==best) continue;
			if(countfitness(d1,i,n)<=countfitness(d2,i,n)) break;
		}
		if(i==foodNum) return 1;
		Passenger[] pr= new Passenger[m];
		for(int t=0;t<m;t++)
			pr[t]=new Passenger();
		Driver nd[]=new Driver[n];
		for(int t=0;t<n;t++)
			nd[t]=new Driver();
		int d=n;
		for(int x=0;x<n+m;x++)
		{
			if(p[x].haveCar==1&&p[x].beenDriver==0)
			{
				d=d-1;
				nd[d].ID=new String(p[x].ID);
				nd[d].startLoc.copy(nd[d].startLoc, p[x].startLoc);
				nd[d].nowLoc.copy(nd[d].nowLoc, p[x].startLoc);
				nd[d].velocity=20;
				nd[d].startTime=p[x].startTime;
				nd[d].finalTime=p[x].finalTime;
				//p[x].beenDriver=3;
				if(d==0) break;
			}
			
		}
		int y=0;
		for(int x=0;x<n+m;x++)
		{
			int r=0;
			for(r=0;r<n;r++)
				if(p[x].ID.equals(nd[r].ID)) break;
			if(r>=n)
			{
				pr[y].ID=new String(p[x].ID);
				pr[y].startLoc.copy(pr[y].startLoc,p[x].startLoc );
				pr[y].startTime=p[x].startTime;
				pr[y].finalTime=p[x].finalTime;
				pr[y].haveCar=p[x].haveCar;
				pr[y].beenDriver=p[x].beenDriver;
				y++;
			}
		}
		
		
		for(int x=0;x<m;x++)
		{
			int j;
			for(;;)
			{
				j=(int)(Math.random()*n);
				if(nd[j].countTime(nd[j].countdis(nd[j].nowLoc,pr[x].startLoc),nd[j].velocity,nd[j].realtime,pr[x].startTime,pr[x].finalTime)!=0&&nd[j].capacity<4)
				{
						break;	
				}
			}
				
			
			
			//d2[i][j].realtime=d2[i][j].countTime(d2[i][j].countdis(d2[i][j].nowLoc,pr[x].startLoc),d2[i][j].velocity,d2[i][j].realtime,pr[x].startTime,pr[x].finalTime);
			nd[j].register(nd[j],pr[x]);
		}
		for(int x=0;x<n;x++) nd[x].distance+=nd[x].countdis(nd[x].nowLoc, nd[x].dest);
		int q=countSimilarity(d2,nd,n,foodNum);
		if(q==0)
			scoutBee(d1,d2,foodNum,n,p,m,best,mark);
		else
		for(int t=0;t<n;t++) {
			d2[i][t]=(Driver)nd[t].clone();
			//d2[i][t].realtime=countRealTime(d2[i][t]);
		}
		if(q==2)
			mark[i]=3;
		for(int f=0;f<foodNum;f++)
			if(mark[f]!=0) mark[f]--;
			
		return 0;
	}
	
	

}
