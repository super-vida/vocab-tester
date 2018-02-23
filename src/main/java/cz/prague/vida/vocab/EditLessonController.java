package cz.prague.vida.vocab;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import cz.prague.vida.vocab.entity.EditedWord;
import cz.prague.vida.vocab.entity.Lesson;
import cz.prague.vida.vocab.entity.Word;
import cz.prague.vida.vocab.entity.WordGroup;
import cz.prague.vida.vocab.persist.GuiGenerator;
import cz.prague.vida.vocab.persist.PersistentManager;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.beans.property.SimpleBooleanProperty;

public class EditLessonController implements Initializable {

	private Lesson lesson;
	@FXML
	private TableView<EditedWord> tableView;
	@FXML
	TableColumn<EditedWord, String> translation;
	@FXML
	TableColumn<EditedWord, String> word;
	@FXML
	TableColumn<EditedWord, Boolean> deleteAction;

	@FXML
	TextField newWord1TextField;
	@FXML
	TextField newWord2TextField;
	
	@FXML
	TextField lessonNameTextField;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		word.setCellValueFactory(new PropertyValueFactory<EditedWord, String>("word"));
		translation.setCellValueFactory(new PropertyValueFactory<EditedWord, String>("translation"));

		deleteAction.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EditedWord, Boolean>, ObservableValue<Boolean>>() {

			@Override
			public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<EditedWord, Boolean> p) {
				return new SimpleBooleanProperty(p.getValue() != null);
			}
		});

		// Adding the Button to the cell
		deleteAction.setCellFactory(new Callback<TableColumn<EditedWord, Boolean>, TableCell<EditedWord, Boolean>>() {

			@Override
			public TableCell<EditedWord, Boolean> call(TableColumn<EditedWord, Boolean> p) {
				return new ButtonCell();
			}

		});

	}

	// Define the button cell
	private class ButtonCell extends TableCell<EditedWord, Boolean> {
		final Button cellButton = new Button("Delete");

		ButtonCell() {

			// Action when the button is pressed
			cellButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent t) {
					// get Selected Item
					EditedWord editedWord = (EditedWord) ButtonCell.this.getTableView().getItems().get(ButtonCell.this.getIndex());
					System.out.println(editedWord);
					// System.out.println("select * from word_group where wg_w1_id = " + editedWord.getWordId() + ";");
					// remove selected item from the table list
					// data.remove(currentPerson);
					PersistentManager.getInstance().deleteWordGroup(lesson.getId(), editedWord.getWordId());
					tableView.getItems().remove(editedWord);
					lesson.setTotalCount(lesson.getTotalCount() - 1);
					PersistentManager.getInstance().persist(lesson);
					cellButton.setVisible(false);
				}
			});
		}

		// Display button if the row is not empty
		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {
				setGraphic(cellButton);
			}
		}
	}

	private boolean addNewWord(Set<EditedWord> set, Word word, Word translation) {

		for (EditedWord testedWord : set) {
			if (testedWord.getWord().equals(word.getText().trim())) {
				if (testedWord.getTranslation().indexOf(translation.getText().trim()) < 0) {
					testedWord.setTranslation(testedWord.getTranslation() + ", " + translation.getText());
				}
				return false;
			}
		}
		set.add(new EditedWord(word.getId(), word.getText(), translation.getText()));
		return true;

	}

	public List<EditedWord> loadVocabulary() {
		List<EditedWord> testedWords = new ArrayList<>();
		PersistentManager persistentManager = PersistentManager.getInstance();
		List<WordGroup> wordGroups = persistentManager.findWordGroups(lesson.getId());
		Set<EditedWord> set = new HashSet<>();
		try {

			for (WordGroup wordGroup : wordGroups) {
				Word word = persistentManager.getWord(wordGroup.getWord1Id());
				Word translation = persistentManager.getWord(wordGroup.getWord2Id());
				addNewWord(set, word, translation);
			}
		}
		finally {
			testedWords.addAll(set);
		}
		Collections.sort(testedWords);
		return testedWords;
	}

	public void initLesson(Lesson lesson) {
		this.lesson = lesson;
		tableView.getItems().clear();
		tableView.getItems().setAll(loadVocabulary());
		lessonNameTextField.setText(lesson.getName());
		lessonNameTextField.setEditable(false);
	}
	
	public void newLesson() {
		this.lesson = new Lesson();
		lessonNameTextField.setEditable(true);
	}
	

	public void addWord() {
		PersistentManager persistentManager = PersistentManager.getInstance();
		if (lesson.getId() == null) {
			lesson.setId(GuiGenerator.generateId());
			lesson.setLessonGroupId(1L);
			lesson.setName(lessonNameTextField.getText());
			lesson.setCorrectCount(0);
			lesson.setTotalCount(0);
		}
		String enText = newWord1TextField.getText().trim();
		String csText = newWord2TextField.getText().trim();
		Word enWord = persistentManager.findWord(enText, "en");
		Word csWord = persistentManager.findWord(csText, "cs");
		if (enWord == null) {
			enWord = new Word();
			enWord.setCorrectCount(0);
			enWord.setId(GuiGenerator.generateId());
			enWord.setIncorrectCount(0);
			enWord.setLanguage("en");
			enWord.setText(enText);
			persistentManager.persist(enWord);
		}
		if (csWord == null) {
			csWord = new Word();
			csWord.setCorrectCount(0);
			csWord.setId(GuiGenerator.generateId());
			csWord.setIncorrectCount(0);
			csWord.setLanguage("cs");
			csWord.setText(csText);
			persistentManager.persist(csWord);
		}
		WordGroup wordGroup = new WordGroup();
		wordGroup.setId(GuiGenerator.generateId());
		wordGroup.setLessonId(lesson.getId());
		wordGroup.setWord1Id(enWord.getId());
		wordGroup.setWord2Id(csWord.getId());
		persistentManager.persist(wordGroup);
		lesson.setTotalCount(lesson.getTotalCount() + 1);
		persistentManager.persist(lesson);
		newWord1TextField.setText("");
		newWord2TextField.setText("");
		initLesson(lesson);

	}

}
