package net.chrisrichardson.productpage.resttemplate.web.tests

import net.chrisrichardson.productpage.webtestutil.JettyLauncher
import org.junit.{After, Before}
import net.chrisrichardson.productpage.resttemplate.backend.util.RestTemplateFactory
import net.chrisrichardson.productpage.resttemplate.backend.dtos.ProductDetails
import org.slf4j.LoggerFactory

trait AbstractWebTest {

  val logger = LoggerFactory.getLogger(this.getClass)
  var jetty : JettyLauncher = _

  @Before
  def initializeJetty() {
    jetty = new JettyLauncher
    jetty.setContextPath("/webapp")
    jetty.setPort(-1)
    jetty.setSrcWebApp("src/main/webapp")
    jetty.start()

    System.setProperty("net.chrisrichardson.resttemplate.baseurl", jetty.getBaseUrl)
    logger.debug("Set property: " + jetty.getBaseUrl)
  }

  @After
  def shutdownJetty() {
    jetty.stop()
  }

  val restTemplate = RestTemplateFactory.makeRestTemplate()

  def assertSuccessful(x : ProductDetails) {
    x match {
      case ProductDetails(_, _, _) =>
    }
  }

}
