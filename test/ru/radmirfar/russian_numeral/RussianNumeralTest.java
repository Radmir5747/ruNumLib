package ru.radmirfar.russian_numeral;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RussianNumeralTest {

    @Test
    @DisplayName("Проверка исключений при отсутствии падежа")
    void checkCaseExceptions() {
        assertThrows(IllegalArgumentException.class,
                () -> new Declension(null, null, null, null, null));
    }

    @Test
    @DisplayName("Прочие проверки")
    void otherChecks() {
        System.out.println("Проверка отрицательных чисел");
        assertAll(() -> assertEquals("минус один", RussianNumeral.getNumeral(-1,
                new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.SINGULAR, Type.CARDINAL, null))),
                () -> assertEquals("минус первый", RussianNumeral.getNumeral(-1,
                        new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null))),
                () -> assertEquals("минус пять седьмых",
                        RussianNumeral.getNumeral(new Fraction(-5, 7),
                                new Declension(null, Case.NOMINATIVE, null, null, null))),
                () -> assertEquals("минус две целых одна пятая",
                        RussianNumeral.getNumeral(new Fraction(-2,1, 5),
                                new Declension(null, Case.NOMINATIVE, null, null, null))));
    }

    @Test
    @DisplayName("Проверка дробей")
    void fractionCheck() {
        Fraction f1 = new Fraction(1, 7); // 1/7
        Fraction f2 = new Fraction(5, 10); // 5/10
        Fraction f3 = new Fraction(1, 2, 5); // 1 2/5
        Fraction f4 = new Fraction(3, 2, 9); // 3 2/9
        String[] test1 = {"одна седьмая", "одной седьмой", "одной седьмой", "одну седьмую", "одной седьмой",
                "одной седьмой"};
        String[] test2 = {"пять десятых", "пяти десятых", "пяти десятым", "пять десятых", "пятью десятыми",
                "пяти десятых"};
        String[] test3 = {"одна целая две пятых", "одной целой двух пятых", "одной целой двум пятым",
                "одну целую две пятых", "одной целой двумя пятыми", "одной целой двух пятых"};
        String[] test4 = {"три целых две девятых", "трёх целых двух девятых", "трём целым двум девятым",
                "три целых две девятых", "тремя целыми двумя девятыми", "трёх целых двух девятых"};
        System.out.println("Простая дробь, 1 в числителе");
        for (Case c : Case.values()) {
            assertEquals(test1[c.ordinal()], RussianNumeral.getNumeral(f1, new DeclensionBuilder(c).build()));
        }
        System.out.println("Простая дробь");
        for (Case c : Case.values()) {
            assertEquals(test2[c.ordinal()], RussianNumeral.getNumeral(f2, new DeclensionBuilder(c).build()));
        }
        System.out.println("Составная дробь, 1 в целой части");
        for (Case c : Case.values()) {
            assertEquals(test3[c.ordinal()], RussianNumeral.getNumeral(f3, new DeclensionBuilder(c).build()));
        }
        System.out.println("Составная дробь");
        for (Case c : Case.values()) {
            assertEquals(test4[c.ordinal()], RussianNumeral.getNumeral(f4, new DeclensionBuilder(c).build()));
        }
        System.out.println("Проверка чисел 11, 21, 101, 111, 121 в числителе");
        assertAll(() -> assertEquals("одиннадцать седьмых",
                RussianNumeral.getNumeral(new Fraction(11 ,7),
                        new DeclensionBuilder(Case.NOMINATIVE).build())),
                () -> assertEquals("двадцать одна седьмая",
                        RussianNumeral.getNumeral(new Fraction(21 ,7),
                                new DeclensionBuilder(Case.NOMINATIVE).build())),
                () -> assertEquals("сто одна седьмая",
                        RussianNumeral.getNumeral(new Fraction(101 ,7),
                                new DeclensionBuilder(Case.NOMINATIVE).build())),
                () -> assertEquals("сто одиннадцать седьмых",
                        RussianNumeral.getNumeral(new Fraction(111 ,7),
                                new DeclensionBuilder(Case.NOMINATIVE).build())),
                () -> assertEquals("сто двадцать одна седьмая",
                        RussianNumeral.getNumeral(new Fraction(121 ,7),
                                new DeclensionBuilder(Case.NOMINATIVE).build())));
    }

    @Test
    @DisplayName("Целые порядковые числительные")
    void getOrdinalNumeral() {
        Declension d1 = new Declension(null, Case.NOMINATIVE, null, Type.ORDINAL, null);
        System.out.println("Отсутствует всё, должен выдать IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getNumeral(1, d1));
        /* в будущем это надо будет поправить */
        System.out.println("Отсутствует род, должен выдать IllegalArgumentException");
        Declension d2 = new Declension(null, Case.GENITIVE, Count.SINGULAR, Type.ORDINAL, null);
        Declension d3 = new Declension(null, Case.GENITIVE, Count.PLURAL, Type.ORDINAL, null);
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getNumeral(1, d2)),
                () -> assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getNumeral(1, d3)));
        System.out.println("Отсутствует грамматическое число, должен выдать IllegalArgumentException");
        Declension d4 = new Declension(Gender.MASCULINE, Case.NOMINATIVE, null, Type.ORDINAL, null);
        assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getNumeral(1, d4));
        System.out.println("Формы числительного третий");
        String[][] third = {{"третий", "третьего", "третьему", "", "третьим", "третьем"},
                {"третья", "третьей", "третьей", "третью", "третьей", "третьей"},
                {"третье", "третьего", "третьему", "третье", "третьим", "третьем"},
                {"третьи", "третьих", "третьим", "", "третьими", "третьих"}};
        for (Case c : Case.values()) {
            assertEquals(third[1][c.ordinal()], RussianNumeral.getNumeral(3,
                    new Declension(Gender.FEMININE, c, Count.SINGULAR, Type.ORDINAL, null)));
            assertEquals(third[2][c.ordinal()], RussianNumeral.getNumeral(3,
                    new Declension(Gender.NEUTER, c, Count.SINGULAR, Type.ORDINAL, null)));
            if (c != Case.ACCUSATIVE) {
                assertEquals(third[0][c.ordinal()], RussianNumeral.getNumeral(3,
                        new Declension(Gender.MASCULINE, c, Count.SINGULAR, Type.ORDINAL, null)));
                assertEquals(third[3][c.ordinal()], RussianNumeral.getNumeral(3,
                        new Declension(Gender.FEMININE, c, Count.PLURAL, Type.ORDINAL, null)));
            }
        }
        System.out.println("Формы винительного падежа должны отличаться в зависимости от одушевлённости");
        for (Count cnt : Count.values()) {
            String result1 = RussianNumeral.getNumeral(3,
                    new Declension(Gender.MASCULINE, Case.ACCUSATIVE, cnt, Type.ORDINAL, Animacy.INANIMATE));
            String result2 = RussianNumeral.getNumeral(3,
                    new Declension(Gender.MASCULINE, Case.ACCUSATIVE, cnt, Type.ORDINAL, Animacy.ANIMATE));
            String expected1 = RussianNumeral.getNumeral(3,
                    new Declension(Gender.MASCULINE, Case.NOMINATIVE, cnt, Type.ORDINAL, null));
            String expected2 = RussianNumeral.getNumeral(3,
                    new Declension(Gender.MASCULINE, Case.GENITIVE, cnt, Type.ORDINAL, null));
            assertAll(() -> assertEquals(expected1, result1), () -> assertEquals(expected2, result2));
        }
        System.out.println("Проверка числительных, оканчивающихся на -ой");
        String[] test2 = {"нулевой", "второй", "шестой", "седьмой", "восьмой", "сороковой"};
        int[] ints = {0, 2, 6, 7, 8, 40};
        for (int i = 0; i < ints.length; i++) {
            assertEquals(test2[i], RussianNumeral.getNumeral(ints[i],
                    new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null)));
        }
        String[] test3 = {"нулевого", "второго", "шестого", "седьмого", "восьмого", "сорокового"};
        for (int i = 0; i < ints.length; i++) {
            assertEquals(test3[i], RussianNumeral.getNumeral(ints[i],
                    new Declension(Gender.MASCULINE, Case.GENITIVE, Count.SINGULAR, Type.ORDINAL, null)));
        }
        System.out.println("Формы винительного падежа должны отличаться в зависимости от одушевлённости");
        for (Count cnt : Count.values()) {
            String result1 = RussianNumeral.getNumeral(40,
                    new Declension(Gender.MASCULINE, Case.ACCUSATIVE, cnt, Type.ORDINAL, Animacy.INANIMATE));
            String result2 = RussianNumeral.getNumeral(40,
                    new Declension(Gender.MASCULINE, Case.ACCUSATIVE, cnt, Type.ORDINAL, Animacy.ANIMATE));
            String expected1 = RussianNumeral.getNumeral(40,
                    new Declension(Gender.MASCULINE, Case.NOMINATIVE, cnt, Type.ORDINAL, null));
            String expected2 = RussianNumeral.getNumeral(40,
                    new Declension(Gender.MASCULINE, Case.GENITIVE, cnt, Type.ORDINAL, null));
            assertAll(() -> assertEquals(expected1, result1), () -> assertEquals(expected2, result2));
        }
        System.out.println("Проверка числительных, оканчивающихся на -ый");
        String[][] test4 = {{"первый", "первого", "первому", "", "первым", "первом"},
                {"четвёртый", "четвёртого", "четвёртому", "", "четвёртым", "четвёртом"},
                {"десятый", "десятого", "десятому", "", "десятым", "десятом"},
                {"девяностый", "девяностого", "девяностому", "", "девяностым", "девяностом"},
                {"сотый", "сотого", "сотому", "", "сотым", "сотом"},
                {"трёхсотый", "трёхсотого", "трёхсотому", "", "трёхсотым", "трёхсотом"},
                {"тысячный", "тысячного", "тысячному", "", "тысячным", "тысячном"},
                {"миллионный", "миллионного", "миллионному", "", "миллионным", "миллионном"},
                {"миллиардный", "миллиардного", "миллиардному", "", "миллиардным", "миллиардном"}
        };
        int[] ints2 = {1, 4, 10, 90, 100, 300, 1000, (int)10e5, (int)10e8};
        for (int i = 0; i < ints2.length; i++) {
            for (Case c : Case.values()) {
                if (c == Case.ACCUSATIVE) continue;
                assertEquals(test4[i][c.ordinal()], RussianNumeral.getNumeral(ints2[i],
                        new Declension(Gender.MASCULINE, c, Count.SINGULAR, Type.ORDINAL, null)));
            }
            for (Count cnt : Count.values()) {
                String result1 = RussianNumeral.getNumeral(ints2[i],
                        new Declension(Gender.MASCULINE, Case.ACCUSATIVE, cnt, Type.ORDINAL, Animacy.INANIMATE));
                String result2 = RussianNumeral.getNumeral(ints2[i],
                        new Declension(Gender.MASCULINE, Case.ACCUSATIVE, cnt, Type.ORDINAL, Animacy.ANIMATE));
                String expected1 = RussianNumeral.getNumeral(ints2[i],
                        new Declension(Gender.MASCULINE, Case.NOMINATIVE, cnt, Type.ORDINAL, null));
                String expected2 = RussianNumeral.getNumeral(ints2[i],
                        new Declension(Gender.MASCULINE, Case.GENITIVE, cnt, Type.ORDINAL, null));
                assertAll(() -> assertEquals(expected1, result1), () -> assertEquals(expected2, result2));
            }
        }
        String[] test5 = {"пятидесятый", "шестидесятый", "семидесятый", "восьмидесятый"};
        for (int i = 5; i < 9; i++) {
            assertEquals(test5[i - 5], RussianNumeral.getNumeral(i * 10,
                    new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null)));
        }
        System.out.println("Составные числительные для двузначных чисел");
        assertAll(() -> assertEquals("двадцать первые", RussianNumeral.getNumeral(21,
                new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.PLURAL, Type.ORDINAL, null))),
                () -> assertEquals("сорок второй", RussianNumeral.getNumeral(42,
                        new Declension(Gender.FEMININE, Case.DATIVE, Count.SINGULAR, Type.ORDINAL, null))),
                () -> assertEquals("тридцать третьем", RussianNumeral.getNumeral(33,
                        new Declension(Gender.NEUTER, Case.PREPOSITIONAL, Count.SINGULAR, Type.ORDINAL, null))),
                () -> assertEquals("семьдесят третьего", RussianNumeral.getNumeral(73,
                        new Declension(Gender.MASCULINE, Case.GENITIVE, Count.SINGULAR, Type.ORDINAL, null))));
        System.out.println("Составные числительные для трёхзначных чисел");
        assertAll(() -> assertEquals("сто двадцать пятая", RussianNumeral.getNumeral(125,
                new Declension(Gender.FEMININE, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null))),
                () -> assertEquals("девятьсот одиннадцатый", RussianNumeral.getNumeral(911,
                        new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null))),
                () -> assertEquals("двести третья", RussianNumeral.getNumeral(203,
                        new Declension(Gender.FEMININE, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null))),
                () -> assertEquals("пятьсот тридцатые", RussianNumeral.getNumeral(530,
                        new Declension(Gender.FEMININE, Case.NOMINATIVE, Count.PLURAL, Type.ORDINAL, null))));
        System.out.println("Составные числительные для чисел с последним классом");
        assertAll(() -> assertEquals("одна тысяча девятьсот сорок первый", RussianNumeral.getNumeral(1941,
                        new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null))),
                () -> assertEquals("две тысячи двадцатому", RussianNumeral.getNumeral(2020,
                        new Declension(Gender.MASCULINE, Case.DATIVE, Count.SINGULAR, Type.ORDINAL, null))),
                () -> assertEquals("тысяча пятисотой", RussianNumeral.getNumeral(1500,
                        new Declension(Gender.FEMININE, Case.PREPOSITIONAL, Count.SINGULAR, Type.ORDINAL, null))),
                () -> assertEquals("двадцать миллионов сто пятьдесят две тысячи трёхсотому",
                        RussianNumeral.getNumeral(20152300,
                                new Declension(Gender.MASCULINE, Case.DATIVE, Count.SINGULAR, Type.ORDINAL, null))),
                () -> assertEquals("один миллион одна тысяча девятисотых",
                        RussianNumeral.getNumeral(1001900,
                                new Declension(Gender.MASCULINE, Case.GENITIVE, Count.PLURAL, Type.ORDINAL, null))));
        System.out.println("Сложные числительные");
        assertAll(() -> assertEquals("десятитысячный", RussianNumeral.getNumeral(10000,
                new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null))),
                () -> assertEquals("одиннадцатитысячный", RussianNumeral.getNumeral(11000,
                        new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null))),
                () -> assertEquals("девятнадцатитысячный", RussianNumeral.getNumeral(19000,
                        new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null))),
                () -> assertEquals("двадцатитысячный", RussianNumeral.getNumeral(20000,
                        new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null))),
                () -> assertEquals("двадцатидвухтысячный", RussianNumeral.getNumeral(22000,
                        new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null))),
                () -> assertEquals("стодвухтысячный", RussianNumeral.getNumeral(102000,
                        new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null))),
                () -> assertEquals("стодвадцатитысячный", RussianNumeral.getNumeral(120000,
                        new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null))),
                () -> assertEquals("стосорокашестимиллионный", RussianNumeral.getNumeral(146000000,
                        new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null))),
                () -> assertEquals("два миллиарда стосорокасемимиллионный", RussianNumeral.getNumeral(2147000000,
                        new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null))),
                () -> assertEquals("два миллиарда сто сорок семь миллионов однотысячный",
                        RussianNumeral.getNumeral(2147001000,
                        new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null))));
        System.out.println("Исключения для сложных числительных");
        assertAll(() -> assertEquals("двадцатиоднотысячный", RussianNumeral.getNumeral(21000,
                new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null))),
                () -> assertEquals("девяностомиллионная", RussianNumeral.getNumeral(90000000,
                        new Declension(Gender.FEMININE, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null))),
                () -> assertEquals("стотысячные", RussianNumeral.getNumeral(100000,
                        new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.PLURAL, Type.ORDINAL, null))),
                () -> assertEquals("стодевяностооднотысячное", RussianNumeral.getNumeral(191000,
                        new Declension(Gender.NEUTER, Case.ACCUSATIVE, Count.SINGULAR, Type.ORDINAL, null))));
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
                        new Declension(Gender.NEUTER, Case.NOMINATIVE, Count.SINGULAR, Type.CARDINAL, null))));
        Declension d5 = new Declension(Gender.MASCULINE, Case.ACCUSATIVE, Count.SINGULAR, Type.CARDINAL, null);
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
        System.out.println("Без одушевлённости в винительном падеже должен выдать IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getNumeral(1, d5));
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
        System.out.println("Без одушевлённости в винительном падеже должен выдать IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getNumeral(4, d5));
        System.out.println("Склонение числа восемь");
        String[] eightForms = {"восемь", "восьми", "восьми", "восемь", "восемью", "восьми"};
        for (Case c : Case.values()) {
            assertEquals(eightForms[c.ordinal()], RussianNumeral.getNumeral(8,
                    new Declension(null, c, null, Type.CARDINAL, null)));
        }
        System.out.println("Числа 5-20, 30 (на примере десяти)");
        String[] tenForms = {"десять", "десяти", "десяти", "десять", "десятью", "десяти"};
        for (Case c : Case.values()) {
            assertEquals(tenForms[c.ordinal()], RussianNumeral.getNumeral(10,
                    new Declension(null, c, null, Type.CARDINAL, null)));
        }
        System.out.println("Числа 50, 60, 70, 80 (на примере пятидесяти)");
        String[] fiftyForms = {"пятьдесят", "пятидесяти", "пятидесяти", "пятьдесят", "пятьюдесятью", "пятидесяти"};
        for (Case c : Case.values()) {
            assertEquals(fiftyForms[c.ordinal()], RussianNumeral.getNumeral(50,
                    new Declension(null, c, null, Type.CARDINAL, null)));
        }
        System.out.println("Числа 40, 90, 100 должны иметь две формы");
        assertAll(() -> assertEquals("сорок", RussianNumeral.getNumeral(40, d1)),
                () -> assertEquals("девяносто", RussianNumeral.getNumeral(90, d1)),
                () -> assertEquals("сто", RussianNumeral.getNumeral(100, d1)));
        assertAll(() -> assertEquals("сорок", RussianNumeral.getNumeral(40, d5)),
                () -> assertEquals("девяносто", RussianNumeral.getNumeral(90, d5)),
                () -> assertEquals("сто", RussianNumeral.getNumeral(100, d5)));
        assertAll(() -> assertEquals("сорока", RussianNumeral.getNumeral(40, d2)),
                () -> assertEquals("девяноста", RussianNumeral.getNumeral(90, d2)),
                () -> assertEquals("ста", RussianNumeral.getNumeral(100, d2)));
        System.out.println("Сотни в именительном и винительном падеже");
        String[] hundreds = {"двести", "триста", "четыреста", "пятьсот", "шестьсот", "семьсот", "восемьсот", "девятьсот"};
        for (int i = 2; i < 10; i++) {
            for (Case c : new Case[] {Case.NOMINATIVE, Case.ACCUSATIVE}) {
                assertEquals(hundreds[i - 2], RussianNumeral.getNumeral(i * 100,
                        new Declension(null, c, null, Type.CARDINAL, null)));
            }
        }
        System.out.println("Формы числительного двести");
        String[] twoHundredForms = {"двести", "двухсот", "двумстам", "двести", "двумястами", "двухстах"};
        for (Case c : Case.values()) {
            assertEquals(twoHundredForms[c.ordinal()], RussianNumeral.getNumeral(200,
                    new Declension(null, c, null, Type.CARDINAL, null)));
        }
        System.out.println("Формы числительного триста");
        String[] threeHundredForms = {"триста", "трёхсот", "трёмстам", "триста", "тремястами", "трёхстах"};
        for (Case c : Case.values()) {
            assertEquals(threeHundredForms[c.ordinal()], RussianNumeral.getNumeral(300,
                    new Declension(null, c, null, Type.CARDINAL, null)));
        }
        System.out.println("Составные числительные от двузначных чисел");
        String[] six_seven = {"шестьдесят семь", "шестидесяти семи", "шестидесяти семи", "шестьдесят семь",
                "шестьюдесятью семью", "шестидесяти семи"};
        for (Case c : Case.values()) {
            assertEquals(six_seven[c.ordinal()], RussianNumeral.getNumeral(67,
                    new Declension(null, c, null, Type.CARDINAL, null)));
        }
        System.out.println("У составных числительных не учитываем одушевлённость...");
        String[] seventyThreeForms = {"семьдесят три", "семидесяти трёх", "семидесяти трём", "семьдесят три",
                "семьюдесятью тремя", "семидесяти трёх"};
        for (Case c : Case.values()) {
            assertAll(() -> assertEquals(seventyThreeForms[c.ordinal()], RussianNumeral.getNumeral(73,
                    new Declension(null, c, null, Type.CARDINAL, null))),
                    () -> assertEquals(seventyThreeForms[c.ordinal()], RussianNumeral.getNumeral(73,
                            new Declension(null, c, null, Type.CARDINAL, Animacy.INANIMATE))),
                    () -> assertEquals(seventyThreeForms[c.ordinal()], RussianNumeral.getNumeral(73,
                            new Declension(null, c, null, Type.CARDINAL, Animacy.ANIMATE))));
        }
        assertEquals("две тысячи двести пятьдесят три", RussianNumeral.getNumeral(2253,
                new Declension(null, Case.ACCUSATIVE, null, Type.CARDINAL, Animacy.ANIMATE)));
        System.out.println("...только если они не оканчиваются на один");
        assertAll(() -> assertEquals("двадцать один", RussianNumeral.getNumeral(21,
                new Declension(Gender.MASCULINE, Case.ACCUSATIVE, Count.SINGULAR, Type.CARDINAL, Animacy.INANIMATE))),
                () -> assertEquals("двадцать одного", RussianNumeral.getNumeral(21,
                        new Declension(Gender.MASCULINE, Case.ACCUSATIVE, Count.SINGULAR, Type.CARDINAL, Animacy.ANIMATE))),
                () -> assertEquals("двадцать одни", RussianNumeral.getNumeral(21,
                        new Declension(Gender.MASCULINE, Case.ACCUSATIVE, Count.PLURAL, Type.CARDINAL, Animacy.INANIMATE))),
                () -> assertEquals("двадцать одних", RussianNumeral.getNumeral(21,
                        new Declension(Gender.MASCULINE, Case.ACCUSATIVE, Count.PLURAL, Type.CARDINAL, Animacy.ANIMATE))));
        System.out.println("Составные числительные от трёхзначных чисел");
        String[] six_six_six = {"шестьсот шестьдесят шесть", "шестисот шестидесяти шести",
                "шестистам шестидесяти шести", "шестьсот шестьдесят шесть", "шестьюстами шестьюдесятью шестью",
                "шестистах шестидесяти шести"};
        String[] four_twenty = {"четыреста двадцать", "четырёхсот двадцати", "четырёмстам двадцати", "четыреста двадцать",
                "четырьмястами двадцатью", "четырёхстах двадцати"};
        for (Case c : Case.values()) {
            assertAll(() -> assertEquals(six_six_six[c.ordinal()], RussianNumeral.getNumeral(666,
                            new Declension(null, c, null, Type.CARDINAL, null))),
                    () -> assertEquals(four_twenty[c.ordinal()], RussianNumeral.getNumeral(420,
                            new Declension(null, c, null, Type.CARDINAL, null))));
        }
        System.out.println("Составные числительные для крупных чисел (несколько классов)");
        String[] test = {"два миллиарда сто сорок семь миллионов четыреста восемьдесят три тысячи шестьсот сорок семь",
                "двух миллиардов ста сорока семи миллионов четырёхсот восьмидесяти трёх тысяч шестисот сорока семи",
                "двум миллиардам ста сорока семи миллионам четырёмстам восьмидесяти трём тысячам шестистам сорока семи",
                "два миллиарда сто сорок семь миллионов четыреста восемьдесят три тысячи шестьсот сорок семь",
                "двумя миллиардами ста сорока семью миллионами четырьмястами восемьюдесятью тремя тысячами шестьюстами сорока семью",
                "двух миллиардах ста сорока семи миллионах четырёхстах восьмидесяти трёх тысячах шестистах сорока семи"};
        for (Case c : Case.values()) {
            assertEquals(test[c.ordinal()], RussianNumeral.getNumeral(Integer.MAX_VALUE, new Declension(null, c,
                    null, Type.CARDINAL, null)));
        }
        String[] test2 = {"двести тысяч", "двухсот тысяч", "двумстам тысячам", "двести тысяч", "двумястами тысячами",
                "двухстах тысячах"};
        for (Case c : Case.values()) {
            assertEquals(test2[c.ordinal()], RussianNumeral.getNumeral(200000, new Declension(null, c,
                    null, Type.CARDINAL, null)));
        }
        String[] test3 = {"сто миллионов", "ста миллионов", "ста миллионам", "сто миллионов", "ста миллионами",
                "ста миллионах"};
        for (Case c : Case.values()) {
            assertEquals(test3[c.ordinal()], RussianNumeral.getNumeral(100000000, new Declension(null, c,
                    null, Type.CARDINAL, null)));
        }
        System.out.println("Пустые классы пропускаются");
        assertEquals("девятнадцать миллионов шесть",
                RussianNumeral.getNumeral(19000006, new Declension(null, Case.NOMINATIVE,null,
                        Type.CARDINAL, null)));
        System.out.println("С классов снимается одушевлённость: вижу две тысячи (человек)");
        assertAll(()-> assertEquals("две тысячи", RussianNumeral.getNumeral(2000,
                new Declension(null, Case.ACCUSATIVE, null, Type.CARDINAL, Animacy.ANIMATE))),
                ()-> assertEquals("три миллиона", RussianNumeral.getNumeral(3000000,
                        new Declension(null, Case.ACCUSATIVE, null, Type.CARDINAL, Animacy.ANIMATE))),
                ()-> assertEquals("четыреста двадцать две тысячи сто двадцать одного",
                        RussianNumeral.getNumeral(422121,
                        new Declension(Gender.MASCULINE, Case.ACCUSATIVE, Count.SINGULAR, Type.CARDINAL, Animacy.ANIMATE))));
        System.out.println("С классов снимается множественное число: одна тысяча сто одни (сутки)");
        assertEquals("одна тысяча сто одни", RussianNumeral.getNumeral(1101,
                new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.PLURAL, Type.CARDINAL, null)));
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
        Declension neuNomDeclension = new Declension(Gender.NEUTER, Case.NOMINATIVE, null, null, null);
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