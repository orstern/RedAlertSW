package models;

import java.util.List;

import play.Logger;
import play.libs.F.Callback;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import UsersManager.dal;
import alertsManager.City;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class GCM {
	private static String GCM_URL = "https://android.googleapis.com/gcm/send";
	private static String API_KEY = "AIzaSyB6KDOPgjpxk__9ASRnLW2brYJ0T03hrSE";
	private static String PROJECT_ID = "548355374500";

	public static void SendAlertNotification(List<City> cities, String title) {
		Logger.info("Sending notification..");
		ObjectNode jsnMessage = new ObjectNode(JsonNodeFactory.instance);
		jsnMessage.put("registration_ids", dal.getAllRegIds());

		ObjectNode jsnData = new ObjectNode(JsonNodeFactory.instance);
		ArrayNode citiesArray = new ArrayNode(JsonNodeFactory.instance);

		for (City currCity : cities) {
			citiesArray.add(currCity.getCity());
		}

		jsnData.put("cities", citiesArray);
		jsnData.put("title", title);
		jsnMessage.put("data", jsnData);
		Promise<WSResponse> promise = WS.url(GCM_URL)
				.setHeader("Authorization", "key=" + API_KEY)
				.setContentType("application/json").post(jsnMessage);

		promise.onRedeem(new Callback<WSResponse>() {

			@Override
			public void invoke(WSResponse response) throws Throwable {
				switch (response.getStatus()) {
				case 200:
					Logger.info("ok");
					Logger.info(response.getBody().toString());
					break;
				default:
					Logger.info("error while sending notificaiton,status:"
							+ response.getStatus() + ": " + response.getBody());
					break;
				}

			}
		});

	}
}
