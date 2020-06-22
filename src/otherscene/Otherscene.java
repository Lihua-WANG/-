package otherscene;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import scene.Record;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;


public class Otherscene extends JPanel implements ActionListener,KeyListener{
	public int iAim=0;
	public int iMoney=0;
	public int iTime=60;
	public int iCount=1;
	public int iMoney1=0;
	public int iMoney2=0;
	public int gameState=1;//0:玩游戏状态，1:不玩游戏状态
	public Integer Thcount=0;
	public File filesingle=new File("filesingle2.txt");
	public File fileteam=new File("fileteam.txt");
	public TreeSet<Record> treesingle=new TreeSet<Record>();
	public TreeSet<Record> treeteam=new TreeSet<Record>();
	public boolean flag=false;
	
	boolean start_flag=false;
	Thing2[] bonus;//至少14物品
	boolean[] is_exist;
	int amount;
	JButton start=new JButton("开始");
	JLabel jlAim=new JLabel("目标能量值: "+iAim);
	JLabel jlMoney=new JLabel("共获能量值: "+iMoney);
	JLabel jlTime=new JLabel("剩余时间: "+iTime);
	JLabel jlMoney_1=new JLabel(""+iMoney1);
	JLabel jlMoney_2=new JLabel(""+iMoney2);
	JLabel jlCount=new JLabel("第 "+iCount+" 关");
	JLabel player=new JLabel("玩家一历史前五得分：");
	JLabel team=new JLabel("团队历史前五得分：");
	JLabel[] jlscore=new JLabel[5];
	JLabel[] jlteam=new JLabel[5];
	JButton jlExit=new JButton("退出本关");
	JButton jlstop=new JButton("暂停");
	JLabel jstop=new JLabel("对方暂停");
	JLabel gg=new JLabel("很遗憾,您没有通过本关");
	JLabel jp2=new JLabel("玩家二");
	JLabel jp1=new JLabel("玩家一");
	JButton jn=new JButton("下一关");
	JButton je=new JButton("退出");
	
	Timer timer;
	Server server;
	Music music;
	int hook1flag=0;
	int hook2flag=0;
	Hook Hhook1;
	Hook Hhook2;
	
	int[] pigNum;
	int pigCount;
	Pig Tp;
	double v=1;

