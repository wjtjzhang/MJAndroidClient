package com.lele.mj.client.service;

import java.io.IOException;

import org.apache.mina.core.session.IoSession;

import com.lele.entity.Response;

public interface Service {
	void process(Response response, IoSession session) throws IOException;
}
