package websockets.web;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import javax.websocket.*;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Scanner;

import org.glassfish.grizzly.Grizzly;
import websockets.service.Eliza;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.springframework.web.socket.CloseStatus;

import java.io.IOException;

public class ElizaWebSocketHandler extends AbstractWebSocketHandler {

    private static final Logger LOGGER = Grizzly.logger(ElizaWebSocketHandler.class);
    private Eliza eliza = new Eliza();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        session.sendMessage(new TextMessage("The doctor is in."));
        session.sendMessage(new TextMessage("What's on your mind?"));
        session.sendMessage(new TextMessage("---"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        Scanner currentLine = new Scanner(message.toString().toLowerCase());
        if (currentLine.findInLine("bye") == null) {
            LOGGER.info("Server recieved \"" + message + "\"");
            session.sendMessage(new TextMessage(eliza.respond(currentLine)));
            session.sendMessage(new TextMessage("---"));
        } else {
            session.sendMessage(new TextMessage("Alright then, goodbye!"));
            session.close();
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws IOException {
        System.out.println("POTATO ERROR NO DEBERIA PASAR");
    }
}