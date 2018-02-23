package cz.prague.vida.vocab;

import static cz.prague.vida.vocab.VocabLogger.LOGGER;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToggleButton.ToggleButtonModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import cz.prague.vida.vocab.persist.PersistentManager;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * The Class VocabTester.
 */
public class VocabTester extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel panel1;
	private JPanel panel2;
	private JPanel panel4;
	private JPanel panel5;
	private JComboBox<Object> comboBoxFiles;
	//private JComboBox<Object> comboBoxMode;
	private JToggleButton buttonStart;
	//private JToggleButton buttonStatistic;
	//private JComboBox<Object> comboBoxDirrection;
	private JFormattedTextField textFieldTranslation;
	private JTextPane labelTestWord;
	private JToggleButton buttonCheckTransalation;
	private VocabHolderDB vocabHolder = new VocabHolderDB();
	private VocabConfigDB vocabConfig;
	private JLabel labelResult;
	private JLabel labelStatistics;
	private JPanel panel3;
	private static final String PREFERRED_LOOK_AND_FEEL = "javax.swing.plaf.metal.MetalLookAndFeel";
	private static final Object MODE_TEST = "Zkou\u0161et";
	private static final Object MODE_LEARN = "U\u010Dit";
	private static final String APP_FONT = "Arial";
	private boolean standardTestMode = true;
	private boolean running = false;
	private Proxy proxy = null;
	private JTextField textFieldNewLessonName;
	private JTextArea textAreaNewLesson;
	private JFrame frameNewLesson;
	private JRadioButtonMenuItem menuCheck;
	private JRadioButtonMenuItem menuLearn;
	private JRadioButtonMenuItem standardMenu;
	private JRadioButtonMenuItem menuOposite;

	/**
	 * Instantiates a new vocab tester.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public VocabTester() throws IOException {
		vocabConfig = new VocabConfigDB();
		if (vocabConfig.isStandardMode()) {
			ImageIcon myAppImage = loadIcon("icon.png");
			if (myAppImage != null) {
				setIconImage(myAppImage.getImage());
			}
		}
		if (vocabConfig.isSimpleMode()) {
			this.setUndecorated(true);
			Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
			setIconImage(icon);
		}
		initComponents();
	}

	private void intVocabularyList() {
		VocabularyListDB vocabularyList = new VocabularyListDB();
		vocabularyList.init();
		comboBoxFiles.setModel(new DefaultComboBoxModel<Object>(vocabularyList.getFileList()));
	}

	private void initConfig() throws IOException {
		vocabConfig = new VocabConfigDB();
	}

	private void initProxy() {
		String proxyString = vocabConfig.getProxy();
		int proxyPort = vocabConfig.getProxyPort();
		if (proxyString != null) {
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyString, proxyPort));
		}
	}

	private ImageIcon loadIcon(String strPath) {
		URL imgURL = getClass().getResource(strPath);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		}
		else {
			return null;
		}
	}

	private void initComponents() {
		setLayout(new GridLayout(5, 1));
		setForeground(Color.WHITE);
		setBackground(Color.WHITE);
		add(getPanel1());
		add(getPanel2());
		add(getPanel3());
		add(getPanel4());
		add(getJPanel5());
		createMenuBar();
		setSize(650, 270);
	}

	private void createMenuBar() {
		JMenuBar menubar = new JMenuBar();
		//menubar.setBorder(null);
		menubar.setBackground(Color.WHITE);
		JMenu menuMenu = new JMenu("Lekce");
		menuMenu.setMnemonic(KeyEvent.VK_M);
		JMenuItem menuItemAddLesson = new JMenuItem("Pøidat lekci");
		menuMenu.add(menuItemAddLesson);
		menubar.add(menuMenu);
		setJMenuBar(menubar);
		menuItemAddLesson.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				menuItemAddLessonActionPerformed(event);
			}
		});
		JMenuItem menuItemStats = new JMenuItem("Statistika");
		menuMenu.add(menuItemStats);
		menuItemStats.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				buttonStatisticActionPerformed(event);
			}
		});
		JMenu checkMenu = new JMenu("Zkoušet/Uèit");
		menuCheck = new JRadioButtonMenuItem("Zkoušet");
		menuCheck.setSelected(true);
		menuLearn = new JRadioButtonMenuItem("Uèit");
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(menuCheck);
		buttonGroup.add(menuLearn);
		checkMenu.add(menuCheck);
		checkMenu.add(menuLearn);
		menubar.add(checkMenu);
		
		JMenu directionMenu = new JMenu("Smìr");
		standardMenu = new JRadioButtonMenuItem("CS-X");
		standardMenu.setSelected(true);
		standardMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if (standardMenu.isSelected()) {
					standardTestMode = true;
				}
				else {
					standardTestMode = false;
				}
			}

			
		});
		menuOposite = new JRadioButtonMenuItem("X-CS");
		menuOposite.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if (menuOposite.isSelected()) {
					standardTestMode = false;
				}
				else {
					standardTestMode = true;
				}
			}

			
		});
		ButtonGroup buttonGroup2 = new ButtonGroup();
		buttonGroup2.add(standardMenu);
		buttonGroup2.add(menuOposite);
		directionMenu.add(standardMenu);
		directionMenu.add(menuOposite);
		menubar.add(directionMenu);
		
				
	}

	private void menuItemAddLessonActionPerformed(ActionEvent event) {
		frameNewLesson = new JFrame("Nová lekce");
		frameNewLesson.setLayout(new BorderLayout());
		frameNewLesson.setForeground(Color.WHITE);
		frameNewLesson.setBackground(Color.WHITE);
		frameNewLesson.setSize(new Dimension(500, 700));
		frameNewLesson.setLocation(this.getLocation());
		frameNewLesson.setAlwaysOnTop(this.isAlwaysOnTopSupported());
		frameNewLesson.setVisible(true);
		JPanel panelLessonName = new JPanel();
		panelLessonName.setForeground(Color.WHITE);
		panelLessonName.setBackground(Color.WHITE);
		JLabel labelName = new JLabel("Název lekce:");
		labelName.setFont(new Font(APP_FONT, Font.BOLD, 13));
		panelLessonName.add(labelName);
		textFieldNewLessonName = new JTextField(30);
		panelLessonName.add(textFieldNewLessonName);
		frameNewLesson.add(panelLessonName,BorderLayout.PAGE_START);
		
		//JPanel panelLesson = new JPanel();
		textAreaNewLesson = new JTextArea();
		textAreaNewLesson.setBackground(Color.WHITE);
		textAreaNewLesson.setFont(new Font(APP_FONT, Font.PLAIN, 16));
		JScrollPane scrolltxt = new JScrollPane(textAreaNewLesson);
		//scrolltxt.setSize(new Dimension(500, 600));
	
		//panelLesson.add(scrolltxt);
		frameNewLesson.add(scrolltxt,BorderLayout.CENTER);
		
		JPanel panelButton = new JPanel();
		JButton buttonSave = new JButton("Uložit");
		buttonSave.setFont(new Font(APP_FONT, Font.BOLD, 13));
		panelButton.add(buttonSave);
		frameNewLesson.add(panelButton,BorderLayout.PAGE_END);
		buttonSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				buttonSaveActionPerformed(event);
			}

			private void buttonSaveActionPerformed(ActionEvent event) {
				NewLessonCreator creator = new NewLessonCreator(); 
				creator.create(textFieldNewLessonName.getText(),textAreaNewLesson.getText(),1L);
				frameNewLesson.dispose();
				intVocabularyList();
				
			}
		});
		
	}

	private JLabel getLabelStatistics() {
		if (labelStatistics == null) {
			labelStatistics = new JLabel();
			labelStatistics.setFont(new Font(APP_FONT, Font.PLAIN, 13));
			labelStatistics.setPreferredSize(new Dimension(470, 40));
			labelStatistics.setHorizontalAlignment(SwingConstants.CENTER);
			labelStatistics.setText("");
		}
		return labelStatistics;
	}

	private JTextPane getLabelTestWord() {
		if (labelTestWord == null) {
			labelTestWord = new JTextPane();
			labelTestWord.setFont(new Font(APP_FONT, Font.PLAIN, vocabConfig.isStandardMode() ? 25 : 15));
			if (vocabConfig.isStandardMode()) {
				labelTestWord.setForeground(vocabConfig.isBlackAndWhiteMode() ? Color.BLACK : Color.BLUE);
			}
			labelTestWord.setEditable(false);
			StyledDocument doc = labelTestWord.getStyledDocument();
			SimpleAttributeSet center = new SimpleAttributeSet();
			StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
			doc.setParagraphAttributes(0, doc.getLength(), center, false);
		}
		return labelTestWord;
	}

	private JScrollPane getSrollPaneTestWord() {
		JScrollPane scrollPane = new JScrollPane(getLabelTestWord());
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(500, 50));
		scrollPane.setBorder(null);
		return scrollPane;
	}

	private JLabel getLabelResult() {
		if (labelResult == null) {
			labelResult = new JLabel();
			labelResult.setFont(new Font(APP_FONT, Font.PLAIN, vocabConfig.isStandardMode() ? 25 : 15));
			if (vocabConfig.isStandardMode()) {
				labelResult.setForeground(Color.GREEN);
			}
			labelResult.setHorizontalAlignment(SwingConstants.CENTER);
			labelResult.setPreferredSize(new Dimension(470, 40));
			labelResult.setText("");
			if (vocabConfig.isPronanciationReady()) {
				labelResult.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						try {
							if (labelResult.getText() != null && labelResult.getText().length() > 2) {
								playWord(labelResult.getText());
							}
						}
						catch (Exception e1) {
							LOGGER.info(e1.getMessage());
							labelResult.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						}
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						if (labelResult.getText() != null && labelResult.getText().length() > 0) {
							labelResult.setCursor(new Cursor(Cursor.HAND_CURSOR));

							labelResult.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(loadIcon("speaker.png").getImage(), new Point(0, 0), ""));

						}
						else {
							labelResult.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						}
					}
				});
			}
		}
		return labelResult;
	}

	private JToggleButton getButtonCheckTransalation() {
		if (buttonCheckTransalation == null) {
			buttonCheckTransalation = new JToggleButton();
			buttonCheckTransalation.setForeground(Color.BLACK);
			buttonCheckTransalation.setBackground(Color.WHITE);
			buttonCheckTransalation.setText(vocabConfig.isStandardMode() ? ">>>" : ">");
			buttonCheckTransalation.setBorder(null);
			buttonCheckTransalation.setEnabled(false);
			buttonCheckTransalation.setPreferredSize(new Dimension(70, 30));
			buttonCheckTransalation.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					buttonCheckTranslationActionPerformed(event);
				}
			});
		}
		return buttonCheckTransalation;
	}

	private JFormattedTextField getTextFieldTranslation() {
		if (textFieldTranslation == null) {
			textFieldTranslation = new JFormattedTextField();
			textFieldTranslation.setFont(new Font(APP_FONT, Font.PLAIN, vocabConfig.isStandardMode() ? 25 : 15));
			textFieldTranslation.setForeground(Color.BLACK);
			textFieldTranslation.setHorizontalAlignment(SwingConstants.CENTER);
			textFieldTranslation.setText("");
			textFieldTranslation.setEditable(false);
			textFieldTranslation.setPreferredSize(new Dimension(500, 40));
			if (vocabConfig.isSimpleMode()) {
				textFieldTranslation.setBorder(null);
			}
			textFieldTranslation.addKeyListener(new KeyAdapter() {

				@Override
				public void keyReleased(KeyEvent event) {
					jFormattedTextField0KeyKeyReleased(event);
				}
			});
		}
		return textFieldTranslation;
	}

	private JToggleButton getButtonStart() {
		if (buttonStart == null) {
			buttonStart = new JToggleButton();
			buttonStart.setText("Start");
			if (vocabConfig.isStandardMode()) {
				buttonStart.setForeground(Color.WHITE);
				buttonStart.setBackground(Color.BLACK);
			}
			else {
				buttonStart.setBackground(Color.WHITE);
				buttonStart.setBorder(null);
			}
			buttonStart.setPreferredSize(new Dimension(70, 28));
			buttonStart.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					buttonStartActionPerformed(event);
				}
			});
		}
		return buttonStart;
	}

	/**
	 * Gets the button statistic.
	 *
	 * @return the button statistic
	 */
