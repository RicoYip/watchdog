package watcher.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import watcher.utils.MyUtils;
import watcher.vo.CommonPacket;
import watcher.vo.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@CrossOrigin
@RestController
@RequestMapping("/network")
public class NetworkController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 保存数据包
     *
     * @param packet
     * @param request
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void get(@RequestBody CommonPacket packet, HttpServletRequest request) {
        redisTemplate.opsForList().leftPush("packet", JSON.toJSONString(packet));
    }


    /**
     * 提取数据包
     *
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String get() {
        return JSON.toJSONString(redisTemplate.opsForList().rightPop("package"));
    }

    /**
     * 存入网卡速率
     *
     * @param num 速度
     */
    @RequestMapping(value = "/savespeed/{num}", method = RequestMethod.GET)
    public void packetspeed(@PathVariable Integer num) {
        redisTemplate.opsForValue().set("packetspeed", num);
    }


    /**
     * 获取网卡速度
     *
     * @return
     */
    @RequestMapping(value = "/getspeed", method = RequestMethod.GET)
    public String getSpeed() {
        Result result = new Result();
        result.setMsg("packetspeed");
        result.setData(Integer.parseInt((String) redisTemplate.opsForValue().get("packetspeed")));
        return JSON.toJSONString(result);
    }

    /**
     * 高级查询数据包内同
     */
    @RequestMapping("/findByCd")
    public String getPackageByCondition(@RequestBody Map<String, String> map) {
        List<String> range = redisTemplate.opsForList().range("package", 0, -1);
        List<String> res = new ArrayList<>();

        //出去空白属性
        map = MyUtils.removeEmptyAttr(map);

        for (String json : range) {
            Map _data = JSON.parseObject((String) JSON.parseObject(json).get("data"), Map.class);
            _data.put("type", JSON.parseObject(json).get("type"));
            int count = 0;
            for (String k : map.keySet()) {//每个属性进行对比
                if (null != _data.get(k)) {
                    if (String.valueOf(_data.get(k)).equals(map.get(k))) {
                        count++;
                    }
                }else{
                    break;
                }

            }
            if(map.keySet().size() == count)
                res.add(JSON.toJSONString(_data));
            count = 0;
        }
        return JSON.toJSONString(res);
    }
}
