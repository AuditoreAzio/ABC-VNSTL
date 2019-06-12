package demo;



public class Node {
	public Driver dr;
	public Node next;
	public Node(Driver db)
	{
		this.dr=(Driver)db.clone();
	}
	public Node()
	{
		this.dr=null;
	}
	public int length()
	{
		int l=0;
		Node temp=this;
		while(temp.next!=null)
		{
			l++;
			temp=temp.next;
		}
		return l;
	}
	public void out()
	{
		this.next=this.next.next;
	}
	public void in(Driver d)
	{
		Node n=new Node(d);
		Node temp=this;
		while(temp.next!=null)
			temp=temp.next;
		temp.next=n;
	}
	public int compare(Driver d)
	{
		Node temp=this;
		while(temp.next!=null)
		{
			int i=0;
			int j=0;
			temp=temp.next;
			if(temp.dr.ID.equals(d.ID)&&temp.dr.capacity==d.capacity)
			{
				for(i=0;i<d.capacity;i++)
				{
					//if(d.pd[i].ID.equals(temp.dr.pd[j].ID)==false) break;
					//j++;
					for(j=0;j<temp.dr.capacity;j++)
						if(d.pd[i].ID.equals(temp.dr.pd[j].ID)) break;
					if(j==temp.dr.capacity) break;
				}
				if(i==d.capacity) return 1;
			}
		}
		return 0;
	}

}
