package com.itsjeel01.finsiblefrontend.common.logging

/** Log domains for categorizing log messages. */
enum class LogDomain(val tag: String) {
    NETWORK("Finsible:Network"),
    DATABASE("Finsible:Database"),
    AUTH("Finsible:Auth"),
    SYNC("Finsible:Sync"),
    CACHE("Finsible:Cache"),
    UI("Finsible:UI"),
    APP("Finsible:App")
}