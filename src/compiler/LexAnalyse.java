package compiler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * �ʷ�������
 * 
 * 
 */
public class LexAnalyse {

	ArrayList<Word> 	wordList 		= new ArrayList<Word>();//���ʱ�
	public ArrayList<ConstWord>constList= new ArrayList<ConstWord>();//������
	ArrayList<Error> 	errorList 		= new ArrayList<Error>();// ������Ϣ�б�
	
	ArrayList<Token>	tokenList_all	= new ArrayList<Token>();//��token�����ظ�
	ArrayList<Token>	tokenList_KT	= new ArrayList<Token>();//�ؼ��ֱ�
	ArrayList<Token>	tokenList_PT	= new ArrayList<Token>();//�����
	ArrayList<Token>	tokenList_IT	= new ArrayList<Token>();//��ʶ����
	ArrayList<Token>	tokenList_CT	= new ArrayList<Token>();//������
	
	int 				wordCount 		= 0;// ͳ�Ƶ��ʸ���
	int 				errorCount 		= 0;// ͳ�ƴ������
	boolean 			noteFlag 		= false;// ����ע�ͱ�־
	boolean 			lexErrorFlag 	= false;// �ʷ����������־
	
	public LexAnalyse() {

	}

	public LexAnalyse(String str) {
		lexAnalyse(str);
	}
	/**
	 * �����ַ��ж�
	 * 
	 * @param ch
	 * @return
	 */
	private static boolean isDigit(char ch) {
		boolean flag = false;
		if ('0' <= ch && ch <= '9')
			flag = true;
		return flag;
	}

	/**
	 * �жϵ����Ƿ�Ϊint����
	 * 
	 * @param string
	 * @return
	 */
	private static boolean isInteger(String word) 
	{
		int i;
		boolean flag = false;
		for (i = 1; i < word.length(); i++) //��Ϊ�Ѿ�ȷ���׷��ŷ����������Ͷ��������±��1��ʼ
		{
			if (Character.isDigit(word.charAt(i))) 
			{
				continue;
			} else 
			{
				break;
			}
		}
		if (i == word.length()) 
		{
			flag = true;
		}
		return flag;
	}
	
	/**
	 * �жϵ����Ƿ�Ϊfloat����
	 * 
	 * @param string
	 * @return
	 */
	private static boolean isFloat(String word) 
	{
		int i;
		boolean flag = false;
		//double sum=0.0;
		//double tempp=0.0;
		
		for (i = 1; i < word.length(); i++) 
		{
			if (Character.isDigit(word.charAt(i))) 
			{
				//sum = sum * 10 + word.charAt(i) - '0'; //�������ִ���ʾ����ֵ�������sum
				continue;
			} 
			else 
			{
				break;
			}
		}
		
		if (word.charAt(i) == 'e' || word.charAt(i) == 'E')    //ָ�����ж�
		{
			i++;
			for (; i < word.length(); i++) 
			{
				if (Character.isDigit(word.charAt(i))) 
				{
					//tempp = tempp * 10 + word.charAt(i) - '0';
					continue;
				} 
				else 
				{
					break;
				}
			}
			//sum = sum*Math.pow(10, tempp);  //pow(x,y)����x��y����.
		}
		if (word.charAt(i) == '.')    //С�����ж�
		{
			i++;
			//int coun=0;
			for (; i < word.length(); i++) 
			{
				if (Character.isDigit(word.charAt(i))) 
				{
					//tempp = tempp + (word.charAt(i)-'0')*Math.pow(0.1,++coun );
					continue;
				} 
				else 
				{
					break;
				}
			}
			//sum = sum + tempp;     //�������ִ���ʾ����ֵ�������sum
		}
		
		if (i == word.length()) 
		{
			//��sum��ֵ��word.value�滻ԭ����ֵ
			//word.value=sum;
			flag = true;
		}
		return flag;
	}

	/**
	 * �ж��ַ��Ƿ�Ϊ��ĸ
	 * 
	 * @param ch
	 * @return
	 */
	private static boolean isLetter(char ch) {
		boolean flag = false;
		if (('a' <= ch && ch <= 'z') || ('A' <= ch && ch <= 'Z'))
			flag = true;
		return flag;
	}

