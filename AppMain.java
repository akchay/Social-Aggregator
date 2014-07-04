package Shoutbox;

import java.util.Date;

import twitter4j.TwitterException;
public class AppMain {

	public static void main(String[] args) throws InterruptedException {
    //make an object of the type conDB which is a concurrent database that will store the 
    //status and tweets from users.
		final ConcurrentDB<Date, String> conDB = new ConcurrentDB<>();
    //create a crawlFB obect
		final CrawlFB fb = new CrawlFB();
		//create a Thread of the type anonymous class and call method to initialize 
    //fb crawler from it.
		Thread fbThread = new Thread() {
			public void run() {
				try {
					fb.fbSetup(conDB);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}   ;
		//Create a thread of the type anonymous thread  and call 
    //method to initialte pulling tweets.
		Thread twitterThread = new Thread() {
			public void run() {
				crawlTweet twitter = new crawlTweet();
				try {
					twitter.twitterSetUp(conDB);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (TwitterException e) {
					e.printStackTrace();
				}
			}
		};
    //start threads for both facebook and twitter.
		twitterThread.start();
		fbThread.start();
		twitterThread.join();
		statusRetrive<Date,String> statusCapture=new statusRetrive(conDB);
		statusCapture.start();
		Entry e;
		TweetRetrieve tweetCapture=new TweetRetrieve(conDB);
		tweetCapture.start();
  }

}
