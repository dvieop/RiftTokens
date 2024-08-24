package dvie.kfeatures.modules.tokens.commands

import dev.splityosis.commandsystem.SYSCommand
import dev.splityosis.commandsystem.SYSCommandBranch
import dvie.kfeatures.modules.util.Util
import dvie.rifttokens.RiftTokens

class TokenCommands(vararg names: String?) : SYSCommandBranch("tokens") {


    init {

        addCommand(
            SYSCommand("balance")
            .setPermission("tokens.balance")
            .setUsage("/tokens balance")
            .executesPlayer { player, args ->
                RiftTokens.configManager.config!!.balance.replace("%balance%", RiftTokens.data.getAmount(player.uniqueId).toString())
                Util.sendMessage(player, RiftTokens.configManager.config!!.balance.replace("%balance%", Util.formatInt(RiftTokens.data.getAmount(player.uniqueId))))
            })


        addCommand(
            SYSCommand("top")
            .setPermission("tokens.top")
            .setUsage("/tokens top")
            .executes { sender, args ->
                Util.sendMessage(sender, RiftTokens.configManager.config!!.top)
                RiftTokens.data.getTop().forEachIndexed { index, pair ->
                    val balance = pair.second.replace(",", "").toInt()
                    Util.sendMessage(sender, RiftTokens.configManager.config!!.topFormat.replace("%position%", (index + 1).toString()).replace("%player%", pair.first).replace("%balance%", Util.formatInt(balance)))
                }
            })
    }
}