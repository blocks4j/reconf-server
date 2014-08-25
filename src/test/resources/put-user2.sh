#!/bin/bash
curl --user reconf:reconf -v -X PUT  -H "Content-Type: application/xml" -d "<user><name>user2</name><password>uol123</password></user>" 'http://localhost:8080/crud/user'
