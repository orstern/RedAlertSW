package controllers;

import java.util.List;

import models.GCM;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import alertsManager.CitiesDictionary;
import alertsManager.City;
import alertsManager.JsonParser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class Application extends Controller {

	public static Result index() {
		return ok();
	}

	public static Result getAllCitiesByIds() {
		return ok(CitiesDictionary.getAllCitiesByIds());
	}

	public static Result sendExampleMessage() {
		Logger.info("Sending test notification..");

		JsonNode alertExample = JsonParser.getAlertExample();

		// Create example
		List<City> cities = JsonParser
				.createCitiesListOutOfData((ArrayNode) (alertExample
						.get("data")));

		GCM.SendAlertNotification(cities, alertExample.get("title").asText());
		return ok();
	}

	public static Result sendNotification() {

		JsonNode requestJson = request().body().asJson();

		if (requestJson.has("cities") && requestJson.has("title")) {
			List<City> cities = JsonParser
					.createCitiesListOutOfData((ArrayNode) requestJson
							.get("cities"));
			String title = requestJson.get("title").asText();
			GCM.SendAlertNotification(cities, title);

		} else {
			return badRequest();
		}

		return ok();
	}

}