//	public JToggleButton getButtonStatistic() {
//		if (buttonStatistic == null) {
//			buttonStatistic = new JToggleButton();
//			ImageIcon myAppImage = loadIcon("info.png");
//			buttonStatistic.setIcon(myAppImage);
//			buttonStatistic.setPreferredSize(new Dimension(10, 10));
//			buttonStatistic.setBackground(Color.WHITE);
//			buttonStatistic.setBorder(null);
//			buttonStatistic.addActionListener(new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent event) {
//					buttonStatisticActionPerformed(event);
//				}
//			});
//		}
//		return buttonStatistic;
//	}

//	private JComboBox<Object> getComboBoxDirection() {
//		if (comboBoxDirrection == null) {
//			comboBoxDirrection = new JComboBox<>();
//			comboBoxDirrection.setBackground(Color.WHITE);
//			comboBoxDirrection.setModel(new DefaultComboBoxModel<>(new Object[] { "CS-X", "X-CS" }));
//			comboBoxDirrection.setDoubleBuffered(false);
//			comboBoxDirrection.setBorder(null);
//			comboBoxDirrection.setPreferredSize(new Dimension(55, 28));
//			comboBoxDirrection.addActionListener(new ActionListener() {
//
//				@Override
//				public void actionPerformed(ActionEvent event) {
//					comboBoxDirrectionActionPerformed(event);
//				}
//
//				private void comboBoxDirrectionActionPerformed(ActionEvent event) {
//					if (comboBoxDirrection.getSelectedIndex() == 0) {
//						standardTestMode = true;
//					}
//					else if (comboBoxDirrection.getSelectedIndex() == 1) {
//						standardTestMode = false;
//					}
//				}
//			});
//		}
//		return comboBoxDirrection;
//	}

	private JComboBox<Object> getComboBoxFiles() {
		if (comboBoxFiles == null) {
			comboBoxFiles = new JComboBox<>();
			comboBoxFiles.setBackground(Color.WHITE);
			comboBoxFiles.setModel(new DefaultComboBoxModel<>(new Object[] { "item0", "item1", "item2", "item3" }));
			comboBoxFiles.setDoubleBuffered(false);
			comboBoxFiles.setBorder(null);
			comboBoxFiles.setPreferredSize(new Dimension(500, 28));
		}
		return comboBoxFiles;
	}

	/**
	 * Gets the combo box mode.
	 *
	 * @return the combo box mode
	 */
