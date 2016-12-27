package compiler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;
/**
 * 语法分析器
 * @author 
 *
 */
public class Parser {

	private LexAnalyse  	lexAnalyse;//词法分析器
	ArrayList<Word>			wordList		=new ArrayList<Word>();//单词表
	Stack<AnalyseNode>		analyseStack	=new Stack<AnalyseNode>();//分析栈
	Stack<String>			semanticStack	=new Stack<String>();//语义栈
	
	ArrayList<FourElement>	fourElemList	=new ArrayList<FourElement>();//四元式列表
	ArrayList<Error>		errorList		=new ArrayList<Error>();//错误信息列表
	
	ArrayList<pfinfNode> 	pfinfTable		=new ArrayList<pfinfNode>();//函数表
	ArrayList<typeNode> 	typeTable		=new ArrayList<typeNode>();//类型表
	ArrayList<SynbolNode> 	synbolTable		=new ArrayList<SynbolNode>();//符号表总表
	ArrayList<ainfNode> 	ainfTable		=new ArrayList<ainfNode>();//数组表
	ArrayList<rinfNode> 	rinfTable		=new ArrayList<rinfNode>();//结构体表
	ArrayList<lengthNode> 	lengthTable		=new ArrayList<lengthNode>();//长度表
	int 					Totaloff		=0;//系统区距
	boolean 				structFlag		=false;//结构体定义@ASS_I=true开始标识符,为真开始定义@TAB_I时=false
	int 					structNum		=0;//定义的结构体数
	boolean					arrayFlag		=false;//数组定义@ASS_U'开始定义数组,为真开始定义@TAB_U'时=false结束数组定义
	String					arrayType;//数组最小单元的类型//在@ASS_U'时初始化保存下来,在@TAB_U'时复原
	int						arrayNum		=0;//定义的数组数
	
	StringBuffer 			bf;//分析栈缓冲流
	int 					errorCount		=0;//统计错误个数
	boolean 				graErrorFlag	=false;//语法分析出错标志
	int 					tempCount		=0;//用于生成临时变量
	int 					fourElemCount	=0;//统计四元式个数
	
	//Vn
	AnalyseNode 			S,B,A,C,X,Y,R,Z,Z1,U,U1,E,E1,H,H1,G,D,L,L1,T,T1,F,O,P,Q;
	AnalyseNode				I,K;//新增
	
	//语义动作----------------------------------------------------------------------------
	AnalyseNode				TAB_U,TAB_U1,INIT_XOFFSET,ASS_Y,ASS_I,TAB_I;//符号表项生成相关语义动作
	AnalyseNode				TAB_S;//填函数表
	
	AnalyseNode 			ADD_SUB,DIV_MUL,ADD,SUB,DIV,MUL,SINGLE_OP;//算术运算
	AnalyseNode				COMPARE,COMPARE_OP;//逻辑
	AnalyseNode				ASS_F,ASS_R,ASS_Q,ASS_U,TRAN_LF,ASS_U1;//初始化(赋值)
	AnalyseNode 			EQ,EQ_K;//赋值
	AnalyseNode				IF_HEAD,IF_FJ,IF_BACKPATCH_FJ,IF_EL,IFEL_FJ,IFEL_BACKPATCH_FJ,IF_END;//if
	AnalyseNode 			WHILE_HEAD,DO,WHILE_FJ,WHILE_RJ,WHILE_BACKPATCH_FJ,WHILE_END;//while
	AnalyseNode				FOR_HEAD,FOR_LINE_RJ,FOR_FJ,FOR_RJ,SINGLE,FOR_BACKPATCH_FJ,FOR_END;//for
	
	//新增AnalyseNode			TAB_U1,INIT_XOFFSET,ASS_Y,ASS_I,TAB_I,ASS_I1,ASS_U1,EQ_K,IF_HEAD,IF_EL,IF_END,IFEL_FJ,WHILE_HEAD,FOR_HEAD,FOR_LINE_RJ,FOR_END
	AnalyseNode			EQ_U1,IF_BACKPATCH_RJ,IF_RJ;//恢复删除结点
	//-------------------------------------------------------------------------------------
	AnalyseNode 			top;//当前分析栈栈顶元素
	Word 					firstWord;//待分析单词
	
	String 					OP=null;
	String 					ARG1,ARG2,RES;
	Error 					error;
	
	
	//int if_fj,if_rj,while_fj,while_rj,for_fj,for_rj;
	Stack<Integer>			if_fj,if_rj,while_fj,while_rj,for_fj,for_rj;//if while for 跳转地址栈
	Stack<String>			for_op=new Stack<String>();

	
	
	public Parser(){
			
		}

	//得到词法分析的结果
	public Parser(LexAnalyse lexAnalyse){
			this.lexAnalyse	=lexAnalyse;
			this.wordList	=lexAnalyse.wordList;
			init();
		}

	//生成四元式中的临时变量t
	private String newTemp(){
		tempCount++;
		return "T"+tempCount;
	}

	//初始化AnalyseNode(非终结符、语义动作)
	public void init(){
		S	=new AnalyseNode(AnalyseNode.NONTERMINAL, "S", null);
		A	=new AnalyseNode(AnalyseNode.NONTERMINAL, "A", null);
		B	=new AnalyseNode(AnalyseNode.NONTERMINAL, "B", null);
		C	=new AnalyseNode(AnalyseNode.NONTERMINAL, "C", null);
		X	=new AnalyseNode(AnalyseNode.NONTERMINAL, "X", null);
		Y	=new AnalyseNode(AnalyseNode.NONTERMINAL, "Y", null);
		Z	=new AnalyseNode(AnalyseNode.NONTERMINAL, "Z", null);
		Z1	=new AnalyseNode(AnalyseNode.NONTERMINAL, "Z'", null);
		U	=new AnalyseNode(AnalyseNode.NONTERMINAL, "U", null);
		U1	=new AnalyseNode(AnalyseNode.NONTERMINAL, "U'", null);
		E	=new AnalyseNode(AnalyseNode.NONTERMINAL, "E", null);
		E1	=new AnalyseNode(AnalyseNode.NONTERMINAL, "E'", null);
		H	=new AnalyseNode(AnalyseNode.NONTERMINAL, "H", null);
		H1	=new AnalyseNode(AnalyseNode.NONTERMINAL, "H'", null);
		G	=new AnalyseNode(AnalyseNode.NONTERMINAL, "G", null);
		F	=new AnalyseNode(AnalyseNode.NONTERMINAL, "F", null);
		D	=new AnalyseNode(AnalyseNode.NONTERMINAL, "D", null);
		L	=new AnalyseNode(AnalyseNode.NONTERMINAL, "L", null);
		L1	=new AnalyseNode(AnalyseNode.NONTERMINAL, "L'", null);
		T	=new AnalyseNode(AnalyseNode.NONTERMINAL, "T", null);
		T1	=new AnalyseNode(AnalyseNode.NONTERMINAL, "T'", null);
		O	=new AnalyseNode(AnalyseNode.NONTERMINAL, "O", null);
		P	=new AnalyseNode(AnalyseNode.NONTERMINAL, "P", null);
		Q	=new AnalyseNode(AnalyseNode.NONTERMINAL, "Q", null);
		R	=new AnalyseNode(AnalyseNode.NONTERMINAL, "R", null);
		
		I	=new AnalyseNode(AnalyseNode.NONTERMINAL, "I", null);//新增
		K	=new AnalyseNode(AnalyseNode.NONTERMINAL, "K", null);//
		//--------------------------------------------------------------------------------
		//符号表项生成相关语义动作(作用在标识符声明时)
		TAB_S				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@TAB_S",null);
		TAB_U				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@TAB_U", null);
		TAB_U1				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@TAB_U'", null);
		INIT_XOFFSET		=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@INIT_XOFFSET", null);
		ASS_Y				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_Y", null);
		ASS_I				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_I", null);
		TAB_I				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@TAB_I", null);
		//算术运算语义动作
		ADD_SUB				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ADD_SUB", null);
		DIV_MUL				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@DIV_MUL", null);
		ADD					=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ADD", null);
		SUB					=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@SUB", null);
		DIV					=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@DIV", null);
		MUL					=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@MUL", null);
		SINGLE_OP			=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@SINGLE_OP", null);
		//逻辑操作语义动作
		COMPARE				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@COMPARE", null);
		COMPARE_OP			=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@COMPARE_OP", null);
		//初始化(赋值)语义动作
		ASS_F				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_F", null);
		ASS_R				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_R", null);
		ASS_Q				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_Q", null);
		ASS_U				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_U", null);
		TRAN_LF				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@TRAN_LF", null);
		ASS_U1				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_U'", null);
		//赋值语义动作
		EQ					=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@EQ", null);
		//EQ_K				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@EQ_K", null);
		EQ_U1				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@EQ_U'", null);
		//if语句语义动作
		IF_HEAD				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@IF_HEAD", null);
		IF_FJ				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@IF_FJ", null);
		IF_RJ				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@IF_RJ", null);
		IF_BACKPATCH_FJ		=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@IF_BACKPATCH_FJ", null);
		IF_EL				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@IF_EL", null);
		IFEL_FJ				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@IFEL_FJ", null);
		IFEL_BACKPATCH_FJ	=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@IFEL_BACKPATCH_FJ", null);
		IF_END				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@IF_END", null);
		IF_BACKPATCH_RJ		=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@IF_BACKPATCH_RJ", null);
		//while语句语义动作
		WHILE_HEAD			=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@WHILE_HEAD", null);
		DO					=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@DO", null);
		WHILE_FJ			=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@WHILE_FJ", null);
		WHILE_RJ			=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@WHILE_RJ", null);
		WHILE_BACKPATCH_FJ	=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@WHILE_BACKPATCH_FJ", null);
		WHILE_END			=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@WHILE_END", null);
		//for语句语义动作
		FOR_HEAD			=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@FOR_HEAD", null);
		FOR_LINE_RJ			=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@FOR_LINE_RJ", null);
		FOR_FJ				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@FOR_FJ", null);
		FOR_RJ				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@FOR_RJ", null);
		SINGLE				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@SINGLE", null);
		FOR_BACKPATCH_FJ	=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@FOR_BACKPATCH_FJ", null);
		FOR_END				=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@FOR_END", null);
		
		if_fj				=new Stack<Integer>();
		if_rj				=new Stack<Integer>();
		while_fj			=new Stack<Integer>();
		while_rj			=new Stack<Integer>();
		for_fj				=new Stack<Integer>();
		for_rj				=new Stack<Integer>();
		
		//offsetStack			=new Stack<Integer>();//用于计算偏移地址
	}

