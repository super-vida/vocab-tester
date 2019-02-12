package cz.prague.vida.vocab;

import static cz.prague.vida.vocab.VocabLogger.LOGGER;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

import cz.prague.vida.vocab.entity.Lesson;
import cz.prague.vida.vocab.entity.LessonCheck;
import cz.prague.vida.vocab.entity.LessonGroup;
import cz.prague.vida.vocab.persist.PersistentManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

/**
 * The Class VocabController.
 */
public class VocabController implements Initializable {

	@FXML
	private TableView<Lesson> tableView;
	@FXML
	private TableColumn<Lesson, String> name;
	@FXML
	private TableColumn<Lesson, String> totalCount;
	@FXML
	private TableColumn<Lesson, String> correctCount;
	@FXML
	private TableColumn<Lesson, String> lessonPercentageColumn;
	@FXML
	private TableColumn<Lesson, String> checkTimeFormated;
	@FXML
	TextField textFieldTranslation;
	@FXML
	TextFlow labelTestWord;
	private VocabHolderDB vocabHolder;
	@FXML
	Label labelResult;
	private boolean standardTestMode = true;
	private VocabConfigDB vocabConfig;

	@FXML
	Label lessonTotalCountLabel;
	@FXML
	Label wordLessonLabel;
	@FXML
	Label lessonTotalCorrectLabel;
	@FXML
	Label wordTotalCorrectLabel;
	@FXML
	Label wordTotalIncorrectLabel;
	@FXML
	Label wordTotalVocabularyLabel;
	@FXML
	Label wordTotalVocabularyCorrectLabel;
	@FXML
	Label wordTotalDistincVocabularyLabel;
	@FXML
	Label lessonCountLabel;
	@FXML
	Label lastCorrectAnswerLabel;
	@FXML
	Label lastIncorrectAnswerLabel;
	@FXML
	ComboBox<Object> vocabularyCombo;
	@FXML
	TableView<LessonCheck> lessonCheckTableView;
	@FXML
	TableColumn<LessonCheck, String> checkTotalCountColumn;
	@FXML
	TableColumn<LessonCheck, String> checkCorrectCountColumn;
	@FXML
	TableColumn<LessonCheck, String> percentColumn;
	@FXML
	TableColumn<LessonCheck, String> checkPercentColumn;
	@FXML
	TableColumn<LessonCheck, String> checkTimeColumn;

	@FXML
	TableColumn<LessonCheck, String> checkDurationtColumn;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		LOGGER.info("init");
		name.setCellValueFactory(new PropertyValueFactory<Lesson, String>("name"));
		totalCount.setCellValueFactory(new PropertyValueFactory<Lesson, String>("totalCount"));
		correctCount.setCellValueFactory(new PropertyValueFactory<Lesson, String>("correctCount"));
		lessonPercentageColumn.setCellValueFactory(new PropertyValueFactory<Lesson, String>("percentage"));
		checkTimeFormated.setCellValueFactory(new PropertyValueFactory<Lesson, String>("checkTimeFormated"));

