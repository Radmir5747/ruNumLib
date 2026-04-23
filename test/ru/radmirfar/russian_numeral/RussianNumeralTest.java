package ru.radmirfar.russian_numeral;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class RussianNumeralTest {

    /**
     * Выдаёт все падежные формы дробного числительного.
     * @param num экземпляр класса Fraction
     * @return список с падежными формами
     */
    ArrayList<String> getAllCases(Fraction num) {
        ArrayList<String> res = new ArrayList<>();
        for (Case c : Case.values()) {
            res.add(RussianNumeral.getNumeral(num, new DeclensionBuilder(c).build()));
        }
        return res;
    }

    /**
     * Выдаёт все падежные формы числительного, принимает на вход клише с грамматическими характеристиками.
     * @param num число
     * @param baseDeclension клише с грамматическими характеристиками
     * @param skipAccusative пропускать форму винительного падежа
     * @return список с падежными формами
     */
    ArrayList<String> getAllCases(int num, Declension baseDeclension, boolean skipAccusative) {
        ArrayList<String> res = new ArrayList<>();
        for (Case c : Case.values()) {
            if (skipAccusative && c == Case.ACCUSATIVE) {
                res.add("");
                continue;
            }
            res.add(RussianNumeral.getNumeral(num, new DeclensionBuilder(baseDeclension).gramCase(c).build()));
        }
        return res;
    }

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
                                new Declension(null, Case.NOMINATIVE, null, null, null))),
                () -> assertEquals("минус два миллиарда сто сорок семь миллионов четыреста восемьдесят три тысячи шестьсот сорок семь",
                        RussianNumeral.getNumeral(Integer.MIN_VALUE + 1, new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null))));
        System.out.println("TEMP: при передаче Integer.MIN_VALUE должен выдать IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () ->
                RussianNumeral.getNumeral(Integer.MIN_VALUE, new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null)));
    }

    @Test
    @DisplayName("Проверка дробей")
    void fractionCheck() {
        Fraction f1 = new Fraction(1, 7); // 1/7
        Fraction f2 = new Fraction(5, 10); // 5/10
        Fraction f3 = new Fraction(1, 2, 5); // 1 2/5
        Fraction f4 = new Fraction(3, 2, 9); // 3 2/9
        ArrayList<String> test1 = new ArrayList<>(), test2 = new ArrayList<>(), test3 = new ArrayList<>(),
                test4 = new ArrayList<>();
        Collections.addAll(test1, "одна седьмая", "одной седьмой", "одной седьмой", "одну седьмую",
                "одной седьмой", "одной седьмой");
        Collections.addAll(test2, "пять десятых", "пяти десятых", "пяти десятым", "пять десятых",
                "пятью десятыми", "пяти десятых");
        Collections.addAll(test3, "одна целая две пятых", "одной целой двух пятых", "одной целой двум пятым",
                "одну целую две пятых", "одной целой двумя пятыми", "одной целой двух пятых");
        Collections.addAll(test4, "три целых две девятых", "трёх целых двух девятых", "трём целым двум девятым",
                "три целых две девятых", "тремя целыми двумя девятыми", "трёх целых двух девятых");
        System.out.println("Простая дробь, 1 в числителе");
        assertLinesMatch(test1, getAllCases(f1));
        System.out.println("Простая дробь");
        assertLinesMatch(test2, getAllCases(f2));
        System.out.println("Составная дробь, 1 в целой части");
        assertLinesMatch(test3, getAllCases(f3));
        System.out.println("Составная дробь");
        assertLinesMatch(test4, getAllCases(f4));
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
        ArrayList<String> third_1 = new ArrayList<>(), third_2 = new ArrayList<>(), third_3 = new ArrayList<>(),
                third_4 = new ArrayList<>();
        Collections.addAll(third_1, "третий", "третьего", "третьему", "", "третьим", "третьем");
        Collections.addAll(third_2, "третья", "третьей", "третьей", "третью", "третьей", "третьей");
        Collections.addAll(third_3, "третье", "третьего", "третьему", "третье", "третьим", "третьем");
        Collections.addAll(third_4, "третьи", "третьих", "третьим", "", "третьими", "третьих");
        assertLinesMatch(third_2, getAllCases(3,
                new Declension(Gender.FEMININE, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null),false));
        assertLinesMatch(third_3, getAllCases(3,
                new Declension(Gender.NEUTER, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null),false));
        assertLinesMatch(third_1, getAllCases(3,
                new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null), true));
        assertLinesMatch(third_4,
                getAllCases(3, new Declension(Gender.FEMININE, Case.NOMINATIVE, Count.PLURAL, Type.ORDINAL, null), true));
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
        int[] ints2 = {1, 4, 10, 90, 100, 300, 1000, (int)1_000_000, (int)1_000_000_000};
        ArrayList<ArrayList<String>> yi_tests = new ArrayList<>();
        for (int i = 0; i < ints2.length; i++) {
            yi_tests.add(new ArrayList<>());
        }
        Collections.addAll(yi_tests.get(0),"первый", "первого", "первому", "", "первым", "первом" );
        Collections.addAll(yi_tests.get(1), "четвёртый", "четвёртого", "четвёртому", "", "четвёртым", "четвёртом");
        Collections.addAll(yi_tests.get(2), "десятый", "десятого", "десятому", "", "десятым", "десятом");
        Collections.addAll(yi_tests.get(3), "девяностый", "девяностого", "девяностому", "", "девяностым", "девяностом");
        Collections.addAll(yi_tests.get(4), "сотый", "сотого", "сотому", "", "сотым", "сотом");
        Collections.addAll(yi_tests.get(5), "трёхсотый", "трёхсотого", "трёхсотому", "", "трёхсотым", "трёхсотом");
        Collections.addAll(yi_tests.get(6), "тысячный", "тысячного", "тысячному", "", "тысячным", "тысячном");
        Collections.addAll(yi_tests.get(7), "миллионный", "миллионного", "миллионному", "", "миллионным", "миллионном");
        Collections.addAll(yi_tests.get(8), "миллиардный", "миллиардного", "миллиардному", "", "миллиардным", "миллиардном");
        for (int i = 0; i < ints2.length; i++) {
            assertLinesMatch(yi_tests.get(i),
                    getAllCases(ints2[i], new Declension(Gender.MASCULINE, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null),
                    true));
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
                () -> assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getNumeral((int)1_000_000, d1)),
                () -> assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getNumeral((int)1_000_000_000, d1)));
        System.out.println("Тысяча, миллион, миллиард");
        String[][][] strings1 = {{{"тысяча", "тысячи", "тысяче", "тысячу", "тысячей", "тысяче"},
                {"тысячи", "тысяч", "тысячам", "тысячи", "тысячами", "тысячах"}},
        {{"миллион", "миллиона", "миллиону", "миллион", "миллионом", "миллионе"},
                {"миллионы", "миллионов", "миллионам", "миллионы", "миллионами", "миллионах"}},
        {{"миллиард", "миллиарда", "миллиарду", "миллиард", "миллиардом", "миллиарде"},
                {"миллиарды", "миллиардов", "миллиардам", "миллиарды", "миллиардами", "миллиардах"}}};
        int[] bigNums = new int[]{1000, (int) 1_000_000, (int) 1_000_000_000};
        for (int i = 0; i < bigNums.length; i++) {
            for (Case c : Case.values()) {
                for (Count cnt : Count.values()) {
                    assertEquals(strings1[i][cnt.ordinal()][c.ordinal()], RussianNumeral.getNumeral(bigNums[i],
                            new Declension(null, c, cnt, Type.CARDINAL, null)));
                }
            }
        }
        System.out.println("Формы числительного ноль");
        ArrayList<String> nullForms = new ArrayList<>();
        Collections.addAll(nullForms, "ноль", "ноля", "нолю", "ноль", "нолём", "ноле");
        assertLinesMatch(nullForms, getAllCases(0,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), false));
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
        ArrayList<String> eightForms = new ArrayList<>(), tenForms = new ArrayList<>(), fiftyForms = new ArrayList<>();
        Collections.addAll(eightForms, "восемь", "восьми", "восьми", "восемь", "восемью", "восьми");
        assertLinesMatch(eightForms, getAllCases(8,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), false));
        System.out.println("Числа 5-20, 30 (на примере десяти)");
        Collections.addAll(tenForms, "десять", "десяти", "десяти", "десять", "десятью", "десяти");
        assertLinesMatch(tenForms, getAllCases(10,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), false));
        System.out.println("Числа 50, 60, 70, 80 (на примере пятидесяти)");
        Collections.addAll(fiftyForms, "пятьдесят", "пятидесяти", "пятидесяти", "пятьдесят", "пятьюдесятью", "пятидесяти");
        assertLinesMatch(fiftyForms, getAllCases(50,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), false));
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
        ArrayList<String> twoHundredForms = new ArrayList<>(), threeHundredForms = new ArrayList<>(),
                six_seven = new ArrayList<>(), seventyThreeForms = new ArrayList<>();
        Collections.addAll(twoHundredForms, "двести", "двухсот", "двумстам", "двести", "двумястами", "двухстах");
        assertLinesMatch(twoHundredForms, getAllCases(200,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), false));
        System.out.println("Формы числительного триста");
        Collections.addAll(threeHundredForms, "триста", "трёхсот", "трёмстам", "триста", "тремястами", "трёхстах");
        assertLinesMatch(threeHundredForms, getAllCases(300,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), false));
        System.out.println("Составные числительные от двузначных чисел");
        Collections.addAll(six_seven, "шестьдесят семь", "шестидесяти семи", "шестидесяти семи",
                "шестьдесят семь", "шестьюдесятью семью", "шестидесяти семи");
        assertLinesMatch(six_seven, getAllCases(67,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), false));
        System.out.println("У составных числительных не учитываем одушевлённость...");
        Collections.addAll(seventyThreeForms, "семьдесят три", "семидесяти трёх", "семидесяти трём",
                "семьдесят три", "семьюдесятью тремя", "семидесяти трёх");
        assertAll(() -> assertLinesMatch(seventyThreeForms, getAllCases(73,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), false)),
                () -> assertLinesMatch(seventyThreeForms, getAllCases(73,
                        new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, Animacy.INANIMATE), false)),
                () -> assertLinesMatch(seventyThreeForms, getAllCases(73,
                        new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, Animacy.ANIMATE), false)));
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
        ArrayList<String> six_six_six = new ArrayList<>(), four_twenty = new ArrayList<>(), test = new ArrayList<>(),
                test2 = new ArrayList<>(), test3 = new ArrayList<>();
        Collections.addAll(six_six_six, "шестьсот шестьдесят шесть", "шестисот шестидесяти шести",
                "шестистам шестидесяти шести", "шестьсот шестьдесят шесть", "шестьюстами шестьюдесятью шестью",
                "шестистах шестидесяти шести");
        assertLinesMatch(six_six_six, getAllCases(666,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), false));
        Collections.addAll(four_twenty, "четыреста двадцать", "четырёхсот двадцати", "четырёмстам двадцати",
                "четыреста двадцать", "четырьмястами двадцатью", "четырёхстах двадцати");
        assertLinesMatch(four_twenty, getAllCases(420,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), false));
        System.out.println("Составные числительные для крупных чисел (несколько классов)");
        Collections.addAll(test,
                "два миллиарда сто сорок семь миллионов четыреста восемьдесят три тысячи шестьсот сорок семь",
                "двух миллиардов ста сорока семи миллионов четырёхсот восьмидесяти трёх тысяч шестисот сорока семи",
                "двум миллиардам ста сорока семи миллионам четырёмстам восьмидесяти трём тысячам шестистам сорока семи",
                "два миллиарда сто сорок семь миллионов четыреста восемьдесят три тысячи шестьсот сорок семь",
                "двумя миллиардами ста сорока семью миллионами четырьмястами восемьюдесятью тремя тысячами шестьюстами сорока семью",
                "двух миллиардах ста сорока семи миллионах четырёхстах восьмидесяти трёх тысячах шестистах сорока семи");
        assertLinesMatch(test, getAllCases(Integer.MAX_VALUE, new Declension(null, Case.NOMINATIVE,
                null, Type.CARDINAL, null), false));
        Collections.addAll(test2, "двести тысяч", "двухсот тысяч", "двумстам тысячам", "двести тысяч",
                "двумястами тысячами", "двухстах тысячах");
        assertLinesMatch(test2, getAllCases(200000, new Declension(null, Case.NOMINATIVE,
                null, Type.CARDINAL, null), false));
        Collections.addAll(test3, "сто миллионов", "ста миллионов", "ста миллионам", "сто миллионов",
                "ста миллионами", "ста миллионах");
        assertLinesMatch(test3, getAllCases(100000000, new Declension(null, Case.NOMINATIVE,
                null, Type.CARDINAL, null), false));
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
        Declension nomDeclension = new Declension(null, Case.NOMINATIVE, null, Type.COLLECTIVE, null);
        System.out.println("Если число меньше 2 или больше 10, должен выдать IllegalArgumentException");
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getNumeral(0, nomDeclension)),
                () -> assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getNumeral(12, nomDeclension)));
        System.out.println("Без одушевлённости в винительном падеже должен выдать IllegalArgumentException");
        Declension errDeclension = new Declension(null, Case.ACCUSATIVE, null, null, null);
        assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getNumeral(2, errDeclension));
        String[] strings = {"двое", "трое", "четверо", "пятеро", "шестеро", "семеро", "восьмеро", "девятеро", "десятеро"};
        System.out.println("Собирательные числительные от 2 до 10 в именительном падеже");
        for (int i = 2; i < 11; i++) {
            assertEquals(strings[i - 2], RussianNumeral.getNumeral(i, nomDeclension));
        }
        System.out.println("Формы винительного падежа должны отличаться в зависимости от одушевлённости");
        Declension accInanDeclension = new Declension(null, Case.ACCUSATIVE, null, Type.COLLECTIVE, Animacy.INANIMATE);
        Declension accAnDeclension = new Declension(null, Case.ACCUSATIVE, null, Type.COLLECTIVE, Animacy.ANIMATE);
        Declension genDeclension = new Declension(null, Case.GENITIVE, null, Type.COLLECTIVE, null);
        for (int i = 2; i < 11; i++) {
            String result1 = RussianNumeral.getNumeral(i, accInanDeclension);
            String result2 = RussianNumeral.getNumeral(i, accAnDeclension);
            String expected1 = RussianNumeral.getNumeral(i, nomDeclension);
            String expected2 = RussianNumeral.getNumeral(i, genDeclension);
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