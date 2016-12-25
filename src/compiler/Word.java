package compiler;

import java.util.ArrayList;

//import compiler.LexAnalyse;

/**
 * ������
 * 
 * @author Administrator 1��������� 2�����ʵ�ֵ 3���������� 4������������ 5�������Ƿ�Ϸ�
 */

public class Word {
	
	public final static String KEY	 		= "�ؼ���";
	public final static String OPERATOR 	= "�����";
	
	public final static String INT_CONST 	= "���γ���";
	public final static String CHAR_CONST 	= "�ַ�����";
	public final static String BOOL_CONST 	= "��������";
	public final static String FLOAT_CONST 	= "���㳣��";//����
	
	public final static String IDENTIFIER 	= "��־��";
	public final static String BOUNDARYSIGN = "���";
	public final static String END 			= "������";
	public final static String UNIDEF 		= "δ֪����";
	
	public static ArrayList<String> key 			= new ArrayList<String>();//�ؼ��ּ���
	public static ArrayList<String> boundarySign 	= new ArrayList<String>();//�������
	public static ArrayList<String> operator 		= new ArrayList<String>();// ���������
	
	static {
		//-----------------------------------------
		Word.key.add("main");//size();
		Word.key.add("printf");
		Word.key.add("scanf");
		Word.key.add("struct");
		Word.key.add("return");
		Word.key.add("if");
		Word.key.add("else");
		Word.key.add("do");
		Word.key.add("while");
		Word.key.add("for");
		Word.key.add("void");
		Word.key.add("int");
		Word.key.add("char");
		Word.key.add("bool");
		Word.key.add("float");
		Word.key.add("true");
		Word.key.add("false");
		//-----------------------------------------
		Word.boundarySign.add(";");//size()+15
		Word.boundarySign.add(",");
		Word.boundarySign.add("[");
		Word.boundarySign.add("]");
		Word.boundarySign.add("(");
		Word.boundarySign.add(")");
		Word.boundarySign.add("{");
		Word.boundarySign.add("}");
		Word.boundarySign.add("'");
		Word.boundarySign.add("\"");
		//-----------------------------------------
		Word.operator.add("=");//size()+25
		Word.operator.add("==");
		Word.operator.add("!=");
		Word.operator.add(">");
		Word.operator.add("<");
		Word.operator.add(">=");
		Word.operator.add("<=");
		Word.operator.add("+");
		Word.operator.add("-");
		Word.operator.add("*");
		Word.operator.add("/");
		Word.operator.add("&&");
		Word.operator.add("||");
		Word.operator.add("!");
		Word.operator.add("++");
		Word.operator.add("--");
		Word.operator.add("?");
		Word.operator.add("|");
		Word.operator.add("&");
		//Word.operator.add("#");
		//Word.operator.add(".");
	}
	
	int id;				// �������
	String value;		// ���ʵ�ֵ
	String type;		// ��������
	int line;			// ����������
	int tokennum;		// ����tokenֵ
	boolean flag = true;//�����Ƿ�Ϸ�

	public Word() {

	}

	public Word(int id, String value, String type, int line) {
		this.id = id;
		this.value = value;
		this.type = type;
		this.line = line;
	}

	public static boolean isKey(String word) {
		return key.contains(word);
	}

	public static boolean isOperator(String word) {
		return operator.contains(word);
	}

	public static boolean isBoundarySign(String word) {
		return boundarySign.contains(word);
	}

	// �жϵ����Ƿ�Ϊ���������
	public static boolean isArOP(String word) {
		if ((word.equals("+") || word.equals("-") || word.equals("*") || word
				.equals("/")))
			return true;
		else
			return false;
	}

	// �жϵ����Ƿ�Ϊ���������
	public static boolean isBoolOP(String word) {
		if ((word.equals(">") || word.equals("<") || word.equals("==")
				|| word.equals("!=") || word.equals("!") || word.equals("&&") || word.equals("||")))
			return true;
		else
			return false;
	}
}
