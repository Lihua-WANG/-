package scene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import java.io.*;
import java.util.*;

public class Scene extends JPanel implements ActionListener,KeyListener{
	public int iAim=0;
	public int iMoney=0;
	public int iTime=60;
	public int iCount=1;
	public int iMoney1=0;
	public int iMoney2=0;
	public int gameState=1;//0:玩游戏状态 1:不玩游戏状态
	public Integer Thcount=0;
	public File filesingle=new File("filesingle.txt");
	public File fileteam=new File("fileteam.txt");
	public TreeSet<Record> treesingle=new TreeSet<Record>();
	public TreeSet<Record> treeteam=new TreeSet<Record>();
	public boolean flag=false;
	
	boolean start_flag=false;
	Thing[] bonus;//至少14: 5 个黄金，4 个石头，3 个干扰物和 2 个”?”口袋
	boolean[] is_exist;
	int amount;
	JButton start=new JButton("开始");
	JLabel jlAim=new JLabel("目标钱数: "+iAim);
	JLabel jlMoney=new JLabel("共获金钱: "+iMoney);
	JLabel jlTime=new JLabel("剩余时间: "+iTime);
	JLabel jlMoney_1=new JLabel("$"+iMoney1);
	JLabel jlMoney_2=new JLabel("$"+iMoney2);
	JLabel jlCount=new JLabel("第 "+iCount+" 关");
	JLabel player=new JLabel("玩家二历史前五得分：");
	JLabel team=new JLabel("团队历史前五得分：");
	JLabel[] jlscore=new JLabel[5];
	JLabel[] jlteam=new JLabel[5];
	JButton jlExit=new JButton("退出本关");
	JButton jlstop=new JButton("暂停");
	JLabel jstop=new JLabel("对方暂停");
	JLabel gg=new JLabel("很遗憾,您没有通过本关");
	JLabel jp2=new JLabel("玩家二：");
	JLabel jp1=new JLabel("玩家一：");
	JButton jn=new JButton("下一关");
	JButton je=new JButton("退出");
	
	Timer timer;
	Client client;
	Music music;
	int hook2flag=0;
	int hook1flag=0;
	Hook Hhook1;
	Hook Hhook2;
	
	int[] pigNum;
	int pigCount;
	Pig Tp;
	double v=1;

