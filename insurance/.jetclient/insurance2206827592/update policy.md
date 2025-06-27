```toml
name = 'update policy'
method = 'PUT'
url = '{{url-insurance}}/policy/POLHIJUN0002/update-expiry-date'
sortWeight = 13000000
id = '6eebb29d-84e4-48fb-90e4-6c7d20124cf6'

[body]
type = 'JSON'
raw = '''
{
  "expiryDate": "2024-03-01"
}'''
```