	//LL1分析方法进行语法分析
	public void grammerAnalyse(){
		if(lexAnalyse.isFail())
			javax.swing.JOptionPane.showMessageDialog(null, "词法分析未通过，不能进行语法分析");
		
		bf=new StringBuffer();
		int gcount=0;
		error=null;
		analyseStack.add(0,S);//分析栈
		analyseStack.add(1,new AnalyseNode(AnalyseNode.END, "#", null));
		semanticStack.add("#");//语义栈
		bf.append("----------------------------------------LL1语法分析记录-------------------------------------------\n");
		while(!analyseStack.empty()&&!wordList.isEmpty()){
			bf.append('\n');
			
			if(gcount++>10000){
				graErrorFlag=true;
				break;
			}
			
			top			=analyseStack.get(0);//当前分析栈顶元素
			firstWord	=wordList.get(0);//待分析单词
			
			
			if(firstWord.value.equals("#")//正常结束
					&&top.name.equals("#")){
				analyseStack.remove(0);
				wordList.remove(0);
			}
			else if(top.name.equals("#")){//当前符‘#’出错
				analyseStack.remove(0);
				graErrorFlag=true;
				break;
			}
			else if(AnalyseNode.isTerm(top)){//终极符时的处理
				 termOP(top.name);
			}else if(AnalyseNode.isNonterm(top)){//非终结符的处理
				nonTermOP(top.name);	
			}else if(top.type.equals(AnalyseNode.ACTIONSIGN)){//栈顶是动作符号时的处理
				actionSignOP();
			}
			bf.append("步骤"+gcount+"\t");
			bf.append("当前分析栈:");
			for(int i=0;i<analyseStack.size();i++){
				bf.append(analyseStack.get(i).name);
			}
			bf.append("\t").append("余留符号串：");
			for(int j=0;j<wordList.size();j++){
				bf.append(wordList.get(j).value);
			}
			bf.append("\t").append("语义栈:");
			for(int k=semanticStack.size()-1;k>=0;k--){
				bf.append(semanticStack.get(k));
			}
		}
	}
	
