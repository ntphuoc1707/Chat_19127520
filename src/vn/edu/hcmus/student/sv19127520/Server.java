package vn.edu.hcmus.student.sv19127520;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
class Infor{
    private String user;
    private String pass;
    Infor(String user, String pass){
        this.user=user;
        this.pass=pass;
    }
    public String getUser() {
        return user;
    }
    public String getPass(){
        return pass;
    }
    public void setPass(String pass){
        this.pass=pass;
    }
}
class SThread extends Thread{
    private Socket socket;
    private static Infor[] infors;
    public SThread(Socket s, Infor[] infors){
        socket=s;
        this.infors=infors;
    }
    public static boolean CheckAccount(String a, String p){
        for (int i = 0; i < infors.length; i++) {
            if(infors[i].getUser().equals(a)){
                if(infors[i].getPass().equals(p)){
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public void run(){
        try {
            do {
                InputStream is = socket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String recv = br.readLine();
                String[] t = recv.split("\t");
                OutputStream os = socket.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
                if (CheckAccount(t[0], t[1])) {
                    bw.write("OKL");
                    bw.newLine();
                    bw.flush();
                } else {
                    bw.write("NOL");
                    bw.newLine();
                    bw.flush();
                }
            }
            while (true);
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
public class Server {
    public static Vector<SThread> sThreads=new Vector<>();
    public static Vector<String> user=new Vector<>();
    public static Infor[] infors;
    public static void LoadData(){
        String infor="";
        try{
            File file = new File("data.txt");
            InputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            int c;
            while ((c = inputStreamReader.read()) != -1){
                infor+=(char)c;
            }
            String[] t=infor.split("\n");
            infors=new Infor[t.length];
            for(int i=0;i<t.length;i++){
                String[] q=t[i].split(" . . . ");
                infors[i]=new Infor(q[0],q[1]);
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try{
            ServerSocket serverSocket=new ServerSocket(3200);
            LoadData();
            do{
                Socket socket=serverSocket.accept();
                SThread t=new SThread(socket,infors);
                sThreads.add(t);
                t.start();
            }
            while (true);
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