		checkTimeColumn.setCellValueFactory(new PropertyValueFactory<LessonCheck, String>("timeFormated"));
		checkTotalCountColumn.setCellValueFactory(new PropertyValueFactory<LessonCheck, String>("totalCount"));
		checkCorrectCountColumn.setCellValueFactory(new PropertyValueFactory<LessonCheck, String>("correctCount"));
		checkPercentColumn.setCellValueFactory(new PropertyValueFactory<LessonCheck, String>("percentage"));
		checkDurationtColumn.setCellValueFactory(new PropertyValueFactory<LessonCheck, String>("durationFormated"));
		PersistentManager.getInstance().updateOldLessons();
		refreshLessons();
		List<LessonGroup> lessonGroups = PersistentManager.getInstance().getAllLessonGroups();
		List<Object> groupNames = new ArrayList<>();
		for (LessonGroup group : lessonGroups) {
			groupNames.add(group.getName());
		}
		vocabularyCombo.getItems().setAll(groupNames);
		vocabularyCombo.setValue(groupNames.get(0));
		try {
			vocabConfig = new VocabConfigDB();
			vocabConfig.init();
		}
		catch (IOException e) {
			LOGGER.log(Level.SEVERE, "", e);
		}
		textFieldTranslation.setBorder(Border.EMPTY);
		fillTotalVocabStats();
	}

	private void refreshLessons() {
		tableView.getItems().clear();
		tableView.getItems().setAll(PersistentManager.getInstance().getAllLessons());
	}

	private void fillTotalVocabStats() {
		wordTotalVocabularyCorrectLabel.setText(String.valueOf(PersistentManager.getInstance().getWordTotalCorrectCount()));
		wordTotalVocabularyLabel.setText(String.valueOf(PersistentManager.getInstance().getWordTotalCount()));
		wordTotalDistincVocabularyLabel.setText(String.valueOf(PersistentManager.getInstance().getDistinctCount()));
		lessonCountLabel.setText(String.valueOf(PersistentManager.getInstance().getLessonCount()));
	}

	private void cleanOldValues() {
		labelResult.setText("");
		textFieldTranslation.setText("");
		lessonTotalCorrectLabel.setText("");
		lessonTotalCountLabel.setText("");
		wordLessonLabel.setText("");
		labelTestWord.getChildren().clear();
		textFieldTranslation.setText("");
	}

	private Text getTestText() {
		Text text = new Text(standardTestMode ? vocabHolder.getCurrentTestWord().getWord1().getText() : vocabHolder.getCurrentTestWord().getWord2().getText());
		text.setFont(Font.font(19));
		text.setFill(javafx.scene.paint.Color.BLUE);
		return text;
	}

	/**
	 * Check lesson.
	 */
	public void checkLesson() {
		cleanOldValues();
		Lesson lesson = tableView.getSelectionModel().getSelectedItem();
		if (lesson == null) {
			return;
		}
		vocabHolder = new VocabHolderDB();
		vocabHolder.loadVocabularyFile(lesson.getName(), lesson);
		vocabHolder.shuffleWords();
		TestedWord testedWord = vocabHolder.getCurrentTestWord();
		// labelTestWord.setText(testedWord.getWord1().getText());
		textFieldTranslation.requestFocus();

		labelTestWord.getChildren().add(getTestText());
		// labelTestWord.setToolTipText(standardTestMode ? vocabHolder.getCurrentTestWord().getWord1().getText() :
		// vocabHolder.getCurrentTestWord().getWord2().getText());

		lessonTotalCountLabel.setText(String.valueOf(vocabHolder.getTotalWords()));
		setWordTotalStatistic(testedWord);
		// buttonCheckTransalation.setEnabled(true);
		textFieldTranslation.setEditable(true);

		textFieldTranslation.requestFocus();
		// if (menuLearn.isSelected()) {
		// textFieldTranslation.setText(vocabHolder.getCurrentTestWord().getWord2().getText());
		// textFieldTranslation.setForeground(Color.GREEN);
		// textFieldTranslation.setEditable(false);
		// }
		loadLessonHistory(lesson);
	}

	private void loadLessonHistory(Lesson lesson) {
		lessonCheckTableView.getItems().clear();
		lessonCheckTableView.getItems().addAll(PersistentManager.getInstance().getLessonHistory(lesson));

	}

	private String formatDate(Date date) {
		if (date != null) {
			return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date);
		}
		return "";
	}

	private void setWordTotalStatistic(TestedWord testedWord) {
		wordTotalCorrectLabel.setText(String.valueOf(testedWord.getWord2().getCorrectCount()));
		wordTotalIncorrectLabel.setText(String.valueOf(testedWord.getWord2().getIncorrectCount()));
		lastCorrectAnswerLabel.setText(formatDate(testedWord.getWord2().getCorrectTime()));
		lastIncorrectAnswerLabel.setText(formatDate(testedWord.getWord2().getIncorrectTime()));
	}

	private void action() {

		if (!textFieldTranslation.isEditable() || processCheck()) {
			textFieldTranslation.setEditable(true);
			labelResult.setText("");
			textFieldTranslation.setText("");
			labelTestWord.getChildren().clear();
			if (vocabHolder.prepareNextPair()) {
				TestedWord testedWord = vocabHolder.getCurrentTestWord();
				textFieldTranslation.requestFocus();

				labelTestWord.getChildren().add(getTestText());
				// Tooltip tooltip = standardTestMode? new Tooltip().setText(testedWord.getWord1().getText()) : new
				// Tooltip().setText(testedWord.getWord2().getText());
				// labelTestWord.setToolTip(tool);
				lessonTotalCorrectLabel.setText(String.valueOf(vocabHolder.getTotalCorrectAnswers()));
				// labelStatistics.setText("Celkem: " + vocabHolder.getTotalWords() + ", Spr\u00E1vn\u011B: " +
				// vocabHolder.getTotalCorrectAnswers() + "\u00DAsp\u011B\u0161nost slov\u00ED\u010Dka: " +
				// testedWord.getPercentage() + "% " + testedWord.getAnswers() + "x (" + testedWord.getCorrectAnswers() + "/" +
				// vocabConfig.getMatchedWordCount() + ")");
				wordLessonLabel.setText(testedWord.getPercentage() + "% " + testedWord.getAnswers() + "x (" + testedWord.getCorrectAnswers() + "/" + vocabConfig.getMatchedWordCount() + ")");
				setWordTotalStatistic(testedWord);
			}
			else {
				labelResult.setText("KONEC");
				textFieldTranslation.setEditable(false);
				lessonTotalCorrectLabel.setText(vocabHolder.getPercentage() + "% (" + vocabHolder.getTotalCorrectAnswers() + "/" + vocabHolder.getTotalAnswers() + ")");
				// Object o = comboBoxFiles.getSelectedItem();
				vocabHolder.updateLessonStats();
				vocabHolder.createIncorrectLesson();
				refreshLessons();
				fillTotalVocabStats();
				// intVocabularyList();
				// comboBoxFiles.setSelectedItem(o);
				// setNotRunning();
			}
		}

	}

	private boolean processCheck() {
		TestedWord testedWord = vocabHolder.getCurrentTestWord();
		if (check(testedWord)) {
			testedWord.addAnswer(true);
			vocabHolder.addAnswer(true);
			if (testedWord.getCorrectAnswers() == vocabConfig.getMatchedWordCount()) {
				vocabHolder.removeLearnedWord(testedWord);
			}
			return true;
		}
		else {
			textFieldTranslation.setStyle(vocabConfig.isStandardMode() ? "-fx-text-fill:red;" : "-fx-text-fill:gray;");
			textFieldTranslation.setEditable(false);
			labelResult.setText(standardTestMode ? testedWord.getWord2().getText() : testedWord.getWord1().getText());
			testedWord.addAnswer(false);
			vocabHolder.addAnswer(false);
			vocabHolder.addIncorrectWord(testedWord);
			return false;
		}

	}

	private boolean check(TestedWord testedWord) {
		if (standardTestMode && testedWord.getWord2().getText().equalsIgnoreCase(textFieldTranslation.getText())) {
			return true;
		}
		else {
			String[] words = testedWord.getWord1().getText().split(",");
			for (int i = 0; i < words.length; i++) {
				if (words[i].equalsIgnoreCase(textFieldTranslation.getText())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Text field translation key released.
	 *
	 * @param event
	 *            the event
	 */
	@FXML
	public void textFieldTranslationKeyReleased(KeyEvent event) {
		KeyCode key = event.getCode();
		if (key == KeyCode.ENTER) {
			action();
			return;
		}
		TestedWord testedWord = vocabHolder.getCurrentTestWord();
		if (standardTestMode && testedWord.getWord2().getText().equalsIgnoreCase(textFieldTranslation.getText())) {
			if (vocabConfig.isStandardMode()) {
				textFieldTranslation.setStyle("-fx-text-fill:green;");
			}
			labelResult.setText(testedWord.getWord2().getText());
		}
		else if (standardTestMode && testedWord.getWord2().getText().toLowerCase().startsWith(textFieldTranslation.getText().toLowerCase())) {
			textFieldTranslation.setStyle("-fx-text-fill:black;");
		}
		else if (!standardTestMode && checkIfTranslationMatchWholeWord(testedWord)) {
			if (vocabConfig.isStandardMode()) {
				textFieldTranslation.setStyle("-fx-text-fill:green;");
			}
			labelResult.setText(testedWord.getWord1().getText());
		}
		else if (!standardTestMode && checkIfTranslationStartsWithWord(testedWord)) {
			textFieldTranslation.setStyle("-fx-text-fill:black;");
		}
		else {
			if (vocabConfig.isStandardMode()) {
				textFieldTranslation.setStyle("-fx-text-fill:red;");
			}
			else {
				textFieldTranslation.setStyle("-fx-text-fill:gray;");
			}
		}
	}

	private boolean checkIfTranslationMatchWholeWord(TestedWord testedWord) {
		String[] words = testedWord.getWord1().getText().split(",");
		for (int i = 0; i < words.length; i++) {
			if (words[i].equalsIgnoreCase(textFieldTranslation.getText())) {
				return true;
			}
		}
		return false;
	}

	private boolean checkIfTranslationStartsWithWord(TestedWord testedWord) {
		String[] words = testedWord.getWord1().getText().split(",");
		for (int i = 0; i < words.length; i++) {
			if (words[i].toLowerCase().startsWith(textFieldTranslation.getText().toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the selected lesson.
	 *
	 * @return the selected lesson
	 */
	public Lesson getSelectedLesson() {
		return tableView.getSelectionModel().getSelectedItem();
	}

	/**
	 * Delete lesson.
	 */
	@FXML
	public void deleteLesson() {
		Lesson lesson = tableView.getSelectionModel().getSelectedItem();
		if (lesson == null) {
			return;
		}
		PersistentManager.getInstance().deleteLesson(lesson);
		refreshLessons();
		fillTotalVocabStats();
	}

	/**
	 * New lesson from file.
	 */
	@FXML
	public void newLessonFromFile() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("new_lesson_from_file_window.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Nová lekce ze souboru");
			stage.setScene(new Scene(root, 400, 600));
			stage.showAndWait();
			refreshLessons();
			fillTotalVocabStats();
		}
		catch (Exception e) {
			LOGGER.log(Level.SEVERE, "", e);
		}

	}

	/**
	 * New lesson.
	 */
	@FXML
	public void newLesson() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("edit_lesson.fxml"));
			Parent root = loader.load();
			EditLessonController controller = loader.<EditLessonController> getController();
			controller.newLesson();
			Stage stage = new Stage();
			stage.setTitle("Nová lekce");
			stage.setScene(new Scene(root, 550, 850));
			stage.showAndWait();
			refreshLessons();
			fillTotalVocabStats();
		}
		catch (Exception e) {
			LOGGER.log(Level.SEVERE, "", e);
		}
	}

	/**
	 * Edits the lesson.
	 */
	@FXML
	public void editLesson() {
		try {
			Lesson editedLesson = getSelectedLesson();
			if (editedLesson == null) {
				return;
			}
			FXMLLoader loader = new FXMLLoader(getClass().getResource("edit_lesson.fxml"));
			Parent root = loader.load();
			EditLessonController controller = loader.<EditLessonController> getController();
			controller.initLesson(editedLesson);
			Stage stage = new Stage();
			stage.setTitle("Editace lekce");
			stage.setScene(new Scene(root, 550, 850));
			stage.showAndWait();
			refreshLessons();
			fillTotalVocabStats();
		}
		catch (Exception e) {
			LOGGER.log(Level.SEVERE, "", e);
		}
	}
	
	/**
	 * Edits the lesson.
	 */
	@FXML
	public void printLesson() {
		try {
			Lesson editedLesson = getSelectedLesson();
			if (editedLesson == null) {
				return;
			}
			FXMLLoader loader = new FXMLLoader(getClass().getResource("print_lesson.fxml"));
			Parent root = loader.load();
			PrintLessonController controller = loader.<PrintLessonController> getController();
			controller.initLesson(editedLesson);
			Stage stage = new Stage();
			stage.setTitle("Tisk lekce");
			stage.setScene(new Scene(root, 550, 850));
			stage.showAndWait();
			refreshLessons();
			fillTotalVocabStats();
		}
		catch (Exception e) {
			LOGGER.log(Level.SEVERE, "", e);
		}
	}

	@FXML
	public void exitProgram() {
		PersistentManager.getInstance().close();
		Platform.exit();
		System.exit(0);
	}

	@FXML
	public void editConfig() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("config.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Konfigurace");
			stage.setScene(new Scene(root, 550, 650));
			stage.showAndWait();
		}
		catch (Exception e) {
			LOGGER.log(Level.SEVERE, "", e);
		}
	}

}