	Scene() throws Exception{
		JFrame myframe=new JFrame();
		myframe.setTitle("黄金矿工-client");
		myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myframe.setContentPane(this);
		this.setLayout(null);
		this.setOpaque(false);
		
		ImageIcon bg=new ImageIcon("主界面.PNG");
		JLabel label=new JLabel(bg);
		label.setBounds(-3,-3,bg.getIconWidth(),bg.getIconHeight());
		myframe.getLayeredPane().add(label,new Integer(-30001));
		myframe.setBounds(0, 0, bg.getIconWidth()+11,bg.getIconHeight()+30);
		
		jlAim.setBounds(5, 10, 100, 30);
		jlMoney.setBounds(5, 50, 100, 30);
		jlTime.setBounds(120,10,100,30);
		jlCount.setBounds(120,50,100,30);
		jlExit.setBounds(600, 50,110,20);
		jlExit.addActionListener(this);
		jlstop.setBounds(610, 20,80,20);
		jlstop.addActionListener(this);
		jstop.setBounds(610, 20,160,20);
		jlMoney_1.setBounds(305,90,100,20);
		jlMoney_1.setFont(new Font("隶书",Font.BOLD,20));
		jlMoney_2.setBounds(490,90,100,20);
		jlMoney_2.setFont(new Font("隶书",Font.BOLD,20));
		jp1.setBounds(300,110,400,50);
		jp1.setFont(new Font("隶书",Font.BOLD,20));
		jp2.setBounds(490,110,400,50);
		jp2.setFont(new Font("隶书",Font.BOLD,20));
		jn.setBounds(290,200,120,50);
		jn.setFont(new Font("隶书",Font.BOLD,20));
		jn.addActionListener(this);
		je.setBounds(490,200,120,50);
		je.setFont(new Font("隶书",Font.BOLD,20));
		je.addActionListener(this);
		gg.setBounds(350,300,400,50);
		je.setFont(new Font("隶书",Font.BOLD,20));
		player.setBounds(50, 230, 300, 300);
		team.setBounds(400, 230, 300, 300);
		start.setBounds(300,300, 80, 40);
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
		while(!start_flag) System.out.println("@@@");
		
		this.produce();
		client=new Client(this);
		client.start();
		client.sendlayout();
		music=new Music(this);
		
		while(gameState!=0) {System.out.println("99");}
		System.out.println("oo");
		this.start();
		
		timer=new Timer();
		timer.schedule(new TimerTask() {
			int a=-1;
			public void run() {
				if(gameState==0) {
					try{client.sendhook();}catch(Exception e) {}
					try{client.sendpig();}catch(Exception e) {}
					a++;
					if(a==4) {
						a=0;
						iTime--;
					    if(iTime==-1) { gameState=1;try{client.sendexit();}catch(Exception e) {}}
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
				clear();
				if(iAim<=iMoney) { 
					gg.setText("您通过了本关");
				    jn.setText("下一关");
				    iCount++;
				}
				else {
					gg.setText("很遗憾,您没有通过本关");
					setrecord();
					display();
					jn.setText("重来");
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
			}
			else music.chainstop();
			try {Thread.sleep(5);}catch(Exception e) {};
		}
	}
	
	public void paintComponent(Graphics g) {//画钩子和线
		if(gameState==0 && hook1flag==1 && hook2flag==1) {
			g.drawLine(Hhook1.hookPos[0], Hhook1.hookPos[1], Hhook1.hookPos[2]+10, Hhook1.hookPos[3]+2);
			g.drawLine(Hhook2.hookPos[0], Hhook2.hookPos[1], Hhook2.hookPos[2]+10, Hhook2.hookPos[3]+2);
		}
	}
	
	public void produce() {
		boolean flag=true;
		int k=iAim;
		while(flag) {
			iAim=k;
			amount=14+(int)(Math.random()*4);
			bonus=new Thing[amount];
			is_exist=new boolean[amount];
			for(int i=0;i<amount;i++) {
				is_exist[i]=true;
			}
			pigNum=new int[amount];
			pigCount=0;
			int a=(int)(Math.random()*2);
			int b=a+(int)(Math.random()*2);
			for(int i=0;i<a;i++) {
				bonus[i]=new Thing(this,"goldsmall",(int)(Math.random()*700), (int)(Math.random()*315+215));
				iAim+=bonus[i].value*0.5;
			}
			for(int i=a;i<b;i++) {
				bonus[i]=new Thing(this,"goldmedium",(int)(Math.random()*700), (int)(Math.random()*255+215));
				iAim+=bonus[i].value*0.5;
			}
			for(int i=b;i<5;i++) {
				bonus[i]=new Thing(this,"goldlarge",(int)(Math.random()*680), (int)(Math.random()*265+215));
				iAim+=bonus[i].value*0.7;
			}
			for(int i=5;i<9;i++) {
				bonus[i]=new Thing(this,"stone",(int)(Math.random()*700), (int)(Math.random()*265+215));
			}
			for(int i=9;i<12;i++) {
				bonus[i]=new Thing(this,"pig",(int)(Math.random()*700), (int)(Math.random()*315+215));
				pigNum[pigCount]=i;
				pigCount++;
			}
			for(int i=12;i<14;i++) {
				bonus[i]=new Thing(this,"pocket",(int)(Math.random()*710), (int)(Math.random()*265+215));
				iAim+=bonus[i].value*0.3;
			}
			for(int i=14;i<amount;i++) {
				switch((int)(Math.random()*6)){
				case 0:bonus[i]=new Thing(this,"goldsmall",(int)(Math.random()*710), (int)(Math.random()*325+200));break;
				case 1:bonus[i]=new Thing(this,"goldmedium",(int)(Math.random()*710), (int)(Math.random()*275+200));break;
				case 2:bonus[i]=new Thing(this,"goldlarge",(int)(Math.random()*660), (int)(Math.random()*275+200));break;
				case 3:bonus[i]=new Thing(this,"stone",(int)(Math.random()*700), (int)(Math.random()*275+200));break;
				case 4:bonus[i]=new Thing(this,"pocket",(int)(Math.random()*700), (int)(Math.random()*275+200));break;
				case 5:bonus[i]=new Thing(this,"pig",(int)(Math.random()*710), (int)(Math.random()*325+200));
				pigNum[pigCount]=i;pigCount++;break;
				default:break;
				}
			}
			flag=false;
			for(int i=0;i<amount;i++) {
				for(int j=i+1;j<amount;j++) {
					if(is_exist[i]==true && bonus[i].is_attack(bonus[j])) {
						flag=true;
						break;
					}
				}
			}
		}
		for(int i=0;i<amount;i++) {
			bonus[i].getLabel();
		}
		jlAim.setText("目标钱数: "+iAim);
	}
	
	public void start(){
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
		if(Hhook1.hookPos[4]!=-1) remove(bonus[Hhook1.hookPos[4]].a);
		if(Hhook2.hookPos[4]!=-1) remove(bonus[Hhook2.hookPos[4]].a);
		remove(Hhook1.jlHook);
		remove(Hhook2.jlHook);
		this.repaint();
	}
	
	public void restart(){
		iTime=60;
		jlMoney.setText("共获金钱: "+iMoney);
		jlTime.setText("剩余时间: "+iTime);
		jlMoney_1.setText("$"+iMoney1);
		jlMoney_2.setText("$"+iMoney2);
		jlCount.setText("第 "+iCount+" 关");
		jp1.setText("玩家一：");
		jp2.setText("玩家二：");
		this.produce();
		repaint();
	}
	public static void main(String[] argvs) throws Exception{
		Scene jpScene=new Scene();
	}
    
	public void read() {
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
	
	public void setrecord() {
		Record single=new Record(iMoney2);
		Record team=new Record(iMoney);
		treesingle.add(single);
		treeteam.add(team);
		String str="";
		try {
			PrintStream out=new PrintStream(filesingle);
			for(Record word:treesingle)
				str+=word.getstring()+'\n';
			System.out.println(str);
			out.print(str);
			out.close();
		}
		catch(Exception e) {}
		try {
			str="";
			PrintStream out=new PrintStream(fileteam);
			for(Record word:treeteam)
				str+=word.getstring()+'\n';
			out.print(str);
			out.close();
		}
		catch(Exception e) {}
	}
	
	public void display() {
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("退出本关")) {
			gameState=1;
			try{client.sendexit();}catch(Exception w) {};
		}
		else if(e.getActionCommand().equals("退出")) {
			try{client.sendchoice(true);}catch(Exception w) {};
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
			System.out.println("zanting");
			gameState=2;
			try{client.sendstop();}catch(Exception e2) {}
			jlstop.setText("继续");
		}
		else if(e.getActionCommand().equals("继续")) {
			System.out.println("继续");
			try{client.sendcontinue();}catch(Exception e2) {}
			jlstop.setText("暂停");
			gameState=0;
		}
		else if(e.getActionCommand().equals("下一关") || e.getActionCommand().equals("重来")) {
			if(jp1.getText().equalsIgnoreCase("玩家一：选择继续")) { 
				try{client.sendchoice(false);}catch(Exception w) {};
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
				try{client.sendchoice(false);}catch(Exception w) {};
				jp2.setText("玩家二：选择继续");
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
		if(e.getKeyCode()==KeyEvent.VK_DOWN && Hhook2.state_hook==0) {
			music.playonce();
			Hhook2.state_hook=1;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
}
