Order Management Demo
=======================

roles:

- user
- manager
- supplier

users:

- supplier1
- supplier2
- supplier3

script

	./add-user.sh -a -u supplier1 -p password --role user,supplier
	./add-user.sh -a -u supplier2 -p password --role user,supplier
	./add-user.sh -a -u supplier3 -p password --role user,supplier
