package com.example.monoweek8exercice.Controller;

import com.example.monoweek8exercice.DTO.Age;
import com.example.monoweek8exercice.DTO.Gender;
import com.example.monoweek8exercice.DTO.NameResponse;
import com.example.monoweek8exercice.DTO.Nation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin
public class NameController {

    @RequestMapping("/probability")
    public NameResponse getDetails(@RequestParam String name) {
        Mono<Age> age = getAge(name);
        Mono<Nation> nation = getNation(name);
        Mono<Gender> gender = getGender(name);

        var information = Mono.zip(age,nation,gender).map(person -> {
            NameResponse nr = new NameResponse();

            nr.setAge(person.getT1().getAge());
            nr.setAgeCount(person.getT1().getCount());

            nr.setCountry(person.getT2().getCountry().get(0).getCountry_id());
            nr.setCountryProbability(person.getT2().getCountry().get(0).getProbability());

            nr.setGender(person.getT3().getGender());
            nr.setGenderProbability(person.getT3().getProbability());
            return nr;
        });

        NameResponse res = information.block();
        res.setName(name);
        return res;

        //d
    }

    private Mono<Age> getAge(String name) {
        Mono<Age> slowResponse = WebClient.create()
                .get()
                .uri("https://api.agify.io/?name=" + name)
                .retrieve()
                .bodyToMono(Age.class)
                .doOnError(e-> System.out.println("UUUps : " + e.getMessage()));
        return slowResponse;
    }

    private Mono<Nation> getNation(String name) {
        Mono<Nation> slowResponse = WebClient.create()
                .get()
                .uri("https://api.nationalize.io/?name=" + name)
                .retrieve()
                .bodyToMono(Nation.class)
                .doOnError(e -> System.out.println("UUUPS : " + e.getMessage()));
        return slowResponse;
    }

    private Mono<Gender> getGender(String name){
        Mono<Gender> slowResponse = WebClient.create()
                .get()
                .uri("https://api.genderize.io/?name=" + name)
                .retrieve()
                .bodyToMono(Gender.class)
                .doOnError(e-> System.out.println("UUUPS : "+e.getMessage()));
        return slowResponse;
    }
}
