package cz.prague.vida.vocab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.text.Normalizer;

import javazoom.jl.decoder.JavaLayerException;

/**
 * The Class VocabPrinter.
 */
public class VocabularyMp3Creator {

	private VocabConfigDB vocabConfig;
	private Proxy proxy;

	private VocabularyMp3Creator() {
		super();
	}

	private void createmp3(File folder, String word, String fileName, boolean english) throws JavaLayerException, IOException {
		String pronanciationUrl = english ? vocabConfig.getPronanciationUrl() : vocabConfig.getPronanciationUrlCzech();
		if (pronanciationUrl != null) {
			try {
				URL url = new URL(pronanciationUrl.replace("REPLACE", URLEncoder.encode(Normalizer.normalize(word.toLowerCase(),Normalizer.Form.NFD),"UTF-8")));
				HttpURLConnection httpcon = proxy != null ? (HttpURLConnection) url.openConnection(proxy) : (HttpURLConnection) url.openConnection();
				try (InputStream inputStream = httpcon.getInputStream()) {
					try (FileOutputStream outputStream = new FileOutputStream(new File(folder, fileName + ".mp3"))) {
						int read;
						byte[] bytes = new byte[1024];

						while ((read = inputStream.read(bytes)) != -1) {
							outputStream.write(bytes, 0, read);
						}

					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	

	private void initProxy() {
		String proxyString = vocabConfig.getProxy();
		int proxyPort = vocabConfig.getProxyPort();
		if (proxyString != null) {
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyString, proxyPort));
		}
	}

	/**
	 * The main method.
	 *
	 * @param args            the arguments
	 * @throws IOException             Signals that an I/O exception has occurred.
	 * @throws JavaLayerException the java layer exception
	 */
	public static void main(String[] args) throws IOException, JavaLayerException {
		VocabularyMp3Creator creator = new VocabularyMp3Creator();
		creator.vocabConfig = new VocabConfigDB();
		creator.vocabConfig.init();
		creator.initProxy();
		args = new String[] { "B-RSZ-10.txt" };
		File fmp3folder = new File("mp3");
		if (!fmp3folder.exists()) {
			fmp3folder.mkdir();
		}		
		String fileName = args != null && args.length > 0 ? args[0] : null;
		File f = new File("mp3", fileName.substring(0, 8));
		if (!f.exists()) {
			f.mkdir();
		}    
		
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream((new File("vocabulary", fileName))),"UTF-8"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] word = line.split(";");
				if (word[0] != null && word[0].trim().length() > 0) {
					//creator.createmp3(f,word[1],word[0] + "-czech", false);
					creator.createmp3(f,word[0],word[0] + "-english", true);
					
				}
			}
		}
	}
}
