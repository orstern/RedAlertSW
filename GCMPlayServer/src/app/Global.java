import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import alertsManager.AlertsChecker;
import alertsManager.CitiesDictionary;
import alertsManager.City;
import alertsManager.JsonParser;

import com.fasterxml.jackson.databind.JsonNode;

public class Global extends GlobalSettings {

	ScheduledExecutorService scheduledExecutorService = Executors
			.newScheduledThreadPool(1);

	public void onStart(Application app) {
		Logger.info("Application has started");

		// Try to load the cities
		try {
			List<City> cities = CitiesDictionary.getCitiesById("עמק יזרעאל 93");
		} catch (Exception ex) {
			Logger.info("There was a problem loading the cities JSON");
		}

		// Try to communicate with PAKAR services
		try {
			JsonNode jsonTry = JsonParser
					.httpGetRequestToJson(AlertsChecker.ALERTS_JSON_URI);
		} catch (Exception ex) {
			Logger.info("There was a problem communicating with PAKAR services");
		}

		// Run the Executor of the alerts
		scheduledExecutorService.scheduleAtFixedRate(new AlertsChecker(), 1, 1,
				TimeUnit.SECONDS);

	}

	public void onStop(Application app) {
		Logger.info("Application shutdown...");
		scheduledExecutorService.shutdown();
	}

}