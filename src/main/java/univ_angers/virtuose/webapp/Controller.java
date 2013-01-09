package univ_angers.virtuose.webapp;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;

import univ_angers.virtuose.rendu.Extract;
import univ_angers.virtuose.search.Search;
import univ_angers.virtuose.utils.Writer;
import univ_angers.virtuose.utils.XmlToHtml;

/**
 * Servlet qui contient la logique controlant l'interface Web
 * @author etudiant
 *
 */
@WebServlet("/controller")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(Controller.class);
	private static String saved_map_folder = "src/main/resources/saved_map/";

	public Controller() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher disp = null;
		HttpSession session = request.getSession();

		String action = request.getParameter("action");
		String keywords = "";

		if (action == null) {

		} else if (action.equals("show")) {
			int id = Integer.parseInt((String) request.getParameter("id"));
			List<String> content = (List<String>) session.getAttribute("content");
			int i = 0;
			for (String c : content) {
				if (i == id) {
					session.setAttribute("map", c);
				}
				i++;
			}
			disp = request.getRequestDispatcher("show.jsp");
			disp.forward(request, response);

		} else if (action.equals("search")) {
			// get access to file that is uploaded from client
			Part p1 = request.getPart("map");
			InputStream is = p1.getInputStream();

			// read filename which is sent as a part
			Part p2 = request.getPart("keywords");
			Scanner s = new Scanner(p2.getInputStream());
			keywords = s.nextLine(); // read filename from stream
			log.debug("Controller: keywords: " + keywords);

			// get filename to use on the server
			String fileName = getFileName(p1);
			log.info("File name : " + fileName);
			String outputfile = saved_map_folder + fileName; // get path on the
																// server
			createDirectoryIfNeeded(saved_map_folder);

			FileOutputStream os = new FileOutputStream(outputfile);

			// write bytes taken from uploaded file to target file
			int ch = is.read();
			while (ch != -1) {
				os.write(ch);
				ch = is.read();
			}

			// on traite le .mm
			Search search = new Search();
			search.index(outputfile);
			Writer writer = new Writer();
			writer.proceed(outputfile);
			os.close();

			s.close();

			// 2. query
			String querystr = keywords;
			ArrayList<Document> docs = search.search(querystr);
			ArrayList<String> xmls = new ArrayList<String>();
			/**
			 * Generate all xml documents according to the lucene doc that match
			 * the request
			 */
			// Document d = docs.get(0);
			log.info("results: " + docs.size());
			for (Document d : docs) {
				String tmp = "";
				try {
					tmp = Extract.extract(d.get("document"), d.get("id"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				xmls.add(tmp);
			}

			List<String> title = new ArrayList<String>();
			List<String> content = new ArrayList<String>();

			for (String xml : xmls) {
				String result = xml;

				log.debug("Controller:result: " + result);
				XmlToHtml.start(result);
				title.add(XmlToHtml.title);
				content.add(XmlToHtml.result);
			}

			// On supprime la map indexée
			deleteFolder(new File("src/ressources/index"));
			session.setAttribute("title", title);
			session.setAttribute("content", content);
			session.setAttribute("keywords", keywords);

			// On supprime la map
			os = new FileOutputStream(outputfile);
			os.write('v');
			os.close();

			disp = request.getRequestDispatcher("result.jsp");
			disp.forward(request, response);
		}
	}

	public static void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) { // some JVMs return null for empty dirs
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				} else {
					f.delete();
				}
			}
		}
		folder.delete();
	}

	private String getFileName(Part part) {
		String partHeader = part.getHeader("content-disposition");
		// log.info("Part Header = " + partHeader);
		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				return cd.substring(cd.indexOf('=') + 1).trim()
						.replace("\"", "");
			}
		}
		return null;

	}

	private void createDirectoryIfNeeded(String directoryName) {
		File theDir = new File(directoryName);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			log.info("creating directory: " + directoryName);
			theDir.mkdir();
		}
	}

	private boolean mapNameExists(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}
}
