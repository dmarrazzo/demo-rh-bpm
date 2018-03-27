curl -i -X POST \
   -H "Content-Type:application/json" \
   -u donato:donato \
   -d \
'{
  "name" : "example",
  "description" : "",
  "owner" : "donato",
  "repositories" : []
}' \
 'http://localhost:8080/business-central/rest/organizationalunits/'

curl -i -X POST \
   -H "Content-Type:application/json" \
   -u donato:donato \
   -d \
'{
  "name":"orders", 
  "organizationalUnitName":"example", 
  "description":"", 
  "userName":"", 
  "password":"", 
  "requestType":"clone", 
  "gitURL":"https://github.com/dmarrazzo/demo-rh-bpm.git"
}' \
 'http://localhost:8080/business-central/rest/repositories/'
