#!/bin/bash
curl -X PUT -v -H "Content-Type: text/plain" -d "'developer1 value'" \
http://localhost:8080/crud/product/test-product/component/my-component/property/simple-property?instance=developer1&description=simple%20description
