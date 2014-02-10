package net.chrisrichardson.productpage.webtestutil;

import java.io.IOException;
import java.net.ServerSocket;

public class PortUtil {

  public static int allocatePort() {
    try {
      ServerSocket s = new ServerSocket();
      s.bind(null);
      int port = s.getLocalPort();
      s.close();
      return port;
    } catch (IOException e) {
      throw new RuntimeException("Couldn't allocate port", e);
    }
  }

  public static int allocatePortIfRequired(int port) {
    return port == -1 ? allocatePort() : port;
  }


}
