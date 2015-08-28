package testWebServiceClient1;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
 
//import org.glassfish.grizzly.http.server.HttpServer;
 
//...
 
public class Main {
 
	static private final String BASE_URI = "";
	
    static private WebTarget target;
 
    static public void main(String[] args) 
    {
        Client c = ClientBuilder.newClient();
        
        String parameters = "parameters";
        
        target = c.target(Main.BASE_URI+"?"+parameters);
 
        String responseMsg = target.path("myresource").request().get(String.class);
        
        System.out.println("response="+responseMsg);
    }
}
