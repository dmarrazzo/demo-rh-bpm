Order Management Demo
=======================

Description
-----------------------
The purchase department receives a purchase request (process start), a purchase specialist defines which supplier to involve and submit a "best offer request" (a bid). All the involved suppliers respond with their best offer.

The **same** purchase specialist, that submitted the request, receives the task of analysing and selecting the offers.

A manager (of the purchase department) can approve or reject the order. In case of approval the order is sent through the ERP procedure.


BPMN Design
-----------------------

![Order Management Process](docs/orders_proc.png)

Users & Roles
-----------------------

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
