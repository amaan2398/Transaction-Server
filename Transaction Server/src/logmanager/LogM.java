package logmanager;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogM {
	public static void manager(String from,String to,String amount){
		Logger logger = Logger.getLogger("Server ");
        FileHandler fh;
         
        try {
             
            // This block configure the logger with handler and formatter
            fh = new FileHandler("LOG/transaction.log");
            logger.addHandler(fh);
            //logger.setLevel(Level.ALL);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
             
            // the following statement is used to log any messages
            logger.info("From: "+from+" to: "+to+" amount: "+amount);
             
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
