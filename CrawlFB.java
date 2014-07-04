package Shoutbox;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import Shoutbox.GlobalParamater;

public class CrawlFB {
  //declare a UserInfo class object to store details of users whose updates have to be tracked.
	static UserInfo info;
  //********************************************************
	static GlobalParamater gp = new GlobalParamater();
	//This method sets up a FB tarcking mechanism for the users.
	public void fbSetup(final ConcurrentDB<Date, String> conDB)
			throws InterruptedException {
    //populates the info object by reading  username,token and initial start date from global parameters object. 
		info = new UserInfo(gp.userId, gp.userToken, gp.userDate);
		//The number of users to be tracked are determined by measuring the length of the Array that stores User IDs.
		  int numUsers = gp.userId.length;
    //Create an array of threads, as many threads have to be created as as the number of users.     
      Thread[] FB_users = new Thread[numUsers];
    // loop to automate thread creation.
      for ( int index = 0;index<numUsers;index++)
          {
              final int  tempIndex=index;
              //System.out.println(index); 
              //create a anonymous thread object which calls the getUpdate method for each user.
              FB_users[index] = new Thread() {
		  public void run() {
			 try {

				    getUpdate(conDB, tempIndex);

			     } catch (Exception e) {
				  // TODO Auto-generated catch block
				    e.printStackTrace();
			}
		}
	};
          
          }
          //start all the threads so that they can start capturing FB updates, these set of threads will
          //get archive update from the users beginning from the specified initial date.
          for(int index = 0;index<numUsers;index++){
          FB_users[index].start();
          }
          //have to wait for all these threads to finish, so that archive data is retrived and stored, before we start
          //tracking live update of the users.
          for(int index = 0;index<numUsers;index++){
              FB_users[index].join();
              }
		
		//An infinite loop that runs to capture live update of the users
		while (true)
			liveFB(conDB);

	}
  //this method captures live update of the users.
	public void liveFB(final ConcurrentDB<Date, String> conDB)
			throws InterruptedException {

		/*same structural approach as above is followed to paralellize the process of tracking live updates.*/
		int numUsers = gp.userId.length;
       
        Thread[] FB_users = new Thread[numUsers];
        // loop to automate thread creation.
        
        for ( int index = 0;index<numUsers;index++)
        {
            final int  tempIndex=index;
            //System.out.println(index);
            FB_users[index] = new Thread() {
		public void run() {
			try {

				getUpdate(conDB, tempIndex);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
        
        }
        
        for(int index = 0;index<numUsers;index++){
        FB_users[index].start();
        }
        for(int index = 0;index<numUsers;index++){
            FB_users[index].join();
            }
		
		

	}
  /*core functionality of retrieving the status*/
	private static void getUpdate(ConcurrentDB<Date, String> conDB, int index)
			throws Exception {
    //get userID of the user from info.
		String id = info.getID(index);
    //get token of the user from info.
		String token = info.getToken(index);
    //get live date of the user from info.
		Date startingDate = info.getLiveDate(index);
    //extract timestamp from the starting date object.
		long timeStamp = startingDate.getTime();
    //frame a http query for this particular user using his authentication token and latest date.
		String url = "https://graph.facebook.com/" + id
				+ "/statuses/?fields=message&access_token=" + token + "&since="
				+ (timeStamp / 1000);
    //send this http query to the facebook server and get the response as input stream.
		BufferedReader in = new BufferedReader(new InputStreamReader(
				DataCommunicator.sendGetDataToServer(url)));

		String inputLine = "";
    //initialize a StringBuffer object, which is just like string but we can change it even after initializing.
		StringBuffer response = new StringBuffer();
		Date liveDate = null;
		boolean flag = false;
    //read the content stream returned from FB server line by line and apppend it to the response until
    //all the content has been read.
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);

		}
		in.close();
     //convert StringBuffer to String and than parse it into a JSON object.
		JSONObject json = (JSONObject) new JSONParser().parse(response
				.toString());
     //the data part of the json object represents an array that conatins the information ,in our case updates.
		JSONArray msg = (JSONArray) json.get("data");
     //get an iterator to move over this array.
		@SuppressWarnings("rawtypes")
		Iterator i = msg.iterator();
     //traverse the array 
		while (i.hasNext()) {
			JSONObject slide = (JSONObject) i.next();
      //frame the message which includes the update retrieved from response and username from info object. these two have to
      //be appended together.
			String mesg = info.getID(index) + (String) slide.get("message");
      //get date by extracting it from JSON object.
			String sdate = (String) slide.get("updated_time");
      // The common date format that we have to use as key while inserting into conDB.
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ssZ");
      //parse the date obtained from Facebook and convert it into the required format.
			Date date = dateFormat.parse(sdate);
      //push the (date:status update) pair into conDB.
			conDB.put(date, mesg);
      //update the liveDate with the latest value.
			if (!flag) {
				liveDate = date;

			}
			flag = true;

		}
    //update  the date in the info object for this user.
		if (liveDate != null)
			info.setLiveDate(index, liveDate);

	}
	
	public static void stop(){
		
	}

}