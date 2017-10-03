package de.eternalwings.awww.config

import com.github.sheigutn.pushbullet.Pushbullet
import de.eternalwings.awww.PushbulletSettings
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(PushbulletSettings::class)
class PushbulletConfig {

    @Autowired lateinit var pushbulletSettings: PushbulletSettings

    @Bean
    fun pushbullet(): Pushbullet {
        return Pushbullet(pushbulletSettings.key)
    }
}
