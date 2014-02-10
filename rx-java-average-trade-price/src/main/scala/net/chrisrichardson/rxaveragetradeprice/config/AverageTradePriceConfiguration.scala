package net.chrisrichardson.rxaveragetradeprice.config

import org.springframework.context.annotation.{ImportResource, Configuration, ComponentScan}
import org.springframework.stereotype.Component

@Configuration
@ComponentScan(Array("net.chrisrichardson.rxaveragetradeprice"))
@ImportResource(Array("classpath:/appctx/messaging.xml"))
class AverageTradePriceConfiguration {

}
