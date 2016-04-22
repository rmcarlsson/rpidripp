package se.trantor.HttClientApp;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.json.*;

public class HttpClient {

	/*
	 * Vid valet av gridpunkt så är det viktigt att tänka på att vald gridpunkt
	 * på land kan ge närmsta gridpunkt ute i havet och omvänt. Vissa parametrar
	 * har stora gradienter i övergången mellan land och hav (ex. det blåser
	 * kraftigare över havet ).
	 */

	public static final String LAT = "59.33";
	public static final String LONG = "13.51";
	public static final String URI_SMHI = "http://opendata-download-metfcst.smhi.se/api/category/pmp1.5g/version/1/geopoint";

	public static void main(String[] args) throws ClientProtocolException, IOException {

		//JsonReader reader = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		//try {
		String uri = URI_SMHI + "/lat/" + LAT + "/lon/" + LONG + "/data.json";
		HttpGet httpget = new HttpGet(uri);

		System.out.println("Executing request " + httpget.getRequestLine());
		CloseableHttpResponse response = httpclient.execute(httpget);
		//StringBuilder rawStrResponse = new StringBuilder();
		//try {
		System.out.println("----------------------------------------");
		System.out.println(response.getStatusLine());

		// Get hold of the response entity
		HttpEntity entity = response.getEntity();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			entity.writeTo(os);
		} catch (IOException e1) {
		}
		String contentString = new String(os.toByteArray());


		//String retSrc = EntityUtils.toString(entity);
		//InputStreamReader in = new InputStreamReader(entity.getContent()
		//reader = Json.createReader(entity.getContent());
		// parsing JSON
		JSONObject result = new JSONObject(contentString); //Convert String to JSON Object
		System.out.println("referenceTime is " + result.getString("referenceTime"));

		JSONArray jsonMainArr = null;
		try {
			jsonMainArr = result.getJSONArray("timeseries");
		}
		catch (Exception e)
		{
			System.out.println(e);
		}


		for (int i = 0; i < jsonMainArr.length(); i++) {  // **line 2**
			JSONObject childJSONObject = jsonMainArr.getJSONObject(i);
			java.util.Calendar t = javax.xml.bind.DatatypeConverter.parseDateTime(childJSONObject.getString("validTime"));
			
		
		
			System.out.println(t.getTime());
			
			System.out.println("validTime is " + childJSONObject.getString("validTime"));
			System.out.println("rainfall is " + childJSONObject.getDouble("t") + "[mm/h]");
		}


		// reader = Json.createReader(new InputStreamReader((response.getEntity().getContent())));

		// If the response does not enclose an entity, there is no need
		// to bother about connection release
		//				if (entity != null) {
		//					BufferedReader br = null;
		//					String line;
		//					InputStream instream = entity.getContent();
		//
		//					try {
		//						instream.read();
		//						br = new BufferedReader(new InputStreamReader(instream));
		//						
		//						reader = Json.createReader(br);
		//						while ((line = br.readLine()) != null) {
		//							rawStrResponse.append(line);
		//						}
		//						
		//						
		//					} catch (Exception ex) {
		//						// In case of an IOException the connection will be released
		//						// back to the connection manager automatically
		//						throw ex;
		//					} finally {
		//						// Closing the input stream will trigger connection release
		//						instream.close();
		//					}
		/*				}
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}*/
	}
}


