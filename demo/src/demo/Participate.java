package demo;

class Participate implements Cloneable{
	String ID; //�����ߵ�ID
	int haveCar; //�Ƿ��г�
	int beenDriver; //3�����Ƿ�����ѡ��˾��
	float startTime; //ʱ�䴰��ʼʱ��
	float finalTime; //ʱ�䴰����ʱ��
	Location startLoc;//�����ߵ���ʼλ�� 
	
	public Participate()
	{
		ID="000";
		haveCar=0;
		beenDriver=0;
		startLoc=new Location();
		
	}
	
}
