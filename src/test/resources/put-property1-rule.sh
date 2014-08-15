#!/bin/bash
curl -X PUT -v -H "Content-Type: text/plain" -d "'ruled value'" 'http://localhost:8080/crud/product/product1/component/component1/property/simple_property1/restricted?desc=simple%20description&rname=rule1&rpriority=2&rexpr=%2Fci-.*%2F'
