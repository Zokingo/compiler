package compiler;

public class ConstWord 
{
	public final static String INT_CONST 	=  	"���γ���";
	public final static String CHAR_CONST 	= 	"�ַ�����";
	public final static String BOOL_CONST 	= 	"��������";
	public final static String FLOAT_CONST 	= 	"���㳣��";
	
	String value;	// ���ʵ�ֵ
	String type;	// ��������
	int line;		// ����������
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
}