	/**
	 * �жϵ����Ƿ�Ϊ�Ϸ���ʶ��
	 * 
	 * @param word
	 * @return
	 */
	private static boolean isID(String word) {
		boolean flag = false;
		int i = 0;
		if (Word.isKey(word))
			return flag;
		char temp = word.charAt(i);
		if (isLetter(temp) || temp == '_') {
			for (i = 1; i < word.length(); i++) {
				temp = word.charAt(i);
				if (isLetter(temp) || temp == '_' || isDigit(temp))
					continue;
				else
					break;
			}
			if (i >= word.length())
				flag = true;
		} else
			return flag;

		return flag;
	}

	/**
	 * �жϴʷ������Ƿ�ͨ��
	 * 
	 */
	public boolean isFail() {
		return lexErrorFlag;
	}

	//�ʷ�����
	public void analyse(String str, int line) 
	{
		int beginIndex;
		int endIndex;
		int index = 0;
		int length = str.length();
		Word word = null;
		ConstWord constWord = null;
		Error error;
		// boolean flag=false;
		char temp;
		while (index < length) 
		{
			temp = str.charAt(index);
			if (!noteFlag) 
			{
				if (isLetter(temp) || temp == '_') 
				{// �ж��ǲ��Ǳ�־��
					beginIndex = index;
					index++;
					//temp=str.charAt(index);
					while ((index < length)//�и��
							&& (!Word.isBoundarySign(str.substring(index,index + 1)))
							//&& (!Word.isOperator(str.substring(index, index + 1)))	//ע����ο��Խ��и��߼����и�ʽ��дʷ���������	
							&& (!Word.isOperator(str.substring(index, index + 1)))//�﷨����ʱ��Ϊ�漰�򵥵ĵ����и���˲���ע�����
							//&&(temp=='_'||isLetter(temp))
							&& (str.charAt(index) != ' ')
							&& (str.charAt(index) != '\t')
							&& (str.charAt(index) != '\r')
							&& (str.charAt(index) != '\n')) 
					{
						index++;
						/*if(index<length){
							temp=str.charAt(index);
						}*/
					}
					endIndex = index;
					word = new Word();
					
					wordCount++;
					word.id = wordCount;
					word.line = line;
					word.value = str.substring(beginIndex, endIndex);
					
					
					if (Word.isKey(word.value)) 
					{
						word.type = Word.KEY;
						getToken(word.value,word.type);
						if(word.value=="true"||word.value=="false")
						{
							constWord=new ConstWord(word.value,word.type);
							if(!constList.contains(constWord))
							{
								constList.add(constWord);
							}
							
						}
						
					} 
					else if (isID(word.value)) 
					{
						word.type = Word.IDENTIFIER;
						getToken(word.value,word.type);
					} 
					else 
					{
						word.type = Word.UNIDEF;
						getToken(word.value,word.type);
						word.flag = false;
						errorCount++;
						error = new Error(errorCount, "�Ƿ���ʶ��", word.line, word);
						errorList.add(error);
						lexErrorFlag = true;
					}
					index--;

				} 
				else if (isDigit(temp)) //����������������ʡ��||temp=='-'||temp=='+'
				{// �ж��ǲ���int����,float������ָ����С����

					beginIndex = index;
					index++;
					// temp=str.charAt(index);
					while ((index < length)
							&& (!Word.isBoundarySign(str.substring(index,
									index + 1)))
							&& (!Word.isOperator(str
									.substring(index, index + 1)))
							&& (str.charAt(index) != ' ')
							&& (str.charAt(index) != '\t')
							&& (str.charAt(index) != '\r')
							&& (str.charAt(index) != '\n')) 
					{
						index++;
					}
					endIndex = index;
					
					word = new Word();
					wordCount++;
					word.id = wordCount;
					word.line = line;
					word.value = str.substring(beginIndex, endIndex);
					
					//�����и�õ��ĵ���
					if (isInteger(word.value)) 
					{
						word.type = Word.INT_CONST;
						getToken(word.value,word.type);
						constWord=new ConstWord(word.value,word.type);
						if(!constList.contains(constWord))
						{
							constList.add(constWord);
						}
						
					} 
					else if(isFloat(word.value)) 
					{
						word.type=Word.FLOAT_CONST;
						getToken(word.value,word.type);
						constWord=new ConstWord(word.value,word.type);
						if(!constList.contains(constWord))
						{
							constList.add(constWord);
						}
					}
					else
					{
						word.type = Word.UNIDEF;
						getToken(word.value,word.type);
						word.flag = false;
						errorCount++;
						error = new Error(errorCount, "�Ƿ���ʶ��", word.line, word);
						errorList.add(error);
						lexErrorFlag = true;
					}
					index--;
				} 
				else if (String.valueOf(str.charAt(index)).equals("'")) 
				{// �ַ�����
					// flag=true;
					beginIndex = index;
					index++;
					temp = str.charAt(index);
					
					
					while (index < length && (0 <= temp && temp <= 255)) 
					{
						if (temp=='\\')//ת�����
						{
							index++;
							temp=str.charAt(index);
							
							if(temp=='a'||temp=='b'||temp=='f'||temp=='n'||temp=='v'||temp=='r'
									||temp=='t'||temp=='\\'||temp=='\''||temp=='\"'||temp=='?'
									||temp=='0')
							{
								index++;
								temp=str.charAt(index);
								//break;
							}
						}
						if(isLetter(temp))
						{
							index++;
						}
						if (String.valueOf(str.charAt(index)).equals("'"))
						{
							index++;
							break;
						}
					}
					
					if (index < length) 
					{
						endIndex = index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);//ȥ�������ţ�������Ҫ�ѵ����ŵ����������
						word.type = Word.CHAR_CONST;
						getToken(word.value,word.type);
						constWord=new ConstWord(str.substring(beginIndex+1, endIndex-1),word.type);
						if(!constList.contains(constWord))
						{
							constList.add(constWord);
						}
						index--;
					} 
					else 
					{
						endIndex = index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.UNIDEF;
						getToken(word.value,word.type);
						word.flag = false;
						errorCount++;
						error = new Error(errorCount, "�Ƿ���ʶ��", word.line, word);
						errorList.add(error);
						lexErrorFlag = true;
						index--;
					}
				} 
				else if (temp == '=') 
				{
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '=') 
					{
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;
						getToken(word.value,word.type);
					} 
					else
					{
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						getToken(word.value,word.type);
						index--;
					}
				}
				else if (temp == '!') 
				{
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '=') 
					{
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;
						getToken(word.value,word.type);
						index++;
					} 
					else 
					{
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						getToken(word.value,word.type);
						index--;
					}
				} 
				else if (temp == '&') 
				{
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '&') 
					{
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;
						getToken(word.value,word.type);
					} 
					else 
					{
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						getToken(word.value,word.type);
						index--;
					}
				} 
				else if (temp == '|') 
				{
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '|') 
					{
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;
						getToken(word.value,word.type);
					} 
					else 
					{
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						getToken(word.value,word.type);
						index--;
					}
				} 
				else if (temp == '+') //Ҳ�п�������ֵ+1.223����+5�ȱ�ʾ
				{
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '+') 
					{
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;
						getToken(word.value,word.type);

					}else if (isDigit(temp)) //����������������ʡ��||temp=='-'||temp=='+'
					{// �ж��ǲ���int����,float������ָ����С����

						beginIndex = index;
						index++;
						// temp=str.charAt(index);
						while ((index < length)
								&& (!Word.isBoundarySign(str.substring(index,
										index + 1)))
								&& (!Word.isOperator(str
										.substring(index, index + 1)))
								&& (str.charAt(index) != ' ')
								&& (str.charAt(index) != '\t')
								&& (str.charAt(index) != '\r')
								&& (str.charAt(index) != '\n')) 
						{
							index++;
						}
						endIndex = index;
						
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						
						//�����и�õ��ĵ���
						if (isInteger(word.value)) 
						{
							word.type = Word.INT_CONST;
							getToken(word.value,word.type);
							constWord=new ConstWord(word.value,word.type);
							if(!constList.contains(constWord))
							{
								constList.add(constWord);
								
							}
							
						} 
						else if(isFloat(word.value)) 
						{
							word.type=Word.FLOAT_CONST;
							getToken(word.value,word.type);
							constWord=new ConstWord(word.value,word.type);
							if(!constList.contains(constWord))
							{
								constList.add(constWord);
							}
						}
						else
						{
							word.type = Word.UNIDEF;
							getToken(word.value,word.type);
							word.flag = false;
							errorCount++;
							error = new Error(errorCount, "�Ƿ���ʶ��", word.line, word);
							errorList.add(error);
							lexErrorFlag = true;
						}
						index--;
					}  
					else
					{
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						getToken(word.value,word.type);
						index--;
					}
				} 
				else if (temp == '-')
				{
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '-') 
					{
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;
						getToken(word.value,word.type);
					}else if (isDigit(temp)) //����������������ʡ��||temp=='-'||temp=='+'
					{// �ж��ǲ���int����,float������ָ����С����

						beginIndex = index;
						index++;
						// temp=str.charAt(index);
						while ((index < length)
								&& (!Word.isBoundarySign(str.substring(index,
										index + 1)))
								&& (!Word.isOperator(str
										.substring(index, index + 1)))
								&& (str.charAt(index) != ' ')
								&& (str.charAt(index) != '\t')
								&& (str.charAt(index) != '\r')
								&& (str.charAt(index) != '\n')) 
						{
							index++;
						}
						endIndex = index;
						
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						
						//�����и�õ��ĵ���
						if (isInteger(word.value)) 
						{
							word.type = Word.INT_CONST;
							getToken(word.value,word.type);
							constWord=new ConstWord(word.value,word.type);
							if(!constList.contains(constWord))
							{
								constList.add(constWord);
							}
							
						} 
						else if(isFloat(word.value)) 
						{
							word.type=Word.FLOAT_CONST;
							getToken(word.value,word.type);
							constWord=new ConstWord(word.value,word.type);
							if(!constList.contains(constWord))
							{
								constList.add(constWord);
							}
						}
						else
						{
							word.type = Word.UNIDEF;
							getToken(word.value,word.type);
							word.flag = false;
							errorCount++;
							error = new Error(errorCount, "�Ƿ���ʶ��", word.line, word);
							errorList.add(error);
							lexErrorFlag = true;
						}
						index--;
					} 
					else 
					{
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						getToken(word.value,word.type);
						index--;
					}
				}
				else if (temp == '/')//�������ŵĴ���
				{
					index++;
					if (index < length && str.charAt(index) == '/')
						break;
					/*
					 * { index++; while(str.charAt(index)!='\n'){ index++; } }
					 */
					else if (index < length && str.charAt(index) == '*')
					{
						noteFlag = true;
					}
					else 
					{
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						getToken(word.value,word.type);
					}
					index--;
				} 
				else 
				{// ���Ǳ�ʶ�������ֳ������ַ�������

					switch (temp) 
					{
					case ' ':
					case '\t':
					case '\r':
					case '\n':
						word = null;
						break;// ���˿հ��ַ�
					case '[':
					case ']':
					case '(':
					case ')':
					case '{':
					case '}':
					case ',':
					case '"':
					case '.':
					case ';':
					// case '+':ǰ���Ѿ�������ķ���
					// case '-':ǰ���Ѿ�������ķ���
					case '*':
					// case '/':ǰ���Ѿ�������ķ���
					case '%':
					case '>':
					case '<':
					case '?':
					case '#':
						word = new Word();
						word.id = ++wordCount;
						word.line = line;
						word.value = String.valueOf(temp);
						if (Word.isOperator(word.value)){
							word.type = Word.OPERATOR;
							getToken(word.value,word.type);
						}
						else if(Word.isBoundarySign(word.value)){
							word.type = Word.BOUNDARYSIGN;
							getToken(word.value,word.type);
						}
						else{
							word.type = Word.END;
							getToken(word.value,word.type);
						}
						break;
					default:
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = String.valueOf(temp);
						word.type = Word.UNIDEF;
						getToken(word.value,word.type);
						word.flag = false;
						errorCount++;
						error = new Error(errorCount, "�Ƿ���ʶ��", word.line, word);
						errorList.add(error);
						lexErrorFlag = true;
					}
				}
			} 
			else
			{
				int i = str.indexOf("*/");
				if (i != -1)
				{
					noteFlag = false;
					index = i + 2;
					continue;
				} else
					break;
			}
			
			
			if (word == null)
			{
				index++;
				continue;
			}

			wordList.add(word);
			index++;
		}
	}

	//�ʷ���������
	public ArrayList<Word> lexAnalyse(String str) {
		String buffer[];
		buffer = str.split("\n");
		int line = 1;
		for (int i = 0; i < buffer.length; i++) {
			analyse(buffer[i].trim(), line);
			line++;
		}
		if (!wordList.get(wordList.size() - 1).type.equals(Word.END)) {
			Word word = new Word(++wordCount, "#", Word.END, line++);
			wordList.add(word);
		}
		return wordList;
	}

	//�ʷ���������
	public ArrayList<Word> lexAnalyse1(String filePath) throws IOException {
		FileInputStream 	fis 	= new FileInputStream(filePath);
		BufferedInputStream bis 	= new BufferedInputStream(fis);
		InputStreamReader 	isr 	= new InputStreamReader(bis, "utf-8");
		BufferedReader 		inbr 	= new BufferedReader(isr);
		String str = "";
		int line = 1;
		while ((str = inbr.readLine()) != null) {
			// System.out.println(str);
			analyse(str.trim(), line);
			line++;
		}
		inbr.close();
		if (!wordList.get(wordList.size() - 1).type.equals(Word.END)) {
			Word word = new Word(++wordCount, "#", Word.END, line++);
			wordList.add(word);
		}
		return wordList;
	}

	//�ʷ�����������
	public String outputWordList() throws IOException {
		File file = new File("./output/");
		if (!file.exists()) {
			file.mkdirs();
			file.createNewFile();// �������ļ������ھʹ�����
		}
		String 					path 	= file.getAbsolutePath();
		FileOutputStream 		fos 	= new FileOutputStream(path + "/wordList.txt");
		BufferedOutputStream 	bos 	= new BufferedOutputStream(fos);
		OutputStreamWriter 		osw1 	= new OutputStreamWriter(bos, "utf-8");
		PrintWriter 			pw1 	= new PrintWriter(osw1);
		pw1.println("�������\t���ʵ�ֵ\t\t��������\t\t���������� \t�����Ƿ�Ϸ�");
		Word word;
		for (int i = 0; i < wordList.size(); i++) {
			word = wordList.get(i);
			pw1.println(word.id + "\t" + word.value + "\t\t" + word.type + "\t"
					+ "\t" + word.line + "\t\t" + word.flag);
		}
		if (lexErrorFlag) {
			Error error;
			pw1.println("������Ϣ���£�");

			pw1.println("�������\t������Ϣ\t\t���������� \t���󵥴�");
			for (int i = 0; i < errorList.size(); i++) {
				error = errorList.get(i);
				pw1.println(error.id + "\t" + error.info + "\t" + error.line
						+ "\t\t" + error.word.value);
			}
		} else {
			pw1.println("�ʷ�����ͨ����");
		}
		pw1.close();
		return path + "/wordList.txt";
	}
	
	//�򵥳������ű����
	public String outputConstList() throws IOException {
		File file = new File("./output/");
		if (!file.exists()) {
			file.mkdirs();
			file.createNewFile();// �������ļ������ھʹ�����
		}
		String 						path 	= file.getAbsolutePath();
		FileOutputStream 			fos 	= new FileOutputStream(path + "/synboltable.txt");
		BufferedOutputStream 		bos 	= new BufferedOutputStream(fos);
		OutputStreamWriter 			osw1 	= new OutputStreamWriter(bos, "utf-8");
		PrintWriter 				pw1 	= new PrintWriter(osw1);
		pw1.println("---------------------------������-------------------------------------------");//��������
		{
			ConstWord constWord;
			if(constList.size()>0){
				pw1.println("���ʵ�ֵ\t\t�������� \t\t��ռ�洢��Ԫ\n");
				for (int i = 0; i < constList.size(); i++) {
					constWord = constList.get(i);
					pw1.println( constWord.value + "\t\t" + constWord.type+ "\t\t"+constWord.offset);
					pw1.println("------------------------------------------------------------------------------------------");
				}
				
			}else{
				pw1.println("\t\t������Ϊ��\n");
			}
			
		}
		pw1.println("\n------------------------------------------------------------------------------------------");//��������
		pw1.close();
		return path + "/synboltable.txt";
	}
	
	public static void main(String[] args) throws IOException 
	{
		LexAnalyse lex = new LexAnalyse();
		lex.lexAnalyse1("b.c");
		lex.outputWordList();
	}

	//��ö�Ӧ��token����
	public int getToken(String value,String type){
		//����string value(word.value),string type(word type)����ͬ����token����
		Token temp=null;
		//{//������ζ������ܱ� tokenList_all
		//�ܱ��tokennum���ǵ����ģ����ǰ���ϵͳ�趨��
		temp=new Token(value,type);
		tokenList_all.add(temp);
		
		
		if(type=="��־��"){//��IT
			int tokennum=tokenList_IT.size();
			if(tokennum==0)
			{
				temp=new Token(tokennum+1,value,type);
				tokenList_IT.add(temp);
			}else{
				temp=new Token(tokennum+1,value,type);
				if(!tokenList_IT.contains(temp)){
					tokenList_IT.add(temp);
				}
			}
			return tokennum;
		}else if(type=="�ؼ���"){//��KT
			int tokennum=tokenList_KT.size();
			if(tokennum==0)
			{
				temp=new Token(tokennum+1,value,type);
				tokenList_KT.add(temp);
			}else{
				temp=new Token(tokennum+1,value,type);
				if(!tokenList_KT.contains(temp)){
					tokenList_KT.add(temp);
				}
			}
			return tokennum;
		}else if(type=="���"){//��PT
			int tokennum=tokenList_PT.size();
			if(tokennum==0)
			{
				temp=new Token(tokennum+1,value,type);
				tokenList_PT.add(temp);
			}else{
				temp=new Token(tokennum+1,value,type);
				if(!tokenList_PT.contains(temp)){
					tokenList_PT.add(temp);
				}
			}
			return tokennum;
		}else if(type=="������"){//��PT
			int tokennum=tokenList_PT.size();
			if(tokennum==0)
			{
				temp=new Token(tokennum+1,value,type);
				tokenList_PT.add(temp);
			}else{
				temp=new Token(tokennum+1,value,type);
				if(!tokenList_PT.contains(temp)){
					tokenList_PT.add(temp);
				}
			}
			return tokennum;
		}else if(type=="�����"){//��PT
			int tokennum=tokenList_PT.size();
			if(tokennum==0)
			{
				temp=new Token(tokennum+1,value,type);
				tokenList_PT.add(temp);
			}else{
				temp=new Token(tokennum+1,value,type);
				if(!tokenList_PT.contains(temp)){
					tokenList_PT.add(temp);
				}
			}
			return tokennum;
		}else if(type=="���γ���"){//��CT
			int tokennum=tokenList_CT.size();
			if(tokennum==0)
			{
				temp=new Token(tokennum+1,value,type);
				tokenList_CT.add(temp);
			}else{
				temp=new Token(tokennum+1,value,type);
				if(!tokenList_CT.contains(temp)){
					tokenList_CT.add(temp);
				}
			}
			return tokennum;
		}/*else if(type=="�ַ�����"){//��CT
			int tokennum=tokenList_CT.size();
			if(tokennum==0)
			{
				temp=new Token(tokennum+1,value,type);
				tokenList_CT.add(temp);
			}else{
				temp=new Token(tokennum+1,value,type);
				if(!tokenList_CT.contains(temp)){
					tokenList_CT.add(temp);
				}
			}
			return tokennum;
		}else if(type=="��������"){//��CT
			int tokennum=tokenList_CT.size();
			if(tokennum==0)
			{
				temp=new Token(tokennum+1,value,type);
				tokenList_CT.add(temp);
			}else{
				temp=new Token(tokennum+1,value,type);
				if(!tokenList_CT.contains(temp)){
					tokenList_CT.add(temp);
				}
			}
			return tokennum;
		}*/else if(type=="���㳣��"){//��CT
			int tokennum=tokenList_CT.size();
			if(tokennum==0)
			{
				temp=new Token(tokennum+1,value,type);
				tokenList_CT.add(temp);
			}else{
				temp=new Token(tokennum+1,value,type);
				if(!tokenList_CT.contains(temp)){
					tokenList_CT.add(temp);
				}
			}
			return tokennum;
		}
		return -1;//����
	}

	//���token
	public String outputToken() throws IOException {
		File file = new File("./output/");
		if (!file.exists()) {
			file.mkdirs();
			file.createNewFile();// �������ļ������ھʹ�����
		}
		String 					path 	= file.getAbsolutePath();
		FileOutputStream 		fos 	= new FileOutputStream(path + "/tokenList.txt");
		BufferedOutputStream 	bos 	= new BufferedOutputStream(fos);
		OutputStreamWriter 		osw1 	= new OutputStreamWriter(bos, "utf-8");
		PrintWriter 			pw1 	= new PrintWriter(osw1);
		Token tok;
		pw1.println("-----------------------------------------Token���б�------------------------------------------");//TK
		if(tokenList_all.size()>0){
			pw1.println("\t\t���\t\t���� \t\t��Ӧ��ֵ\n");
			for (int i = 0; i < tokenList_all.size(); i++) {
				tok = tokenList_all.get(i);
				pw1.println( "\t\t"+(i+1) + "\t\t" + tok.value+ "\t\t  "+tok.i);
				pw1.println("----------------------------------------------------------------------------------------------");
			}
		}else{
			pw1.println("\t\tToken���б�Ϊ��\n");
		}
		pw1.println("----------------------------------------------------------------------------------------------");//TK
		
		pw1.println("-----------------------------------------KT�ؼ��ֱ�-------------------------------------------");//KT
		if(tokenList_KT.size()>0){
			pw1.println("\t\t\t���\t\t����\n");
			for (int i = 0; i < tokenList_KT.size(); i++) {
				tok = tokenList_KT.get(i);
				pw1.println( "\t\t\t"+(i+1) + "\t\t" + tok.value);
				pw1.println("----------------------------------------------------------------------------------------------");
			}
		}else{
			pw1.println("\t\tKT�ؼ��ֱ�Ϊ��\n");
		}
		pw1.println("----------------------------------------------------------------------------------------------");//KT
		pw1.println("-----------------------------------------PT����ű�-------------------------------------------");//PT
		if(tokenList_PT.size()>0){
			pw1.println("\t\t\t���\t\t����\n");
			for (int i = 0; i < tokenList_PT.size(); i++) {
				tok = tokenList_PT.get(i);
				pw1.println( "\t\t\t"+(i+1) + "\t\t" + tok.value);
				pw1.println("----------------------------------------------------------------------------------------------");
			}
		}else{
			pw1.println("\t\tPT����ű�Ϊ��\n");
		}
		pw1.println("----------------------------------------------------------------------------------------------");//PT
		pw1.println("------------------------------------------CT������--------------------------------------------");//CT
		if(tokenList_CT.size()>0){
			pw1.println("\t\t\t���\t\t����\n");
			for (int i = 0; i < tokenList_CT.size(); i++) {
				tok = tokenList_CT.get(i);
				pw1.println( "\t\t\t"+(i+1) + "\t\t" + tok.value);
				pw1.println("----------------------------------------------------------------------------------------------");
			}
			}else{
			pw1.println("\t\tCT������Ϊ��\n");
		}
		pw1.println("----------------------------------------------------------------------------------------------");//CT
		pw1.println("-----------------------------------------IT��ʶ����-------------------------------------------");//IT
		if(tokenList_IT.size()>0){
			pw1.println("\t\t\t���\t\t����\n");
			for (int i = 0; i < tokenList_IT.size(); i++) {
				tok = tokenList_IT.get(i);
				pw1.println( "\t\t\t"+(i+1) + "\t\t" + tok.value);
				pw1.println("----------------------------------------------------------------------------------------------");
			}
		}else{
			pw1.println("\t\tIT��ʶ����Ϊ��\n");
		}
		pw1.println("----------------------------------------------------------------------------------------------");//IT
		pw1.close();
		return path + "/tokenList.txt";
	}
}
