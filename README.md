# 🍽️ Reservation System

A modern and user-friendly restaurant reservation management system. Developed with **Spring Boot** and **Java Swing**, this comprehensive application allows users to easily make reservations and place orders.

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
- ✅ **User Management**: Add, list, update, and delete users  
- ✅ **Table Management**: View and manage table availability  
- ✅ **Reservation System**: Automatically generate and manage reservation codes  
- ✅ **Order Management**: Create, list, and track customer orders  
- ✅ **Menu Integration**: Fetch menu categories via external API (themealdb.com)  
- ✅ **Reporting**: Monthly occupancy reports (PDF/Excel formats)  
- ✅ **Email Notifications**: Send system logs via email  
- ✅ **Modern GUI**: User-friendly Swing interface with gradient design  

### GUI Features
- 🎨 Modern gradient background design  
- 📱 Responsive and intuitive interface  
- 🎯 Easy navigation  
- 💾 Copy reservation code to clipboard  
- 🔄 Automatic form reset  
- ✅ Real-time status notifications  

## 🛠️ Technologies

### Backend
- **Java**: 17  
- **Spring Boot**: 3.4.5  
- **Spring Data JPA**: For database operations  
- **Spring MVC**: For REST API  
- **Lombok**: To reduce boilerplate code  
- **H2 Database**: Local database for development  
- **PostgreSQL**: Production database support  

### Frontend
- **Java Swing**: For the graphical user interface  
- **Graphics2D**: For modern gradient backgrounds  

### Other Libraries
- **iText**: For generating PDF reports (v5.5.13.3)  
- **Apache POI**: For generating Excel reports (v5.2.5)  
- **SpringDoc OpenAPI**: For API documentation (v2.3.0)  
- **Spring Boot Mail**: For email functionality  

### Testing
- **JUnit 5**: Unit testing (v5.10.2)  
- **Mockito**: Mocking framework (v5.2.0)  
- **AssertJ**: Fluent assertions (v3.24.2)  

## 📦 Installation

### Requirements
- Java 17 or higher  
- Maven 3.6+  
- (Optional) PostgreSQL (for production)

### Steps

1. **Clone the project**
```bash
git clone https://github.com/username/ReservationSystem.git
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

Or run the `ReservationSystemApplication` class directly from your IDE.

4. **Access the application**
- Backend API: `http://localhost:8080`  
- Swagger UI: `http://localhost:8080/swagger-ui.html`  
- GUI will open automatically  

### Configuration

Edit the `src/main/resources/application.properties` file:

```properties
# Server configuration
server.port=8080

# Database configuration (H2 - development)
spring.datasource.url=jdbc:h2:mem:reservationdb
spring.datasource.driverClassName=org.h2.Driver
spring.h2.console.enabled=true

# Email configuration
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

1. **Create a Reservation**
   - The reservation screen opens automatically at startup.  
   - Enter your name, surname, and date.  
   - Click the **✅ Make Reservation** button.  
   - Your reservation code is automatically generated.  
   - Click **📋 Copy** to copy the code to your clipboard.  

2. **Create an Order**
   - After copying the reservation code, the order screen opens automatically.  
   - Or enter the reservation code manually.  
   - Select categories from the available list.  
   - Click **➡️ Add** to move them to the selected list.  
   - Click **✅ Create Order** to complete your order.  

### API Usage

#### User Operations

**Add a New User**
```bash
POST /rest/api/users/add
Content-Type: application/json

{
  "name": "Ahmet",
  "surname": "Yılmaz",
  "date": "15/01/2025"
}
```

**List All Users**
```bash
GET /rest/api/users/list
```

**Update a User**
```bash
PUT /rest/api/users/update/{id}
Content-Type: application/json

{
  "name": "Mehmet",
  "surname": "Demir",
  "date": "20/01/2025"
}
```

**Delete a User**
```bash
DELETE /rest/api/users/delete/{id}
```

#### Category Operations

**Fetch Menu Categories**
```bash
GET /rest/api/menu
```

#### Order Operations

**Create an Order**
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

**Close an Order**
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

For detailed API documentation, visit Swagger UI:  
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
│   │   │       ├── GUI/             # GUI components
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
├── pom.xml                           # Maven configuration
├── README.md                         # This file
└── .gitignore
```

## 🧪 Test Scenarios

Run tests with:
```bash
mvn test
```

### UserService Test Scenarios
- ✅ Add user with valid data  
- ❌ Attempt to add user with empty name  
- ❌ Attempt to add user with past date  
- ❌ Attempt to add user when no tables are available  

### ExternalMenuService Test Scenarios
- ✅ Successfully fetch category data from external API  
- ⚠️ External API returns empty category list  
- ⚠️ External API returns null response  
- ❌ External API throws an exception  

## 🔧 Development

### Running in Development Mode

1. Open the project in your IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)  
2. Ensure Maven dependencies are installed  
3. Run `ReservationSystemApplication`  
4. GUI will open automatically  

### Adding a New Feature

1. Create a new branch:  
   ```bash
   git checkout -b feature/new-feature
   ```  
2. Implement your changes  
3. Write and run tests  
4. Commit your changes:  
   ```bash
   git commit -m "Added new feature"
   ```  
5. Push your branch:  
   ```bash
   git push origin feature/new-feature
   ```  
6. Open a Pull Request  

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository  
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)  
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)  
4. Push to the branch (`git push origin feature/AmazingFeature`)  
5. Open a Pull Request  

### Contribution Rules
- Follow code style guidelines  
- Write unit tests  
- Update README.md if needed  
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

This project is open source and licensed under the **MIT License**.

## 👥 Team

Developed by **Group 7**.

## 📧 Contact

For questions, please open an issue or submit a pull request.

## 🙏 Acknowledgments

- [Spring Boot](https://spring.io/projects/spring-boot)  
- [TheMealDB API](https://www.themealdb.com/)  
- All open-source library developers  

---

⭐ If you like this project, don’t forget to give it a star!
