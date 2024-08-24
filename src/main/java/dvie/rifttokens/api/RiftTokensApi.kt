package dvie.rifttokens.api

import dvie.rifttokens.RiftTokens
import java.util.*

object RiftTokensApi {

    fun getPlayerTokens(uuid: UUID?): Int {
        return RiftTokens.data.getAmount(uuid!!)
    }

    fun setPlayerTokens(uuid: UUID?, amount: Int) {
        RiftTokens.data.setAmount(uuid!!, amount)
    }

    fun addPlayerTokens(uuid: UUID?, amount: Int) {
        return RiftTokens.data.addAmount(uuid!!, amount)
    }

    fun removePlayerTokens(uuid: UUID?, amount: Int) {
        return RiftTokens.data.removeAmount(uuid!!, amount)
    }

    fun resetPlayerTokens(uuid: UUID?) {
        RiftTokens.data.clearAmount(uuid!!)
    }

    fun save(uuid: UUID?) {
        RiftTokens.data.save()
    }

    fun clear() {
        RiftTokens.data.clear()
    }
}
