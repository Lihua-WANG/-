package otherscene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Thing2 {
	public int x;
	public int y;
	public ImageIcon image;
	public int value;
	public String type;
	public int velocity;
	public boolean is_get=false;
	public Rectangle myrec;
	public JLabel a;
	public Otherscene jp;
	Thing2(Otherscene k,String a,int k1,int k2){
		type=a;
		x=k1;
		y=k2;
		jp=k;
		switch(type) {//每个物品的能量值、上升速度、图像导入和碰撞矩形
		case "goldsmall":
			value=100;
			velocity=6;
			image=new ImageIcon("香菇.PNG");
			myrec=new Rectangle(x,y,image.getIconWidth(),image.getIconHeight());
			break;
		case "stone":
			value=20;
			velocity=1;
			image=new ImageIcon("大白菜.PNG");
			myrec=new Rectangle(x+1,y+1,image.getIconWidth()-1,image.getIconHeight()-1);
			break;
		case "goldmedium":
			value=200;
			velocity=4;
			image=new ImageIcon("虾.PNG");
			myrec=new Rectangle(x,y,image.getIconWidth(),image.getIconHeight());
			break;
		case "goldlarge":
			value=500;
			velocity=2;
			image=new ImageIcon("羊肉.PNG");
			myrec=new Rectangle(x+1,y+1,image.getIconWidth()-1,image.getIconHeight()-1);
			break;
		case "pocket":
			value=(int)(Math.random()*1001);
			velocity=5;
			image=new ImageIcon("口袋.PNG");
			myrec=new Rectangle(x+1,y,image.getIconWidth()-1,image.getIconHeight());
			break;
		case "pig":
			value=5;
			velocity=4;
			image=new ImageIcon("八爪鱼.PNG");
			myrec=new Rectangle(x+1,y+1,image.getIconWidth()-1,image.getIconHeight()-1);
			break;
		default:break;
		}
	}
	public boolean is_attack(Thing2 a) {//看初始化所有物品时存不存在碰撞，如果有两个及以上重合的要重新排列
		return myrec.intersects(a.myrec);
	}
	public JLabel getLabel() {//初始化物品放在界面布局上
		a=new JLabel(image);
		a.setBounds(x, y, image.getIconWidth(), image.getIconHeight());
		jp.add(a);
		return a;
	}
	public void moveJLabel() {//移动物品时图像的坐标跟着移动
		a.setBounds(x, y, image.getIconWidth(), image.getIconHeight());
	}
	public void moverec() {//移动物品时被包含的碰撞框坐标也跟着移动
		myrec.setBounds(x, y, image.getIconWidth(), image.getIconHeight());
	}
}

