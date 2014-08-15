#!/bin/bash
curl --user reconf:reconf -X PUT -v -H "Content-Type: text/plain" -d "'global value'" 'http://localhost:8080/crud/product/product1/component/component1/property/simple_property1?desc=simple%20description'
