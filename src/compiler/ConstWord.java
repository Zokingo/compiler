package compiler;

public class ConstWord 
{
	public final static String INT_CONST 	=  	"���γ���";//offset=4B
	public final static String CHAR_CONST 	= 	"�ַ�����";//offset=1B
	public final static String BOOL_CONST 	= 	"��������";//offset=1B
	public final static String FLOAT_CONST 	= 	"���㳣��";//offset=8B
	
	String value;	// ���ʵ�ֵ
	String type;	// ��������
	int line;		// ����������
	int offset=4;

	ConstWord(String value, String type, int line)
	{
		this.value = value;
		this.type = type;
		this.line = line;
	}
	
	ConstWord()
	{
		
	}
	
	ConstWord(String value, String type)
	{
		this.value = value;
		this.type = type;
		if(this.type=="���ͳ���"){
			offset=4;
		}else if(this.type=="�ַ�����"||this.type=="��������"){
			offset=1;
		}else if(this.type=="���㳣��"){
			offset=8;
		}
	}
	
	public boolean equals(Object obj)
	{
		if(!(obj instanceof ConstWord))
		{
			return false;
		}
		ConstWord cw=(ConstWord)obj;
		return this.value.equals(cw.value)&&this.type.equals(cw.type);
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
