package com.baeldung.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.baeldung.ib.service.ConnectionHandler;
import com.baeldung.model.Message;

@ServerEndpoint(value = "/update/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class Endpoints {
    private Session session;
    
//    Logger logger = 
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    
    private static final Set<Endpoints> chatEndpoints = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();
    private static Relay relay;
    
    public Endpoints(){
    	System.out.println(" started*********");
    }
    
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
    	if(username.equals("simba1624") || username.equals("simba123")){
    		if(relay==null || !relay.isConnected()){
    			relay = new Relay();
    			new Thread(relay).start();
    		}
    		
    		relay.addEndPoint(this);
    		
            this.session = session;
            chatEndpoints.add(this);
            users.put(session.getId(), username);

            Message message = new Message();
            message.setFrom(username);
            message.setContent("Connected!");
            broadcast(message);
    	}else{
            Message message = new Message();
            message.setFrom(username);
            message.setContent("Bad user name!");
            broadcast(message);
    	}

    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException {
        message.setFrom(users.get(session.getId()));
        message.setContent("Nebsie");
        broadcast(message);
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        chatEndpoints.remove(this);
//        relay.removeEndPoint(this);
        Message message = new Message();
        message.setFrom(users.get(session.getId()));
        message.setContent("Disconnected!");
        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    public static void broadcast(Message message) throws IOException, EncodeException {
        chatEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote()
                        .sendObject(message);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    
}
