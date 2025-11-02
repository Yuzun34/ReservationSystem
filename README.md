# 🍽️ Reservation System

A modern and user-friendly restaurant reservation management system. Developed using Spring Boot and Java Swing, this comprehensive application enables users to easily make reservations and place orders.

## 📋 Table of Contents

- [Features](#-features)
- [Technologies](#-technologies)
- [Installation](#-installation)
- [Usage](#-usage)
- [API Documentation](#-api-documentation)
- [Project Structure](#-project-structure)
- [GUI Usage](#-gui-usage)
- [Test Scenarios](#-test-scenarios)
- [Contributing](#-contributing)
- [License](#-license)

## ✨ Features

### Core Features
- ✅ **User Management**: Add, list, update, and delete user operations
- ✅ **Table Management**: View and manage table status
- ✅ **Reservation System**: Automatic reservation code generation and management
- ✅ **Order Management**: Create, list, and track orders
- ✅ **Menu Integration**: Fetch menu categories from external API (themealdb.com)
- ✅ **Reporting**: Monthly occupancy reports (PDF/Excel format)
- ✅ **Email Notifications**: Send system logs via email
- ✅ **Modern GUI**: Gradient-colored and user-friendly interface built with Java Swing

### GUI Features
- 🎨 Modern gradient background design
- 📱 Responsive and user-friendly interface
- 🎯 Intuitive navigation
- 💾 Copy reservation code to clipboard
- 🔄 Automatic form clearing
- ✅ Real-time status notifications

## 🛠️ Technologies

### Backend
- **Java**: 17
- **Spring Boot**: 3.4.5
- **Spring Data JPA**: For database operations
- **Spring MVC**: For REST API
- **Lombok**: To reduce code repetition
- **H2 Database**: Local database for development environment
- **PostgreSQL**: Production database support

### Frontend
- **Java Swing**: For graphical user interface
- **Graphics2D**: For modern gradient backgrounds

### Other Libraries
- **iText**: For creating PDF reports (v5.5.13.3)
- **Apache POI**: For creating Excel reports (v5.2.5)
- **SpringDoc OpenAPI**: For API documentation (v2.3.0)
- **Spring Boot Mail**: For sending emails

### Testing
- **JUnit 5**: Unit tests (v5.10.2)
- **Mockito**: Test mocking library (v5.2.0)
- **AssertJ**: For test assertions (v3.24.2)

## 📦 Installation

### Requirements
- Java 17 or higher
- Maven 3.6+ 
- (Optional) PostgreSQL (for production)

### Steps

1. **Clone the project**
```bash
git clone https://github.com/Yuzun34/ReservationSystem.git
cd ReservationSystem
```

2. **Install Maven dependencies**
```bash
mvn clean install
```

3. **Run the application**
```bash
mvn spring-boot:run
```

Or run the `ReservationSystemApplication` class in your IDE.

4. **Access the application**
- Backend API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- GUI will open automatically

### Configuration

You can configure settings in the `src/main/resources/application.properties` file:

```properties
# Server settings
server.port=8080

# Database settings (H2 - for development)
spring.datasource.url=jdbc:h2:mem:reservationdb
spring.datasource.driverClassName=org.h2.Driver
spring.h2.console.enabled=true

# Email settings
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Logging
logging.file.name=reservation-system.log
```

## 🚀 Usage

### GUI Usage

1. **Creating a Reservation**
   - The reservation screen opens automatically when the application starts
   - Enter name, surname, and date information
   - Click the "✅ Make Reservation" button
   - Your reservation code is automatically generated
   - Copy the code to clipboard using the "📋 Copy" button

2. **Creating an Order**
   - The order screen opens automatically after copying the reservation code
   - Or manually enter the reservation code
   - Select from available categories
   - Add categories to the selected list using the "➡️ Add" button
   - Click the "✅ Create Order" button

### API Usage

#### User Operations

**Add New User**
```bash
POST /rest/api/users/add
Content-Type: application/json

{
  "name": "John",
  "surname": "Doe",
  "date": "15/01/2025"
}
```

**List All Users**
```bash
GET /rest/api/users/list
```

**Update User**
```bash
PUT /rest/api/users/update/{id}
Content-Type: application/json

{
  "name": "Jane",
  "surname": "Smith",
  "date": "20/01/2025"
}
```

**Delete User**
```bash
DELETE /rest/api/users/delete/{id}
```

#### Category Operations

**Get Menu Categories**
```bash
GET /rest/api/menu
```

#### Order Operations

**Create Order**
```bash
POST /rest/api/orders/save
Content-Type: application/json

{
  "reservationCode": "RES-2025-001",
  "categoryIds": ["1", "2", "3"]
}
```

**Get Order by Reservation Code**
```bash
GET /rest/api/orders/{reservationCode}
```

**Close Order**
```bash
POST /rest/api/orders/close/{id}
```

#### Table Operations

**List All Tables**
```bash
GET /rest/api/tables
```

#### Report Operations

**Monthly Occupancy Report**
```bash
GET /rest/api/reports/{year}/{month}
```

#### Email Operations

**Send Log File via Email**
```bash
POST /rest/api/send-email
Content-Type: application/json

{
  "to": "destination@example.com",
  "subject": "Reservation System Logs"
}
```

## 📚 API Documentation

For detailed API documentation, you can use Swagger UI:
- URL: `http://localhost:8080/swagger-ui.html`

## 📁 Project Structure

```
ReservationSystem/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/grup7/
│   │   │       ├── Config/          # Configuration classes
│   │   │       ├── Controller/      # REST API endpoints
│   │   │       ├── Dto/             # Data transfer objects
│   │   │       ├── Entity/          # JPA entities
│   │   │       ├── Exception/       # Custom exception classes
│   │   │       ├── GUI/             # User interface components
│   │   │       ├── Repository/      # Database repositories
│   │   │       ├── Service/         # Business logic services
│   │   │       ├── Util/            # Utility classes
│   │   │       └── ReservationSystem/
│   │   │           └── ReservationSystemApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   └── test/
│       └── java/
│           └── com/grup7/
│               └── Service/          # Test classes
├── pom.xml                          # Maven configuration
├── README.md                        # This file
└── .gitignore
```

## 🖥️ GUI Usage

### Reservation Screen
1. Enter your name and surname
2. Select the reservation date (Day/Month/Year)
3. Click the "✅ Make Reservation" button
4. Your generated reservation code will appear on the screen
5. Copy the code to clipboard using the "📋 Copy" button

### Order Screen
1. Enter your reservation code (can be auto-pasted)
2. Select menu categories from the left list
3. Add categories to the selected list using the "➡️ Add" button
4. Remove categories from the list using the "⬅️ Remove" button
5. Complete your order by clicking the "✅ Create Order" button

## 🧪 Test Scenarios

The project includes comprehensive unit tests. To run tests:

```bash
mvn test
```

### UserService Test Scenarios
- ✅ Add user with valid information
- ❌ Attempt to add user with empty name
- ❌ Attempt to add user with past date
- ❌ Attempt to add user without available table

### ExternalMenuService Test Scenarios
- ✅ Successfully fetch category data from external API
- ⚠️ Empty category list returned from external API
- ⚠️ Null response returned from external API
- ❌ Exception thrown from external API

## 🔧 Development

### Running the Project in Development Environment

1. Open the project in your IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)
2. Ensure Maven dependencies are loaded
3. Run the `ReservationSystemApplication` class
4. GUI will open automatically

### Adding New Features

1. Create a new branch: `git checkout -b feature/new-feature`
2. Make your changes
3. Write and run tests
4. Commit: `git commit -m "Add new feature"`
5. Push branch: `git push origin feature/new-feature`
6. Create a Pull Request

## 🤝 Contributing

We welcome your contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Contribution Guidelines
- Follow code standards
- Write unit tests
- Update README.md
- Use descriptive commit messages

## 📝 Changelog

### v0.0.1-SNAPSHOT
- ✨ Initial release
- ✅ User management
- ✅ Reservation system
- ✅ Order management
- ✅ Modern GUI interface
- ✅ REST API endpoints
- ✅ Reporting features
- ✅ Email notifications

## 📄 License

This project is open source and licensed under the MIT License.

## 👥 Team

Developed by Group 7.

## 📧 Contact

For questions, please open an issue or submit a pull request.

## 🙏 Acknowledgments

- [Spring Boot](https://spring.io/projects/spring-boot)
- [TheMealDB API](https://www.themealdb.com/)
- All open source library developers

---

⭐ If you liked this project, don't forget to give it a star!
