package ru.radmirfar.russian_numeral;

import static ru.radmirfar.russian_numeral.Consts.*;

/**
 * Основной класс, содержит статические методы по преобразованию чисел в текст
 */
public class RussianNumeral {
    /**
     * Выдаёт число прописью.
     * @param num число
     * @param d грамматические признаки (разряд числительного, род, падеж, грамматическое число, одушевлённость)
     * @return число прописью
     * @throws IllegalArgumentException если отсутствуют необходимые грамматические характеристики
     */
    public static String getNumeral(int num, Declension d) {
        if (d.type == null) throw new IllegalArgumentException("Missing type of numeral");
        if (d.type == Type.COLLECTIVE) return getCollectiveNumeral(num, d);
        boolean negative = false;
        if (num < 0) {
            if (num == Integer.MIN_VALUE) throw new IllegalArgumentException("This number is not supported"); // FIXME
            negative = true;
            num *= -1;
        }
        if (d.type == Type.CARDINAL) return (negative ? "минус " : "") + getCardinalNumeral(num, d);
        return (negative ? "минус " : "") + getOrdinalNumeral(num, d);
    }

    /**
     * <p>Выдаёт дробное количественное числительное в нужном падеже.</p>
     * <p>При отсутствии падежа выдаёт исключение.</p>
     * @param num простая или смешанная дробь
     * @param d грамматические признаки (падеж)
     * @return число прописью
     * @throws IllegalArgumentException если отсутствует падеж
     */
    public static String getNumeral(Fraction num, Declension d) {
        String res = "";
        // числитель и целая часть в форме единственного числа женского рода
        Declension baseDeclension =
                new Declension(Gender.FEMININE, d.gramCase, Count.SINGULAR, Type.CARDINAL, Animacy.INANIMATE);
        // TODO: сюда можно добавить возможность выбирать: ноль целых или ничего
        if (num.whole != 0) {
            String[] parts = getNumeralWithNoun(num.whole, Consts.whole, baseDeclension);
            res += parts[0] + " " + parts[1] + " ";
        }
        res += getNumeral(num.numerator, baseDeclension) + " "; // числитель
        // знаменатель
        Declension s = new DeclensionBuilder(Declension.supplementalDeclension(num.numerator, true, d)).
                gender(Gender.FEMININE).
                build();
        // заплатка для множественного числа: в именительном и винительном падеже принимает форму родительного падежа
        if (s.isNomAcc() && s.count == Count.PLURAL) s = new DeclensionBuilder(s).gramCase(Case.GENITIVE).build();
        return res + getOrdinalNumeral(num.denominator, s);
    }

    /**
     * <p>Выдаёт согласованные числительное и существительное.</p>
     * <p>Необходимо передать падеж и разряд числительного, в случае порядкового числительного - грамматическое число,
     * иначе выдаёт исключение.</p>
     * @param num число
     * @param noun существительное
     * @param d грамматические характеристики (разряд числительного, падеж, грамматическое число)
     * @return массив, в котором первый элемент - числительное, а второй - существительное
     * @throws IllegalArgumentException если отсутствуют необходимые грамматические характеристики
     */
    public static String[] getNumeralWithNoun(int num, Noun noun, Declension d) {
        if (d.type == null) throw new IllegalArgumentException("Missing type of numeral");
        // если числительное порядковое, грамматическое число не задано и существительное не pluralia tantum
        if (d.type == Type.ORDINAL && d.count == null && !noun.pluraliaTantum) {
            throw new IllegalArgumentException("Missing count");
        }
        String[] res = new String[2];
        // согласуем числительное в роде и переносим одушевлённость
        DeclensionBuilder tempDeclension = new DeclensionBuilder(d).animacy(noun.animacy).gender(noun.gender);
        // если числительное порядковое, то ещё и грамматическое число
        if (d.type == Type.ORDINAL) {
            if (!noun.pluraliaTantum) tempDeclension.count(d.count);
            else tempDeclension.count(Count.PLURAL);
        }
        else if (d.type == Type.CARDINAL) {
            if (noun.pluraliaTantum) tempDeclension.count(Count.PLURAL);
            // заплатка для числа один: если существительное употребляется только не только во множественном числе,
            // то ставим числительное в форму единственного числа.
            // FIXME, когда будет исправлена связь между родом и грамматическим числом у числа один
            else tempDeclension.count(Count.SINGULAR);
        }
        Declension baseDeclension = tempDeclension.build();
        // первый компонент - числительное
        res[0] = getNumeral(num, baseDeclension);
        if (baseDeclension.type == Type.ORDINAL) { // в порядковых числительных не нужно изменять форму в зависимости от числа
            if (baseDeclension.count == Count.SINGULAR) res[1] = noun.singularForms[baseDeclension.gramCase.ordinal()];
            else res[1] = noun.pluralForms[baseDeclension.gramCase.ordinal()];
        }
        else {
            // если заданы особые формы для чисел 2-4, и число оканчивается на 2-4, используем их
            if (Declension.isPaucal(num) && noun.usePaucalForms) {
                res[1] = noun.paucalForms[baseDeclension.gramCase.ordinal()];
            }
            // для собирательных числительных двое-четверо в именительном падеже используем родительный падеж
            // множественного числа (исправление ошибки *двое мальчика)
            else if (Declension.isPaucal(num) && baseDeclension.gramCase == Case.NOMINATIVE
                    && baseDeclension.type == Type.COLLECTIVE) {
                res[1] = noun.getCaseForm(new DeclensionBuilder(Case.GENITIVE).count(Count.PLURAL).build());
            }
            else { // иначе согласуем существительное с числительным
                Declension s1 = Declension.supplementalDeclension(num, noun.gender == Gender.FEMININE, baseDeclension);
                if (s1.count == Count.SINGULAR) res[1] = noun.singularForms[s1.gramCase.ordinal()];
                else res[1] = noun.pluralForms[s1.gramCase.ordinal()];
            }
        }
        return res;
    }

