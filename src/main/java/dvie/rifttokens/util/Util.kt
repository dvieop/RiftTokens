package dvie.kfeatures.modules.util

import net.md_5.bungee.api.ChatColor
import org.bukkit.*
import org.bukkit.command.CommandSender
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.Plugin
import java.io.File
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.floor
import kotlin.math.min

class Util() {
    fun getConfigFromPath(pluginName: String?, path: String): FileConfiguration {
        val plugin: Plugin? = Bukkit.getServer().pluginManager.getPlugin((pluginName)!!)
        val file: File = File(plugin!!.dataFolder, path.replace("/", System.lineSeparator()))
        return YamlConfiguration.loadConfiguration(file)
    }


    companion object {
        val LOG_PREFIX: String = "&8[&eRiftTokens&8]"

        fun sendMessage(to: CommandSender, message: String?) {
            to.sendMessage(colorize(message))
        }

        fun sendMessage(to: CommandSender, message: List<String>) {
            message.forEach(java.util.function.Consumer { s: String? ->
                sendMessage(to, s)
            })
        }

        fun broadcast(message: String) {
            for (onlinePlayer: Player in Bukkit.getOnlinePlayers()) sendMessage(onlinePlayer, message)
            log(message)
        }

        fun broadcast(message: List<String>) {
            for (onlinePlayer: Player in Bukkit.getOnlinePlayers()) sendMessage(onlinePlayer, message)
            log(message)
        }

        fun log(message: String) {
            sendMessage(Bukkit.getServer().consoleSender, LOG_PREFIX + " " + message)
        }

        fun addEnchantToItem(item: ItemStack, enchantment: Enchantment?, level: Int, hideEnchants: Boolean) {
            val meta: ItemMeta? = item.itemMeta
            meta!!.addEnchant((enchantment)!!, level, true)
            if (hideEnchants) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            item.setItemMeta(meta)
        }

        fun log(message: List<String>?) {
            val msg: MutableList<String> = ArrayList(message)
            if (!msg.isEmpty()) msg.set(0, LOG_PREFIX + " " + msg.get(0))
            sendMessage(Bukkit.getServer().consoleSender, msg)
        }

        private val HEX_PATTERN: Pattern = Pattern.compile("&(#\\w{6})")
        fun colorize(str: String?): String {
            val matcher: Matcher = HEX_PATTERN.matcher(ChatColor.translateAlternateColorCodes('&', str))
            val buffer: StringBuffer = StringBuffer()

            while (matcher.find()) matcher.appendReplacement(buffer, ChatColor.stripColor(matcher.group(1)).toString())

            return org.bukkit.ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString())
        }

        fun colorize(lst: List<String?>?): List<String>? {
            if (lst == null) return null
            val newList: MutableList<String> = ArrayList()
            lst.forEach(java.util.function.Consumer { s: String? ->
                newList.add(colorize(s))
            })
            return newList
        }

        fun createItemStack(material: Material?, amount: Int, name: String?, lore: List<String?>?): ItemStack {
            val itemStack: ItemStack = ItemStack((material)!!, amount)
            val meta: ItemMeta? = itemStack.itemMeta
            if (name != null) meta!!.setDisplayName(colorize(name))
            meta!!.lore = colorize(lore)
            itemStack.setItemMeta(meta)
            return itemStack
        }

        fun createItemStack(material: Material?, amount: Int, name: String?, vararg lore: String?): ItemStack {
            return createItemStack(material, amount, name, Arrays.asList(*lore))
        }

        fun createItemStack(material: Material?, amount: Int): ItemStack {
            return createItemStack(material, amount, null)
        }

        fun replaceList(lst: List<String>?, from: String?, to: String?): List<String>? {
            if (lst == null) return null
            val newList: MutableList<String> = ArrayList()
            lst.forEach(java.util.function.Consumer { s: String ->
                newList.add(
                    s.replace(
                        (from)!!,
                        (to)!!
                    )
                )
            })
            return newList
        }

