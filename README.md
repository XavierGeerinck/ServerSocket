ServerSocket
============

Server socket is a framework to make sockets on.
It is mostly based to create game servers.

Advantages:
- Huge scalability
- Easy management of the different sockets.
- Server can control the sockets
- You can put the clients on another network and still be able to communicate with the server.
- You can disconnect a client without interrupting the server, this means that you can take down parts of the system without having any downtime of the server.

Example:
- Worldserver
- Client CHAT
- Client INSTANCE
- Client BATTLEGROUND

The worldserver now acts as the request manager and will accept connections of the chat, the instance and battleground clients.
Now the clients can ask data from the worldserver and the worldserver can manage the sockets.
You can now say for example: "sendall message" and it will send a message to all the clients.
You can also say "closeSocket socket" and that will close a connected client.