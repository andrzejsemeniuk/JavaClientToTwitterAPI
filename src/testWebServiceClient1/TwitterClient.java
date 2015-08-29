package testWebServiceClient1;

import java.io.ByteArrayInputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;

public class TwitterClient {

	enum URI {
		Base				("https://api.twitter.com/1.1/"), 
		Authentication	("https://api.twitter.com/oauth2/token"), 
		Search			("https://api.twitter.com/1.1/search/tweets.json");

		URI(String v) {
			address = v;
		}

		public final String address;
	}

	private Client 		client 			= null;
	private String 		access_token		= null;

	private WebTarget 	target 			= null;

	
	
	TwitterClient() 
	{
		client = ClientBuilder.newClient();
	}

	
	
	public boolean authenticate()
	{
		if (access_token==null)
		{
			try 
			{
				target = client.target(URI.Authentication.address);
				
				String authorization = java.net.URLEncoder.encode(TwitterCredentials.CustomerKey,"utf-8")
						+ ":"
						+ java.net.URLEncoder.encode(TwitterCredentials.ConsumerSecret,"utf-8");
				
				String authorizationBase64Encoded = new String(Base64.encodeBase64(authorization.getBytes()));
				
				Form form = new Form();
				form.param("grant_type", "client_credentials");
				
				Entity entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
				
				// NOTE: This is a synchronous call, therefore we'll wait until it completes.
				Response response = target
						.request()
//						.accept("application/json")
						.acceptEncoding("utf-8")
						.header("Content-Type","application/x-www-form-urlencoded;charset=UTF-8")
						.header(	"Authorization","Basic " + authorizationBase64Encoded)
						.post(entity);
        
				System.out.println("authenticate().response status="+response.getStatus());
				String payload = response.readEntity(String.class);				
				System.out.println("authenticate().response="+payload);
				
				if (200==response.getStatus())
				{
			        JsonReader 	jsonReader 		= Json.createReader(new ByteArrayInputStream(payload.getBytes()));
			        JsonObject 	jsonObject 		= jsonReader.readObject();
			        
			        String 		token_type 		= jsonObject.getString("token_type");
			        String 		token			= jsonObject.getString("access_token");
			        
			        if (token_type.equals("bearer"))
			        {
			        	access_token = token;
			        }
				}
			} 
			catch (Exception e) 
			{
				System.out.println(e);
			}
			finally
			{
				if (access_token==null)
				{
	        			System.out.println("authenticate(): failed to authenticate");
				}
				else
				{
	        			System.out.println("authenticate(): authenticated!");
				}
			}
		}
		
		return access_token != null;
	}

	
	
	public void search(String query, int count) {
		if (authenticate()) 
		{
			try 
			{
				target = client.target(URI.Search.address);
				
				// NOTE: This is a synchronous call, therefore we'll wait until it completes.
				Response response = target
						.queryParam("q", query)
						.queryParam("count", Integer.toString(count))
						.request()
//						.accept("application/json")
						.acceptEncoding("utf-8")
						.header("Content-Type","application/x-www-form-urlencoded;charset=UTF-8")
						.header(	"Authorization","Bearer " + access_token)
						.get();
        
				System.out.println("authenticate().response status="+response.getStatus());
				String payload = response.readEntity(String.class);				
				System.out.println("authenticate().response="+payload);
				
				if (200==response.getStatus())
				{
			        JsonReader 	jsonReader 		= Json.createReader(new ByteArrayInputStream(payload.getBytes()));
			        JsonObject 	jsonObject 		= jsonReader.readObject();			        
				}
			} 
			catch (Exception e) 
			{
				System.out.println(e);
			}
			finally
			{
			}

		}
	}

	

    static public void main(String[] args) 
    {
    	// process:
    	//  1 authenticate
    	//  2 call api
    	
    	TwitterClient client = new TwitterClient();
    	
    	client.search("Austin",3);
    	
    }

}