    /**
     * <p>Выдаёт согласованные дробное количественное числительное и существительное.</p>
     * <p>Необходимо передать падеж и грамматическое число, иначе выдаёт исключение.</p>
     * @param num простая или смешанная дробь
     * @param noun существительное
     * @param d грамматические характеристики (падеж, грамматическое число)
     * @return массив, в котором первый элемент - числительное, а второй - существительное
     * @throws IllegalArgumentException если отсутствуют необходимые грамматические характеристики
     */
    public static String[] getNumeralWithNoun(Fraction num, Noun noun, Declension d) {
        String[] res = new String[2];
        res[0] = getNumeral(num, d);
        /*
         Дробные числительные всегда управляют Р. п. существительного, а его число зависит от смысла конструкции,
         ср.: одна вторая конфеты — одна вторая конфет.
         https://gramota.ru/biblioteka/spravochniki/russkij-yazyk-kratkij-teoreticheskij-kurs-dlya-shkolnikov/grammaticheskie-priznaki-kolichestvennykh-chislitelnykh
         */
        res[1] = noun.getCaseForm(new DeclensionBuilder(d).gramCase(Case.GENITIVE).build());
        return res;
    }

    /**
     * <p>Выдаёт необходимую форму числительного <i>оба</i> в зависимости от грамматического рода,
     * падежа и одушевлённости существительного, к которому относится числительное.</p>
     * <p>Необходимо передать падеж, род, в случае винительного падежа также
     * одушевлённость существительного, к которому относится числительное, иначе выдаёт исключение.</p>
     * @param d грамматические характеристики числительного (род, падеж, одушевлённость)
     * @return числительное <i>оба</i> в нужной форме
     * @throws IllegalArgumentException если отсутствуют необходимые грамматические характеристики
     */
    public static String getBoth(Declension d) {
        if (d.gender == null) throw new IllegalArgumentException("Missing gender");
        int base = d.gender == Gender.FEMININE ? 1 : 0; // в зависимости от рода используем набор окончаний
        if (d.gramCase == Case.ACCUSATIVE) { // для винительного падежа учитываем одушевлённость
            return getAccusativeForm("об", BOTH_ENDINGS[base], d);
        }
        return "об" + BOTH_ENDINGS[base][d.gramCase.ordinal()];
    }

    /**
     * <p>Выдаёт необходимую форму числительного <i>полтора</i> в зависимости от грамматического рода и падежа.</p>
     * <p>Необходимо передать падеж, в случае именительного и винительного падежа - род, иначе выдаёт исключение.</p>
     * @param d грамматические характеристики числительного (род, падеж)
     * @return числительное <i>полтора</i> в нужной форме
     * @throws IllegalArgumentException если отсутствуют необходимые грамматические характеристики
     */
    public static String getOneAndAHalf(Declension d) {
        if (!d.isNomAcc()) return "полутора"; // в падежах кроме именительного и винительного полутора
        // в именительном и винительном падеже
        if (d.gender == null) throw new IllegalArgumentException("Missing gender");
        if (d.gender == Gender.FEMININE) return "полторы"; // у женского рода полторы
        return "полтора"; // у мужского и среднего рода полтора
    }

