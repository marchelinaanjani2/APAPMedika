```toml
name = 'register patient'
method = 'POST'
url = '{{url-profile}}/endUser/add'
sortWeight = 4000000
id = '841458d9-19ad-43ff-ad18-315cd8732935'

[[headers]]
key = 'Authorization'
value = 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTczMjQ1ODQ0MiwiZXhwIjoxNzMyNTQ0ODQyfQ.gzSoqjkxcDob24oihd7UuX0lOYCzRR0Wd_eZ3jfsI_U'
disabled = true

[body]
type = 'JSON'
raw = '''
{
  name: "patientreal",
  username: "patientreal",
  password: "patientreal",
  email: "patientreal@patient.com",
  gender: true,
  role: "PATIENT",

  nik: "100",
  birthPlace: "Bekasi",
  birthDate: "2004-01-01",
  pClass: 3,
}'''
```
