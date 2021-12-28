package org.jabref.gui;

import com.airhacks.afterburner.injection.Injector;
import org.jabref.gui.actions.SimpleCommand;

public class SearchLibrariesAction extends SimpleCommand {

    private DialogService dialogService;

    public SearchLibrariesAction(DialogService dialogService, StateManager stateManager) {
        this.dialogService = dialogService;
        //this.executable.bind(needsDataBase(stateManager));
    }

    @Override
    public void execute() {
        DialogService dialogService = Injector.instantiateModelOrService(DialogService.class);
        dialogService.showCustomDialog(new SearchLibraries(dialogService));
    }
}
