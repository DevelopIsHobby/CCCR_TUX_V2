package com.machbase.tag;

import com.machbase.tag.domain.Tag;
import com.machbase.tag.domain.TagPK;
import com.machbase.tag.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Timestamp;
import java.util.*;

@SpringBootApplication
public class TagtableApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(TagtableApplication.class);

    private final TagRepository repository;

    public TagtableApplication(TagRepository repository) {
        this.repository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(TagtableApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("TestTagApplication...");
        ArrayList<Tag> tagList = new ArrayList<Tag>();

        repository.save(new Tag(new TagPK("Nana", TagPK.get_arrival_time()), 17));

        tagList.add(new Tag(new TagPK("Noah", TagPK.get_arrival_time()), 1));
        tagList.add(new Tag(new TagPK("John", TagPK.get_arrival_time()), 2));
        tagList.add(new Tag(new TagPK("John", TagPK.get_arrival_time()), 3));
        tagList.add(new Tag(new TagPK("John", TagPK.get_arrival_time()), 4));
        tagList.add(new Tag(new TagPK("John", TagPK.get_arrival_time()), 5));
        repository.saveAll(tagList);

        System.out.println("\nfindAll()");
        repository.findAll().forEach(System.out::println);

        System.out.println("\nfindTagsByName(\"John\")");
        repository.findTagsByName("John").forEach(System.out::println);

        System.gc();
        System.exit(0);
    }
}
