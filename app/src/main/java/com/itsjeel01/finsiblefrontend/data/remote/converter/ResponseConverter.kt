package com.itsjeel01.finsiblefrontend.data.remote.converter

import okhttp3.ResponseBody
import retrofit2.Converter

/** Generic response converter that applies custom processing after deserialization. */
class ResponseConverter<T>(
    private val delegate: Converter<ResponseBody, T>,
    private val processor: ResponseHandler?
) : Converter<ResponseBody, T> {

    override fun convert(value: ResponseBody): T? {
        val result = delegate.convert(value)

        processor?.process(result)

        return result
    }
}