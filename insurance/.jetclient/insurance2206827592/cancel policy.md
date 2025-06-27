```toml
name = 'cancel policy'
method = 'PUT'
url = '{{url-insurance}}/policy/POLPGNAS0003/cancel'
sortWeight = 15000000
id = 'e40a6d46-499b-4c16-95a6-03cbdb5294ef'

[body]
type = 'JSON'
raw = '''
{
  "id": "POLBGRIY0121",
  "expiryDate": "2024-02-02"
}'''
```
