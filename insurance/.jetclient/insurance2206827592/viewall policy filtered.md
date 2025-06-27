```toml
name = 'viewall policy filtered'
method = 'GET'
url = '{{url-insurance}}/policy/viewall/filtered?status=0&minCoverage&maxCoverage'
sortWeight = 10000000
id = '90517e80-adbc-4fa3-aed6-0e1e5761b410'

[[queryParams]]
key = 'status'
value = '0'

[[queryParams]]
key = 'minCoverage'

[[queryParams]]
key = 'maxCoverage'
```
