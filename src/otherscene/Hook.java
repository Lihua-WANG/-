package otherscene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Hook extends Thread{
	double radius;
	int name;
	Otherscene jp;
	Rectangle rHook;
	public int[] hookPos=new int[5];//0 1是开始点的坐标，2 3是结束点，为线程所共享，4是被勾上的物体的编号
	int state_hook=0;//0:空转 1:下夹 2:上夹
	JLabel jlHook=new JLabel(new ImageIcon("筷子.PNG"));
	//运动参量
	int v;//速度
	double zeta=0;//角度
	double w=0;
	int is_clock=1;//时间
	float x=0;//横纵坐标
	float y=0;
	boolean a=true;//筷子没在用
	
	public Hook(int a,Otherscene k){
		jp=k;
		name=a;
		radius=40;
		if(name==1) {//玩家一的筷子位置初始化
			hookPos[0]=300;
			hookPos[1]=100;
			hookPos[2]=310;
			hookPos[3]=100;
			jp.hook1flag=1;
		}
		if(name==2) {//玩家二的筷子位置初始化
			hookPos[0]=500;
			hookPos[1]=100;
			hookPos[2]=510;
			hookPos[3]=100;
			jp.hook2flag=1;
		}
	    hookPos[4]=-1;
	    rHook=new Rectangle(hookPos[2],hookPos[3],20,20);//将筷子结束的位置用矩形框起来
	    jlHook.setBounds(hookPos[2], hookPos[3], 20,20);
	    jp.add(jlHook);
	    jp.Thcount++;
	}
	
	public void run() {
		while(jp.gameState!=1) {//正在玩游戏
			if(jp.gameState==0) {
				switch(state_hook) {
				case 0://筷子空悬，按照半径为40的圆考虑实际重力情况进行180度旋转
					w+=is_clock*(9.8*Math.cos(zeta)/radius);
					zeta+=w*0.2;
					hookPos[2]=(int)(Math.cos(zeta)*radius)+hookPos[0];
					hookPos[3]=(int)(Math.sin(zeta)*radius)+hookPos[1];
					break;
				case 1://筷子往下夹东西
					if(a) {
						v=6;//筷子运行速度
						x=hookPos[2];
						y=hookPos[3];
						a=false;
					}
					for(int i=0;i<jp.amount;i++) {
						rHook.x=hookPos[2];//矩形的横纵坐标
						rHook.y=hookPos[3];
						if(jp.is_exist[i]==true && rHook.intersects(jp.bonus[i].myrec)){//碰撞检测，必须让筷子结束位置的矩形和想要夹的东西位置的矩形线程相交，这样才能得到物品
							hookPos[4]=i;//夹到物品的编号赋到对应筷子的数组里
							state_hook=2;//筷子应该夹着东西往上走了
							v=jp.bonus[i].velocity;
							jp.is_exist[i]=false;//刚刚被夹走物品的位置空了
						}
						if(hookPos[2]<=10 || hookPos[2]>=710 || hookPos[3]<=50 || hookPos[3]>=520) {
							state_hook=2;
	                        v=7;
						}
					}
					x+=Math.cos(zeta)*v;//算出筷子下降的目的物品位置
					y+=Math.sin(zeta)*v;
					hookPos[2]=(int)x;
					hookPos[3]=(int)y;
					break;
				case 2:
					x-=Math.cos(zeta)*v;//算出筷子上升至的原始位置
					y-=Math.sin(zeta)*v;
					hookPos[2]=(int)x;
					hookPos[3]=(int)y;
					if(hookPos[4]!=-1) {//筷子上夹着物品，物品移动结束的横纵坐标设为筷子的初始位置
						jp.bonus[hookPos[4]].x=hookPos[2];
						jp.bonus[hookPos[4]].y=hookPos[3];
						jp.bonus[hookPos[4]].moveJLabel();
						if(Math.sqrt((hookPos[2]-hookPos[0])*(hookPos[2]-hookPos[0])+(hookPos[3]-hookPos[1])*(hookPos[3]-hookPos[1]))<=radius) {
							state_hook=0;//上升到人所在位置，结束上升状态，成为空悬状态
							jp.iMoney+=jp.bonus[hookPos[4]].value;//算对应物品的价值并赋给夹这个物品上来的用户
							jp.jlMoney.setText("共获能量值: "+jp.iMoney);
							if(name==1) {
								jp.iMoney1+=jp.bonus[hookPos[4]].value;
								jp.jlMoney_1.setText(""+jp.iMoney1);
							}
							else {
								jp.iMoney2+=jp.bonus[hookPos[4]].value;
								jp.jlMoney_2.setText(""+jp.iMoney2);
							}
							jp.remove(jp.bonus[hookPos[4]].a);//把夹上来的物品设为不存在，因为已经转换为相应能量值
							jp.is_exist[hookPos[4]]=false;
							hookPos[4]=-1;
							v=6;
							a=true;
							if(name==1) 
								jp.music.chainstop();
						}
					}
					else {
						if(Math.sqrt((hookPos[2]-hookPos[0])*(hookPos[2]-hookPos[0])+(hookPos[3]-hookPos[1])*(hookPos[3]-hookPos[1]))<=radius) {
							state_hook=0;
							v=6;
							a=true;
							if(name==1) 
								jp.music.chainstop();
						}
					}
					break;
				default:break;
				}
				if(jp.gameState==0) {//不玩游戏，界面消失
					this.moveJLabel();
				}
			}
			try {sleep(50);}catch(Exception e) {}//每50毫秒让server和client同步数据，之前都是按照自己线程的进度，但同步后要对应另一个玩家相应改变自己
	    }
		synchronized(jp.Thcount) {//哪个用户的筷子在用就设置标志位，但不可能同时竞争，因为线程同步
			if(name==1)
				jp.hook1flag=0;
			else 
				jp.hook2flag=0;
			jp.Thcount--;
		}
	}
	
	public void moveJLabel() {
		jlHook.setBounds(hookPos[2], hookPos[3], 20,20);
	}
}


