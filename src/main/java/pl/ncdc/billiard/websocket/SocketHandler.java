package pl.ncdc.billiard.websocket;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;

import pl.ncdc.billiard.BilliardTable;

//https://www.devglan.com/spring-boot/spring-websocket-integration-example-without-stomp
//https://keyholesoftware.com/2017/04/10/websockets-with-spring-boot/
@Component
public class SocketHandler extends TextWebSocketHandler {

	static List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("conection established");
		// the messages will be broadcasted to all users.
		sessions.add(session);
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("conection closed");
		sessions.remove(session);
	}

//	@Override
//	public void handleTextMessage(WebSocketSession session, TextMessage message)
//			throws InterruptedException, IOException {
//		System.out.println("wiadomosc " + message.getPayload());
//		session.sendMessage(new TextMessage("Hello " + "123" + " !"));
//	}

	public void sendToAll(BilliardTable table) {
		for (WebSocketSession webSocketSession : sessions) {
			String serialisedTable = new Gson().toJson(table);
			try {
				webSocketSession.sendMessage(new TextMessage(serialisedTable));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}