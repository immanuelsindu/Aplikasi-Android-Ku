package id.ac.ukdw.sub1_intermediate

import okhttp3.Request
import okhttp3.Interceptor

class MyInterceptor : Interceptor {
    override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response{

        val request : Request = chain.request()
            .newBuilder()
            .addHeader("Content-Type","multipart/form-data")
            .addHeader("Authorization","$LoginActivity.myToken")
            .build()
        return chain.proceed(request)
    }
}