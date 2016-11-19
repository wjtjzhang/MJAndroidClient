package com.lele.mj.client.service;

import java.io.IOException;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lele.entity.Response;

public class HallService implements Service {

	Logger LOG = LoggerFactory.getLogger(HallService.class);
	
	public void process(Response response, IoSession session) throws IOException {
		switch (response.getAction()) {
		case HALL:
			LOG.info(response.getObject().toString());
			break;
		default:
			break;

		}
		
	}
}
