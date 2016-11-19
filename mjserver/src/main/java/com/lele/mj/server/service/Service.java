package com.lele.mj.server.service;

import java.io.IOException;

import org.apache.mina.core.session.IoSession;

import com.lele.entity.Request;

public interface Service {

	void process(Request request, IoSession session) throws IOException, ClassNotFoundException;

}