	//分析栈栈顶为终极符时
	private void termOP(String term){
		if(firstWord.type.equals(Word.INT_CONST)||firstWord.type.equals(Word.CHAR_CONST)||
				firstWord.type.equals(Word.BOOL_CONST)||firstWord.type.equals(Word.FLOAT_CONST)||
				term.equals(firstWord.value)||
				(term.equals("id")&&firstWord.type.equals(Word.IDENTIFIER))){
			analyseStack.remove(0);
			wordList.remove(0);
		}
		else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"termOP语法错误",firstWord.line,firstWord);
			errorList.add(error);
			graErrorFlag=true;
		}	
		
	}

	//栈顶为非终结符时
	private void nonTermOP(String nonTerm){
		if(nonTerm.equals("Z'"))nonTerm="1";
		if(nonTerm.equals("U'"))nonTerm="2";
		if(nonTerm.equals("E'"))nonTerm="3";
		if(nonTerm.equals("H'"))nonTerm="4";
		if(nonTerm.equals("L'"))nonTerm="5";
		if(nonTerm.equals("T'"))nonTerm="6";
		switch(nonTerm.charAt(0)){
		case 'S':
			if(firstWord.value.equals("void")){
				analyseStack.remove(0);
				semanticStack.push("空型");
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "void", null));
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "main", null));
				analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, "(", null));
				analyseStack.add(3,new AnalyseNode(AnalyseNode.TERMINAL, ")", null));
				analyseStack.add(4,new AnalyseNode(AnalyseNode.TERMINAL, "{", null));
				analyseStack.add(5,ASS_Y);
				analyseStack.add(6,TAB_S);
				analyseStack.add(7,A);
				analyseStack.add(8,new AnalyseNode(AnalyseNode.TERMINAL, "}", null));
			}else if(firstWord.value.equals("int")){
				analyseStack.remove(0);
				semanticStack.push("整型");
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "int", null));
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "main", null));
				analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, "(", null));
				analyseStack.add(3,new AnalyseNode(AnalyseNode.TERMINAL, ")", null));
				analyseStack.add(4,new AnalyseNode(AnalyseNode.TERMINAL, "{", null));
				analyseStack.add(5,ASS_Y);
				analyseStack.add(6,TAB_S);
				analyseStack.add(7,A);
				analyseStack.add(8,new AnalyseNode(AnalyseNode.TERMINAL, "return", null));
				analyseStack.add(9,new AnalyseNode(AnalyseNode.TERMINAL, "num", null));
				analyseStack.add(10,new AnalyseNode(AnalyseNode.TERMINAL, ";", null));
				analyseStack.add(11,new AnalyseNode(AnalyseNode.TERMINAL, "}", null));
			}else{
				errorCount++;
				analyseStack.remove(0);
				wordList.remove(0);
				error=new Error(errorCount,"主函数没有返回值",firstWord.line,firstWord);
				errorList.add(error);	
				graErrorFlag=true;
			}
			break;
			
		case 'A':
			if(firstWord.value.equals("int")||firstWord.value.equals("char")
					||firstWord.value.equals("bool")||firstWord.value.equals("float")){
				analyseStack.remove(0);
				analyseStack.add(0,C);
				analyseStack.add(1,A);
			}else if(firstWord.value.equals("printf")){
				analyseStack.remove(0);
				analyseStack.add(0,C);
				analyseStack.add(1,A);
			}else if(firstWord.value.equals("scanf")){
				analyseStack.remove(0);
				analyseStack.add(0,C);
				analyseStack.add(1,A);
			}else if(firstWord.value.equals("if")){
				analyseStack.remove(0);
				analyseStack.add(0,C);
				analyseStack.add(1,A);
			}else if(firstWord.value.equals("while")){
				analyseStack.remove(0);
				analyseStack.add(0,C);
				analyseStack.add(1,A);
			}else if(firstWord.value.equals("for")){
				analyseStack.remove(0);
				analyseStack.add(0,C);
				analyseStack.add(1,A);
			}else if(firstWord.value.equals("struct")){
				analyseStack.remove(0);
				analyseStack.add(0,C);
				analyseStack.add(1,A);
			}else if(firstWord.type.equals(Word.IDENTIFIER)){
				analyseStack.remove(0);
				analyseStack.add(0,C);
				analyseStack.add(1,A);
			}else{
				analyseStack.remove(0);
			}
			break;
		
		case 'B':
			if(firstWord.value.equals("printf")){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "printf", null));
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "(", null));
				analyseStack.add(2,P);
				analyseStack.add(3,new AnalyseNode(AnalyseNode.TERMINAL, ")", null));
				analyseStack.add(4,new AnalyseNode(AnalyseNode.TERMINAL, ";", null));
				analyseStack.add(5,A);
			}
			else if(firstWord.value.equals("scanf")){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "scanf", null));
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "(", null));
				analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, "id", null));
				analyseStack.add(3,new AnalyseNode(AnalyseNode.TERMINAL, ")", null));
				analyseStack.add(4,new AnalyseNode(AnalyseNode.TERMINAL, ";", null));
				analyseStack.add(5,A);
			}
			else if(firstWord.value.equals("if")){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "if", null));
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "(", null));
				analyseStack.add(2,G);
				analyseStack.add(3,new AnalyseNode(AnalyseNode.TERMINAL, ")", null));
				analyseStack.add(4,IF_HEAD);
				analyseStack.add(5,IF_FJ);
				analyseStack.add(6,new AnalyseNode(AnalyseNode.TERMINAL, "{", null));
				analyseStack.add(7,A);
				analyseStack.add(8,new AnalyseNode(AnalyseNode.TERMINAL, "}", null));
				analyseStack.add(9,IF_BACKPATCH_FJ);
				analyseStack.add(10,IF_EL);
				analyseStack.add(11,new AnalyseNode(AnalyseNode.TERMINAL, "else", null));
				analyseStack.add(12,new AnalyseNode(AnalyseNode.TERMINAL, "{", null));
				analyseStack.add(13,A);
				analyseStack.add(14,new AnalyseNode(AnalyseNode.TERMINAL, "}", null));
				analyseStack.add(15,IF_END);
			}
			else if(firstWord.value.equals("while")){
				analyseStack.remove(0);
				analyseStack.add(0,WHILE_HEAD);
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "while", null));
				analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, "(", null));
				analyseStack.add(3,G);
				analyseStack.add(4,new AnalyseNode(AnalyseNode.TERMINAL, ")", null));
				analyseStack.add(5,DO);
				analyseStack.add(6,WHILE_FJ);
				analyseStack.add(7,new AnalyseNode(AnalyseNode.TERMINAL, "{", null));
				analyseStack.add(8,A);
				analyseStack.add(9,new AnalyseNode(AnalyseNode.TERMINAL, "}", null));
				analyseStack.add(10,WHILE_RJ);
				analyseStack.add(11,WHILE_BACKPATCH_FJ);
				analyseStack.add(12,WHILE_END);
			}
			else if(firstWord.value.equals("for")){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "for", null));
				analyseStack.add(1,FOR_HEAD);
				analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, "(", null));
				analyseStack.add(3,Y);
				analyseStack.add(4,Z);
				analyseStack.add(5,new AnalyseNode(AnalyseNode.TERMINAL, ";", null));
				analyseStack.add(6,FOR_LINE_RJ);//存真跳点
				analyseStack.add(7,G);
				analyseStack.add(8,FOR_FJ);
				analyseStack.add(9,new AnalyseNode(AnalyseNode.TERMINAL, ";", null));
				analyseStack.add(10,Q);
				analyseStack.add(11,new AnalyseNode(AnalyseNode.TERMINAL, ")", null));
				analyseStack.add(12,new AnalyseNode(AnalyseNode.TERMINAL, "{", null));
				analyseStack.add(13,A);
				analyseStack.add(14,SINGLE);
				analyseStack.add(15,new AnalyseNode(AnalyseNode.TERMINAL, "}", null));
				analyseStack.add(16,FOR_RJ);
				analyseStack.add(17,FOR_BACKPATCH_FJ);
				analyseStack.add(18,FOR_END);
			}
			else{
				analyseStack.remove(0);
			}
			break;
			
		case 'C':
				analyseStack.remove(0);
				analyseStack.add(0,X);
				analyseStack.add(1,B);
				analyseStack.add(2,R);
				analyseStack.add(3,I);
			break;
			
		case 'X':
			if(firstWord.value.equals("int")||firstWord.value.equals("char")
				||firstWord.value.equals("bool")||firstWord.value.equals("float")){
				analyseStack.remove(0);
				analyseStack.add(0,Y);
				analyseStack.add(1,Z);
				analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, ";", null));
				analyseStack.add(3,INIT_XOFFSET);
			}else{
				analyseStack.remove(0);
			}
			break;
			
		case 'Y':
			if(firstWord.value.equals("int")){
				analyseStack.remove(0);
				analyseStack.add(0,ASS_Y);
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "int", null));
			}else if(firstWord.value.equals("char")){
				analyseStack.remove(0);
				analyseStack.add(0,ASS_Y);//
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "char", null));
			}else if(firstWord.value.equals("bool")){
				analyseStack.remove(0);
				analyseStack.add(0,ASS_Y);//
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "bool", null));
			}else if(firstWord.value.equals("float")){
				analyseStack.remove(0);
				analyseStack.add(0,ASS_Y);//
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "float", null));
			}else{
				errorCount++;
				analyseStack.remove(0);
				wordList.remove(0);
				error=new Error(errorCount,"非法数据类型",firstWord.line,firstWord);
				errorList.add(error);	
				graErrorFlag=true;
			}
			break;
			
		case 'Z':
			if(firstWord.type.equals(Word.IDENTIFIER)){
				analyseStack.remove(0);
				analyseStack.add(0,U);
				analyseStack.add(1,Z1);
			}else{
				errorCount++;
				analyseStack.remove(0);
				wordList.remove(0);
				error=new Error(errorCount,"非法标识符",firstWord.line,firstWord);
				errorList.add(error);
				graErrorFlag=true;
			}
			break;
			
		case '1'://z'
			if(firstWord.value.equals(",")){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, ",", null));
				semanticStack.push(synbolTable.get(synbolTable.size()-1).type);//类型和符号表最后一个的类型一样，因此将它取出来再压入语义栈中即可
				analyseStack.add(1,Z);
			}else if(firstWord.value.equals("[")){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "[", null));
				analyseStack.add(1,ASS_U1);
				analyseStack.add(2,TAB_U);//开始定义多维数组
				analyseStack.add(3,new AnalyseNode(AnalyseNode.TERMINAL, "num", null));
				analyseStack.add(4,new AnalyseNode(AnalyseNode.TERMINAL, "]", null));
				analyseStack.add(5,U1);//进行多维数组定义
				analyseStack.add(6,TAB_U1);//多维数组定义完
			}else{
				analyseStack.remove(0);
			}
			break;
			
		case 'U':
			if(firstWord.type.equals(Word.IDENTIFIER)){
				analyseStack.remove(0);
				analyseStack.add(0,ASS_U);
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "id", null));
				analyseStack.add(2,U1);
			}else{
				errorCount++;
				analyseStack.remove(0);
				wordList.remove(0);
				error=new Error(errorCount,"非法标识符",firstWord.line,firstWord);
				errorList.add(error);
				graErrorFlag=true;
			}
			break;
			
		case '2'://U'
			if(firstWord.value.equals("=")){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "=", null));
				analyseStack.add(1,L);
				analyseStack.add(2,EQ_U1);
			}else if(firstWord.value.equals("[")){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "[", null));
				analyseStack.add(1,ASS_U1);
				analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, "num", null));
				analyseStack.add(3,new AnalyseNode(AnalyseNode.TERMINAL, "]", null));
				//analyseStack.add(4,TAB_U1);
			}else{			
				analyseStack.remove(0);
			}
			break;
			
		case 'R':
			if(firstWord.type.equals(Word.IDENTIFIER)){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_R", null));
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "id", null));
				analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, "=", null));
				analyseStack.add(3,L);
				analyseStack.add(4,EQ);
				analyseStack.add(5,new AnalyseNode(AnalyseNode.TERMINAL, ";", null));
			}
			else{
				analyseStack.remove(0);
			}
			break;

			
		case 'E':
			if(firstWord.type.equals(Word.IDENTIFIER)){
				analyseStack.remove(0);
				analyseStack.add(0,H);
				analyseStack.add(1,E1);
			}else if(firstWord.type.equals(Word.INT_CONST)){
				analyseStack.remove(0);
				analyseStack.add(0,H);
				analyseStack.add(1,E1);
			}else if(firstWord.value.equals("(")||firstWord.value.equals("!")){
				analyseStack.remove(0);
				analyseStack.add(0,H);
				analyseStack.add(1,E1);
			}
			else{
				errorCount++;
				analyseStack.remove(0);
				wordList.remove(0);
				error=new Error(errorCount,"不能进行算术运算的数据类型",firstWord.line,firstWord);
				errorList.add(error);	
				graErrorFlag=true;
			}
			break;
			
		case '3'://E'
			if(firstWord.value.equals("&&")){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "&&", null));
				analyseStack.add(1,E);
			}else {
				analyseStack.remove(0);
			}
			break;
			
		case 'H':
			if(firstWord.type.equals(Word.IDENTIFIER)){
				analyseStack.remove(0);
				analyseStack.add(0,G);
				analyseStack.add(1,H1);
			}else if(firstWord.type.equals(Word.INT_CONST)){
				analyseStack.remove(0);
				analyseStack.add(0,G);
				analyseStack.add(1,H1);
			}else if(firstWord.value.equals("(")||firstWord.value.equals("!")){
				analyseStack.remove(0);
				analyseStack.add(0,G);
				analyseStack.add(1,H1);
			}
			else{
				errorCount++;
				analyseStack.remove(0);
				wordList.remove(0);
				error=new Error(errorCount,"不能进行算术运算的数据类型",firstWord.line,firstWord);
				errorList.add(error);	
				graErrorFlag=true;
			}
			break;
			
		case 'G':
			if(firstWord.type.equals(Word.IDENTIFIER)){
				analyseStack.remove(0);
				analyseStack.add(0,F);
				analyseStack.add(1,D);
				analyseStack.add(2,F);
				analyseStack.add(3,COMPARE);
			}else if(firstWord.type.equals(Word.INT_CONST)){
				analyseStack.remove(0);
				analyseStack.add(0,F);
				analyseStack.add(1,D);
				analyseStack.add(2,F);
				analyseStack.add(3,COMPARE);
			}
			else if(firstWord.value.equals("(")){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "(", null));
				analyseStack.add(1,E);
				analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, ")", null));
			}
			else if(firstWord.value.equals("!")){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "!", null));
				analyseStack.add(1,E);
			}
			else{
				errorCount++;
				analyseStack.remove(0);
				wordList.remove(0);
				error=new Error(errorCount,"不能进行算术运算的数据类型或括号不匹配",firstWord.line,firstWord);
				errorList.add(error);
				graErrorFlag=true;
			}
			break;	
			
		case '4'://H'
			if(firstWord.value.equals("||")){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "||", null));
				analyseStack.add(1,E);
			}else {
				analyseStack.remove(0);
			}
			break;
			
		case 'D':
			if(firstWord.value.equals("==")){
				analyseStack.remove(0);
				analyseStack.add(0,COMPARE_OP);
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "==", null));
			}else if(firstWord.value.equals("!=")){
				analyseStack.remove(0);
				analyseStack.add(0,COMPARE_OP);
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "!=", null));
				
			}else if(firstWord.value.equals(">")){
				analyseStack.remove(0);
				analyseStack.add(0,COMPARE_OP);
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, ">", null));
			}else if(firstWord.value.equals("<")){
				analyseStack.remove(0);
				analyseStack.add(0,COMPARE_OP);
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "<", null));
			}else if(firstWord.value.equals(">=")){//新增
				analyseStack.remove(0);
				analyseStack.add(0,COMPARE_OP);
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, ">=", null));
			}else if(firstWord.value.equals("<=")){//新增
				analyseStack.remove(0);
				analyseStack.add(0,COMPARE_OP);
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "<=", null));
			}else{
				errorCount++;
				analyseStack.remove(0);
				wordList.remove(0);
				error=new Error(errorCount,"非法运算符",firstWord.line,firstWord);
				errorList.add(error);	
				graErrorFlag=true;
			}
			break;
			
		
			
		case 'L':
			if(firstWord.type.equals(Word.IDENTIFIER)){
				analyseStack.remove(0);
				analyseStack.add(0,T);
				analyseStack.add(1,L1);
				analyseStack.add(2,ADD_SUB);
			}else if(firstWord.type.equals(Word.INT_CONST)){
				analyseStack.remove(0);
				analyseStack.add(0,T);
				analyseStack.add(1,L1);
				analyseStack.add(2,ADD_SUB);
			}else if(firstWord.type.equals(Word.CHAR_CONST)){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "ch", null));
				//
				semanticStack.push(firstWord.value);
			}else if(firstWord.type.equals(Word.FLOAT_CONST)){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "floatnum", null));
				semanticStack.push(firstWord.value);
			}
			else if(firstWord.value.equals("(")){
				analyseStack.remove(0);
				analyseStack.add(0,T);
				analyseStack.add(1,L1);
				analyseStack.add(2,ADD_SUB);
			}
			else{
				errorCount++;
				analyseStack.remove(0);
				wordList.remove(0);
				error=new Error(errorCount,"不能进行算术运算的数据类型或括号不匹配",firstWord.line,firstWord);
				errorList.add(error);
				graErrorFlag=true;
			}
			break;
		case '5'://l'
			if(firstWord.value.equals("+")){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "+", null));
				analyseStack.add(1,L);
				analyseStack.add(2,ADD);
			}
			else if(firstWord.value.equals("-")){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "-", null));
				analyseStack.add(1,L);
				analyseStack.add(2,SUB);
			}else{
				analyseStack.remove(0);
			}
			break;
	
		case 'T':
			if(firstWord.type.equals(Word.IDENTIFIER)){
				analyseStack.remove(0);
				analyseStack.add(0,F);
				analyseStack.add(1,T1);
				analyseStack.add(2,DIV_MUL);
			}else if(firstWord.type.equals(Word.INT_CONST)){
				analyseStack.remove(0);
				analyseStack.add(0,F);
				analyseStack.add(1,T1);
				analyseStack.add(2,DIV_MUL);
			}
			else if(firstWord.value.equals("(")){
				analyseStack.remove(0);
				analyseStack.add(0,F);
				analyseStack.add(1,T1);
				analyseStack.add(2,DIV_MUL);
			}
			else{
				errorCount++;
				analyseStack.remove(0);
				wordList.remove(0);
				error=new Error(errorCount,"不能进行算术运算的数据类型",firstWord.line,firstWord);
				errorList.add(error);	
				graErrorFlag=true;
			}
			break;
		case '6'://T'
			if(firstWord.value.equals("*")){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "*", null));
				analyseStack.add(1,T);
				analyseStack.add(2,MUL);
			}
			else if(firstWord.value.equals("/")){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "/", null));
				analyseStack.add(1,T);
				analyseStack.add(2,DIV);
			}else {
				analyseStack.remove(0);
			}
			break;
		case 'F':
			if(firstWord.type.equals(Word.IDENTIFIER)){
				analyseStack.remove(0);
				analyseStack.add(0,ASS_F);
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "id", null));
			}else if(firstWord.type.equals(Word.INT_CONST)){
				analyseStack.remove(0);
				analyseStack.add(0,ASS_F);
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "num", null));
			}else if(firstWord.value.equals("(")){
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "(", null));
				analyseStack.add(1,L);
				analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, ")", null));
				analyseStack.add(3,TRAN_LF);
			}
			else{
				analyseStack.remove(0);
			}
			break;
		case 'O':
			if(firstWord.value.equals("++")){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.ACTIONSIGN, "@SINGLE_OP", null));
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "++", null));
			}else if(firstWord.value.equals("--")){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.ACTIONSIGN, "@SINGLE_OP", null));
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "--", null));
	
			}else {
				analyseStack.remove(0);
			}
			break;
					
		case 'P':
			if(firstWord.type.equals(Word.IDENTIFIER)){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "id", null));
			}else if(firstWord.type.equals(Word.INT_CONST)){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "num", null));
			}else if(firstWord.type.equals(Word.CHAR_CONST)){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "ch", null));
			}
			else if(firstWord.type.equals(Word.FLOAT_CONST)){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "floatnum", null));
			}
			else{
				errorCount++;
				analyseStack.remove(0);
				wordList.remove(0);
				error=new Error(errorCount,"不能输出的数据类型",firstWord.line,firstWord);
				errorList.add(error);	
				graErrorFlag=true;
			}
			break;
			
		case 'Q'://Q
			if(firstWord.type.equals(Word.IDENTIFIER)){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_Q", null));
				analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "id", null));
				analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, "O", null));
			}else {
				analyseStack.remove(0);
			}
			break;
			
		case 'I':
			if(firstWord.value.equals("struct")){
				analyseStack.remove(0);
				analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "struct", null));
				analyseStack.add(1,ASS_I);
				analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, "id", null));
				analyseStack.add(3,new AnalyseNode(AnalyseNode.TERMINAL, "{", null));
				analyseStack.add(4,A);
				analyseStack.add(5,new AnalyseNode(AnalyseNode.TERMINAL, "}", null));
				analyseStack.add(6,TAB_I);
				analyseStack.add(7,new AnalyseNode(AnalyseNode.TERMINAL, ";", null));
			}else{
				analyseStack.remove(0);
			}
			break;
		}
	}

	//栈顶是动作符号时的处理//根据对应语义动作生成四元式
	private void actionSignOP(){
		if(top.name.equals("@TAB_S")){
			analyseStack.remove(0);
			Totaloff=0;
			if(semanticStack.size()>0){
				SynbolNode snm=new SynbolNode("main",semanticStack.pop(),Totaloff);
				snm.setCat("函数");
				synbolTable.add(snm);
				Totaloff+=snm.lengthoff;
			}
			pfinfNode pf=new pfinfNode();
			pfinfTable.add(pf);
		}else if(top.name.equals("@TAB_U")){
			analyseStack.remove(0);
			arrayFlag=true;//说明开始定义多维数组
		}else if(top.name.equals("@TAB_U'")){
			analyseStack.remove(0);
			arrayFlag=false;//说明定义完了数组
		}else if(top.name.equals("@INIT_XOFFSET")){
			analyseStack.remove(0);
		}else if(top.name.equals("@ASS_Y")){
			analyseStack.remove(0);
			String temp_y=analyseStack.firstElement().name;
			if(temp_y.equals("void")){
				typeNode tn=new typeNode("空型");
				if(typeTable.size()==0){
					typeTable.add(tn);
				}else if(!typeTable.contains(tn)){
					typeTable.add(tn);
				}
				semanticStack.push(tn.type);
			}else if(temp_y.equals("int")){
				typeNode tn=new typeNode("整型");
				if(typeTable.size()==0){
					typeTable.add(tn);
				}else if(!typeTable.contains(tn)){
					typeTable.add(tn);
				}
				semanticStack.push(tn.type);
			}else if(temp_y.equals("char")){
				typeNode tn=new typeNode("字符");
				if(typeTable.size()==0){
					typeTable.add(tn);
				}else if(!typeTable.contains(tn)){
					typeTable.add(tn);
				}
				semanticStack.push(tn.type);
			}else if(temp_y.equals("bool")){
				typeNode tn=new typeNode("布尔");
				if(typeTable.size()==0){
					typeTable.add(tn);
				}else if(!typeTable.contains(tn)){
					typeTable.add(tn);
				}
				semanticStack.push(tn.type);
			}else if(temp_y.equals("float")){
				typeNode tn=new typeNode("浮点");
				if(typeTable.size()==0){
					typeTable.add(tn);
				}else if(!typeTable.contains(tn)){
					typeTable.add(tn);
				}
				semanticStack.push(tn.type);
			}
		}else if(top.name.equals("@ASS_I")){
			analyseStack.remove(0);
			String res=firstWord.value;
			typeNode tn2 =new typeNode("结构体");
			structNum++;
			tn2.index=structNum;
			
			if(typeTable.size()==0){
				typeTable.add(tn2);
			}else if(!typeTable.contains(tn2)){
				typeTable.add(tn2);
			}
			
			SynbolNode sn2=new SynbolNode(res,tn2.type,Totaloff);
			sn2.setIndex(structNum);
			sn2.setCat("变量");
			
			synbolTable.add(sn2);
			structFlag=true;
			rinfNode rinfnode=new rinfNode(res);
			rinfnode.setIndex(structNum);
			rinfTable.add(rinfnode);
		}else if(top.name.equals("@TAB_I")){
			analyseStack.remove(0);
			structFlag=false;
		}//算术运算语义动作
		else if(top.name.equals("@ADD_SUB")){
			if(OP!=null&&(OP.equals("+")||OP.equals("-"))){
				ARG2=semanticStack.pop();
				ARG1=semanticStack.pop();
				RES=newTemp();
				FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1,ARG2,RES);
				fourElemList.add(fourElem);
				L.value=RES;
				semanticStack.push(L.value);
				OP=null;
			}
			analyseStack.remove(0);
		}else if(top.name.equals("@DIV_MUL")){
			if(OP!=null&&(OP.equals("*")||OP.equals("/"))){
				ARG2=semanticStack.pop();
				ARG1=semanticStack.pop();
				RES=newTemp();
				FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1,ARG2,RES);
				fourElemList.add(fourElem);
				T.value=RES;
				semanticStack.push(T.value);
				OP=null;
			}
			analyseStack.remove(0);
		}else if(top.name.equals("@ADD")){
			OP="+";
			analyseStack.remove(0);
		}else if(top.name.equals("@SUB")){
			OP="-";
			analyseStack.remove(0);
		}
		else if(top.name.equals("@DIV")){
			OP="/";
			analyseStack.remove(0);
			}
		else if(top.name.equals("@MUL")){
			OP="*";
			analyseStack.remove(0);
		}else if(top.name.equals("@SINGLE_OP")){
			for_op.push(firstWord.value);
			analyseStack.remove(0);
		}//逻辑操作语义动作
		else if(top.name.equals("@COMPARE")){
			ARG2=semanticStack.pop();
			OP=semanticStack.pop();
			ARG1=semanticStack.pop();
			RES=newTemp();
			FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1,ARG2,RES);
			fourElemList.add(fourElem);
			G.value=RES;
			semanticStack.push(G.value);
			OP=null;
			analyseStack.remove(0);
		}else if(top.name.equals("@COMPARE_OP")){
			D.value=firstWord.value;
			semanticStack.push(D.value);
			analyseStack.remove(0);
		}//初始化(赋值)语义动作
		else if(top.name.equals("@ASS_F")){
			F.value=firstWord.value;
			semanticStack.push(F.value);
			analyseStack.remove(0);
		}else if(top.name.equals("@ASS_R")){
			R.value=firstWord.value;//让分析结点的属性值等于当前符号的属性值
			semanticStack.push(R.value);
			analyseStack.remove(0);
		}else if(top.name.equals("@ASS_Q")){
			Q.value=firstWord.value;
			semanticStack.push(Q.value);
			analyseStack.remove(0);
		}
		else if(top.name.equals("@ASS_U")){
			U.value=firstWord.value;
			if(semanticStack.size()>0){
				SynbolNode sn=new SynbolNode(U.value,semanticStack.pop(),Totaloff);
				if(sn.typenode.tpoint==0){
					Totaloff=sn.lengthoff+sn.offset;
				}
				
				if(structFlag==true)
				{
					sn.setCat("域名");
					if(synbolTable.size()==0)
					{
						synbolTable.add(sn);
						rinfTable.get(rinfTable.size()-1).putElement(sn.lengthoff, sn);
					}else{
						if(!synbolTable.contains(sn)){
							synbolTable.add(sn);
							rinfTable.get(rinfTable.size()-1).putElement(sn.lengthoff, sn);
						}else{
							errorCount++;
							error=new Error(errorCount,"语义分析出错,单词重定义",firstWord.line,firstWord);
							errorList.add(error);	
							graErrorFlag=true;
						}
					}
				}else{
					sn.setCat("变量");//变量
					if(synbolTable.size()==0)
					{
						synbolTable.add(sn);
					}else{
						if(!synbolTable.contains(sn)){
							synbolTable.add(sn);
						}else{//出错,重定义
							errorCount++;
							error=new Error(errorCount,"语义分析出错,单词重定义",firstWord.line,firstWord);
							errorList.add(error);	
							graErrorFlag=true;
						}
					}
				}
			}
			semanticStack.push(U.value);
			analyseStack.remove(0);
		}else if(top.name.equals("@TRAN_LF")){//属性赋值语义动作
			F.value=L.value;
			//semanticStack.push(F.value);
			analyseStack.remove(0);
		}else if(top.name.equals("@ASS_U'")){//填数组表项、填
			analyseStack.remove(0);
			typeNode tn1=new typeNode("数组");//类型结点
			arrayNum++;
			tn1.setIndex(arrayNum);
			
			if(typeTable.size()==0){
				typeTable.add(tn1);
			}else if(!typeTable.contains(tn1)){
				typeTable.add(tn1);//tn2
			}
			//获取刚刚填到标识符表的数组标识名:类型信息
			SynbolNode snode1=synbolTable.get(synbolTable.size()-1);
			
			String res=firstWord.value;//得到数组下标
			int up=Integer.parseInt(res);
			ainfNode an_temp=new ainfNode();
			
			if(snode1.type=="数组"){//说明定义的不是第一维,数组表肯定不为空,要修改上一维数数组结点的类型信息,成分类型以及,成分长度
				ainfNode an=new ainfNode(up,new SynbolNode(snode1.value,arrayType,Totaloff));//按照第一维的值单元初始化数组结点
				an_temp=an;
				for(int i=ainfTable.size()-1;i>=0;i--){
					if(ainfTable.get(i).equals(an)){
						ainfTable.get(i).setPreDiemension("数组", 1, an_temp.offsize);
						an_temp=ainfTable.get(i);
					}else{
						break;
					}
				}
				ainfTable.add(an);
				int oldlength=tn1.lengthoff;
				Totaloff-=synbolTable.get(synbolTable.size()-1).lengthoff;//Totaloff减去原先第一维的长度Totaloff-=synbolTable.get(ainCount).lengthoff;//Totaloff减去原先第一维的长度
				tn1.setlengthoff(an_temp.offsize-oldlength);
				synbolTable.get(synbolTable.size()-1).setTypenode(tn1);
				synbolTable.get(synbolTable.size()-1).setIndex(arrayNum);
				Totaloff=synbolTable.get(synbolTable.size()-1).offset+synbolTable.get(synbolTable.size()-1).lengthoff-oldlength;
			}else{
				arrayType=snode1.type;//第一维
				ainfNode an=new ainfNode(up,snode1);
				ainfTable.add(an);
				//
				int oldlength=tn1.lengthoff;
				tn1.setlengthoff(an.offsize-oldlength);
				synbolTable.get(synbolTable.size()-1).setTypenode(tn1);
				//synbolTable.get(synbolTable.size()-1).setIndex(arrayNum);
				Totaloff=synbolTable.get(synbolTable.size()-1).offset+synbolTable.get(synbolTable.size()-1).lengthoff-oldlength;
			}
			semanticStack.pop();
		}//赋值语义动作
		else if(top.name.equals("@EQ")){
			OP="=";
			ARG1=semanticStack.pop();
			RES=semanticStack.pop();
			FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1," ",RES);
			fourElemList.add(fourElem);
			OP=null;
			analyseStack.remove(0);
		}else if(top.name.equals("@EQ_K")){
			analyseStack.remove(0);
			//
		}else if(top.name.equals("@EQ_U'")){
			OP="=";
			ARG1=semanticStack.pop();
			RES=semanticStack.pop();
			//fourElemCount++;
			FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1," ",RES);
			fourElemList.add(fourElem);
			OP=null;
			analyseStack.remove(0);
		}
		//if语句语义动作
		else if(top.name.equals("@IF_HEAD")){
			analyseStack.remove(0);
			OP="if";
			ARG1=semanticStack.lastElement();
			FourElement fourElem1=new FourElement(++fourElemCount,OP,ARG1," "," ");
			fourElemList.add(fourElem1);
			OP=null;
		}else if(top.name.equals("@IF_FJ")){
			OP="FJ";//修改四元式
			ARG1=semanticStack.pop();
			FourElement fourElem=new FourElement(++fourElemCount,OP," "," ",RES);
			if_fj.push(fourElemCount);
			fourElemList.add(fourElem);
			OP=null;
			analyseStack.remove(0);
		}else if(top.name.equals("@IF_RJ")){
			OP="RJ";//真跳
			//RES=(while_fj.peek()-2)+"";
			FourElement fourElem=new FourElement(++fourElemCount,OP," "," "," ");
			if_rj.push(fourElemCount);
			fourElemList.add(fourElem);
			OP=null;
			analyseStack.remove(0);
		}else if(top.name.equals("@IF_BACKPATCH_FJ")){
			backpatch(if_fj.pop(), fourElemCount+2);
			analyseStack.remove(0);
		}else if(top.name.equals("@IF_EL")){
			analyseStack.remove(0);
			OP="el";
			FourElement fourElem1=new FourElement(++fourElemCount,OP," "," "," ");
			fourElemList.add(fourElem1);
		}else if(top.name.equals("@IFEL_FJ")){
			analyseStack.remove(0);
			
		}else if(top.name.equals("@IFEL_BACKPATCH_FJ")){
			analyseStack.remove(0);
			
		}else if(top.name.equals("@IF_END")){
			analyseStack.remove(0);
			OP="ie";
			FourElement fourElem1=new FourElement(++fourElemCount,OP," "," "," ");
			fourElemList.add(fourElem1);
		}else if(top.name.equals("@IF_BACKPATCH_RJ")){
			backpatch(if_rj.pop(), fourElemCount);
			analyseStack.remove(0);
		}
		//while语句语义动作
		else if(top.name.equals("@WHILE_HEAD")){
			analyseStack.remove(0);
			OP="wh";
			FourElement fourElem1=new FourElement(++fourElemCount,OP," "," "," ");
			fourElemList.add(fourElem1);
		}else if(top.name.equals("@DO")){
			analyseStack.remove(0);
			OP="do";
			ARG1=semanticStack.lastElement();
			FourElement fourElem1=new FourElement(++fourElemCount,OP,ARG1," "," ");
			fourElemList.add(fourElem1);
		}else if(top.name.equals("@WHILE_FJ")){
			OP="FJ";
			ARG1=semanticStack.pop();
			FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1," "," ");
			while_fj.push(fourElemCount);
			fourElemList.add(fourElem);
			OP=null;
			analyseStack.remove(0);
		}else if(top.name.equals("@WHILE_RJ")){
			OP="RJ";
			RES=(while_fj.peek()-2)+"";
			FourElement fourElem=new FourElement(++fourElemCount,OP," "," ",RES);
			for_rj.push(fourElemCount);
			fourElemList.add(fourElem);
			OP=null;
			analyseStack.remove(0);
		}else if(top.name.equals("@WHILE_BACKPATCH_FJ")){
			backpatch(while_fj.pop(), fourElemCount);
			analyseStack.remove(0);
		}else if(top.name.equals("@WHILE_END")){
			OP="wend";
			FourElement fourElem1=new FourElement(++fourElemCount,OP," "," "," ");
			fourElemList.add(fourElem1);
			analyseStack.remove(0);
		}//for语句语义动作
		else if(top.name.equals("@FOR_HEAD")){
			OP="for";
			FourElement fourElem1=new FourElement(++fourElemCount,OP," "," "," ");
			fourElemList.add(fourElem1);
			analyseStack.remove(0);
		}else if(top.name.equals("@FOR_LINE_RJ")){
			analyseStack.remove(0);
		}else if(top.name.equals("@FOR_FJ")){
			OP="FJ";//for假跳
			ARG1=semanticStack.pop();
			FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1," "," ");
			for_fj.push(fourElemCount);
			fourElemList.add(fourElem);
			OP=null;
			analyseStack.remove(0);
		}else if(top.name.equals("@FOR_RJ")){
			OP="RJ";//for继续
			RES=(for_fj.peek()-2)+"";//退到for逻辑表达式入口
			FourElement fourElem=new FourElement(++fourElemCount,OP," "," ",RES);
			//for_rj.push(fourElemCount);
			fourElemList.add(fourElem);
			OP=null;
			analyseStack.remove(0);
		}else if(top.name.equals("@SINGLE")){
			if(for_op.peek()!=null){
				ARG1=semanticStack.pop();
				RES=ARG1;
				//fourElemCount++;
				FourElement fourElem=new FourElement(++fourElemCount,for_op.pop(),ARG1," ",RES);
				fourElemList.add(fourElem);
			}
			analyseStack.remove(0);
		}else if(top.name.equals("@FOR_BACKPATCH_FJ")){
			backpatch(for_fj.pop(), fourElemCount);
			analyseStack.remove(0);
		}else if(top.name.equals("@FOR_END")){
			OP="fend";
			FourElement fourElem1=new FourElement(++fourElemCount,OP," "," "," ");
			for_rj.push(fourElemCount);
			fourElemList.add(fourElem1);
			analyseStack.remove(0);
		}
	}

	//回填
	private void backpatch(int i,int res){
		FourElement temp=fourElemList.get(i-1);
		temp.result=res+"";
		fourElemList.set(i-1, temp);
	}
	
	//输出LL1分析过程表
	public String outputLL1() throws IOException{
		//grammerAnalyse();
		File file=new File("./output/");
		if(!file.exists()){
			file.mkdirs();
			file.createNewFile();//如果这个文件不存在就创建它
		}
		String 				 path=file.getAbsolutePath();
		FileOutputStream 	 fos=new FileOutputStream(path+"/LL1.txt");  
		BufferedOutputStream bos=new BufferedOutputStream(fos); 
		OutputStreamWriter 	 osw1=new OutputStreamWriter(bos,"utf-8");
		PrintWriter 		 pw1=new PrintWriter(osw1);
		pw1.println(bf.toString());
		bf.delete(0, bf.length());
		if(graErrorFlag){
			if(errorList.size()>0){
				Error error;
				pw1.println("\n----------------------------------------错误错误信息如下--------------------------------------------");
				pw1.println("错误序号\t错误信息\t\t错误所在行 \t错误单词");
				for(int i=0;i<errorList.size();i++){
					error=errorList.get(i);
					pw1.println(error.id+"\t"+error.info+"\t"+error.line+"\t"+error.word.value);
				}
				pw1.println("\n----------------------------------------LL1语法分析失败--------------------------------------------");
			}else{
				pw1.println("\n----------------------------------------LL1语法分析失败--------------------------------------------");
			}
		}else {
			pw1.println("\n----------------------------------------LL1语法分析成功--------------------------------------------");
		}
		pw1.close();
		return path+"/LL1.txt";
	}
	
	//输出四元式
	public String outputFourElem1() throws IOException{
		
		File file=new File("./output/");
		if(!file.exists()){
			file.mkdirs();
			file.createNewFile();//如果这个文件不存在就创建它
		}
		String path=file.getAbsolutePath();
		FileOutputStream fos=new FileOutputStream(path+"/FourElement1.txt");  
		BufferedOutputStream bos=new BufferedOutputStream(fos); 
		OutputStreamWriter osw1=new OutputStreamWriter(bos,"utf-8");
		PrintWriter pw1=new PrintWriter(osw1);
		pw1.println("生成的四元式如下");
		pw1.println("序号（OP,ARG1，ARG2，RESULT）");
		FourElement temp;
		for(int i=0;i<fourElemList.size();i++){
			temp=fourElemList.get(i);
			pw1.println(temp.id+"("+temp.op+","+temp.arg1+","+temp.arg2+","+temp.result+")");
		}
		pw1.close();
		
		return path+"/FourElement1.txt";
	}
	
	//得到四元式
	public String outputFourElem() throws IOException{
		
		File file=new File("./output/");
		if(!file.exists()){
			file.mkdirs();
			file.createNewFile();//如果这个文件不存在就创建它
		}
		String path=file.getAbsolutePath();
		FileOutputStream fos=new FileOutputStream(path+"/FourElement.txt");  
		BufferedOutputStream bos=new BufferedOutputStream(fos); 
		OutputStreamWriter osw1=new OutputStreamWriter(bos,"utf-8");
		PrintWriter pw1=new PrintWriter(osw1);
		pw1.println("--------------------------生成的四元式如下--------------------------");
		pw1.println("\t序号:\t"+"OP\t"+"ARG1\t"+"ARG2\t"+"RESULT\n");
		FourElement temp;
		for(int i=0;i<fourElemList.size();i++){
			temp=fourElemList.get(i);
			pw1.println("\t  "+temp.id+"\t "+temp.op+"\t "+temp.arg1+"\t "+temp.arg2+"\t "+temp.result);
		pw1.println("--------------------------------------------------------------------");
		}
		pw1.close();
		
		return path+"/FourElement.txt";
	}

	
	//输出函数表//类型表//符号表总表//数组表//结构体表//长度表
	public String outputTable() throws IOException{
		
		File file=new File("./output/");
		if(!file.exists()){
			file.mkdirs();
			file.createNewFile();//如果这个文件不存在就创建它
		}
		String path=file.getAbsolutePath();
		FileOutputStream fos=new FileOutputStream(path+"/table.txt");  
		BufferedOutputStream bos=new BufferedOutputStream(fos); 
		OutputStreamWriter osw1=new OutputStreamWriter(bos,"utf-8");
		PrintWriter pw1=new PrintWriter(osw1);
		getlengthTable();//获取长度表
		pw1.println("---------------------------函数表-------------------------------------------");//PT
		pfinfNode tab0;
		if(pfinfTable.size()>0){
			pw1.println("函数名\t层次\t区距\t参数个数\t参数表\t入口地址\n");//int=0
			for (int i = 0; i < pfinfTable.size(); i++) {
				tab0 = pfinfTable.get(i);
				pw1.println("main"+"\t"+tab0.level+"\t"+tab0.off+"\t"+tab0.fn+"\t"+tab0.fn+"\t"+tab0.entry);
				pw1.println("----------------------------------------------------------------------------------------------");
			}
		}else{
			pw1.println("\t\t函数表为空\n");
		}
		pw1.println("----------------------------------------------------------------------------------------------");
		
		pw1.println("---------------------------类型表-------------------------------------------");//PT
		typeNode tab;
		if(typeTable.size()>0){
			pw1.println("序号\t\t类型名\t\t类型指针\n");//int=0
			for (int i = 0; i < typeTable.size(); i++) {
				tab = typeTable.get(i);
				pw1.println((i+1) + "\t\t"+tab.type+tab.index+"\t\t" + tab.tpoint);
				pw1.println("----------------------------------------------------------------------------------------------");
			}
		}else{
			pw1.println("\t\t类型表为空\n");
		}
		pw1.println("----------------------------------------------------------------------------------------------");
		
		pw1.println("---------------------------符号表总表-------------------------------------------");//PT
		SynbolNode tab2;
		if(synbolTable.size()>0){
			pw1.println("序号\t标识符名\t类型指针\t种类\t地址\t所占内存单元:字节\n");//int=0//+tab2.type
			for (int i = 0; i < synbolTable.size(); i++) {
				tab2 = synbolTable.get(i);
				pw1.println((i+1) + "\t"+tab2.value+"\t"+tab2.type+tab2.index+"\t"+tab2.cat+"\t"+tab2.offset+"\t"+tab2.lengthoff);
				pw1.println("----------------------------------------------------------------------------------------------");
			}
		}else{
			pw1.println("\t\t符号表总表为空\n");
		}
		pw1.println("----------------------------------------------------------------------------------------------");
		
		pw1.println("---------------------------数组表-------------------------------------------");//PT
		ainfNode tab3;
		if(ainfTable.size()>0){
			pw1.println("序号\t数组名\t下界\t上界\t成分类型\t成分长度\n");//int=0//+tab2.type
			for (int i = 0; i < ainfTable.size(); i++) {
				tab3 = ainfTable.get(i);
				pw1.println((i+1) + "\t"+tab3.type.value+"\t"+tab3.low
						+"\t"+ tab3.up+"\t"+tab3.typeValue
						+tab3.type.index+"\t"+tab3.offsize);
				pw1.println("----------------------------------------------------------------------------------------------");
			}
		}else{
			pw1.println("\t\t数组表为空\n");
		}
		pw1.println("----------------------------------------------------------------------------------------------");
		
		pw1.println("---------------------------结构体表-------------------------------------------");//PT
		rinfNode tab4;
		if(rinfTable.size()>0){
			pw1.println("序号\t结构体名\t\n");//int=0//+tab2.type
			for (int i = 0; i < rinfTable.size(); i++) {
				tab4 = rinfTable.get(i);
				pw1.println((i+1)+"\t" + tab4.value+"\t域名信息\t区距\t成分类型");
				for(int j=0;j<tab4.type_n.size();j++){
					pw1.println("\t\t" + tab4.off_value.get(j)
							+"\t"+tab4.off_n.get(j)+"\t"+tab4.type_n_tpoint.get(j));
				}
				pw1.println("----------------------------------------------------------------------------------------------");
			}
		}else{
			pw1.println("\t\t结构体表为空\n");
		}
		pw1.println("----------------------------------------------------------------------------------------------");
		
		
		pw1.println("---------------------------长度表-------------------------------------------");//PT
		//lengthNode tab66;
		if(lengthTable.size()>0){
			pw1.println("序号\t长度:字节\t\n");//int=0//+tab2.type
			for (int i = 0; i < lengthTable.size(); i++) {
				pw1.println((i+1)+"\t"+lengthTable.get(i).offset);
				pw1.println("----------------------------------------------------------------------------------------------");
			}
		}else{
			pw1.println("\t\t长度表为空\n");
		}
		pw1.println("----------------------------------------------------------------------------------------------");
		
		pw1.close();
		return path+"/table.txt";
	}
	
	public static void main(String[] args) {
			// TODO Auto-generated method stub
	
		}
	//获取长度表
	public void getlengthTable(){
		SynbolNode temp;
		int aindex=0;
		int rindex=0;
		if(synbolTable.size()>0){
			for(int i=0;i<synbolTable.size();i++){
				temp=synbolTable.get(i);
				if(temp.tpoint==0){
					lengthNode ln=new lengthNode(temp.lengthoff);
					lengthTable.add(ln);
				}else if(temp.tpoint==1){//标识符是数组,同时要修改对应符号表中的lengthoff
					lengthNode ln=new lengthNode(ainfTable.get(aindex).offsize);
					aindex++;
					synbolTable.get(i).setLengthoff(ln.offset);
					lengthTable.add(ln);
				}else if(temp.tpoint==2){//标识符是结构体,同时要修改对应符号表中的lengthoff
					lengthNode ln=new lengthNode(rinfTable.get(rindex).offrinf);
					rindex++;
					synbolTable.get(i).setLengthoff(ln.offset);
					lengthTable.add(ln);
				}
			}
			
		}
	}
}


