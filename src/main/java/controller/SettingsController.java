package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
import util.CustomLogger;

public class SettingsController implements Controller {
    private static final Preferences userPreferences = Preferences.userRoot().node("IGT_Settings");

    @FXML public CheckBox consoleOutput;
    @FXML public CheckBox searchForMoreVideos;
    @FXML public CheckBox verticalFG;

    @FXML
    private void changeConsoleOutput() {
        CustomLogger.changeConsoleOutput();
        userPreferences.putBoolean("logToConsole", consoleOutput.isSelected());
    }

    @FXML
    private void onSearchForMoreVideosClicked() {
        userPreferences.putBoolean("searchForMoreVideos", searchForMoreVideos.isSelected());
    }

    @FXML
    private void onVerticalFGClicked(){
        userPreferences.putBoolean("verticalFieldGenerator", verticalFG.isSelected());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registerController();

        var consolePreference = userPreferences.getBoolean("logToConsole", true);
        consoleOutput.setSelected(consolePreference);
        if (!CustomLogger.isConsoleOn() && consolePreference) {
            CustomLogger.changeConsoleOutput();
        }

        var searchForMoreVideosPreference = userPreferences.getBoolean("searchForMoreVideos", false);
        searchForMoreVideos.setSelected(searchForMoreVideosPreference);
        var tooltip = new Tooltip("When enabled, the autotrack view will try to find and enumerate all video devices that are connected to the computer. This is helpful if you have more than one camera. However, this will increase the time needed before the view is ready");
        tooltip.setWrapText(true);
        searchForMoreVideos.setTooltip(tooltip);

        var exchangeYZPreference = userPreferences.getBoolean("exchangeYZ", false);
        verticalFG.setSelected(exchangeYZPreference);
    }

    @FXML
    @Override
    public void close() {
        unregisterController();
    }
}
