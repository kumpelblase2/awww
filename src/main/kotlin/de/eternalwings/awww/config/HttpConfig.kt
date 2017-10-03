package de.eternalwings.awww.config

import de.eternalwings.awww.ImgurSettings
import de.eternalwings.awww.http.ImgurApi
import okhttp3.Interceptor.Chain
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Configuration
@EnableConfigurationProperties(ImgurSettings::class)
class HttpConfig {
    @Bean
    @Qualifier("imgur-api")
    fun imgurApiRetrofit(imgurSettings: ImgurSettings): Retrofit {
        return Retrofit.Builder().client(OkHttpClient.Builder().addInterceptor({ chain: Chain ->
            val newBuilder = chain.request().newBuilder()
            newBuilder.addHeader("Authorization", "Client-ID ${imgurSettings.clientId}")
            chain.proceed(newBuilder.build())
        }).build()).addConverterFactory(GsonConverterFactory.create()).baseUrl(ImgurApi.BASE_URL).build()
    }

    @Bean
    fun imgurApi(@Qualifier("imgur-api") retrofit: Retrofit): ImgurApi {
        return ImgurApi.create(retrofit)
    }
}
