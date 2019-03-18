package com.iknow.okretrofit.utils

import com.google.gson.*
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

import java.io.IOException
import java.lang.reflect.Type
import java.util.Date

/**
 * author : J.Chou
 * e-mail : who_know_me@163.com
 * time   : 2017/06/14/10:09 AM
 * desc   :
 * version: 1.0
 */
object JSONUtil {

    val defaultGson: Gson
        get() {
            val ser =
                JsonSerializer<Date> { src, typeOfSrc, context -> if (src == null) null else JsonPrimitive(src.time) }
            val deser =
                JsonDeserializer<Date> { json, typeOfT, context -> if (json == null) null else Date(json.asLong) }

            return GsonBuilder().registerTypeAdapter(Date::class.java, ser)
                .registerTypeAdapter(Date::class.java, deser)
                .registerTypeAdapter(Boolean::class.java, booleanAsIntAdapter)
                .registerTypeAdapter(Boolean::class.javaPrimitiveType, booleanAsIntAdapter)
                .create()
        }

    val defaultGsonBuilder: GsonBuilder
        get() {
            val ser =
                JsonSerializer<Date> { src, typeOfSrc, context -> if (src == null) null else JsonPrimitive(src.time) }
            val deser =
                JsonDeserializer<Date> { json, typeOfT, context -> if (json == null) null else Date(json.asLong) }
            return GsonBuilder()
                .registerTypeAdapter(Date::class.java, ser)
                .registerTypeAdapter(Date::class.java, deser)
                .registerTypeAdapter(Boolean::class.java, booleanAsIntAdapter)
                .registerTypeAdapter(Boolean::class.javaPrimitiveType, booleanAsIntAdapter)
        }

    private val booleanAsIntAdapter = object : TypeAdapter<Boolean>() {
        @Throws(IOException::class)
        override fun write(out: JsonWriter, value: Boolean?) {
            if (value == null) {
                out.nullValue()
            } else {
                out.value(value)
            }
        }

        @Throws(IOException::class)
        override fun read(`in`: JsonReader): Boolean? {
            val peek = `in`.peek()
            when (peek) {
                JsonToken.BOOLEAN -> return `in`.nextBoolean()
                JsonToken.NULL -> {
                    `in`.nextNull()
                    return null
                }
                JsonToken.NUMBER -> return `in`.nextInt() != 0
                JsonToken.STRING -> return `in`.nextString().equals("1", ignoreCase = true)
                else -> throw IllegalStateException("Expected BOOLEAN or NUMBER but was $peek")
            }
        }
    }

    /**
     * 对象转换成json字符串
     */
    fun toJson(obj: Any): String? {
        try {
            val gson = Gson()
            return gson.toJson(obj)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return null
    }

    fun toJson(obj: Any, typeOfSrc: Type): String? {
        try {
            val gson = Gson()
            return gson.toJson(obj, typeOfSrc)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * json字符串转成对象
     */
    fun <T> fromJson(str: String, type: Type): T? {
        try {
            val gson = Gson()
            return gson.fromJson<T>(str, type)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * json字符串转成对象
     */
    fun <T> fromJson(str: String, clzz: Class<T>): T? {
        try {
            val gson = Gson()
            return gson.fromJson(str, clzz)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return null
    }

    fun <T> fromJson(jsonElement: JsonElement, clzz: Class<T>): T? {
        try {
            return Gson().fromJson(jsonElement, clzz)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return null
    }
}
