# 💰 Expense Tracker API

A personal finance tracker built with **Java + Spring Boot**, featuring ML-based expense categorization, a REST API, persistent H2 storage, and an interactive HTML/CSS/JavaScript dashboard.

## Features

- **REST API** built with Spring Boot and Spring Data JPA
- **Persistent storage** using an embedded H2 database
- **ML-based expense categorization** using a Naive Bayes text classifier
- **Training dataset** containing labeled transaction descriptions for model training
- Automatic categorization into:
  - Food
  - Transport
  - Housing
  - Utilities
  - Entertainment
  - Education
  - Shopping
  - Health
- **Full CRUD support** — add, view, update, and delete expenses
- **Insights endpoint** — total spend, spending by category, and top spending category
- **Monthly trend tracking**
- **Interactive dashboard** built with vanilla HTML/CSS/JavaScript
  - Real-time spending summary
  - Category breakdown
  - Category filter
  - Date range filter
  - Inline edit/delete
  - Monthly spending trend chart
  - Expense entry form

## Machine Learning

The application uses a **Naive Bayes text classification approach** to automatically categorize expense descriptions.

The model is trained using labeled examples stored in:

```text
src/main/resources/training-data.csv