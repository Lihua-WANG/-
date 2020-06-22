package otherscene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Hook extends Thread{
	double radius;
	int name;
	Otherscene jp;
	Rectangle rHook;
	public int[] hookPos=new int[5];//0 1�ǿ�ʼ������꣬2 3�ǽ����㣬Ϊ�߳�������4�Ǳ����ϵ�����ı��
	int state_hook=0;//0:��ת 1:�¼� 2:�ϼ�
	JLabel jlHook=new JLabel(new ImageIcon("����.PNG"));
	//�˶�����
	int v;//�ٶ�
	double zeta=0;//�Ƕ�
	double w=0;
	int is_clock=1;//ʱ��
	float x=0;//��������
	float y=0;
	boolean a=true;//����û����
	
	public Hook(int a,Otherscene k){
		jp=k;
		name=a;
		radius=40;
		if(name==1) {//���һ�Ŀ���λ�ó�ʼ��
			hookPos[0]=300;
			hookPos[1]=100;
			hookPos[2]=310;
			hookPos[3]=100;
			jp.hook1flag=1;
		}
		if(name==2) {//��Ҷ��Ŀ���λ�ó�ʼ��
			hookPos[0]=500;
			hookPos[1]=100;
			hookPos[2]=510;
			hookPos[3]=100;
			jp.hook2flag=1;
		}
	    hookPos[4]=-1;
	    rHook=new Rectangle(hookPos[2],hookPos[3],20,20);//�����ӽ�����λ���þ��ο�����
	    jlHook.setBounds(hookPos[2], hookPos[3], 20,20);
	    jp.add(jlHook);
	    jp.Thcount++;
	}
	
	public void run() {
		while(jp.gameState!=1) {//��������Ϸ
			if(jp.gameState==0) {
				switch(state_hook) {
				case 0://���ӿ��������հ뾶Ϊ40��Բ����ʵ�������������180����ת
					w+=is_clock*(9.8*Math.cos(zeta)/radius);
					zeta+=w*0.2;
					hookPos[2]=(int)(Math.cos(zeta)*radius)+hookPos[0];
					hookPos[3]=(int)(Math.sin(zeta)*radius)+hookPos[1];
					break;
				case 1://�������¼ж���
					if(a) {
						v=6;//���������ٶ�
						x=hookPos[2];
						y=hookPos[3];
						a=false;
					}
					for(int i=0;i<jp.amount;i++) {
						rHook.x=hookPos[2];//���εĺ�������
						rHook.y=hookPos[3];
						if(jp.is_exist[i]==true && rHook.intersects(jp.bonus[i].myrec)){//��ײ��⣬�����ÿ��ӽ���λ�õľ��κ���Ҫ�еĶ���λ�õľ����߳��ཻ���������ܵõ���Ʒ
							hookPos[4]=i;//�е���Ʒ�ı�Ÿ�����Ӧ���ӵ�������
							state_hook=2;//����Ӧ�ü��Ŷ�����������
							v=jp.bonus[i].velocity;
							jp.is_exist[i]=false;//�ոձ�������Ʒ��λ�ÿ���
						}
						if(hookPos[2]<=10 || hookPos[2]>=710 || hookPos[3]<=50 || hookPos[3]>=520) {
							state_hook=2;
	                        v=7;
						}
					}
					x+=Math.cos(zeta)*v;//��������½���Ŀ����Ʒλ��
					y+=Math.sin(zeta)*v;
					hookPos[2]=(int)x;
					hookPos[3]=(int)y;
					break;
				case 2:
					x-=Math.cos(zeta)*v;//���������������ԭʼλ��
					y-=Math.sin(zeta)*v;
					hookPos[2]=(int)x;
					hookPos[3]=(int)y;
					if(hookPos[4]!=-1) {//�����ϼ�����Ʒ����Ʒ�ƶ������ĺ���������Ϊ���ӵĳ�ʼλ��
						jp.bonus[hookPos[4]].x=hookPos[2];
						jp.bonus[hookPos[4]].y=hookPos[3];
						jp.bonus[hookPos[4]].moveJLabel();
						if(Math.sqrt((hookPos[2]-hookPos[0])*(hookPos[2]-hookPos[0])+(hookPos[3]-hookPos[1])*(hookPos[3]-hookPos[1]))<=radius) {
							state_hook=0;//������������λ�ã���������״̬����Ϊ����״̬
							jp.iMoney+=jp.bonus[hookPos[4]].value;//���Ӧ��Ʒ�ļ�ֵ�������������Ʒ�������û�
							jp.jlMoney.setText("��������ֵ: "+jp.iMoney);
							if(name==1) {
								jp.iMoney1+=jp.bonus[hookPos[4]].value;
								jp.jlMoney_1.setText(""+jp.iMoney1);
							}
							else {
								jp.iMoney2+=jp.bonus[hookPos[4]].value;
								jp.jlMoney_2.setText(""+jp.iMoney2);
							}
							jp.remove(jp.bonus[hookPos[4]].a);//�Ѽ���������Ʒ��Ϊ�����ڣ���Ϊ�Ѿ�ת��Ϊ��Ӧ����ֵ
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
				if(jp.gameState==0) {//������Ϸ��������ʧ
					this.moveJLabel();
				}
			}
			try {sleep(50);}catch(Exception e) {}//ÿ50������server��clientͬ�����ݣ�֮ǰ���ǰ����Լ��̵߳Ľ��ȣ���ͬ����Ҫ��Ӧ��һ�������Ӧ�ı��Լ�
	    }
		synchronized(jp.Thcount) {//�ĸ��û��Ŀ������þ����ñ�־λ����������ͬʱ��������Ϊ�߳�ͬ��
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


