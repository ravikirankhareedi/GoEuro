import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.csvreader.CsvWriter;

/**
 * This class provides methods to parse the response from the web service and write the required entries to CSV file
 */
public class  Parse_Write{
	
	JSONArray arrayObject;
	
	public Parse_Write() {
		
	}
	
	/**
	 * Function to parse the response
	 * @param resp - response string from service to be parsed
	 * @return int - error code
	 */
	public int parseResponse(String resp)
	{
		JSONParser parser = new JSONParser();
		Object ob = null;
		int result = 0;
		try {
			ob = parser.parse(resp);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		arrayObject = new JSONArray();
		arrayObject = (JSONArray)ob;

		if(arrayObject.size() == 0)
		{
			result = -1; //No entries to write 
		}
		return result;
	}

	/**
	 * Function to write the response to CSV with entries _type, _id, name, type, latitude, longitude
	 * @param 
	 * @return void
	 */
	public void writefile()
	{
		String fname = new String("Output_"+System.currentTimeMillis()+".csv");
		CsvWriter writer = new CsvWriter(fname);
	
		try {
			writer.write("_type");
			writer.write("_id");
			writer.write("name");
			writer.write("type");
			writer.write("latitude");
			writer.write("longitude");
			writer.endRecord();

			//In this function, the key list is known and used. If required we can have an additional step to check if key is present  
			for(int i = 0; i < arrayObject.size(); i++)
			{
				JSONObject ob1 = (JSONObject)arrayObject.get(i);

				writer.write(ob1.get("_type") == null ?"null":ob1.get("_type").toString());
				writer.write(ob1.get("_id").toString());
				writer.write(ob1.get("name") == null ? "null" : ob1.get("name").toString());
				writer.write(ob1.get("type") == null ? "null" : ob1.get("type").toString());
				
				JSONObject ob2 = (JSONObject)ob1.get("geo_position");
				writer.write(ob2.get("latitude").toString());
				writer.write(ob2.get("longitude").toString());
				
				writer.endRecord();
			}
			System.out.println("Result written to " + fname);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			writer.close();
		}
	}
}
