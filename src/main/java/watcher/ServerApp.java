package watcher;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sun.nio.ch.IOUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


@SpringBootApplication
public class ServerApp {
    public static void main(String[] args) throws FileNotFoundException {
        //开启新线程开启socket监控服务端图片输入
        File file = new File("I:/p.jpg");
        FileOutputStream fos = new FileOutputStream(file);
        new Thread(()->{
            try {
                ServerSocket socket = new ServerSocket(7979);
                while (true) {
                    System.out.println("wating....");
                    Socket accept = socket.accept();
                    InputStream inputStream = accept.getInputStream();
                    IOUtils.copy(inputStream, fos);
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        SpringApplication.run(ServerApp.class).start();
    }
}
