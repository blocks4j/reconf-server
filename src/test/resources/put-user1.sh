#!/bin/bash
curl --user reconf:reconf -v -X PUT  -H "Content-Type: application/json" -d "{\"username\":\"user1\", \"password\": \"uol123\"}" 'http://localhost:8080/crud/user'