        fun getItemFromSection(itemSection: ConfigurationSection?): ItemStack? {
            if (itemSection == null) return null
            val material: String? = itemSection.getString("material")
            var amount: Int = itemSection.getInt("amount")
            if (amount == 0) amount = 1
            val customName: String? = itemSection.getString("custom-name")
            val lore: List<String?> = itemSection.getStringList("custom-lore")

            val enchants: MutableMap<Enchantment?, Int> = HashMap()
            val enchantsSection: ConfigurationSection? = itemSection.getConfigurationSection("enchants")
            if (enchantsSection != null) for (key: String? in enchantsSection.getKeys(false)) enchants[Enchantment.getByName(
                key
            )] =
                enchantsSection.getInt(
                    (key)!!
                )

            val item: ItemStack =
                createItemStack(
                    Objects.requireNonNull(material)?.let { Material.getMaterial(it) },
                    amount,
                    customName,
                    lore
                )
            item.addUnsafeEnchantments(enchants)
            return item
        }

        fun setItemInConfig(section: ConfigurationSection, path: String, item: ItemStack) {
            var name: String? = null
            var lore: List<String?>? = null

            if (item.itemMeta != null) {
                if (item.itemMeta!!.hasDisplayName()) name = item.itemMeta!!.displayName
                if (item.itemMeta!!.hasLore()) lore = item.itemMeta!!.lore
            }

            section.set("$path.material", item.type.name)
            section.set("$path.custom-name", name)
            section.set("$path.custom-lore", lore)

            val enchs: Map<Enchantment, Int> = item.enchantments
            for (enchantment: Enchantment in enchs.keys) {
                section.set(path + ".enchants." + enchantment.name, enchs.get(enchantment))
            }
        }

        fun locationToString(location: Location): String {
            val world: String = location.world!!.name
            val x: String = location.blockX.toString()
            val y: String = location.blockY.toString()
            val z: String = location.blockZ.toString()
            return world + "_" + x + "_" + y + "_" + z
        }

        fun locationFromString(str: String): Location {
            val arr: Array<String> = str.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val world: World? = Bukkit.getWorld(arr.get(0))
            val x: Double = arr.get(1).toDouble()
            val y: Double = arr.get(2).toDouble()
            val z: Double = arr.get(3).toDouble()
            return Location(world, x, y, z)
        }

