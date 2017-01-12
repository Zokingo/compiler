package compiler;
///import java.util.ArrayList;


public class SynbolNode {//��ʶ���ܱ���
	String value;		//ֵ==>Word.value
	typeNode typenode;	//���ͽ��==>����type�õ���Ӧ�����ͽ��
	String type;		//����==>���嶯��ʱ��������ջ�е��������͹ؼ��ִ�������:int,�ַ�:char,����:bool,����:float
	int tpoint;			//����Ӧ����ָ��,�������ͽ��typenode.tpoint�õ�
	int lengthoff;		//��ռ�ڴ��ֽ���==>typenode.lengthoff�õ�,�������typenode.lengthoff*Clen;�ṹ����ǵ���===>���������Ӧ�ĳ��ȱ���
	String cat;			//���(����λ�ڲ�ͬλ�õ����嶯������:�����������һ��Ϊ��������V)
	int offset;			//���ڴ��е�ƫ��������ں�������ַ(��0��ʼ,ͨ�������ȱ�����������ټ��Ͻ����ռ�ֽ���lengthoff)�õ�
	int ADDR;			//��ַָ��=====>
	int index=0;
	SynbolNode()
	{
		
	}
	SynbolNode(String value)
	{
		this.value=value;
	}
	
	
	SynbolNode(String value,String type,int offSystem)//���뵥�ʵ�ֵ,����,ϵͳ���ݵ�ַƫ����:0��ʼ
	{
		this.value=value;
		this.type=type;
		if(this.type=="����"||this.type=="����"){
			typenode=new typeNode(type);
			this.tpoint=typenode.tpoint;
			lengthoff=4;
			offset=offSystem;
		}else if(this.type=="�ַ�"||this.type=="����"){
			typenode=new typeNode(type);
			this.tpoint=typenode.tpoint;
			lengthoff=1;
			offset=offSystem;
		}else if(this.type=="����"){
			typenode=new typeNode(type);
			this.tpoint=typenode.tpoint;
			lengthoff=8;
			offset=offSystem;
		}else if(this.type=="����"){//����ʱ�����offSystem=������ռ�ڴ�
			typenode=new typeNode(type);
			this.tpoint=typenode.tpoint;
			offset=offSystem;
		}else if(this.type=="�ṹ��"){
			typenode=new typeNode(type);
			this.tpoint=typenode.tpoint;
			offset=offSystem;
		}
	}
	
	public void setCat(String cat){//���嶯��ʱ�ֶ�����
		this.cat=cat;
	}
	
	public void setLengthoff(int lengthoff){//���嶯��ʱ�ֶ�����:�����ƫ����,�ṹ���ƫ����
		this.lengthoff=lengthoff;
	}
	
	public void setOffsystem(int offSystem){//�޸ı�ʶ���ĵ�ַ
		this.offset=offSystem;
	}
	
	public void setTypenode(typeNode tn){//���嶯��ʱ�ֶ�����:�����ƫ����,�ṹ���ƫ����
		this.typenode=tn;
		this.tpoint=typenode.tpoint;
		this.lengthoff=typenode.lengthoff;
		this.type=typenode.type;
	}
	
	public boolean equals(Object obj)
	{
		if(!(obj instanceof SynbolNode))
		{
			return false;
		}
		SynbolNode sn=(SynbolNode)obj;
		return this.value.equals(sn.value);
	}
	
	public void setIndex(int index){
		this.index=index;
	}

	public String getType(String value)
	{
		if(this.value==value)
		{
			return this.type;//����ֵ������
		}else{
			return null;
		}
		
	}
}
