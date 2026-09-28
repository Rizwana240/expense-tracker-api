# 💰 Expense Tracker API

A personal finance tracker built with **Java + Spring Boot**, featuring automatic expense categorization, a REST API, and a live HTML/JS dashboard.

## Features

- **REST API** built with Spring Boot and Spring Data JPA
- **Persistent storage** using an embedded H2 database
- **Automatic categorization** of expenses based on keyword matching (e.g. "Uber" → Transport, "Swiggy" → Food)
- **Full CRUD support** — add, view, update, and delete expenses
- **Insights endpoint** — total spend, spend-by-category breakdown, and top spending category
- **Monthly trend tracking**
- **Live dashboard** (vanilla HTML/CSS/JS) with:
  - Real-time summary cards
  - Category filter and date range filter
  - Inline edit/delete
  - Monthly spend trend chart

## Tech Stack

- Java 17
- Spring Boot 4.x (Spring Web, Spring Data JPA)
- H2 Database (file-based, embedded)
- Maven
- HTML/CSS/JavaScript (no frameworks) for the dashboard

## API Endpoints

| Method | Endpoint             | Description                          |
|--------|----------------------|---------------------------------------|
| GET    | `/expenses`          | List all expenses                     |
| POST   | `/expenses`          | Add a new expense                     |
| PUT    | `/expenses/{id}`     | Update an existing expense            |
| DELETE | `/expenses/{id}`     | Delete an expense                     |
| GET    | `/expenses/summary`  | Total spend, category breakdown, top category |
| GET    | `/expenses/monthly`  | Spend grouped by month                |
| POST   | `/expenses/sample`   | Load demo sample data                 |

### Example: Add an expense
```
POST /expenses
Content-Type: application/json

{
  "amount": 250,
  "description": "Swiggy dinner order"
}
```

## Running Locally

1. Clone the repository
   ```
   git clone <your-repo-url>
   cd expense-tracker-api
   ```

2. Run the application (uses the bundled Maven wrapper, no separate Maven install needed)
   ```
   ./mvnw spring-boot:run        # macOS/Linux
   .\mvnw.cmd spring-boot:run    # Windows
   ```

3. The API will be available at `http://localhost:8080`

4. Open `dashboard.html` in your browser to use the live dashboard (it connects to `http://localhost:8080` automatically).

## Project Structure
```
expense-tracker-api/
├── src/main/java/expense_tracker_api/
│   ├── ExpenseTrackerApiApplication.java
│   ├── Expense.java              # JPA entity
│   ├── ExpenseRepository.java    # Spring Data repository
│   ├── CategorizerService.java   # Keyword-based auto-categorization
│   └── ExpenseController.java    # REST endpoints
├── src/main/resources/
│   └── application.properties    # H2 database config
├── dashboard.html                 # Frontend dashboard (run separately in browser)
└── pom.xml
```

## Future Improvements

- Replace keyword-based categorization with a simple ML text classifier
- Add user authentication for multi-user support
- Export data to CSV
- Deploy to a cloud platform (e.g. Render, Railway)

## Author

Built as a learning project combining backend development (Java/Spring Boot) with practical full-stack skills.
