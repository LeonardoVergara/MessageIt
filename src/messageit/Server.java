package messageit;

import info.Info;
import info.Pack;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Server {

    public static void main(String args[]) {
        Server server;
        try {
            server = new Server(askPort(), "MessageIt");
        } catch (UnknownHostException ex) {
            System.out.println("Failed to create server");
            return;
        }
        server.start();
    }

    public Server() throws UnknownHostException {
        this(3000, "server");
    }

    public Server(int port, String name) throws UnknownHostException {
        this.port = port;
        this.name = name;
        clients = new ArrayList();
        ip = InetAddress.getLocalHost().getHostAddress();
    }

    static int askPort() {
        int port = -1;
        String pkgPrefix = "";
        Scanner s = new Scanner(System.in);

        while (port <= 0 || port > 9999) {
            try {
                System.out.print(pkgPrefix + "Type your port: ");
                port = s.nextInt();
            } catch (InputMismatchException e) {
                port = -1;
                if (pkgPrefix.equals("")) {
                    pkgPrefix = "Try Again. ";
                }
            }
        }
        return port;
    }

    void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try ( ServerSocket server = new ServerSocket(port)) {
                    System.out.println("Server open on port " + port + "!\n");
                    
                    listenByConsole();
                    
                    while (true) {
                        try (
                                 Socket client = server.accept();  ObjectInputStream in = new ObjectInputStream(client.getInputStream())) {
                            Pack pkg = (Pack) in.readObject();
                            System.out.println("Request incoming... " + pkg.getType());
                            switch (pkg.getType()) {
                                case Pack.CHECK_SERVER_REQUEST:
                                    checkRequest(pkg);
                                    break;
                                case Pack.JOIN:
                                    joinRequest(pkg);
                                    break;
                                case Pack.MESSAGE:
                                    msgRequest(pkg);
                                    break;
                                case Pack.LEAVE:
                                    quitUser(pkg);
                                    break;
                            }
                            System.out.println("");
                        } catch (Exception ex) {
                            System.out.println("ERROR: Unable to read Pack.");
                            ex.printStackTrace();
                        }
                    }

                } catch (IOException ex) {
                    System.out.println("ERROR: unable to open port.");
                    ex.printStackTrace();
                }
            }
        }).start();
    }
    
    private void listenByConsole() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    System.out.println("Options:"
                            + "\n1. List clients."
                            + "\n2. Clean server."
                            + "\n3. Close server."
                            + "\n");
                    Scanner s = new Scanner(System.in);
                    int op;
                    try {
                        op = s.nextInt();
                    }catch(InputMismatchException e) {
                        op = 0;
                    }
                    switch(op) {
                        case 1:
                            printClients();
                            break;
                        case 2:
                            cleanServer();
                            break;
                        case 3:
                            exit();
                            break;
                    }
                    System.out.println("");
                }while(true);
            }
        }).start();
    }

    boolean checkRequest(Pack pkg) throws IOException {
        Info info = (Info) pkg.getBody();
        System.out.println("Getting info: Ip=" + ip + ", Port=" + info.getPort());

        try (
                Socket socket = new Socket(info.getIp(), info.getPort());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.writeObject(new Pack(Pack.CHECK_SERVER_APROVED, infoServer(socket)));
        }
        return false;
    }

    boolean joinRequest(Pack pkg) throws IOException {
        Info info = (Info) pkg.getBody();
        
        try (
                Socket socket = new Socket("localhost", info.getPort());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            
            if (validClient(info)) {
                out.writeObject(new Pack(Pack.JOIN_APROVED, new Object[] {new Info(ip, port, name), clients}));
                
                ArrayList<Info> left = new ArrayList();
                for (Info c : clients) {
                    try(
                            Socket socket2 = new Socket(c.getIp(), c.getPort());
                            ObjectOutputStream out2 = new ObjectOutputStream(socket2.getOutputStream())
                            ) {
                        out2.writeObject(new Pack(Pack.JOIN, info));
                    }catch(ConnectException e) {
                        System.out.println("Oigan, ¿y " + c.getName() + "?");
                        left.add(c);
                    }catch(Exception e) {
                        System.out.println("Unexpected error");
                    }
                }
                for (Info c: left) clients.remove(c);
                
                clients.add(info);
                System.out.println("****** " + info.getName() + " joins the party!");
                return true;
            } else {
                out.writeObject(new Pack(Pack.JOIN_REJECTED, infoServer(socket)));
            }
        }
        return false;
    }

    void msgRequest(Pack pkg) throws IOException {
        Object[] body = (Object[]) pkg.getBody();
        Info client = (Info) body[1];

        try(
                Socket socket = new Socket(client.getIp(), client.getPort());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            if (hasClient(client)) {
//                System.out.println(client.getName() + " (" + client.getIp() + "): " + pkg.getBody());
                out.writeObject(new Pack(Pack.MESSAGE_SENT));

                for (Info c : clients)
                    if (!client.equals(c))
                        try(
                                Socket socket2 = new Socket(c.getIp(), c.getPort());
                                ObjectOutputStream out2 = new ObjectOutputStream(socket2.getOutputStream())) {
                            out2.writeObject(pkg);
                        }
            
            } else {
                System.out.println("Message rejected");
                out.writeObject(new Pack(Pack.MESSAGE_REJECTED));
            }
        }
    }

    void quitUser(Pack pkg) throws IOException {
        Info client = (Info) pkg.getBody();
        if (!hasClient(client)) return;
        
        removeClient(client);
        System.out.println("****** " + client.getName() + " leaves the chat.");

        for (Info c : clients) {
            if (!client.equals(c))
                try (
                     Socket socket = new Socket(c.getIp(), c.getPort());  ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                out.writeObject(new Pack(Pack.LEAVE, client));
            }
        }
    }

    boolean validClient(Info client) {
        if (new Info(ip, port, name).equals(client)) return false;
        
        return !hasClient(client);
    }

    boolean hasClient(Info client) {
        for (Info c : clients) {
            if (client.equals(c)) {
                return true;
            }
        }

        return false;
    }

    ArrayList<Info> infoServer(Socket socket) {
        ArrayList<Info> infoUsers = new ArrayList();

        infoUsers.add(new Info(socket.getInetAddress().getHostAddress(), port, name));
        for (Info c : clients) infoUsers.add(c);

        return infoUsers;
    }
    
    private void removeClient(Info client) {
        for(int i = 0; i < clients.size(); i++)
            if(clients.get(i).equals(client)) {
                clients.remove(i);
                break;
            }
    }
    
    private void cleanServer() {
        for(Info c: clients)
            try(
                    Socket socket = new Socket(c.getIp(), c.getPort());
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                out.writeObject(new Pack(Pack.REMOVE_FROM_SERVER));
            }catch(IOException e) {
                System.out.println("Couldn't remove " + c.getName() + ".");
            }
        clients = new ArrayList();
    }
    
    void printClients() {
        System.out.println("Clients:");
        for(Info c: clients) System.out.println(c);
    }
    
    void exit() {
//        TODO
        System.exit(0);
    }

    private final String ip, name;
    private final int port;
    private ArrayList<Info> clients;
}