    /**
     * <p>Выдаёт необходимую форму числительного <i>полтораста</i> в зависимости от падежа.</p>
     * <p>Необходимо передать падеж, иначе выдаёт исключение.</p>
     * @param d грамматические характеристики числительного (падеж)
     * @return числительное <i>полтораста</i> в нужной форме
     * @throws IllegalArgumentException если отсутствуют необходимые грамматические характеристики
     */
    public static String get150(Declension d) {
        return getOneAndAHalf(new DeclensionBuilder(d.gramCase).gender(Gender.MASCULINE).build()) + "ста";
    }

    /**
     * <p>Выдаёт количественное собирательное числительное прописью в нужной форме.</p>
     * <p>Необходимо передать падеж, в случае винительного падежа - одушевлённость существительного, к которому относится
     * числительное, иначе выдаёт исключение.</p>
     * <p>При передаче числа, не принадлежащего интервалу [2;10], выдаёт исключение
     * (т.к. собирательные числительные больше десяти не употребляются,
     * а меньше двух не бывают).</p>
     *
     * @param num число от 2 до 10
     * @param d грамматические характеристики числительного (падеж, одушевлённость)
     * @return число прописью
     * @throws IllegalArgumentException если число меньше 2 или больше 10; отсутствуют
     * необходимые грамматические характеристики.
     */
    private static String getCollectiveNumeral(int num, Declension d) {
        if (num < 2 || num > 10) throw new IllegalArgumentException("Only numbers from 2 to 10 are supported");
        String res = COLLECTIVE_NUMERAL_BASES[num - 2];
        int base = num < 4 ? 0 : 1; // двое, трое - мягкая основа
        if (d.gramCase == Case.ACCUSATIVE) { // для винительного падежа учитываем одушевлённость
            return getAccusativeForm(res, COLLECTIVE_NUM_ENDINGS[base], d);
        }
        return res + COLLECTIVE_NUM_ENDINGS[base][d.gramCase.ordinal()];
    }

