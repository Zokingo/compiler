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
		if(type=="��־��"){
			this.i=45;
		}else if(type=="���γ���"){
			this.i=46;
		}else if(type=="�ַ�����"){
			this.i=47;
		}else if(type=="���㳣��"){
			this.i=48;
		}else if(type=="��������"){
			this.i=49;
		}else if(type=="������"){
			this.i=44;
		}else if(type=="δ֪����"){//������
			this.i=-1;
		}else if(type=="�ؼ���"){//����value��Word.key,i=Word.key.
			this.i=Word.key.indexOf(value);
		}else if(type=="���"){
			this.i=Word.boundarySign.indexOf(value)+15;
		}else if(type=="�����"){
			this.i=Word.operator.indexOf(value)+25;
		}
		
	}
	
	Token(int i,String value,String type)
	{
		this.i=i;
		this.value=value;
		this.type=type;
	}
	
	void set_i(String value)//���ݻ�õ�value���趨��Ӧ��i
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
