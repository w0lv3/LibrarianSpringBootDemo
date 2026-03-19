# LibrarianSpringBootDemo
A Spring Boot-based REST API for managing a library/store workflow with books, customers, purchase orders, and users.  The app includes JWT authentication, role-based access control, database persistence, in-memory caching, and basic validation.

Authentication -> Users can register, log in, and refresh access tokens.
Authorization->	Supports role-based access for ADMIN and USER.
Book management -> Admins can create, update, patch, delete, and list books.
Customer management -> Admins can manage customers; users can update/delete their own customer profile.
Purchase orders -> Purchase orders can be created, updated, deleted, marked as paid, and totaled.
Database persistence -> Data is stored in a relational database using JPA entities and repositories.
In-memory caching -> Books, customers, and purchase orders are also cached in structures for quick access. HashMap
Validation -> Request DTOs use validation rules to protect input quality.
Security ->	JWT-based security with password hashing and protected endpoints.