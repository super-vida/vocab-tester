package cz.prague.vida.vocab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javazoom.jl.decoder.JavaLayerException;

/**
 * The Class VocabCounter.
 */
public class VocabularyMp3CreatorAll {
	
	private VocabConfigDB vocabConfig;
	private Proxy proxy;
	
	private void initProxy() {
		String proxyString = vocabConfig.getProxy();
		int proxyPort = vocabConfig.getProxyPort();
		if (proxyString != null) {
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyString, proxyPort));
		}
	}

	/**
	 * Inits the.
	 *
	 * @return the file[]
	 */
	public File[] init() {
		File dir = new File("vocabulary");
		return dir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				if (file.getName().toLowerCase().endsWith(".txt")) {
					return true;
				}
				return false;
			}
		});
	}
	
	private void createmp3(File folder, String englishWord) throws JavaLayerException, IOException {
		String pronanciationUrl = vocabConfig.getPronanciationUrl();
		if (pronanciationUrl != null) {
			try {
				URL url = new URL(pronanciationUrl.replace("REPLACE", englishWord.toLowerCase()));
				HttpURLConnection httpcon = proxy != null ? (HttpURLConnection) url.openConnection(proxy) : (HttpURLConnection) url.openConnection();
				try (InputStream inputStream = httpcon.getInputStream()) {
					try (FileOutputStream outputStream = new FileOutputStream(new File(folder, englishWord + ".mp3"))) {
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


	/**
	 * Count.
	 *
	 * @return the string
	 */
	public String count() {
		File[] directoryListing = init();
		StringBuilder sb = new StringBuilder();
		String line = null;
		File fmp3folder = new File("mp3");
		if (!fmp3folder.exists()) {
			fmp3folder.mkdir();
		}	
		for (int i = 0; i < directoryListing.length; i++) {
			try (BufferedReader br = new BufferedReader(new FileReader(directoryListing[i]))) {
				File f = new File("mp3", directoryListing[i].getName().replace(".txt", ""));
				if (!f.exists()) {
					f.mkdir();
				}  
				while ((line = br.readLine()) != null) {
					String[] word = line.split(";");
					createmp3(f, word[0]);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return sb.toString();
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		VocabularyMp3CreatorAll counter = new VocabularyMp3CreatorAll();
		counter.vocabConfig = new VocabConfigDB();
		counter.vocabConfig.init();
		counter.initProxy();
		System.out.println(counter.count());
	}

}
