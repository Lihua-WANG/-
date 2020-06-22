package otherscene;

import java.io.*;
import java.net.*;
import java.util.*;

import scene.Thing;

public class Server extends Thread{
	ServerSocket welcome;
	Socket socketServer;
	BufferedReader brin;
	DataOutputStream brout;
	Otherscene jp;
	
	public Server(Otherscene j) throws Exception{
		welcome=new ServerSocket(6789);//����Ҫʹ�õĶ˿ں���Ϊ��������������������
		socketServer=welcome.accept();// ����,�ȴ��ͻ������Ӷ˿�
		brin=new BufferedReader(new InputStreamReader(socketServer.getInputStream()));//��ȡ�ͻ��˴������ֽ����浽������
		brout=new DataOutputStream(socketServer.getOutputStream());//Ϊ�׽��ַ���һ�����������
		jp=j;
		String str="";
		produce(str);
	}
	
	public void sendhook() throws Exception{
		String str="";
		switch(jp.Hhook1.state_hook) {
		case 0://�������ڿ���
			str="# "+jp.Hhook1.state_hook+" "+jp.Hhook1.x+" "+jp.Hhook1.y+" "+jp.Hhook1.zeta+" "
		            +jp.Hhook1.is_clock+" "+jp.Hhook1.w+'\n';
			brout.writeBytes(str);
			break;
		case 1://�������¼ж���
			str="# "+jp.Hhook1.state_hook+" "+jp.Hhook1.x+" "+jp.Hhook1.y+" "+jp.Hhook1.zeta+" "
					+jp.Hhook1.v+'\n';
			brout.writeBytes(str);
			break;
		case 2://��������������
			str="# "+jp.Hhook1.state_hook+" "+jp.Hhook1.x+" "+jp.Hhook1.y+" "
					+jp.Hhook1.v+" "+jp.Hhook1.hookPos[4]+'\n';
			brout.writeBytes(str);
			break;
		}
	}
	
	public void sendexit() throws Exception{
		brout.writeBytes("*"+'\n');
	}
	
	public void sendchoice(boolean a) throws Exception{
		if(a) {
			brout.writeBytes("%"+'\n');//�����˳�
		}
		else {
			brout.writeBytes("&"+'\n');
		}
	}
	
	public void sendstart() throws Exception{
		brout.writeBytes("!"+'\n');
	}
	
	public void sendstop() throws Exception{
		brout.writeBytes("("+'\n');
	}
	
	public void sendcontinue() throws Exception{
		brout.writeBytes(")"+'\n');
	}

	public void produce(String str) {
		try{str=brin.readLine();}catch(Exception e) {}
		jp.amount=Integer.parseInt(str);//�ӿͻ��˽���һ���������ٸ������Ʒ��������
		jp.bonus=new Thing2[jp.amount];
		jp.is_exist=new boolean[jp.amount];
		for(int i=0;i<jp.amount;i++) {
			jp.is_exist[i]=true;
		}
		jp.pigNum=new int[jp.amount];
		jp.pigCount=0;
		for(int i=0;i<jp.amount;i++) {
			try{str=brin.readLine();}catch(Exception e) {}
			Scanner in=new Scanner(str);
			String s=in.next();
			if(s.equalsIgnoreCase("pig")){
				jp.pigNum[jp.pigCount]=i;
				jp.pigCount++;
			}
			jp.bonus[i]=new Thing2(jp,s,in.nextInt(), in.nextInt());
            if(s.equalsIgnoreCase("pocket")) {
				jp.bonus[i].value=in.nextInt();
			}
			in.close();
		}
		try{str=brin.readLine();}catch(Exception e) {}
		jp.iAim=Integer.parseInt(str);
		for(int i=0;i<jp.amount;i++) {
			jp.bonus[i].getLabel();
		}
		jp.jlAim.setText("Ŀ������ֵ: "+jp.iAim);
		try {sendstart();}catch(Exception e) {}
		jp.gameState=0;
	}
	
