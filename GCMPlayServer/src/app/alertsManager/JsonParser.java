package alertsManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import play.Logger;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class JsonParser {

	public static JsonNode httpGetRequestToJson(String url) {

		try {
			Promise<WSResponse> promise = WS.url(url).get();
			WSResponse response = promise.get(10000);
			return response.asJson();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static JsonNode getAlertExample() {
		String json = "{ \"id\" : \"1405053379253\",\"title\" : \"פיקוד העורף התרעה במרחב \",\"data\" : [\"test\"]}";
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		try {
			JsonNode example = mapper.readTree(json);
			ArrayNode exampleData = (ArrayNode) example.get("data");

			List<String> randomCities = CitiesDictionary.getRandomCitiesID();
			for (String string : randomCities) {
				exampleData.add(string);
			}

			return example;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static List<City> createCitiesListOutOfData(ArrayNode arrayOfCityIds) {

		List<City> citiesArray = new ArrayList<City>();

		for (final JsonNode currCity : arrayOfCityIds) {

			ObjectMapper mapper = new ObjectMapper();
			Object obj;
			String stringValue;
			try {
				obj = mapper.treeToValue(currCity, Object.class);
				stringValue = mapper.writeValueAsString(obj);
				if (stringValue.startsWith("\"") && stringValue.endsWith("\"")) {
					stringValue = stringValue.substring(1,
							stringValue.length() - 1);
				}

				String currCityId = stringValue;
				List<City> citiesToAlert = CitiesDictionary
						.getCitiesById(currCityId);

				if (citiesToAlert == null) {
					System.out.println("city ID " + currCityId
							+ " dosen't exist");
				} else {
					for (City city : citiesToAlert) {
						citiesArray.add(city);
					}
				}
			} catch (JsonProcessingException e) {
				Logger.info("createCitiesListOutOfData:there was a problem with the given arraynode of cities");
				e.printStackTrace();
			}
		}

		return citiesArray;
	}

}
