package dvie.kfeatures.modules.tokens.config

import de.exlll.configlib.YamlConfigurationProperties
import de.exlll.configlib.YamlConfigurations
import dvie.rifttokens.RiftTokens
import lombok.Getter
import java.io.File

@Getter
class ConfigManager(plugin: RiftTokens) {
    val plugin: RiftTokens = plugin
    var config: Config? = null


    init {
        loadConfigs()
    }

    fun loadConfigs() {
        val properties: YamlConfigurationProperties = YamlConfigurationProperties.newBuilder()
            .footer("Authors: dvie")
            .build()
        val settingsFile: File = File(plugin.getDataFolder(), "config.yml")

        config = YamlConfigurations.update(
            settingsFile.toPath(),
            Config::class.java,
            properties
        )

    }

    fun saveConfigs() {
        val properties: YamlConfigurationProperties = YamlConfigurationProperties.newBuilder()
            .footer("Authors: dvie")
            .build()
        YamlConfigurations.save(
            File(plugin.getDataFolder(), "config.yml").toPath(),
            Config::class.java, config, properties
        )

    }

    fun reload() {
        val properties: YamlConfigurationProperties = YamlConfigurationProperties.newBuilder()
            .footer("Authors: dvie")
            .build()
        config = YamlConfigurations.load(
            File(plugin.getDataFolder(), "config.yml").toPath(),
            Config::class.java, properties
        )
    }
}