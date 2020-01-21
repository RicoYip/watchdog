package watcher.controller;


import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import watcher.vo.Result;

@CrossOrigin
@RestController
@RequestMapping("/send")
public class SendClientMsgController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/packetspeed",method = RequestMethod.GET)
    public String get(){
        Result result = new Result();
        result.setMsg("packetspeed");
        result.setData((Integer)redisTemplate.opsForValue().get("packetspeed"));
        return JSON.toJSONString(result);
    }

}
