package org.jabref.gui;

import com.airhacks.afterburner.views.ViewLoader;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import org.jabref.gui.util.BaseDialog;
import org.jabref.logic.l10n.Localization;

public class SearchLibrariesResultDialog extends BaseDialog {

    @FXML private ListView<String> list = new ListView<>();

    public SearchLibrariesResultDialog() {

        setTitle(Localization.lang("Search results from all libraries"));
        ViewLoader.view(this)
                .load()
                .setAsDialogPane(this);
        initModality(Modality.NONE);
    }

    public void updateResult(ObservableList<String> types) {
        list.setItems(types);
    }

}
