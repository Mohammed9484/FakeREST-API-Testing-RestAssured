# 🤖 FakeREST API Automation Testing with RestAssured, TestNG & Maven

This project demonstrates **automation testing** for the [FakeREST API](https://fakerestapi.azurewebsites.net/) using **RestAssured**, **TestNG**, and **Maven**. It includes API validation and UI automation (if applicable), with integration-ready project structure and reports.

---

## 📌 Project Structure

```
FakeREST-Automation/
├── src/
│   ├── main/java/         # Application logic (if any)
│   └── test/java/         # TestNG tests & RestAssured scripts
│       └── tests/
├── testng.xml             # Test suite configuration
├── pom.xml                # Maven dependencies and plugins
├── reports/               # Test execution reports
└── README.md              # Project documentation
```

---

## 🧰 Technologies Used

- Java
- RestAssured
- TestNG
- Maven
- RestAssured (optional for API testing)
- Extent Reports / Allure (optional for reporting)

---

## ✨ Features

- Automated API and UI testing
- TestNG test structure with annotations
- Configurable test suites via `testng.xml`
- Maven-based dependency management
- Customizable test reports

---

## 📝 Prerequisites

- Java JDK 8 or above
- Maven
- IDE (e.g., IntelliJ IDEA, Eclipse)
- ChromeDriver or WebDriver setup

---

---

## ▶️ Running Tests

Run the full test suite:

```bash
mvn test
```

Run with a specific suite:

```bash
mvn clean test -DsuiteXmlFile=testng.xml
```

---

## 📊 Reports

Reports will be generated in the `/reports` directory. You can integrate ExtentReports or Allure for better visualization.

---

## 🧪 Test Coverage

- 🔗 **API Endpoints** – Validate RESTful API response codes and payloads
- 🖥️ **(Optional)** UI Interaction – Automate fake data management via web UI (if any)
- ✅ Assertion validation using TestNG

---

## 📬 Contact

For questions or contributions:

- 📧 Email: mohammed.hasan.abdelfattah@gmail.com
- 🐛 Issues and PRs are welcome in this repository

---


