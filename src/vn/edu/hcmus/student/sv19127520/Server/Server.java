package vn.edu.hcmus.student.sv19127520.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

class Infor{
    private String user;
    private String pass;
    private Socket socket;
    Infor(String user, String pass){
        this.user=user;
        this.pass=pass;
        this.socket=null;
    }
    public void setSocket(Socket socket){this.socket=socket;}
    public Socket getSocket(){ return socket;}
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
    private static Vector<Infor> infors=new Vector<>();
    private Set<String> user_chatting=new HashSet<>();
    public SThread(Socket s, Vector<Infor> infors){
        socket=s;
        SThread.infors =infors;
    }
    public boolean CheckAccount4Login(String a, String p){
        for (int i = 0; i < infors.size(); i++) {
            if(infors.elementAt(i).getUser().equals(a)){
                if(infors.elementAt(i).getPass().equals(p)){
                    infors.elementAt(i).setSocket(socket);
                    return true;
                }
                return false;
            }
        }
        return false;
    }
    public boolean CheckAccount4SignUp(String a, String p){
        for (int i = 0; i < infors.size(); i++) {
            if(infors.elementAt(i).getUser().equals(a)){
                return false;
            }
        }
        infors.add(new Infor(a,p));
        try{
            FileOutputStream fos = new FileOutputStream("data.txt", true);
            DataOutputStream d = new DataOutputStream(fos);
            d.writeBytes("\n" + a + " . . . " + p);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return true;
    }
    public void ConnectToChat(){
        try {
            String p="List"+"\t";
            for (int i=0;i<infors.size();i++) {
                if (infors.elementAt(i).getSocket() != null) {
                    user_chatting.add(infors.elementAt(i).getUser());
                    p += infors.elementAt(i).getUser() + "\t";
                    System.out.println(infors.elementAt(i).getSocket());
                    System.out.println(infors.elementAt(i).getUser());
                    System.out.println();
                }
                else{
                    user_chatting.remove(infors.elementAt(i).getUser());
                }
            }
            for (int i=0;i<infors.size();i++)
                if (infors.elementAt(i).getSocket()!=null){
                    OutputStream outputStream=infors.elementAt(i).getSocket().getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream));
                    bufferedWriter.write(p);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }

            do {
                InputStream is = socket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String recv = br.readLine();
                System.out.println(recv);
                String[] t=recv.split("\t"+" to "+"\t");
                if(t[0].equals("Msg")){
                    for(int i=0;i<infors.size();i++)
                        if(infors.elementAt(i).getUser().equals(t[2])&& infors.elementAt(i).getSocket()!=null){
                            OutputStream outputStream=infors.elementAt(i).getSocket().getOutputStream();
                            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream));
                            bufferedWriter.write("From"+"\t"+t[1]+"\t"+t[3]);
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                        }
                }
                else if (t[0].equals("EXIT")) {
                    if(user_chatting.contains(t[1])){
                        for(int i=0;i<infors.size();i++)
                            if(!infors.elementAt(i).getUser().equals(t[1])&& infors.elementAt(i).getSocket()!=null){
                                OutputStream outputStream=infors.elementAt(i).getSocket().getOutputStream();
                                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream));
                                bufferedWriter.write("Exit"+"\t"+t[1]);
                                bufferedWriter.newLine();
                                bufferedWriter.flush();
                            }
                        }
                        return;
                }
                else {
                    for(int i=0;i<infors.size();i++)
                        if(infors.elementAt(i).getUser().equals(t[2])&& infors.elementAt(i).getSocket()!=null){
                            OutputStream outputStream=infors.elementAt(i).getSocket().getOutputStream();
                            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream));
                            bufferedWriter.write(t[0]+"\t"+t[1]+"\t"+t[3]);
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                        }

                }
            } while (true);
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
    public void run(){
        try {
            do {
                InputStream is = socket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String recv = br.readLine();
                String[] t = recv.split("\t");
                System.out.println(recv);
                OutputStream os = socket.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
                if(t[0].equals("Login")) {
                    if (CheckAccount4Login(t[1], t[2])) {
                        bw.write("OKL");
                        bw.newLine();
                        bw.flush();
                        ConnectToChat();
                        String p="List"+"\t";
                        for (int i=0;i<infors.size();i++) {
                            if (infors.elementAt(i).getSocket() != null && infors.elementAt(i).getSocket()!=socket) {
                                p += infors.elementAt(i).getUser() + "\t";
                            }
                        }
                        for (int i=0;i<infors.size();i++)
                            if (infors.elementAt(i).getSocket()!=null&& infors.elementAt(i).getSocket()!=socket){
                                OutputStream outputStream=infors.elementAt(i).getSocket().getOutputStream();
                                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream));
                                bufferedWriter.write(p);
                                bufferedWriter.newLine();
                                bufferedWriter.flush();
                            }
                        socket=null;
                        return;
                    } else {
                        bw.write("NOL");
                        bw.newLine();
                        bw.flush();
                    }
                }
                else if(t[0].equals("SignUp")){
                    if(CheckAccount4SignUp(t[1], t[2])){

                        bw.write("OKS");
                        bw.newLine();
                        bw.flush();
                    }
                    else{
                        bw.write("NOS");
                        bw.newLine();
                        bw.flush();
                    }
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
    public static Vector<Infor> infors=new Vector<>();
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
            for(int i=0;i<t.length;i++){
                String[] q=t[i].split(" . . . ");
                infors.add(new Infor(q[0],q[1]));
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
                //sThreads.add(t);
                t.start();
            }
            while (true);
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
