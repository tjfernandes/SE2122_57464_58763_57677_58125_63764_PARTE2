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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import org.controlsfx.control.textfield.CustomTextField;
import org.jabref.gui.externalfiletype.ExternalFileTypes;
import org.jabref.gui.util.BaseDialog;
import org.jabref.gui.util.TooltipTextUtil;
import org.jabref.logic.l10n.Localization;
import org.jabref.logic.search.SearchQuery;
import org.jabref.preferences.PreferencesService;
import org.jabref.preferences.SearchPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.undo.UndoManager;
import java.util.List;

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
    private SearchLibrariesResultDialog resultDialog;

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
            //resultDialog = new SearchLibrariesResultDialog(ExternalFileTypes.getInstance());
            resultDialog = new SearchLibrariesResultDialog(ExternalFileTypes.getInstance(), preferencesService, stateManager, dialogService, undoManager);
            this.performSearch();
            resultDialog.showAndWait();
        });

        gridPane.getChildren().addAll(textField, searchButton);

    }

    private void performSearch() {
        LOGGER.debug("Flags: {}", searchPreferences.getSearchFlags());
        LOGGER.debug("Run search " + textField.getText());

        // An empty search field should cause the search to be cleared.
        if (textField.getText().isEmpty()) {
            currentResults.setText("");
            setSearchFieldHintTooltip(null);
            stateManager.clearSearchQuery();
            return;
        }

        SearchQuery searchQuery = new SearchQuery(this.textField.getText(), searchPreferences.getSearchFlags());
        if (!searchQuery.isValid()) {
            informUserAboutInvalidSearchQuery();
            return;
        }
        stateManager.setSearchQuery(searchQuery);
    }

    private void informUserAboutInvalidSearchQuery() {
        textField.pseudoClassStateChanged(CLASS_NO_RESULTS, true);

        stateManager.clearSearchQuery();

        String illegalSearch = Localization.lang("Search failed: illegal search expression");
        currentResults.setText(illegalSearch);
    }

    private void setSearchFieldHintTooltip(TextFlow description) {
        if (preferencesService.getGeneralPreferences().shouldShowAdvancedHints()) {
            String genericDescription = Localization.lang("Hint:\n\nTo search all fields for <b>Smith</b>, enter:\n<tt>smith</tt>\n\nTo search the field <b>author</b> for <b>Smith</b> and the field <b>title</b> for <b>electrical</b>, enter:\n<tt>author=Smith and title=electrical</tt>");
            List<Text> genericDescriptionTexts = TooltipTextUtil.createTextsFromHtml(genericDescription);

            if (description == null) {
                TextFlow emptyHintTooltip = new TextFlow();
                emptyHintTooltip.getChildren().setAll(genericDescriptionTexts);
            } else {
                description.getChildren().add(new Text("\n\n"));
                description.getChildren().addAll(genericDescriptionTexts);
            }
        }
    }
}
