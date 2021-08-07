package spaceshooter;

import java.io.*;
import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

public class SpaceShooter extends JFrame implements ActionListener {

    private static final int JFXPANEL_WIDTH_INT = 600;
    private static final int JFXPANEL_HEIGHT_INT = 480;
    private static JFXPanel fxContainer;

    JMenuBar menu_bar;
    JMenu menu_option_start, menu_option_others;
    JMenuItem menu_item_login, menu_item_logout, menu_item_play, menu_item_play_as_guest, menu_item_new_player, menu_item_statistics, menu_item_controls, menu_item_about;
    JTextArea textarea_main;
    BufferedReader buffered_reader;
    Connection connection;
    PreparedStatement prepared_statement;
    String username, password;
    int points, asteroids, death_count;
    DatabaseConnector database_connector;

    SpaceShooter() throws Exception {
        setTitle("Space Shooter Control Panel");
        setSize(600, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createUI();
        menu_item_logout.setEnabled(false);
        menu_item_play.setEnabled(false);
        setResizable(false);
        setVisible(true);
        database_connector = new DatabaseConnector();
        display("D:\\Projects\\SpaceShooter\\src\\spaceshooter\\data\\Information.txt");
    }

    void createUI() {
        menu_bar = new JMenuBar();
        menu_bar.add(new JLabel("        "));

        menu_option_start = new JMenu("Start");
        menu_item_login = new JMenuItem("Login");
        menu_item_login.addActionListener(this);
        menu_item_logout = new JMenuItem("Logout");
        menu_item_logout.addActionListener(this);
        menu_item_play = new JMenuItem("Play");
        menu_item_play.addActionListener(this);
        menu_item_play_as_guest = new JMenuItem("Play as Guest");
        menu_item_play_as_guest.addActionListener(this);
        menu_item_new_player = new JMenuItem("New Player");
        menu_item_new_player.addActionListener(this);
        menu_option_start.add(menu_item_login);
        menu_option_start.addSeparator();
        menu_option_start.add(menu_item_logout);
        menu_option_start.addSeparator();
        menu_option_start.add(menu_item_play);
        menu_option_start.addSeparator();
        menu_option_start.add(menu_item_play_as_guest);
        menu_option_start.addSeparator();
        menu_option_start.add(menu_item_new_player);
        menu_bar.add(menu_option_start);

        menu_option_others = new JMenu("Others");
        menu_item_statistics = new JMenuItem("Statistics");
        menu_item_statistics.addActionListener(this);
        menu_item_controls = new JMenuItem("Controls");
        menu_item_controls.addActionListener(this);
        menu_item_about = new JMenuItem("About");
        menu_item_about.addActionListener(this);
        menu_option_others.add(menu_item_statistics);
        menu_option_others.addSeparator();
        menu_option_others.add(menu_item_controls);
        menu_option_others.addSeparator();
        menu_option_others.add(menu_item_about);
        menu_bar.add(menu_option_others);

        setJMenuBar(menu_bar);

        textarea_main = new JTextArea();
        textarea_main.setLineWrap(true);
        textarea_main.setEditable(false);
        add(new JScrollPane(textarea_main), BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        switch (action) {
            case "Login":
                getLoginInformation();
                break;

            case "Logout":
                username = password = null;
                points = asteroids = death_count = 0;
                menu_item_login.setEnabled(true);
                menu_item_logout.setEnabled(false);
                menu_item_play.setEnabled(false);
                menu_item_play_as_guest.setEnabled(true);
                break;

            case "Play":
            case "Play as Guest":
                init();        
                break;
                
            case "New Player":
                addPlayer();
                break;
                
            case "Statistics":
                viewStatistics();
                break;
                
            case "Controls":
                display("D:\\Projects\\SpaceShooter\\src\\spaceshooter\\data\\Controls.txt");
                break;
                
            case "About":
                display("D:\\Projects\\SpaceShooter\\src\\spaceshooter\\data\\Information.txt");
                break;
        }
    }

    void display(String file) {
        try {
            textarea_main.setText("");
            buffered_reader = new BufferedReader(new FileReader(file));
            String line = buffered_reader.readLine();
            while (line != null) {
                textarea_main.append(line + "\n");
                line = buffered_reader.readLine();
            }
            buffered_reader.close();
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    
    void addPlayer(){
        try{
            String userName = JOptionPane.showInputDialog(this,"Provide Username");
            if(userName != null)
            {
                String password=JOptionPane.showInputDialog(this,"Provide Password");
                if(password != null)
                    JOptionPane.showMessageDialog(this,database_connector.addPlayer(userName,password)+" player added");
                else
                    JOptionPane.showMessageDialog(this,"Password cannot be empty");
            }
            else
                JOptionPane.showMessageDialog(this,"Username cannot be empty");
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(this,ex.getMessage());
        }
    }

    void getLoginInformation() {
        try {
            username = JOptionPane.showInputDialog(this, "Username");
            if (database_connector.validateUsername(username)) {
                password = JOptionPane.showInputDialog(this, "Password");
                if (database_connector.validatePassword(password)) {
                    menu_item_login.setEnabled(false);
                    menu_item_logout.setEnabled(true);
                    menu_item_play.setEnabled(true);
                    menu_item_play_as_guest.setEnabled(false);
                }
                else
                    JOptionPane.showMessageDialog(this, "Incorrect password");
            }
            else
                JOptionPane.showMessageDialog(this, "Invalid username");
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    
    void viewStatistics(){
        try{
            ResultSet rs = database_connector.getStatistics();
            textarea_main.setText("");
            textarea_main.append("Username\tHigh Score\tAsteroids\tDeaths\n");
            textarea_main.append("-----------------------------------------------------------------------------------------------------------------------\n");
            while(rs.next()){
                textarea_main.append    (
                                            rs.getString("username")+"\t"+
                                            rs.getString("points")+"\t"+
                                            rs.getString("asteroids")+"\t"+
                                            rs.getString("death_count")+"\n"
                                        );
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage()+".");
        }
    }

    public void init() {
        fxContainer = new JFXPanel();
        fxContainer.setPreferredSize(new Dimension(JFXPANEL_WIDTH_INT, JFXPANEL_HEIGHT_INT));
        add(fxContainer, BorderLayout.CENTER);
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    String[] args = {"Guest", "0", "0", "0"};
                    if(username != null)
                        args = database_connector.fetchDetails();
                    
                    new GameLoader(args).start(new Stage());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                    new SpaceShooter();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}