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
		direction=new int[jp.pigCount];//��ʼ�����������ƶ�����Ϊ������
		for(int i=0;i<jp.pigCount;i++) {
			direction[i]=1;
		}
		while(jp.gameState!=1) {
			if(jp.gameState==0) {//������Ϸ״̬
				for(int i=0;i<jp.pigCount;i++) {
					jp.bonus[jp.pigNum[i]].x+=direction[i]*jp.v;//����ÿ�ΰ��չ̶����ٶ������ƶ�
					if(jp.bonus[jp.pigNum[i]].x<=2 || jp.bonus[jp.pigNum[i]].x>=700) {//һ�����������Ե���������ն������ٶ��ƶ�
						direction[i]=-direction[i];
						jp.bonus[jp.pigNum[i]].x+=2*direction[i]*jp.v;
					}
					for(int j=0;j<jp.amount;j++) {
						if( j!=jp.pigNum[i] && jp.is_exist[j]==true) {
							if(jp.gameState!=0) break;
							if(jp.bonus[jp.pigNum[i]].is_attack(jp.bonus[j])) {//һ���ƶ�����������������Ʒ���ͻᷴ���򰴶����ٶ��ƶ�
								direction[i]=-direction[i];
								jp.bonus[jp.pigNum[i]].x+=2*direction[i]*jp.v;
								break;
							}
						}	
					}
					if(jp.gameState==0) {//��Ʒ�ƶ���ʱ��������ǵ�ͼ��Ͱ������ǵ���ײ��Ҳһ���ƶ�
						jp.bonus[jp.pigNum[i]].moveJLabel();
						jp.bonus[jp.pigNum[i]].moverec();
					}
				}
			}
			if(jp.gameState!=1) {//ÿ��60�������һ��ѭ����Ҳ����ÿ60�����ƶ�һ����������
				try {sleep(60);}catch(Exception e) {}
			}
		}
		synchronized(jp.Thcount) {
			jp.Thcount--;
		}
	}
}

