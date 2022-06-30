package gui;

import info.Info;
import info.Pack;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.util.ArrayList;
import javax.swing.*;
import messageit.*;

public class HomeView extends JFrame {
    
//    public static void main(String[] args) {
//        new HomeView().setVisible(true);
//    }
    
    public HomeView() {
        init();
    }
    
    public HomeView(Client client, String ip) {
        this.client = client;
        this.ip = ip;
        init();
    }
    
    private void init() {
        int width=600, height=600;
        
        Color backgroundLeft = new Color(230, 230, 230),
                backgroundRight = new Color(210, 210, 210),
                backgroundUp = Color.GRAY.brighter();
        
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    btnCheck.doClick();
            }
        };
        
        labelTitle = new JLabel("MessageIt!");
        labelTitle.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 20));
        labelTitle.setBounds(width / 4 - labelTitle.getPreferredSize().width / 2, height / 10 - labelTitle.getPreferredSize().height / 2, labelTitle.getPreferredSize().width, labelTitle.getPreferredSize().height);
        
        labelIp = new JLabel("Ip Address (empty for localhost)");
        labelIp.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 12));
        labelIp.setBounds(width / 4 - labelIp.getPreferredSize().width / 2, height * 6 / 20 - labelIp.getPreferredSize().height / 2, labelIp.getPreferredSize().width, labelIp.getPreferredSize().height);
        
        fieldIp = new JTextField();
        fieldIp.setFont(new Font("Nirmala UI Semilight", Font.PLAIN, 12));
        fieldIp.setPreferredSize(new Dimension(width / 2 * 6 / 10, fieldIp.getPreferredSize().height));
        fieldIp.setBounds(width / 4 - fieldIp.getPreferredSize().width / 2, height * 7 / 20 - fieldIp.getPreferredSize().height / 2, fieldIp.getPreferredSize().width, fieldIp.getPreferredSize().height);
        fieldIp.setHorizontalAlignment(JTextField.CENTER);
        fieldIp.addKeyListener(keyAdapter);
        
        labelServerPort = new JLabel("Port");
        labelServerPort.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 12));
        labelServerPort.setBounds(width / 4 - labelServerPort.getPreferredSize().width / 2, height * 9 / 20 - labelServerPort.getPreferredSize().height / 2, labelServerPort.getPreferredSize().width, labelServerPort.getPreferredSize().height);
        
        fieldServerPort = new JTextField();
        fieldServerPort.setFont(new Font("Nirmala UI Semilight", Font.PLAIN, 12));
        fieldServerPort.setPreferredSize(new Dimension(width / 2 * 6 / 10, fieldServerPort.getPreferredSize().height));
        fieldServerPort.setBounds(width / 4 - fieldServerPort.getPreferredSize().width / 2, height * 10 / 20 - fieldServerPort.getPreferredSize().height / 2, fieldServerPort.getPreferredSize().width, fieldServerPort.getPreferredSize().height);
        fieldServerPort.setHorizontalAlignment(JTextField.CENTER);
        fieldServerPort.addKeyListener(keyAdapter);
        
        btnCheck = new JButton("Check Server");
        btnCheck.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 12));
        btnCheck.setBounds(width / 4 - btnCheck.getPreferredSize().width / 2, height * 7 / 10 - btnCheck.getPreferredSize().height / 2, btnCheck.getPreferredSize().width, btnCheck.getPreferredSize().height);
        btnCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkServer();
            }
        });
        
        keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    btnEnter.doClick();
            }
        };
        
        labelIpFound = new JLabel();
        labelIpFound.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 12));
        labelIpFound.setBounds(width / 2 / 10, height * 1 / 20 - labelIpFound.getPreferredSize().height / 2, labelIpFound.getPreferredSize().width, labelIpFound.getPreferredSize().height);
        
        labelPortFound = new JLabel();
        labelPortFound.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 12));
        labelPortFound.setBounds(width / 2 / 10, height * 2 / 20 - labelPortFound.getPreferredSize().height / 2, labelPortFound.getPreferredSize().width, labelPortFound.getPreferredSize().height);
        
        areaUsers = new JTextArea();
        areaUsers.setFont(new Font("Nirmala UI Semilight", Font.PLAIN, 10));
        areaUsers.setEnabled(false);
        areaUsers.setDisabledTextColor(Color.BLACK);
        
        scrollUsers = new JScrollPane(areaUsers);
        scrollUsers.setPreferredSize(new Dimension(width / 2 * 8 / 10, height * 8 / 20));
        scrollUsers.setBounds(width / 4 - scrollUsers.getPreferredSize().width / 2, height * 7 / 20 - scrollUsers.getPreferredSize().height / 2, scrollUsers.getPreferredSize().width, scrollUsers.getPreferredSize().height);
        
        labelPort = new JLabel("Port");
        labelPort.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 12));
        labelPort.setBounds(width / 4 - labelPort.getPreferredSize().width / 2, height * 12 / 20 - labelPort.getPreferredSize().height / 2, labelPort.getPreferredSize().width, labelPort.getPreferredSize().height);
        
        fieldPort = new JTextField();
        fieldPort.setFont(new Font("Nirmala UI Semilight", Font.PLAIN, 12));
        fieldPort.setPreferredSize(new Dimension(width / 2 * 6 / 10, fieldPort.getPreferredSize().height));
        fieldPort.setBounds(width / 4 - fieldPort.getPreferredSize().width / 2, height * 13 / 20 - fieldPort.getPreferredSize().height / 2, fieldPort.getPreferredSize().width, fieldPort.getPreferredSize().height);
        fieldPort.setHorizontalAlignment(JTextField.CENTER);
        fieldPort.addKeyListener(keyAdapter);
        
        labelName = new JLabel("Name");
        labelName.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 12));
        labelName.setBounds(width / 4 - labelName.getPreferredSize().width / 2, height * 14 / 20 - labelName.getPreferredSize().height / 2, labelName.getPreferredSize().width, labelName.getPreferredSize().height);
        
        fieldName = new JTextField();
        fieldName.setFont(new Font("Nirmala UI Semilight", Font.PLAIN, 12));
        fieldName.setPreferredSize(new Dimension(width / 2 * 6 / 10, fieldName.getPreferredSize().height));
        fieldName.setBounds(width / 4 - fieldName.getPreferredSize().width / 2, height * 15 / 20 - fieldName.getPreferredSize().height / 2, fieldName.getPreferredSize().width, fieldName.getPreferredSize().height);
        fieldName.setHorizontalAlignment(JTextField.CENTER);
        fieldName.addKeyListener(keyAdapter);
        
        btnEnter = new JButton("Enter");
        btnEnter.setBounds(width / 4 - btnEnter.getPreferredSize().width / 2, height * 17 / 20 - btnEnter.getPreferredSize().height / 2, btnEnter.getPreferredSize().width, btnEnter.getPreferredSize().height);
        btnEnter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                join();
            }
        });
        
        panelLeft = new JPanel() {
            @Override
            public void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                setBackground(backgroundLeft);
                g.setColor(backgroundUp);
                g.fillRect(0, 0, this.getWidth(), height / 5);
            }
        };
        panelLeft.setLayout(null);
        panelLeft.add(labelTitle);
        panelLeft.add(labelIp);
        panelLeft.add(fieldIp);
        panelLeft.add(labelServerPort);
        panelLeft.add(fieldServerPort);
        panelLeft.add(btnCheck);
        
        panelRight = new JPanel() {
            @Override
            public void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                setBackground(backgroundRight);
            }
        };
        panelRight.setLayout(null);
        panelRight.add(labelIpFound);
        panelRight.add(labelPortFound);
        panelRight.add(scrollUsers);
        panelRight.add(labelPort);
        panelRight.add(fieldPort);
        panelRight.add(labelName);
        panelRight.add(fieldName);
        panelRight.add(btnEnter);
        setRightComponentsEnabled(false);
        
        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));
        panel.add(panelLeft);
        panel.add(panelRight);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(null);
        setResizable(false);
        add(panel);
    }
    
    private void checkServer() {
        try {
            serverPort = Integer.parseInt(fieldServerPort.getText().trim());
        }catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Port must be numeric");
            return;
        }
        serverIp = fieldIp.getText().trim();
        
        if(Utils.validIp(serverIp) && Utils.validPort(serverPort)) {
            int tries = 0;
            if(!client.isListening())
                while(!client.listen((port = Utils.randomPort())) && ++tries < 5);
            
            if(tries < 5) client.checkServer(serverIp, serverPort);
            else JOptionPane.showMessageDialog(null, "Invalid port. Please check server again.");
        }
        else JOptionPane.showMessageDialog(null, "Invalid input");
    }
    
    public void checkServerPortValid() {
        
    }
    
    private void join() {
        if(!serverChecked) return;
        
        int port;
        try {
            port = Integer.parseInt(fieldPort.getText().trim());
        }catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Port must be numeric");
            return;
        }
        name = fieldName.getText().trim();
        
