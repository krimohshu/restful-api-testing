#!/bin/bash

# RESTful API Testing - Quick Start Script

echo "========================================"
echo "RESTful API Testing - Quick Start"
echo "========================================"
echo ""

# Check Java version
echo "Checking Java installation..."
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 11 or higher."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 11 ]; then
    echo "❌ Java 11 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi
echo "✅ Java version OK"

# Check Maven
echo "Checking Maven installation..."
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven 3.6 or higher."
    exit 1
fi
echo "✅ Maven OK"

# Clean and compile
echo ""
echo "Building project..."
mvn clean compile

if [ $? -ne 0 ]; then
    echo "❌ Build failed"
    exit 1
fi

echo ""
echo "========================================"
echo "Choose test execution option:"
echo "========================================"
echo "1) Run all tests"
echo "2) Run smoke tests only"
echo "3) Run CRUD tests"
echo "4) Run error handling tests"
echo "5) Run edge case tests"
echo "6) Exit"
echo ""
read -p "Enter option (1-6): " option

case $option in
    1)
        echo "Running all tests..."
        mvn test
        ;;
    2)
        echo "Running smoke tests..."
        mvn test -Dcucumber.filter.tags="@smoke"
        ;;
    3)
        echo "Running CRUD tests..."
        mvn test -Dcucumber.filter.tags="@create or @retrieve or @update or @delete"
        ;;
    4)
        echo "Running error handling tests..."
        mvn test -Dcucumber.filter.tags="@negative"
        ;;
    5)
        echo "Running edge case tests..."
        mvn test -Dcucumber.filter.tags="@edge-case"
        ;;
    6)
        echo "Exiting..."
        exit 0
        ;;
    *)
        echo "Invalid option"
        exit 1
        ;;
esac

# Open report if tests completed
if [ $? -eq 0 ]; then
    echo ""
    echo "========================================"
    echo "Tests completed!"
    echo "========================================"
    echo ""
    read -p "Open HTML report? (y/n): " open_report
    if [ "$open_report" = "y" ] || [ "$open_report" = "Y" ]; then
        if [ -f "target/cucumber-reports/cucumber.html" ]; then
            open target/cucumber-reports/cucumber.html 2>/dev/null || xdg-open target/cucumber-reports/cucumber.html 2>/dev/null
        else
            echo "Report file not found"
        fi
    fi
fi
