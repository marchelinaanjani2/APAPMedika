```toml
name = 'register doctor'
method = 'POST'
url = '{{url-profile}}/endUser/add'
sortWeight = 3000000
id = '24f7f0a1-8eb3-44c0-9bbd-385e04ee78f0'

[[headers]]
key = 'Authorization'
value = 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTczMjQ1ODQ0MiwiZXhwIjoxNzMyNTQ0ODQyfQ.gzSoqjkxcDob24oihd7UuX0lOYCzRR0Wd_eZ3jfsI_U'
disabled = true

[body]
type = 'JSON'
raw = '''
{
  name: "doctor",
  username: "doctor",
  password: "doctor",
  email: "doctor@doctor.com",
  gender: true,
  role: "DOCTOR",

  //jika Doctor
  specialist: 1,
  yearsOfExperience: 10,
  fee: 1000000,
  schedule: [1,3,5]
}'''
```
