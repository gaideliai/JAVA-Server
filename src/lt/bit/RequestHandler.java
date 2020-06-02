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
import java.net.Socket;

public class RequestHandler extends Thread {
    
    private Socket sc;
    private File f;
    private String fileName;
    
    public RequestHandler(Socket sc) {
        if (sc == null) {
            throw new NullPointerException("Socket cannot be null");
        }
        this.sc = sc;
    }
    
    @Override
    public void run() {
        try {
            InputStream is = this.sc.getInputStream();
            Reader r = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(r);
            
            OutputStream os = this.sc.getOutputStream();
            Writer w = new OutputStreamWriter(os,  "UTF-8");
            BufferedWriter bw = new BufferedWriter(w);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            String line = br.readLine();
            if (line != null && !"".equals(line)) {
                String [] parts = line.split(" ");
                if (parts.length >= 3) {
                    this.fileName = parts[1];
                    System.out.println(this.fileName);
                    if (this.fileName.equals("/quit")) {
                        quit(bw);
                    } else {
                        String path = "D:\\Bit\\Front end\\8 js chess";                        
                        this.f = new File(path + this.fileName);                        
                        if (this.f.exists()) {
                            if (this.f.isDirectory()) {
                                listFiles(bw);
                            } else {
                                sendResponse(bw);
                            }
                        } else {
                            write404(bw);
                        }
                    }
                    bw.flush();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                this.sc.close();
            } catch (Exception ex){
                //ignored
            }
            
        }
    }
    
    public void write404(BufferedWriter bw) throws IOException {
        bw.write("HTTP/1.1 404 File not found");
        bw.newLine();
        bw.newLine();
    }
    
    public void quit(BufferedWriter bw) throws IOException {
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
        Server.work = false;
    }
    
    public void listFiles(BufferedWriter bw) throws IOException {
        bw.write("HTTP/1.1 200 OK");
        bw.newLine();
        bw.write("Content-Type: text/html; charset=UTF-8");
        bw.newLine();
        bw.newLine();
        File [] files;
        files = this.f.listFiles();
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
    }
    
    public void sendResponse(BufferedWriter bw) throws IOException {
        bw.write("HTTP/1.1 200 OK");
        bw.newLine();
        if (this.fileName.endsWith(".html")) {
            bw.write("Content-Type: text/html");
            bw.newLine();
        } else if (this.fileName.endsWith(".js")) {
            bw.write("Content-Type: text/javascript;charset=UTF-8");
            bw.newLine();
        } else if (this.fileName.endsWith(".css")) {
            bw.write("Content-Type: text/css");
            bw.newLine();
        } else if (this.fileName.endsWith(".txt")) {
            bw.write("Content-Type: text/plain;charset=UTF-8");
            bw.newLine();
        }
        bw.newLine();
        try (
            FileInputStream fis = new FileInputStream(this.f);
            Reader fr = new InputStreamReader(fis, "UTF-8");
            BufferedReader fbr = new BufferedReader(fr);
            ) {
            String fileLine;
            while ((fileLine = fbr.readLine()) != null) {
                bw.write(fileLine);
                bw.newLine();
            }
        }
    }
}
