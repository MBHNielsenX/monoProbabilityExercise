package com.example.monoweek8exercice.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NameResponse {
    public String name;
    public String gender;
    public double genderProbability;
    public int age;
    public int ageCount;
    public String country;
    public double countryProbability;

    public void setGenderProbability(double p) {
        genderProbability = p * 100;
    }

    public void setCountryProbability(double p) {
        countryProbability = p * 100;
    }
}
