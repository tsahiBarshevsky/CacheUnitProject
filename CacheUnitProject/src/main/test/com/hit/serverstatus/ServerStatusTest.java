package com.hit.serverstatus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class ServerStatusTest {

	ServerStatus serverStatus;
	StatObj stat;

	public ServerStatusTest() {
		stat = new StatObj();
		stat.setAvarageDatamodelID(new Integer(5));
		stat.setCurrentUsers(new Long(19));
		stat.setMaxUsers(new Long(34));
		stat.setTime();		
		Map<String, String> userID;
		userID = new HashMap<String, String>();
		userID.put("userId", UUID.randomUUID().toString());
		userID.put("Requests", UUID.randomUUID().toString());
		stat.setUserID(userID);
		serverStatus = new ServerStatus(stat);
	}
	
	@Test
	void get() {
		
		serverStatus.get();
		Assert.assertEquals("19", stat.getCurrentUsers().toString());
		Assert.assertEquals("34", stat.getMaxUsers().toString());
		Assert.assertEquals("5.0", stat.getAvarageDatamodelID().toString());

	}
}
