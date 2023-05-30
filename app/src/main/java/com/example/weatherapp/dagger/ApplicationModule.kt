package com.example.weatherapp.dagger

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.weatherapp.BaseApplication
import com.example.weatherapp.retrofit.ApiInterface
import com.example.weatherapp.storage.AppPref
import com.example.weatherapp.storage.USER_PREFERENCE_NAME
import com.example.weatherapp.storage.database.AppDB
import com.example.weatherapp.utils.BASE_URL
import com.example.weatherapp.utils.DATABASE_NAME
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class ApplicationModule(private val baseApp: BaseApplication) {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return baseApp
    }

    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return baseApp.applicationContext
    }

    @Provides
    @Singleton
    fun provideAppPref(context: Context): AppPref {
        val sharedPreferences =
            context.getSharedPreferences(USER_PREFERENCE_NAME, Activity.MODE_PRIVATE)
        return AppPref(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideDBHelper(context: Context): AppDB {
        return Room.databaseBuilder(context, AppDB::class.java, DATABASE_NAME)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    internal fun provideAppApi(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    internal fun provideRetrofitInterface(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    internal fun provideHttpClient(): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
        builder.addNetworkInterceptor(provideNetworkInterceptor())
        builder.connectTimeout(3, TimeUnit.MINUTES)
        builder.readTimeout(3, TimeUnit.MINUTES)
        return builder.build()
    }

    @Provides
    @Singleton
    internal fun provideNetworkInterceptor(): Interceptor {

        return Interceptor(fun(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("Accept-Encoding", "gzip, deflate")
                .build()
            return chain.proceed(request)
        })
    }

}