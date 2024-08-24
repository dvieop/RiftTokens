package dvie.rifttokens.listeners

import dvie.rifttokens.RiftTokens
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        val uuid = player.uniqueId
        if (!player.hasPlayedBefore()) {
            RiftTokens.data.setAmount(uuid, RiftTokens.configManager.config!!.loginAmount)
        }
    }
}