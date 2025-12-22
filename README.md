# ExpenseShare - Splitwise-like Backend System
ExpenseShare is a backend system inspired by Splitwise, designed to manage shared expenses among groups of users.
The project focuses on correct financial modeling, balance tracking, settlement logic and real-world edge cases, rather than simple CRUD operations.

---
## 🚀 Features
### 👤 User Management
- Create Users
- View all users
- Delete users
- Update users

### 👥 Group Management
- Create groups
- Add members to group
- View group members

### 💸 Expense Management
- Add shared expenses in a group
- Supported split types: EXACT, EQUAL, PERCENTAG
- Validation to ensure split correctness

### 📊 Balance Tracking (Core Logic)
- Tracks who owes whom inside a group
- Maintains raw balances as the source of truth
- Users can see how much they owe and how much others owe them

### 🔄 Balance Simplification (Netting)
- Computes **simplified (net) balances** to reduce unnecessary transactions
- Example:
  ```
  A → B : 100
  B → C : 100
  C → A : 100
  ```
  becomse
  `No settlement required`
- Simplified balances are **derived on demand**, not stored in DB

### ✅ Settlement Module
- Supports partial and full settlements
- Prevents
  - Overpayment
  - Duplicate Payment
  - Floating point precision error
- Settlements are applied to **raw balances** based on simplified balance view
- Settlement history is maintained

---
## 🧠 Key Design Decisions (What Makes This Project Stand Out)
### 1️⃣ Raw Balance vs Simplified Balance
- **Raw Balance**
  - Stored in DB
  - Created from Expenses
  - Source of truth
- **Simplified Balance**
  - Derived at runtime
  - Used for UI & settlement decisions
  - Never stored

### 2️⃣ Expense-Agnostic Settlements
- Settlements are not tied to individual expenses
- Users settle net dues, not specific bills
- This keeps settlement logic simple and scalable

### 3️⃣ Financial Integrity & Edge Case Handling
- Exact split validation
- Percentage split validation
- Floating-point precision handling
- Overpayment prevention
- Transactional consistency using `@Transactional`

---
## 🛠️ Tech Stack
- **Language**: Java
- **Framework**: Spring Boot
- **ORM**: Spring Data JPA / Hibernate
- **Database**: PostgreSQL (Dockerized)
- **Build Tool**: Maven
- **API Style**: REST

## 🗄️ Database Design (High Level)
### Core Entities
- `Users`
- `Groups`
- `GroupMember`
- `Expenses`
- `ExpenseSplit`
- `Balance`
- `Settlement`

### Relationship Highlight
- Groups ↔ Users → Many-to-Many via `GroupMember`
- Expense generate `ExpenseSplit`
- ExpenseSplits update `Balance`
- Settlements reduce `Balance`

## 📦 Running the Project
### Prerequisites
- Docker
- Java 17+
- Maven
### Start PostgreSQL
```
docker-compose up -d
```
### Run Application
```
./mvnw spring-boot:run 
```
## 📌 Future Enhancements
- Authentication & Authorization
- User dashboard summary
- Expense editing & deletion
- Unit and integration test
