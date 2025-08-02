# Financial Tracker Application

_Track and manage your personal finances with ease_

---

## Project Description

The **Financial Tracker** application is designed to help users monitor and manage their income and expenses efficiently. It allows users to add, view, edit, and delete financial transactions, categorize them, and get a clear summary of their overall financial status including total income, expenses, and current balance. Users can also filter their transactions by date or category and set monthly budgets for better financial control.

This application is intended for anyone who wants to take control of their personal finances, including students, professionals, or families looking to budget and plan their spending.

I am interested in this project because managing personal finances is a valuable skill that benefits everyone. Developing this application will not only improve my programming skills but also provide a practical tool that can be used daily to encourage responsible financial habits.

---

### Key Features

- Add, view, edit, and delete income and expense transactions
- Categorize transactions for better organization
- View summaries of income, expenses, and balance
- Filter transactions by date and category
- Export financial data for backup or external use
- As a user, when I select the quit option from the application menu, I want to be reminded to save my to-do list to file and have the option to do so or not.

---

### User Stories for Financial Tracker

- As a user, I want to be able to add an income or expense entry so that I can keep track of my money flow.
- As a user, I want to be able to view a list of all my income and expense entries to see where my money is coming from and going to.
- As a user, I want to be able to categorize my transactions (e.g., groceries, rent, salary) to better organize my finances.
- As a user, I want to be able to edit or delete any transaction in case I made a mistake or need to update information.
- I want to be able to see a summary of my total income, total expenses, and current balance to understand my financial situtation
- I want to be able to filter transactions by date range or category (such as Food, Rent, Salary)
- As a user, I want to be able to save my Financial History
- As a user, I want to be able to be able to load my Financial History from file
- As a user, when I select the quit option from the application menu, I want to be reminded to save my Transactions and have the option to do so or not.

---

### GUI Requirements

The application will include a graphical user interface (GUI) that supports the following mandatory user stories:

#### Core GUI User Stories

**1. Multiple X to Y Relationship Management**

- **User Story**: "As a user, I want to be able to add multiple Transactions to a Financial Tracker"
- **Requirements**:
  - The GUI must include a panel displaying all transactions that have been added to the financial tracker
  - Implementation of two related actions:
    - A button/menu item to add a new transaction to the financial tracker
    - A button/menu item to display transactions filtered by category (e.g., show only "Food" or "Rent" transactions)

**2. Data Persistence**

- **User Story Option A**: "As a user, I want to be able to load and save the state of the application"
  - Implementation: Buttons or menu items for loading/saving data to/from file
- **User Story Option B**: "As a user, I want to be prompted with the option to load data from file when the application starts and prompted with the option to save data to file when the application ends"
  - Implementation: Pop-up windows with Yes/No buttons for load/save prompts

#### GUI Features

- Visual display panel for all financial transactions
- Interactive buttons for adding new transactions
- Filter functionality to display transaction subsets by category
- File management interface for loading and saving application state
- User-friendly prompts and confirmation dialogs
