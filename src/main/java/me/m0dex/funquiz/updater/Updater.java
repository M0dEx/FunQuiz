package me.m0dex.funquiz.updater;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.m0dex.funquiz.FunQuiz;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Updater {

	private final FunQuiz instance;
	
	private URL downloadURL = null;
	private boolean updateAvailable;

	private Path updateFolder;

	public Updater(FunQuiz _instance) {
		instance = _instance;

		updateFolder = Paths.get(instance.getServer().getUpdateFolderFile().getPath());
	}

	public boolean checkForUpdate() {


		try {

			HttpURLConnection connection = (HttpURLConnection) new URL("https://api.spiget.org/v2/resources/44907/versions?size=1&sort=-id&fields=name").openConnection();
			connection.addRequestProperty("User-Agent", "FunQuiz.44907@Author=M0dEx");// Set User-Agent

			// If you're not sure if the request will be successful,
			// you need to check the response code and use #getErrorStream if it returned an error code
			InputStream inputStream = connection.getInputStream();
			InputStreamReader reader = new InputStreamReader(inputStream);

			String externalVersion;
			// This could be either a JsonArray or JsonObject
			JsonElement element = new JsonParser().parse(reader);
			if (element.isJsonArray()) {
				JsonObject versionObject = element.getAsJsonArray().get(0).getAsJsonObject();
				externalVersion = versionObject.get("name").getAsString().replace("v", "");
				downloadURL = new URL("https://api.spiget.org/v2/resources/44907/download");
			} else {
				
				instance.getLogger().info("[UPDATER]: Failed to get version number");
				updateAvailable = false;
				return false;
				
			}

			if (newVersionAvailable(instance.getDescription().getVersion(), externalVersion)) {
				instance.getLogger().info("[UPDATER]: An update is avaiable (" + externalVersion + ")");
				instance.getLogger().info("[UPDATER]: You can download it at: https://www.spigotmc.org/resources/funquiz.44907/");
				instance.getLogger().info("[UPDATER]: Or use '/fq update' and let the updater do the work :)");

				updateAvailable = true;
				return true;
			}

		} catch (Exception ex) {
				
			instance.getLogger().severe("[UPDATER]: An exception occured during reading the version list");
			ex.printStackTrace();

		}

		updateAvailable = false;
		return false;
	}

	private boolean newVersionAvailable(String oldV, String newV) {

		Version oldVersion = Version.fromString(oldV);
		Version newVersion = Version.fromString(newV);

		return newVersion.isNewerThan(oldVersion);
	}

	public boolean update() {

		if(downloadURL == null) {
			instance.getLogger().info("[UPDATER]: Download URL invalid");
			return false;
		}

		try {

			HttpURLConnection conn = (HttpURLConnection) downloadURL.openConnection();
			conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36");

			try (InputStream in = conn.getInputStream()) {

				if (!Files.exists(updateFolder))
					Files.createDirectories(updateFolder);

				Files.copy(in, Paths.get(updateFolder.toString(), "FunQuiz.jar"), StandardCopyOption.REPLACE_EXISTING);

				instance.getLogger().info("[UPDATER]: Update folder: " + instance.getServer().getUpdateFolder());
				instance.getLogger().info("[UPDATER]: Successfully updated the plugin!");
				instance.getLogger().info("[UPDATER]: Restart the server to apply the changes");

				return true;

			} catch (Exception ex) {
				instance.getLogger().severe("[UPDATER]: Couldn't download the update...");
				ex.printStackTrace();
			}
		} catch (Exception ex) {
			instance.getLogger().severe("[UPDATER]: Couldn't download the update...");
			ex.printStackTrace();
		}

		return false;
	}

	public synchronized boolean isUpdateAvailable() {
		return updateAvailable;
	}
}
