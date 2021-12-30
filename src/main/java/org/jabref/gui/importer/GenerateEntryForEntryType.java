package org.jabref.gui.importer;

import com.airhacks.afterburner.views.ViewLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.controlsfx.control.PopOver;
import org.jabref.gui.DialogService;
import org.jabref.gui.LibraryTab;
import org.jabref.gui.StateManager;
import org.jabref.gui.icon.IconTheme;
import org.jabref.gui.util.TaskExecutor;
import org.jabref.logic.l10n.Localization;
import org.jabref.model.database.BibDatabaseContext;
import org.jabref.model.entry.BibEntry;
import org.jabref.preferences.PreferencesService;

import java.util.List;
import java.util.Locale;

public class GenerateEntryForEntryType {

    @FXML DialogPane dialogPane;
    @FXML TextField idTextField;
    @FXML TextField startYear;
    @FXML TextField endYear;
    @FXML Button generateButton;
    @FXML private ListView<String> list = new ListView<>();
    ObservableList<String> types = FXCollections.observableArrayList();

    private final PreferencesService preferencesService;
    private final DialogService dialogService;
    private final LibraryTab libraryTab;
    private final TaskExecutor taskExecutor;
    private final StateManager stateManager;
    private PopOver entryPopOver;

    private final static int MAX_CHARATERS = 300;

    public GenerateEntryForEntryType(LibraryTab libraryTab, DialogService dialogService, PreferencesService preferencesService, TaskExecutor taskExecutor, StateManager stateManager) {
        ViewLoader.view(this).load();
        this.preferencesService = preferencesService;
        this.dialogService = dialogService;
        this.libraryTab = libraryTab;
        this.taskExecutor = taskExecutor;
        this.stateManager = stateManager;
        this.generateButton.setGraphic(IconTheme.JabRefIcons.SEARCH.asButton());
        this.generateButton.setDefaultButton(true);
    }

    @FXML private void generateEntry() {

        if (idTextField.getText().isEmpty()) {
            dialogService.notify(Localization.lang("Enter a valid entry type"));
            return;
        }

        this.idTextField.requestFocus();

        BibDatabaseContext db = stateManager.getActiveDatabase().orElseThrow(NullPointerException::new);
        List<BibEntry> entries = db.getEntries();

        types.remove(0,types.size());
        for(BibEntry e:entries){

            if(testDates(startYear) && testDates(endYear)){
                if(     e.getType().getName().toLowerCase(Locale.ROOT).compareTo(idTextField.getText().toLowerCase(Locale.ROOT)) == 0)
                    types.add(e.getAuthorTitleYear(MAX_CHARATERS));
            }
            else
                if(testDates(endYear)){
                    if(     e.getType().getName().toLowerCase(Locale.ROOT).compareTo(idTextField.getText().toLowerCase(Locale.ROOT)) == 0
                            && getYear(e.getAuthorTitleYear(MAX_CHARATERS)).compareTo(startYear.getText()) >= 0)
                        types.add(e.getAuthorTitleYear(MAX_CHARATERS));
                }
                else
                    if(testDates(startYear)){
                        if(     e.getType().getName().toLowerCase(Locale.ROOT).compareTo(idTextField.getText().toLowerCase(Locale.ROOT)) == 0
                                && getYear(e.getAuthorTitleYear(MAX_CHARATERS)).compareTo(endYear.getText()) <= 0)
                            types.add(e.getAuthorTitleYear(MAX_CHARATERS));
                    }
                    else {
                        if(     e.getType().getName().toLowerCase(Locale.ROOT).compareTo(idTextField.getText().toLowerCase(Locale.ROOT)) == 0
                                && getYear(e.getAuthorTitleYear(MAX_CHARATERS)).compareTo(startYear.getText()) >= 0
                                && getYear(e.getAuthorTitleYear(MAX_CHARATERS)).compareTo(endYear.getText()) <= 0)
                            types.add(e.getAuthorTitleYear(MAX_CHARATERS));
                    }
        }

        for(int i=0;i<types.size()-1;i++){
            for(int d = 0; d<types.size()-i-1;d++){
                String s1 = getYear(types.get(d));
                String s2 = getYear(types.get(d+1));
                if(s1.compareTo(s2) > 0){
                    String temp = types.get(d);
                    types.remove(d);
                    types.add(d+1,temp);
                }
            }
        }
        list.setItems(types);
    }

    private boolean testDates(TextField textField){
        if(textField.getText().isEmpty()) return true;
        String s = textField.getText();
        boolean flag = false;

        for(int i =0; i<s.length();i++){
            if(!Character.isDigit(s.charAt(i))){
                flag = true;
                dialogService.notify(Localization.lang("'"+textField.getText() +"' is not a valid year, insert a valid one"));
                break;
            }
        }

        return flag;
    }


    private String getYear(String s){
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        for (int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == ')') break;
            if(flag == true){
                sb.append(s.charAt(i));
                continue;
            }
            if(s.charAt(i) == '(') {
                flag = true;
            }
        }
        return sb.toString();
    }

    public void setEntryFromIdPopOver(PopOver entryFromIdPopOver) {
        this.entryPopOver = entryFromIdPopOver;
    }

    public DialogPane getDialogPane() {
        return dialogPane;
    }

}
