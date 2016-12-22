package compiler;

public class ConstWord 
{
	public final static String INT_CONST 	=  	"整形常量";
	public final static String CHAR_CONST 	= 	"字符常量";
	public final static String BOOL_CONST 	= 	"布尔常量";
	public final static String FLOAT_CONST 	= 	"浮点常量";
	
	String value;	// 单词的值
	String type;	// 单词类型
	int line;		// 单词所在行
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
