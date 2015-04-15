package UsersManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import models.User;
import play.Logger;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class dal {

	private static List<User> usersList = new ArrayList<User>();
	private static AtomicInteger userCounter = new AtomicInteger(0);

	public static void addUser(String regId) {
		if (findUserByRegId(regId) != null) {
			User newUser = new User(regId);
			usersList.add(newUser);

		} else {
			Logger.info("addUser:reg id " + regId
					+ "already exists,didnt add user");
		}
	}

	public static void removeUser(User userToRemove) {

		if (findUserByRegId(userToRemove.getRegId()) != null) {
			for (int i = 0; i < usersList.size(); i++) {
				if (usersList.get(i).getId() == userToRemove.getId()) {
					usersList.remove(i);
					break;
				}
			}
		} else {
			Logger.info("removeUser:no user found with the regId "
					+ userToRemove.getRegId());
		}
	}

	public static User findUserByRegId(String regId) {
		for (User user : usersList) {
			if (user.getRegId() == regId) {
				return user;
			}
		}
		return null;
	}

	public static int GenerateUserId() {
		return userCounter.incrementAndGet();
	}

	public static ArrayNode getAllRegIds() {
		ArrayNode lstRegIds = new ArrayNode(JsonNodeFactory.instance);
		// for (User user : usersList) {
		// lstRegIds.add(user.getRegId());
		// }
		lstRegIds
				.add("APA91bFAILjWNmvSczxE4TbedB1cmZ2umGWB2Y48SCbVG21eKgjNvzQaxUrlvIvMweiepixG-mosxEmu9na4A0Mv_RhmEF1BvDLP3m8Gr_ID7LECxBdHWEVoDx1h7D3JLQzjMrXC2w_InC1U-H5IhRThFPFiu8ro3w");

		return lstRegIds;
	}

}
