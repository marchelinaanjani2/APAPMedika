```toml
name = 'update status'
method = 'PUT'
url = '{{url-insurance}}/policy/update-status'
sortWeight = 14000000
id = 'b02b0a58-09e6-451e-b462-ce028c73b47d'

[body]
type = 'JSON'
raw = '''
{
  "id": "POLBGRIY0121",
  "expiryDate": "2024-02-02"
}'''
```
