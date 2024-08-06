package dvie.kfeatures.modules.tokens.placeholders

import dvie.kfeatures.modules.util.Util
import dvie.rifttokens.RiftTokens
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer

class TokenPlaceHolders : PlaceholderExpansion() {


    override fun getIdentifier(): String {
        return "tokens"
    }

    override fun getAuthor(): String {
        return "dvie"
    }

    override fun getVersion(): String {
        return "1.0.0"
    }

    override fun onRequest(player: OfflinePlayer?, params: String): String? {
        if (player == null) return null

        return when (params) {
            "balance" -> Util.formatInt(RiftTokens.data.getAmount(player.uniqueId))
            else -> null
        }
    }
}