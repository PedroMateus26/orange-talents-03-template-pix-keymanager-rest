package com.pedromateus.zup

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("com.pedromateus.zup")
		.start()
}

