package controllers;

import java.util.List;

import models.GCM;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import alertsManager.City;
import alertsManager.JsonParser;

import com.fasterxml.jackson.databind.node.ArrayNode;

public class Application extends Controller {

	public static Result index() {
		return ok();
	}

	public static Result sendExampleMessage() {
		Logger.info("Sending test notification..");

		// Create example
		List<City> cities = JsonParser
				.createCitiesListOutOfData((ArrayNode) (JsonParser
						.getAlertExample().get("data")));

		GCM.SendAlertNotification(cities);
		return ok();
	}

}
