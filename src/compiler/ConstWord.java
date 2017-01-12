package compiler;

public class ConstWord 
{
	public final static String INT_CONST 	=  	"整形常量";//offset=4B
	public final static String CHAR_CONST 	= 	"字符常量";//offset=1B
	public final static String BOOL_CONST 	= 	"布尔常量";//offset=1B
	public final static String FLOAT_CONST 	= 	"浮点常量";//offset=8B
	
	String value;	// 单词的值
	String type;	// 单词类型
	int line;		// 单词所在行
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
		if(this.type=="整型常量"){
			offset=4;
		}else if(this.type=="字符常量"||this.type=="布尔常量"){
			offset=1;
		}else if(this.type=="浮点常量"){
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
			return this.type;//返回值的类型
		}else{
			return null;
		}
		
	}
}
