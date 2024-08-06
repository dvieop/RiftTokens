package dvie.kfeatures.modules.tokens.config.data

import dvie.kfeatures.modules.util.Util
import org.bukkit.Bukkit
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.io.File
import java.io.IOException
import java.util.*

class DataFile(private val file: File, val plugin: JavaPlugin) {
    var config: FileConfiguration? = null
    private var autoSaveCode = 0.0

    init {
        try {
            if (!file.parentFile.exists()) file.parentFile.mkdirs()
            if (!file.exists()) file.createNewFile()
            config = YamlConfiguration()
            (config as YamlConfiguration).load(file)
        } catch (e: IOException) {
            plugin.logger.severe("Failed to create or load data file: ${e.message}")
            throw RuntimeException(e)
        } catch (e: InvalidConfigurationException) {
            plugin.logger.severe("Failed to load data file configuration: ${e.message}")
            throw RuntimeException(e)
        }
    }

    fun save() {
        try {
            config!!.save(file)
        } catch (e: IOException) {
            plugin.logger.severe("Failed to save data file: ${e.message}")
            throw RuntimeException(e)
        }
    }

    fun startAutoSave(period: Long) {
        if (isAutoSave) return
        autoSaveCode = Math.random()
        object : BukkitRunnable() {
            private val sessionCode = autoSaveCode
            override fun run() {
                if (sessionCode != autoSaveCode) {
                    cancel()
                    return
                }
                save()
            }
        }.runTaskTimerAsynchronously(plugin, period, period)
    }

    fun stopAutoSave() {
        autoSaveCode = 0.0
    }

    val isAutoSave: Boolean
        get() = autoSaveCode != 0.0

    fun reload() {
        try {
            config!!.load(file)
        } catch (e: IOException) {
            plugin.logger.severe("Failed to reload data file: ${e.message}")
            throw RuntimeException(e)
        } catch (e: InvalidConfigurationException) {
            plugin.logger.severe("Invalid configuration in data file: ${e.message}")
            throw RuntimeException(e)
        }
    }

    fun set(playerId: UUID, path: String, value: Any?) {
        config!!["players.${playerId}.$path"] = value
    }

    fun getAmount(playerId: UUID): Int {
        return config!!.getInt("players.${playerId}.amount", 0)
    }

    fun setAmount(playerId: UUID, amount: Int) {
        config!!["players.${playerId}.amount"] = amount
        save()
    }

    fun addAmount(playerId: UUID, amount: Int) {
        val total = getAmount(playerId)
        setAmount(playerId, total + amount)
    }

    fun clearAmount(playerId: UUID) {
        setAmount(playerId, 0)
    }

    fun removeAmount(playerId: UUID, amount: Int) {
        val total = getAmount(playerId)
        setAmount(playerId, total - amount)
    }

    fun clear() {
        config!!.set("players", null)
        save()
    }

    fun getTop(): List<Pair<String, String>> {
        val top = mutableListOf<Pair<String, String>>()
        config!!.getConfigurationSection("players")?.getKeys(false)?.forEach { playerId ->
            val uuid = UUID.fromString(playerId)
            val playerName = Bukkit.getOfflinePlayer(uuid).name ?: "Unknown"
            val formattedAmount = Util.formatInt(getAmount(uuid))
            top.add(Pair(playerName, formattedAmount))
        }
        return top.sortedByDescending { it.second.replace(",", "").toInt() }.take(10)
    }
}
