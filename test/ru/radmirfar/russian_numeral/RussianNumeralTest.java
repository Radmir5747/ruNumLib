package ru.radmirfar.russian_numeral;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RussianNumeralTest {

    @Test
    @Disabled("Not implemented")
    @DisplayName("Порядковые числительные")
    void getOrdinalNumeral() {

    }
    @Test
    @DisplayName("Целые количественные числительные")
    void getCardinalNumeral() {
        System.out.println("Для тысячи, миллиона, миллиарда должно быть грамматическое число, иначе " +
                "должен выдать IllegalArgumentException");
        Declension d1 = new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null);
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getNumeral(1000, d1)),
                () -> assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getNumeral((int)10e5, d1)),
                () -> assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getNumeral((int)10e8, d1)));
        System.out.println("Тысяча, миллион, миллиард");
        String[][][] strings1 = {{{"тысяча", "тысячи", "тысяче", "тысячу", "тысячей", "тысяче"},
                {"тысячи", "тысяч", "тысячам", "тысячи", "тысячами", "тысячах"}},
        {{"миллион", "миллиона", "миллиону", "миллион", "миллионом", "миллионе"},
                {"миллионы", "миллионов", "миллионам", "миллионы", "миллионами", "миллионах"}},
        {{"миллиард", "миллиарда", "миллиарду", "миллиард", "миллиардом", "миллиарде"},
                {"миллиарды", "миллиардов", "миллиардам", "миллиарды", "миллиардами", "миллиардах"}}};
        int[] bigNums = new int[]{1000, (int) 10e5, (int) 10e8};
        for (int i = 0; i < bigNums.length; i++) {
            for (Case c : Case.values()) {
                for (Count cnt : Count.values()) {
                    assertEquals(strings1[i][cnt.ordinal()][c.ordinal()], RussianNumeral.getNumeral(bigNums[i],
                            new Declension(null, c, cnt, Type.CARDINAL, null)));
                }
            }
        }
        System.out.println("Формы числительного ноль");
        String[] nullForms = {"ноль", "ноля", "нолю", "ноль", "нолём", "ноле"};
        for (Case c : Case.values()) {
            assertEquals(nullForms[c.ordinal()], RussianNumeral.getNumeral(0,
                    new Declension(null, c, null, Type.CARDINAL, null)));
        }
        System.out.println("Exception'ы для числительного один");
        System.out.println("Отсутствует всё, должен выдать IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getNumeral(1, d1));
        /* в будущем это надо будет поправить */
        System.out.println("Отсутствует род, должен выдать IllegalArgumentException");
        Declension d2 = new Declension(null, Case.GENITIVE, Count.SINGULAR, Type.CARDINAL, null);
        Declension d3 = new Declension(null, Case.GENITIVE, Count.PLURAL, Type.CARDINAL, null);
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getNumeral(1, d2)),
                () -> assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getNumeral(1, d3)));
        System.out.println("Отсутствует грамматическое число, должен выдать IllegalArgumentException");
        Declension d4 = new Declension(Gender.MASCULINE, Case.NOMINATIVE, null, Type.CARDINAL, null);
        assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getNumeral(1, d4));
        System.out.println("Именительный падеж для мужского и среднего рода");
        assertAll(() -> assertEquals("один", RussianNumeral.getNumeral(1,
                new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.SINGULAR, Type.CARDINAL, null))),
                () -> assertEquals("одно", RussianNumeral.getNumeral(1,
                        new Declension(Gender.NEUTRAL, Case.NOMINATIVE, Count.SINGULAR, Type.CARDINAL, null))));
        System.out.println("Формы винительного падежа должны отличаться в зависимости от одушевлённости");
        for (Count cnt : Count.values()) {
            String result1 = RussianNumeral.getNumeral(1,
                    new Declension(Gender.MASCULINE, Case.ACCUSATIVE, cnt, Type.CARDINAL, Animacy.INANIMATE));
            String result2 = RussianNumeral.getNumeral(1,
                    new Declension(Gender.MASCULINE, Case.ACCUSATIVE, cnt, Type.CARDINAL, Animacy.ANIMATE));
            String expected1 = RussianNumeral.getNumeral(1,
                    new Declension(Gender.MASCULINE, Case.NOMINATIVE, cnt, Type.CARDINAL, null));
            String expected2 = RussianNumeral.getNumeral(1,
                    new Declension(Gender.MASCULINE, Case.GENITIVE, cnt, Type.CARDINAL, null));
            assertAll(() -> assertEquals(expected1, result1), () -> assertEquals(expected2, result2));
        }
        System.out.println("Для числительного два должен быть род, иначе должен выдать IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getNumeral(2, d2));
        System.out.println("Именительный падеж для мужского и женского рода числительного два");
        assertAll(() -> assertEquals("два", RussianNumeral.getNumeral(2,
                        new Declension(Gender.MASCULINE, Case.NOMINATIVE, null, Type.CARDINAL, null))),
                () -> assertEquals("две", RussianNumeral.getNumeral(2,
                        new Declension(Gender.FEMININE, Case.NOMINATIVE, null, Type.CARDINAL, null))));
        System.out.println("Три, четыре");
        assertAll(() -> assertEquals("три", RussianNumeral.getNumeral(3,
                        new Declension(Gender.MASCULINE, Case.NOMINATIVE, null, Type.CARDINAL, null))),
                () -> assertEquals("четыре", RussianNumeral.getNumeral(4,
                        new Declension(Gender.MASCULINE, Case.NOMINATIVE, null, Type.CARDINAL, null))));
        System.out.println("Формы винительного падежа числительных два-четыре должны отличаться " +
                "в зависимости от одушевлённости");
        for (int i = 2; i < 5; i++) {
            for (Gender g : Gender.values()) {
                String result1 = RussianNumeral.getNumeral(i,
                        new Declension(g, Case.ACCUSATIVE, null, Type.CARDINAL, Animacy.INANIMATE));
                String result2 = RussianNumeral.getNumeral(i,
                        new Declension(g, Case.ACCUSATIVE, null, Type.CARDINAL, Animacy.ANIMATE));
                String expected1 = RussianNumeral.getNumeral(i,
                        new Declension(g, Case.NOMINATIVE, null, Type.CARDINAL, null));
                String expected2 = RussianNumeral.getNumeral(i,
                        new Declension(g, Case.GENITIVE, null, Type.CARDINAL, null));
                assertAll(() -> assertEquals(expected1, result1), () -> assertEquals(expected2, result2));
            }
        }
        // to be continued
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