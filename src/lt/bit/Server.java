package lt.bit;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    
    public static boolean work = true;
    
    public static void main(String[] args) throws IOException {
        try (ServerSocket sc = new ServerSocket(9000);) {
            while (work) {
                Socket socket = sc.accept();
                RequestHandler rh = new RequestHandler(socket);
                rh.start();
            }
        }
//        ServerSocket sc = null;
//        Socket socket = null;
//        try {
//            sc = new ServerSocket(9000);
//            while (work) {                
//                try {
//                    socket = sc.accept();
//                    RequestHandler rh = new RequestHandler(socket);
//                    rh.start();
//                } finally {
//                    try {
//                        socket.close();
//                    } catch (Exception ex) {
//                        //ignored
//                    }
//                }                 
//            }
//        } finally {
//            try {
//                sc.close();
//            } catch (Exception ex){
//                //ignored
//            }
//        }        
    }
}
