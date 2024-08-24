package dvie.rifttokens

import dvie.kfeatures.modules.tokens.commands.TokenCommands
import dvie.kfeatures.modules.tokens.config.ConfigManager
import dvie.kfeatures.modules.tokens.config.data.DataFile
import dvie.kfeatures.modules.tokens.placeholders.TokenPlaceHolders
import dvie.rifttokens.commands.AdminTokenCommands
import dvie.rifttokens.listeners.JoinListener
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class RiftTokens : JavaPlugin() {

    companion object {
        lateinit var instance: RiftTokens
        lateinit var data: DataFile
        lateinit var configManager: ConfigManager
        private set
    }

    override fun onEnable() {
        instance = this
        data = DataFile(File(dataFolder, "Tokens.yml"), this)
        data.startAutoSave(20 * 60 * 5)
        configManager = ConfigManager(this)
        configManager.loadConfigs()
        TokenPlaceHolders().register()
        TokenCommands().registerCommandBranch(this)
        AdminTokenCommands().registerCommandBranch(this)
        server.pluginManager.registerEvents(JoinListener(), this)
    }

    override fun onDisable() {
        configManager.saveConfigs()
    }
}
