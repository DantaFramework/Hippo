package mypkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HelloServlet2 extends HttpServlet {

    static final String DANTA_CONTENT_MODEL = "DANTA_CONTENT_MODEL";

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException {
      // Set the response message's MIME type
      response.setContentType("text/html;charset=UTF-8");
      // Allocate a output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();

      // Write the response message, in an HTML page
      try {
         out.println("<h1>Hello, world 2!!!</h1>");  // says Hello
      } finally {
         out.close();  // Always close the output writer
      }
   }
}
