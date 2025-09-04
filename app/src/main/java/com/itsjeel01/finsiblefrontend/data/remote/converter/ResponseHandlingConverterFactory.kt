package com.itsjeel01.finsiblefrontend.data.remote.converter

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/** Generic custom converter factory that wraps existing converter and adds custom processing. */
class ResponseHandlingConverterFactory(
    private val factory: Converter.Factory,
    private val responseHandler: ResponseHandler? = null
) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val delegateConverter = factory.responseBodyConverter(type, annotations, retrofit)
            ?: return null

        return ResponseConverter(delegateConverter, responseHandler)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        return factory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
    }

    companion object {
        fun create(
            delegate: Converter.Factory,
            processor: ResponseHandler? = null
        ): ResponseHandlingConverterFactory {
            return ResponseHandlingConverterFactory(delegate, processor)
        }
    }
}