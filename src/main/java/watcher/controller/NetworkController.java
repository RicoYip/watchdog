package watcher.controller;


import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import watcher.vo.CommonPacket;
import watcher.vo.Result;

import javax.servlet.http.HttpServletRequest;


@CrossOrigin
@RestController
@RequestMapping("/network")
public class NetworkController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 保存数据包
     * @param packet
     * @param request
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public void get(@RequestBody CommonPacket packet, HttpServletRequest request){
        redisTemplate.opsForList().leftPush("packet", JSON.toJSONString(packet));
    }


    /**
     * 提取数据包
     * @return
     */
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public String get(){
        return JSON.toJSONString(redisTemplate.opsForList().rightPop("package"));
    }

    /**
     * 存入网卡速率
     * @param num 速度
     */
    @RequestMapping(value = "/savespeed/{num}",method = RequestMethod.GET)
    public void packetspeed(@PathVariable Integer num){
        redisTemplate.opsForValue().set("packetspeed",num);
    }


    /**
     * 获取网卡速度
     * @return
     */
    @RequestMapping(value = "/getspeed",method = RequestMethod.GET)
    public String getSpeed(){
        Result result = new Result();
        result.setMsg("packetspeed");
        result.setData(Integer.parseInt((String)redisTemplate.opsForValue().get("packetspeed")));
        return JSON.toJSONString(result);
    }
}
