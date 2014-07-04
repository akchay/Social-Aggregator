package Shoutbox;
//This is the reader thread which pops values from
//conDB , the print functionality is implemented in 
//in the pop function itself. 
public class statusRetrive<K, V> extends Thread {
	ConcurrentDB<K, V> conDB;

	statusRetrive(ConcurrentDB<K, V> conDBVal) {
		this.conDB = conDBVal;
	}
 //the run method has a infinite loop which keeps running 
 //forever.
	@Override
	public void run() {
		while (true) {
      //if the size of conDB is greter than 0, which
      //implies there is a new update/tweet.
      //while true was used as even if the size of
      //conDB falls to 0 ,other threads will write to 
      //it and so that will have to be popped and
      //printed.
			while (conDB.size() > 0) {
				conDB.pop();
			}
		}

	}

}