//表项定义
class typeNode{//类型表结点
	String type;		//类型==>语义动作时根据语义栈中的声明类型关键字传入整型:int,字符:char,布尔:bool,浮点:float
	int tpoint;			//类型指针==>根据type信息得到整型:0,字符:0,布尔:0,浮点:0
	int lengthoff;		//所占内存字节数==>根据type信息得到整型:4,字符:1,布尔:1,浮点:8
	int index=0;
	typeNode(){}
	typeNode(String type)
	{
		this.type=type;
		
		if(type=="整型"||type=="空型"){
			this.tpoint=0;
			this.lengthoff=4;
		}else if(type=="浮点"){
			this.tpoint=0;
			this.lengthoff=8;
		}else if(type=="字符"){
			this.tpoint=0;
			this.lengthoff=1;
		}else if(type=="布尔"){
			this.tpoint=0;
			this.lengthoff=1;
		}else if(type=="数组"){
			this.tpoint=1;
			this.lengthoff=0;//在自己的表里设置
		}else if(type=="结构体"){
			this.tpoint=2;
			this.lengthoff=0;//在自己的表里设置
		}
	}
	
	public void setlengthoff(int off){
		this.lengthoff=off;
	}
	
	public void setIndex(int index){
		this.index=index;
	}
	
	public boolean equals(Object obj)
	{
		if(!(obj instanceof typeNode))
		{
			return false;
		}
		typeNode ty=(typeNode)obj;
		if(this.type=="数组"||this.type=="结构体")
		{
			return false;
		}else{
			return this.type.equals(ty.type)&&(this.tpoint==ty.tpoint);
		}
	}
}

