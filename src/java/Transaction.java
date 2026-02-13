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

@WebServlet("/Transaction")
public class Transaction extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://maglev.proxy.rlwy.net:36092/railway?useSSL=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD =  "pBmBhzXntXUAVnQKQzuEsmmWAirzYYEx";
    private static final Logger LOGGER = Logger.getLogger(Transaction.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "SELECT id, renterName, paymentAmount, paymentMethod, paymentScreenshot, additionalComments, paymentStatus FROM payments";
                PreparedStatement pst = con.prepareStatement(query);
                ResultSet rs = pst.executeQuery();

                out.println("<!DOCTYPE html>");
                out.println("<html lang='en'>");
                out.println("<head>");
                out.println("<meta charset='UTF-8'>");
                out.println("<title>Transaction History</title>");
                out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css' rel='stylesheet'>");
                out.println("</head>");
                out.println("<body>");
                out.println("<div class='container mt-5'>");
                out.println("<h2 class='mb-4'>Transaction History</h2>");
                out.println("<table class='table table-bordered'>");
                out.println("<thead>");
                out.println("<tr>");
                out.println("<th>id</th>");
                out.println("<th>Renter Name</th>");
                out.println("<th>Amount</th>");
                out.println("<th>Payment Method</th>");
                out.println("<th>Screenshot</th>");
                out.println("<th>Comments</th>");
                out.println("<th>Confirmation</th>");
                out.println("</tr>");
                out.println("</thead>");
                out.println("<tbody>");

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String renterName = rs.getString("renterName");
                    double paymentAmount = rs.getDouble("paymentAmount");
                    String paymentMethod = rs.getString("paymentMethod");
                    String paymentScreenshot = rs.getString("paymentScreenshot");
                    String additionalComments = rs.getString("additionalComments");
                    String paymentStatus = rs.getString("paymentStatus");

                    out.println("<tr>");
                    
                    
                    out.println("<td>" + id + "</td>");
                    out.println("<td>" + renterName + "</td>");
                    out.println("<td>₹" + paymentAmount + "</td>");
                    out.println("<td>" + paymentMethod + "</td>");
                    out.println("<td><a href='" + paymentScreenshot + "' target='_blank'>View</a></td>");
                    out.println("<td>" + (additionalComments != null ? additionalComments : "N/A") + "</td>");
                    out.println("<td>");

                    if ("Received".equals(paymentStatus)) {
                        out.println("<span class='badge bg-success'>Received</span>");
                    } else if ("Not Received".equals(paymentStatus)) {
                        out.println("<span class='badge bg-danger'>Not Received</span>");
                    } else {
                        // Form for updating payment status
                        out.println("<form method='POST' action='Transaction'>");
                        out.println("<input type='hidden' name='paymentId' value='" + id + "'>");
                        out.println("<input type='hidden' name='renterName' value='" + renterName + "'>");
                        out.println("<button type='submit' name='status' value='Received' class='btn btn-success btn-sm'>Received</button>");
                        out.println("<button type='submit' name='status' value='Not Received' class='btn btn-danger btn-sm'>Not Received</button>");
                        out.println("</form>");
                    }

                    out.println("</td>");
                    out.println("</tr>");
                }

                out.println("</tbody>");
                out.println("</table>");
                out.println("</div>");
                out.println("</body>");
                out.println("</html>");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching transaction history", e);
            response.sendRedirect("error.html");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int paymentId = Integer.parseInt(request.getParameter("paymentId"));
        String renterName = request.getParameter("renterName");
        String status = request.getParameter("status");

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            con.setAutoCommit(false); // Start transaction

            try {
                // Step 1: Update paymentStatus in the payments table
                String updateQuery = "UPDATE payments SET paymentStatus = ? WHERE id = ?";
                try (PreparedStatement pst = con.prepareStatement(updateQuery)) {
                    pst.setString(1, status);
                    pst.setInt(2, paymentId);
                    pst.executeUpdate();
                }

                // Step 2: Insert into the payment_logs table
                String insertLogQuery = "INSERT INTO payment_logs (paymentId, status, updated_at) VALUES (?, ?, NOW())";
                try (PreparedStatement logPst = con.prepareStatement(insertLogQuery)) {
                    logPst.setInt(1, paymentId);
                    logPst.setString(2, status);
                    logPst.executeUpdate();
                }

                // Step 3: Insert into the notification table
               

                con.commit(); // Commit transaction
                response.sendRedirect("Transaction"); // Refresh page
            } catch (Exception e) {
                con.rollback(); // Rollback if any issue occurs
                LOGGER.log(Level.SEVERE, "Error updating payment status", e);
                response.sendRedirect("error.html");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Database connection error", e);
            response.sendRedirect("error.html");
        }
    }
}