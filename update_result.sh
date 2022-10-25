#!/usr/bin/env bash
ZIP_FILE="tm4j_result.zip"
AUTO_CREATE_TEST_CASES="true"
JIRA_URL="https://jira.xxxx.com.vn"

PROJECT_KEY=$([[ "$PROJECT_KEY" == "" ]] && echo "QE" || echo "$PROJECT_KEY")
PROJECT_ID=$([[ "$PROJECT_ID" == "" ]] && echo "11702" || echo "$PROJECT_ID")
PLAN_NAME=$([[ "$PLAN_NAME" == "" ]] && echo "Automated test executed on "${ENV}"" || echo "$PLAN_NAME")


echo "Removing old zip files"
rm -f $BUILD_DIR/$ZIP_FILE

echo "Sending zip file to TM4J"
zip $BUILD_DIR/$ZIP_FILE $BUILD_DIR/tm4j_result.json -j

echo "Sending a zip to TM4J"
response=$(curl -s --user $JIRA_USER:$JIRA_PASS -F "file=@$BUILD_DIR/$ZIP_FILE;type=application/x-zip-compressed" -X POST $JIRA_URL/rest/atm/1.0/automation/execution/$PROJECT_KEY?autoCreateTestCases=$AUTO_CREATE_TEST_CASES
)

echo "response: ${response}"

key=$(echo $response | grep -oP '(?<="key":")[^"]*')
id=$(echo $response | grep -oP '(?<="id":)[^,]*')

if [ "$key" == "" ] || [ "$id" == "" ] || [ "$PLAN_NAME" == "" ]; then
  echo "Empty"
else
  now=$(date "+%Y-%m-%d %R")
  echo "key: $key"
  echo "id: $id"
  echo ""
  echo "Cycle Name: ${PLAN_NAME}"
  curl -s --user $JIRA_USER:$JIRA_PASS -X PUT -H "Content-Type: application/json" -d '{"id":'$id', "description":"<h1>'$BUILD_URL'Automation_20Report/<h1>", "projectId":'$PROJECT_ID', "name": "'"${PLAN_NAME} at ${now}"'"}' "$JIRA_URL/rest/tests/1.0/testrun/$key"
fi

echo "Finished"