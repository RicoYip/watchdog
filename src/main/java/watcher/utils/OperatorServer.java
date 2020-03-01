package watcher.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class OperatorServer {

    //流对象
    public static OutputStream outputStream;
    public static InputStream inputStream;

    @Autowired
    private  RedisTemplate redisTemplate;


    public OperatorServer(){
        startService();
    }

    public  void startService(){
        new Thread(()->{
            try {
                ServerSocket serverSocket = new ServerSocket(7878);
                Socket accept = serverSocket.accept();
                outputStream = new BufferedOutputStream(accept.getOutputStream());
                inputStream = new BufferedInputStream(accept.getInputStream());
                //挥手
                out("server ready...");
                //一直读取
                while (true) {
                    //读出数据,写入缓存
                    redisTemplate.opsForList().leftPush("result",read());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public  void out(String data) throws IOException {
        if(null == outputStream){
            throw new RuntimeException("流未建立");
        }
        byte[] _data = data.getBytes();
        outputStream.write(MyUtils.intToByteArray(_data.length));
        outputStream.write(_data);
        outputStream.flush();
    }

    public  String read() throws IOException {
        if(null == inputStream){
            throw new RuntimeException("流未建立");
        }
        byte[] length = new byte[4];
        inputStream.read(length);
        byte[] data = new byte[MyUtils.byteArrayToInt(length)];
        inputStream.read(data);

        return new String(data);
    }

}
