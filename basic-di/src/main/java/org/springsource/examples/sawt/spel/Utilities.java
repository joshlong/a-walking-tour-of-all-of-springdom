package org.springsource.examples.sawt.spel;

import org.springframework.stereotype.Component;

@Component
public class Utilities {
    public char[] buildRomanAlphabetOnThisComputer() {

        char a = 'a';
        char z = 'z';
        int len = 1 + (z - a);
        char[] alphabet = new char[len];

        int ctr = 0;
        for (char c = a; c <= z; c++) {
            alphabet[ctr] = c;
            ctr += 1;
        }
        return alphabet;
    }
}
