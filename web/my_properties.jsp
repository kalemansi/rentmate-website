<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Property Listing Form</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="styles.css">
</head>

<body>
    <div class="container mt-5">
        <h2 class="mb-4">List a Property</h2>

        <!-- Form Submission to addProperty.jsp -->
        <form action="my_propertiesServlet" method="post" enctype="multipart/form-data">
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Property Name</label>
                    <input type="text" class="form-control" name="propertyName" placeholder="Enter property name" required>
                </div>

                <div class="col-md-6 mb-3">
                    <label class="form-label">Property Type</label>
                    <select class="form-select" name="propertyType" required>
                        <option value="" disabled selected>Select property type</option>
                        <option value="house">House</option>
                        <option value="apartment">Apartment</option>
                        <option value="commercial">Commercial</option>
                    </select>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Price</label>
                    <input type="number" class="form-control" name="price" placeholder="Enter price" required>
                </div>

                <div class="col-md-6 mb-3">
                    <label class="form-label">Location</label>
                    <input type="text" class="form-control" name="location" placeholder="Enter location" required>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12 mb-3">
                    <label class="form-label">Property Description</label>
                    <textarea class="form-control" name="description" rows="4" placeholder="Enter a brief description" required></textarea>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Upload Property Image</label>
                    <input type="file" class="form-control" name="propertyImage">
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Upload QR</label>
                    <input type="file" class="form-control" name="Scanner">
                </div>
            </div>

            <div class="row">
                <div class="col-md-12 text-center">
                    <button type="submit" class="btn btn-primary">List Property</button>
                </div>
            </div>
        </form>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
</body>

</html>
