package vn.edu.hcmus.student.sv19127520;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

/**
 * vn.edu.hcmus.student.sv19127520;
 * Created by Phuoc -19127520
 * Date 06/01/2022 - 09:06 CH
 * Description: ...
 */

public class Client1 {
    public static JFrame f=new JFrame();
    public static Socket socket;
    public static void Send(Socket s, String content){
        try {
            OutputStream os = s.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(content);
            bw.flush();
            bw.close();
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
    }
    public static void createReg(){
        f.dispose();
//        JFrame.setDefaultLookAndFeelDecorated(true);
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f=new JFrame("Sign Up");
        JPanel panel=new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel n=new JLabel("SIGN UP");
        n.setFont(new Font("Verdana",Font.BOLD,30));
        panel.add(n);

        JPanel panel1=new JPanel();
        panel1.setLayout(new FlowLayout());
        JPanel panel2=new JPanel();
        panel2.setLayout(new BoxLayout(panel2,BoxLayout.Y_AXIS));
        panel2.add(Box.createRigidArea(new Dimension(0,20)));

        JPanel user=new JPanel();
        user.setLayout(new FlowLayout());
        user.add(new JLabel("                      User: "));
        JTextField _user=new JTextField("",15);
        user.add(_user);
        panel2.add(user);

        JPanel pass=new JPanel();
        pass.setLayout(new FlowLayout());
        pass.add(new JLabel("              Password: "));
        JPasswordField _pass=new JPasswordField("",15);
        pass.add(_pass);
        panel2.add(pass);

        JPanel passConfirm=new JPanel();
        passConfirm.setLayout(new FlowLayout());
        passConfirm.add(new JLabel("Confirm password: "));
        JPasswordField _passConfirm=new JPasswordField("",15);
        passConfirm.add(_passConfirm);
        panel2.add(passConfirm);

        JButton signUp=new JButton("Sign Up");
        panel2.add(signUp);

        JButton retrn=new JButton("Return");
        panel2.add(retrn);

        ActionListener y=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s=e.getActionCommand();
                if(s.equals("Sign Up")){
                    String p=new String(_pass.getPassword());
                    String pc=new String(_passConfirm.getPassword());
                    if(!p.equals(pc)){
                        JOptionPane.showMessageDialog(f,"The password is not the same as the confirmation password");
                    }
                    if(_user.getText().length()==0||p.length()==0||pc.length()==0){
                        JOptionPane.showMessageDialog(f,"User or Password invalid or existed!");
                    }
                    else{
                        try {
                            OutputStream os = socket.getOutputStream();
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
                            bw.write("SignUp"+"\t"+_user.getText()+"\t"+new String(_pass.getPassword()));
                            bw.newLine();
                            bw.flush();
                            InputStream is = socket.getInputStream();
                            BufferedReader br = new BufferedReader(new InputStreamReader(is));
                            String recv=br.readLine();
                            if(recv.equals("OKS")){
                                JOptionPane.showMessageDialog(f,"SignUp Successfully","",JOptionPane.INFORMATION_MESSAGE);
                                f.dispose();
                                f=new JFrame();
                                createLog();
                            }
                            else{
                                JOptionPane.showMessageDialog(f,"User or Password invalid or existed","",JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                if(s.equals("Return")){
                    f.dispose();
                    f=new JFrame();
                    createLog();
                }
            }
        };
        signUp.addActionListener(y);
        retrn.addActionListener(y);
        panel1.add(panel2);
        f.setLayout(new BorderLayout());
        f.add(panel,BorderLayout.PAGE_START);
        f.add(panel1,BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);

    }
    public static void createLog(){
        JPanel panel=new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel n=new JLabel("LET'S CHAT");
        n.setFont(new Font("Verdana",Font.BOLD,30));
        panel.add(n);

        JPanel panel1=new JPanel();
        panel1.setLayout(new FlowLayout());
        JPanel panel2=new JPanel();
        panel2.setLayout(new BoxLayout(panel2,BoxLayout.Y_AXIS));
        panel2.add(Box.createRigidArea(new Dimension(0,20)));

        JPanel user=new JPanel();
        user.setLayout(new FlowLayout());
        user.add(new JLabel("         User: "));
        JTextField _user=new JTextField("",15);
        user.add(_user);
        panel2.add(user);

        JPanel pass=new JPanel();
        pass.setLayout(new FlowLayout());
        pass.add(new JLabel("Password: "));
        JPasswordField _pass=new JPasswordField("",15);
        pass.add(_pass);
        panel2.add(pass);

        JButton login=new JButton("Login");
        panel2.add(login);

        JPanel p=new JPanel();
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        // CThread thread = new CThread(p,socket, f);
        //thread.start();

        JPanel signUp=new JPanel();
        signUp.setLayout(new FlowLayout());
        signUp.add(new JLabel("Do not have an account? "));
        JButton sign=new JButton("Sign Up");
        signUp.add(sign);
        panel2.add(signUp);

        ActionListener y=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s=e.getActionCommand();
                try {
                    if (s.equals("Login")) {
                        if (_user.getText().length() == 0 || new String(_pass.getPassword()).length() == 0) {
                            JOptionPane.showMessageDialog(f, "User or Password invalid!");
                        } else {
                            OutputStream os = socket.getOutputStream();
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
                            bw.write("Login"+"\t"+_user.getText()+"\t"+new String(_pass.getPassword()));
                            bw.newLine();
                            bw.flush();
                            InputStream is=socket.getInputStream();
                            BufferedReader br=new BufferedReader(new InputStreamReader(is));
                            String recv=br.readLine();
                            if(recv.equals("OKL")){

                            }
                            else{
                                JOptionPane.showMessageDialog(f,"User or password not right","",JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
                    else if(s.equals("Sign Up")){
                        f.dispose();
                        f=new JFrame();
                        createReg();
                    }
                }
                catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        };
        login.addActionListener(y);
        sign.addActionListener(y);

        panel1.add(panel2);
        f.setLayout(new BorderLayout());
        f.add(panel,BorderLayout.PAGE_START);
        f.add(panel1,BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
    }
    public static void main(String[] args){
    try{
        socket=new Socket("localhost",3200);
        JFrame.setDefaultLookAndFeelDecorated(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createLog();
    }catch (Exception exception){
        exception.printStackTrace();
    }
    }
}
