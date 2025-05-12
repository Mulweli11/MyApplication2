Budgeting Android App
Overview
This is a budgeting application built for Android. It allows users to manage their income, expenses, goals, and financial progress. The app includes registration, login, and navigation functionality, leveraging SQLite for local data storage.

Features
User Authentication: Users can register and log in.

Income Tracking: Allows users to input their income and track financial goals.

Expense Tracking: Enables users to input expenses with categories, descriptions, and limits.

Goal Management: Users can set financial goals and track progress towards achieving them.

Real-Time Updates: Displays updated balance and goal progress in real-time on the home screen.

App Structure
Login Activity: Users log in with their username and password.

Registration Activity: New users can register by providing a username and password.

Home Activity: Displays the income, available balance, expenses, and goal progress.

Expense Activity: Allows users to add and manage their expenses.

Goal Activity: Allows users to set and track their financial goals.

Technologies Used
Android SDK: Used to build the app for the Android platform.

SQLite: Used for storing user data, income, expenses, and goals locally.

Kotlin: Programming language used for development.

Database Structure
The app uses an SQLite database with the following tables:

users: Stores user data (username, password).

income: Stores user income data.

goals: Stores financial goals with descriptions and target amounts.

expenses: Stores expense data with descriptions, amounts, limits, and categories.

cash_setup: Stores the initial amount of cash setup by the user.

Installation
Clone or download the repository.

Open the project in Android Studio.

Run the app on an emulator or Android device.

How to Use
Register: If you're a new user, click on the "Register" button and create an account.

Login: After registration, log in with your username and password.

Home Screen: View your income, available balance, and goals progress.

Add Income/Expense: Add income and expenses to keep track of your finances.

Set Goals: Set and track financial goals for the future.

Screenshots
(Include screenshots of the app's user interface here, if desired.)


