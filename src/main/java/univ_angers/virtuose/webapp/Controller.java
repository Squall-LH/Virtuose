package univ_angers.virtuose.webapp;

import java.io.*;
import java.util.Collection;
import java.util.Scanner;

import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/controller")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
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
			Collection<Part> parts = request.getParts();
			// get access to file that is uploaded from client
            Part p1 = request.getPart("map");
            InputStream is = p1.getInputStream();

            // read filename which is sent as a part
            Part p2  = request.getPart("keywords");
            Scanner s = new Scanner(p2.getInputStream());
            keywords = s.nextLine();    // read filename from stream

            // get filename to use on the server
            String outputfile = "/tmp/map.out";  // get path on the server
            FileOutputStream os = new FileOutputStream (outputfile);
            
            // write bytes taken from uploaded file to target file
            int ch = is.read();
            while (ch != -1) {
                 os.write(ch);
                 ch = is.read();
            }

            session.setAttribute("keywords", keywords);
			disp = request.getRequestDispatcher("result.jsp");
			disp.forward(request, response);
		}
	}
}