package com.machbase.lookup;

import com.machbase.lookup.domain.Lookup;
import com.machbase.lookup.repository.LookupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class LookupApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(LookupApplication.class);

    private final LookupRepository repository;

    public LookupApplication(LookupRepository repository) {
        this.repository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(LookupApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("TestMachbaseApplication...");
        ArrayList<Lookup> lookupList = new ArrayList<Lookup>();

        lookupList.add(new Lookup(1L, "Noah", Lookup.get_arrival_time()));
        lookupList.add(new Lookup(2L, "John", Lookup.get_arrival_time()));
        repository.saveAll(lookupList);

        repository.save(new Lookup(3L, "Nana", Lookup.get_arrival_time()));

        System.out.println("\nfindAll()");
        repository.findAll().forEach(System.out::println);

        System.out.println("\nfindById(1)");
        repository.findById(1L).ifPresent(x -> System.out.println(x.getName()));

        System.out.println("\nfindByName(\"Noah\")");
        System.out.println(repository.findByName("Noah").toString());

        System.gc();
        System.exit(0);
    }
}
