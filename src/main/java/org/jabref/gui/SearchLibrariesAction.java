package org.jabref.gui;

import org.jabref.gui.actions.SimpleCommand;

public class SearchLibrariesAction extends SimpleCommand {


    private DialogService dialogService;
    private StateManager stateManager;

    public SearchLibrariesAction(DialogService dialogService, StateManager stateManager) {
        this.dialogService = dialogService;
        this.stateManager = stateManager;
    }

    @Override
    public void execute() {
        dialogService.showCustomDialog(new SearchLibrariesView(dialogService, stateManager));
    }
}
