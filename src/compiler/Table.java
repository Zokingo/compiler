package compiler;

//import java.util.ArrayList;

public class Table {
	//N:S,B,A,C,,X,R,Z,Z’,U,U’,E,E’,H,H’,G,M,D,L,L’,T,T’,F,O,P,Q
	/**
	 * 非终结符：N:S,B,A,C,,X,R,Z,Z’,U,U’,E,E’,H,H’,G,M,D,L,L’,T,T’,F,O,P,Q
	 * 新增 I,
	 * 新增 K、R'、B'
	 */
	public final static int S=0;
	public final static int A=1;
	public final static int B=2;
	public final static int C=3;
	public final static int X=4;
	public final static int Y=5;
	public final static int Z=6;
	public final static int Z1=7;
	public final static int U=8;
	public final static int U1=9;
	public final static int R=10;
	public final static int E=11;
	public final static int E1=12;
	public final static int H=13;
	public final static int G=14;
	public final static int H1=15;
	public final static int D=16;
	public final static int L=17;
	public final static int L1=18;
	public final static int T=19;
	public final static int T1=20;
	public final static int F=21;
	public final static int O=22;
	public final static int Q=23;
	public final static int I=24;
	public final static int I1=25;
	public final static int K=26;//新增
	public final static int R1=27;//新增
	public final static int B1=28;//新增  ===>文法变换方括号运算符[]
	 /**
	  * 终结符：main	printf	scanf	void	int 	char	bool	id(自定义变量)	num（int常量）	ch(char常量)
	  * if	else	while	for	;	,(	)	{	}	=	== !=	>	<	+	-	*	/	&&	||	!	++	--	#
	  * 增加  [ ] float floatnum(float常量) struct 
	  */
	public final static int MAIN=0;
	public final static int PRINTF=1;
	public final static int SCANF=2;
	public final static int VOID=3;
	public final static int INT=4;
	public final static int CHAR=5;
	public final static int BOOL=6;
	public final static int ID=7;
	public final static int NUM=8;
	public final static int CH=9;
	public final static int IF=10;
	public final static int ELSE=11;
	public final static int WHILE=12;
	public final static int FOR=13;
	public final static int SEMI=14;
	public final static int COMMA=15;
	public final static int LBRA=16;
	public final static int RBRA=17;
	public final static int LBIGBRA=18;
	public final static int RBIGBRA=19;
	public final static int ASS=20;
	public final static int EQ=21;
	public final static int UNEQ=22;
	public final static int BIG=23;
	public final static int LESS=24;
	public final static int ADD=25;
	public final static int SUB=26;
	public final static int MUL=27;
	public final static int AND=28;
	public final static int OR=29;
	public final static int NON=30;
	public final static int DADD=31;
	public final static int DSUB=32;
	public final static int END=33;
	
	
	public final static int LMB=34;//新增  数组定义[]
	public final static int RMB=35;
	public final static int FLOAT=36;
	public final static int FLOATNUM=37;
	public final static int STRUCT=38;
	public final static int RETURN=38;
	
	/**
	 * 产生式PRO:S,B,A,C,,X,R,Z,Z’,U,U’,E,E’,H,H’,G,M,D,L,L’,T,T’,F,O,P,Q
	 */
	String [] PRO_S_void={"void","main","(",")","{","A","}"};
	String [] PRO_S_int={"int","main","(",")","{","A","return","0",";","}"};//new_added
	String [] PRO_S_subfunc={"Y","id","(","X",")","{","A","}"};//子函数调用//新增
	
	String [] PRO_X={"Y","Z"};
	String [] PRO_Y_int={"int"};
	String [] PRO_Y_char={"char"};
	String [] PRO_Y_bool={"bool"};
	String [] PRO_Y_float={"float"};
	
	
	String [] PRO_Z={"U","Z1"};
	String [] PRO_Z1={",","Z"};
	String [] PRO_Z1_$={"$"};
	String [] PRO_U={"id","U1"};
	//String [] PRO_U1={"=","L"};
	String [] PRO_U1_equal={"=","K"};//new_edit
	String [] PRO_U1_array={"[","num","]",";"};//
	String [] PRO_U1_$={"$"};
	String [] PRO_K_L={"L"};//
	String [] PRO_K_P={"P"};//
	
	//String [] PRO_I={"struct","id","{","X","}",";"};
	String [] PRO_I={"struct","id","{","X","}","I1",";"};//new_edit
	String [] PRO_I1_id={"id"};//
	String [] PRO_I1_$={"$"};//
	
	//String [] PRO_R={"id","=","L"};
	String [] PRO_R_id={"id","=","R1"};//new_edit
	String [] PRO_R_L={"L",";"};//--------------------------------------L、P的选择集合可能有交
	String [] PRO_R_P={"P",";"};//--------------------------------------
	
	String [] PRO_L={"T","L1"};
	String [] PRO_L1_add={"+","L"};
	String [] PRO_L1_sub={"-","L"};
	String [] PRO_L1_$={"$"};
	String [] PRO_T={"F","T1"};
	String [] PRO_T1_mul={"*","T"};
	String [] PRO_T1_div={"/","T"};
	String [] PRO_T1_$={"$"};
	String [] PRO_F={"(","L",")"};
	String [] PRO_F_id={"id"};
	String [] PRO_F_num={"num"};//新增
	
	String [] PRO_O_dadd={"++"};
	String [] PRO_O_dsub={"--"};
	String [] PRO_O_$={"$"};
	String [] PRO_Q_forop={"id","O"};
	String [] PRO_Q_$={"$"};//新增
	
	String [] PRO_E={"H","E1"};
	String [] PRO_E1_and={"&&","E"};
	String [] PRO_E1_$={"$"};
	String [] PRO_H={"G","H1"};
	String [] PRO_H1_or={"||","H"};
	String [] PRO_H1_$={"$"};
	String [] PRO_G={"F","D","F"};
	String [] PRO_G_lbra={"(","E",")"};
	String [] PRO_G_un={"!","E"};
	String [] PRO_D_less={"<"};//"F<"
	String [] PRO_D_big={">"};
	String [] PRO_D_eq={"=="};
	String [] PRO_D_ueq={"!="};
	String [] PRO_D_less_eq={"<="};//new_added
	String [] PRO_D_big_eq={">="};
	
	//String [] PRO_B_if_else={"if","(","E",")","{","A","}","else","{","A","}"};//新增
	String [] PRO_B_if_else={"if","(","E",")","{","A","}","B1","else","{","A","}","B1"};//new_edit
	String [] PRO_B1_else={"{","A","}"};//
	String [] PRO_B1_$={"$"};//
	
	String [] PRO_B_while={"while","(","E",")","{","A","}"};
	String [] PRO_B_for={"for","(","Y","Z",";","G",";","Q",")","{","A","}"};
	
	
	String [] PRO_B_printf={"printf","(","P",")",";"};
	String [] PRO_B_scanf={"scanf","(","id",")",";"};
	String [] PRO_P_id={"id"};
	String [] PRO_P_ch={"ch"};
	String [] PRO_P_num={"num"};
	String [] PRO_P_floatnum={"floatnum"};
	String [] PRO_P_boolid={"boolid"};//new_added
	
	String [] PRO_A={"C","A"};//新增
	String [] PRO_A_$={"$"};
	String [] PRO_C_X={"X"};
	String [] PRO_C_B={"B"};
	String [] PRO_C_R={"R"};
	String [] PRO_C_I={"I"};
}
