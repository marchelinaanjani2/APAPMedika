```toml
name = 'register end user'
method = 'POST'
url = '{{url-profile}}/endUser/add'
sortWeight = 2000000
id = 'd50f9ca5-dea4-4ceb-94a9-e724120a92bb'

[[headers]]
key = 'Authorization'
value = 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTczMjQ1ODg5NywiZXhwIjoxNzMyNTQ1Mjk3fQ.v_ef-mzFlbe6LNm8sPfRtP_G7qhhpyx2fbsyMWaV3D8'
disabled = true

[body]
type = 'JSON'
raw = '''
{
  name: "end user",
  username: "enduser",
  password: "enduser",
  email: "enduser@enduser.com",
  gender: true,
  role: "NURSE",
}'''
```
