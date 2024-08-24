package dvie.kfeatures.modules.tokens.config

import de.exlll.configlib.Comment
import de.exlll.configlib.Configuration
import lombok.Getter
import lombok.NoArgsConstructor

@Configuration
@Getter
@SuppressWarnings("FieldMayBeFinal")
@NoArgsConstructor
class Config {


    @Comment("Messages")
    var balance: String = "&aYour balance is &f%balance%"
    var addToken: String = "&aYou have added &f%amount% &atokens to &f%player%"
    var removeToken: String = "&aYou have removed &f%amount% &atokens from &f%player%"
    var setToken: String = "&aYou have set &f%player%'s &atokens to &f%amount%"
    var resetTokens: String = "&aYou have reset &f%player%'s &atokens"
    var resetAll: String = "&aYou have reset all tokens"
    var reload: String = "&aYou have reloaded the config"
    var loginAmount: Int = 100

    @Comment("", "Token top message")
    var top: String = "&aTop 10 players:"
    var topFormat: String = "&f%position%. &a%player% &f- &a%balance%"

}