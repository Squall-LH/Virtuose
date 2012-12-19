package univ_angers.virtuose.cut;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import univ_angers.virtuose.search.Search;
import univ_angers.virtuose.utils.Writer;

public class Cut {
	private static Logger log = Logger.getLogger(Cut.class);
	
	// count line number of filename
	public static int count(String filename) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(filename));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n')
						++count;
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}

	public static void start(String filePath, String docs) throws IOException {
		log.info("filePath to index: " + filePath);
		Writer.proceed(docs, filePath);
		List<String> fileToCut;
		List<String> proceed = new ArrayList<String>();

		do {
			fileToCut = new ArrayList<String>();
			File file = new File(docs);
			for (String fileName : file.list()) {
				if (count(docs + fileName) > 10 && !proceed.contains(docs + fileName)) {
					fileToCut.add(docs + fileName);
				}
			}

			for (String fileName : fileToCut) {
				Writer.proceed(docs, fileName);
				proceed.add(fileName);
			}
		} while (fileToCut.size() > 0);
	}

}
