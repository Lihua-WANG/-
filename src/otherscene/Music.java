package otherscene;

import java.applet.*; 
import java.net.*; 
import java.io.*; 
import javax.swing.JApplet; 

public class Music extends JApplet
{ 
	AudioClip snd;
	AudioClip sn;
	Otherscene jp;
    public void play(Otherscene j) {//�ѱ������ļ�װ�뵽AudioClip���󣬲���ͣѭ����������
    	jp=j;
    	URL mus=null;    
        try   
        {  
            mus = new File("background.wav").toURI().toURL();   
              
        } catch (MalformedURLException e) {  
                e.printStackTrace();  
        }  
        snd = JApplet.newAudioClip(mus);   
        snd.loop();
    }  
    public void playonce() {//������ת���������ļ�װ�뵽AudioClip����ֻ���ڿ������»�����ʱ����һ������
    	URL mus=null;    
        try   
        {  
            mus = new File("4469.wav").toURI().toURL();   
              
        } catch (MalformedURLException e) {  
                e.printStackTrace();  
        }  
        sn = JApplet.newAudioClip(mus);   
        sn.play();  
    }
    public void stop() {
    	snd.stop();//ֹͣ���ű�������
    }
    public void start() {//��ʼѭ�����ű�������
    	snd.loop();
    }
    public void chainstop() {//ֹͣ��������ת��������
    	if(jp.flag==true) {
    		sn.stop();
    	    jp.flag=false;
    	}
    	else;
    }
} 
