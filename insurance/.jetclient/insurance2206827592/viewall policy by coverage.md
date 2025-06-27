```toml
name = 'viewall policy by coverage'
method = 'GET'
url = '{{url-insurance}}/policy/viewall-by-coverage'
sortWeight = 17000000
id = '0d2bbf43-7f97-4714-98c6-3522dfb629f4'

[body]
type = 'JSON'
raw = '''
{
  "listIdTreatment": [16]
}'''
```