class ainfNode{//数组表结点
	int low=0;
	int up;//数组长度下标-1
	SynbolNode type;//数组结点(存着数组结点的名字value)
	String value;
	String typeValue;//值单元类型
	int tpoint;//值单元对应的指针下标
	int offsize;//值单元总长度:成分类型
	
	int clen;//值单元个数
	
	int index =0;
	ainfNode(){
		
	}

	ainfNode(int up,SynbolNode ty){//用上界初始化，以及结点的类型
		type=ty;
		this.value=ty.value;//获得数组结点名字
		this.up=up;
		this.clen=this.up-low;//值单元个数
		this.typeValue=type.typenode.type;
		this.tpoint=type.typenode.tpoint;
		this.offsize=this.clen*(ty.typenode.lengthoff);
	}
	
	public void setPreDiemension(String tv,int tp,int off){//修改上一维的成分类型为数组,
		this.typeValue=tv;
		this.tpoint=tp;
		type.lengthoff=off;//单元长度
		this.offsize=this.clen*off;//总成分长度
	}
	
	public boolean equals(Object obj)//根据名字找到同一个数组
	{
		if(!(obj instanceof ainfNode))
		{
			return false;
		}
		ainfNode af=(ainfNode)obj;
		return this.value.equals(af.value);
	}
	public void setIndex(int index){
		this.index=index;
	}
}

