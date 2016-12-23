package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.*;
import compiler.*;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	//�ʷ���:Token����������ʶ����(�ʷ�����)
	//�����:
	
	
	TextArea 	sourseFile;//������ʾԴ�ļ����ı���
	String 		soursePath;// Դ�ļ�·��
	String 		LL1Path;
	String 		wordListPath;
	String 		synbolTablePath;
	String 		TokenTablePath;
	String 		fourElementPath;
	LexAnalyse 	lexAnalyse;
	
	Parser parser;
	
	public MainFrame() {
		this.init();
	}

	public void init() {

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screen = toolkit.getScreenSize();
		setTitle("��C������");
		setSize(750, 480);
		super.setResizable(false);
		super.setLocation(screen.width / 2 - this.getWidth() / 2, screen.height
				/ 2 - this.getHeight() / 2);
		this.setContentPane(this.createContentPane());
	}

	private JPanel createContentPane() {
		JPanel p = new JPanel(new BorderLayout());
		p.add(BorderLayout.NORTH, createUpPane());
		p.add(BorderLayout.CENTER, createcCenterPane());
		p.add(BorderLayout.SOUTH, creatBottomPane());
		// p.setBorder(new EmptyBorder(8,8,8,8));
		return p;
	}

	private Component createUpPane() {
		JPanel p = new JPanel(new FlowLayout());
		final FilePanel fp = new FilePanel("ѡ��������ļ�");
		JButton button = new JButton("ȷ��");
		button.addActionListener(new ActionListener() 
		{
		
			//@Override
			public void actionPerformed(ActionEvent e) 
			{
				String text;
				try {
					soursePath = fp.getFileName();
					text = readFile(soursePath);
					sourseFile.setText(text);

				} catch (IOException e1) {
					e1.printStackTrace();
				}            

			}
		});
		p.add(fp);
		p.add(button);
		return p;
	}

	private Component createcCenterPane() {
		JPanel p = new JPanel(new BorderLayout());
		JLabel label = new JLabel("Դ�ļ����£�");
		sourseFile = new TextArea();
		sourseFile.setText("");
		p.add(BorderLayout.NORTH, label);
		p.add(BorderLayout.CENTER, sourseFile);
		return p;
	}

	private Component creatBottomPane() {
		JPanel p = new JPanel(new FlowLayout());
		JButton bt0 = new JButton("�ʷ�������");
		JButton bt1 = new JButton("�ʷ�����");
		JButton bt2 = new JButton("�﷨����");
		JButton bt3 = new JButton("���ű�����");
		JButton bt4 = new JButton("�м��������");
		JButton bt5 = new JButton("Ŀ���������");
		
		bt0.addActionListener(new ActionListener() {

			//@Override
			public void actionPerformed(ActionEvent e) {
				try {
					lexAnalyse=new LexAnalyse(sourseFile.getText());
					TokenTablePath = lexAnalyse.outputToken();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				Dimension screen = toolkit.getScreenSize();
				InfoFrame inf = new InfoFrame("�ʷ�������", TokenTablePath);
				inf.setSize(540,750);
				inf.setLocation(screen.width / 2 - inf.getWidth() / 2, screen.height
						/ 2 - inf.getHeight() / 2);
				inf.setVisible(true);
			}
		});
		
		
		bt1.addActionListener(new ActionListener() {

			//@Override//outputToken
			public void actionPerformed(ActionEvent e) {
				try {
					lexAnalyse=new LexAnalyse(sourseFile.getText());
					wordListPath = lexAnalyse.outputWordList();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				Dimension screen = toolkit.getScreenSize();
				InfoFrame inf = new InfoFrame("�ʷ�����", wordListPath);
				inf.setSize(750,1000);
				
				inf.setLocation(screen.width / 2 - inf.getWidth() / 2, screen.height
						/ 2 - inf.getHeight() / 2);
				
				inf.setVisible(true);
			}
		});
		
		bt2.addActionListener(new ActionListener() {

			//@Override
			public void actionPerformed(ActionEvent e) {
			lexAnalyse=new LexAnalyse(sourseFile.getText());
			parser=new Parser(lexAnalyse);
				try {
					parser.grammerAnalyse();
					LL1Path= parser.outputLL1();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				Dimension screen = toolkit.getScreenSize();
				InfoFrame inf = new InfoFrame("�﷨����", LL1Path);
				inf.setSize(750,1000);
				inf.setLocation(screen.width / 2 - inf.getWidth() / 2, screen.height
						/ 2 - inf.getHeight() / 2);
				inf.setVisible(true);
			}
		});
		
		bt3.addActionListener(new ActionListener() {

			//@Override
			public void actionPerformed(ActionEvent e) {
				try {
					lexAnalyse=new LexAnalyse(sourseFile.getText());
					synbolTablePath = lexAnalyse.outputConstList();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				Dimension screen = toolkit.getScreenSize();
				InfoFrame inf = new InfoFrame("���ű�����", synbolTablePath);
				inf.setSize(750,480);
				inf.setLocation(screen.width / 2 - inf.getWidth() / 2, screen.height
						/ 2 - inf.getHeight() / 2);
				inf.setVisible(true);
			}
		});
		
		bt4.addActionListener(new ActionListener() {

			//@Override
		public void actionPerformed(ActionEvent e) {
			try {
				lexAnalyse=new LexAnalyse(sourseFile.getText());
				parser=new Parser(lexAnalyse);
				parser.grammerAnalyse();
				fourElementPath=parser.outputFourElem();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension screen = toolkit.getScreenSize();
			InfoFrame inf = new InfoFrame("�м��������", fourElementPath);
			inf.setSize(480,800);
			inf.setLocation(screen.width / 2 - inf.getWidth() / 2, screen.height
					/ 2 - inf.getHeight() / 2);
			inf.setVisible(true);
		}
		});
		
		bt5.addActionListener(new ActionListener() {

			//@Override
			public void actionPerformed(ActionEvent e) {
				try {
					lexAnalyse=new LexAnalyse(sourseFile.getText());
					parser=new Parser(lexAnalyse);
					parser.grammerAnalyse();
					fourElementPath=parser.outputFourElem1();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				huibian inf = new huibian("Ŀ���������", fourElementPath);
				inf.setVisible(true);
			}
		});
		p.add(bt0);
		p.add(bt1);
		p.add(bt2);
		p.add(bt3);
		p.add(bt4);
		p.add(bt5);
		return p;
	}

	public static String readFile(String fileName) throws IOException {
		StringBuilder sbr = new StringBuilder();
		String str;
		FileInputStream fis = new FileInputStream(fileName);
		BufferedInputStream bis = new BufferedInputStream(fis);
		InputStreamReader isr = new InputStreamReader(bis, "UTF-8");
		BufferedReader in = new BufferedReader(isr);
		while ((str = in.readLine()) != null) {
			sbr.append(str).append('\n');
		}
		in.close();
		return sbr.toString();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainFrame mf = new MainFrame();
		//TinyCompiler tinyCompiler = new TinyCompiler();
		//mf.tinyCompiler = tinyCompiler;
		mf.setVisible(true);
	}
}

class FilePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	FilePanel(String str) {
		JLabel label = new JLabel(str);
		JTextField fileText = new JTextField(35);
		JButton chooseButton = new JButton("���...");
		this.add(label);
		this.add(fileText);
		this.add(chooseButton);
		clickAction ca = new clickAction(this);
		chooseButton.addActionListener(ca);
	}

	public String getFileName() {
		JTextField jtf = (JTextField) this.getComponent(1);
		return jtf.getText();    
	}

	// ��ť��Ӧ����
	private class clickAction implements ActionListener {
		private Component cmpt;

		clickAction(Component c) {
			cmpt = c;
		}

		public void actionPerformed(ActionEvent event) {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File("."));
			int ret = chooser.showOpenDialog(cmpt);
			if (ret == JFileChooser.APPROVE_OPTION) {
				JPanel jp = (JPanel) cmpt;
				JTextField jtf = (JTextField) jp.getComponent(1);//��ȡ���
				jtf.setText(chooser.getSelectedFile().getPath());
			}
		}
	}
}