	public void run() {
		String str="";
		try {sleep(300);}catch(Exception e1) {}
		while(true) {
			while(jp.gameState==0) {
				try {str=brin.readLine();}catch(Exception e) {str="$$$";}
				if(str.charAt(0)=='#') {
					Scanner in=new Scanner(str);
					in.next();
					switch(in.nextInt()) {
					case 0:
						if(jp.Hhook2.state_hook!=0) { //����˿��Ӳ�����
							if(jp.Hhook2.hookPos[4]!=-1) {//����˿����ϼ�����Ʒ
								jp.Hhook2.state_hook=0;
								jp.iMoney+=jp.bonus[jp.Hhook2.hookPos[4]].value;
								jp.jlMoney.setText("��������ֵ: "+jp.iMoney);
								jp.iMoney2+=jp.bonus[jp.Hhook2.hookPos[4]].value;
								jp.jlMoney_2.setText(""+jp.iMoney2);
								jp.remove(jp.bonus[jp.Hhook2.hookPos[4]].a);
								jp.is_exist[jp.Hhook2.hookPos[4]]=false;
								jp.Hhook2.hookPos[4]=-1;
								jp.Hhook2.v=6;
								jp.Hhook2.a=true;
								jp.Hhook2.x=in.nextFloat();
								jp.Hhook2.y=in.nextFloat();
							}
							else {
								jp.Hhook2.state_hook=0;
								jp.Hhook2.v=6;
								jp.Hhook2.a=true;
								jp.Hhook2.x=in.nextFloat();
								jp.Hhook2.y=in.nextFloat();
							}
						}
						else {
							jp.Hhook2.x=in.nextFloat();
						    jp.Hhook2.y=in.nextFloat();
						    jp.Hhook2.zeta=in.nextFloat();
						    jp.Hhook2.is_clock=in.nextInt();
						    jp.Hhook2.w=in.nextFloat();
						}
						break;
					case 1:
						if(jp.Hhook2.state_hook==0) {
							jp.Hhook2.state_hook=1;
							jp.Hhook2.v=6;
						    jp.Hhook2.x=in.nextFloat();
						    jp.Hhook2.y=in.nextFloat();
						    jp.Hhook2.zeta=in.nextFloat();
						    jp.Hhook2.v=in.nextInt();
						    jp.Hhook2.a=false;
						}
						else {
							jp.Hhook2.x=in.nextFloat();
						    jp.Hhook2.y=in.nextFloat();
						}
						break;
					case 2:
						if(jp.Hhook2.state_hook==2) {
							jp.Hhook2.x=in.nextFloat();
						    jp.Hhook2.y=in.nextFloat();
						}
						else {
							jp.Hhook2.state_hook=2;
							jp.Hhook2.x=in.nextFloat();
						    jp.Hhook2.y=in.nextFloat();
						    jp.Hhook2.v=in.nextInt();
						    jp.Hhook2.hookPos[4]=in.nextInt();
						    if(jp.Hhook2.hookPos[4]!=-1) jp.is_exist[jp.Hhook2.hookPos[4]]=false;
						}
						break;
					default:break;
					}
					in.close();
				}
				else if(str.charAt(0)=='*') {
					jp.gameState=1;
					System.out.println("���յ����˳�����");
					try{sendexit();}catch(Exception e2) {}
				}
				else if(str.charAt(0)=='(') {
					jp.gameState=2;
					jp.remove(jp.jlstop);
					jp.add(jp.jstop);
					jp.repaint();
					while(true) {
						try {str=brin.readLine();}catch(Exception e) {str="$$$";}
						if(str.equalsIgnoreCase(")")) break;
					}
					jp.remove(jp.jstop);
					jp.add(jp.jlstop);
					jp.gameState=0;
				}
				else if(str.charAt(0)=='@') {
					Scanner in=new Scanner(str);
					in.next();
					int i=in.nextInt();
					jp.Tp.direction[i]=in.nextInt();
					jp.bonus[jp.pigNum[i]].x=in.nextInt();
				}
				else;
				try {sleep(40);}catch(Exception e1) {}
			}
			while(jp.gameState!=0) {
				if(jp.gameState==2) {
					while(jp.gameState==2) System.out.println("stop");
				}
				else {
					try{str=brin.readLine();}catch(Exception e) {System.out.println("WHAT?");}
					if(str.charAt(0)=='%') {
						if(jp.jn.getText().equalsIgnoreCase("��һ��")) {
							jp.display();
							jp.repaint();
							try {sleep(3000);}catch(Exception e) {}
						}
						System.exit(0);
					}
					else if(str.charAt(0)=='&'){
						if(jp.jp1.getText().equalsIgnoreCase("���һ")) {
							jp.jp2.setText("��Ҷ���ѡ�����");
							jp.repaint();
							while(jp.gameState==1){try {sleep(30);}catch(Exception e1) {}}
							produce(str);
						}
						else {
							jp.remove(jp.gg);
							jp.remove(jp.je);
							jp.remove(jp.jp1);
							jp.remove(jp.jp2);
							jp.remove(jp.jn);
							jp.add(jp.jlExit);
							jp.add(jp.jlstop);
							if(jp.jn.getText().equals("����")) { 
								jp.remove(jp.player);
								jp.remove(jp.team);
								for(int i=0;i<5;i++) {
									jp.remove(jp.jlscore[i]);
									jp.remove(jp.jlteam[i]);
								}
							}
							jp.restart();
							produce(str);
						}
					}
					else;
				}
				try {sleep(30);}catch(Exception e1) {}
			}
		}
	}
}
