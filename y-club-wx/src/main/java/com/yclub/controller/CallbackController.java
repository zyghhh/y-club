package com.yclub.controller;

import com.yclub.handler.MsgTypeFactory;
import com.yclub.handler.WeChatMsgHandler;
import com.yclub.redis.RedisUtil;
import com.yclub.utils.MessageUtil;
import com.yclub.utils.SHA1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-17  15:10
 */
@RestController
@Slf4j
public class CallbackController {

    private static final String token = "admintest";
    private static final String SEPARATOR = ".";

    @Resource
    private RedisUtil redisUtil;

    @Resource
    MsgTypeFactory msgTypeFactory;


    @RequestMapping("/test")
    public String test(){
        redisUtil.set("test","testVal");
        return "test";
    }

    @GetMapping("/callback")
    public String callback(@RequestParam("signature")String signature,
                           @RequestParam("timestamp")String timestamp,
                           @RequestParam("nonce")String nonce,
                           @RequestParam("echostr")String echostr){
        log.info("get验签请求参数：signature:{}，timestamp:{}，nonce:{}，echostr:{}",
                signature, timestamp, nonce, echostr);
        String shaStr = SHA1.getSHA1(token, timestamp, nonce, "");
        if (signature.equals(shaStr)) {
            return echostr;
        }
        return "unknown";
    }

    @PostMapping(value = "callback", produces = "application/xml;charset=UTF-8")
    public String callback(
            @RequestBody String requestBody,
            @RequestParam("signature") String signature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam(value = "msg_signature", required = false) String msgSignature){
        log.info("接收到微信消息：requestBody：\n{}", requestBody);
        Map<String, String> messageMap = MessageUtil.parseXml(requestBody);
        String msgType = messageMap.get("MsgType");
        String event = messageMap.get("Event") == null ? "" : messageMap.get("Event");
        log.info("msgType:{},event:{}", msgType, event);

        StringBuilder sb = new StringBuilder();
        sb.append(msgType);
        if(!StringUtils.isEmpty(event)){
            sb.append(SEPARATOR);
            sb.append(event);
        }
        WeChatMsgHandler handler = msgTypeFactory.getHandler(sb.toString());
        if(Objects.isNull(handler)){
            return "unknown";
        }
        String content = handler.dealMsg(messageMap);
        return content;
    }

}
