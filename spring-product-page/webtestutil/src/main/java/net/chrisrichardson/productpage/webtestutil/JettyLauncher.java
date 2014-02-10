package net.chrisrichardson.productpage.webtestutil;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.Assert;

public class JettyLauncher {
  Log logger = LogFactory.getLog(getClass());

  private Server server;
  private String contextPath;
  private int port;
  private File srcWebApp;
  private int actualPort;

  public void start() {
    actualPort = PortUtil.allocatePortIfRequired(port);
    server = new Server(actualPort);
    WebAppContext context = new WebAppContext();

    context.setContextPath(contextPath);
    if (srcWebApp.isDirectory()) {
      context.setDescriptor(srcWebApp + "/WEB-INF/web.xml");
      context.setResourceBase(srcWebApp.getAbsolutePath());
      context.setParentLoaderPriority(true);
    } else {
      context.setWar(srcWebApp.getAbsolutePath());
    }

    server.setHandler(context);
    
    logger.info("Starting jetty on " + actualPort);

    try {
      server.start();
      // server.join();
    } catch (Exception e) {
      logger.error("Error starting", e);
      throw new RuntimeException(e);
    }
    logger.info("jetty started");
  }

  public void setContextPath(String contextPath) {
    this.contextPath = contextPath;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public void setSrcWebApp(String srcWebApp) {
    this.srcWebApp = new File(srcWebApp);
    Assert.assertTrue("Should be directory or .war: " + srcWebApp, this.srcWebApp.isDirectory() || this.srcWebApp.isFile() && srcWebApp.endsWith(".war"));
  }

  public void stop() {
    try {
      server.stop();
    } catch (Exception e) {
      throw new RuntimeException("exception thrown when stopping", e);
    }
  }

  public int getActualPort() {
    return actualPort;
  }

  public void main(String[] args) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("implement me");
  }

  public String getBaseUrl() {
    return String.format("http://localhost:%d%s", actualPort, contextPath);
  }

  public String makeUrl(String suffix) {
    String s = String.format("http://localhost:%d%s/%s",
            actualPort, contextPath, suffix);
    return s;
  }
}
