package com.arittek.demo.controller;

import com.arittek.demo.model.Developer;
import com.arittek.demo.model.Skill;
import com.arittek.demo.repository.DeveloperRepository;
import com.arittek.demo.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DevelopersController {

    @Autowired
    DeveloperRepository repository;

    @Autowired
    SkillRepository skillRepository;


    @RequestMapping("/developer/{id}")
    public String developer(@PathVariable Long id, Model model) {

        model.addAttribute("developer", repository.findById(id));
        model.addAttribute("skills", skillRepository.findAll());
        return "developer/developer";

    }

    @RequestMapping(value = "/developers", method = RequestMethod.GET)
    public String developersList(Model model) {

        model.addAttribute("developers", repository.findAll());
        return "developer/developers";
    }

    @RequestMapping(value = "/developers", method = RequestMethod.POST)
    public String developersAdd(@RequestParam String email,
                                @RequestParam String firstName, @RequestParam String lastName, Model model) {
        Developer newDeveloper = new Developer();
        newDeveloper.setEmail(email);
        newDeveloper.setFirstName(firstName);
        newDeveloper.setLastName(lastName);
        repository.save(newDeveloper);

        model.addAttribute("developer", newDeveloper);
        model.addAttribute("skills", skillRepository.findAll());
        return "redirect:/developer/" + newDeveloper.getId();
    }

    @RequestMapping(value = "/developer/{id}/skills", method = RequestMethod.POST)
    public String developersAddSkill(@PathVariable Long id, @RequestParam Long skillId, Model model) {

        Skill skill = skillRepository.findById(skillId).orElse(null);
        Developer developer = repository.findById(id).orElse(null);

        if (developer != null) {

            if (!developer.hasSkill(skill)) {
                developer.getSkills().add(skill);
            }
            repository.save(developer);
            model.addAttribute("developer", repository.findById(id));
            model.addAttribute("skills", skillRepository.findAll());
            return "redirect:/developer/" + developer.getId();
        }

        model.addAttribute("developers", repository.findAll());
        return "redirect:/developers";
    }

}