//	public JComboBox<Object> getComboBoxMode() {
//		if (comboBoxMode == null) {
//			comboBoxMode = new JComboBox<>();
//			comboBoxMode.setBackground(Color.WHITE);
//			comboBoxMode.setModel(new DefaultComboBoxModel<>(new Object[] { MODE_TEST, MODE_LEARN }));
//			comboBoxMode.setDoubleBuffered(false);
//			comboBoxMode.setBorder(null);
//			comboBoxMode.setPreferredSize(new Dimension(80, 28));
//		}
//		return comboBoxMode;
//	}

	private JPanel getJPanel5() {
		if (panel5 == null) {
			panel5 = new JPanel();
			panel5.setBackground(Color.WHITE);
			panel5.setLayout(new FlowLayout());
			panel5.add(getLabelStatistics());
			panel5.add(getFiller());
		}
		return panel5;
	}

	private JPanel getPanel4() {
		if (panel4 == null) {
			panel4 = new JPanel();
			panel4.setBackground(Color.WHITE);
			panel4.setLayout(new FlowLayout());
			panel4.add(getTextFieldTranslation());
			panel4.add(getButtonCheckTransalation());
		}
		return panel4;
	}

	private JPanel getPanel2() {
		if (panel2 == null) {
			panel2 = new JPanel();
			panel2.setBackground(Color.WHITE);
			panel2.setLayout(new FlowLayout());
			panel2.add(getSrollPaneTestWord());
			panel2.add(getFiller());
		}
		return panel2;
	}

	private JPanel getPanel3() {
		if (panel3 == null) {
			panel3 = new JPanel();
			panel3.setBackground(Color.WHITE);
			panel3.setLayout(new FlowLayout());
			panel3.add(getLabelResult());
			panel3.add(getFiller());
		}
		return panel3;
	}

	private JLabel getFiller() {
		JLabel filler = new JLabel("");
		filler.setPreferredSize(new Dimension(70, 40));
		return filler;
	}

	private JPanel getPanel1() {
		if (panel1 == null) {
			panel1 = new JPanel();
			panel1.setBackground(Color.WHITE);
			panel1.add(getComboBoxFiles());
			//panel1.add(getComboBoxMode());
			//panel1.add(getComboBoxDirection());
			//panel1.add(getButtonStatistic());
			panel1.add(getButtonStart());
		}
		return panel1;
	}

	private void playWord(String englishWord) throws JavaLayerException, IOException {
		String pronanciationUrl = vocabConfig.getPronanciationUrl();
		if (pronanciationUrl != null) {
			URL url = new URL(pronanciationUrl.replace("REPLACE", URLEncoder.encode(englishWord.toLowerCase(), "UTF-8")));
			HttpURLConnection httpcon = proxy != null ? (HttpURLConnection) url.openConnection(proxy) : (HttpURLConnection) url.openConnection();
			new Player(httpcon.getInputStream()).play();
		}
	}

	private static void installLnF() {
		try {
			String lnfClassname = PREFERRED_LOOK_AND_FEEL;
			if (lnfClassname == null) {
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
			}
			UIManager.setLookAndFeel(lnfClassname);
		}
		catch (Exception e) {
			System.err.println("Cannot install " + PREFERRED_LOOK_AND_FEEL + " on this platform:" + e.getMessage());
		}
	}

	/**
	 * Main entry of the class. Note: This class is only created so that you can easily preview the result at runtime. It is not
	 * expected to be managed by the designer. You can modify it as you like.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		installLnF();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				VocabTester frame = null;
				try {
					frame = new VocabTester();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.intVocabularyList();
					frame.initConfig();
					frame.initProxy();
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					frame.setResizable(false);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					if (frame.vocabConfig.isStandardMode()) {
						frame.setTitle("jV vocabulary");
					}
					frame.getContentPane().setPreferredSize(frame.getSize());

					FrameDragListener frameDragListener = new FrameDragListener(frame);
					frame.addMouseListener(frameDragListener);
					frame.addMouseMotionListener(frameDragListener);

					frame.pack();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);

					frame.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							PersistentManager.getInstance().close();
						}
					});
				}

				catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	private void jFormattedTextField0KeyKeyReleased(KeyEvent event) {
		int key = event.getKeyCode();
		if (key == KeyEvent.VK_ENTER) {
			action();
			return;
		}
		TestedWord testedWord = vocabHolder.getCurrentTestWord();
		if (standardTestMode && testedWord.getWord2().getText().equalsIgnoreCase(textFieldTranslation.getText())) {
			if (vocabConfig.isStandardMode()) {
				textFieldTranslation.setForeground(Color.GREEN);
			}
			labelResult.setText(testedWord.getWord2().getText());
		}
		else if (standardTestMode && testedWord.getWord2().getText().toLowerCase().startsWith(textFieldTranslation.getText().toLowerCase())) {
			textFieldTranslation.setForeground(Color.BLACK);
		}
		else if (!standardTestMode && checkIfTranslationMatchWholeWord(testedWord)) {
			if (vocabConfig.isStandardMode()) {
				textFieldTranslation.setForeground(Color.GREEN);
			}
			labelResult.setText(testedWord.getWord1().getText());
		}
		else if (!standardTestMode && checkIfTranslationStartsWithWord(testedWord)) {
			textFieldTranslation.setForeground(Color.BLACK);
		}
		else {
			if (vocabConfig.isStandardMode()) {
				textFieldTranslation.setForeground(Color.RED);
			}
			else {
				textFieldTranslation.setForeground(Color.LIGHT_GRAY);
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
			textFieldTranslation.setForeground(vocabConfig.isStandardMode() ? Color.red : Color.lightGray);
			textFieldTranslation.setEditable(false);
			labelResult.setText(standardTestMode ? testedWord.getWord2().getText() : testedWord.getWord1().getText());
			testedWord.addAnswer(false);
			vocabHolder.addAnswer(false);
			vocabHolder.addIncorrectWord(testedWord);
			return false;
		}

	}

	private void processLearnMode() {
		vocabHolder.removeLearnedWord(vocabHolder.getCurrentTestWord());
		if (vocabHolder.prepareNextPair()) {
			TestedWord testedWord = vocabHolder.getCurrentTestWord();
			labelTestWord.setText(testedWord.getWord1().getText());
			textFieldTranslation.setText(testedWord.getWord2().getText());
		}
		else {
			labelResult.setText("KONEC");
			setNotRunning();
		}
		labelStatistics.setText("Celkem: " + vocabHolder.getTotalWords() + ", Zobrazeno: " + vocabHolder.getRestCount());
	}

	private void action() {

		if (menuLearn.isSelected()) {
			processLearnMode();
			return;
		}

		if (!textFieldTranslation.isEditable() || processCheck()) {
			textFieldTranslation.setEditable(true);
			labelResult.setText("");
			textFieldTranslation.setText("");
			labelTestWord.setText("");
			if (vocabHolder.prepareNextPair()) {
				TestedWord testedWord = vocabHolder.getCurrentTestWord();
				textFieldTranslation.requestFocus();

				if (standardTestMode && testedWord.getWord1().getText().length() > 50) {
					labelTestWord.setFont(new Font(APP_FONT, Font.PLAIN, vocabConfig.isStandardMode() ? getFontHeight(vocabHolder.getCurrentTestWord().getWord1().getText().length()) : 15));
				}
				else {
					labelTestWord.setFont(new Font(APP_FONT, Font.PLAIN, vocabConfig.isStandardMode() ? 25 : 15));
				}
				labelTestWord.setText(standardTestMode ? testedWord.getWord1().getText() : testedWord.getWord2().getText());
				labelTestWord.setToolTipText(standardTestMode ? testedWord.getWord1().getText() : testedWord.getWord2().getText());
				labelStatistics.setText("Celkem: " + vocabHolder.getTotalWords() + ", Spr\u00E1vn\u011B: " + vocabHolder.getTotalCorrectAnswers() + " \u00DAsp\u011B\u0161nost slov\u00ED\u010Dka: " + testedWord.getPercentage() + "% " + testedWord.getAnswers() + "x (" + testedWord.getCorrectAnswers() + "/" + vocabConfig.getMatchedWordCount() + ")");
			}
			else {
				labelResult.setText("KONEC");
				textFieldTranslation.setEditable(false);
				getLabelStatistics().setText("Celkem slov\u00ED\u010Dek: " + vocabHolder.getTotalWords() + ", Celkov\u00E1 \u00FAsp\u011B\u0161nost: " + vocabHolder.getPercentage() + "%  (" + vocabHolder.getTotalCorrectAnswers() + "/" + vocabHolder.getTotalAnswers() + ")");
				Object o = comboBoxFiles.getSelectedItem();
				vocabHolder.updateLessonStats();
				vocabHolder.createIncorrectLesson();
				intVocabularyList();
				comboBoxFiles.setSelectedItem(o);
				setNotRunning();
			}
		}
	}

	private void buttonCheckTranslationActionPerformed(ActionEvent event) {
		action();
	}

	private void setNotRunning() {
		buttonStart.setText("Start");
		if (vocabConfig.isStandardMode()) {
			buttonStart.setForeground(Color.WHITE);
		}
		comboBoxFiles.setEnabled(true);
		//comboBoxMode.setEnabled(true);
		//comboBoxDirrection.setEnabled(true);

		if (vocabConfig.isSimpleMode()) {
			comboBoxFiles.setVisible(true);
			//comboBoxMode.setVisible(true);
			//comboBoxDirrection.setVisible(true);
			buttonStart.setVisible(true);
			labelStatistics.setVisible(true);
			//buttonStatistic.setVisible(true);
			setSize(650, 270);
		}

		running = false;
	}

	private void setRunning() {
		buttonStart.setText("Stop");
		if (vocabConfig.isStandardMode()) {
			buttonStart.setForeground(Color.RED);
		}
		else {
			buttonStart.setBackground(Color.WHITE);
		}
		comboBoxFiles.setEnabled(false);
		//comboBoxMode.setEnabled(false);
		//comboBoxDirrection.setEnabled(false);
		if (vocabConfig.isSimpleMode()) {
			comboBoxFiles.setVisible(false);
			//comboBoxMode.setVisible(false);
			//comboBoxDirrection.setVisible(false);
			buttonStart.setVisible(false);
			labelStatistics.setVisible(false);
			//buttonStatistic.setVisible(false);
		}

		running = true;
	}

	private void buttonStartActionPerformed(ActionEvent event) {
		if (running) {
			setNotRunning();
			return;
		}
		vocabHolder.loadVocabularyFile((String) comboBoxFiles.getSelectedItem(),null);
		vocabHolder.shuffleWords();

		if (standardTestMode && vocabHolder.getCurrentTestWord().getWord1().getText().length() > 50) {
			labelTestWord.setFont(new Font(APP_FONT, Font.PLAIN, vocabConfig.isStandardMode() ? getFontHeight(vocabHolder.getCurrentTestWord().getWord1().getText().length()) : 15));
		}
		else {
			labelTestWord.setFont(new Font(APP_FONT, Font.PLAIN, vocabConfig.isStandardMode() ? 25 : 15));
		}

		labelTestWord.setText(standardTestMode ? vocabHolder.getCurrentTestWord().getWord1().getText() : vocabHolder.getCurrentTestWord().getWord2().getText());
		labelTestWord.setToolTipText(standardTestMode ? vocabHolder.getCurrentTestWord().getWord1().getText() : vocabHolder.getCurrentTestWord().getWord2().getText());
		labelResult.setText("");
		labelStatistics.setText("Celkem slov\u00ED\u010Dek: " + vocabHolder.getTotalWords());
		buttonCheckTransalation.setEnabled(true);
		textFieldTranslation.setEditable(true);
		textFieldTranslation.setText("");
		textFieldTranslation.requestFocus();
		if (menuLearn.isSelected()) {
			textFieldTranslation.setText(vocabHolder.getCurrentTestWord().getWord2().getText());
			textFieldTranslation.setForeground(Color.GREEN);
			textFieldTranslation.setEditable(false);
		}
		setRunning();
	}

	private int getFontHeight(int length) {
		return 22;
	}

	private void buttonStatisticActionPerformed(ActionEvent event) {
		JDialog dialog = new JOptionPane(new VocabCounter().count()).createDialog(this, "Statistika slovíèek");
		dialog.setLocation(this.getLocation());
		dialog.setAlwaysOnTop(this.isAlwaysOnTopSupported());
		dialog.setVisible(true);
	}

}
