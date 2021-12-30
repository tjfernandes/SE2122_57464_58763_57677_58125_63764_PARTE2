package org.jabref.gui;

import com.airhacks.afterburner.views.ViewLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import org.controlsfx.control.textfield.CustomTextField;
import org.jabref.gui.util.BaseDialog;
import org.jabref.logic.l10n.Localization;
import org.jabref.model.database.BibDatabaseContext;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.StandardField;

import java.util.Locale;

public class SearchLibrariesView extends BaseDialog{

    @FXML private DialogPane dialogPane;

    private StateManager stateManager;

    private GridPane gridPane = new GridPane();
    private TextField textField = new CustomTextField();
    private Button searchButton = new Button("Search");

    private final static int MAX_CHARATERS = 500;

    private final DialogService dialogService;
    private SearchLibrariesResultDialog resultDialog;

    private ObservableList<String> types = FXCollections.observableArrayList();

    public SearchLibrariesView(DialogService dialogService, StateManager stateManager) {
        this.dialogService = dialogService;
        this.stateManager = stateManager;



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
            resultDialog = new SearchLibrariesResultDialog();
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
                    String lib = "Untilted Lib";
                        if (db.getDatabasePath().isPresent())
                            lib = db.getDatabasePath().get().getFileName().toString();
                    types.add(lib + " --> " + e.getAuthorTitleYear(MAX_CHARATERS));
                }
            }
        }
    }

}
