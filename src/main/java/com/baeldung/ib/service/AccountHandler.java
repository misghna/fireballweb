package com.baeldung.ib.service;

import com.ib.controller.ApiController.IAccountHandler;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.baeldung.websocket.Relay;
import com.ib.controller.Position;

public class AccountHandler implements IAccountHandler {

	
	private Map<String,String> acctMap = new HashMap<String,String>();
	
	private final List<String> reqFields = Arrays.asList(new String[]{"InitMarginReq","MaintMarginReq",
					"NetLiquidation","RealizedPnL","UnrealizedPnL","TotalCashBalance","TotalCashValue"});
	
	private double lastRealizedValue =0;
	private double lastUnRealizedValue =0;
	
	public AccountHandler(){
	}
	
	@Override
	public void accountDownloadEnd(String arg0) {

	}

	@Override
	public void accountTime(String arg0) {


	}

	@Override
	public void accountValue(String acctNo, String type, String amount, String currency) {
		if(currency!=null && currency.equals("USD") && reqFields.contains(type)){
		if(type.equals("RealizedPnL")){
			lastRealizedValue = Double.parseDouble(amount);
		}else if(type.equals("UnrealizedPnL")){
			lastUnRealizedValue = Double.parseDouble(amount);
		}
		
		acctMap.put(type, amount);
		Double total = lastRealizedValue + lastUnRealizedValue;
		acctMap.put("totalPnL", total.toString());
		
		Long time = System.currentTimeMillis();
		acctMap.put("lastUpdatedTime", time.toString());
	}

	}

	@Override
	public void updatePortfolio(Position pos) {
		StringBuffer sb = new StringBuffer();
		sb.append("Position : " + pos.position() + ":");
		sb.append("P&L Realized : " + pos.realPnl() + ":");
		sb.append("P&L Unrealized : " + pos.unrealPnl() + ":");
		acctMap.put("Ticker : " + pos.contract().symbol(), sb.toString());

	}
	
	public Map<String,String> getActUpdate(){
		return new HashMap<String,String>(acctMap);
	}
	

}
