package demo;

class Location implements Cloneable {
	float x;
	float y;
	public Location()
	{
		x=0;
		y=0;
	}
	public void setLocation(float nx, float ny)
	{
		this.x=nx;
		this.y=ny;
	}
	public void copy(Location c,Location b)
	{
		c.x=b.x;
		c.y=b.y;
	}
	@Override
	public Object clone()
	{
		Location l1=new Location();
		/*try{
			l1=(Location)super.clone();
		}
		catch(CloneNotSupportedException e) {  
            e.printStackTrace();
		}*/
		l1.x=x;
		l1.y=y;
		return l1;
	}
}