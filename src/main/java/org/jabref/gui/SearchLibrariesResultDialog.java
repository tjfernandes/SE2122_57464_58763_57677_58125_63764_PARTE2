package org.jabref.gui;

import com.airhacks.afterburner.views.ViewLoader;
import com.tobiasdiez.easybind.EasyBind;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jabref.gui.externalfiletype.ExternalFileTypes;
import org.jabref.gui.icon.IconTheme;
import org.jabref.gui.maintable.columns.SpecialFieldColumn;
import org.jabref.gui.preview.PreviewViewer;
import org.jabref.gui.search.SearchResultsTable;
import org.jabref.gui.search.SearchResultsTableDataModel;
import org.jabref.gui.util.BaseDialog;
import org.jabref.logic.l10n.Localization;
import org.jabref.preferences.PreferencesService;

import javax.swing.undo.UndoManager;

public class SearchLibrariesResultDialog extends BaseDialog {

    private final ExternalFileTypes externalFileTypes;
    private final UndoManager undoManager;

    private PreferencesService preferencesService;
    private StateManager stateManager;
    private DialogService dialogService;

    @FXML private SplitPane container;
    @FXML private ToggleButton keepOnTop;

    private SearchLibrariesResultDialogViewModel viewModel;


    public SearchLibrariesResultDialog(ExternalFileTypes externalFileTypes, PreferencesService preferencesService,
                                       StateManager stateManager, DialogService dialogService, UndoManager undoManager) {
        this.undoManager = undoManager;
        this.externalFileTypes = externalFileTypes;
        this.dialogService = dialogService;
        this.stateManager = stateManager;
        this.preferencesService = preferencesService;

        setTitle(Localization.lang("Search results from all libraries"));
        ViewLoader.view(this)
                .load()
                .setAsDialogPane(this);
        initModality(Modality.NONE);
    }

    @FXML
    private void initialize() {
        viewModel = new SearchLibrariesResultDialogViewModel(preferencesService);

        PreviewViewer previewViewer = new PreviewViewer(viewModel.getSearchDatabaseContext(), dialogService, stateManager);
        previewViewer.setTheme(preferencesService.getTheme());
        previewViewer.setLayout(preferencesService.getPreviewPreferences().getCurrentPreviewStyle());

        SearchResultsTableDataModel model = new SearchResultsTableDataModel(viewModel.getSearchDatabaseContext(), preferencesService, stateManager);
        SearchResultsTable resultsTable = new SearchResultsTable(model, viewModel.getSearchDatabaseContext(), preferencesService, undoManager, dialogService, stateManager, externalFileTypes);

        resultsTable.getColumns().removeIf(col -> col instanceof SpecialFieldColumn);
        resultsTable.getSelectionModel().selectFirst();
        resultsTable.getSelectionModel().selectedItemProperty().addListener((obs, old, newValue) -> {
            if (newValue != null) {
                previewViewer.setEntry(newValue.getEntry());
            } else {
                previewViewer.setEntry(old.getEntry());
            }
        });

        container.getItems().addAll(resultsTable, previewViewer);

        keepOnTop.selectedProperty().bindBidirectional(viewModel.keepOnTop());
        EasyBind.subscribe(viewModel.keepOnTop(), value -> {
            Stage stage = (Stage) getDialogPane().getScene().getWindow();
            stage.setAlwaysOnTop(value);
            keepOnTop.setGraphic(value
                    ? IconTheme.JabRefIcons.KEEP_ON_TOP.getGraphicNode()
                    : IconTheme.JabRefIcons.KEEP_ON_TOP_OFF.getGraphicNode());
        });
    }

}
