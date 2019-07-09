package cc.shallow.kotlin_vertx_examples

import com.google.inject.Injector
import io.vertx.core.Verticle
import io.vertx.core.VertxOptions
import io.vertx.core.spi.VerticleFactory
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

private val logger = LoggerFactory.getLogger("Verticles")

fun options(eventLoops: Int = 2 * Runtime.getRuntime().availableProcessors()): VertxOptions {
  return VertxOptions()
    .setEventLoopPoolSize(eventLoops)
    .setWarningExceptionTime(1)
    .setMaxEventLoopExecuteTime(TimeUnit.SECONDS.toNanos(1))
    .setMaxWorkerExecuteTime(TimeUnit.SECONDS.toNanos(1))
    .setBlockedThreadCheckInterval(TimeUnit.SECONDS.toMillis(1))
}

class GuiceVerticleFactory(private val injector:Injector):VerticleFactory{
  companion object {
    private const val prefix: String = "cc.shallow"
  }
  override fun prefix(): String = prefix

  override fun createVerticle(verticleName: String, classLoader: ClassLoader): Verticle {
    val verticle = VerticleFactory.removePrefix(verticleName)
    return injector.getInstance(classLoader.loadClass(verticle)) as Verticle
  }
}
