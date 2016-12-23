package compiler;

public class Token {
	
	int i;
	String value;
	String type;
	
	Token()
	{
		
	}
	
	Token(String value,String type)
	{
		this.value=value;
		this.type=type;
		if(type=="标志符"){
			this.i=45;
		}else if(type=="整形常量"){
			this.i=46;
		}else if(type=="字符常量"){
			this.i=47;
		}else if(type=="浮点常量"){
			this.i=48;
		}else if(type=="布尔常量"){
			this.i=49;
		}else if(type=="结束符"){
			this.i=44;
		}else if(type=="未知类型"){//出错了
			this.i=-1;
		}else if(type=="关键字"){//根据value查Word.key,i=Word.key.
			this.i=Word.key.indexOf(value);
		}else if(type=="界符"){
			this.i=Word.boundarySign.indexOf(value)+15;
		}else if(type=="运算符"){
			this.i=Word.operator.indexOf(value)+25;
		}
		
	}
	
	Token(int i,String value,String type)
	{
		this.i=i;
		this.value=value;
		this.type=type;
	}
	
	void set_i(String value)//根据获得的value来设定对应的i
	{
		
	}
	
	public boolean equals(Object obj)
	{
		if(!(obj instanceof Token))
		{
			return false;
		}
		Token cw=(Token)obj;
		return this.value.equals(cw.value)&&this.type.equals(cw.type);
	}
}
