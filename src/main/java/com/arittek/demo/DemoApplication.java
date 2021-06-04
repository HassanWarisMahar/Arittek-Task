package com.arittek.demo;

import com.arittek.demo.model.Developer;
import com.arittek.demo.model.Skill;
import com.arittek.demo.repository.DeveloperRepository;
import com.arittek.demo.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    @Autowired
    DeveloperRepository developerRepository;

    @Autowired
    SkillRepository skillRepository;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Skill javascript = new Skill("javascript", "Javascript language skill");
        Skill ruby = new Skill("ruby", "Ruby language skill");
        Skill emberjs = new Skill("emberjs", "Emberjs framework");
        Skill angularjs = new Skill("angularjs", "Angularjs framework");

        skillRepository.save(javascript);
        skillRepository.save(ruby);
        skillRepository.save(emberjs);
        skillRepository.save(angularjs);


        List<Developer> developers = new LinkedList<Developer>();
        developers.add(new Developer("Mueed", "Hameerani", "mueed.hameerani@gmail.com",
                Arrays.asList(new Skill[]{javascript, ruby})));
        developers.add(new Developer("Hassan", "Waris", "hassanwaris36@gmail.com",
                Arrays.asList(new Skill[]{emberjs, ruby})));
        developers.add(new Developer("Kirat", "Kumar", "kirat@gmail.com",
                Arrays.asList(new Skill[]{angularjs, ruby})));
        developers.add(new Developer("Sakina", "Gohar", "Sakina@gmail.com",
                Arrays.asList(new Skill[]{emberjs, angularjs, javascript})));
        developers.add(new Developer("Sindho", "Aminabadi", "sindhu@aminabadi.com",
                Arrays.asList(new Skill[]{emberjs})));


        developerRepository.saveAll(developers);
    }


}
