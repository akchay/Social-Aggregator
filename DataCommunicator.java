package Shoutbox;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
public class DataCommunicator {
  //time out value for request in milliseconds.
	private static final int TIMEOUT = 120000;
	// gets required content for the http query and returns a stram containing the content.
	public static InputStream sendGetDataToServer(String serverAddress) throws Exception{
    //initialize objects required for the process of 
    //sending the http query, recieving the response and
    //extracting useful content from it.
	  HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(serverAddress);
		HttpParams httpParams = httpclient.getParams();
		//configure the connection time-out value for this http query.
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
    //configure socket time-out value.
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT);
		try{
       //catch the response returned from the server for this request.
			 HttpResponse response = httpclient.execute(httpGet);
       //store the message entity by extracting it from the response.
			 HttpEntity entity = response.getEntity();
			 
		        if(entity == null)
		        	return null;
		        else{
		        	//System.out.println(entity.getContent().toString());
              //returns content stream of the entity as Input stream.
		        	return entity.getContent();}
		} 
    //handle possible kinds of exceptions.
    catch (ClientProtocolException e) {
	    	System.out.println("ClientProtocolException in sendPostDataToServer");
	    	e.printStackTrace();
	    	throw e;
	    } catch (IOException e) {
	    	System.out.println("IOException in sendPostDataToServer");
	    	e.printStackTrace();
	    	throw e;
	    }catch(Exception e){
	    	System.out.println( "Exception in sendPostDataToServer");
	    	e.printStackTrace();
	    	throw e;
	    }

	}


}
