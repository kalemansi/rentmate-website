<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Page</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            background: #f1f1f1;
            font-family: Arial, sans-serif;
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .login-container {
            display: flex;
            width: 100%;
            height: 100%;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        /* Left side - Image background */
        .image-section {
            flex: 1;
            background-image: url('background.jpg'); /* Your background image */
            background-size: cover;
            background-position: center;
            border-top-left-radius: 10px;
            border-bottom-left-radius: 10px;
        }

        /* Right side - Login Form */
        .login-form {
            flex: 1;
            background-color: rgba(255, 255, 255, 0.9);
            border-top-right-radius: 10px;
            border-bottom-right-radius: 10px;
            padding: 40px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            height: 100%; /* Ensures the login form takes the full height */
        }

        h1 {
            color: #033163;
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-top: 10px;
            font-size: 14px;
        }

        input {
            width: 100%;
            padding: 10px;
            margin: 5px 0 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-sizing: border-box;
        }

        .login-options {
            display: flex;
            justify-content: space-between;
            width: 100%;
        }

        .login-btn {
            background-color: #033163;
            color: white;
            border: none;
            padding: 12px 30px; /* Slightly larger padding for a better look */
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
            transition: background-color 0.3s ease;
            margin-top: 20px; /* Space above the button */
            align-items: center;
            justify-content: center;
            text-align:center;
        }

        .login-btn:hover {
            background-color: #021c3c;
            
            justify-content: center;
        }

        a {
            text-decoration: none;
            color: #033163;
        }

        a:hover {
            text-decoration: underline;
        }

        p {
            margin-top: 10px;
        }
    </style>
</head>

<body>

    <div class="login-container">
        <!-- Left side: Image -->
        <div class="image-section"></div>

        <!-- Right side: Login Form -->
        <div class="login-form">
            <h1>Account Login</h1>

            <form action="LoginServlet" method="post">
                <label for="username">Username:</label>
                <input type="text" id="username" name="username" required>

                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>

                <p>Please choose your role to login:</p>

                <div class="login-options">
                    <label>
                        <input type="radio" name="role" value="renter" required> Login as Renter
                    </label>
                    <label>
                        <input type="radio" name="role" value="owner" required> Login as Owner
                    </label>
                </div>

                <!-- Centering the button -->
                <button type="submit" class="login-btn">Login</button>
            </form>

            <p><a href="index.html">Back to Home</a></p>

            <%-- Display error message if login fails --%>
            <% 
                String error = request.getParameter("error");
                if (error != null && error.equals("1")) { 
            %>
                <p style="color: red;">Invalid username or password. Please try again.</p>
            <% 
                } 
            %>
        </div>
    </div>

</body>

</html>