package messageit;

import info.*;
import gui.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {

    public static void main(String args[]) {
        Client c;
        try {
            c = new Client();
        } catch (UnknownHostException ex) {
            System.out.println("Failed to create client");
            return;
        }
        c.homeView = new HomeView(c, c.ip);
        c.homeView.setVisible(true);
    }

    public Client() throws UnknownHostException {
        ip = InetAddress.getLocalHost().getHostAddress();
    }

    public synchronized boolean listen(int port) {
        if(!freePort(port)) return false;
        
        stopListening();
        this.port = port;
        
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                try {
                    server = new ServerSocket(port);
                } catch (IOException ex) {
                    System.out.println("ERROR: unable to open port.");
                    return;
                }
                listening = true;
                
                try {
                    while (listening) {
                        Socket smbd;
                        try {
                            smbd = server.accept();
                        }catch(SocketException e){break;}
                        ObjectInputStream in = new ObjectInputStream(smbd.getInputStream());
                        
                        Pack pkg = (Pack) in.readObject();
                        System.out.println("Request incoming... " + pkg.getType());
                        
                        switch (pkg.getType()) {
                            case Pack.CHECK_SERVER_APROVED:
                                checkServerAproved(pkg);
                                break;
                            case Pack.JOIN_APROVED:
                                joinAproved(pkg);
                                break;
                            case Pack.JOIN_REJECTED:
                                joinRejected();
                                break;
                            case Pack.JOIN:
                                userJoined(pkg);
                                break;
                            case Pack.MESSAGE:
                                receiveMsg(pkg);
                                break;
                            case Pack.LEAVE:
                                userLeft(pkg);
                                break;
                            case Pack.REMOVE_FROM_SERVER:
                                removedFromServer();
                                break;
                        }
                    }
                    server.close();
                    System.out.println("listening on port " + port + " finished");
                } catch (IOException ex) {
                    System.out.println("ERROR: unable to receive/process pack");
                } catch (Exception ex) {
                    System.out.println("ERROR: unexpected exception");
                }
            }
        }, "Listening " + (port)).start();
        
        return true;
    }

    public void checkServer(String serverIp, int serverPort) {

        System.out.println("Getting info: Ip=" + serverIp + ", Port=" + serverPort);
        try (
                 Socket socket = new Socket(serverIp, serverPort);  ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.writeObject(new Pack(Pack.CHECK_SERVER_REQUEST, new Info(ip, port)));

        } catch (IOException ex) {
            System.out.println("ERROR: Impossible connection.");
            homeView.checkServerRejected();
        }
    }
    
    public void join(String serverIp, int serverPort, int port, String name) {
        if(this.port != port && !listen(port)) return;
        this.name = name;
        
        try (
                 Socket socket = new Socket(serverIp, serverPort);  ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.writeObject(new Pack(Pack.JOIN, new Info(ip, port, name)));

        } catch (IOException ex) {
            System.out.println("ERROR: Impossible connection.");
        }
    }

    public void sendMsg(String serverIp, int serverPort, String msg) {
//        System.out.println("Sending message: \"" + msg + "\".");
        System.out.println("Sending message");

        try (
                 Socket socket = new Socket(serverIp, serverPort);  ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.writeObject(new Pack(Pack.MESSAGE, new Object[]{msg, new Info(ip, port, name)}));
        } catch (IOException ex) {
            System.out.println("ERROR: Impossible connection.");
        }
    }
    
    public void leaveServer(String serverIp, int serverPort) {
        System.out.println("Leaving server...");
        try (
                Socket socket = new Socket(serverIp, serverPort);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.writeObject(new Pack(Pack.LEAVE, new Info(ip, port, name)));
        } catch (IOException ex) {
            System.out.println("ERROR: Couldn't leave server.");
        }
        chatView.setVisible(false);
//        chatView = null;
        homeView.setVisible(true);
    }

    public void leave(String serverIp, int serverPort) {
        System.out.println("Leaving...");
        leaveServer(serverIp, serverPort);
        stopListening();
    }

    private void checkServerAproved(Pack pkg) {
        System.out.println("Check server aproved");
        homeView.checkServerAproved(pkg);
    }

    private void receiveMsg(Pack pkg) {
        Object[] o = (Object[]) pkg.getBody();
        chatView.receiveMsg((String) o[0], (Info) o[1]);
    }

    private void userJoined(Pack pkg) {
        Info client = (Info) pkg.getBody();
        System.out.println(client.getName() + " joined!");
        chatView.userJoined(client);
    }

    private void userLeft(Pack pkg) {
        Info client = (Info) pkg.getBody();
        System.out.println(client.getName() + " left");
        chatView.userLeft(client);
    }
    
    private void removedFromServer() {
        chatView.removedFromServer();
    }

    private void joinAproved(Pack pkg) {
        System.out.println("Join server aproved");
        
        Object[] o = (Object[]) pkg.getBody();
        ArrayList<Info> clients = new ArrayList();
        clients.add(new Info(ip, port, name));
        for(Info c: (ArrayList<Info>) o[1]) clients.add(c);
        
        homeView.setVisible(false);
        chatView = new Chat(this, (Info) o[0], clients);
        chatView.setVisible(true);
    }

    private void joinRejected() {
        System.out.println("Couldn't join the server");
        homeView.joinRejected();
    }
    
    public void stopListening() {
        if(!listening) return;
        
        listening = false;
        try {
            server.close();
        } catch (IOException ex) {
            System.out.println("Couldn't finish listening");
        }
    }
    
    public static synchronized boolean freePort(int port) {
        try(ServerSocket socket = new ServerSocket(port)) {
            
        }catch(IOException e) {
            return false;
        }
        return true;
    }
    
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HomeView getHomeView() {
        return homeView;
    }

    public void setHomeView(HomeView homeView) {
        this.homeView = homeView;
    }

    public Chat getChatView() {
        return chatView;
    }

    public void setChatView(Chat chatView) {
        this.chatView = chatView;
    }

    public boolean isListening() {
        return listening;
    }

    public void setListening(boolean listening) {
        this.listening = listening;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public ServerSocket getServer() {
        return server;
    }

    public void setServer(ServerSocket server) {
        this.server = server;
    }
    
    private int port;
    private String ip, name;
    private HomeView homeView;
    private Chat chatView;
    private volatile boolean listening;
    private ServerSocket server;
}
