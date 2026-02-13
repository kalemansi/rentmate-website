import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/NotificationServlet")
public class NotificationServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://maglev.proxy.rlwy.net:36092/railway?useSSL=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "pBmBhzXntXUAVnQKQzuEsmmWAirzYYEx";
    private static final Logger LOGGER = Logger.getLogger(NotificationServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            String id = request.getParameter("id"); // Get id from request

            // Debugging: Print received ID in server logs
            System.out.println("Received ID: " + id);

            if (id == null || id.trim().isEmpty()) {
                out.println("<div class='alert alert-warning'>ID is missing!</div>");
                return;
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "SELECT status FROM payment_logs WHERE id = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, id);
                ResultSet rs = pst.executeQuery();

                // Start HTML response
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Payment Notification</title>");
                out.println("<style>");
                out.println("body { font-family: Arial, sans-serif; padding: 20px; }");
                out.println(".notification-section { max-width: 400px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px; background: #f9f9f9; text-align: center; }");
                out.println(".alert { padding: 15px; margin: 10px 0; border-radius: 5px; font-weight: bold; }");
                out.println(".alert-success { background-color: #4CAF50; color: white; }");
                out.println(".alert-danger { background-color: #f44336; color: white; }");
                out.println(".alert-warning { background-color: #ff9800; color: white; }");
                out.println(".alert-info { background-color: #2196F3; color: white; }");
                out.println(".alert-secondary { background-color: #9e9e9e; color: white; }");
                out.println("</style>");
                out.println("</head>");
                out.println("<body>");
                out.println("<div class='notification-section'>");

                if (rs.next()) {
                    String status = rs.getString("status");

                    if ("Received".equalsIgnoreCase(status)) {
                        out.println("<div class='alert alert-success'>✅ Payment was successful!</div>");
                    } else if ("Not Received".equalsIgnoreCase(status)) {
                        out.println("<div class='alert alert-danger'>❌ Payment failed. Please try again.</div>");
                    } else {
                        out.println("<div class='alert alert-secondary'>ℹ️ Unknown payment status.</div>");
                    }
                } else {
                    out.println("<div class='alert alert-info'>📢 No recent payment records found.</div>");
                }

                out.println("</div>"); // Close notification section
                out.println("</body>");
                out.println("</html>");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error: ", e);
            response.sendRedirect("error.html");
        }
    }
}