    /**
     * Выдаёт порядковое числительное в нужной форме.
     * @param num число
     * @param d грамматические признаки (род, падеж, грамматическое число, одушевлённость)
     * @return число прописью
     * @throws IllegalArgumentException если отсутствуют необходимые грамматические характеристики
     */
    private static String getOrdinalNumeral(int num, Declension d) {
        if (d.gender == null) throw new IllegalArgumentException("Missing gender");
        if (d.count == null) throw new IllegalArgumentException("Missing grammatical count");
        String res = "";
        int ending = d.count == Count.PLURAL ? 3 : d.gender.ordinal(); // выбираем набор окончаний
        if (num == 3) { // числительное третий использует набор мягких окончаний
            // для винительного падежа у мужского рода и множественного числа учитываем одушевлённость
            if (d.adjCheck()) return getAccusativeForm(ZERO_EIGHT_ORD_BASES[3], SOFT_ORD_ENDINGS[ending], d);
            return ZERO_EIGHT_ORD_BASES[3] + SOFT_ORD_ENDINGS[ending][d.gramCase.ordinal()];
        }
        // эти числительные имеют окончание ой в начальной форме
        if (num == 0 || num == 2 || num == 6 || num == 7 || num == 8 || num == 40) {
            res = num == 40 ? "сороков" : ZERO_EIGHT_ORD_BASES[num];
            // используем окончание ой в именительном / винительном (у неодуш.) падеже мужского рода
            if (d.gender == Gender.MASCULINE && d.count == Count.SINGULAR) {
                if (d.gramCase == Case.NOMINATIVE) return res + "ой"; // в именительном падеже окончание ой
                /*
                 Для винительного падежа учитываем одушевлённость. Почему это работает? Винительный падеж использует
                 формы именительного или родительного падежа в зависимости от одушевлённости. Значения enum'а
                 для этих падежей - 0 и 1 соотв. Мы гарантированно не выйдем за пределы массива.
                */
                if (d.gramCase == Case.ACCUSATIVE) return getAccusativeForm(res, new String[]{"ой", "ого"}, d);
            }
        }
        // получаем основы числительных 1, 4, 5-20, (3-9)0, (1-9)00, 1k, 1m, 1b
        res = res.isEmpty() ? getOrdBases(num) : res; // исключаем вероятно ошибочные 6-8
        if (!res.isEmpty()) { // если есть основа с твёрдыми окончаниями
            // для винительного падежа у мужского рода и множественного числа учитываем одушевлённость
            if (d.adjCheck()) return getAccusativeForm(res, HARD_ORD_ENDINGS[ending], d);
            // в остальных случаях используем стандартный набор твёрдых окончаний
            return res + HARD_ORD_ENDINGS[ending][d.gramCase.ordinal()];
        }
        if (num == 50 || num == 60 || num == 70 || num == 80) {
            return getCardinalNumeral(num / 10, GENITIVE_DECLENSION) // I часть в форме родительного падежа
                    + getOrdinalNumeral(10, d); // вторая часть изменяется как числительное десятый
        }
        if (num > 20 && num < 100) { // составные числительные для двузначных чисел
            return getCardinalNumeral(num / 10 * 10, NOMINATIVE_DECLENSION) + " "
                    + getOrdinalNumeral(num % 10, d);
        }
        if (num % 1000 != 0) { // если имеются единицы-десятки-сотни
            if (num % 100 != 0) { // \d+YZ: порядковым числительным становится YZ, а \d+00 становится количественным
                return getCardinalNumeral(num / 100 * 100, NOMINATIVE_DECLENSION) + " "
                        + getOrdinalNumeral(num % 100, d);
            }
            // \d+X00: порядковым числительным становится X00, а \d+000 становится количественным
            return getCardinalNumeral(num / 1000 * 1000, NOMINATIVE_DECLENSION) + " " +
                    getOrdinalNumeral(num % 1000, d);
        }
        // сложное порядковое числительное с большими классами (тысяча, миллион и проч.)
        int baseCount = 0;
        while (num % 1000 == 0) { // доходим до последнего ненулевого разряда
            num /= 1000;
            baseCount++;
        }
        if (num > 1000) {
            /*
             Если есть классы впереди, порядковым числительным становится последний класс:
             три миллиарда два миллиона тысячный.
             https://gramota.ru/spravka/vopros/271407#question
            */
            // TODO: сюда можно добавить возможность выбирать: тысяча или одна тысяча
            res += getCardinalNumeral((int) (num / 1000 * Math.pow(1000, baseCount + 1)), NOMINATIVE_DECLENSION) + " ";
        }
        num %= 1000; // выделяем последний разряд
        int hundreds = num / 100;
        if (hundreds == 1) res += "сто"; // стотысячный, а не *статысячный
        else if (hundreds > 1) res += getCardinalNumeral(hundreds * 100, GENITIVE_DECLENSION);
        num %= 100;
        if (num > 10 && num < 20) res += getCardinalNumeral(num, GENITIVE_DECLENSION); // 11-19
        else {
            int tens = num / 10;
            if (tens == 9) res += "девяносто"; // девяностотысячный, а не *девяностатысячный
            else if (tens > 0) res += getCardinalNumeral(tens * 10, GENITIVE_DECLENSION);
            num %= 10;
            if (num == 1) res += "одно"; // двадцатиоднотысячный, а не *двадцатиодноготысячный
            else if (num > 1)
                res += getCardinalNumeral(num, new DeclensionBuilder(Case.GENITIVE).gender(Gender.MASCULINE).build());
        }
        return res + getOrdinalNumeral((int) Math.pow(1000, baseCount), d);
    }

