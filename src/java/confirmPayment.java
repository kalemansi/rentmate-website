import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/confirmPayment")
public class confirmPayment extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://maglev.proxy.rlwy.net:36092/railway?useSSL=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "pBmBhzXntXUAVnQKQzuEsmmWAirzYYEx";
    private static final Logger LOGGER = Logger.getLogger(confirmPayment.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String renterName = request.getParameter("renterName");
        String status = request.getParameter("status");

        if (renterName == null || status == null) {
            response.getWriter().write("fail: Missing parameters");
            return;
        }

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Insert notification into notification table
            String notificationMessage = "Payment status updated for " + renterName + " to: " + status;
            String insertSQL = "INSERT INTO notification (renterName, status, message) VALUES (?, ?, ?)";

            try (PreparedStatement insertStmt = con.prepareStatement(insertSQL)) {
                insertStmt.setString(1, renterName);
                insertStmt.setString(2, status);
                insertStmt.setString(3, notificationMessage);
                int rowsInserted = insertStmt.executeUpdate();

                if (rowsInserted > 0) {
                    response.getWriter().write("success");
                } else {
                    response.getWriter().write("fail: Insert failed");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inserting notification", e);
            response.getWriter().write("error: Database error");
        }
    }
}
