package cz.prague.vida.vocab;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewLessonFromFileWindow {

	@FXML
	TextField newLessonTextField;
	@FXML TextArea newLessonTextArea;
	@FXML Button createButton;
	@FXML public void createLesson() {
		NewLessonCreator creator = new NewLessonCreator(); 
		if (newLessonTextField.getText().isEmpty() || newLessonTextArea.getText().isEmpty()) {
			return;
		}
		creator.create(newLessonTextField.getText(),newLessonTextArea.getText(),1L);
		Stage stage = (Stage) createButton.getScene().getWindow();
		Stage owner = (Stage) stage.getOwner();
	    stage.close();
		
//		frameNewLesson.dispose();
//		intVocabularyList();
	}

}
