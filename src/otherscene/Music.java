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
    public void play(Otherscene j) {//把背景乐文件装入到AudioClip对象，并不停循环播放声音
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
    public void playonce() {//把锁链转动的音乐文件装入到AudioClip对象，只是在筷子向下或向上时播放一次声音
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
    	snd.stop();//停止播放背景音乐
    }
    public void start() {//开始循环播放背景音乐
    	snd.loop();
    }
    public void chainstop() {//停止播放锁链转动的声音
    	if(jp.flag==true) {
    		sn.stop();
    	    jp.flag=false;
    	}
    	else;
    }
} 
