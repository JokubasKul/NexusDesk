package nexusDesk.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;

import nexusDesk.ColourPicker;
import nexusDesk.database.BookmarkDatabase;
import nexusDesk.database.OptionDatabase;
import nexusDesk.models.Bookmark;
import nexusDesk.models.Colour;

import java.awt.Desktop;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;

/**
 * Functionality of Bookmarks module
 */
public class BookmarksController {

    private StackPane mainContent;
    public void setMainContent(StackPane mainContent) {
        this.mainContent = mainContent;
    }

    @FXML
    private VBox bookmarksList;

    private final BookmarkDatabase bookmarkDatabase = new BookmarkDatabase();
    private final OptionDatabase optionDatabase = new OptionDatabase();

    private final ColourPicker colourPicker = new ColourPicker();

    @FXML
    public void initialize() {
        loadBookmarks();
    }

    /**
     * Loops through all the bookmarks and sets them up
     */
    private void loadBookmarks() {

        bookmarksList.getChildren().clear();

        try {
            List<Bookmark> bookmarks = bookmarkDatabase.getAllBookmarks();

            for (Bookmark bookmark : bookmarks) {

                VBox bookmarkCard = new VBox(10);
                bookmarkCard.getStyleClass().add("bookmarkCard");

                String colourHex = optionDatabase.getColourHex(bookmark.getColourId());
                bookmarkCard.setStyle(
                        "-fx-background-color: " + colourHex + "99;"
                );

                String darkenedColour = colourPicker.darkenColour(colourHex);
                bookmarkCard.setOnMouseEntered(mouseEvent ->
                        bookmarkCard.setStyle(
                                "-fx-background-color:"  + darkenedColour + "99;"
                        )
                );
                bookmarkCard.setOnMouseExited(mouseEvent ->
                        bookmarkCard.setStyle(
                                "-fx-background-color:"  + colourHex + "99;"
                        )
                );


                HBox bookmarkFunctionality = new HBox(10);
                bookmarkFunctionality.setAlignment(Pos.CENTER_LEFT
                );


                Label title = new Label(bookmark.getTitle());
                title.getStyleClass().add("bookmarkName");


                Region spacer = new Region();
                HBox.setHgrow(
                        spacer,
                        Priority.ALWAYS
                );


                Button colourButton = new Button("🎨");

                colourButton.getStyleClass().add("bookmarkColour");

                colourButton.setOnAction(event -> {

                    Colour selectedColour = ColourPicker.show();

                    if (selectedColour != null) {
                        try {
                            optionDatabase.updateBookmarkColour(
                                    bookmark.getBookmarkId(),
                                    selectedColour.getColourId()
                            );
                            loadBookmarks();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });


                Button editButton = new Button("✎");

                editButton.getStyleClass().add("bookmarkEdit");

                editButton.setOnAction(event ->
                        editBookmark(bookmark, bookmarkCard)
                );


                Button deleteButton = new Button("✕");
                deleteButton.getStyleClass().add("bookmarkDelete");

                deleteButton.setOnAction(event -> {

                    try {
                        bookmarkDatabase.deleteBookmark(
                                bookmark.getBookmarkId()
                        );
                        bookmarksList.getChildren().remove(bookmarkCard);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });


                bookmarkFunctionality.getChildren().addAll(
                        title,
                        spacer
                );


                if (bookmark.getNeedsUrl() == 1) {
                    Button websiteButton = new Button("🌐");

                    websiteButton.getStyleClass().add("bookmarkWebsite");

                    websiteButton.setOnAction(event ->
                            openWebsite(bookmark.getUrl())
                    );

                    bookmarkFunctionality.getChildren().add(
                            websiteButton
                    );
                }

                bookmarkFunctionality.getChildren().addAll(
                        colourButton,
                        editButton,
                        deleteButton
                );


                Label description = new Label();

                if (bookmark.getDescription() == null || bookmark.getDescription().isBlank()) {
                    description.setText("No description");

                } else {
                    description.setText(bookmark.getDescription());
                }

                description.setWrapText(true);
                description.getStyleClass().add("bookmarkDescription");


                bookmarkCard.getChildren().addAll(
                        bookmarkFunctionality,
                        description
                );

                bookmarksList.getChildren().add(
                        bookmarkCard
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a website
     * @param url the url of the website
     */
    private void openWebsite(String url) {

        if (url == null || url.isBlank()) {
            return;
        }

        try {
            Desktop.getDesktop().browse(
                    new URI(url)
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a VBox where the new bookmark is added
     */
    @FXML
    private void addBookmark() {

        VBox addBox = new VBox(10);
        addBox.getStyleClass().add("bookmarkCard");

        TextField titleField = new TextField();
        titleField.setPromptText("Bookmark title...");

        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Description...");
        descriptionField.setWrapText(true);
        descriptionField.setPrefRowCount(3);

        descriptionField.setTextFormatter(
                new TextFormatter<String>(change ->
                        change.getControlNewText().length() <= 100
                                ? change
                                : null
                )
        );

        CheckBox needsUrlCheckBox = new CheckBox("Add website");

        TextField urlField = new TextField();
        urlField.setPromptText("Website URL...");
        urlField.setDisable(true);

        needsUrlCheckBox.setOnAction(event -> {

            boolean needsUrl = needsUrlCheckBox.isSelected();

            urlField.setDisable(!needsUrl);

            if (!needsUrl) {
                urlField.clear();
            }
        });

        HBox buttons = new HBox(10);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        buttons.getChildren().addAll(
                saveButton,
                cancelButton
        );

        saveButton.setOnAction(event ->
                saveBookmark(
                        addBox,
                        titleField,
                        descriptionField,
                        needsUrlCheckBox,
                        urlField
                )
        );

        cancelButton.setOnAction(event ->
                bookmarksList.getChildren().remove(addBox)
        );

        addBox.getChildren().addAll(
                titleField,
                descriptionField,
                needsUrlCheckBox,
                urlField,
                buttons
        );

        bookmarksList.getChildren().add(0, addBox);

        titleField.requestFocus();
    }

    /**
     * Saves the bookmark in the database and reloads the bookmarksList
     */
    private void saveBookmark(VBox addBox, TextField titleField, TextArea descriptionField, CheckBox needsUrlCheckBox, TextField urlField) {

        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();

        int needsUrl = needsUrlCheckBox.isSelected() ? 1 : 0;

        String url = urlField.getText().trim();

        if (title.isEmpty()) {
            return;
        }

        if (needsUrl == 1 && url.isEmpty()) {
            return;
        }

        try {

            bookmarkDatabase.createBookmark(
                    title,
                    description,
                    needsUrl,
                    url
            );

            bookmarksList.getChildren().remove(addBox);

            loadBookmarks();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the bookmark
     */
    private void editBookmark(Bookmark bookmark, VBox bookmarkCard) {

        VBox editBox = new VBox(10);
        editBox.getStyleClass().add("bookmarkCard");

        TextField titleField = new TextField(bookmark.getTitle());

        TextArea descriptionField = new TextArea(bookmark.getDescription());

        descriptionField.setWrapText(true);
        descriptionField.setPrefRowCount(3);

        descriptionField.setTextFormatter(
                new TextFormatter<String>(change ->
                        change.getControlNewText().length() <= 100
                                ? change
                                : null
                )
        );

        CheckBox needsUrlCheckBox = new CheckBox("Add website");

        needsUrlCheckBox.setSelected(bookmark.getNeedsUrl() == 1);

        TextField urlField = new TextField(bookmark.getUrl());

        urlField.setPromptText("Website URL...");

        urlField.setDisable(
                bookmark.getNeedsUrl() != 1
        );

        needsUrlCheckBox.setOnAction(event -> {

            boolean needsUrl = needsUrlCheckBox.isSelected();
            urlField.setDisable(!needsUrl);

            if (!needsUrl) {
                urlField.clear();
            }
        });

        HBox buttons = new HBox(10);

        Button saveButton = new Button("Save");

        Button cancelButton = new Button("Cancel");

        buttons.getChildren().addAll(
                saveButton,
                cancelButton
        );

        saveButton.setOnAction(event ->
                saveEditedBookmark(
                        bookmark,
                        bookmarkCard,
                        editBox,
                        titleField,
                        descriptionField,
                        needsUrlCheckBox,
                        urlField
                )
        );

        cancelButton.setOnAction(event -> {

            int index = bookmarksList.getChildren().indexOf(editBox);

            bookmarksList.getChildren().set(index, bookmarkCard);
        });

        editBox.getChildren().addAll(
                titleField,
                descriptionField,
                needsUrlCheckBox,
                urlField,
                buttons
        );

        int index = bookmarksList.getChildren().indexOf(bookmarkCard);

        bookmarksList.getChildren().set(index, editBox);

        titleField.requestFocus();
        titleField.selectAll();
    }

    /**
     * Saves the edited bookmark in the database
     */
    private void saveEditedBookmark(Bookmark bookmark, VBox bookmarkCard, VBox editBox, TextField titleField, TextArea descriptionField, CheckBox needsUrlCheckBox, TextField urlField) {

        String title = titleField.getText().trim();

        String description = descriptionField.getText().trim();

        int needsUrl = needsUrlCheckBox.isSelected() ? 1 : 0;

        String url = urlField.getText().trim();

        if (title.isEmpty()) {
            return;
        }

        if (needsUrl == 1 && url.isEmpty()) {
            return;
        }

        try {

            bookmarkDatabase.updateBookmarkTitle(
                    bookmark.getBookmarkId(),
                    title
            );

            bookmarkDatabase.updateBookmarkDescription(
                    bookmark.getBookmarkId(),
                    description
            );

            bookmarkDatabase.updateBookmarkUrl(
                    bookmark.getBookmarkId(),
                    needsUrl,
                    url
            );

            bookmark.setTitle(title);
            bookmark.setDescription(description);
            bookmark.setNeedsUrl(needsUrl);
            bookmark.setUrl(url);

            int index = bookmarksList.getChildren().indexOf(editBox);

            bookmarksList.getChildren().set(index, bookmarkCard);

            loadBookmarks();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
