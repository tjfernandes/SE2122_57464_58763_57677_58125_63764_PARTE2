package org.jabref.gui;

import org.jabref.gui.actions.SimpleCommand;
import org.jabref.preferences.PreferencesService;

import javax.swing.undo.UndoManager;

public class SearchLibrariesAction extends SimpleCommand {

    private DialogService dialogService;
    private PreferencesService preferencesService;
    private StateManager stateManager;
    private UndoManager undoManager;

    public SearchLibrariesAction(DialogService dialogService, StateManager stateManager, PreferencesService preferencesService, UndoManager undoManager) {
        this.dialogService = dialogService;
        //this.executable.bind(needsDataBase(stateManager));
        this.stateManager = stateManager;
        this.preferencesService = preferencesService;
        this.undoManager = undoManager;
    }

    @Override
    public void execute() {
        //DialogService dialogService = Injector.instantiateModelOrService(DialogService.class);
        dialogService.showCustomDialog(new SearchLibrariesView(dialogService, stateManager, preferencesService, undoManager));
    }
}
