package lt.bit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    
    public static void main(String[] args) throws IOException {
        try (ServerSocket sc = new ServerSocket(9000);) {
            boolean work = true;
            while (work) {
                try (Socket socket = sc.accept();) {
                    InputStream is = socket.getInputStream();
                    Reader r = new InputStreamReader(is, "UTF-8");
                    BufferedReader br = new BufferedReader(r);

                    OutputStream os = socket.getOutputStream();
                    Writer w = new OutputStreamWriter(os,  "UTF-8");
                    BufferedWriter bw = new BufferedWriter(w);

                    String line = br.readLine();
                    if (line != null && !"".equals(line)) {                        
                        String [] parts = line.split(" ");
                        if (parts.length >= 3) {
                            String fileName = parts[1];
                            System.out.println(fileName);
                            if (fileName.equals("/quit")) {
                                bw.write("HTTP/1.1 200 OK");
                                bw.newLine();
                                bw.write("Content-Type: text/html; charset=UTF-8");
                                bw.newLine();
                                bw.newLine();
                                bw.write("<html>");        
                                bw.write("<body>");
                                bw.write("<h1>Server is shutting down. Bye.</h1>");
                                bw.write("</body>");
                                bw.write("</html>");
                                work = false;
                            } else {
                                String path = "D:\\Bit\\Front end\\8 js chess";
                                
                                File f = new File(path + fileName);
                                
                                if (f.exists()) {
                                    if (f.isDirectory()) {
                                        bw.write("HTTP/1.1 200 OK");
                                        bw.newLine();
                                        bw.write("Content-Type: text/html; charset=UTF-8");
                                        bw.newLine();
                                        bw.newLine();
                                        File [] files;                                        
                                        files = f.listFiles();
                                        bw.write("<html>");        
                                        bw.write("<body>");
                                        bw.write("<a href=\"..\">..</a>");
                                        bw.write("<br>");
                                        for (File file : files) {
                                            bw.write("<a href=\"" + file.getName() 
                                                + ((file.isDirectory()) ? "/" : "")
                                                + "\">" + file.getName() + "</a>");
                                            bw.write("<br>");
                                            
                                        }
                                        bw.write("</body>");
                                        bw.write("</html>");
                                        bw.newLine();
                                    } else {
                                        bw.write("HTTP/1.1 200 OK");
                                        bw.newLine();
                                        if (fileName.endsWith(".html")) {
                                            bw.write("Content-Type: text/html");
                                            bw.newLine();  
                                        } else if (fileName.endsWith(".js")) {
                                            bw.write("Content-Type: text/javascript;charset=UTF-8");
                                            bw.newLine();
                                        } else if (fileName.endsWith(".css")) {
                                            bw.write("Content-Type: text/css");
                                            bw.newLine();
                                        } else if (fileName.endsWith(".txt;charset=UTF-8")) {
                                            bw.write("Content-Type: text/plain");
                                            bw.newLine();
                                        }
                                        bw.newLine();
                                        try (
                                            FileInputStream fis = new FileInputStream(f);
                                            Reader fr = new InputStreamReader(fis, "UTF-8");
                                            BufferedReader fbr = new BufferedReader(fr);                                                
                                        ) {

    //                                        if (!fileName.endsWith(".png")) {
    //                                            String fileLine;
    //                                            while ((fileLine = fbr.readLine()) != null) {
    //                                                bw.write(fileLine);
    //                                                bw.newLine();
    //                                            }
    //                                        }
                                            String fileLine;
                                            while ((fileLine = fbr.readLine()) != null) {
                                                bw.write(fileLine);
                                                bw.newLine();
                                            }
                                        }
        //                                    try (FileInputStream fis = new FileInputStream(f);) {
        //                                        int b = 0;
        //                                        while ((b = fis.read()) != -1) {
        //                                            os.write(b);
        //                                            bw.newLine();
        //                                        }
        //                                    }
                                    }
                                } else {
                                    write404(bw);
                                }

                            }
                            bw.flush();
                        }
                    }
                }
            }
        }
    }
    public static void write404(BufferedWriter bw) throws IOException {
        bw.write("HTTP/1.1 404 File not found");
        bw.newLine();
        bw.newLine();
    }
}
