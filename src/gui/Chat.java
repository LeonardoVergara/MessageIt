package gui;

import info.Info;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import messageit.Client;

public class Chat extends JFrame {

//    public static void main(String[] args) {
//        new Chat(null, new Info("localhost", 1, "server"), new ArrayList<Info>()).setVisible(true);
//    }

    public Chat(Client client, Info server, ArrayList<Info> clients) {
        this.client = client;
        this.server = server;
        this.clients = clients;
        connected = true;
        init();
    }

    private void init() {
        int width = 660, height = 600, widthLeft = (int)(width * 0.3), widthRight = width - widthLeft;
        
        btnBack = new JButton("Back");
        btnBack.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 10));
        btnBack.setBounds(10, height * 1 / 20 - btnBack.getPreferredSize().height / 2, btnBack.getPreferredSize().width, btnBack.getPreferredSize().height);
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leaveServer();
            }
        });
        
        labelServer = new JLabel("Server");
        labelServer.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 14));
        labelServer.setBounds(10, height * 2 / 20 - labelServer.getPreferredSize().height / 2, labelServer.getPreferredSize().width, labelServer.getPreferredSize().height);

        labelServerName = new JLabel(server.getName());
        labelServerName.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 12));
        labelServerName.setBounds(10, height * 3 / 20 - labelServerName.getPreferredSize().height / 2, labelServerName.getPreferredSize().width, labelServerName.getPreferredSize().height);

        labelServerInfo = new JLabel(server.getIp() + ":" + server.getPort());
        labelServerInfo.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 12));
        labelServerInfo.setBounds(10, height * 4 / 20 - labelServerInfo.getPreferredSize().height / 2, labelServerInfo.getPreferredSize().width, labelServerInfo.getPreferredSize().height);
        
        labelClients = new JLabel("Users");
        labelClients.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 14));
        labelClients.setBounds(10, height * 6 / 20 - labelClients.getPreferredSize().height / 2, labelClients.getPreferredSize().width, labelClients.getPreferredSize().height);

        panelClients = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(BACKGROUND_LEFT);
            }
        };
        panelClients.setLayout(new BoxLayout(panelClients, BoxLayout.PAGE_AXIS));
        for(Info c: clients) addClientToPanel(c);
        
        scrollClients = new JScrollPane(panelClients);
        scrollClients.setPreferredSize(new Dimension(widthLeft - 2 * 10, height * 6 / 20));
        scrollClients.setBounds(10, height * 10 / 20 - scrollClients.getPreferredSize().height / 2, scrollClients.getPreferredSize().width, scrollClients.getPreferredSize().height);
        
        panelLeft = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(BACKGROUND_LEFT);
            }
        };
        panelLeft.setBounds(0, 0, widthLeft, height);
        panelLeft.setLayout(null);
        panelLeft.add(btnBack);
        panelLeft.add(labelServer);
        panelLeft.add(labelServerName);
        panelLeft.add(labelServerInfo);
        panelLeft.add(labelClients);
        panelLeft.add(scrollClients);
        
        areaMsgs = new JTextArea("Welcome to " + server.getName() + "! Have a seat.\n\n");
        areaMsgs.setFont(new Font("Nirmala UI Semilight", Font.PLAIN, 12));
        areaMsgs.setEnabled(false);
        areaMsgs.setDisabledTextColor(Color.BLACK);
        
        scrollMsgs = new JScrollPane(areaMsgs);
        scrollMsgs.setPreferredSize(new Dimension(widthRight * 9 / 10, height * 16 / 20));
        scrollMsgs.setBounds(widthRight / 2 - scrollMsgs.getPreferredSize().width / 2, height * 9 / 20 - scrollMsgs.getPreferredSize().height / 2, scrollMsgs.getPreferredSize().width, scrollMsgs.getPreferredSize().height);
        
        fieldMsg = new JTextField();
        fieldMsg.setFont(new Font("Nirmala UI Semilight", Font.PLAIN, 12));
        fieldMsg.setBounds(widthRight * 5 / 100, height * 18 / 20 - fieldMsg.getPreferredSize().height / 2, widthRight * 70 / 100, fieldMsg.getPreferredSize().height);
        fieldMsg.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    btnSend.doClick();
            }
        });
        
        btnSend = new JButton("Send");
        btnSend.setFont(new Font("Nirmala UI Semilight", Font.PLAIN, 12));
        btnSend.setBounds(widthRight * 5 / 100 + fieldMsg.getSize().width + widthRight * 5 / 100, height * 18 / 20 - btnSend.getPreferredSize().height / 2, widthRight * 15 / 100, btnSend.getPreferredSize().height);
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                send();
            }
        });
        
        panelRight = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(BACKGROUND_RIGHT);
                
                fieldMsg.requestFocus();
            }
        };
        panelRight.setBounds(widthLeft, 0, widthRight, height);
        panelRight.setLayout(null);
        panelRight.add(scrollMsgs);
        panelRight.add(fieldMsg);
        panelRight.add(btnSend);
        
        panel = new JPanel();
        panel.setLayout(null);
        panel.add(panelLeft);
        panel.add(panelRight);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(null);
        setResizable(false);
        add(panel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                leave();
            }
        });
    }

    private void send() {
        String msg = fieldMsg.getText().trim();
        fieldMsg.setText("");
        if(msg.equals("")) return;
        
        client.sendMsg(server.getIp(), server.getPort(), msg);
        appendMsgToArea("  You: " + msg + "\n\n");
    }
    
    private void leave() {
        client.leaveServer(server.getIp(), server.getPort());
        client.stopListening();
    }
    
    private void leaveServer() {
        client.leaveServer(server.getIp(), server.getPort());
    }

    public void userJoined(Info client) {
        clients.add(client);
        addClientToPanel(client);
        panelClients.revalidate();
//        panelClients.repaint();
        appendMsgToArea("* " + client.getName() + " joined the server\n\n");
    }

    public void userLeft(Info client) {
        removeClient(client);
        removeClientFromPanel(client);
        panelClients.revalidate();
        panelClients.repaint(); // No sé por qué no funciona sin el repaint xdd
        appendMsgToArea("* " + client.getName() + " left the server\n\n");
    }

    public void removedFromServer() {
        connected = false;
        appendMsgToArea("* You've been taken off this server\n\n");
    }
    
    public void receiveMsg(String msg, Info info) {
        appendMsgToArea(info.getName() + ": " + msg + "\n\n");
    }
    
    void appendMsgToArea(String msg) {
        areaMsgs.append(msg);
        JScrollBar scrollVert = scrollMsgs.getVerticalScrollBar();
        scrollVert.setValue(scrollVert.getMaximum());
    }
    
    private void addClientToPanel(Info client) {
        JLabel labelName = new JLabel(client.getName());
        labelName.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 12));
        labelName.setBorder(new EmptyBorder(8, 10, 0, 0));

        JLabel labelInfo = new JLabel(client.getIp() + ":" + client.getPort());
        labelInfo.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 12));
        labelInfo.setBorder(new EmptyBorder(0, 10, 8, 0));
        
        JPanel panelClient = new PanelClient(client);
        panelClient.setLayout(new BoxLayout(panelClient, BoxLayout.PAGE_AXIS));
        panelClient.add(labelName);
        panelClient.add(labelInfo);
        
        panelClients.add(panelClient);
    }
    
    private void removeClient(Info client) {
        for(int i = 0; i < clients.size(); i++)
            if(clients.get(i).equals(client)) {
                clients.remove(i);
                break;
            }
    }
    
    private void removeClientFromPanel(Info client) {
        for(int i = 0; i < panelClients.getComponentCount(); i++)
            if( ((PanelClient)panelClients.getComponent(i)).client.equals(client)) {
                panelClients.remove(i);
                break;
            }
    }
    
    private class PanelClient extends JPanel  {
        PanelClient(Info client) {
            this.client = client;
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(BACKGROUND_LEFT);
        }
        
        Info client;
    }
    
    private ArrayList<Info> clients;
    private Info server;
    private boolean connected;
    
    private Client client;
    private JPanel panel, panelLeft, panelRight, panelClients;
    private JScrollPane scrollClients;
    private JLabel labelServer, labelServerName, labelServerInfo, labelClients;
    private JTextField fieldMsg;
    private JButton btnSend, btnBack;
    private JTextArea areaMsgs;
    private JScrollPane scrollMsgs;
    
    private static final Color 
            BACKGROUND_RIGHT = new Color(230, 230, 230),
            BACKGROUND_LEFT = new Color(210, 210, 210);
    
}
