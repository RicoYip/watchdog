package watcher.controller;


import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import watcher.vo.CommonPacket;

import javax.servlet.http.HttpServletRequest;


@CrossOrigin
@RestController
@RequestMapping("/receive")
public class ReceiveClientMsgController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String test(){
        System.out.println("hi");
        return "hi";
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public void get(@RequestBody CommonPacket packet, HttpServletRequest request){
        redisTemplate.opsForList().leftPush("packet", JSON.toJSONString(packet));
    }


    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public String get(){
        return JSON.toJSONString(redisTemplate.opsForList().rightPop("packet"));
    }


    /**
     * 获取网卡速率
     * @param num 速度
     */
    @RequestMapping(value = "/packetspeed/{num}",method = RequestMethod.GET)
    public void packetspeed(@PathVariable Integer num){
        redisTemplate.opsForValue().set("packetspeed",num);
    }
}
