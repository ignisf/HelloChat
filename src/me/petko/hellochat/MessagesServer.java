package me.petko.hellochat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.json.*;

import me.petko.hellochat.model.Message;

@ServerEndpoint(value = "/chat")
public class MessagesServer {
    private static final Logger LOGGER =
        Logger.getLogger(MessagesServer.class.getName());

    private static Set<Session> clients =
        Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session session) {
        clients.add(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
    	JSONObject jsonMessage = new JSONObject(message);

        Context context;
        DataSource dataSource;
        Connection connection = null;

        try {
            context = new InitialContext();
            dataSource = (DataSource)context.lookup("java:comp/env/jdbc/postgres");
            connection = dataSource.getConnection();

            Message messageObject = new Message(new Date(), jsonMessage.getString("author"), jsonMessage.getString("text"));
            messageObject.save(connection);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error: {0}",
                       new Object[] {e.getMessage()});
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error: {0}",
                               new Object[] {e.getMessage()});
                }
            }
        }

        synchronized(clients){
            // Iterate over the connected sessions
            // and broadcast the received message
            for(Session client : clients){
            	if (!client.equals(session)){
                    try {
                        client.getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        LOGGER.log(Level.SEVERE, "Error: {0}",
                                   new Object[] {e.getMessage()});
                    }
            	}
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        clients.remove(session);
    }

    @OnError
    public void onError(Throwable exception, Session session) {
        clients.remove(session);
    }
}
