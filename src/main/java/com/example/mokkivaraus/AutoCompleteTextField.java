package com.example.mokkivaraus;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class AutoCompleteTextField<T> extends TextField {
    // Attributes
    private ObjectProperty<T> lastSelectedItem = new SimpleObjectProperty<>();
    private ObservableList<T> entries = FXCollections.observableArrayList();
    private ContextMenu entriesPopup;
    private boolean addCreateLbl = false;

    // Constructors
    public AutoCompleteTextField(Collection<T> entries) {
        super();
        this.entriesPopup = new ContextMenu();
        this.entries.addAll(entries);
        setListener();
    }

    public AutoCompleteTextField(Collection<T> entries, boolean addCreateLbl) {
        this(entries);
        this.addCreateLbl = addCreateLbl;
    }

    // Methods
    public void setLastSelectedItem(Object lastSelectedItem) {
        entries.add((T) lastSelectedItem);
        this.lastSelectedItem.set((T) lastSelectedItem);
        setText(lastSelectedItem.toString());
    }

    public Object getLastSelectedItem() {
        return lastSelectedItem.get();
    }

    public ObjectProperty<T> lastSelectedItemProperty() {
        return lastSelectedItem;
    }

    public boolean isAddCreateLbl() {
        return addCreateLbl;
    }

    public void setAddCreateLbl(boolean addCreateLbl) {
        this.addCreateLbl = addCreateLbl;
    }

    /**
     * Add text listener
     */
    private void setListener() {
        // List of filtered data
        FilteredList<T> filteredData = new FilteredList<>(entries);

        // Add listener on key typed
        this.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String newValue = getText();
                lastSelectedItem.set(null);

                // Search in entries
                filteredData.setPredicate(object -> {
                    // if no search value
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }
                    // Search Conditions
                    return getSearchConditions(object, newValue);
                });

                // Populate context menu with list of matches
                SortedList<T> sortedData = new SortedList<>(filteredData);
                populatePopup(sortedData);
                // Show context menu
                if (!entriesPopup.isShowing()) {
                    entriesPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
                }
            }
        });
    }

    /**
     * List of search conditions
     * @param object object to compare
     * @param newValue Search field text
     * @return True - if condition match
     */
    public boolean getSearchConditions(T object, String newValue) {
        return object.toString().toLowerCase().contains(newValue.trim().toLowerCase());
    }

    /**
     * Populate popup menu with data
     * @param sortedData Data to populate with
     */
    private void populatePopup(SortedList<T> sortedData) {
        // List of menu items
        List<CustomMenuItem> menuItems = new LinkedList<>();

        for (T itemObject : sortedData) {
            // New context menu item
            CustomMenuItem item = new CustomMenuItem(new Label(itemObject.toString()), true);
            // If item is selected
            item.setOnAction((ActionEvent event) -> {
                lastSelectedItem.set(itemObject);
                entriesPopup.hide();
                setText(itemObject.toString());
            });
            // Add item into list of menu items
            menuItems.add(item);
        }

        // Add 'Create' label with action -> onCreateLabelClicked()
        if (addCreateLbl) {
            // 'Create' label
            CustomMenuItem item = new CustomMenuItem(new Label("Lisää uusi"), true);
            item.setOnAction((ActionEvent event) -> {
                onCreateLabelClicked();
            });
            menuItems.add(item);
        }

        // Set items
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }

    /**
     * Method called when the 'Create' label is selected
     */
    public void onCreateLabelClicked(){};
}
