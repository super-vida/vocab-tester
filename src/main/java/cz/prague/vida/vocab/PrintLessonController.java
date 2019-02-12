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
import cz.prague.vida.vocab.persist.PersistentManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PrintLessonController implements Initializable {

	private Lesson lesson;
	@FXML
	private TextArea tableView;
	

	@FXML
	TextField newWord1TextField;
	@FXML
	TextField newWord2TextField;
	
	@FXML
	TextField lessonNameTextField;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}


	public String loadVocabulary() {
		List<EditedWord> testedWords = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
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
		
		for (EditedWord editedWord : testedWords) {
			sb.append(editedWord.getWord());
			sb.append("=");
			sb.append(editedWord.getTranslation());
			sb.append("\n");
		}
		
		for (EditedWord editedWord : testedWords) {
			sb.append(editedWord.getWord());
			sb.append("\n");
		}
		
		return sb.toString();
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

	public void initLesson(Lesson lesson) {
		this.lesson = lesson;
		tableView.clear();
		tableView.setText(loadVocabulary());
		lessonNameTextField.setText(lesson.getName());
		lessonNameTextField.setEditable(false);
	}
	
	public void newLesson() {
		this.lesson = new Lesson();
		lessonNameTextField.setEditable(true);
	}
	


}