	Otherscene() throws Exception{
		JFrame myframe=new JFrame();
		myframe.setTitle("海底捞-server");
		myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myframe.setContentPane(this);
		this.setLayout(null);
		this.setOpaque(false);
		
		ImageIcon bg=new ImageIcon("主界面.PNG");
		JLabel label=new JLabel(bg);
		label.setBounds(-3,-3,bg.getIconWidth(),bg.getIconHeight());
		myframe.getLayeredPane().add(label,new Integer(-30001));
		myframe.setBounds(0, 0, bg.getIconWidth()+11, bg.getIconHeight()+30);
		
		jlAim.setBounds(5, 10, 100, 30);
		jlAim.setFont(new Font("幼圆",Font.BOLD,10));
		jlMoney.setBounds(5, 50, 100, 30);
		jlMoney.setFont(new Font("幼圆",Font.BOLD,10));
		jlTime.setBounds(120,10,100,30);
		jlTime.setFont(new Font("幼圆",Font.BOLD,15));
		jlCount.setBounds(120,50,100,30);
		jlCount.setFont(new Font("幼圆",Font.BOLD,15));
		jlExit.setBounds(600, 50,110,20);
		ImageIcon icon = new ImageIcon("退出本关.png");
		jlExit.setIcon(icon);
		jlExit.addActionListener(this);
		jlstop.setBounds(610, 20,110,20);
		ImageIcon icon1 = new ImageIcon("暂停游戏.png");
		jlstop.setIcon(icon1);
		jlstop.addActionListener(this);
		jstop.setBounds(610, 20,160,20);
		jstop.setFont(new Font("幼圆",Font.BOLD,15));
		jlMoney_1.setBounds(235,95,100,20);
		jlMoney_1.setFont(new Font("幼圆",Font.BOLD,30));
		jlMoney_2.setBounds(480,95,100,20);
		jlMoney_2.setFont(new Font("幼圆",Font.BOLD,30));
		jp1.setBounds(230,130,400,50);
		jp1.setFont(new Font("幼圆",Font.BOLD,30));
		jp2.setBounds(470,130,400,50);
		jp2.setFont(new Font("幼圆",Font.BOLD,30));
		jn.setBounds(210,200,120,50);
		ImageIcon icon2 = new ImageIcon("下一关.png");
		jn.setIcon(icon2);
		jn.addActionListener(this);
		je.setBounds(450,200,120,50);
		ImageIcon icon3 = new ImageIcon("退出.png");
		je.setIcon(icon3);
		je.addActionListener(this);
		gg.setBounds(100,250,800,100);
		gg.setFont(new Font("幼圆",Font.BOLD,50));
		player.setBounds(50, 230, 300, 300);
		player.setFont(new Font("幼圆",Font.BOLD,10));
		team.setBounds(400, 230, 300, 300);
		team.setFont(new Font("幼圆",Font.BOLD,10));
		start.setBounds(-3,-3,bg.getIconWidth(),bg.getIconHeight());
		ImageIcon icon4 = new ImageIcon("开始.png");
		start.setIcon(icon4);
		this.add(start);
		start.addActionListener(this);
		for(int i=0;i<5;i++) {
			jlscore[i]=new JLabel();
			jlteam[i]=new JLabel();
			jlscore[i].setBounds(50, 250+i*15, 300, 300);
			jlteam[i].setBounds(400, 250+i*15, 300, 300);
		}
		this.addKeyListener(this);
		this.add(jlAim);
		this.add(jlMoney);
		this.add(jlTime);
		this.add(jlCount);
		this.add(jlExit);
		this.add(jlstop);
		this.add(jlMoney_1);
		this.add(jlMoney_2);
		
		read();
		myframe.setVisible(true);
		while(!start_flag) System.out.println("&&&&&");
	    
		server=new Server(this);//在界面中导入服务器对象
		server.start();
		this.start();
		music=new Music();//在界面中导入音乐播放器对象
	    music.play(this);
		
		timer=new Timer();
		timer.schedule(new TimerTask() {//计时器，每次倒数一秒都会重新导入界面显示
			int a=-1;
			public void run() {
				if(gameState==0) {
					try{server.sendhook();}catch(Exception e) {}
					a++;
					if(a==4) {
						a=0;
						iTime--;
					    if(iTime==-1) { gameState=1;try{server.sendexit();}catch(Exception e) {}}
					    if(gameState==0) jlTime.setText("剩余时间: "+iTime);
					}
				}
			}
		},0,250);
		
		while(true) {
			if(gameState==0) {
				this.requestFocus();
				repaint();
			}
			else if(gameState==1){
				music.stop();
				clear();
				if(iAim<=iMoney) { 
					gg.setText("恭喜您，您通过了本关");
					jn.setText("下一关");
					ImageIcon i2 = new ImageIcon("下一关.png");
					jn.setIcon(i2);
					iCount++;
				}
				else { 
					gg.setText("很遗憾,您没有通过本关");
					setrecord();
					display();
					jn.setText("重来");
					ImageIcon i2 = new ImageIcon("重来.png");
					jn.setIcon(i2);
					iCount=1;
					iMoney=iMoney1=iMoney2=0;
					iAim=0;
				}
				this.remove(jlExit);
				this.remove(jlstop);
				this.add(gg);
				this.add(jp1);
				this.add(jp2);
				this.add(je);
				this.add(jn);
				music.chainstop();
				repaint();
				while(gameState!=0){ try{Thread.sleep(20);}catch(Exception e) {}}
				this.start();
				music.start();
			}
			else music.chainstop();
			try {Thread.sleep(5);}catch(Exception e) {};
		}
	}
	
	public void paintComponent(Graphics g) {//画筷子和线
		if(gameState==0 && hook1flag==1 && hook2flag==1) {
			g.drawLine(Hhook1.hookPos[0], Hhook1.hookPos[1], Hhook1.hookPos[2]+10, Hhook1.hookPos[3]+2);
			g.drawLine(Hhook2.hookPos[0], Hhook2.hookPos[1], Hhook2.hookPos[2]+10, Hhook2.hookPos[3]+2);
		}
	}
	
	public void start() {//开始游戏时导入筷子和辣椒
		gameState=0;
		Tp=new Pig(this);
		Tp.start();
		Hhook1=new Hook(1,this);
		Hhook2=new Hook(2,this);
		Hhook1.start();
		Hhook2.start();
	}
	
	public void clear(){
		while(Thcount>0){
			try {Thread.sleep(100);}catch(Exception e) {};
		}
		for(int i=0;i<amount;i++) {
			if(is_exist[i]) remove(bonus[i].a);
		}
		remove(Hhook1.jlHook);
		remove(Hhook2.jlHook);
		if(Hhook1.hookPos[4]!=-1) remove(bonus[Hhook1.hookPos[4]].a);
		if(Hhook2.hookPos[4]!=-1) remove(bonus[Hhook2.hookPos[4]].a);
		this.repaint();
	}
	
