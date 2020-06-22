package scene;

import java.applet.*; 
import java.net.*; 
import java.io.*; 
import javax.swing.JApplet; 

public class Music extends JApplet
{ 
	AudioClip sn;
	Scene jp;
	public Music(Scene j) {
		jp=j;
	}
    public void playonce() {
    	URL mus=null;    
        try   
        {  
            mus = new File("4469.wav").toURI().toURL();   
              
        } catch (MalformedURLException e) {  
                e.printStackTrace();  
        }  
        jp.flag=true;
        sn = JApplet.newAudioClip(mus);   
        sn.play();  
        //System.out.println(new File("4469.wav").length());  
    }
    public void chainstop() {
    	if(jp.flag==true) {
    		sn.stop();
    	    jp.flag=false;
    	}
    	else;
    }
} 

