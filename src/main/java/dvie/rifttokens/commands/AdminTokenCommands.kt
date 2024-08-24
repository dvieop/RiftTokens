package dvie.rifttokens.commands

import dev.splityosis.commandsystem.SYSCommand
import dev.splityosis.commandsystem.SYSCommandBranch
import dev.splityosis.commandsystem.arguments.IntegerArgument
import dev.splityosis.commandsystem.arguments.PlayerArgument
import dvie.kfeatures.modules.util.Util
import dvie.rifttokens.RiftTokens
import org.bukkit.Bukkit

class AdminTokenCommands(vararg names: String?) : SYSCommandBranch("rtokens") {

    init {
        setPermission("tokens.admin")


        addCommand(
            SYSCommand("addtokens")
            .setPermission("tokens.addtokens")
            .setUsage("/rtokens addtokens <player> <amount>")
            .setArguments(PlayerArgument(), IntegerArgument())
            .executes { sender, args ->
                val target = Bukkit.getPlayer(args[0] as String)
                val amount: Int = args.get(1).toInt()
                RiftTokens.data.addAmount(target!!.uniqueId, amount)
                Util.sendMessage(sender, RiftTokens.configManager.config!!.addToken.replace("%player%", target.name).replace("%amount%", Util.formatInt(amount)))
            })

        addCommand(
            SYSCommand("removetokens")
            .setPermission("tokens.removetokens")
            .setUsage("/rtokens removetokens <player> <amount>")
            .setArguments(PlayerArgument(), IntegerArgument())
            .executes { sender, args ->
                val target = Bukkit.getPlayer(args[0] as String)
                val amount: Int = args.get(1).toInt()
                RiftTokens.data.removeAmount(target!!.uniqueId, amount)
                Util.sendMessage(sender, RiftTokens.configManager.config!!.removeToken.replace("%player%", target.name).replace("%amount%", Util.formatInt(amount)))
            })

        addCommand(
            SYSCommand("settokens")
            .setPermission("tokens.settokens")
            .setUsage("/rtokens settokens <player> <amount>")
            .setArguments(PlayerArgument(), IntegerArgument())
            .executes { sender, args ->
                val target = Bukkit.getPlayer(args[0] as String)
                val amount: Int = args.get(1).toInt()
                RiftTokens.data.setAmount(target!!.uniqueId, amount)
                Util.sendMessage(sender, RiftTokens.configManager.config!!.setToken.replace("%player%", target.name).replace("%amount%", Util.formatInt(amount)))
            })

        addCommand(
            SYSCommand("reset")
            .setPermission("tokens.reset")
            .setUsage("/rtokens reset <player>")
            .setArguments(PlayerArgument())
            .executes { sender, args ->
                val target = Bukkit.getPlayer(args[0] as String)
                RiftTokens.data.setAmount(target!!.uniqueId, 0)
                Util.sendMessage(sender, RiftTokens.configManager.config!!.resetTokens.replace("%player%", target.name))
            })

        addCommand(
            SYSCommand("resetall")
            .setPermission("tokens.resetall")
            .setUsage("/rtokens resetall")
            .executes { sender, args ->
                RiftTokens.data.clear()
                Util.sendMessage(sender, "&aReset all tokens")
                Util.sendMessage(sender, RiftTokens.configManager.config!!.resetAll)
            })

        addCommand(
            SYSCommand("reload")
            .setPermission("tokens.reload")
            .setUsage("/rtokens reload")
            .executes { sender, args ->
                RiftTokens.configManager.reload()
                Util.sendMessage(sender, RiftTokens.configManager.config!!.reload)
            })
    }
}