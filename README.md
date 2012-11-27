ServerSocket
============

The purpose of this project is to provide a stable base of a server socket, the user can make a server and different sockets to be able to scale it's server. Example: Worldserver is the loadbalancer (the server) and you got clients like : Instance, Chat, Item, Battleground, ... those connect to the server and the server can manage those. The purpose is to allow the spreading of the clients over different racks.