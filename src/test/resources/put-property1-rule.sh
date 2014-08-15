#!/bin/bash
curl --user reconf:reconf -X PUT -v -H "Content-Type: text/plain" -d "'ruled value'" 'http://localhost:8080/crud/product/product1/component/component1/property/simple_property1/rule/rule1?desc=simple%20description&rpriority=2&rexpr=%2Fci-.*%2F'
