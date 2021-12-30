package org.jabref.gui;

import com.airhacks.afterburner.views.ViewLoader;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import org.jabref.gui.externalfiletype.ExternalFileTypes;
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

    @FXML private ListView<String> list = new ListView<>();


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

    }

    public void updateResult(ObservableList<String> types) {
        list.setItems(types);
    }

}
