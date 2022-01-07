package vn.edu.hcmus.student.sv19127520;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.Vector;

/**
 * vn.edu.hcmus.student.sv19127520;
 * Created by Phuoc -19127520
 * Date 06/01/2022 - 09:06 CH
 * Description: ...
 */
class CThread1 extends Thread{
    public static Vector<JFrame> frames=new Vector<>();
    public Socket socket;
    public JPanel panel;
    public JFrame frame;
    public String user;
    CThread1(JFrame frame,JPanel panel, Socket socket, String user){
        this.user=user;
        this.frame=frame;
        this.panel=panel;
        this.socket=socket;
    }
    public void run(){
        try {
            do {
                InputStream is = socket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String recv = br.readLine();
                System.out.println(recv);
                String[] t=recv.split("\t");
                if(t[0].equals("List")){
                    panel.removeAll();
                    panel.repaint();
                    JPanel pn=new JPanel();
                    pn.setLayout(new BoxLayout(pn,BoxLayout.Y_AXIS));
                    JLabel l=new JLabel("User online: ");
                    l.setAlignmentX(Component.CENTER_ALIGNMENT);
                    pn.add(l);
                    for (int i=1;i<t.length;i++) {
                        if(!t[i].equals(user)) {
                            JPanel p=new JPanel();
                            p.setLayout(new FlowLayout());
                            JLabel label = new JLabel(t[i]);
                            label.setFont(new Font("Verdana",Font.ITALIC,15));
                            JButton button=new JButton("Chat");
                            int finalI = i;
                            button.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    JFrame f=new JFrame(t[finalI]);
                                    for (JFrame x: frames) {
                                        if(x.getTitle().equals(t[finalI]))
                                            return;
                                    }
                                    f.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            for(JFrame x: frames){
                                                if(x.getTitle().equals(t[finalI])) {
                                                    frames.remove(x);
                                                    return;
                                                }
                                            }
                                        }
                                    });
                                    frames.add(f);
                                    f.pack();
                                    f.setVisible(true);
                                }
                            });
                            label.setAlignmentX(Component.CENTER_ALIGNMENT);
                            p.add(label);
                            p.add(button);
                            pn.add(p);
                        }
                    }
                    JScrollPane pane=new JScrollPane(pn);
                    pane.setMaximumSize(new Dimension(700,700));
                    panel.add(pane);
                }
                frame.pack();
            } while (true);
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
public class Client1 {
    public static JFrame f=new JFrame();
    public static Socket socket;
    public static String user_name;
    public static Vector<JFrame> frames=new Vector<>();
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
                    else {
                        if (_user.getText().length() == 0 || p.length() == 0 || pc.length() == 0) {
                            JOptionPane.showMessageDialog(f, "User or Password invalid or existed!");
                        } else {
                            try {
                                OutputStream os = socket.getOutputStream();
                                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
                                bw.write("SignUp" + "\t" + _user.getText() + "\t" + new String(_pass.getPassword()));
                                bw.newLine();
                                bw.flush();
                                InputStream is = socket.getInputStream();
                                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                                String recv = br.readLine();
                                if (recv.equals("OKS")) {
                                    JOptionPane.showMessageDialog(f, "SignUp Successfully", "", JOptionPane.INFORMATION_MESSAGE);
                                    f.dispose();
                                    f = new JFrame();
                                    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                    createLog();
                                } else {
                                    JOptionPane.showMessageDialog(f, "User or Password invalid or existed", "", JOptionPane.INFORMATION_MESSAGE);
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
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
    public static void createMainUI(){
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new FlowLayout());
        f.setMinimumSize(new Dimension(200,200));
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try{
                    OutputStream os = socket.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
                    bw.write("EXiT. . . EXIt...");
                    bw.newLine();
                    bw.flush();
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        });
        JPanel panel=new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
//        try{
//            do{
//                InputStream is = socket.getInputStream();
//                BufferedReader br = new BufferedReader(new InputStreamReader(is));
//                String recv = br.readLine();
//                String[] t=recv.split("\t");
//                if(t[0].equals("List")){
//                    for (String x:t) {
//                        JLabel label = new JLabel(x);
//                        f.add(label);
//                    }
//                }
//            }while (true);
//        }catch (Exception exception){
//            exception.printStackTrace();
//        }
        JTextField textField=new JTextField("",15);
        JButton button=new JButton("Find");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    OutputStream os = socket.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
                    bw.write("Chat"+"\t"+textField.getText());
                    bw.newLine();
                    bw.flush();
                    InputStream is = socket.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String recv = br.readLine();
                    if(recv.equals("OKC")){

                    }
                    else {
                        JOptionPane.showMessageDialog(f, "User offline or not exist!");
                    }
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        });
        //f.add(textField);
        f.add(panel);
        CThread1 cThread1=new CThread1(f,panel,socket,user_name);
        cThread1.start();
        //f.add(button);
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
                                f.dispose();
                                f=new JFrame(_user.getText());
                                user_name=_user.getText();
                                createMainUI();
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
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
            }
        });
        createLog();
    }catch (Exception exception){
        exception.printStackTrace();
    }
    }
}
