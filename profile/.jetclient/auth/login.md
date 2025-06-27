```toml
name = 'login'
method = 'POST'
url = '{{url-profile}}/auth/login'
sortWeight = 1000000
id = '1ece0f8a-4177-41b0-800c-6b5de0a8a8e5'

[body]
type = 'JSON'
raw = '''
{
  email: "admin@admin.com",
  password: "admin"
}'''
```
