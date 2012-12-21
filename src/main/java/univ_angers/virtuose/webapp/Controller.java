package univ_angers.virtuose.webapp;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
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

@WebServlet("/controller")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(Controller.class);

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

		} else if (action.equals("search")) {
			// get access to file that is uploaded from client
            Part p1 = request.getPart("map");
            InputStream is = p1.getInputStream();

            // read filename which is sent as a part
            Part p2  = request.getPart("keywords");
            Scanner s = new Scanner(p2.getInputStream());
            keywords = s.nextLine();    // read filename from stream

            // get filename to use on the server
            String outputfile = "/tmp/map.mm";  // get path on the server
            FileOutputStream os = new FileOutputStream (outputfile);
            
            // write bytes taken from uploaded file to target file
            int ch = is.read();
            while (ch != -1) {
                 os.write(ch);
                 ch = is.read();
            }
            

            // on traite le .mm
            Search.index("/tmp/map.mm");
        	Writer.proceed("/tmp/map.mm");
            // 2. query
        	String querystr = keywords;
            ArrayList<Document> docs = Search.search(querystr);
            ArrayList<String> xmls = new ArrayList<String>();
            /**
             * Generate all xml documents according to the lucene doc that match the request
             */
            //Document d = docs.get(0);
            for (Document d : docs){
            	String tmp = "";
				try {
					tmp = Extract.extract(d.get("document"), d.get("id"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	xmls.add(tmp);
            }
            
        	String result = xmls.get(0);
            
            
            log.debug("Controller:result: "+ result);
            FileWriter writer = null;
            try{
                writer = new FileWriter("/tmp/map.xml", true);
                //writer.write(result,0,result.length());
                writer.write(result);
           }catch(IOException ex){
               ex.printStackTrace();
           }finally{
             if(writer != null){
                writer.close();
             }
           }
            XmlToHtml.start("/tmp/map.xml");
            
            // on l'affiche:
            BufferedReader br = null;
            String map = "";
    		try {
     
    			String sCurrentLine;
     
    			br = new BufferedReader(new FileReader("/tmp/page.html"));
    			br.readLine();
    			
    			while ((sCurrentLine = br.readLine()) != null) {
    				map += sCurrentLine;
    				map += "\n";
    			}
     
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		
    		File map_xml = new File("/tmp/map.xml");
    		map_xml.delete();
    		session.setAttribute("map", map);
            session.setAttribute("keywords", keywords);
			disp = request.getRequestDispatcher("result.jsp");
			disp.forward(request, response);
		}
	}
	
	public void writeTextFile(String fileName, String s) {
	    FileWriter output = null;
	    try {
	      output = new FileWriter(fileName);
	      BufferedWriter writer = new BufferedWriter(output);
	      writer.write(s);
	    } catch (IOException e) {
	      e.printStackTrace();
	    } finally {
	      if (output != null) {
	        try {
	          output.close();
	        } catch (IOException e) {
	          e.printStackTrace();
	        }
	      }
	    }

	  } 
}