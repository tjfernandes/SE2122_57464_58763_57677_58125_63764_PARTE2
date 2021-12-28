package org.jabref.gui;

import com.airhacks.afterburner.views.ViewLoader;
import javafx.fxml.FXML;
import org.controlsfx.control.textfield.CustomTextField;
import org.jabref.gui.icon.IconTheme;
import org.jabref.gui.util.BaseDialog;
import org.jabref.logic.l10n.Localization;

public class SearchLibraries extends BaseDialog{

    @FXML
    private CustomTextField searchBox;
    private final DialogService dialogService;

    public SearchLibraries(DialogService dialogService) {
        this.dialogService = dialogService;

        this.setTitle(Localization.lang("Search on all libraries"));
        ViewLoader.view(this)
                .load()
                .setAsDialogPane(this);
    }

    @FXML
    public void initialize() {
        searchBox.setPromptText(Localization.lang("Search") + "...");
        searchBox.setLeft(IconTheme.JabRefIcons.SEARCH.getGraphicNode());
    }

}
