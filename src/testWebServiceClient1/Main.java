package testWebServiceClient1;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
 
public class Main {
 
	
    static public void main(String[] args) 
    {
    	// process:
    	//  1 authenticate
    	//  2 call api
    	
    	TwitterClient client = new TwitterClient();
    	
    	client.search("Austin",3);
    	
    }
}
