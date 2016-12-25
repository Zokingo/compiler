package compiler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.JPanel;
//import javax.swing.JTextArea;
import java.awt.*;

public class huibian extends JFrame {

	private static final long serialVersionUID = 8766059377195109228L;
	private static String title;
	private static String fileName;
	
	private static TextArea text;
	
	public huibian() {
		init();
	}
	
	public huibian(String title,String fileName){
		huibian.title=title;
		huibian.fileName=fileName;
		init();
		this.setTitle(title);
		try {
			String str[] =readFile(fileName).split("\n");//妈蛋修改完四元式之后，文本格式出错了
			String temp2="1 example   SEGMENT\n2 ASSUME CS:example,DS:example\n";
			text.append(temp2);
			int i;
			int j=2;
			for( i = 2; i < str.length; i++,j++)
			{
				
				String temp[] = str[i].split(",");
				//让行号加2
				if(temp[0].charAt(temp[0].length() -1) == '='){
					String temp1=(j+1)+"  MOV  "+"AX,"+temp[1] + "\n";
					text.append(temp1);
					temp1 = (j+1) + "  MOV  " + temp[3].substring(0,temp[3].length() - 1) + ",AX" +"\n";
					text.append(temp1);
				}else if(temp[0].charAt(temp[0].length() -1) == '+' && temp[0].charAt(temp[0].length() -2) == '+'){
					String temp1 = (j+1) + " INC "  + temp[1] + "\n";
					text.append(temp1);
				}else if(temp[0].charAt(temp[0].length() -1) == '+'){
					String temp1 = (j+1) + " ADD " + temp[3].substring(0,temp[3].length() - 1) + "," + temp[1] + "\n";
					text.append(temp1);
				}else if(temp[0].charAt(temp[0].length() -1) == '-'){
					String temp1 = (j+1) + " SUB " + temp[3].substring(0,temp[3].length() - 1) + "," + temp[1] + "\n";
					text.append(temp1);
				}else if(temp[0].charAt(temp[0].length() -1) == '*'){
					String temp1 = (j+1) + " MUL " + temp[3].substring(0,temp[3].length() - 1) + "," + temp[1] + "\n";
					text.append(temp1);
				}else if(temp[0].charAt(temp[0].length() -1) == '/'){
					String temp1 = (j+1) + " DIV "  + temp[3].substring(0,temp[3].length() - 1) + "," + temp[1] + "\n";
					text.append(temp1);
				}else if(temp[0].charAt(temp[0].length() -1) == 'J' && temp[0].charAt(temp[0].length() -2) == 'R'){
					String temp1 = (j+1) + " JMP " + temp[3].substring(0,temp[3].length() - 1)  + "\n";
					text.append(temp1);
				}else if(temp[0].charAt(temp[0].length() -1) == 'J' && temp[0].charAt(temp[0].length() -2) == 'F'){
					String temp1 = (j+1) + " JZ " + temp[3].substring(0,temp[3].length() - 1)  + "\n";
					text.append(temp1);
				}else if(temp[0].charAt(temp[0].length() -1) == '>'){
					String temp1 = (j+1) + " JG " + temp[3].substring(0,temp[3].length() - 1)  + "\n";
					text.append(temp1);
				}else if(temp[0].charAt(temp[0].length() -1) == '<'){
					String temp1 = (j+1) + " JL " + temp[3].substring(0,temp[3].length() - 1)  + "\n";
					text.append(temp1);
				}else{
					String temp1 = (j+1) +"未知四元式处理：后续待添加"+"\n";
					text.append(temp1);
				}
			}
			j=j+1;
			String temp3=j+" INT   21H\n"+(j+1)+" RET\n"+(j+2)+" example ENDS\n";
			text.append(temp3);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}	
	}
	
	private void init() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screen = toolkit.getScreenSize();
		setSize(500, 780);
		super.setLocation(screen.width / 2 - this.getWidth() / 2, screen.height
				/ 2 - this.getHeight() / 2);
		setContentPane(createContentPane());
	}

	private Container createContentPane() {
		JPanel pane = new JPanel(new BorderLayout());
		text = new TextArea();
		text.setForeground(Color.BLUE);
		//text.setFont(new Font("宋体",Font.PLAIN,32));
		pane.add(BorderLayout.CENTER, text);
		return pane;
	}
	
	private String readFile(String filename) 
	throws IOException{
			StringBuilder sbr = new StringBuilder();
			String str;
			FileInputStream fis = new FileInputStream(filename);
			BufferedInputStream bis = new BufferedInputStream(fis);
			InputStreamReader isr = new InputStreamReader(bis, "UTF-8");
			BufferedReader in=new BufferedReader(isr);
			while((str=in.readLine())!=null){
				sbr.append(str).append('\n');
			}
			in.close();
		return sbr.toString();
	}
	
	public static String getTitl() {
		return title;
	}

	public static void setTitl(String title) {
		huibian.title = title;
	}

	public static String getFileName() {
		return fileName;
	}

	public static void setFileName(String fileName) {
		huibian.fileName = fileName;
	}

	public static TextArea getText() {
		return text;
	}

	public static void setText(TextArea jText) {
		huibian.text = jText;
	}
	
	public static void main(String[] args) {
		huibian inf=new huibian("测试","test.txt");
		inf.setVisible(true);
	}

}
