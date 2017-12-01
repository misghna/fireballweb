package com.baeldung.ib.service;

import java.util.List;

import com.ib.controller.ApiController;
import com.ib.controller.ApiController.IConnectionHandler;

public class ConnectionHandler implements IConnectionHandler {

	
	private boolean connected;
	@Override
	public void connected() {
		System.out.println("connected");
		connected = true;

	}

	@Override
	public void disconnected() {
		System.out.println("disconnected");

	}

	@Override
	public void accountList(List<String> list) {
		// TODO Auto-generated method stub

	}

	@Override
	public void error(Exception e) {
		e.printStackTrace();

	}

	@Override
	public void message(int id, int errorCode, String errorMsg) {
		System.out.println(errorMsg);

	}

	@Override
	public void show(String string) {
		System.out.println(string);

	}

	public boolean isConnected() {
		return connected;
	}

	
}
