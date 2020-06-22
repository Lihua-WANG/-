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
		welcome=new ServerSocket(6789);//用需要使用的端口号作为参数来创建服务器对象
		socketServer=welcome.accept();// 监听,等待客户端连接端口
		brin=new BufferedReader(new InputStreamReader(socketServer.getInputStream()));//获取客户端传来的字节流存到缓冲区
		brout=new DataOutputStream(socketServer.getOutputStream());//为套接字返回一个数据输出流
		jp=j;
		String str="";
		produce(str);
	}
	
	public void sendhook() throws Exception{
		String str="";
		switch(jp.Hhook1.state_hook) {
		case 0://筷子正在空悬
			str="# "+jp.Hhook1.state_hook+" "+jp.Hhook1.x+" "+jp.Hhook1.y+" "+jp.Hhook1.zeta+" "
		            +jp.Hhook1.is_clock+" "+jp.Hhook1.w+'\n';
			brout.writeBytes(str);
			break;
		case 1://筷子往下夹东西
			str="# "+jp.Hhook1.state_hook+" "+jp.Hhook1.x+" "+jp.Hhook1.y+" "+jp.Hhook1.zeta+" "
					+jp.Hhook1.v+'\n';
			brout.writeBytes(str);
			break;
		case 2://筷子往上挑东西
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
			brout.writeBytes("%"+'\n');//代表退出
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
		jp.amount=Integer.parseInt(str);//从客户端接收一共产生多少个随机物品的数据流
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
		jp.jlAim.setText("目标能量值: "+jp.iAim);
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
						if(jp.Hhook2.state_hook!=0) { //服务端筷子不空闲
							if(jp.Hhook2.hookPos[4]!=-1) {//服务端筷子上夹着物品
								jp.Hhook2.state_hook=0;
								jp.iMoney+=jp.bonus[jp.Hhook2.hookPos[4]].value;
								jp.jlMoney.setText("共获能量值: "+jp.iMoney);
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
					System.out.println("我收到了退出请求");
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
						if(jp.jn.getText().equalsIgnoreCase("下一关")) {
							jp.display();
							jp.repaint();
							try {sleep(3000);}catch(Exception e) {}
						}
						System.exit(0);
					}
					else if(str.charAt(0)=='&'){
						if(jp.jp1.getText().equalsIgnoreCase("玩家一")) {
							jp.jp2.setText("玩家二：选择继续");
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
							if(jp.jn.getText().equals("重来")) { 
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
