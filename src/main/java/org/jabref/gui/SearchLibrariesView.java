package org.jabref.gui;

import com.airhacks.afterburner.views.ViewLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.jabref.gui.externalfiletype.ExternalFileTypes;
import org.jabref.gui.util.BaseDialog;
import org.jabref.logic.l10n.Localization;
import org.jabref.model.database.BibDatabaseContext;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.StandardField;
import org.jabref.preferences.PreferencesService;
import org.jabref.preferences.SearchPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.undo.UndoManager;
import java.util.Locale;

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

    private final static int MAX_CHARATERS = 500;

    private static final PseudoClass CLASS_NO_RESULTS = PseudoClass.getPseudoClass("emptyResult");

    private SearchPreferences searchPreferences;

    private final DialogService dialogService;
    private SearchLibrariesResultDialog resultDialog;

    private UndoManager undoManager;

    private ObservableList<String> types = FXCollections.observableArrayList();

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
            //resultDialog = new SearchLibrariesResultDialog(ExternalFileTypes.getInstance());
            resultDialog = new SearchLibrariesResultDialog(ExternalFileTypes.getInstance(), preferencesService, stateManager, dialogService, undoManager);
            this.performSearch();
            resultDialog.updateResult(types);
            resultDialog.showAndWait();
        });

        gridPane.getChildren().addAll(textField, searchButton);

    }

    private void performSearch() {
        // An empty search field should cause the search to be cleared.
        if (textField.getText().isEmpty()) {
            dialogService.notify(Localization.lang("No content found"));
            return;
        }

        ObservableList<BibDatabaseContext> listDB = stateManager.getOpenDatabases();
        types.removeAll(types);

        for(BibDatabaseContext db : listDB) {
            for(BibEntry e : db.getEntries()) {
                String author = e.getField(StandardField.AUTHOR).isEmpty() ? "N/A" : e.getField(StandardField.AUTHOR).get();
                String year = e.getField(StandardField.YEAR).isEmpty() ? "N/A" : e.getField(StandardField.YEAR).get();
                String title = e.getField(StandardField.TITLE).isEmpty() ? "N/A" : e.getField(StandardField.TITLE).get();
                if (e.getType().getName().toLowerCase(Locale.ROOT).compareTo(textField.getText().toLowerCase(Locale.ROOT)) == 0 ||
                        author.toLowerCase(Locale.ROOT).compareTo(textField.getText().toLowerCase(Locale.ROOT)) == 0 ||
                        year.toLowerCase(Locale.ROOT).compareTo(textField.getText().toLowerCase(Locale.ROOT)) == 0 ||
                        title.toLowerCase(Locale.ROOT).compareTo(textField.getText().toLowerCase(Locale.ROOT)) == 0) {
                    types.add(e.getAuthorTitleYear(MAX_CHARATERS));
                }
            }
        }
    }

    private void informUserAboutInvalidSearchQuery() {
        textField.pseudoClassStateChanged(CLASS_NO_RESULTS, true);

        stateManager.clearSearchQuery();

        String illegalSearch = Localization.lang("Search failed: illegal search expression");
        currentResults.setText(illegalSearch);
    }
}
