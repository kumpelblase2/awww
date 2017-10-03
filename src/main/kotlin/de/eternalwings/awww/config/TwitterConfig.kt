package de.eternalwings.awww.config

import de.eternalwings.awww.TwitterSettings
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import twitter4j.TwitterStream
import twitter4j.TwitterStreamFactory
import twitter4j.conf.ConfigurationBuilder

@Configuration
@EnableConfigurationProperties(TwitterSettings::class)
class TwitterConfig {

    @Autowired lateinit var twitterSettings: TwitterSettings

    @Bean
    fun twitterStream(): TwitterStream = TwitterStreamFactory(twitterConfiguration()).instance

    @Bean
    fun twitterConfiguration(): twitter4j.conf.Configuration {
        return ConfigurationBuilder().setOAuthConsumerKey(twitterSettings.consumerKey).setOAuthConsumerSecret(
                twitterSettings.consumerSecret).setOAuthAccessToken(
                twitterSettings.accessToken).setOAuthAccessTokenSecret(twitterSettings.accessTokenSecret).build()
    }
}
