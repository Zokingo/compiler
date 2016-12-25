package compiler;

import java.util.ArrayList;

/**
 * 分析栈节点类
 *	String type;//节点类型
	String name;//节点名
	Object value;//节点值
 */
public class AnalyseNode {
	
	public final static String NONTERMINAL	="非终结符";
	public final static String TERMINAL		="终结符";
	public final static String ACTIONSIGN	="动作符";
	public final static String END			="结束符";
	
	static ArrayList<String>nonterminal	=new ArrayList<String>();//非终结符集合
	static ArrayList<String>actionSign	=new ArrayList<String>();//动作符集合
	static{
		nonterminal.add("S");
		nonterminal.add("A");
		nonterminal.add("B");
		nonterminal.add("C");
		nonterminal.add("X");
		nonterminal.add("Y");
		nonterminal.add("Z");
		nonterminal.add("Z'");
		nonterminal.add("U");
		nonterminal.add("U'");
		nonterminal.add("R");
		nonterminal.add("E");
		nonterminal.add("E'");
		nonterminal.add("H");
		nonterminal.add("G");
		nonterminal.add("H'");
		nonterminal.add("D");
		nonterminal.add("L");
		nonterminal.add("L'");
		nonterminal.add("T");
		nonterminal.add("T'");
		nonterminal.add("F");
		nonterminal.add("O");
		nonterminal.add("P");
		nonterminal.add("Q");
		nonterminal.add("Q'");//新增
		nonterminal.add("I");//新增
		nonterminal.add("I'");//
		nonterminal.add("K");//
		nonterminal.add("R'");//
		nonterminal.add("B'");//
//----------------------------------------------
		//符号表项生成相关语义动作(作用在标识符声明时)
		actionSign.add("@TAB_S");
		actionSign.add("@TAB_U");
		actionSign.add("@TAB_U'");
		actionSign.add("@INIT_XOFFSET");
		actionSign.add("@ASS_Y");
		actionSign.add("@ASS_I");
		actionSign.add("@TAB_I");
		actionSign.add("@ASS_I'");
		//算术运算语义动作
		actionSign.add("@ADD_SUB");
		actionSign.add("@DIV_MUL");
		actionSign.add("@ADD");
		actionSign.add("@SUB");
		actionSign.add("@DIV");
		actionSign.add("@MUL");
		actionSign.add("@SINGLE_OP");
		//逻辑操作语义动作
		actionSign.add("@COMPARE");
		actionSign.add("@COMPARE_OP");
		//初始化(赋值)语义动作
		actionSign.add("@ASS_F");
		actionSign.add("@ASS_R");
		actionSign.add("@ASS_Q");
		actionSign.add("@ASS_U");
		actionSign.add("@TRAN_LF");
		actionSign.add("@ASS_U'");
		//赋值语义动作
		actionSign.add("@EQ");
		actionSign.add("@EQ_K");
		//actionSign.add("@EQ_U'");
		//if语句语义动作
		actionSign.add("@IF_HEAD");
		actionSign.add("@IF_FJ");
		//actionSign.add("@IF_RJ");
		actionSign.add("@IF_BACKPATCH_FJ");
		actionSign.add("@IF_EL");
		actionSign.add("@IFEL_FJ");
		actionSign.add("@IFEL_BACKPATCH_FJ");
		actionSign.add("@IF_END");
		//actionSign.add("@IF_BACKPATCH_RJ");
		//while语句语义动作
		actionSign.add("@WHILE_HEAD");
		actionSign.add("@DO");
		actionSign.add("@WHILE_FJ");
		actionSign.add("@WHILE_RJ");
		actionSign.add("@WHILE_BACKPATCH_FJ");
		actionSign.add("@WHILE_END");
		//for语句语义动作
		actionSign.add("@FOR_HEAD");
		actionSign.add("@FOR_LINE_RJ");
		actionSign.add("@FOR_FJ");
		actionSign.add("@FOR_RJ");
		actionSign.add("@SINGLE");
		actionSign.add("@FOR_BACKPATCH_FJ");
		actionSign.add("@FOR_END");
	}
	
	String type;//节点类型(终结符、非终结符、动作符、结束符)
	String name;//节点名(S、A、B,@开头的动作符号等)//
	String value;//节点值(对应wordList中的单词)
	
	//非终结符
	public static boolean isNonterm(AnalyseNode node){
		return nonterminal.contains(node.name);
	}
	
	//终结符
	public static boolean isTerm(AnalyseNode node){
		return Word.isKey(node.name)||Word.isOperator(node.name)||Word.isBoundarySign(node.name)
		||node.name.equals("id")||node.name.equals("num")||node.name.equals("ch")||node.name.equals("floatnum")||node.name.equals("boolid");
	}
	
	//语义动作
	public static boolean isActionSign(AnalyseNode node){
		return actionSign.contains(node.name);
	}
	
	
	public AnalyseNode(){
		
	}
	
	
	public AnalyseNode(String type,String name,String value){
		this.type=type;
		this.name=name;
		this.value=value;
	}

}
