#!/bin/bash
curl -X PUT -v -H "Content-Type: text/plain" -d "'global value'" http://localhost:8080/crud/product/test-product/component/my-component/property/simple-property?description=simple%20description
