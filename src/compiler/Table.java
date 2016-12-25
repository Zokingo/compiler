package compiler;

//import java.util.ArrayList;

class Table {

	//非终结符Vn:S,A,B,C,X,Y,Z,Z1(1),U,U1(2),R,E,E1(3),H,G,H1(4),D,L,L1(5),T,T1(6),F,O,P,Q,I,I1(7),K,R1(8),B1(9)
	public final static int S	=0;
	public final static int A	=1;
	public final static int B	=2;
	public final static int C	=3;
	public final static int X	=4;
	public final static int Y	=5;
	public final static int Z	=6;
	public final static int Z1	=7;
	public final static int U	=8;
	public final static int U1	=9;
	public final static int R	=10;
	public final static int E	=11;
	public final static int E1	=12;
	public final static int H	=13;
	public final static int G	=14;
	public final static int H1	=15;
	public final static int D	=16;
	public final static int L	=17;
	public final static int L1	=18;
	public final static int T	=19;
	public final static int T1	=20;
	public final static int F	=21;
	public final static int O	=22;
	public final static int P	=23;////
	public final static int Q	=24;
	public final static int Q1	=25;
	public final static int I	=26;
	public final static int I1	=27;
	public final static int K	=28;//新增
	public final static int R1	=29;//新增
	public final static int B1	=30;//新增  ===>文法变换方括号运算符[]
	
	
	//终结符
	//-----------------------------------关键字
	public final static int MAIN	=0;
	public final static int PRINTF	=1;
	public final static int SCANF	=2;
	public final static int STRUCT	=3;//struct
	public final static int RETURN	=4;//return
	public final static int IF		=5;//if
	public final static int ELSE	=6;//else
	public final static int DO		=7;//do
	public final static int WHILE	=8;//while
	public final static int FOR		=9;//for
	public final static int VOID	=10;
	public final static int INT		=11;
	public final static int CHAR	=12;
	public final static int BOOL	=13;
	public final static int FLOAT	=14;
	//public final static int TRUE	=15;//没有用到
	//public final static int FALSE	=16;
	
	//-----------------------------------界符
	public final static int SEMI	=15;//;
	public final static int COMMA	=16;//,
	public final static int LMB		=17;//[
	public final static int RMB		=18;//]
	public final static int LBRA	=19;//(
	public final static int RBRA	=20;//)
	public final static int LBIGBRA	=21;//{
	public final static int RBIGBRA	=22;//}
	public final static int CHARDEF	=23;//'
	public final static int STRDEF	=24;//"
	
