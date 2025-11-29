package com.itsjeel01.finsiblefrontend.common.logging

import timber.log.Timber

/** Custom Timber tree that formats log tags with class name, method name, and line number. */
class DebugLogTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String {
        return "Finsible:${element.className.substringAfterLast('.')}.${element.methodName}:${element.lineNumber}"
    }
}