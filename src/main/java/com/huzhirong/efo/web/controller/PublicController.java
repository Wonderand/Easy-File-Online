package com.huzhirong.efo.web.controller;

import com.huzhirong.efo.util.MailUtils;
import org.apache.ibatis.annotations.Param;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/public")
public class PublicController {

//    @Autowired
//    private IPublicService service;

    @Autowired
    private MailUtils mail;

    @Autowired
    private RedissonClient redissonClient;

    @PostMapping("/ip")
    public String getIpAddr(HttpServletRequest request){
        String ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress= inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
    @PostMapping("/sendemail")
    public String getEmailCode(@Param("email") String email){
        //生成随机6位数验证码
//        String mail1 = service.getMail(email);
//        System.out.println("");
//        if (mail1==null)
//            return "邮件不正确";
        String s = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        RMap<String,String> emailCode = redissonClient.getMap("emailCode");
        emailCode.put(email,s);
        System.out.println(emailCode.get(email));
        mail.sendMail(email,"您的验证码是:\n\r"+s+"\n请勿泄露!","EFO验证码");

        return "success";
    }
}
