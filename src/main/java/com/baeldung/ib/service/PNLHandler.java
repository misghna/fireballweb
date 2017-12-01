package com.baeldung.ib.service;

import com.baeldung.websocket.Relay;
import com.ib.controller.ApiController.IPnLHandler;

public class PNLHandler implements IPnLHandler {

	private Relay relay;
	
	public PNLHandler(Relay relay){
		this.relay=relay;
	}
	
	@Override
	public void pnl(int arg0, double arg1, double arg2) {
		String result = arg1 + ", " + arg2;
		relay.sendMessage(result);
	}

}
