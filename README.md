# MessageIt
Socket-based chat in Java that works under the client-server model on a LAN. This program consists of two separate applications: server and client. Both server and users must be placed on the same LAN.

<img src="https://user-images.githubusercontent.com/73978713/176770899-9d9a8fca-af59-4591-bcf6-d1b0ff73386c.png" height="500">

### Built With

 - [Java Development Kit](https://www.oracle.com/java/technologies/downloads/).
 - Apache NetBeans IDE 14.

## Getting Started
### Prerequisites

 - The Java Development Kit. You can find it on [Oracle](https://www.oracle.com/java/technologies/downloads/).
 - [Apache NetBeans IDE 14](https://netbeans.apache.org).

You can clone this repsitory to get the source code

    git clone https://github.com/LeonardoVergara/MessageIt.git

## Usage

Open the project with your IDE.

### Server Application

  1. Run the class `messageit.Server.java`.
  2. Provide the listening port by console.
  3. Interact with the server!
  <img src="https://user-images.githubusercontent.com/73978713/176770895-a399eef5-23ee-4b1f-8102-14a60a6f4fa1.png" height="140">

### Client Application

 1. Run the class `messageit.Client.java`.
 2. Provide the ip address and the port of the server on the local network.
 3. Choose a port to listen and type your name.
 4. You're ready to chat!
 
 <img src="https://user-images.githubusercontent.com/73978713/176770904-73af4fa3-a91f-4d79-bf50-4172ddd0840e.png" height="500">
 <img src="https://user-images.githubusercontent.com/73978713/176770899-9d9a8fca-af59-4591-bcf6-d1b0ff73386c.png" height="500">
 
## Build

The process of generating a .jar executable is guided by your IDE.

You will need to generate two programs: the server application and the client application. Configure the project's main class with your IDE for each application (`messageit.Server.java` and `messageit.Client.java`).

<img src="https://user-images.githubusercontent.com/73978713/176773195-6ae7a368-34cd-4a8b-b8ea-cb1e1eb33a11.png" height="500">

> Note: The server application should be run from a CLI. Open your CLI on the server application folder and type the command `java -jar .\MessageIt.jar`.

## Acknowledgements

 - [pildorasinformaticas](https://youtube.com/playlist?list=PLU8oAlHdN5BktAXdEVCLUYzvDyqRQJ2lk)
