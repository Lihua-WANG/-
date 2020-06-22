package scene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Hook extends Thread{
	double radius;
	int name;
	Scene jp;
	Rectangle rHook;
	public int[] hookPos=new int[5];//0 1是开始点的坐标xy，2 3是结束点xy，为线程所共享，4是被勾上的物体的编号
	int state_hook=0;//0:空转 1:下钩 2:上勾
	JLabel jlHook=new JLabel(new ImageIcon("筷子.PNG"));
	//运动参量
	int v;
	double zeta=0;
	double w=0;
	int is_clock=1;
	float x=0;
	float y=0;
	boolean a=true;
		
	public Hook(int a,Scene k){
		jp=k;
		name=a;
		radius=40;
		if(name==1) {
			hookPos[0]=300;
			hookPos[1]=100;
			hookPos[2]=310;
			hookPos[3]=100;
			jp.hook1flag=1;
		}
		if(name==2) {
			hookPos[0]=500;
			hookPos[1]=100;
			hookPos[2]=510;
			hookPos[3]=100;
			jp.hook2flag=1;
		}
	    hookPos[4]=-1;
	    rHook=new Rectangle(hookPos[2],hookPos[3],20,20);
	    jlHook.setBounds(hookPos[2], hookPos[3], 20,20);
	    jp.add(jlHook);
	    jp.Thcount++;
	}
	
	public void run() {
		while(jp.gameState!=1) {
			if(jp.gameState==0) {
				switch(state_hook) {
				case 0:
					w+=is_clock*(9.8*Math.cos(zeta)/radius);
					zeta+=w*0.2;
					hookPos[2]=(int)(Math.cos(zeta)*radius)+hookPos[0];
					hookPos[3]=(int)(Math.sin(zeta)*radius)+hookPos[1];
					//System.out.println(zeta);
					break;
				case 1:
					v=6;
					if(a) {
						x=hookPos[2];
						y=hookPos[3];
						a=false;
					}
					for(int i=0;i<jp.amount;i++) {
						rHook.x=hookPos[2];
						rHook.y=hookPos[3];
						if(jp.is_exist[i]==true && rHook.intersects(jp.bonus[i].myrec)) {
							hookPos[4]=i;
							state_hook=2;
							v=jp.bonus[i].velocity;
							jp.is_exist[i]=false;
						}
						if(hookPos[2]<=10 || hookPos[2]>=710 || hookPos[3]<=50 || hookPos[3]>=520) {
							state_hook=2;
	                        v=7;
						}
					}
					x+=Math.cos(zeta)*v;
					y+=Math.sin(zeta)*v;
					hookPos[2]=(int)x;
					hookPos[3]=(int)y;
					break;
				case 2:
					x-=Math.cos(zeta)*v;
					y-=Math.sin(zeta)*v;
					hookPos[2]=(int)x;
					hookPos[3]=(int)y;
					if(hookPos[4]!=-1) {
						jp.bonus[hookPos[4]].x=hookPos[2];//Math.cos(zeta)*v;
						jp.bonus[hookPos[4]].y=hookPos[3];//Math.sin(zeta)*v;
						jp.bonus[hookPos[4]].moveJLabel();
						if(Math.sqrt((hookPos[2]-hookPos[0])*(hookPos[2]-hookPos[0])+(hookPos[3]-hookPos[1])*(hookPos[3]-hookPos[1]))<=radius) {
							state_hook=0;
							jp.iMoney+=jp.bonus[hookPos[4]].value;
							jp.jlMoney.setText("共获金钱: "+jp.iMoney);
							if(name==1) {
								jp.iMoney1+=jp.bonus[hookPos[4]].value;
								jp.jlMoney_1.setText("$"+jp.iMoney1);
							}
							else {
								jp.iMoney2+=jp.bonus[hookPos[4]].value;
								jp.jlMoney_2.setText("$"+jp.iMoney2);
							}
							jp.remove(jp.bonus[hookPos[4]].a);
							jp.is_exist[hookPos[4]]=false;
							hookPos[4]=-1;
							v=6;
							a=true;
							if(name==2) jp.music.chainstop();
						}
					}
					else {
						if(Math.sqrt((hookPos[2]-hookPos[0])*(hookPos[2]-hookPos[0])+(hookPos[3]-hookPos[1])*(hookPos[3]-hookPos[1]))<=radius) {
							state_hook=0;
							v=6;
							a=true;
							if(name==2) jp.music.chainstop();
						}
					}
					break;
				default:break;
				}
				if(jp.gameState==0) {
					this.moveJLabel();
				}
			}
			try {sleep(50);}catch(Exception e) {}
	    }
		synchronized(jp.Thcount) {
			if(name==1)jp.hook1flag=0;
			else jp.hook2flag=0;
			jp.Thcount--;
		}
	}
	
	public void moveJLabel() {
		jlHook.setBounds(hookPos[2], hookPos[3], 20,20);
	}
}

