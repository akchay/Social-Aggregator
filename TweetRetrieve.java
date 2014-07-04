package Shoutbox;

import java.util.Date;

public class TweetRetrieve extends Thread{

	ConcurrentDB<Date,String> conDB;
	public TweetRetrieve(ConcurrentDB<Date,String> conDBVal) {
		// TODO Auto-generated constructor stub
		this.conDB = conDBVal;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		crawlTweet twitter = new crawlTweet();
		twitter.liveTweet(conDB);

	}
}
