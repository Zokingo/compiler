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
 * 词法分析器
 * 
 * 
 */
public class LexAnalyse {

	ArrayList<Word> 	wordList 		= new ArrayList<Word>();//单词表
	public ArrayList<ConstWord>constList= new ArrayList<ConstWord>();//常数表
	ArrayList<Error> 	errorList 		= new ArrayList<Error>();// 错误信息列表
	
	ArrayList<Token>	tokenList_all	= new ArrayList<Token>();//总token表不能重复
	ArrayList<Token>	tokenList_KT	= new ArrayList<Token>();//关键字表
	ArrayList<Token>	tokenList_PT	= new ArrayList<Token>();//界符表
	ArrayList<Token>	tokenList_IT	= new ArrayList<Token>();//标识符表
	ArrayList<Token>	tokenList_CT	= new ArrayList<Token>();//常量表
	
	int 				wordCount 		= 0;// 统计单词个数
	int 				errorCount 		= 0;// 统计错误个数
	boolean 			noteFlag 		= false;// 多行注释标志
	boolean 			lexErrorFlag 	= false;// 词法分析出错标志
	
	public LexAnalyse() {

	}

	public LexAnalyse(String str) {
		lexAnalyse(str);
	}
	/**
	 * 数字字符判断
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
	 * 判断单词是否为int常量
	 * 
	 * @param string
	 * @return
	 */
	private static boolean isInteger(String word) 
	{
		int i;
		boolean flag = false;
		for (i = 1; i < word.length(); i++) //因为已经确定首符号符合整数类型定义所以下标从1开始
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
	 * 判断单词是否为float常量
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
				//sum = sum * 10 + word.charAt(i) - '0'; //根据数字串表示将其值算出存入sum
				continue;
			} 
			else 
			{
				break;
			}
		}
		
		if (word.charAt(i) == 'e' || word.charAt(i) == 'E')    //指数的判断
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
			//sum = sum*Math.pow(10, tempp);  //pow(x,y)计算x的y次幂.
		}
		if (word.charAt(i) == '.')    //小数的判断
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
			//sum = sum + tempp;     //根据数字串表示将其值算出存入sum
		}
		
		if (i == word.length()) 
		{
			//将sum赋值给word.value替换原来的值
			//word.value=sum;
			flag = true;
		}
		return flag;
	}

	/**
	 * 判断字符是否为字母
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
	 * 判断单词是否为合法标识符
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
	 * 判断词法分析是否通过
	 * 
	 */
	public boolean isFail() {
		return lexErrorFlag;
	}

	//词法分析
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
				{// 判断是不是标志符
					beginIndex = index;
					index++;
					//temp=str.charAt(index);
					while ((index < length)//切割单词
							&& (!Word.isBoundarySign(str.substring(index,index + 1)))
							//&& (!Word.isOperator(str.substring(index, index + 1)))	//注释这段可以进行更高级的切割单词进行词法分析报错	
							&& (!Word.isOperator(str.substring(index, index + 1)))//语法分析时因为涉及简单的单词切割因此不能注释这段
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
						error = new Error(errorCount, "非法标识符", word.line, word);
						errorList.add(error);
						lexErrorFlag = true;
					}
					index--;

				} 
				else if (isDigit(temp)) //正或负数，正数可以省略||temp=='-'||temp=='+'
				{// 判断是不是int常数,float常量（指数和小数）

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
					
					//分析切割得到的单词
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
						error = new Error(errorCount, "非法标识符", word.line, word);
						errorList.add(error);
						lexErrorFlag = true;
					}
					index--;
				} 
				else if (String.valueOf(str.charAt(index)).equals("'")) 
				{// 字符常量
					// flag=true;
					beginIndex = index;
					index++;
					temp = str.charAt(index);
					
					
					while (index < length && (0 <= temp && temp <= 255)) 
					{
						if (temp=='\\')//转义符号
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
						word.value = str.substring(beginIndex, endIndex);//去掉单引号？？但是要把单引号当作界符存入
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
						error = new Error(errorCount, "非法标识符", word.line, word);
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
				else if (temp == '+') //也有可能是数值+1.223或者+5等表示
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

					}else if (isDigit(temp)) //正或负数，正数可以省略||temp=='-'||temp=='+'
					{// 判断是不是int常数,float常量（指数和小数）

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
						
						//分析切割得到的单词
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
							error = new Error(errorCount, "非法标识符", word.line, word);
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
					}else if (isDigit(temp)) //正或负数，正数可以省略||temp=='-'||temp=='+'
					{// 判断是不是int常数,float常量（指数和小数）

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
						
						//分析切割得到的单词
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
							error = new Error(errorCount, "非法标识符", word.line, word);
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
				else if (temp == '/')//其它符号的处理
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
				{// 不是标识符、数字常量、字符串常量

					switch (temp) 
					{
					case ' ':
					case '\t':
					case '\r':
					case '\n':
						word = null;
						break;// 过滤空白字符
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
					// case '+':前面已经处理过的符号
					// case '-':前面已经处理过的符号
					case '*':
					// case '/':前面已经处理过的符号
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
						error = new Error(errorCount, "非法标识符", word.line, word);
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

	//词法分析最终
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

	//词法分析测试
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

	//词法分析结果输出
	public String outputWordList() throws IOException {
		File file = new File("./output/");
		if (!file.exists()) {
			file.mkdirs();
			file.createNewFile();// 如果这个文件不存在就创建它
		}
		String 					path 	= file.getAbsolutePath();
		FileOutputStream 		fos 	= new FileOutputStream(path + "/wordList.txt");
		BufferedOutputStream 	bos 	= new BufferedOutputStream(fos);
		OutputStreamWriter 		osw1 	= new OutputStreamWriter(bos, "utf-8");
		PrintWriter 			pw1 	= new PrintWriter(osw1);
		pw1.println("单词序号\t单词的值\t\t单词类型\t\t单词所在行 \t单词是否合法");
		Word word;
		for (int i = 0; i < wordList.size(); i++) {
			word = wordList.get(i);
			pw1.println(word.id + "\t" + word.value + "\t\t" + word.type + "\t"
					+ "\t" + word.line + "\t\t" + word.flag);
		}
		if (lexErrorFlag) {
			Error error;
			pw1.println("错误信息如下：");

			pw1.println("错误序号\t错误信息\t\t错误所在行 \t错误单词");
			for (int i = 0; i < errorList.size(); i++) {
				error = errorList.get(i);
				pw1.println(error.id + "\t" + error.info + "\t" + error.line
						+ "\t\t" + error.word.value);
			}
		} else {
			pw1.println("词法分析通过：");
		}
		pw1.close();
		return path + "/wordList.txt";
	}
	
	//简单常量符号表输出
	public String outputConstList() throws IOException {
		File file = new File("./output/");
		if (!file.exists()) {
			file.mkdirs();
			file.createNewFile();// 如果这个文件不存在就创建它
		}
		String 						path 	= file.getAbsolutePath();
		FileOutputStream 			fos 	= new FileOutputStream(path + "/synboltable.txt");
		BufferedOutputStream 		bos 	= new BufferedOutputStream(fos);
		OutputStreamWriter 			osw1 	= new OutputStreamWriter(bos, "utf-8");
		PrintWriter 				pw1 	= new PrintWriter(osw1);
		pw1.println("---------------------------常量表-------------------------------------------");//测试内容
		{
			ConstWord constWord;
			if(constList.size()>0){
				pw1.println("单词的值\t\t单词类型 \t\t所占存储单元\n");
				for (int i = 0; i < constList.size(); i++) {
					constWord = constList.get(i);
					pw1.println( constWord.value + "\t\t" + constWord.type+ "\t\t"+constWord.offset);
					pw1.println("------------------------------------------------------------------------------------------");
				}
				
			}else{
				pw1.println("\t\t常量表为空\n");
			}
			
		}
		pw1.println("\n------------------------------------------------------------------------------------------");//测试内容
		pw1.close();
		return path + "/synboltable.txt";
	}
	
	public static void main(String[] args) throws IOException 
	{
		LexAnalyse lex = new LexAnalyse();
		lex.lexAnalyse1("b.c");
		lex.outputWordList();
	}

	//获得对应的token号码
	public int getToken(String value,String type){
		//根据string value(word.value),string type(word type)来共同决定token号码
		Token temp=null;
		//{//无论如何都先填总表 tokenList_all
		//总表的tokennum不是递增的，而是按照系统设定的
		temp=new Token(value,type);
		tokenList_all.add(temp);
		
		
		if(type=="标志符"){//填IT
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
		}else if(type=="关键字"){//填KT
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
		}else if(type=="界符"){//填PT
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
		}else if(type=="结束符"){//填PT
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
		}else if(type=="运算符"){//填PT
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
		}else if(type=="整形常量"){//填CT
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
		}/*else if(type=="字符常量"){//填CT
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
		}else if(type=="布尔常量"){//填CT
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
		}*/else if(type=="浮点常量"){//填CT
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
		return -1;//出错
	}

	//输出token
	public String outputToken() throws IOException {
		File file = new File("./output/");
		if (!file.exists()) {
			file.mkdirs();
			file.createNewFile();// 如果这个文件不存在就创建它
		}
		String 					path 	= file.getAbsolutePath();
		FileOutputStream 		fos 	= new FileOutputStream(path + "/tokenList.txt");
		BufferedOutputStream 	bos 	= new BufferedOutputStream(fos);
		OutputStreamWriter 		osw1 	= new OutputStreamWriter(bos, "utf-8");
		PrintWriter 			pw1 	= new PrintWriter(osw1);
		Token tok;
		pw1.println("-----------------------------------------Token序列表------------------------------------------");//TK
		if(tokenList_all.size()>0){
			pw1.println("\t\t序号\t\t单词 \t\t对应码值\n");
			for (int i = 0; i < tokenList_all.size(); i++) {
				tok = tokenList_all.get(i);
				pw1.println( "\t\t"+(i+1) + "\t\t" + tok.value+ "\t\t  "+tok.i);
				pw1.println("----------------------------------------------------------------------------------------------");
			}
		}else{
			pw1.println("\t\tToken序列表为空\n");
		}
		pw1.println("----------------------------------------------------------------------------------------------");//TK
		
		pw1.println("-----------------------------------------KT关键字表-------------------------------------------");//KT
		if(tokenList_KT.size()>0){
			pw1.println("\t\t\t序号\t\t单词\n");
			for (int i = 0; i < tokenList_KT.size(); i++) {
				tok = tokenList_KT.get(i);
				pw1.println( "\t\t\t"+(i+1) + "\t\t" + tok.value);
				pw1.println("----------------------------------------------------------------------------------------------");
			}
		}else{
			pw1.println("\t\tKT关键字表为空\n");
		}
		pw1.println("----------------------------------------------------------------------------------------------");//KT
		pw1.println("-----------------------------------------PT界符号表-------------------------------------------");//PT
		if(tokenList_PT.size()>0){
			pw1.println("\t\t\t序号\t\t单词\n");
			for (int i = 0; i < tokenList_PT.size(); i++) {
				tok = tokenList_PT.get(i);
				pw1.println( "\t\t\t"+(i+1) + "\t\t" + tok.value);
				pw1.println("----------------------------------------------------------------------------------------------");
			}
		}else{
			pw1.println("\t\tPT界符号表为空\n");
		}
		pw1.println("----------------------------------------------------------------------------------------------");//PT
		pw1.println("------------------------------------------CT常数表--------------------------------------------");//CT
		if(tokenList_CT.size()>0){
			pw1.println("\t\t\t序号\t\t单词\n");
			for (int i = 0; i < tokenList_CT.size(); i++) {
				tok = tokenList_CT.get(i);
				pw1.println( "\t\t\t"+(i+1) + "\t\t" + tok.value);
				pw1.println("----------------------------------------------------------------------------------------------");
			}
			}else{
			pw1.println("\t\tCT常数表为空\n");
		}
		pw1.println("----------------------------------------------------------------------------------------------");//CT
		pw1.println("-----------------------------------------IT标识符表-------------------------------------------");//IT
		if(tokenList_IT.size()>0){
			pw1.println("\t\t\t序号\t\t单词\n");
			for (int i = 0; i < tokenList_IT.size(); i++) {
				tok = tokenList_IT.get(i);
				pw1.println( "\t\t\t"+(i+1) + "\t\t" + tok.value);
				pw1.println("----------------------------------------------------------------------------------------------");
			}
		}else{
			pw1.println("\t\tIT标识符表为空\n");
		}
		pw1.println("----------------------------------------------------------------------------------------------");//IT
		pw1.close();
		return path + "/tokenList.txt";
	}
}
