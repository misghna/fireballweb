package com.baeldung.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.websocket.EncodeException;

import org.json.simple.JSONObject;

import com.baeldung.ib.service.AccountHandler;
import com.baeldung.ib.service.ConnectionHandler;
import com.baeldung.ib.service.LoggerIn;
import com.baeldung.ib.service.LoggerOut;
import com.baeldung.ib.service.PNLHandler;
import com.baeldung.model.Message;
import com.ib.controller.ApiController;


public class Relay implements Runnable {

//	private ChatEndpoint chatEndPoint;
	private final Set<Endpoints> endPoints = new HashSet<Endpoints>();
	
	String actNo = "DU689205";
	
	private static ConnectionHandler conHandler;
	private AccountHandler acctHandler;
	private ApiController api;
	private List<String> histObjTime = new ArrayList<String>();
	private List<Double> histObjVal = new ArrayList<Double>();
	private double lastPnLValue=0;
	
	public Relay(){
		try{

			conHandler = new ConnectionHandler();
			api = new ApiController(conHandler,new LoggerIn(),new LoggerOut());
			api.connect("localhost", 4002, 3, null);

			while(!conHandler.isConnected()){
				sendMessage("waiting to be connected");
				Thread.sleep(5000);		
			}
			
			acctHandler = new AccountHandler();
			api.reqAccountUpdates(true,actNo,acctHandler);
			
		}catch(Exception e){
			sendMessage(e.getMessage());
		}
	}
	
	@Override
	public void run() {
		try{
			while(true){	
				Map<String, String> map = acctHandler.getActUpdate();
				processHistorical(map);
				
				Long time = System.currentTimeMillis();
				map.put("RefreshedAt ", time.toString());
				for(Map.Entry<String, String> entry : map.entrySet()){
					sendMessage(entry.getKey() + " : " + entry.getValue());
				}
				
				Thread.sleep(15000);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	private void processHistorical(Map<String, String> map) {
		if(map.containsKey("totalPnL")){
			Double val = Double.parseDouble(map.get("totalPnL"));
			if(Math.abs(Math.abs(val)-Math.abs(lastPnLValue))>50){
				lastPnLValue = val;
				histObjTime.add(getDoubleTime().toString());
				histObjVal.add(val);
			}
		}
		sendMessage("HistoricalTotalPnL : " + histObjTime.toString()  + " : " + histObjVal.toString());
	}

	public void addEndPoint(Endpoints endPoint){
		endPoints.add(endPoint);
	}
	
	public void removeEndPoint(Endpoints endPoint){
		if(endPoints.contains(endPoints))
			endPoints.remove(endPoint);
	}
	
	public boolean isConnected(){
		return conHandler.isConnected();
	}
	
	public void sendMessage(String msg){
		
		try {
			for (Endpoints chatEndpoint : endPoints) {
				Message message = new Message();		        
		        message.setFrom("Server");
		        message.setContent(msg);
		        chatEndpoint.broadcast(message);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public Double getDoubleTime(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		calendar.setTimeInMillis(System.currentTimeMillis());
		Integer hr = calendar.get(Calendar.HOUR_OF_DAY);
        Integer min = calendar.get(Calendar.MINUTE);
        
        return Double.parseDouble(hr + "." + min);
	}
	
}
