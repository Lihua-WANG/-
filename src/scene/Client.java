package scene;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client extends Thread{
	Scene jp;
	Socket socketClient;
	BufferedReader brin;
	DataOutputStream brout;
	
	public Client(Scene j) throws Exception{
		System.out.println("我想发送");
		socketClient=new Socket("localhost",6789);
		System.out.println("我已经执行完了发送程序");
		brin=new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
		brout=new DataOutputStream(socketClient.getOutputStream());
		jp=j;
	}
	
	public void sendlayout() throws Exception{
		String str=Integer.toString(jp.amount)+'\n';
		brout.writeBytes(str);
		for(int i=0;i<jp.amount;i++) {
			str=jp.bonus[i].type+" "+jp.bonus[i].x+" "+jp.bonus[i].y+'\n';
			if(jp.bonus[i].type.equalsIgnoreCase("pocket"))
				str=jp.bonus[i].type+" "+jp.bonus[i].x+" "+jp.bonus[i].y+" "+jp.bonus[i].value+'\n';
			brout.writeBytes(str);
		}
		str=Integer.toString(jp.iAim)+'\n';
		brout.writeBytes(str);
	}
	
	public void sendhook() throws Exception{
		String str="";
		switch(jp.Hhook2.state_hook) {
		case 0:
			str="# "+jp.Hhook2.state_hook+" "+jp.Hhook2.x+" "+jp.Hhook2.y+" "+jp.Hhook2.zeta+" "
		            +jp.Hhook2.is_clock+" "+jp.Hhook2.w+'\n';
			brout.writeBytes(str);
			break;
		case 1:
			str="# "+jp.Hhook2.state_hook+" "+jp.Hhook2.x+" "+jp.Hhook2.y+" "+jp.Hhook2.zeta+" "
					+jp.Hhook2.v+'\n';
			brout.writeBytes(str);
			break;
		case 2:
			str="# "+jp.Hhook2.state_hook+" "+jp.Hhook2.x+" "+jp.Hhook2.y+" "
					+jp.Hhook2.v+" "+jp.Hhook2.hookPos[4]+'\n';
			brout.writeBytes(str);
			break;
		}
	}
	
	public void sendexit() throws Exception{
		brout.writeBytes("*"+'\n');
	}
	
	public void sendstop() throws Exception{
		brout.writeBytes("("+'\n');
	}
	
	public void sendcontinue() throws Exception{
		brout.writeBytes(")"+'\n');
	}
	
	public void sendchoice(boolean a) throws Exception{
		if(a) {
			brout.writeBytes("%"+'\n');//代表退出
		}
		else {
			brout.writeBytes("&"+'\n');
		}
	}
	
	public void sendpig() throws Exception{
		String str="";
		for(int i=0;i<jp.pigCount;i++) {
			str="@"+" "+i+" "+jp.Tp.direction[i]+" "+jp.bonus[jp.pigNum[i]].x+'\n';
			brout.writeBytes(str);
		}
	}
	
	public void run(){
		String str="";
		while(jp.gameState!=0) {
			try {str=brin.readLine();} catch(Exception e) {}
			System.out.println(str);
			jp.gameState=0;
			try {sleep(30);}catch(Exception e1) {}
		}
		while(true) {
			while(jp.gameState==0) {
				if(jp.hook1flag==1 && jp.hook2flag==1) {
					try {str=brin.readLine();}catch(Exception e) {}
					//System.out.println(str);
					if(str.charAt(0)=='#') {
						Scanner in=new Scanner(str);
						in.next();
						switch(in.nextInt()) {
						case 0:
							if(jp.Hhook1.state_hook!=0) { 
								if(jp.Hhook1.hookPos[4]!=-1) {
									jp.Hhook1.state_hook=0;
									jp.iMoney+=jp.bonus[jp.Hhook1.hookPos[4]].value;
									jp.jlMoney.setText("共获金钱: "+jp.iMoney);
									jp.iMoney1+=jp.bonus[jp.Hhook1.hookPos[4]].value;
									jp.jlMoney_1.setText("$"+jp.iMoney1);
									jp.remove(jp.bonus[jp.Hhook1.hookPos[4]].a);
									jp.is_exist[jp.Hhook1.hookPos[4]]=false;
									jp.Hhook1.hookPos[4]=-1;
									jp.Hhook1.v=6;
									jp.Hhook1.a=true;
									jp.Hhook1.x=in.nextFloat();
									jp.Hhook1.y=in.nextFloat();
								}
								else {
									jp.Hhook1.state_hook=0;
									jp.Hhook1.v=6;
									jp.Hhook1.a=true;
									jp.Hhook1.x=in.nextFloat();
									jp.Hhook1.y=in.nextFloat();
								}
							}
							else {
								jp.Hhook1.x=in.nextFloat();
							    jp.Hhook1.y=in.nextFloat();
							    jp.Hhook1.zeta=in.nextFloat();
							    jp.Hhook1.is_clock=in.nextInt();
							    jp.Hhook1.w=in.nextFloat();
							}
							break;
						case 1:
							if(jp.Hhook1.state_hook==0) {
								jp.Hhook1.state_hook=1;
								jp.Hhook1.v=6;
							    jp.Hhook1.x=in.nextFloat();
							    jp.Hhook1.y=in.nextFloat();
							    jp.Hhook1.zeta=in.nextFloat();
							    jp.Hhook1.v=in.nextInt();
							    jp.Hhook1.a=false;
							}
							else {
								jp.Hhook1.x=in.nextFloat();
							    jp.Hhook1.y=in.nextFloat();
							}
							break;
						case 2:
							if(jp.Hhook1.state_hook==2) {
								jp.Hhook1.x=in.nextFloat();
							    jp.Hhook1.y=in.nextFloat();
							}
							else {
								jp.Hhook1.state_hook=2;
								jp.Hhook1.x=in.nextFloat();
							    jp.Hhook1.y=in.nextFloat();
							    jp.Hhook1.v=in.nextInt();
							    jp.Hhook1.hookPos[4]=in.nextInt();
							    if(jp.Hhook1.hookPos[4]!=-1) jp.is_exist[jp.Hhook1.hookPos[4]]=false;
							}
							break;
						default:break;
						}
						in.close();
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
					else if(str.charAt(0)=='*') {
						jp.gameState=1;
						try{sendexit();}catch(Exception e2) {}
					}
					else;
				}
				try {sleep(40);}catch(Exception e1) {}
			}
			while(jp.gameState!=0) {
				if(jp.gameState==2) {
					while(jp.gameState==2) System.out.println("stop");
				}
				else {
					try{str=brin.readLine();}catch(Exception e) {}//System.out.println(str);
					if(str.charAt(0)=='%') {
						if(jp.jn.getText().equalsIgnoreCase("下一关")) {
							jp.display();
							jp.repaint();
							try {sleep(3000);}catch(Exception e) {}
						}
						System.exit(0);
					}
					else if(str.charAt(0)=='&'){
						if(jp.jp2.getText().equalsIgnoreCase("玩家二：")) {
							jp.jp1.setText("玩家一：选择继续");
							jp.repaint();
							while(jp.gameState==1){ try{Thread.sleep(20);}catch(Exception e) {}}
							try {sendlayout();}catch(Exception e) {}
							while(jp.gameState!=0) {
								try {str=brin.readLine();} catch(Exception e) {}
								if(str.charAt(0)=='!') {
									jp.gameState=0;
									jp.start();
								}
							}
						}
						else {
							jp.remove(jp.gg);
							jp.remove(jp.je);
							jp.remove(jp.jp1);
							jp.remove(jp.jp2);
							jp.remove(jp.jn);
							jp.add(jp.jlExit);
							jp.add(jp.jlstop);
							if(jp.jn.getText().equals("重来")) { 
								jp.remove(jp.player);
								jp.remove(jp.team);
								for(int i=0;i<5;i++) {
									jp.remove(jp.jlscore[i]);
									jp.remove(jp.jlteam[i]);
								}
							}
							jp.restart();
							try {sendlayout();}catch(Exception e) {}
							while(jp.gameState!=0) {
								try {str=brin.readLine();} catch(Exception e) {}
								if(str.charAt(0)=='!') { 
									jp.gameState=0;
									jp.start();
								}
							}
						}
					}
					else;
				}
				try {sleep(30);}catch(Exception e1) {}
			}
		}	
	}
}
