package uk.co.thomasc.codmw.sql;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.bukkit.Bukkit;

import uk.co.thomasc.codmw.util.MCrypt;

public class SqlLogin {

	public String url = "";

	public SqlLogin() {
		Properties props = new Properties();

		try {
			props.load(this.getClass().getClassLoader().getResourceAsStream("sqllogin.properties"));
			String e = props.getProperty("password");

			try {
				MCrypt e1 = new MCrypt();
				e = new String(e1.decrypt(e));
			} catch (Exception var4) {
				var4.printStackTrace();
			}

			this.url = "jdbc:mysql://" + props.getProperty("dbserver") + ":3306/thomasc2_minecod?autoReconnect=true&user=" + props.getProperty("username") + "&password=" + e.trim();
		} catch (FileNotFoundException var5) {
			var5.printStackTrace();
		} catch (IOException var6) {
			var6.printStackTrace();
		}
	}
}
