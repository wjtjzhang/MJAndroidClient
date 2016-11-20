package com.lele.service;

import java.io.IOException;

import org.apache.mina.core.session.IoSession;

import android.os.Handler;

import com.lele.entity.Response;

public interface Service {
	void process(Response response, IoSession session, Handler handler) throws IOException;
}
