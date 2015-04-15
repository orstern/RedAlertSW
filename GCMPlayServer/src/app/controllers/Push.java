package controllers;

import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import UsersManager.dal;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class Push extends Controller {

	public static Result register(String regId) {
		ObjectNode result = Json.newObject();

		User user = dal.findUserByRegId(regId);

		if (user != null) {
			result.put("status", "already registered");
		} else {
			dal.addUser(regId);
			result.put("status", "registered");

			Logger.info("registered user with the regId" + regId);
		}

		return ok(result);
	}

	public static Result unregister(String regId) {
		ObjectNode result = Json.newObject();

		User userToUnregister = dal.findUserByRegId(regId);

		if (userToUnregister != null) {
			result.put("status", "unregistered");
			dal.removeUser(userToUnregister);
		} else {
			result.put("status", "not registered");
		}

		return ok(result);
	}
}
