import java.io.*;
import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class SpaceShooterMain extends JFrame implements ActionListener{
    JMenuBar mnuBar;
    JMenu mnuStart, mnuOthers;
    JMenuItem itmLogin, itmLogout, itmPlay, itmPlayAsGuest, itmNewPlayer, itmStatistics, itmControls, itmAbout;
    JTextArea txtMain, txtWest, txtSouth;
    FileReader fr;
    BufferedReader br;
    Connection conn;
    PreparedStatement pstmt;
    String username, password;
    int points, asteroids, death_count;
    DatabaseConnector dbc;
    
    SpaceShooterMain() throws Exception
    {
        setTitle("SSM Control Panel");
        setSize(600,480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createUI();
        itmLogout.setEnabled(false);
        itmPlay.setEnabled(false);
        setResizable(false);
        setVisible(true);
        dbc=new DatabaseConnector();
        display("data/Information.txt");
    }

    void createUI(){
        mnuBar=new JMenuBar();
        mnuBar.add(new JLabel("        "));

        mnuStart=new JMenu("Start");
        itmLogin=new JMenuItem("Login");
        itmLogin.addActionListener(this);
        itmLogout=new JMenuItem("Logout");
        itmLogout.addActionListener(this);
        itmPlay=new JMenuItem("Play");
        itmPlay.addActionListener(this);
        itmPlayAsGuest=new JMenuItem("Play as Guest");
        itmPlayAsGuest.addActionListener(this);
        itmNewPlayer=new JMenuItem("New Player");
        itmNewPlayer.addActionListener(this);
        mnuStart.add(itmLogin);
        mnuStart.addSeparator();
        mnuStart.add(itmLogout);
        mnuStart.addSeparator();
        mnuStart.add(itmPlay);
        mnuStart.addSeparator();
        mnuStart.add(itmPlayAsGuest);
        mnuStart.addSeparator();
        mnuStart.add(itmNewPlayer);
        mnuBar.add(mnuStart);

        mnuOthers=new JMenu("Others");
        itmStatistics=new JMenuItem("Statistics");
        itmStatistics.addActionListener(this);
        itmControls=new JMenuItem("Controls");
        itmControls.addActionListener(this);
        itmAbout=new JMenuItem("About");
        itmAbout.addActionListener(this);
        mnuOthers.add(itmStatistics);
        mnuOthers.addSeparator();
        mnuOthers.add(itmControls);
        mnuOthers.addSeparator();
        mnuOthers.add(itmAbout);
        mnuBar.add(mnuOthers);

        setJMenuBar(mnuBar);

        txtWest=new JTextArea(2,2);
        txtWest.setBackground(Color.GRAY);
        txtWest.setEditable(false);
        add(txtWest, BorderLayout.WEST);
        txtSouth=new JTextArea(1,2);
        txtSouth.setBackground(Color.GRAY);
        txtSouth.setEditable(false);
        add(txtSouth, BorderLayout.SOUTH);
        txtMain=new JTextArea();
        txtMain.setLineWrap(true);
        txtMain.setEditable(false);
        add(new JScrollPane(txtMain),BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent ae){
        String actionCommand=ae.getActionCommand();
        switch(actionCommand){
            case "Login":
                getLoginInformation();
                break;

            case "Logout":
                setTitle("SSM Control Panel");
                username=null;
                password=null;
                points=0;
                death_count=0;
                itmLogin.setEnabled(true);
                itmLogout.setEnabled(false);
                itmPlay.setEnabled(false);
                itmPlayAsGuest.setEnabled(true);
                break;

            case "Play":
                initGame(username);
                break;

            case "Play as Guest":
                initGame("Guest");        
                break;

            case "New Player":
                addPlayer();
                break;

            case "Statistics":
                viewStatistics();
                break;

            case "Controls":
                display("data/Controls.txt");
                break;

            case "About":
                display("data/Information.txt");
                break;
        }
    }

    void display(String file){
        try{
            txtMain.setText("");
            fr=new FileReader(file);
            br=new BufferedReader(fr);
            String line=br.readLine();
            while(line!=null){
                txtMain.append(line+"\n");
                line=br.readLine();
            }
            fr.close();
            br.close();
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(this,ex.getMessage());
        }
    }

    void getLoginInformation(){
        try{
            username=JOptionPane.showInputDialog(this,"Username");
            if(dbc.validateUsername(username))
            {
                password=JOptionPane.showInputDialog(this,"Password");
                if(dbc.validatePassword(password))
                {
                    itmLogin.setEnabled(false);
                    itmLogout.setEnabled(true);
                    itmPlay.setEnabled(true);
                    itmPlayAsGuest.setEnabled(false);
                    setTitle("SSM Control Panel - ["+username+"]");
                }
                else
                    JOptionPane.showMessageDialog(this,"Incorrect password");
            }
            else
                JOptionPane.showMessageDialog(this,"Invalid username");
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(this,ex.getMessage());
        }
    }

    void addPlayer(){
        try{
            String userName=JOptionPane.showInputDialog(this,"Provide Username");
            if(userName!=null)
            {
                String password=JOptionPane.showInputDialog(this,"Provide Password");
                if(password!=null)
                    JOptionPane.showMessageDialog(this,dbc.addPlayer(userName,password)+" player added");
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

    void initGame(String uname){
        try{
            points=dbc.getPoints(uname);
            asteroids=dbc.getAsteroids(uname);
            death_count=dbc.getDeathCount(uname);
            String args=uname+" "+points+" "+asteroids+" "+death_count;
            String fx_path="--module-path \"E:\\Java\\openjfx-11.0.2_windows-x64_bin-sdk\\javafx-sdk-11.0.2\\lib\" --add-modules javafx.controls";
            Runtime.getRuntime().exec("javac "+fx_path+" GameLoader.java");
            Runtime.getRuntime().exec("cmd /c start cmd.exe /K java "+fx_path+" GameLoader.java "+args);
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(this,ex.getMessage()+".\nPlease restart the application.");
        }
    }

    void viewStatistics()
    {
        try{
            ResultSet rs=dbc.getStatistics();
            txtMain.setText("");
            txtMain.append("Username\tHigh Score\tAsteroids\tDeaths\tK/D Ratio \n");
            txtMain.append("-----------------------------------------------------------------------------------------------------------------------\n");
            while(rs.next())
            {
                int pts=rs.getInt("points");
                int ad=rs.getInt("asteroids");
                int dc=rs.getInt("death_count");
                double kd=(dc==0)?0:ad/dc;
                txtMain.append(rs.getString("username")+"\t"+pts+"\t"+ad+"\t"+dc+"\t"+kd+"\n");
            }
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(this,ex.getMessage()+".");
        }
    }


    public static void main(String[] args) throws Exception
    {
        new SpaceShooterMain();
    }
}
