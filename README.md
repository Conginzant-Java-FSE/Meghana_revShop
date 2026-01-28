<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
</head>
<body>

<h1>RevShop – Console-Based E-Commerce Marketplace</h1>

<p>
RevShop is a Java console-based e-commerce application that connects Buyers and Sellers on a single platform.
It supports complete shopping workflows including product management, cart operations, secure authentication,
order processing, payments (UPI / Card / COD), reviews, notifications, favorites, and inventory alerts.
</p>

<p>
The application follows a clean layered architecture, uses a normalized MySQL database,
and applies secure password hashing.
</p>

<hr>

<h2>Project Architecture</h2>

<p>RevShop follows a layered architecture to ensure separation of concerns and maintainability.</p>

<h3>Model Layer (org.example.models)</h3>
<ol>
    <li>Contains core business entities such as Buyer, Seller, Product, Order, Cart, Review, and Notification</li>
    <li>Acts as data carriers between layers</li>
</ol>

<h3>DAO Layer (org.example.dao)</h3>
<ol>
    <li>Handles all database operations using JDBC and PreparedStatements</li>
    <li>Performs CRUD operations</li>
    <li>Handles authentication and recovery queries</li>
    <li>Manages order, payment, and inventory updates</li>
    <li>Stores notifications</li>
    <li>Uses centralized DBConnection</li>
</ol>

<h3>Service Layer (org.example.service)</h3>
<ol>
    <li>Contains business logic and workflow orchestration</li>
    <li>Reduces stock after checkout</li>
    <li>Credits seller wallet after payment</li>
    <li>Triggers inventory threshold alerts</li>
    <li>Sends buyer and seller notifications</li>
</ol>

<h3>Controller Layer (org.example.controller)</h3>
<ol>
    <li>Implements console-based UI (CLI)</li>
    <li>Displays menus</li>
    <li>Accepts user input</li>
    <li>Navigates Buyer and Seller dashboards</li>
</ol>

<h3>Config Layer (org.example.config)</h3>
<ol>
    <li>Provides centralized database connection using DBConnection</li>
</ol>

<h3>Utility Layer (org.example.util)</h3>
<ol>
    <li>Contains shared utilities such as password hashing and verification</li>
</ol>

<hr>

<h2>Database Design</h2>

<p>The MySQL database (revshop_db) is fully normalized.</p>

<h3>Key Relationships</h3>
<ol>
    <li>Buyer → Cart → Cart Items</li>
    <li>Buyer → Orders → Order Items</li>
    <li>Seller → Products</li>
    <li>Product → Reviews</li>
    <li>Buyer / Seller → Notifications</li>
</ol>

<p>Foreign keys and cascading rules maintain data integrity.</p>

<hr>

<h2>Application Workflow</h2>

<h3>Authentication</h3>
<ol>
    <li>Buyer and Seller registration and login</li>
    <li>Secure password hashing</li>
    <li>Recovery options:
        <ul>
            <li>Forgot Password (via email)</li>
            <li>Forgot Email (via phone)</li>
        </ul>
    </li>
</ol>

<h3>Buyer Flow</h3>
<ol>
    <li>Browse and search products</li>
    <li>Manage cart</li>
    <li>Checkout using COD / UPI / Card</li>
    <li>View orders</li>
    <li>Write reviews</li>
    <li>Manage favorites</li>
    <li>View notifications</li>
</ol>

<h3>Seller Flow</h3>
<ol>
    <li>Add, update, and delete products</li>
    <li>Set stock thresholds</li>
    <li>View received orders</li>
    <li>Monitor wallet balance</li>
    <li>View product reviews</li>
    <li>Receive notifications</li>
    <li>Track low-stock inventory</li>
</ol>

<h3>Order Lifecycle</h3>
<ol>
    <li>Buyer checkout</li>
    <li>Payment processed</li>
    <li>Stock updated</li>
    <li>Seller wallet credited</li>
    <li>Order and order items created</li>
    <li>Buyer and Seller notified</li>
    <li>Inventory thresholds checked</li>
</ol>

<hr>

<h2>Security (Password Hashing)</h2>
<ol>
    <li>Implemented using jBCrypt</li>
    <li>Plain-text passwords are never stored</li>
</ol>

<h3>Maven Dependency</h3>
<pre>
&lt;dependency&gt;
    &lt;groupId&gt;org.mindrot&lt;/groupId&gt;
    &lt;artifactId&gt;jbcrypt&lt;/artifactId&gt;
    &lt;version&gt;0.4&lt;/version&gt;
&lt;/dependency&gt;
</pre>

<h3>Password Flow</h3>
<ol>
    <li>Registration → Password hashed before storing</li>
    <li>Login → Input verified against hash</li>
    <li>Reset password → New password hashed before update</li>
</ol>

<hr>

<h2>Input Validation</h2>
<ol>
    <li>Menu input validation</li>
    <li>Email format validation</li>
    <li>UPI format validation</li>
    <li>Card number length validation</li>
    <li>Quantity and stock checks</li>
</ol>

<p>Handled at Controller and Service levels.</p>

<hr>

<h2>Testing</h2>
<ol>
    <li>JUnit 5</li>
    <li>Mockito</li>
</ol>

<hr>

<h2>Technical Stack</h2>
<ol>
    <li>Language: Java 21 (LTS)</li>
    <li>Database: MySQL 8+</li>
    <li>Build Tool: Maven</li>
    <li>Logging: SLF4J + Logback</li>
    <li>Security: jBCrypt</li>
    <li>Testing: JUnit 5, Mockito</li>
</ol>

<hr>

<h2>How to Run</h2>
<ol>
    <li>Clone the repository</li>
    <li>Run db_schema.sql in MySQL</li>
    <li>Update database credentials in:<br>
        src/main/java/org/example/config/DBConnection.java
    </li>
    <li>Build the project:
        <pre>mvn clean install</pre>
    </li>
    <li>Run:
        <pre>org.example.Main</pre>
    </li>
</ol>

</body>
</html>