	public void restart() {//再次开始（重来或下一关）重新导入界面
		iTime=60;
		jlMoney.setText("共获能量值: "+iMoney);
		jlTime.setText("剩余时间: "+iTime);
		jlMoney_1.setText(""+iMoney1);
		jlMoney_2.setText(""+iMoney2);
		jlCount.setText("第 "+iCount+" 关");
		jp1.setText("玩家一");
		jp2.setText("玩家二");
		repaint();
	}
	
	public void read() {//把游戏记录写入文本文件
		String str="";
		try {
			Scanner in=new Scanner(filesingle);
			while(in.hasNext()) {
				str=in.nextLine();
				treesingle.add(new Record(str));
			}
			in.close();
		}
		catch(Exception e) {}
		try {
			Scanner in=new Scanner(fileteam);
			while(in.hasNext()) {
				str=in.nextLine();
				treeteam.add(new Record(str));
			}
			in.close();
		}
		catch(Exception e) {}
	}
	
	public void setrecord() {//读出游戏记录
		Record single=new Record(iMoney1);
		Record team=new Record(iMoney);
		treesingle.add(single);
		treeteam.add(team);
		String str="";
		try {
			PrintStream out=new PrintStream(filesingle);
			for(Record word:treesingle)
				str+=word.getstring()+'\n';
			out.print(str);
			out.close();
		}
		catch(Exception e) {}
	}
	
	public void display() {//在界面上显示出单人玩家和团队分别的前五名的游戏记录
		this.add(player);
		this.add(team);
		String str="";
		int i=0;
		for(Record word:treesingle) {
			jlscore[i].setText(word.other);
			this.add(jlscore[i]);
			i++;
			if(i==5) break;
		}
		str="";
		while(i<5) {
			jlscore[i].setText("");
			this.add(jlscore[i]);
			i++;
		}
		i=0;
		for(Record word:treeteam) {
			jlteam[i].setText(word.other);
			this.add(jlteam[i]);
			i++;
			if(i==5) break;
		}
		while(i<5) {
			jlteam[i].setText("");
			this.add(jlteam[i]);
			i++;
		}
	}
	
	public static void main(String[] argvs){
		try{Otherscene jpScene=new Otherscene();}catch(Exception e) {}
	}

	@Override
	public void actionPerformed(ActionEvent e) {//点击不同按钮所产生的不同监听器事件
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("退出本关")) {
			gameState=1;
			try{server.sendexit();}catch(Exception e1) {}
		}
		else if(e.getActionCommand().equals("退出")) {
			try{server.sendchoice(true);}catch(Exception w) {};
			if(jn.getText().equalsIgnoreCase("下一关")) {
				System.out.println("9898");
				display();
				this.repaint();
				Timer time=new Timer();
				time.schedule(new TimerTask() {
					int a=0;
					public void run() {
						a++;
						if(a==4) {
							System.exit(0);
						}
					}
				},0,1000);
			}
			else System.exit(0);
		}
		else if(e.getActionCommand().equalsIgnoreCase("暂停")) {
			gameState=2;
			try{server.sendstop();}catch(Exception e2) {}
			jlstop.setText("继续");
			ImageIcon i1 = new ImageIcon("继续游戏.png");
			jlstop.setIcon(i1);
		}
		else if(e.getActionCommand().equals("继续")) {
			try{server.sendcontinue();}catch(Exception e2) {}
			jlstop.setText("暂停");
			ImageIcon i1 = new ImageIcon("暂停游戏.png");
			jlstop.setIcon(i1);
			gameState=0;
		}
		else if(e.getActionCommand().equals("下一关") || e.getActionCommand().equals("重来")) {
			if(jp2.getText().equalsIgnoreCase("玩家二：选择继续")) { 
				try{server.sendchoice(false);}catch(Exception w) {};
				this.remove(gg);
				this.remove(je);
				this.remove(jp1);
				this.remove(jp2);
				this.remove(jn);
				this.add(jlExit);
				this.add(jlstop);
				if(e.getActionCommand().equals("重来")) { 
					remove(player);
					remove(team);
					for(int i=0;i<5;i++) {
						remove(jlscore[i]);
						remove(jlteam[i]);
					}
				}
				restart();
				gameState=2;
			}
			else {
				try{server.sendchoice(false);}catch(Exception w) {};
				jp1.setText("玩家一：选择继续");
				jp1.setBounds(180,130,400,50);
			}
		}
		else if(e.getActionCommand().equals("开始")) {
			start_flag=true;
			this.remove(start);
		}
		else;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_DOWN && Hhook1.state_hook==0) {
			music.playonce();
			Hhook1.state_hook=1;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
}
