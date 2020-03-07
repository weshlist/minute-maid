package io.weshlist.minutemaid.aspect

import io.weshlist.minutemaid.result.Result
import mu.KotlinLogging
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

// Aop not works on annotation, why?
@Aspect
@Component
class PrintAccessLog {

	@Around(value = "@annotation(io.weshlist.minutemaid.utils.PrintLog)")
	fun proceed(joinPoint: ProceedingJoinPoint): Any? {
		val log = KotlinLogging.logger { }

		val args = if (JoinPoint.METHOD_EXECUTION == joinPoint.kind) joinPoint.args else null
		val handlerName = joinPoint.target.javaClass.simpleName

		val output = lazy {
			joinPoint.proceed() as? Result<*, *>
		}

		val elapsed: Long = measureTimeMillis { output }

		log.info {"handler: $handlerName, args: $args, elapsed: $elapsed ms"}

		return output
	}
}