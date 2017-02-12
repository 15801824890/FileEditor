package com.shiyanlou.fileeditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.print.attribute.standard.OutputDeviceAssigned;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Scrollable;

public class FileEditor extends JFrame{
    private JTextField selectField;
    private JTextArea editArea;
    private JButton saveBtn;
    private JButton openFileBtn;
    //��¼Ŀ¼�����
    private int level=0;
    public FileEditor(){
    	this.init();
    }
	private void init() {
		// TODO Auto-generated method stub
		this.setTitle("Editor");
		this.setBounds(300, 50, 600, 650);
		selectField=new JTextField(40);
		openFileBtn=new JButton("Browse");
		openFileBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				FileEditor.this.level=0;
				String path=selectField.getText();
				//���Ŀ¼�ļ�
				openDirOrFile(path.replaceAll("//", "\\\\"));
			}
		});
		JPanel upPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		upPanel.setBackground(Color.CYAN);
		upPanel.add(selectField);
		upPanel.add(openFileBtn);
		this.add(upPanel,BorderLayout.NORTH);
		
		//�����ı��༭��
		editArea=new JTextArea();
		ScrollPane scrollPane=new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
		scrollPane.add(editArea);
		this.add(scrollPane,BorderLayout.CENTER);
		
		//��������
		saveBtn=new JButton("Save");
		saveBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				saveFile();
			}
		});
		
		JPanel southPanel=new JPanel();
		southPanel.setBackground(Color.green);
		southPanel.add(saveBtn);
		this.add(southPanel,BorderLayout.SOUTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	//save 
	private void saveFile() {
		FileDialog fd=new FileDialog(this,"Save File");
		//���ú�׺
		fd.setFile("untitled.txt");
		fd.setMode(FileDialog.SAVE);
		fd.setVisible(true);
		//��ȡ�ļ���
		String fileName=fd.getFile();
		//��ȡ�Ի���ǰĿ¼
		String dir=fd.getDirectory();
		//����Ҫ�����Ŀ���ļ�
		File newFile=new File(dir+File.separator+fileName);
		PrintWriter pw=null;
		try {
			pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(newFile)));
			String str=editArea.getText();
			pw.println(str);
			pw.flush();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			pw.close();
		}
	}
	
       	
		private void openDirOrFile(String absolutePath) {
				// TODO Auto-generated method stub
				//absolutePath ָ��Ŀ¼�ļ�����·����
				File file=new File(absolutePath);
				//�ж��ļ���Ŀ¼�Ƿ����
				if (!(file.exists())) {
					editArea.setText("The file does not exist");
				}else if (file.isDirectory()) {//�ж��Ƿ��Ǹ�Ŀ¼
					editArea.setText(null);
					showDir(file);
				}else if (file.isFile()) {//�ж��Ƿ��ļ�
					try {
						FileInputStream fis=new FileInputStream(file);
						BufferedReader br=new BufferedReader(new InputStreamReader(fis));
						String str=null;
						editArea.setText(null);
						while ((str=br.readLine())!=null) {
							editArea.append(str+"\r\n");
						}
						br.close();
						
					} catch (Exception e) {
						// TODO: handle exception
						e.getStackTrace();
					}
				}
			}
            //���Ŀ¼����������ͼ
			private void showDir(File directory) {
				// TODO Auto-generated method stub
				File[] files=directory.listFiles();
				int len=files.length;
				for (int i = 0; i < len; i++) {
					if (files[i].isDirectory()) {
						for (int j = 0; j <this.level; j++) {
							editArea.append("  ");
						}
						editArea.append("|--"+files[i].getName()+"(Folder)\r\n");
						this.level++;
						showDir(files[i]);
						this.level--;
						
					}else if (files[i].isFile()) {
						for (int j = 0; j < this.level; j++) {
							editArea.append("   ");
						}
						editArea.append("|--"+files[i].getAbsolutePath()+"\r\n");
					}
				}
			}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
          new FileEditor();
	}

}
