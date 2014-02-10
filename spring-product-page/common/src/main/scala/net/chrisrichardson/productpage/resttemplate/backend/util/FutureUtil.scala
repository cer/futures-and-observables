package net.chrisrichardson.productpage.resttemplate.backend.util

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.concurrent.duration.Duration
import org.jboss.netty.util.{TimerTask, HashedWheelTimer}
import java.util.concurrent.{TimeoutException, TimeUnit}
import org.jboss.netty.util.Timeout

/**
 * From http://eng.42go.com/future-safefuture-timeout-cancelable/
 */

object FutureUtil {

  implicit class KestrelCombinator[A](val a: A) extends AnyVal {
    def withSideEffect(fun: A => Unit): A = { fun(a); a }
    def tap(fun: A => Unit): A = withSideEffect(fun)
  }

  object TimeoutFuture {
    def apply[T](future: Future[T], onTimeout: => Unit = Unit)(implicit ec: ExecutionContext, after: Duration): Future[T] = {
      val timer = new HashedWheelTimer(10, TimeUnit.MILLISECONDS)
      val promise = Promise[T]()
      val timeout = timer.newTimeout(new TimerTask {
        def run(timeout: Timeout){
          onTimeout
          promise.failure(new TimeoutException(s"Future timed out after ${after.toMillis}ms"))
        }
      }, after.toNanos, TimeUnit.NANOSECONDS)
      // does not cancel future, only resolves result in approx duration. Your future may still be running!
      Future.firstCompletedOf(Seq(future, promise.future)).tap(_.onComplete { case result => timeout.cancel() })
    }
  }
}
