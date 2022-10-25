package com.automation.handler;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.automation.tools.WebSocketUtils;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

@org.eclipse.jetty.websocket.api.annotations.WebSocket(maxTextMessageSize = 1 * 1024)
public class TcWebSocket {

  private volatile Session session;
  private volatile Throwable error;
  private ConcurrentLinkedQueue<String> messages = new ConcurrentLinkedQueue<>();
  private final CountDownLatch connect = new CountDownLatch(1);
  private final Semaphore messagesArrived = new Semaphore(0);

  public TcWebSocket() {
    // init
  }

  @OnWebSocketConnect
  public void onConnect(Session session) {
    this.session = session;
    connect.countDown();
  }

  public void send(String msg) throws IOException {
    session.getRemote().sendString(msg);
  }

  @OnWebSocketError
  public void methodName(Session session, Throwable error) {
    this.error = error;
    connect.countDown();
  }

  @OnWebSocketMessage
  public void onMessage(String msg) throws IOException {
    messages.add(WebSocketUtils.getBase64(msg));
    messagesArrived.release();
  }

  public void awaitConnection(long secondsToWait) throws InterruptedException, TimeoutException {
    if (!connect.await(secondsToWait, TimeUnit.SECONDS)) {
      throw new TimeoutException("Failure while waiting for connection to be established.");
    }

    if (error != null) {
      throw Throwables.propagate(error);
    }
  }

  public List<String> awaitCompletion(int messagesExpected, long secondsToWait) throws InterruptedException {
    if (!messagesArrived.tryAcquire(messagesExpected, secondsToWait, TimeUnit.SECONDS)) {
      throw new AssertionError(String.format("Expected %d messages but didn't receive them within timeout. Total messages received %d.",
        messagesExpected, messagesArrived.availablePermits()));
    }
    return Lists.newArrayList(messages);
  }

  public Session getSession() {
    return session;
  }
}
