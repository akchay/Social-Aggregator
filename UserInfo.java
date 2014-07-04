package Shoutbox;

import java.util.Date;

public class UserInfo {
  //Array to store FB UserIds of the users.
	private String userId[] = new String[3];
  //Array to store Authentication tokens for user's FB account.
	private String userToken[] = new String[3];
  //This array keeps latest Date/time value of the previously scanned update of a user.
	private Date userLiveDate[] = new Date[3];
  //Constructor for user info class.
  /*The constructor will initialize the arrays with the values passed*/
	UserInfo(String id[], String token[], Date liveDate[]) {
		for (int i = 0; i < userId.length; i++) {
			this.userId[i]=new String();
			this.userId[i] = id[i];
		}
		for (int i = 0; i < userToken.length; i++){
			this.userToken[i]=new String();
			this.userToken[i] = token[i];}
		for (int i = 0; i < userLiveDate.length; i++){
			
			this.userLiveDate[i] = liveDate[i];
			}
		}
  //returns the userd ID of a user given its index
	public String getID(int index) {
		return (this.userId[index]);
	}
  //returns the authentication token of a user given its index
	public String getToken(int index) {
		return (this.userToken[index]);
	}
  //returns the LiveDate of a user given its index
	public Date getLiveDate(int index) {
		return (this.userLiveDate[index]);
	}
  //updates the LiveDate of a user in the Array.
	public void setLiveDate(int index, Date liveDate) {
		this.userLiveDate[index] = liveDate;
		
	}

}
