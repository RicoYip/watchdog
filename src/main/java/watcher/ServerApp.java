package watcher;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sun.nio.ch.IOUtil;
import watcher.utils.MyUtils;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Time;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


@SpringBootApplication
public class ServerApp {
    public static void main(String[] args) throws Exception {
        //监控客户端输入
        File file = new File("D:/pV.jpg");
        if (!file.exists()) file.createNewFile();
        new Thread(()->{
            try {
                ServerSocket socket = new ServerSocket(7979);
                Socket accept = socket.accept();
                System.out.println("wating....");
                BufferedInputStream br = new BufferedInputStream(accept.getInputStream());
                //协议：图片长度+图片数据
                int i = 0;
                while (i != -1) {
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buff = new byte[4];//长度
                    i = br.read(buff, 0, 4);//读取长度
                    int length = MyUtils.byteArrayToInt(buff);
                    byte[] buffPic = new byte[length];//数据
                    br.read(buffPic);//读入数据
//                int len = 0;
//                while ((len = br.read())!=-1) {
//                    System.out.println(len);
//                }

                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffPic);
                    IOUtils.copy(byteArrayInputStream, fos);
                    fos.close();
                    TimeUnit.SECONDS.sleep(3);
                    System.out.println("next...");
                    file.delete();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
//        //发送数据给客户端
//        new Thread(()->{
//
//            try {
//                Socket socket = new Socket("localhost",7878);
//                OutputStream outputStream = socket.getOutputStream();
//                IOUtils.copy(fis, outputStream);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//        }).start();
        SpringApplication.run(ServerApp.class).start();
    }
}
