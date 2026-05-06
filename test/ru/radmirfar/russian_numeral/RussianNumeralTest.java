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

    /**
     * Выдаёт все падежные формы связки числительное + существительное, принимает на вход грамматические характеристики
     * @param num число
     * @param baseDeclension грамматические характеристики
     * @param noun существительное
     * @return список с падежными формами вида "числительное существительное"
     */
    ArrayList<String> getAllNumeralWithNounCases(int num, Declension baseDeclension, Noun noun) {
        ArrayList<String> res = new ArrayList<>();
        for (Case c : Case.values()) {
            String[] tmp = RussianNumeral.getNumeralWithNoun(num, noun, new DeclensionBuilder(baseDeclension).gramCase(c).build());
            res.add(tmp[0] + " " + tmp[1]);
        }
        return res;
    }

    /**
     * Выдаёт все падежные формы связки числительное + существительное, принимает на вход грамматические характеристики
     * @param num число
     * @param baseDeclension грамматические характеристики
     * @param noun существительное
     * @return список с падежными формами вида "числительное существительное"
     */
    ArrayList<String> getAllNumeralWithNounCases(Fraction num, Declension baseDeclension, Noun noun) {
        ArrayList<String> res = new ArrayList<>();
        for (Case c : Case.values()) {
            String[] tmp = RussianNumeral.getNumeralWithNoun(num, noun, new DeclensionBuilder(baseDeclension).gramCase(c).build());
            res.add(tmp[0] + " " + tmp[1]);
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
        int[] ints2 = {1, 4, 10, 90, 100, 300, 1000, 1_000_000, 1_000_000_000};
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
                () -> assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getNumeral(1_000_000, d1)),
                () -> assertThrows(IllegalArgumentException.class, () -> RussianNumeral.getNumeral(1_000_000_000, d1)));
        System.out.println("Тысяча, миллион, миллиард");
        String[][][] strings1 = {{{"тысяча", "тысячи", "тысяче", "тысячу", "тысячей", "тысяче"},
                {"тысячи", "тысяч", "тысячам", "тысячи", "тысячами", "тысячах"}},
        {{"миллион", "миллиона", "миллиону", "миллион", "миллионом", "миллионе"},
                {"миллионы", "миллионов", "миллионам", "миллионы", "миллионами", "миллионах"}},
        {{"миллиард", "миллиарда", "миллиарду", "миллиард", "миллиардом", "миллиарде"},
                {"миллиарды", "миллиардов", "миллиардам", "миллиарды", "миллиардами", "миллиардах"}}};
        int[] bigNums = new int[]{1000, 1_000_000, 1_000_000_000};
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
                ()-> assertEquals("три тысячи", RussianNumeral.getNumeral(3000,
                        new Declension(null, Case.ACCUSATIVE, null, Type.CARDINAL, Animacy.ANIMATE))),
                ()-> assertEquals("два миллиона", RussianNumeral.getNumeral(2000000,
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

    @Test
    @DisplayName("Согласование числительного и существительного")
    void getNumeralWithNoun() {
        Noun year = new Noun(Gender.MASCULINE, Animacy.INANIMATE,
                new String[]{"год", "года", "году", "год", "годом", "годе"},
                new String[]{"годы", "лет", "годам", "годы", "годами", "годах"});
        ArrayList<String> correctYearForms1 = new ArrayList<>(), correctYearForms2 = new ArrayList<>(),
                correctYearForms5 = new ArrayList<>(), correctYearForms0 = new ArrayList<>(),
                yearOrdinalSingularForms = new ArrayList<>(), yearOrdinalPluralForms = new ArrayList<>();
        Collections.addAll(correctYearForms1, "один год", "одного года", "одному году",
                "один год", "одним годом", "одном годе");
        Collections.addAll(correctYearForms2, /*"джва года"*/"два года", "двух лет", "двум годам", "два года",
                "двумя годами", "двух годах");
        Collections.addAll(correctYearForms5, "пять лет", "пяти лет", "пяти годам", "пять лет",
                "пятью годами", "пяти годах");
        Collections.addAll(correctYearForms0, "ноль лет", "ноля лет", "нолю лет", "ноль лет",
                "нолём лет", "ноле лет");
        Collections.addAll(yearOrdinalSingularForms, "первый год", "первого года", "первому году",
                "первый год", "первым годом", "первом годе");
        Collections.addAll(yearOrdinalPluralForms, "первые годы", "первых лет", "первым годам",
                "первые годы", "первыми годами", "первых годах");
        System.out.println("Мужской род, неодушевлённое (смешанные формы)");
        System.out.println("Количественные числительные");
        assertLinesMatch(correctYearForms1, getAllNumeralWithNounCases(1,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), year));
        assertLinesMatch(correctYearForms2, getAllNumeralWithNounCases(2,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), year));
        assertLinesMatch(correctYearForms5, getAllNumeralWithNounCases(5,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), year));
        assertLinesMatch(correctYearForms0, getAllNumeralWithNounCases(0,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), year));
        System.out.println("Порядковые числительные");
        assertLinesMatch(yearOrdinalSingularForms, getAllNumeralWithNounCases(1,
                new Declension(null, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null), year));
        assertLinesMatch(yearOrdinalPluralForms, getAllNumeralWithNounCases(1,
                new Declension(null, Case.NOMINATIVE, Count.PLURAL, Type.ORDINAL, null), year));
        Noun hand = new Noun(Gender.FEMININE, Animacy.INANIMATE,
                new String[]{"рука", "руки", "руке", "руку", "рукой", "руке"},
                new String[]{"руки", "рук", "рукам", "руки", "руками", "руках"});
        ArrayList<String> correctHandForms1 = new ArrayList<>(), correctHandForms2 = new ArrayList<>(),
                correctHandForms5 = new ArrayList<>(), correctHandForms0 = new ArrayList<>(),
                handOrdinalSingularForms = new ArrayList<>(), handOrdinalPluralForms = new ArrayList<>();
        Collections.addAll(correctHandForms1, "одна рука", "одной руки", "одной руке", "одну руку",
                "одной рукой", "одной руке");
        Collections.addAll(correctHandForms2, "две руки", "двух рук", "двум рукам", "две руки",
                "двумя руками", "двух руках");
        Collections.addAll(correctHandForms5, "пять рук", "пяти рук", "пяти рукам", "пять рук",
                "пятью руками", "пяти руках");
        Collections.addAll(correctHandForms0, "ноль рук", "ноля рук", "нолю рук", "ноль рук",
                "нолём рук", "ноле рук");
        Collections.addAll(handOrdinalSingularForms, "третья рука", "третьей руки", "третьей руке",
                "третью руку", "третьей рукой", "третьей руке");
        Collections.addAll(handOrdinalPluralForms, "третьи руки", "третьих рук", "третьим рукам",
                "третьи руки", "третьими руками", "третьих руках");
        System.out.println("Женский род, неодушевлённое");
        System.out.println("Количественные числительные");
        assertLinesMatch(correctHandForms1, getAllNumeralWithNounCases(1,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), hand));
        assertLinesMatch(correctHandForms2, getAllNumeralWithNounCases(2,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), hand));
        assertLinesMatch(correctHandForms5, getAllNumeralWithNounCases(5,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), hand));
        assertLinesMatch(correctHandForms0, getAllNumeralWithNounCases(0,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), hand));
        System.out.println("Порядковые числительные");
        assertLinesMatch(handOrdinalSingularForms, getAllNumeralWithNounCases(3,
                new Declension(null, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null), hand));
        assertLinesMatch(handOrdinalPluralForms, getAllNumeralWithNounCases(3,
                new Declension(null, Case.NOMINATIVE, Count.PLURAL, Type.ORDINAL, null), hand));
        Noun girl = new Noun(Gender.FEMININE, Animacy.ANIMATE,
                new String[]{"девочка", "девочки", "девочке", "девочку", "девочкой", "девочке"},
                new String[]{"девочки", "девочек", "девочкам", "девочек", "девочками", "девочках"});
        ArrayList<String> correctGirlForms1 = new ArrayList<>(), correctGirlForms2 = new ArrayList<>(),
                correctGirlForms5 = new ArrayList<>(), correctGirlForms0 = new ArrayList<>(),
                girlOrdinalSingularForms = new ArrayList<>(), girlOrdinalPluralForms = new ArrayList<>();
        Collections.addAll(correctGirlForms1, "одна девочка", "одной девочки", "одной девочке",
                "одну девочку", "одной девочкой", "одной девочке");
        Collections.addAll(correctGirlForms2, "две девочки", "двух девочек", "двум девочкам", "двух девочек",
                "двумя девочками", "двух девочках");
        Collections.addAll(correctGirlForms5, "пять девочек", "пяти девочек", "пяти девочкам", "пять девочек",
                "пятью девочками", "пяти девочках");
        Collections.addAll(correctGirlForms0, "ноль девочек", "ноля девочек", "нолю девочек", "ноль девочек",
                "нолём девочек", "ноле девочек");
        Collections.addAll(girlOrdinalSingularForms, "пятая девочка", "пятой девочки", "пятой девочке",
                "пятую девочку", "пятой девочкой", "пятой девочке");
        Collections.addAll(girlOrdinalPluralForms, "пятые девочки", "пятых девочек", "пятым девочкам",
                "пятых девочек", "пятыми девочками", "пятых девочках");
        System.out.println("Женский род, одушевлённое");
        System.out.println("Количественные числительные");
        assertLinesMatch(correctGirlForms1, getAllNumeralWithNounCases(1,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), girl));
        assertLinesMatch(correctGirlForms2, getAllNumeralWithNounCases(2,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), girl));
        assertLinesMatch(correctGirlForms5, getAllNumeralWithNounCases(5,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), girl));
        assertLinesMatch(correctGirlForms0, getAllNumeralWithNounCases(0,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), girl));
        System.out.println("Порядковые числительные");
        assertLinesMatch(girlOrdinalSingularForms, getAllNumeralWithNounCases(5,
                new Declension(null, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null), girl));
        assertLinesMatch(girlOrdinalPluralForms, getAllNumeralWithNounCases(5,
                new Declension(null, Case.NOMINATIVE, Count.PLURAL, Type.ORDINAL, null), girl));
        Noun table = new Noun(Gender.MASCULINE, Animacy.INANIMATE,
                new String[]{"стол", "стола", "столу", "стол", "столом", "столе"},
                new String[]{"столы", "столов", "столам", "столы", "столами", "столах"});
        ArrayList<String> correctTableForms1 = new ArrayList<>(), correctTableForms2 = new ArrayList<>(),
                correctTableForms5 = new ArrayList<>(), correctTableForms0 = new ArrayList<>(),
                tableOrdinalSingularForms = new ArrayList<>(), tableOrdinalPluralForms = new ArrayList<>();
        Collections.addAll(correctTableForms1, "один стол", "одного стола", "одному столу", "один стол",
                "одним столом", "одном столе");
        Collections.addAll(correctTableForms2, "два стола", "двух столов", "двум столам", "два стола",
                "двумя столами", "двух столах");
        Collections.addAll(correctTableForms5, "пять столов", "пяти столов", "пяти столам", "пять столов",
                "пятью столами", "пяти столах");
        Collections.addAll(correctTableForms0, "ноль столов", "ноля столов", "нолю столов", "ноль столов",
                "нолём столов", "ноле столов");
        Collections.addAll(tableOrdinalSingularForms, "второй стол", "второго стола", "второму столу",
                "второй стол", "вторым столом", "втором столе");
        Collections.addAll(tableOrdinalPluralForms, "вторые столы", "вторых столов", "вторым столам",
                "вторые столы", "вторыми столами", "вторых столах");
        System.out.println("Мужской род, неодушевлённое");
        System.out.println("Количественные числительные");
        assertLinesMatch(correctTableForms1, getAllNumeralWithNounCases(1,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), table));
        assertLinesMatch(correctTableForms2, getAllNumeralWithNounCases(2,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), table));
        assertLinesMatch(correctTableForms5, getAllNumeralWithNounCases(5,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), table));
        assertLinesMatch(correctTableForms0, getAllNumeralWithNounCases(0,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), table));
        System.out.println("Порядковые числительные");
        assertLinesMatch(tableOrdinalSingularForms, getAllNumeralWithNounCases(2,
                new Declension(null, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null), table));
        assertLinesMatch(tableOrdinalPluralForms, getAllNumeralWithNounCases(2,
                new Declension(null, Case.NOMINATIVE, Count.PLURAL, Type.ORDINAL, null), table));
        Noun boy = new Noun(Gender.MASCULINE, Animacy.ANIMATE,
                new String[]{"мальчик", "мальчика", "мальчику", "мальчика", "мальчиком", "мальчике"},
                new String[]{"мальчики", "мальчиков", "мальчикам", "мальчиков", "мальчиками", "мальчиках"});
        ArrayList<String> correctBoyForms1 = new ArrayList<>(), correctBoyForms2 = new ArrayList<>(),
                correctBoyForms5 = new ArrayList<>(), correctBoyForms0 = new ArrayList<>(),
                boyOrdinalSingularForms = new ArrayList<>(), boyOrdinalPluralForms = new ArrayList<>();
        Collections.addAll(correctBoyForms1, "один мальчик", "одного мальчика", "одному мальчику",
                "одного мальчика", "одним мальчиком", "одном мальчике");
        Collections.addAll(correctBoyForms2, "два мальчика", "двух мальчиков", "двум мальчикам",
                "двух мальчиков", "двумя мальчиками", "двух мальчиках");
        Collections.addAll(correctBoyForms5, "пять мальчиков", "пяти мальчиков", "пяти мальчикам",
                "пять мальчиков", "пятью мальчиками", "пяти мальчиках");
        Collections.addAll(correctBoyForms0, "ноль мальчиков", "ноля мальчиков",
                "нолю мальчиков", "ноль мальчиков", "нолём мальчиков", "ноле мальчиков");
        Collections.addAll(boyOrdinalSingularForms, "первый мальчик", "первого мальчика", "первому мальчику",
                "первого мальчика", "первым мальчиком", "первом мальчике");
        Collections.addAll(boyOrdinalPluralForms, "первые мальчики", "первых мальчиков", "первым мальчикам",
                "первых мальчиков", "первыми мальчиками", "первых мальчиках");
        System.out.println("Мужской род, одушевлённое");
        System.out.println("Количественные числительные");
        assertLinesMatch(correctBoyForms1, getAllNumeralWithNounCases(1,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), boy));
        assertLinesMatch(correctBoyForms2, getAllNumeralWithNounCases(2,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), boy));
        assertLinesMatch(correctBoyForms5, getAllNumeralWithNounCases(5,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), boy));
        assertLinesMatch(correctBoyForms0, getAllNumeralWithNounCases(0,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), boy));
        System.out.println("Порядковые числительные");
        assertLinesMatch(boyOrdinalSingularForms, getAllNumeralWithNounCases(1,
                new Declension(null, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null), boy));
        assertLinesMatch(boyOrdinalPluralForms, getAllNumeralWithNounCases(1,
                new Declension(null, Case.NOMINATIVE, Count.PLURAL, Type.ORDINAL, null), boy));
        Noun time = new Noun(Gender.NEUTER, Animacy.INANIMATE,
                new String[]{"знамя", "знамени", "знамени", "знамя", "знаменем", "знамени"},
                new String[]{"знамёна", "знамён", "знамёнам", "знамёна", "знамёнами", "знамёнах"});
        ArrayList<String> correctTimeForms1 = new ArrayList<>(), correctTimeForms2 = new ArrayList<>(),
                correctTimeForms5 = new ArrayList<>(), correctTimeForms0 = new ArrayList<>(),
                timeOrdinalSingularForms = new ArrayList<>(), timeOrdinalPluralForms = new ArrayList<>();
        Collections.addAll(correctTimeForms1, "одно знамя", "одного знамени", "одному знамени", "одно знамя",
                "одним знаменем", "одном знамени");
        Collections.addAll(correctTimeForms2, "два знамени", "двух знамён", "двум знамёнам", "два знамени",
                "двумя знамёнами", "двух знамёнах");
        Collections.addAll(correctTimeForms5, "пять знамён", "пяти знамён", "пяти знамёнам", "пять знамён",
                "пятью знамёнами", "пяти знамёнах");
        Collections.addAll(correctTimeForms0, "ноль знамён", "ноля знамён", "нолю знамён", "ноль знамён",
                "нолём знамён", "ноле знамён");
        Collections.addAll(timeOrdinalSingularForms, "первое знамя", "первого знамени", "первому знамени",
                "первое знамя", "первым знаменем", "первом знамени");
        Collections.addAll(timeOrdinalPluralForms, "первые знамёна", "первых знамён", "первым знамёнам",
                "первые знамёна", "первыми знамёнами", "первых знамёнах");
        System.out.println("Средний род, неодушевлённое");
        System.out.println("Количественные числительные");
        assertLinesMatch(correctTimeForms1, getAllNumeralWithNounCases(1,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), time));
        assertLinesMatch(correctTimeForms2, getAllNumeralWithNounCases(2,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), time));
        assertLinesMatch(correctTimeForms5, getAllNumeralWithNounCases(5,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), time));
        assertLinesMatch(correctTimeForms0, getAllNumeralWithNounCases(0,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), time));
        System.out.println("Порядковые числительные");
        assertLinesMatch(timeOrdinalSingularForms, getAllNumeralWithNounCases(1,
                new Declension(null, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null), time));
        assertLinesMatch(timeOrdinalPluralForms, getAllNumeralWithNounCases(1,
                new Declension(null, Case.NOMINATIVE, Count.PLURAL, Type.ORDINAL, null), time));
        Noun face = new Noun(Gender.NEUTER, Animacy.ANIMATE, // лицо в значении человек (несколько незнакомых лиц)
                new String[]{"лицо", "лица", "лицу", "лицо", "лицом", "лице"},
                new String[]{"лица", "лиц", "лицам", "лиц", "лицами", "лицах"});
        ArrayList<String> correctFaceForms1 = new ArrayList<>(), correctFaceForms2 = new ArrayList<>(),
                correctFaceForms5 = new ArrayList<>(), correctFaceForms0 = new ArrayList<>(),
                faceOrdinalSingularForms = new ArrayList<>(), faceOrdinalPluralForms = new ArrayList<>();;
        Collections.addAll(correctFaceForms1, "одно лицо", "одного лица", "одному лицу", "одно лицо",
                "одним лицом", "одном лице");
        Collections.addAll(correctFaceForms2, "два лица", "двух лиц", "двум лицам", "двух лиц",
                "двумя лицами", "двух лицах");
        Collections.addAll(correctFaceForms5, "пять лиц", "пяти лиц", "пяти лицам", "пять лиц",
                "пятью лицами", "пяти лицах");
        Collections.addAll(correctFaceForms0, "ноль лиц", "ноля лиц", "нолю лиц", "ноль лиц", "нолём лиц",
                "ноле лиц");
        Collections.addAll(faceOrdinalSingularForms, "первое лицо", "первого лица", "первому лицу",
                "первое лицо", "первым лицом", "первом лице");
        Collections.addAll(faceOrdinalPluralForms, "первые лица", "первых лиц", "первым лицам", "первых лиц",
                "первыми лицами", "первых лицах");
        System.out.println("Средний род, одушевлённое");
        assertLinesMatch(correctFaceForms1, getAllNumeralWithNounCases(1,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), face));
        assertLinesMatch(correctFaceForms2, getAllNumeralWithNounCases(2,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), face));
        assertLinesMatch(correctFaceForms5, getAllNumeralWithNounCases(5,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), face));
        assertLinesMatch(correctFaceForms0, getAllNumeralWithNounCases(0,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), face));
        System.out.println("Порядковые числительные");
        assertLinesMatch(faceOrdinalSingularForms, getAllNumeralWithNounCases(1,
                new Declension(null, Case.NOMINATIVE, Count.SINGULAR, Type.ORDINAL, null), face));
        assertLinesMatch(faceOrdinalPluralForms, getAllNumeralWithNounCases(1,
                new Declension(null, Case.NOMINATIVE, Count.PLURAL, Type.ORDINAL, null), face));
        System.out.println("Дополнительные проверки");
        Declension genitive = new Declension(null, Case.GENITIVE, null, Type.CARDINAL, null);
        Noun face_inan = new Noun(Gender.NEUTER, Animacy.INANIMATE, // лицо как часть тела
                new String[]{"лицо", "лица", "лицу", "лицо", "лицом", "лице"},
                new String[]{"лица", "лиц", "лицам", "лица", "лицами", "лицах"});
        String[] test1 = RussianNumeral.getNumeralWithNoun(2, face_inan,
                new Declension(null, Case.ACCUSATIVE, null, Type.CARDINAL, null));
        assertEquals("два лица", test1[0] + " " + test1[1]);
        String[] test4 = RussianNumeral.getNumeralWithNoun(1231, boy,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null));
        assertEquals("одна тысяча двести тридцать один мальчик", test4[0] + " " + test4[1]);
        String[] test2 = RussianNumeral.getNumeralWithNoun(1234, boy,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null));
        assertEquals("одна тысяча двести тридцать четыре мальчика", test2[0] + " " + test2[1]);
        String[] test3 = RussianNumeral.getNumeralWithNoun(1235, boy,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null));
        assertEquals("одна тысяча двести тридцать пять мальчиков", test3[0] + " " + test3[1]);
        String[] test3_0 = RussianNumeral.getNumeralWithNoun(1252, boy, genitive);
        assertEquals("одной тысячи двухсот пятидесяти двух мальчиков", test3_0[0] + " " + test3_0[1]);
        System.out.println("Паукальная форма (согласование слова целая)");
        Noun whole = new Noun(Gender.FEMININE, Animacy.INANIMATE,
                new String[]{"целая", "целой", "целой", "целую", "целой", "целой"},
                new String[]{"целые", "целых", "целым", "целые", "целыми", "целых"});
        // вводим паукальные счётные формы
        whole.setPaucalForms(new String[]{"целых", "целых", "целым", "целых", "целыми", "целых"});
        ArrayList<String> correctWholeForms1 = new ArrayList<>(), correctWholeForms2 = new ArrayList<>(),
                correctWholeForms5 = new ArrayList<>(), correctWholeForms0 = new ArrayList<>();
        Collections.addAll(correctWholeForms1, "одна целая", "одной целой", "одной целой", "одну целую",
                "одной целой", "одной целой");
        Collections.addAll(correctWholeForms2, "две целых", "двух целых", "двум целым", "две целых",
                "двумя целыми", "двух целых");
        Collections.addAll(correctWholeForms5, "пять целых", "пяти целых", "пяти целым", "пять целых",
                "пятью целыми", "пяти целых");
        Collections.addAll(correctWholeForms0, "ноль целых", "ноля целых", "нолю целых", "ноль целых",
                "нолём целых", "ноле целых");
        assertLinesMatch(correctWholeForms1, getAllNumeralWithNounCases(1,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), whole));
        assertLinesMatch(correctWholeForms2, getAllNumeralWithNounCases(2,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), whole));
        assertLinesMatch(correctWholeForms5, getAllNumeralWithNounCases(5,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), whole));
        assertLinesMatch(correctWholeForms0, getAllNumeralWithNounCases(0,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), whole));
        System.out.println("Согласование с дробью");
        Noun candy = new Noun(Gender.FEMININE, Animacy.INANIMATE,
                new String[]{"конфета", "конфеты", "конфете", "конфету", "конфетой", "конфете"},
                new String[]{"конфеты", "конфет", "конфетам", "конфеты", "конфетами", "конфетах"});
        Fraction oneHalf = new Fraction(1, 2);
        ArrayList<String> candyFractionSingularForms = new ArrayList<>(), candyFractionPluralForms = new ArrayList<>();
        Collections.addAll(candyFractionSingularForms, "одна вторая конфеты", "одной второй конфеты",
                "одной второй конфеты", "одну вторую конфеты", "одной второй конфеты", "одной второй конфеты");
        Collections.addAll(candyFractionPluralForms, "одна вторая конфет", "одной второй конфет",
                "одной второй конфет", "одну вторую конфет", "одной второй конфет", "одной второй конфет");
        assertLinesMatch(candyFractionSingularForms, getAllNumeralWithNounCases(oneHalf,
                new Declension(null, Case.NOMINATIVE, Count.SINGULAR, null, null), candy));
        assertLinesMatch(candyFractionPluralForms, getAllNumeralWithNounCases(oneHalf,
                new Declension(null, Case.NOMINATIVE, Count.PLURAL, null, null), candy));
        System.out.println("Согласование со словами тысяча, миллион");
        // По мотивам Письмовника Грамоты.ру, раздел «Тысяче работникам» или «тысяче работников»?
        // https://classic2.gramota.ru/spravka/letters/61-rubric-92
        Declension dative = new Declension(null, Case.DATIVE, null, Type.CARDINAL, null);
        Declension instrumental = new Declension(null, Case.INSTRUMENTAL, null, Type.CARDINAL, null);
        Noun worker = new Noun(Gender.MASCULINE, Animacy.ANIMATE,
                new String[]{"работник", "работника", "работнику", "работника", "работником", "работнике"},
                new String[]{"работники", "работников", "работникам", "работников", "работниками", "работниках"});
        String[] test5 = RussianNumeral.getNumeralWithNoun(1000, worker, dative);
        String[] test6 = RussianNumeral.getNumeralWithNoun(1_000_000, worker, dative);
        String[] test7 = RussianNumeral.getNumeralWithNoun(3000, worker, dative);
        String[] test8 = RussianNumeral.getNumeralWithNoun(1000, worker, instrumental);
        String[] test9 = RussianNumeral.getNumeralWithNoun(1_000_000, worker, instrumental);
        String[] test10 = RussianNumeral.getNumeralWithNoun(3000, worker, instrumental);
        String[] test11 = RussianNumeral.getNumeralWithNoun(25000, worker, dative);
        String[] test12 = RussianNumeral.getNumeralWithNoun(25100, worker, dative);
        assertAll(() -> assertEquals("тысяче работников", test5[0] + " " + test5[1]),
                () -> assertEquals("миллиону работников", test6[0] + " " + test6[1]),
                () -> assertEquals("трём тысячам работников", test7[0] + " " + test7[1]),
                () -> assertEquals("тысячей работников", test8[0] + " " + test8[1]),
                () -> assertEquals("миллионом работников", test9[0] + " " + test9[1]),
                () -> assertEquals("тремя тысячами работников", test10[0] + " " + test10[1]),
                () -> assertEquals("двадцати пяти тысячам работников", test11[0] + " " + test11[1]),
                () -> assertEquals("двадцати пяти тысячам ста работникам", test12[0] + " " + test12[1]));
        System.out.println("Собирательные числительные");
        ArrayList<String> boyCollective = new ArrayList<>(), faceCollective = new ArrayList<>();
        Collections.addAll(boyCollective, "двое мальчиков", "двоих мальчиков", "двоим мальчикам",
                "двоих мальчиков", "двоими мальчиками", "двоих мальчиках");
        Collections.addAll(faceCollective, "трое лиц", "троих лиц", "троим лицам", "троих лиц",
                "троими лицами", "троих лицах");
        assertLinesMatch(boyCollective, getAllNumeralWithNounCases(2,
                new Declension(null, Case.NOMINATIVE, null, Type.COLLECTIVE, null), boy));
        assertLinesMatch(faceCollective, getAllNumeralWithNounCases(3,
                new Declension(null, Case.NOMINATIVE, null, Type.COLLECTIVE, null), face));
        System.out.println("Pluralia tantum");
        Noun days = new Noun(Animacy.INANIMATE, new String[]{"сутки", "суток", "суткам", "сутки", "сутками", "сутках"});
        ArrayList<String> correctDayForms1 = new ArrayList<>(), correctDayForms5 = new ArrayList<>(),
                ordinalDayForms = new ArrayList<>();
        Collections.addAll(correctDayForms1, "одни сутки", "одних суток", "одним суткам", "одни сутки",
                "одними сутками", "одних сутках");
        Collections.addAll(correctDayForms5, "пять суток", "пяти суток", "пяти суткам", "пять суток",
                "пятью сутками", "пяти сутках");
        Collections.addAll(ordinalDayForms, "первые сутки", "первых суток", "первым суткам", "первые сутки",
                "первыми сутками", "первых сутках");
        assertLinesMatch(correctDayForms1, getAllNumeralWithNounCases(1,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), days));
        assertLinesMatch(correctDayForms5, getAllNumeralWithNounCases(5,
                new Declension(null, Case.NOMINATIVE, null, Type.CARDINAL, null), days));
        assertLinesMatch(ordinalDayForms, getAllNumeralWithNounCases(1,
                new Declension(null, Case.NOMINATIVE, null, Type.ORDINAL, null), days));
    }
}