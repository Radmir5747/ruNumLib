package ru.radmirfar.russian_numeral;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RussianNumeralTest {

    @Test
    @Disabled("Not implemented")
    void getNumeral() {
    }

    @Test
    @DisplayName("Собирательные числительные")
    void getCollectiveNumeral() {
        Declension nomDeclension = new Declension(null, Case.NOMINATIVE, null, null, null);
        System.out.println("Если число меньше 2 или больше 10, должен выдать IllegalArgumentException");
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getCollectiveNumeral(0, nomDeclension)),
                () -> assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getCollectiveNumeral(12, nomDeclension)));
        System.out.println("Без одушевлённости в винительном падеже должен выдать IllegalArgumentException");
        Declension errDeclension = new Declension(null, Case.ACCUSATIVE, null, null, null);
        assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getCollectiveNumeral(2, errDeclension));
        String[] strings = {"двое", "трое", "четверо", "пятеро", "шестеро", "семеро", "восьмеро", "девятеро", "десятеро"};
        System.out.println("Собирательные числительные от 2 до 10 в именительном падеже");
        for (int i = 2; i < 11; i++) {
            assertEquals(strings[i - 2], RussianNumeral.getCollectiveNumeral(i, nomDeclension));
        }
        System.out.println("Формы винительного падежа должны отличаться в зависимости от одушевлённости");
        Declension accInanDeclension = new Declension(null, Case.ACCUSATIVE, null, null, Animacy.INANIMATE);
        Declension accAnDeclension = new Declension(null, Case.ACCUSATIVE, null, null, Animacy.ANIMATE);
        Declension genDeclension = new Declension(null, Case.GENITIVE, null, null, null);
        for (int i = 2; i < 11; i++) {
            String result1 = RussianNumeral.getCollectiveNumeral(i, accInanDeclension);
            String result2 = RussianNumeral.getCollectiveNumeral(i, accAnDeclension);
            String expected1 = RussianNumeral.getCollectiveNumeral(i, nomDeclension);
            String expected2 = RussianNumeral.getCollectiveNumeral(i, genDeclension);
            assertAll(() -> assertEquals(expected1, result1), () -> assertEquals(expected2, result2));
        }
    }

    @Test
    @DisplayName("Оба / обе")
    void getBoth() {
        Declension withoutGender = new Declension(null, Case.GENITIVE, null, null, null);
        System.out.println("Без рода должен выдать IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getBoth(withoutGender));
        Declension withoutAnimacy = new Declension(Gender.FEMININE, Case.ACCUSATIVE, null, null, null);
        System.out.println("Без одушевлённости в винительном падеже должен выдать IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getBoth(withoutAnimacy));
        System.out.println("Числительные мужского и среднего рода отличаются от числительных женского рода");
        String[][] strings = {{"оба", "обоих", "обоим", "", "обоими", "обоих"},
                {"обе", "обеих", "обеим", "", "обеими", "обеих"}};
        for (Case c : Case.values()) {
            for (Gender g : Gender.values()) {
                if (c == Case.ACCUSATIVE) continue;
                int i = g == Gender.FEMININE ? 1 : 0;
                String result = RussianNumeral.getBoth(new Declension(g, c, null, null, null));
                assertEquals(strings[i][c.ordinal()], result);
            }
        }
        System.out.println("Формы винительного падежа должны отличаться в зависимости от одушевлённости");
        for (Gender g : Gender.values()) {
            String result1 = RussianNumeral.getBoth(new Declension(g, Case.ACCUSATIVE, null, null, Animacy.INANIMATE));
            String result2 = RussianNumeral.getBoth(new Declension(g, Case.ACCUSATIVE, null, null, Animacy.ANIMATE));
            String expected1 = RussianNumeral.getBoth(new Declension(g, Case.NOMINATIVE, null, null, null));
            String expected2 = RussianNumeral.getBoth(new Declension(g, Case.GENITIVE, null, null, null));
            assertAll(() -> assertEquals(expected1, result1), () -> assertEquals(expected2, result2));
        }
    }

    @Test
    @DisplayName("Полтора / полторы / полутора")
    void getOneAndAHalf() {
        Declension femNomDeclension = new Declension(Gender.FEMININE, Case.NOMINATIVE, null, null, null);
        Declension mascNomDeclension = new Declension(Gender.MASCULINE, Case.NOMINATIVE, null, null, null);
        Declension neuNomDeclension = new Declension(Gender.NEUTRAL, Case.NOMINATIVE, null, null, null);
        Declension baseDeclension = new Declension(null, Case.INSTRUMENTAL, null, null, null);
        System.out.println("В женском роде для именительного падежа - полторы");
        assertEquals("полторы", RussianNumeral.getOneAndAHalf(femNomDeclension));
        System.out.println("В мужском и среднем роде для именительного падежа - полторы");
        assertEquals("полтора", RussianNumeral.getOneAndAHalf(mascNomDeclension));
        assertEquals("полтора", RussianNumeral.getOneAndAHalf(neuNomDeclension));
        System.out.println("Для косвенных падежей - полторы");
        assertEquals("полутора", RussianNumeral.getOneAndAHalf(baseDeclension));
        Declension errorDeclension = new Declension(null, Case.ACCUSATIVE, null, null, null);
        System.out.println("Для винительного падежа без рода должен выдать IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getOneAndAHalf(errorDeclension));
    }

    @Test
    @DisplayName("Полтораста / полутораста")
    void get150() {
        Declension baseDeclension = new Declension(null, Case.INSTRUMENTAL, null, null, null);
        Declension baseNomDeclension = new Declension(null, Case.NOMINATIVE, null, null, null);
        assertEquals("полутораста", RussianNumeral.get150(baseDeclension));
        assertEquals("полтораста", RussianNumeral.get150(baseNomDeclension));
    }
}