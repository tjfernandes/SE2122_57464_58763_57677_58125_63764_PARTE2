package org.jabref.gui;

import com.airhacks.afterburner.injection.Injector;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import org.controlsfx.control.CheckTreeView;
import org.jabref.gui.actions.SimpleCommand;
import org.jabref.gui.externalfiles.UnlinkedFilesDialogViewModel;
import org.jabref.gui.util.FileNodeViewModel;
import org.jabref.gui.util.TaskExecutor;
import org.jabref.model.util.FileUpdateMonitor;
import org.jabref.preferences.PreferencesService;

import javax.inject.Inject;
import javax.swing.undo.UndoManager;

public class SearchLibrariesAction extends SimpleCommand {

    private final DialogService dialogService;
    private final StateManager stateManager;
    private UnlinkedFilesDialogViewModel viewModel;

    @FXML
    private CheckTreeView<FileNodeViewModel> list;
    @FXML
    private TitledPane filePane;

    @Inject
    private PreferencesService preferencesService;
    @Inject private UndoManager undoManager;
    @Inject private TaskExecutor taskExecutor;
    @Inject private FileUpdateMonitor fileUpdateMonitor;

    public SearchLibrariesAction(DialogService dialogService, StateManager stateManager) {
        this.dialogService = dialogService;
        this.stateManager = stateManager;
        //this.executable.bind(ActionHelper.needsDatabase(stateManager));

        //viewModel = new SearchLibraries(dialogService, ExternalFileTypes.getInstance(), undoManager, fileUpdateMonitor, preferencesService, stateManager, taskExecutor);
    }

    @Override
    public void execute() {
        DialogService dialogService = Injector.instantiateModelOrService(DialogService.class);
        dialogService.showCustomDialogAndWait(new SearchLibraries(dialogService));
    }
/*
    private void initUnlinkedFilesList() {
        new ViewModelTreeCellFactory<FileNodeViewModel>()
                .withText(FileNodeViewModel::getDisplayTextWithEditDate)
                .install(list);

        list.maxHeightProperty().bind(((Control) filePane.contentProperty().get()).heightProperty());
        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        list.rootProperty().bind(EasyBind.map(viewModel.treeRootProperty(),
                fileNode -> fileNode.map(fileNodeViewModel -> new RecursiveTreeItem<>(fileNodeViewModel, FileNodeViewModel::getChildren))
                        .orElse(null)));

        EasyBind.subscribe(list.rootProperty(), root -> {
            if (root != null) {
                ((CheckBoxTreeItem<FileNodeViewModel>) root).setSelected(true);
                root.setExpanded(true);
                EasyBind.bindContent(viewModel.checkedFileListProperty(), list.getCheckModel().getCheckedItems());
            } else {
                EasyBind.bindContent(viewModel.checkedFileListProperty(), FXCollections.observableArrayList());
            }
        });
    }*/
}
