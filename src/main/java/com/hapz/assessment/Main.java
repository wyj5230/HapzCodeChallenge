package com.hapz.assessment;

/* DO NOT CHANGE */
/* Please do not change the imported functions as you will be assessed based on your usage of the selected libraries */
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.logging.*;
import java.util.regex.*;
import java.net.*;
import com.google.gson.*;
import com.google.gson.stream.*;
/* Please do not change the imported functions as you will be assessed based on your usage of the selected libraries */
/* DO NOT CHANGE */

/**
 *
 * @author dev
 */
public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	static String apiKey = "wyjwyj5230@gmail.com";
	static String url = "http://assessment.hapz.com/api/?";
	static String charset = "UTF-8";

	public static String[] getMovieTitles(String substr) {
		//retrieve title
		int currentPage = 0;
		int totalPage = 0;
		HashSet<String> movieTitles = new HashSet<>();
		do {
			currentPage += 1;
			JsonObject apiResponse = null;
			try {
				apiResponse = getMovieFromAPI(substr, currentPage);
				totalPage = apiResponse.get("total_pages").getAsInt();
				JsonArray movies = apiResponse.getAsJsonArray("data");
				for (JsonElement movie : movies) {
					JsonObject movieObject = movie.getAsJsonObject();
					movieTitles.add(movieObject.get("Title").getAsString());
				}
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "Error when getting API response" + e.getMessage());
			}
		} while (currentPage < totalPage);
		//sorting
		ArrayList<String> movieTitleList = new ArrayList<String>(movieTitles);
		Collections.sort(movieTitleList);
		return movieTitleList.toArray(new String[0]);
	}

	private static JsonObject getMovieFromAPI(String title, int pageNumber) throws IOException {
		String query = String.format("apiKey=%s&Title=%s&page=%s", 
				URLEncoder.encode(apiKey, charset),
                URLEncoder.encode(title, charset),
                pageNumber);
		HttpURLConnection connection = (HttpURLConnection) new URL(url + query).openConnection();
		
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.connect();
		JsonObject result = null;
		if (connection.getResponseCode() == 201 || connection.getResponseCode() == 200) {
			InputStream response = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(response));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			br.close();
			JsonParser jsonParser = new JsonParser();
			result = (JsonObject) jsonParser.parse(sb.toString());
		}
		connection.disconnect();
		return result;
	}
	
	

}
