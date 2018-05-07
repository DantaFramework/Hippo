package mypkg;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class HelloServlet extends HttpServlet {

    static final String DANTA_CONTENT_MODEL = "DANTA_CONTENT_MODEL";

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException {

        // Set the response message's MIME type
        response.setContentType("text/html;charset=UTF-8");
        // Allocate a output writer to write the response message into the network socket
        PrintWriter out = response.getWriter();

        // Trying out the filter
        String value = (String) request.getAttribute(DANTA_CONTENT_MODEL);
        if (value == null) {
        value = "NO CONTENT MODEL";
        }

        // Trying out invoking OSGi services

        try {
           HostApplication hstApp = new HostApplication();
           boolean result = hstApp.execute("Command", "COMMAND");
           System.out.println(result);
           hstApp.shutdownApplication();
        } catch (Exception e) {
           e.printStackTrace();
        }


        // Write the response message, in an HTML page
        try {
             out.println("<!DOCTYPE html>");
             out.println("<html><head>");
             out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
             out.println("<title>Hello, World!</title></head>");
             out.println("<body>");
             out.println("<h1>Hello, world!!!</h1>");  // says Hello
             out.println("<h2>" + value  + "</h2>");  // Test filter
             // Echo client's request information
             out.println("<p>Request URI: " + request.getRequestURI() + "</p>");
             out.println("<p>Protocol: " + request.getProtocol() + "</p>");
             out.println("<p>PathInfo: " + request.getPathInfo() + "</p>");
             out.println("<p>Remote Address: " + request.getRemoteAddr() + "</p>");
             // Generate a random number upon each request
             out.println("<p>A Random Number: <strong>" + Math.random() + "</strong></p>");
             out.println("</body>");
             out.println("</html>");
          } finally {
             out.close();  // Always close the output writer
          }
   }
}
