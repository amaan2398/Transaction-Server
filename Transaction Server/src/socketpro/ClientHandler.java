package socketpro;

import java.io.DataInputStream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

import mysqldatabasehandling.SqlHandling;

public class ClientHandler extends Thread{
	private SqlHandling sql;
	final DataInputStream dis; 
    final DataOutputStream dos; 
    final Socket s; 
    
    private String LogID;
    private String Pass;
    private String info;
  
    private String amount;
    private String from;
    private String to;
    // Constructor 
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)  
    { 
        this.s = s; 
        this.dis = dis; 
        this.dos = dos; 
    }
    //String Slice for different method
    public void stringSlice(String str,String who) {
    	StringTokenizer st1 = new StringTokenizer(str);
    	if(who.equals("login")) {
    		LogID=st1.nextToken();
    		Pass=st1.nextToken();
    	}
    	else if (who.equals("transaction")) {
    		amount=st1.nextToken();
    		from=st1.nextToken();
    		to=st1.nextToken();
    	}
    }
    //thread run method
    @Override
    public void run()  
    { 
    	
        String received; 

        while (true)  
        {
        	sql=new SqlHandling();
            try { 
  
                // receive the answer from client 
                received = dis.readUTF(); 
                
                if(received.equals("Exit")) 
                {  
                    System.out.println("Client " + this.s + " sends exit..."); 
                    System.out.println("Closing this connection."); 
                    this.s.close(); 
                    System.out.println("Connection closed"); 
                    break; 
                }
                switch(received) {
                case "login":
                	receivelogin();
                	break;
                case "transaction":
                	receivetransaction();
                	break;
                case "refresh":
                	receiverefresh();
                	break;
                }
                
                 
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } 
          
        try
        { 
            // closing resources 
            this.dis.close(); 
            this.dos.close(); 
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
    }
    private void receivelogin() {
    	String received; 
        String toreturn;
        try {
			received=dis.readUTF();
			stringSlice(received,"login");
			synchronized(sql) {
				SqlHandling.mysqlconnect("root", "Root123");
				info=SqlHandling.logincheck(LogID,Pass);
				SqlHandling.mysqldissconnect();
			}      
			toreturn=info;
			dos.writeUTF(toreturn);
		} catch (Exception e) {
			System.out.println("Login Error Server!!!");
			System.out.println(e);
		}

    }
    
    private void receivetransaction() {
    	String received; 
        String toreturn;
    	try {
			received=dis.readUTF();
			stringSlice(received,"transaction");
			synchronized(sql) {
				SqlHandling.mysqlconnect("root", "Root123");
				toreturn=SqlHandling.transaction(amount, from, to);
				SqlHandling.mysqldissconnect();
			}
			dos.writeUTF(toreturn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    private void receiverefresh(){
    	String received; 
        String toreturn;
        try {
			received=dis.readUTF();
			synchronized(sql) {
				SqlHandling.mysqlconnect("root", "Root123");
				toreturn=SqlHandling.refresh(received);
				SqlHandling.mysqldissconnect();
			}
			dos.writeUTF(toreturn);
		} catch (Exception e) {
			System.out.println("Error in Server refresh!!");
		}
    }


}
