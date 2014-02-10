package net.chrisrichardson.productpage.resttemplate.backend.config

import org.springframework.context.annotation.{ComponentScan, Configuration, Bean}
import akka.actor.ActorSystem
import org.springframework.scheduling.annotation.EnableAsync

@Configuration
@EnableAsync
@ComponentScan(Array("net.chrisrichardson.productpage.resttemplate.backend"))
class ApplicationConfiguration {

    @Bean
    def actorSystem = ActorSystem()
}
