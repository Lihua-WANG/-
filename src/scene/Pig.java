package scene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Pig extends Thread{
	Scene jp;
	public int[] direction;
	public Pig(Scene j) {
		jp=j;
		jp.Thcount++;
	}
	public void run() {
		direction=new int[jp.pigCount];
		for(int i=0;i<jp.pigCount;i++) {
			direction[i]=1;
		}
		while(jp.gameState!=1) {
			if(jp.gameState==0) {
				for(int i=0;i<jp.pigCount;i++) {
					jp.bonus[jp.pigNum[i]].x+=direction[i]*jp.v;
					if(jp.bonus[jp.pigNum[i]].x<=2 || jp.bonus[jp.pigNum[i]].x>=700) {
						direction[i]=-direction[i];
						jp.bonus[jp.pigNum[i]].x+=2*direction[i]*jp.v;
					}
					for(int j=0;j<jp.amount;j++) {
						if( j!=jp.pigNum[i] && jp.is_exist[j]==true) {
							if(jp.gameState!=0) break;
							if(jp.bonus[jp.pigNum[i]].is_attack(jp.bonus[j])) {
								direction[i]=-direction[i];
								jp.bonus[jp.pigNum[i]].x+=2*direction[i]*jp.v;
								break;
							}
						}	
					}
					if(jp.gameState==0) {
						jp.bonus[jp.pigNum[i]].moveJLabel();
						jp.bonus[jp.pigNum[i]].moverec();
					}
				}
			}
			if(jp.gameState!=1) {
				try {sleep(60);}catch(Exception e) {}
			}
		}
		synchronized(jp.Thcount) {
			jp.Thcount--;
		}
	}
}
