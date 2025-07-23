package xyz.diaduck.selfroles;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;

public class Roles {
    public static Role ROLE1;
    public static Role ROLE2;
    public static Role ROLE3;

    public static void initRoles(JDA jda) {
        ROLE1 = jda.getRoleById("1397584131755348040");
        ROLE2 = jda.getRoleById("1397584242036445316");
        ROLE3 = jda.getRoleById("1397584255219007653");
    }
}
