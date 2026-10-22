package com.example.apiflags

import android.content.Context
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import okhttp3.OkHttpClient

object FlagImageLoader {

    fun create(context: Context): ImageLoader {

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->

                val request = chain.request()
                    .newBuilder()
                    .header(
                        "User-Agent",
                        "WordFlags/1.0 (Android)"
                    )
                    .build()

                chain.proceed(request)
            }
            .build()

        return ImageLoader.Builder(context)
            .components {
                add(
                    OkHttpNetworkFetcherFactory(
                        callFactory = okHttpClient
                    )
                )
            }
            .build()
    }
}