    /**
     * Вспомогательная функция, выдаёт основы порядковых числительных с твёрдыми окончаниями.
     * @param num число
     * @return основа числительного
     */
    private static String getOrdBases(int num) {
        if (num == 1 || num == 4) { // у этих чисел особые основы
            return ZERO_EIGHT_ORD_BASES[num];
        }
        if ((num > 4 && num < 21) || (num == 30)) { // 5 - 20, 30
            int base = num == 30 ? 16 : num - 5;
            return FIVE_TWENTY_BASES[base];
        }
        if (num == 90) return "девяност";
        if (num == 100) return "сот";
        if (num == 200 || num == 300 || num == 400 || num == 500 || num == 600 || num == 700 || num == 800 || num == 900) {
            return getCardinalNumeral(num, GENITIVE_DECLENSION); // основа совпадает с числительным в родительном падеже
        }
        if (num == 1000) return "тысячн";
        if (num == 1_000_000) return "миллионн";
        if (num == 1_000_000_000) return "миллиардн";
        return "";
    }
    /**
     * Выдаёт количественное числительное в нужной форме.
     * @param num число
     * @param d грамматические признаки (род, падеж, грамматическое число, одушевлённость)
     * @return число прописью
     * @throws IllegalArgumentException если отсутствуют необходимые грамматические характеристики
     */
    private static String getCardinalNumeral(int num, Declension d) {
        String res = "";
        if (num == 1000) {
            if (d.count == null) throw new IllegalArgumentException("Missing grammatical count");
            return "тысяч" + THOUSAND_CARD_ENDINGS[d.count.ordinal()][d.gramCase.ordinal()];
        }
        if (num == 1_000_000 || num == 1_000_000_000) {
            if (d.count == null) throw new IllegalArgumentException("Missing grammatical count");
            res = num == 1_000_000 ? "миллион" : "миллиард";
            return res + MILLION_CARD_ENDINGS[d.count.ordinal()][d.gramCase.ordinal()];
        }
        if (num == 0) { // ноль - не числительное, но в его образовании участвует
            return NULL_FORMS[0] + new String[]{"ь", "я", "ю", "ь", "ём", "е"}[d.gramCase.ordinal()];
        }
        if (num == 1) {
            if (d.gender == null) throw new IllegalArgumentException("Missing gender");
            if (d.count == null) throw new IllegalArgumentException("Missing grammatical count");
            res = "од";
            int ending = d.count == Count.PLURAL ? 3 : d.gender.ordinal(); // выбираем набор окончаний
            // для винительного падежа у мужского рода и множественного числа учитываем одушевлённость
            if (d.adjCheck()) return getAccusativeForm(res, ONE_CARD_ENDINGS[ending], d);
            return res + ONE_CARD_ENDINGS[ending][d.gramCase.ordinal()];
        }
        if (num > 1 && num < 5) { // 2, 3, 4
            if (num == 2 && d.gender == null) throw new IllegalArgumentException("Missing gender");
            int ending = num - 1;
            if (num == 2) ending = d.gender == Gender.FEMININE ? 1 : 0; // учитываем род для числ. 2
            if (d.gramCase == Case.ACCUSATIVE) { // у винительного падежа учитываем одушевлённость
                return getAccusativeForm(TWO_FOUR_CARD_BASES[num - 2], TWO_FOUR_CARD_ENDINGS[ending], d);
            }
            return TWO_FOUR_CARD_BASES[num - 2] + TWO_FOUR_CARD_ENDINGS[ending][d.gramCase.ordinal()];
        }
        if ((num > 4 && num < 21) || (num == 30)) { // 5 - 20, 30
            if (num == 8) return EIGHT_CARD_CASES[d.gramCase.ordinal()]; // у числительного восемь учитываем беглую гласную
            int base = num == 30 ? 16 : num - 5;
            return FIVE_TWENTY_BASES[base] + FIVE_TWENTY_CARD_ENDINGS[d.gramCase.ordinal()];
        }
        if (num == 50 || num == 60 || num == 70 || num == 80) {
            return getCardinalNumeral(num / 10, d) + "десят" + // в именительном и винительном падеже нулевое ок.
                    (d.isNomAcc() ? "" : FIVE_TWENTY_CARD_ENDINGS[d.gramCase.ordinal()]); // иначе - ок. числ. десять
        }
        if (num == 40 || num == 90 || num == 100) {
            if (d.isNomAcc()) { // в именительном и винительном падеже
                if (num == 40) return FORTY_NINETY_CARD_BASES[0]; // сорок
                return FORTY_NINETY_CARD_BASES[num == 90 ? 1 : 2] + "о"; // девяносто, сто
            }
            return FORTY_NINETY_CARD_BASES[num == 40 ? 0 : (num == 90 ? 1 : 2)] + "а"; // в остальных падежах ок. а
        }
        if (num == 200 || num == 300 || num == 400 || num == 500 || num == 600 || num == 700 || num == 800 || num == 900) {
            Declension first_d = new DeclensionBuilder(d.gramCase)
                    .animacy(Animacy.INANIMATE).gender(Gender.FEMININE).build(); // правильная форма для двести
            res = getCardinalNumeral(num / 100, first_d);
            if (d.isNomAcc()) { // окончания для именительного / винительного падежа
                int i = 2; // окончание сот для чисел 500-900
                if (num == 200) i = 0; // окончание сти
                else if (num == 300 || num == 400) i = 1; // окончание ста
                return res + HUNDRED_CARD_NOM_ENDINGS[i];
            }
            return res + HUNDRED_CARD_ENDINGS[d.gramCase.ordinal()]; // окончания косвенных падежей одинаковые
        }
        if (num > 20 && num < 100) { // составные числительные для двузначных чисел
            // снимаем одушевлённость с числительных два-четыре
            // В конструкциях с составными числительными, оканчивающимися на два, три, четыре, винительный падеж
            // сохраняет форму именительного независимо от категории одушевленности: люблю двести семьдесят три ученика
            // https://gramota.ru/spravka/vopros/296810
            // https://gramota.ru/spravka/vopros/322608
            // НО вижу двадцать одного мальчика
            // https://gramota.ru/spravka/vopros/210165
            Declension newDeclension = num % 10 == 1 ? d : new DeclensionBuilder(d).animacy(Animacy.INANIMATE).build();
            return getCardinalNumeral(num / 10 * 10, d) + " " + getCardinalNumeral(num % 10, newDeclension);
        }
        if (num > 100 && num < 1000) { // составные числительные для трёхзначных чисел
            return getCardinalNumeral(num / 100 * 100, d) + " " + getCardinalNumeral(num % 100, d);
        }
        // разбиваем числа больше 999 на разряды
        int[] nums = getBases(num);
        // идём с конца
        // TODO: сюда можно добавить возможность выбирать: тысяча или одна тысяча
        for (int i = nums.length - 1; i >= 0; i--) {
            if (nums[i] == 0) continue; // классы с нулём не отражаются на письме (*два миллиона ноль тысяч три)
            DeclensionBuilder baseDeclension = new DeclensionBuilder(d); // копируем исходные грамматические признаки
            if (i != 0) { // т.к. последний разряд согласуется с существительным, его хар-ки не меняем
                baseDeclension.animacy(Animacy.INANIMATE); // снимаем одушевлённость (убить *трёх тысячи)
                baseDeclension.count(Count.SINGULAR); // снимаем множественное число (*одни тысяча сто одни)
                if (i == 1) baseDeclension.gender(Gender.FEMININE); // слово тысяча женского рода
                else baseDeclension.gender(Gender.MASCULINE); // остальные классы мужского
            }
            res += getCardinalNumeral(nums[i], baseDeclension.build()); // склоняем три цифры
            if (i == 0) continue; // единицы-десятки-сотни не имеют слова, отражающего разряд
            // склоняем разряд
            res += " " + getCardinalNumeral((int)Math.pow(1000, i), Declension.supplementalDeclension(nums[i],
                    i == 1, baseDeclension.build())) + " "; // i == 1 => разряд - тысяча, женский род
        }
        return res.strip(); // убираем лишние пробелы в конце
    }

