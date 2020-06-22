package otherscene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Pig extends Thread{
	Otherscene jp;
	int[] direction;
	public Pig(Otherscene j) {
		jp=j;
		jp.Thcount++;
	}
	public void run() {
		direction=new int[jp.pigCount];//初始化所有辣椒移动方向为正方向
		for(int i=0;i<jp.pigCount;i++) {
			direction[i]=1;
		}
		while(jp.gameState!=1) {
			if(jp.gameState==0) {//正在游戏状态
				for(int i=0;i<jp.pigCount;i++) {
					jp.bonus[jp.pigNum[i]].x+=direction[i]*jp.v;//辣椒每次按照固定的速度向左移动
					if(jp.bonus[jp.pigNum[i]].x<=2 || jp.bonus[jp.pigNum[i]].x>=700) {//一旦碰到界面边缘，反方向按照二倍的速度移动
						direction[i]=-direction[i];
						jp.bonus[jp.pigNum[i]].x+=2*direction[i]*jp.v;
					}
					for(int j=0;j<jp.amount;j++) {
						if( j!=jp.pigNum[i] && jp.is_exist[j]==true) {
							if(jp.gameState!=0) break;
							if(jp.bonus[jp.pigNum[i]].is_attack(jp.bonus[j])) {//一旦移动的辣椒碰到其他物品，就会反方向按二倍速度移动
								direction[i]=-direction[i];
								jp.bonus[jp.pigNum[i]].x+=2*direction[i]*jp.v;
								break;
							}
						}	
					}
					if(jp.gameState==0) {//物品移动的时候代表它们的图像和包含它们的碰撞框也一起移动
						jp.bonus[jp.pigNum[i]].moveJLabel();
						jp.bonus[jp.pigNum[i]].moverec();
					}
				}
			}
			if(jp.gameState!=1) {//每隔60毫秒进行一次循环，也就是每60毫秒移动一次所有辣椒
				try {sleep(60);}catch(Exception e) {}
			}
		}
		synchronized(jp.Thcount) {
			jp.Thcount--;
		}
	}
}

