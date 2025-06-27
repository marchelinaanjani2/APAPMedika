```toml
name = 'get detail user by id or name or username'
method = 'GET'
url = '{{url-profile}}/endUser/detail?id=a00c7b90-757d-41d3-b917-82f6e021e7aa&username=admin&email=patient@patient.com'
sortWeight = 5000000
id = '1b5b235f-1b82-4330-95e2-6fcdf317e33e'

[[queryParams]]
key = 'id'
value = 'a00c7b90-757d-41d3-b917-82f6e021e7aa'

[[queryParams]]
key = 'username'
value = 'admin'

[[queryParams]]
key = 'email'
value = 'patient@patient.com'

[[headers]]
key = 'Authorization'
value = 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTczMjQ1ODQ0MiwiZXhwIjoxNzMyNTQ0ODQyfQ.gzSoqjkxcDob24oihd7UuX0lOYCzRR0Wd_eZ3jfsI_U'

[body]
type = 'JSON'
raw = '''
{
  name: "patient",
  username: "patient",
  password: "patient",
  email: "patient@patient.com",
  gender: true,
  role: "NURSE",

  //jika Patient
  nik: "1",
  birthPlace: "Bekasi",
  birthDate: "2004-01-01",
  pClass: 3,
}'''
```
