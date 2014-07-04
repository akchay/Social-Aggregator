package Shoutbox;
import java.util.Date;


public class Entry  
{
    Date key;
    String value;
    public Entry(Date key, String value)
    {
        this.key = key;
        this.value = value;
    }
    
    public Date getDate(){
    	return this.key;
    			
    }
    public String getMsg(){
    	return this.value;
    			
    }
}
