//package com.chattest.config;
//
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.PreDestroy;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import redis.embedded.RedisServer;
//
//import java.io.IOException;
//import java.net.ServerSocket;
//
//@Profile("local")
//@Configuration
//public class EmbeddedRedisConfig {
//    @Value("${spring.data.redis.port}")
//    private int redisPort;
//
//    private RedisServer redisServer;
//
//    @PostConstruct
//    public void redisServer() {
//        // 포트가 사용 중이면 다른 포트로 변경
//        if (!isPortAvailable(redisPort)) {
//            redisPort = 6380;
//        }
//        redisServer = RedisServer.builder()
//                        .port(redisPort)
//                                        .build();
//        redisServer.start();
//    }
//
//    @PreDestroy
//    public void stopRedis() {
//        if(redisServer != null) {
//            redisServer.stop();
//        }
//    }
//
//    private boolean isPortAvailable(int port) {
//        try (ServerSocket serverSocket = new ServerSocket(port)) {
//            return true;
//        } catch (IOException e) {
//            return false;
//        }
//    }
//}
//
