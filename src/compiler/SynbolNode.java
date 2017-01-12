package compiler;
///import java.util.ArrayList;


public class SynbolNode {//标识符总表结点
	String value;		//值==>Word.value
	typeNode typenode;	//类型结点==>根据type得到对应的类型结点
	String type;		//类型==>语义动作时根据语义栈中的声明类型关键字传入整型:int,字符:char,布尔:bool,浮点:float
	int tpoint;			//结点对应类型指针,根据类型结点typenode.tpoint得到
	int lengthoff;		//所占内存字节数==>typenode.lengthoff得到,数组的是typenode.lengthoff*Clen;结构体的是叠加===>内容填入对应的长度表中
	String cat;			//类别(根据位于不同位置的语义动作产生:语句块声明语句一般为变量类型V)
	int offset;			//在内存中的偏移量相对于函数基地址(从0开始,通过将长度表中内容相加再加上结点所占字节数lengthoff)得到
	int ADDR;			//地址指针=====>
	int index=0;
	SynbolNode()
	{
		
	}
	SynbolNode(String value)
	{
		this.value=value;
	}
	
	
	SynbolNode(String value,String type,int offSystem)//传入单词的值,类型,系统数据地址偏移量:0开始
	{
		this.value=value;
		this.type=type;
		if(this.type=="整型"||this.type=="空型"){
			typenode=new typeNode(type);
			this.tpoint=typenode.tpoint;
			lengthoff=4;
			offset=offSystem;
		}else if(this.type=="字符"||this.type=="布尔"){
			typenode=new typeNode(type);
			this.tpoint=typenode.tpoint;
			lengthoff=1;
			offset=offSystem;
		}else if(this.type=="浮点"){
			typenode=new typeNode(type);
			this.tpoint=typenode.tpoint;
			lengthoff=8;
			offset=offSystem;
		}else if(this.type=="数组"){//数组时传入的offSystem=数组所占内存
			typenode=new typeNode(type);
			this.tpoint=typenode.tpoint;
			offset=offSystem;
		}else if(this.type=="结构体"){
			typenode=new typeNode(type);
			this.tpoint=typenode.tpoint;
			offset=offSystem;
		}
	}
	
	public void setCat(String cat){//语义动作时手动设置
		this.cat=cat;
	}
	
	public void setLengthoff(int lengthoff){//语义动作时手动设置:数组的偏移量,结构体的偏移量
		this.lengthoff=lengthoff;
	}
	
	public void setOffsystem(int offSystem){//修改标识符的地址
		this.offset=offSystem;
	}
	
	public void setTypenode(typeNode tn){//语义动作时手动设置:数组的偏移量,结构体的偏移量
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
			return this.type;//返回值的类型
		}else{
			return null;
		}
		
	}
}
