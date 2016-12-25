package compiler;

import java.util.ArrayList;

/**
 * ����ջ�ڵ���
 *	String type;//�ڵ�����
	String name;//�ڵ���
	Object value;//�ڵ�ֵ
 */
public class AnalyseNode {
	
	public final static String NONTERMINAL	="���ս��";
	public final static String TERMINAL		="�ս��";
	public final static String ACTIONSIGN	="������";
	public final static String END			="������";
	
	static ArrayList<String>nonterminal	=new ArrayList<String>();//���ս������
	static ArrayList<String>actionSign	=new ArrayList<String>();//����������
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
		nonterminal.add("Q'");//����
		nonterminal.add("I");//����
		nonterminal.add("I'");//
		nonterminal.add("K");//
		nonterminal.add("R'");//
		nonterminal.add("B'");//
//----------------------------------------------
		//���ű�������������嶯��(�����ڱ�ʶ������ʱ)
		actionSign.add("@TAB_S");
		actionSign.add("@TAB_U");
		actionSign.add("@TAB_U'");
		actionSign.add("@INIT_XOFFSET");
		actionSign.add("@ASS_Y");
		actionSign.add("@ASS_I");
		actionSign.add("@TAB_I");
		actionSign.add("@ASS_I'");
		//�����������嶯��
		actionSign.add("@ADD_SUB");
		actionSign.add("@DIV_MUL");
		actionSign.add("@ADD");
		actionSign.add("@SUB");
		actionSign.add("@DIV");
		actionSign.add("@MUL");
		actionSign.add("@SINGLE_OP");
		//�߼��������嶯��
		actionSign.add("@COMPARE");
		actionSign.add("@COMPARE_OP");
		//��ʼ��(��ֵ)���嶯��
		actionSign.add("@ASS_F");
		actionSign.add("@ASS_R");
		actionSign.add("@ASS_Q");
		actionSign.add("@ASS_U");
		actionSign.add("@TRAN_LF");
		actionSign.add("@ASS_U'");
		//��ֵ���嶯��
		actionSign.add("@EQ");
		actionSign.add("@EQ_K");
		//actionSign.add("@EQ_U'");
		//if������嶯��
		actionSign.add("@IF_HEAD");
		actionSign.add("@IF_FJ");
		//actionSign.add("@IF_RJ");
		actionSign.add("@IF_BACKPATCH_FJ");
		actionSign.add("@IF_EL");
		actionSign.add("@IFEL_FJ");
		actionSign.add("@IFEL_BACKPATCH_FJ");
		actionSign.add("@IF_END");
		//actionSign.add("@IF_BACKPATCH_RJ");
		//while������嶯��
		actionSign.add("@WHILE_HEAD");
		actionSign.add("@DO");
		actionSign.add("@WHILE_FJ");
		actionSign.add("@WHILE_RJ");
		actionSign.add("@WHILE_BACKPATCH_FJ");
		actionSign.add("@WHILE_END");
		//for������嶯��
		actionSign.add("@FOR_HEAD");
		actionSign.add("@FOR_LINE_RJ");
		actionSign.add("@FOR_FJ");
		actionSign.add("@FOR_RJ");
		actionSign.add("@SINGLE");
		actionSign.add("@FOR_BACKPATCH_FJ");
		actionSign.add("@FOR_END");
	}
	
	String type;//�ڵ�����(�ս�������ս������������������)
	String name;//�ڵ���(S��A��B,@��ͷ�Ķ������ŵ�)//
	String value;//�ڵ�ֵ(��ӦwordList�еĵ���)
	
	//���ս��
	public static boolean isNonterm(AnalyseNode node){
		return nonterminal.contains(node.name);
	}
	
	//�ս��
	public static boolean isTerm(AnalyseNode node){
		return Word.isKey(node.name)||Word.isOperator(node.name)||Word.isBoundarySign(node.name)
		||node.name.equals("id")||node.name.equals("num")||node.name.equals("ch")||node.name.equals("floatnum")||node.name.equals("boolid");
	}
	
	//���嶯��
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
