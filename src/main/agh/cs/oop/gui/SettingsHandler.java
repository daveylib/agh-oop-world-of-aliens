package agh.cs.oop.gui;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SettingsHandler {
    private final static String settingsPath = "parameters.json";
    private final ObjectMapper mapper = new ObjectMapper();
    public Settings settings;

    public SettingsHandler() throws IOException {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        // Create the settings file if it does not exist
        File settingsFile = new File(settingsPath);
        if (!settingsFile.exists() && !settingsFile.createNewFile()) {
            throw new IOException("Settings file can't be created!");
        }

        // Read settings from the file or set them to default
        try {
            this.settings = this.mapper.readValue(new File(settingsPath), Settings.class);
        } catch (JsonMappingException e) {
            this.settings = new Settings();
            saveSettings();
        }
    }

    // Save settings by writing to the file
    public void saveSettings() throws IOException {
        this.mapper.writeValue(new FileOutputStream(settingsPath), this.settings);
    }
}
