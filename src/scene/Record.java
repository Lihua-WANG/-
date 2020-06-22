package scene;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.*;

public class Record implements Comparable<Record>{
	public int score;
	public long time;
	public String other="";
	String timestr;
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	public Record(String str) {
		Scanner in=new Scanner(str);
		other+=in.next()+" ";
		score=in.nextInt();
		other+=score+" ";
		other+=in.next()+" ";
		other+=in.next()+" ";
		other+=in.next()+" ";
		time=in.nextLong();
		timestr=str;
	}
	public Record(int a) {
		score=a;
		time=new Date().getTime();
		other="SCORE: "+score+" "+"TIME: "+df.format(new Date());
		timestr="SCORE: "+score+" "+"TIME: "+df.format(new Date())+" "+time;
	}
	public String getstring() {
		return timestr;
	}
	@Override
	public int compareTo(Record kk) {
		if(kk.score>score) return 1;
		else if(kk.score<score) return -1;
		else {
			if(time>kk.time) return 1;
			else return -1;
		}
	}
	
}
