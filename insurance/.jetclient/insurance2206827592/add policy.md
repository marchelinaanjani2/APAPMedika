```toml
name = 'add policy'
method = 'POST'
url = '{{url-insurance}}/policy/add'
sortWeight = 4000000
id = '1fc1864c-f69f-47c6-81b3-9dd8be809ddf'

[body]
type = 'JSON'
raw = '''
{
  "companyId": "5e89f6ce-b08c-4f69-9620-1073286cdad3",
  "patientId": "e17a0a4d-5faf-4ee4-85d0-699511a36a37",
  "expiryDate": "2020-01-01",
}'''
```
