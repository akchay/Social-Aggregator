package Shoutbox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GlobalParamater {
  //initializes a DateFormat object and configures it with the required format.
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"EEE MMM dd HH:mm:ss Z yyyy");
  //This Array stores usernames of the people whose status-updates have to be captured.    
	String userId[] = { "saumitra.aditya", "akhil.jain.56614",
			"akchay.srivastava" };
	
	//The folowing array stores the access tokens of the users.
  /*Note: The access tokens expire after an hour, or when a user logs out*/
	String userToken[] = {
			"CAACEdEose0cBAAv58D5o7TLAs80RA3vZAwiRNSn3cGhS2fF6anVUy4BZC8MbIFFrXFBxbqZBZBZC7RDI3vlpABlSNknuceo3P6v3ZAY0pLZBwMUEVtw4oTmQE1MBguzzSaM5jvquYD2LwGJBiefpjJlzQZCAqE3uy7fIZA6jHZBgAYVNZBmAtYUvR3UpDZAeAtWJh14ZD",
			"CAACEdEose0cBAGltJHd783mGOTrcTSBn4nVCmp4jIYmdaZBX3EQxgHlxm1EzZAkaCYXujk5YSuvLP3npJ7kq501ihRznzdkIOA1qNqMzaelC3g4JRApuTX1dJOGRc1EBhEPVjSK9yoiG6DBuP8b3OqV43xJ61q7vN7yfc03nn5LLjE6z4PELZAAvtOoQhYZD",
			"CAACEdEose0cBADloDoxX45UZAZBYtaXDCR4KK9UZBCH2Ex9VFOrfkyqP60Y5J9gdZABnBVaMUAaRriCZCRG2XnXRr0iWHifKbiGVbcFdDmGbZBbX9ZAKsXcSZBHiorlYUErcxDXs90rYTYGFWyMo4ZBXRssNCEjYo1znPMNLajHIsJStCZAYmfniWF2FRllCrQ5gkZD" };
	//A initial start date beginning from which the updates of users will be extracted.
	String originalStartDate = "Fri Jan 01 02:23:34 EST 2010";
  //In this case same start date is used for all the 3 users currently being followed.
	Date userDate[] = { fromString(originalStartDate),
			fromString(originalStartDate), fromString(originalStartDate) 
			};
  //String that captures the Date format for twitter.    
	final String LARGE_TWITTER_DATE_FORMAT = "EEE MMM dd HH:mm:ss Z yyyy";
  //Array storing the twitter handle of the users to be monitored on twitter.
	String twitterHandle[] = { "Akkusach", "Aditya_ANSI", "AkchaySriv" };
  //the function below takes a string object parses it and returns a date object.
	private static final Date fromString(String str) {
		try {

			return dateFormat.parse(str);
		} catch (ParseException dfe) {
			// return invalidDate;
			return null;
		}

	}

}
