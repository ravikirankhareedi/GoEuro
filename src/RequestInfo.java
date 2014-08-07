import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class provides method to query the web service
 */
public class RequestInfo {
	
	String response; //Default value is null;
	
	public RequestInfo() {
		
	}
	/**
	 * This method is used to query the results from the web service
	 * @param key - search string
	 * @return response string 
	 */
	public String queryService(String key)
	{
		String path = "http://api.goeuro.com/api/v2/position/suggest/en/"+ key;
		URL url = null;
		
		try {
			url = new URL(path);
			
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
			//Reading in UTF-8 encoding to handle german/special characters
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			
			String input = null;
			StringBuffer resp = new StringBuffer();
			while((input = in.readLine()) != null)
			{
				resp.append(input);
			}
			in.close();
			response = resp.toString();
		
		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	public static void main(String[] args)
	{
		if (args.length == 0) //User has not entered any arguments
		{
			System.out.println("Please enter a STRING");
			System.out.println("Format: java -jar GoEuroTest.jar STRING");
			return;
		}
		
		if(args.length > 1) //User has entered more than one arguments
		{
			System.out.println("There are more than one arguments. 1st argument is considered");
		}
		
		//Used Facade design pattern. main() acts as a controller.
		
		RequestInfo rObject = new RequestInfo();
		String respString = rObject.queryService(args[0]);
		
		if(respString != null)
		{
			Parse_Write wObject = new Parse_Write();
			int result = wObject.parseResponse(respString);
			
			//0 - Successful parsing; -1 - No entries to write;  
			if(result == 0)
			{
				wObject.writefile();
			}
			else if(result == -1)
			{
				System.out.println("No entries to write");
			}
			else
			{
				//Can add more error codes in future
			}
		}
		
	}

}
