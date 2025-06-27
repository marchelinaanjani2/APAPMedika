```toml
name = 'viewall patient policy filtered'
method = 'GET'
url = '{{url-insurance}}/policy/viewall/1/filtered?status=0&minCoverage=0&maxCoverage=100000000'
sortWeight = 11000000
id = '74d0faff-d5bf-4a08-b4c3-10731ca39a02'

[[queryParams]]
key = 'status'
value = '0'

[[queryParams]]
key = 'minCoverage'
value = '0'

[[queryParams]]
key = 'maxCoverage'
value = '100000000'
```