	public final static int ASS		=25;//=
	public final static int EQ		=26;//==
	public final static int UNEQ	=27;//!=
	public final static int BIG		=28;//>
	public final static int LESS	=29;//<
	public final static int BIGEQ	=30;//>=
	public final static int LESSEQ	=31;//<=
	public final static int ADD		=32;//+
	public final static int SUB		=33;//-
	public final static int MUL		=34;//*
	public final static int DIV		=35;// /
	public final static int AND		=36;//&&
	public final static int OR		=37;//||
	public final static int NON		=38;//!
	public final static int DADD	=39;//++
	public final static int DSUB	=40;//--
	public final static int QUES	=41;//?
	public final static int BOR		=42;//|
	public final static int BAND	=43;//&
	public final static int END		=44;//#
	//-----------------------------------标识符
	public final static int ID		=45;
	//-----------------------------------常量表
	public final static int NUM		=46;
	public final static int CH		=47;
	public final static int FLOATNUM=48;
	public final static int BOOLID	=49;
	
	
	//产生式PRO Vn:S,A,B,C,X,Y,Z,Z1(1),U,U1(2),R,E,E1(3),H,G,H1(4),D,L,L1(5),T,T1(6),F,O,P,Q,Q1(7),I,I1(8),K,R1(9),B1(0)
	//case S:
	String [] PRO_S_void={"void","main","(",")","{","A","}"};
	String [] PRO_S_int={"int","main","(",")","{","A","return","0",";","}"};//new_added
	//String [] PRO_S_subfunc={"Y","id","(","X",")","{","A","}"};//子函数调用//新增
	//case A:
	String [] PRO_A={"C","A"};//新增
	String [] PRO_A_$={"$"};
	//case B:
	String [] PRO_B_printf={"printf","(","P",")",";"};
	String [] PRO_B_scanf={"scanf","(","id",")",";"};
	//String [] PRO_B_if_else={"if","(","E",")","{","A","}","else","{","A","}"};//新增
	String [] PRO_B_if_else={"if","(","E",")","{","A","}","B1","else","{","A","}","B1"};//new_edit
	String [] PRO_B_while={"while","(","E",")","{","A","}"};
	String [] PRO_B_for={"for","(","Y","Z",";","G",";","Q",")","{","A","}"};
	//case C:
	String [] PRO_C_X={"X"};
	String [] PRO_C_B={"B"};
	String [] PRO_C_R={"R"};
	String [] PRO_C_I={"I"};
	//case X:
	String [] PRO_X={"Y","Z"};
	//case Y:
	String [] PRO_Y_int={"int"};
	String [] PRO_Y_char={"char"};
	String [] PRO_Y_bool={"bool"};
	String [] PRO_Y_float={"float"};
	//case Z:
	String [] PRO_Z={"U","Z1"};
	//case Z1(1):
	String [] PRO_Z1={",","Z"};
	String [] PRO_Z1_$={"$"};
	//case U:
	String [] PRO_U={"id","U1"};
	//case U1(2):
	String [] PRO_U1_equal={"=","K"};//new_edit
	String [] PRO_U1_array={"[","num","]",";"};//
	String [] PRO_U1_$={"$"};
	//String [] PRO_U1={"=","L"};
	//case R:
	//String [] PRO_R={"id","=","L"};
	String [] PRO_R_id={"id","=","R1"};//new_edit
	String [] PRO_R_L={"L",";"};//--------------------------------------L、P的选择集合可能有交
	String [] PRO_R_P={"P",";"};//--------------------------------------
	//case E:
	String [] PRO_E={"H","E1"};
	//case E'(3):
	String [] PRO_E1_and={"&&","E"};
	String [] PRO_E1_$={"$"};
	//case H:
	String [] PRO_H={"G","H1"};
	//case G:
	String [] PRO_G={"F","D","F"};
	String [] PRO_G_lbra={"(","E",")"};
	String [] PRO_G_un={"!","E"};
	//case H'(4):
	String [] PRO_H1_or={"||","H"};
	String [] PRO_H1_$={"$"};
	//case D:
	String [] PRO_D_eq={"=="};
	String [] PRO_D_ueq={"!="};
	String [] PRO_D_big={">"};
	String [] PRO_D_less={"<"};//"F<"
	String [] PRO_D_big_eq={">="};
	String [] PRO_D_less_eq={"<="};//new_added
	//case L:
	String [] PRO_L={"T","L1"};
	//case L'(5):
	String [] PRO_L1_add={"+","L"};
	String [] PRO_L1_sub={"-","L"};
	String [] PRO_L1_$={"$"};
	//case T:
	String [] PRO_T={"F","T1"};
	//case T'(6):
	String [] PRO_T1_mul={"*","T"};
	String [] PRO_T1_div={"/","T"};
	String [] PRO_T1_$={"$"};
	//case F:
	String [] PRO_F={"(","L",")"};
	String [] PRO_F_id={"id"};
	String [] PRO_F_num={"num"};//新增
	//case O:
	String [] PRO_O_dadd={"++"};
	String [] PRO_O_dsub={"--"};
	String [] PRO_O_$={"$"};
	//case P:
	String [] PRO_P_id={"id"};
	String [] PRO_P_ch={"ch"};
	String [] PRO_P_num={"num"};
	String [] PRO_P_floatnum={"floatnum"};
	String [] PRO_P_boolid={"boolid"};//new_added
	//case Q:
	String [] PRO_Q_forop={"id","O","Q1"};
	String [] PRO_Q_$={"$"};//新增
	//case Q'(7):
	String [] PRO_Q1_seme={";"};//新
	String [] PRO_Q1_$={"$"};//新
	//case I:
	//String [] PRO_I={"struct","id","{","X","}",";"};
	String [] PRO_I={"struct","id","{","X","}","I1",";"};//new_edit
	//case I'(8):
	String [] PRO_I1_id={"id"};//
	String [] PRO_I1_$={"$"};//
	//case K:
	String [] PRO_K_L={"L"};//
	String [] PRO_K_P={"P"};//
	//case R'(9):
	String [] PRO_R1_L={"L",";"};//
	String [] PRO_R1_P={"P",";"};//
	//case B'(0):
	String [] PRO_B1_else={"{","A","}"};//
	String [] PRO_B1_$={"$"};//
}
