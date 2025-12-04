<%@ page import="java.util.*, com.greentech.model.*" %>
<!DOCTYPE html>
<html>
<body>
    <h1>Marketplace</h1>
    <% 
        List<Product> products = (List<Product>) request.getAttribute("productList");
        if(products != null) {
            for(Product p : products) {
                if("APPROVED".equals(p.getStatus())) {
    %>
        <div style="border:1px solid black; margin:10px; padding:10px;">
            <h3><%= p.getName() %></h3>
            <p>Price: $<%= p.getPrice() %></p>
            <form action="ProductServlet" method="post">
                <input type="hidden" name="action" value="buy">
                <button type="submit">Buy Now</button>
            </form>
        </div>
    <% }}} %>
</body>
</html>