    /**
     * Вспомогательная функция, разбивает число на разряды. Важно: разряды начинаются с последнего (самый высокий в конце).
     * @param num число
     * @return массив с разрядами числа
     */
    private static int[] getBases(int num) {
        int baseCount = (int)Math.log10(num) / 3; // количество разрядов
        int[] nums = new int[baseCount + 1];
        for (int i = 0; i <= baseCount; i++) { // разделяем число на разряды, начиная с последнего (самый высокий будет в конце)
            nums[i] = num % 1000;
            num /= 1000;
        }
        return nums;
    }

    /**
     * Вспомогательная функция, выдаёт нужную форму числительного в винительном падеже в зависимости
     * от одушевлённости.
     * @param base основа числительного
     * @param endings набор окончаний
     * @param d грамматические признаки (одушевлённость)
     * @return число прописью
     */
    private static String getAccusativeForm(String base, String[] endings, Declension d) {
        if (d.animacy == null) throw new IllegalArgumentException("Missing animacy");
        // одушевлённое - окончание как у родительного падежа
        // неодушевлённое - как у именительного падежа
        if (d.animacy == Animacy.ANIMATE) return base + endings[Case.GENITIVE.ordinal()];
        return base + endings[Case.NOMINATIVE.ordinal()];
    }
}
