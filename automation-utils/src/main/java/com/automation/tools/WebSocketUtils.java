package com.automation.tools;


import com.automation.handler.TcWebSocket;
import net.thucydides.core.steps.StepEventBus;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.URI;


public class WebSocketUtils {
  public static String getBase64(String receivedMsg) {
    String[] strArray = receivedMsg.split("\\|");
    if (strArray.length > 0) {
      return strArray[strArray.length - 1];
    } else {
      return null;
    }
  }

  public static boolean connectToWS(WebSocketClient client, TcWebSocket socket, String uri) {

    try {
      client.start();
      URI socketUri = new URI(uri);
      ClientUpgradeRequest request = new ClientUpgradeRequest();
      //request.setSubProtocols(Lists.newArrayList(getAuthHeaderValue()));
      client.connect(socket, socketUri, request);
      socket.awaitConnection(2);
      return true;
    } catch (Exception e) {
      StepEventBus.getEventBus().testFailed(new AssertionError(e.getMessage()));
    }
    return false;
  }
}
