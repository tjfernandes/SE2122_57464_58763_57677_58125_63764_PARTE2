package org.jabref.gui;

import com.airhacks.afterburner.views.ViewLoader;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import org.controlsfx.control.textfield.CustomTextField;
import org.jabref.gui.util.BaseDialog;
import org.jabref.logic.l10n.Localization;
import org.jabref.preferences.PreferencesService;
import org.jabref.preferences.SearchPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.undo.UndoManager;

public class SearchLibrariesView extends BaseDialog{



    //@FXML private CustomTextField searchBox;

   // @FXML private Button searchButton;
    @FXML private DialogPane dialogPane;

    private StateManager stateManager;
    private PreferencesService preferencesService;
    private final Label currentResults = new Label("");

    private GridPane gridPane = new GridPane();
    private TextField textField = new CustomTextField();
    private Button searchButton = new Button("Search");

    private static final PseudoClass CLASS_NO_RESULTS = PseudoClass.getPseudoClass("emptyResult");

    private SearchPreferences searchPreferences;

    private final DialogService dialogService;

    private UndoManager undoManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchLibrariesView.class);

    public SearchLibrariesView(DialogService dialogService, StateManager stateManager, PreferencesService preferencesService, UndoManager undoManager) {
        this.dialogService = dialogService;
        this.stateManager = stateManager;
        this.preferencesService = preferencesService;
        this.searchPreferences = preferencesService.getSearchPreferences();
        this.undoManager = undoManager;

        currentResults.setPrefWidth(150);


        super.setTitle(Localization.lang("Search results from all libraries"));
        ViewLoader.view(this)
                .load()
                .setAsDialogPane(this);
        super.initModality(Modality.NONE);
    }

    @FXML
    public void initialize() {
        dialogPane.setContent(gridPane);

        textField.setPromptText("Search...");
        textField.setPrefHeight(30.0);
        textField.setPrefWidth(300.0);

        gridPane.setConstraints(searchButton, 1, 0);
        searchButton.setAlignment(Pos.CENTER_RIGHT);
        searchButton.setStyle("-fx-background-color: #a3acc0");
        searchButton.setOnAction(event -> {

        });

        gridPane.getChildren().addAll(textField, searchButton);

    }
}