        fun getFormattedEntityName(entityType: EntityType): String {
            val builder: StringBuilder = StringBuilder()
            for (s: String in entityType.name.lowercase(Locale.getDefault()).split("_".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()) builder.append(s.substring(0, 1).uppercase(Locale.getDefault())).append(s.substring(1))
                .append(" ")
            return builder.substring(0, builder.length - 1)
        }

        fun createBar(
            completed: Int,
            total: Int,
            completedChar: String?,
            emptyChar: String?,
            maxedOutChar: String?
        ): String {
            val builder: StringBuilder = StringBuilder()
            if (completed == total) {
                for (i in 0 until total) builder.append(maxedOutChar)
                return colorize(builder.toString())
            }

            for (i in 0 until completed) builder.append(completedChar)

            for (i in 0 until (total - completed)) builder.append(emptyChar)
            return colorize(builder.toString())
        }

        fun replaceTextInItem(itemStack: ItemStack, from: String?, to: String?): ItemStack {
            val item: ItemStack = itemStack.clone()
            val meta: ItemMeta? = item.itemMeta
            if (meta == null) return item
            if (meta.hasDisplayName()) meta.setDisplayName(meta.displayName.replace((from)!!, (to)!!))
            if (meta.hasLore()) meta.lore = replaceList(meta.lore, from, to)
            item.setItemMeta(meta)
            return item
        }


        fun getItemName(item: ItemStack): String {
            var name: String
            if (item.itemMeta != null && item.itemMeta!!.hasDisplayName()) name = item.itemMeta!!.displayName
            else {
                name = item.type.name.lowercase(Locale.getDefault()).replace("_", " ")
                if (!(name == "TNT")) {
                    name = name.lowercase(Locale.getDefault())
                    val stringBuilder: StringBuilder = StringBuilder()
                    for (s: String in name.split(Pattern.quote(" ").toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()) {
                        stringBuilder.append(s.get(0).uppercaseChar()).append(s.substring(1)).append(" ")
                    }
                    name = stringBuilder.substring(0, stringBuilder.length - 1)
                }
            }
            return name
        }

        fun getLore(itemStack: ItemStack): List<String>? {
            if (itemStack.itemMeta!!.hasLore()) {
                return itemStack.itemMeta!!.lore
            }
            return ArrayList()
        }

        fun formatDouble(num: Double): String {
            if (num == floor(num)) return DecimalFormat("###,###").format(num)
            val s: String = DecimalFormat("###,###.##").format(num)
            if (s.split(Pattern.quote(".").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    .get(1).length == 1
            ) return s + "0"
            return s
        }

        fun sendTitle(player: Player, header: String?, footer: String?) {
            player.sendTitle(colorize(header), colorize(footer))
        }

        fun formatInt(num: Int): String {
            return DecimalFormat("###,###").format(num.toLong())
        }

        fun giveItemsToPlayer(player: Player, itemStack: ItemStack) {
            val maxStackSize: Int = itemStack.maxStackSize
            var remainingAmount: Int = itemStack.amount

            while (remainingAmount > 0) {
                val giveAmount: Int = min(remainingAmount.toDouble(), maxStackSize.toDouble()).toInt()
                val giveItem: ItemStack = ItemStack(itemStack)
                giveItem.amount = giveAmount

                val leftovers: HashMap<Int, ItemStack> = player.inventory.addItem(giveItem)
                if (!leftovers.isEmpty()) {
                    // If there are leftovers, drop them on the floor
                    val world: World = player.world
                    val playerLocation: Location = player.location
                    for (leftover: ItemStack? in leftovers.values) {
                        world.dropItemNaturally(playerLocation, (leftover)!!)
                    }
                }

                remainingAmount -= giveAmount
            }
        }

        fun formatTime(milliseconds: Long): String {
            val totalSeconds: Int = (milliseconds / 1000).toInt()
            val minutes: Int = totalSeconds / 60
            val seconds: Int = totalSeconds % 60

            val m: String
            if (minutes == 0) m = "00"
            else if (minutes < 10) m = "0$minutes"
            else m = minutes.toString()

            val s: String
            if (seconds == 0) s = "00"
            else if (seconds < 10) s = "0$seconds"
            else s = seconds.toString()

            return "$m:$s"
        }

        fun isMaterial(itemStack: ItemStack?, material: Material): Boolean {
            if (itemStack != null && (itemStack.type == material)) {
                return true
            }
            return false
        }

        fun isNotNullOrAir(itemStack: ItemStack?): Boolean {
            if (itemStack != null && !(itemStack.type == Material.AIR)) {
                return true
            }
            return false
        }

        fun isNullOrAir(itemStack: ItemStack?): Boolean {
            if (itemStack != null && (itemStack.type == Material.AIR)) {
                return true
            }
            if (itemStack == null) {
                return true
            }
            return false
        }

        fun getRandomIntBetween(min: Double, max: Double): Int {
            if (min >= max) {
                throw IllegalArgumentException("Min value must be less than max value.")
            }

            val random: Random = Random()
            val range: Int = (max - min).toInt() + 1
            return random.nextInt(range) + min.toInt()
        }

        fun reverseColorize(lst: List<String?>?): List<String>? {
            if (lst == null) return null
            val newLst: MutableList<String> = ArrayList()
            for (s: String? in lst) newLst.add(reverseColorize(s))
            return newLst
        }

        private val patternAll: Pattern = Pattern.compile("§x(§[0-9a-fA-F]){6}")
        fun reverseColorize(input: String?): String {
            val matcher: Matcher = patternAll.matcher(input)

            val sb: StringBuffer = StringBuffer()
            while (matcher.find()) {
                val colorCode: String = matcher.group().replace("§".toRegex(), "")
                matcher.appendReplacement(sb, "&#" + colorCode.substring(1))
            }
            matcher.appendTail(sb)

            return sb.toString().replace("§([0-9a-fklmnorx])".toRegex(), "&$1")
        }
    }
}