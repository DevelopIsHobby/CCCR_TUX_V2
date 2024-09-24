package com.machbase.log;

import com.machbase.log.domain.User;
import com.machbase.log.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Timestamp;
import java.util.*;

@SpringBootApplication
public class LogApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(LogApplication.class);

    private final UserRepository repository;

    public LogApplication(UserRepository repository) {
        this.repository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(LogApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("TestLogApplication...");
        ArrayList<User> userList = new ArrayList<User>();
        Timestamp time;

        repository.save(new User(User.get_arrival_time(), "Nana", 17));

        userList.add(new User(User.get_arrival_time(), "Noah", 28));
        userList.add(new User(User.get_arrival_time(), "John", 31));
        userList.add(new User(User.get_arrival_time(), "John", 1));
        userList.add(new User(User.get_arrival_time(), "John", 2));
        userList.add(new User(User.get_arrival_time(), "John", 3));
        userList.add(new User(User.get_arrival_time(), "John", 4));
        userList.add(new User(User.get_arrival_time(), "John", 5));
        repository.saveAll(userList);

        System.out.println("\nfindAll()");
        repository.findAll().forEach(System.out::println);
        // that lambda formula can be replaced with `System.out::println`

        System.out.println("\nfindByName('Noah')");
        System.out.println(repository.findByName("Noah").toString());

        System.gc();
        System.exit(0);
    }
}
