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

    // Constructors
    public AutoCompleteTextField(Collection<T> entries) {
        super();
        this.entriesPopup = new ContextMenu();
        this.entries.addAll(entries);
        setListener();
    }

    // Methods
    public void setLastSelectedItem(Object lastSelectedItem) {
        this.lastSelectedItem.set((T) lastSelectedItem);
        setText(lastSelectedItem.toString());
    }

    public Object getLastSelectedItem() {
        return lastSelectedItem.get();
    }

    public ObjectProperty<T> lastSelectedItemProperty() {
        return lastSelectedItem;
    }

    private void setListener() {
        // Search in entries
        FilteredList<T> filteredData = new FilteredList<>(entries);

        this.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String newValue = getText();
                lastSelectedItem.set(null);

                filteredData.setPredicate(object -> {
                    // if no search value
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }
                    // Search Conditions
                    return object.toString().toLowerCase().contains(newValue.trim().toLowerCase());
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

    private void populatePopup(SortedList<T> sortedData) {
        List<CustomMenuItem> menuItems = new LinkedList<>();

        for (int i = 0; i < sortedData.size(); i++) {
            // New context menu item
            T itemObject = sortedData.get(i);
            CustomMenuItem item = new CustomMenuItem(new Label(itemObject.toString()), true);
            // if item is selected
            item.setOnAction((ActionEvent event) -> {
                lastSelectedItem.set(itemObject);
                entriesPopup.hide();
                setText(itemObject.toString());
            });
            // Add item into list of items
            menuItems.add(item);
        }

        // Show 'Create' label if there is no matches
        if (sortedData.size() == 0) {
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

    public void onCreateLabelClicked(){};
}
