package Shoutbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;
 //This class makes use of twitter4j API to implement its functionality.
public class crawlTweet {
	//static Map<Date, String> map = new TreeMap<Date, String>();
	//static Map<Date, String> synmap = Collections.synchronizedMap(map);
  //create an ArrayList of lists, the list contains objects of the type status
  //status is a class 
  //each list contain the statuses of a particular user 
  // list <--> user
        ArrayList<List<Status>> statusList;
	
  //The following class implements the runnable interface **************     
        class StatusStore implements Runnable
        {
           ArrayList<List<Status>> statusList;
           ConcurrentDB<Date, String> conDB;
           int index;
           //The constructor for the class, pass reference of statuslist , conDB and index for the user as arguments.
           public StatusStore(ArrayList<List<Status>> statusList, ConcurrentDB<Date, String> conDB,int index) 
           {
               this.statusList = statusList;
               this.conDB = conDB;
               this.index = index;
           }

           public void run() 
            {
               //  crawl the tweets(not tweet in conventional sense contains additional metadata) 
               //of this particular user           
      				for (Status status : statusList.get(index))
               {
                //extract the name of the user 
      					String userName = (String) status.getUser().getName();
                //create the message to be stored in the concurrent database by appending username and the actual tweet.
      					String msg = userName + ":" + (String) status.getText();
                //extract the time for creation of this tweet.
      					Date date = status.getCreatedAt();
                //push the <date,msg> pair into the database.
                conDB.put(date, msg);
                }
				
			       }  
        }
  //*************************************************************
	public void twitterSetUp(final ConcurrentDB<Date, String> conDB)
			throws IllegalStateException, TwitterException {
    //create an object of class Twitter which represents a users timeline.
		Twitter twitter = TwitterFactory.getSingleton();
    //the global parameter object contains the user name of users whose tweets have
    //to be extracted in array.
		GlobalParamater gp = new GlobalParamater();
    //get the number of users for whom we need to capture tweets.
    final int numUsers = gp.twitterHandle.length;
    //System.out.println("************************"+numUsers);
    //populate the statusList where each list in StatusList correponds to a unique user.
    statusList=new ArrayList<List<Status>>();
    for (int i = 0;i<numUsers;i++)
    {   //in the first step from the global parameter object we get twiiter handle of the user,remember this was hardcoded.
        //in the second step we get the timeline of the user using API call, which contains all his tweets, this is basically a list
        //in third step we store  this list into our ArrayList 
        statusList.add(i,twitter.getUserTimeline(gp.twitterHandle[i]));
    }
    //create an array of threads
    Thread[] TW_threads = new  Thread[numUsers];
    //initialize this array of threads with runnable objects.
    //all these threads will write into the same database
    //even though it seems that the array statusList is shared and there might be
    //interfrence when accessed from multiple threads, there is none as each thread will
    //access its own list using the index passed to the thread.
    for (int i = 0; i<numUsers;i++)
    {
        TW_threads[i] = new Thread( new StatusStore(statusList,conDB,i));
    }
    //start all these threads.
    for (int i = 0; i<numUsers;i++)
        TW_threads[i].start();
  }

	
  //*********************************************
	public void liveTweet(final ConcurrentDB<Date, String> conDB) {
    //create a API specific listener object which will check for updates 
    //and push it into the database
		final UserStreamListener listener = new UserStreamListener()  {
			@Override
			public void onStatus(Status status) {
        //extract username from the status
				String userName = (String) status.getUser().getName();
        //append username and tweet to create the message.
				String msg = userName + ":" + (String) status.getText();
        //extract the date(timestamp) at which this tweet was created.
				Date date = status.getCreatedAt();
				//push the <date,msg> pair into the database.
				conDB.put(date, msg);
				
			}

			@Override
			public void onException(Exception ex) {
				ex.printStackTrace();
				System.out.println("onException:" + ex.getMessage());
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTrackLimitationNotice(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onBlock(User arg0, User arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDeletionNotice(long arg0, long arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDirectMessage(DirectMessage arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFavorite(User arg0, User arg1, Status arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFollow(User arg0, User arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFriendList(long[] arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUnblock(User arg0, User arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUnfavorite(User arg0, User arg1, Status arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUnfollow(User arg0, User arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUserListCreation(User arg0, UserList arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUserListDeletion(User arg0, UserList arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUserListMemberAddition(User arg0, User arg1,
					UserList arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUserListMemberDeletion(User arg0, User arg1,
					UserList arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUserListSubscription(User arg0, User arg1,
					UserList arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUserListUnsubscription(User arg0, User arg1,
					UserList arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUserListUpdate(User arg0, UserList arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUserProfileUpdate(User arg0) {
				// TODO Auto-generated method stub

			}
		};
		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		twitterStream.addListener(listener);

		// user method-call internally makes use of 
		// TwitterStream and call the adequate listener methods continuously  .
		twitterStream.user();
	}

}