//        if(!name.equals("") && !name.equals(serverName) && ((this.port == port) || (Utils.validPort(port) && (!ip.equals(serverIp) || port != serverPort)))) {
        if(Utils.validName(name)) {
            if(this.port != port && Utils.validPort(port)) this.port = port;
            client.join(serverIp, serverPort, port, name);
        } else JOptionPane.showMessageDialog(null, "Invalid input");
    }
    
    public void checkServerRejected() {
        cleanRightComponents();
        setRightComponentsEnabled(false);
        serverIp = "";
        serverPort = -1;
        serverName = "";
        serverChecked = false;
        JOptionPane.showMessageDialog(null, "Server not found");
    }
    
    private void setRightComponentsEnabled(boolean en) {
        fieldName.setEnabled(en);
        fieldPort.setEnabled(en);
        btnEnter.setEnabled(en);
    }
    
    private void cleanRightComponents() {
        labelIpFound.setText("");
        labelPortFound.setText("");
        fieldName.setText("");
        fieldPort.setText("");
        areaUsers.setText("");
    }
    
    public void checkServerAproved(Pack pkg) {
        System.out.println(pkg);
        ArrayList<Info> infoUsers = (ArrayList<Info>)pkg.getBody();
        
        Info infoServer = infoUsers.get(0);
        String body = "Name | Ip | Port\n"
                + infoServer.getName() + " (server) | " + infoServer.getIp() + " | " + infoServer.getPort() + "\n";
        
        for(int i = 1; i < infoUsers.size(); i++) {
            Info info = infoUsers.get(i);
            body += info.getName() + " | " + infoServer.getIp() + " | " + info.getPort() + "\n";
        }
        
        labelIpFound.setText("Ip: " + infoServer.getIp());
        labelIpFound.setSize(labelIpFound.getPreferredSize().width, labelIpFound.getPreferredSize().height);
        
        labelPortFound.setText("Port: " + infoServer.getPort());
        labelPortFound.setSize(labelPortFound.getPreferredSize().width, labelPortFound.getPreferredSize().height);
        
        areaUsers.setText(body);
        setRightComponentsEnabled(true);
        fieldPort.setText(String.valueOf(port));
        
        serverIp = infoServer.getIp();
        serverPort = infoServer.getPort();
        serverName = infoServer.getName();
        
        serverChecked = true;
    }
    
    public void joinAproved() {
        System.out.println("Aproved!");
    }
    
    public void joinRejected() {
        JOptionPane.showMessageDialog(null, "Invalid data");
    }
    
    private String ip, serverIp, serverName, name;
    private int port, serverPort;
    private boolean serverChecked;
    
    private Client client;
    private JPanel panel, panelLeft, panelRight;
    private JLabel labelTitle, labelIp, labelServerPort, labelIpFound, labelPortFound, labelPort, labelName;
    private JTextField fieldIp, fieldServerPort, fieldName, fieldPort;
    private JButton btnCheck, btnEnter;
    private JTextArea areaUsers;
    private JScrollPane scrollUsers;
}
