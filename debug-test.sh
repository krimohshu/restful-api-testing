#!/bin/bash

# Debug script for Cucumber BDD tests
# This script starts Maven in debug mode and waits for VS Code debugger to attach

echo "======================================"
echo "Maven Debug Mode for Cucumber Tests"
echo "======================================"
echo ""
echo "Instructions:"
echo "1. Set breakpoints in your step definition files"
echo "2. In VS Code, press Cmd+Shift+D to open Debug view"
echo "3. Select 'Debug Cucumber Test'"
echo "4. Press F5 to attach the debugger"
echo ""
echo "Maven will wait for debugger on port 5005..."
echo "======================================"
echo ""

# Maven debug arguments
DEBUG_ARGS="-Dmaven.surefire.debug=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005"

# Check if a tag argument is provided
if [ -z "$1" ]; then
    echo "Running ALL tests in debug mode..."
    mvn test $DEBUG_ARGS "-Dcucumber.filter.tags=not @wip"
else
    echo "Running tests with tag: $1"
    mvn test $DEBUG_ARGS "-Dcucumber.filter.tags=$1"
fi
