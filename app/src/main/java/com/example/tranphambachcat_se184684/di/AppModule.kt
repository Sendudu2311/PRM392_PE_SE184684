package com.example.tranphambachcat_se184684.di

import android.content.Context
import androidx.room.Room
import com.example.tranphambachcat_se184684.data.local.CourseDao
import com.example.tranphambachcat_se184684.data.local.CourseDatabase
import com.example.tranphambachcat_se184684.data.local.FavoriteDao
import com.example.tranphambachcat_se184684.data.remote.CourseApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            android.util.Log.d("API_LOG", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Smart DNS resolver with fallback for emulator compatibility
        val dns = object : okhttp3.Dns {
            override fun lookup(hostname: String): List<java.net.InetAddress> {
                // Try normal DNS resolution first
                return try {
                    val addresses = okhttp3.Dns.SYSTEM.lookup(hostname)
                    android.util.Log.d("DNS_RESOLVER", "✅ DNS resolved $hostname successfully")
                    addresses
                } catch (e: java.net.UnknownHostException) {
                    // Fallback to hardcoded IP for known hosts
                    android.util.Log.w("DNS_RESOLVER", "⚠️ DNS failed for $hostname, trying fallback...")
                    when (hostname) {
                        "skillverse.vn" -> {
                            android.util.Log.d("DNS_RESOLVER", "✅ Using fallback IP: 221.132.33.141")
                            listOf(java.net.InetAddress.getByAddress(
                                hostname,
                                byteArrayOf(221.toByte(), 132.toByte(), 33.toByte(), 141.toByte())
                            ))
                        }
                        else -> {
                            // Re-throw if no fallback available
                            android.util.Log.e("DNS_RESOLVER", "❌ No fallback for $hostname")
                            throw e
                        }
                    }
                }
            }
        }

        return OkHttpClient.Builder()
            .dns(dns)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideCourseApiService(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): CourseApiService {
        return Retrofit.Builder()
            .baseUrl(CourseApiService.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CourseApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCourseDatabase(
        @ApplicationContext context: Context
    ): CourseDatabase {
        return Room.databaseBuilder(
            context,
            CourseDatabase::class.java,
            CourseDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCourseDao(database: CourseDatabase): CourseDao {
        return database.courseDao()
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(database: CourseDatabase): FavoriteDao {
        return database.favoriteDao()
    }
}
