<!DOCTYPE html>
<html>
<body>
    <h1>Vendor Dashboard</h1>
    <h3>Add New Product</h3>
    <form id="addProductForm" action="ProductServlet" method="post" onsubmit="return validateProduct()">
        <input type="hidden" name="action" value="add">
        Name: <input type="text" id="name" name="name" required><br>
        Desc: <input type="text" id="description" name="description"><br>
        Price: <input type="number" id="price" name="price" step="0.01" required><br>
        Quantity: <input type="number" id="quantity" name="quantity" value="0" min="0" required><br>
        <button type="submit">List Product</button>
    </form>

    <script>
        function validateProduct(){
            const name = document.getElementById('name').value.trim();
            const price = parseFloat(document.getElementById('price').value);
            const qty = parseInt(document.getElementById('quantity').value, 10);
            if (!name) { alert('Product name is required'); return false; }
            if (isNaN(price) || price <= 0) { alert('Price must be a positive number'); return false; }
            if (isNaN(qty) || qty < 0) { alert('Quantity must be 0 or greater'); return false; }
            return true;
        }
    </script>
</body>
</html>