class rinfNode{//结构体结点
	String value;//结构体名,填符号表时赋给value
	int index=0;
	
	Vector<String> off_value;//结构体内变量的名字
	Vector<Integer> off_n;//结构体内变量的偏移值(相对于结构体而言)
	Vector<SynbolNode> type_n;//结构体内变量结点
	Vector<Integer> type_n_tpoint;//结点类型===>根据type_n.typenode.tpoint得到
	
	int offrinf=0;//结构体所占字节数,填符号表时有用直接赋给lengthoff
	
	
	rinfNode(String v){
		this.value			=v;
		this.off_value		=new Vector<String>();//结构体内变量的名字
		this.off_n			=new Vector<Integer>();//结构体内变量的偏移值(相对于结构体而言)
		this.type_n			=new Vector<SynbolNode>();//结构体内变量结点
		this.type_n_tpoint	=new Vector<Integer>();//结点类型===>根据type_n.typenode.tpoint得到
	}
	
	
	public void putElement(int off,SynbolNode type){//语义动作时往结构体结点里面添加符号结点
		off_n.add(offrinf+off);
		offrinf+=off;
		type_n.add(type);
		type_n_tpoint.add(type.typenode.tpoint);
		off_value.add(type.value);//将结构体里声明的变量放进结构体表中
	}
	
	public void setIndex(int index){
		this.index=index;
	}
	
}
//长度表结点
class lengthNode{
	int offset;
	lengthNode(){
	}
	lengthNode(int offset){
		this.offset=offset;
	}
}


//函数表
class pfinfNode{
	public int level=0;
	public int off=0;
	public int fn;
	public int entry=0;
	public ArrayList<SynbolNode> param=new ArrayList<SynbolNode>();
	public void setfn(){
		fn=param.size();
	}
	pfinfNode(){
		setfn();
	}
}