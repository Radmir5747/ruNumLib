package ru.radmirfar;

public class RussianNumeral {
    /**
     * <p>Грамматический род:</p>
     * <ul>
     * <li>{@link #MASCULINE} - мужской</li>
     * <li>{@link #FEMININE} - женский</li>
     * <li>{@link #NEUTRAL} - средний</li>
     * </ul>
     */
    public enum Gender {
        /**
         * Мужской род
         */
        MASCULINE,
        /**
         * Женский род
         */
        FEMININE,
        /**
         * Средний род
         */
        NEUTRAL
    }

    /**
     * <p>Падеж:</p>
     * <ul>
     * <li>{@link #NOMINATIVE} - именительный</li>
     * <li>{@link #GENITIVE} - родительный</li>
     * <li>{@link #DATIVE} - дательный</li>
     * <li>{@link #ACCUSATIVE} - винительный</li>
     * <li>{@link #INSTRUMENTAL} - творительный</li>
     * <li>{@link #PREPOSITIONAL} - предложный</li>
     * </ul>
     */
    public enum Case {
        /**
         * Именительный падеж
         */
        NOMINATIVE,
        /**
         * Родительный падеж
         */
        GENITIVE,
        /**
         * Дательный падеж
         */
        DATIVE,
        /**
         * Винительный падеж
         */
        ACCUSATIVE,
        /**
         * Творительный падеж
         */
        INSTRUMENTAL,
        /**
         * Предложный падеж
         */
        PREPOSITIONAL
    }

    /**
     * <p>Число (для числительных типа один, тысяча):</p>
     * <ul>
     * <li>{@link #SINGULAR} - единственное</li>
     * <li>{@link #PLURAL} - множественное</li>
     * </ul>
     */
    public enum Count {
        /**
         * Единственное число
         */
        SINGULAR,
        /**
         * Множественное число
         */
        PLURAL
    }

    /**
     * <p>Разряд числительного:</p>
     * <ul>
     * <li>{@link #CARDINAL} - количественное</li>
     * <li>{@link #ORDINAL} - порядковое</li>
     * </ul>
     */
    public enum Type {
        /**
         * Количественное числительное
         */
        CARDINAL,
        /**
         * Порядковое числительное
         */
        ORDINAL
    }

    /**
     * <p>Одушевлённость существительного, к которому относится числительное:</p>
     * <ul>
     * <li>{@link #ANIMATE} - одушевлённое</li>
     * <li>{@link #INANIMATE} - неодушевлённое</li>
     * </ul>
     */
    public enum Animacy {
        /**
         * Одушевлённое существительное
         */
        ANIMATE,
        /**
         * Неодушевлённое существительное
         */
        INANIMATE
    }
    //<editor-fold desc="Константы - окончания и основы">
    /**
     * <p>Окончания порядковых числительных с твёрдой основой. Индексы массива обозначают окончания:</p>
     * <ul>
     * <li>0 - мужского рода</li>
     * <li>1 - женского рода</li>
     * <li>2 - среднего рода</li>
     * <li>3 - множественного числа</li>
     * </ul>
     */
    private static final String[][] HARD_ORD_ENDINGS = {
            {"ый", "ого", "ому", "", "ым", "ом"}, /*2, 6, 8, 40 - именительный падеж - ой!*/
            {"ая", "ой", "ой", "ую", "ой", "ой"},
            {"ое", "ого", "ому", "ое", "ым", "ом"},
            {"ые", "ых", "ым", "", "ыми", "ых"}
    };
    /**
     * <p>Окончания порядковых числительных с мягкой основой. Индексы массива обозначают окончания:</p>
     * <ul>
     * <li>0 - мужского рода</li>
     * <li>1 - женского рода</li>
     * <li>2 - среднего рода</li>
     * <li>3 - множественного числа</li>
     * </ul>
     */
    private static final String[][] SOFT_ORD_ENDINGS = {
            {"ий", "ьего", "ьему", "", "ьим", "ьем"},
            {"ья", "ьей", "ьей", "ью", "ьей", "ьей"},
            {"ье", "ьего", "ьему", "ье", "ьим", "ьем"},
            {"ьи", "ьих", "ьим", "", "ьими", "ьих"}
    };
    /**
     * <p>Окончания собирательных числительных. Индексы массива обозначают окончания:</p>
     * <ul>
     * <li>0 - числительных с мягкой основой (двое, трое)</li>
     * <li>1 - числительных с твёрдой основой (все остальные)</li>
     * </ul>
     */
    private static final String[][] COLLECTIVE_NUM_ENDINGS = {
            {"е", "их", "им", "", "ими", "их"}, // мягкая основа
            {"о", "ых", "ым", "", "ыми", "ых"}  // твёрдая основа
    };
    /**
     * Основы собирательных числительных.
     */
    private static final String[] COLLECTIVE_NUMERAL_BASES = {"дво", "тро", "четвер", "пятер", "шестер", "семер", "восьмер", "девятер", "десятер"};
    /**
     * <p>Окончания числительных <i>оба / обе</i>. Индексы массива обозначают окончания:</p>
     * <ul>
     * <li>0 - числительного оба</li>
     * <li>1 - числительного обе</li>
     * </ul>
     */
    private static final String[][] BOTH_ENDINGS = {
            {"а", "оих", "оим", "", "оими", "оих"},
            {"е", "еих", "еим", "", "еими", "еих"}
    };
    /**
     * <p>Окончания числительного <i>тысяча</i>. Индексы массива обозначают окончания:</p>
     * <ul>
     * <li>0 - единственного числа</li>
     * <li>1 - множественного числа</li>
     * </ul>
     */
    private static final String[][] THOUSAND_CARD_ENDINGS = new String[][]{
            {"а", "и", "е", "у", "ей", "е"},
            {"и", "", "ам", "и", "ами", "ах"}
    };
    /**
     * <p>Окончания числительных <i>миллион</i>, <i>миллиард</i> и т. д. Индексы массива обозначают окончания:</p>
     * <ul>
     * <li>0 - единственного числа</li>
     * <li>1 - множественного числа</li>
     * </ul>
     */
    private static final String[][] MILLION_CARD_ENDINGS = {
            {"", "а", "у", "", "ом", "е"},
            {"ы", "ов", "ам", "ы", "ами", "ах"}
    };
    /**
     * <p>Окончания числительного <i>один</i>. Индексы массива обозначают окончания:</p>
     * <ul>
     * <li>0 - мужского рода</li>
     * <li>1 - женского рода</li>
     * <li>2 - среднего рода</li>
     * <li>3 - множественного числа</li>
     * </ul>
     */
    private static final String[][] ONE_CARD_ENDINGS = {
            {"ин", "ного", "ному", "", "ним", "ном"},
            {"на", "ной", "ной", "ну", "ной", "ной"},
            {"но", "ного", "ному", "но", "ним", "ном"},
            {"ни", "них", "ним", "", "ними", "них"}
    };
    /**
     * <p>Окончания числительных <i>два / две</i>, <i>три</i>, <i>четыре</i>.
     * Индексы массива обозначают окончания числительных:</p>
     * <ul>
     * <li>0 - два</li>
     * <li>1 - две</li>
     * <li>2 - три</li>
     * <li>3 - четыре</li>
     * </ul>
     */
    private static final String[][] TWO_FOUR_CARD_ENDINGS = {
            {"а", "ух", "ум", "", "умя", "ух"},
            {"е", "ух", "ум", "", "умя", "ух"},
            {"и", "ёх", "ём", "", "емя", "ёх"},
            {"е", "ёх", "ём", "", "ьмя", "ёх"}
    };
    /**
     * Основы числительных <i>два / две</i>, <i>три</i>, <i>четыре</i>.
     */
    private static final String[] TWO_FOUR_CARD_BASES = new String[]{"дв", "тр", "четыр"};
    /**
     * Склонение числительного <i>восемь</i>.
     */
    private static final String[] EIGHT_CARD_CASES = {"восемь", "восьми", "восьми", "восемь", "восемью", "восьми"};
    /**
     * Основы числительных <i>пять-двадцать</i>, <i>тридцать</i>.
     */
    private static final String[] FIVE_TWENTY_CARD_BASES = {"пят", "шест", "сем", "", "девят", "десят", "одиннадцат",
            "двенадцат", "тринадцат", "четырнадцат", "пятнадцат", "шестнадцат", "семнадцат",
            "восемнадцат", "девятнадцат", "двадцат", "тридцат"};
    /**
     * Окончания числительных <i>пять-двадцать</i>, <i>тридцать</i>.
     */
    private static final String[] FIVE_TWENTY_CARD_ENDINGS = {"ь", "и", "и", "ь", "ью", "и"};
    /**
     * Основы числительных <i>сорок</i>, <i>девяносто</i>, <i>сто</i>.
     */
    private static final String[] FORTY_NINETY_CARD_BASES = {"сорок", "девяност", "ст"};
    /**
     * Окончания косвенных падежей сложных числительных, оканчивающихся на <i>сто / ста / сти</i>
     */
    private static final String[] HUNDRED_CARD_ENDINGS = {"", "сот", "стам", "", "стами", "стах"};
    /**
     * Окончания именительного падежа падежей сложных числительных, оканчивающихся на <i>сто / ста / сти</i>
     */
    private static final String[] HUNDRED_CARD_NOM_ENDINGS = {"сти", "ста", "сот"};
    /**
     * Варианты числа 0 - ноль или нуль.
     */
    private static final String[] NULL_FORMS = {"нол", "нул"};
    /**
     * Основы порядковых числительных от 0 до 8.
     */
    private static final String[] ZERO_EIGHT_ORD_BASES = {"нулев", "перв", "втор", "трет", "четверт", "", "шест",
            "седьм", "восьм"};
    //</editor-fold>
    /**
     * Выдаёт число прописью.
     * @param num число
     * @param d грамматические признаки (тип числительного, род, падеж, грамматическое число, одушевлённость)
     * @return число прописью
     * @throws IllegalArgumentException если отсутствуют необходимые грамматические характеристики
     */
    public static String getNumeral(int num, Declension d) {
        if (d.type == null) throw new IllegalArgumentException("Missing type of numeral");
        boolean negative = false;
        if (num < 0) {
            negative = true;
            num *= -1;
        }
        if (d.type == Type.CARDINAL) return (negative ? "минус " : "") + getCardinalNumeral(num, d);
        return (negative ? "минус " : "") + getOrdinalNumeral(num, d);
    }

    /* дробные числительные бывают только количественными */
    /**
     * <p>Выдаёт дробное количественное числительное в нужном падеже.</p>
     * <p>При отсутствии падежа выдаёт исключение.</p>
     * @param num десятичная дробь
     * @param d грамматические признаки (падеж)
     * @return число прописью
     * @throws IllegalArgumentException если отсутствует падеж
     */
    public static String getNumeral(double num, Declension d) {
        if (d.gramCase == null) throw new IllegalArgumentException("Missing grammatical case");
        String res = "";
        return res;
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
        if (d.gramCase == null) throw new IllegalArgumentException("Missing grammatical case");
        String res = "";
        return res;
    }

    /**
     * <p>Выдаёт количественное собирательное числительное прописью в нужной форме.</p>
     * <p>Необходимо передать падеж и одушевлённость существительного, к которому относится
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
    public static String getCollectiveNumeral(int num, Declension d) {
        if (d.gramCase == null) throw new IllegalArgumentException("Missing grammatical case");
        if (num < 2 || num > 10) throw new IllegalArgumentException("Only numbers from 2 to 10 are supported");
        String res = COLLECTIVE_NUMERAL_BASES[num - 2];
        int base = num < 4 ? 0 : 1; // двое, трое - мягкая основа
        if (d.gramCase == Case.ACCUSATIVE) { // для винительного падежа учитываем одушевлённость
            return modifyForAnimacy(res, COLLECTIVE_NUM_ENDINGS[base], d);
        }
        return res + COLLECTIVE_NUM_ENDINGS[base][d.gramCase.ordinal()];
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
        if (d.gramCase == null) throw new IllegalArgumentException("Missing grammatical case");
        int base = d.gender == Gender.FEMININE ? 1 : 0; // в зависимости от рода используем набор окончаний
        if (d.gramCase == Case.ACCUSATIVE) { // для винительного падежа учитываем одушевлённость
            return modifyForAnimacy("об", BOTH_ENDINGS[base], d);
        }
        return "об" + BOTH_ENDINGS[base][d.gramCase.ordinal()];
    }

    /**
     * <p>Выдаёт необходимую форму числительного <i>полтора</i> в зависимости от грамматического рода и падежа.</p>
     * <p>Необходимо передать падеж и род, иначе выдаёт исключение.</p>
     * @param d грамматические характеристики числительного (род, падеж)
     * @return числительное <i>полтора</i> в нужной форме
     * @throws IllegalArgumentException если отсутствуют необходимые грамматические характеристики
     */
    public static String getOneAndAHalf(Declension d) {
        if (d.gender == null) throw new IllegalArgumentException("Missing gender");
        if (d.gramCase == null) throw new IllegalArgumentException("Missing grammatical case");
        if (d.isNomAcc()) { // в именительном и винительном падеже
            if (d.gender == Gender.FEMININE) return "полторы"; // у женского рода полторы
            return "полтора"; // у мужского и среднего рода полтора
        }
        return "полутора"; // в остальных падежах полутора
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
     * Выдаёт порядковое числительное в нужной форме.
     * @param num число
     * @param d грамматические признаки (род, падеж, грамматическое число, одушевлённость)
     * @return число прописью
     * @throws IllegalArgumentException если отсутствуют необходимые грамматические характеристики
     */
    private static String getOrdinalNumeral(int num, Declension d) {
        if (d.gender == null) throw new IllegalArgumentException("Missing gender");
        if (d.gramCase == null) throw new IllegalArgumentException("Missing grammatical case");
        if (d.count == null) throw new IllegalArgumentException("Missing grammatical count");
        String res = "";
        int ending = d.count == Count.PLURAL ? 3 : d.gender.ordinal(); // выбираем набор окончаний
        if (num == 3) {
            // для винительного падежа у мужского рода и множественного числа учитываем одушевлённость
            if (d.adjCheck()) return modifyForAnimacy(ZERO_EIGHT_ORD_BASES[3], SOFT_ORD_ENDINGS[ending], d);
            return ZERO_EIGHT_ORD_BASES[3] + SOFT_ORD_ENDINGS[ending][d.gramCase.ordinal()];
        }
        return res;
    }
    /**
     * Выдаёт количественное числительное в нужной форме.
     * @param num число
     * @param d грамматические признаки (род, падеж, грамматическое число, одушевлённость)
     * @return число прописью
     * @throws IllegalArgumentException если отсутствуют необходимые грамматические характеристики
     */
    private static String getCardinalNumeral(int num, Declension d) {
        if (d.gramCase == null) throw new IllegalArgumentException("Missing grammatical case");
        String res = "";
        if (num == 1000) {
            if (d.count == null) throw new IllegalArgumentException("Missing grammatical count");
            return "тысяч" + THOUSAND_CARD_ENDINGS[d.count.ordinal()][d.gramCase.ordinal()];
        }
        if (num == 10e5 || num == 10e8) {
            if (d.count == null) throw new IllegalArgumentException("Missing grammatical count");
            res = num == 10e5 ? "миллион" : "миллиард";
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
            if (d.adjCheck()) return modifyForAnimacy(res, ONE_CARD_ENDINGS[ending], d);
            return res + ONE_CARD_ENDINGS[ending][d.gramCase.ordinal()];
        }
        if (num > 1 && num < 5) { // 2, 3, 4
            if (num == 2 && d.gender == null) throw new IllegalArgumentException("Missing gender");
            int ending = num - 1;
            if (num == 2) ending = d.gender == Gender.FEMININE ? 1 : 0; // учитываем род для числ. 2
            if (d.gramCase == Case.ACCUSATIVE) { // у винительного падежа учитываем одушевлённость
                return modifyForAnimacy(TWO_FOUR_CARD_BASES[num - 2], TWO_FOUR_CARD_ENDINGS[ending], d);
            }
            return TWO_FOUR_CARD_BASES[num - 2] + TWO_FOUR_CARD_ENDINGS[ending][d.gramCase.ordinal()];
        }
        if ((num > 4 && num < 21) || (num == 30)) { // 5 - 20, 30
            if (num == 8) return EIGHT_CARD_CASES[d.gramCase.ordinal()]; // у числительного восемь учитываем беглую гласную
            int base = num == 30 ? 16 : num - 5;
            return FIVE_TWENTY_CARD_BASES[base] + FIVE_TWENTY_CARD_ENDINGS[d.gramCase.ordinal()];
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
            // составное количественное числительное управляет существительным: люблю двести семьдесят три ученика
            // НО люблю двести семьдесят одного ученика
            // https://gramota.ru/spravka/vopros/322608
            Declension newDeclension = num % 10 == 1 ? d : new DeclensionBuilder(d).animacy(Animacy.INANIMATE).build();
            return getCardinalNumeral(num / 10 * 10, d) + " " + getCardinalNumeral(num % 10, newDeclension);
        }
        if (num > 100 && num < 1000) { // составные числительные для трёхзначных чисел
            return getCardinalNumeral(num / 100 * 100, d) + " " + getCardinalNumeral(num % 100, d);
        }
        // разбиваем числа больше 999 на разряды
        int baseCount = (int)Math.log10(num) / 3; // количество разрядов
        Integer[] nums = new Integer[baseCount + 1];
        for (int i = 0; i <= baseCount; i++) { // разделяем число на разряды, начиная с последнего (самый высокий будет в конце)
            nums[i] = num % 1000;
            num /= 1000;
        }
        // идём с конца
        for (int i = baseCount; i >= 0; i--) {
            if (nums[i] == 0) continue; // разряды с нулём не отражаются на письме (*ноль тысяч девятнадцать)
            DeclensionBuilder baseDeclension = new DeclensionBuilder(d); // копируем исходные грамматические признаки
            if (i != 0) { // последний разряд согласуется с существительным
                baseDeclension.animacy(Animacy.INANIMATE); // снимаем одушевлённость (убить *трёх тысячи)
                if (i == 1) baseDeclension.gender(Gender.FEMININE); // слово тысяча женского рода
                else baseDeclension.gender(Gender.MASCULINE);
            }
            res += getCardinalNumeral(nums[i], baseDeclension.build()); // склоняем три цифры
            if (i == 0) continue; // единицы-десятки-сотни не имеют слова, отражающего разряд
            // склоняем разряд
            res += " " + getCardinalNumeral((int)Math.pow(1000, i), supplementalDeclension(nums[i], i == 1,
                            new DeclensionBuilder(d).build())) + " ";
        }
        return res;
    }

    /**
     * Вспомогательная функция, выдаёт нужную форму числительного в винительном падеже в зависимости
     * от одушевлённости.
     * @param base основа числительного
     * @param endings набор окончаний
     * @param d грамматические признаки (одушевлённость)
     * @return число прописью
     */
    private static String modifyForAnimacy(String base, String[] endings, Declension d) {
        if (d.animacy == null) throw new IllegalArgumentException("Missing animacy");
        // одушевлённое - окончание как у родительного падежа
        // неодушевлённое - как у именительного падежа
        if (d.animacy == Animacy.ANIMATE) return base + endings[Case.GENITIVE.ordinal()];
        return base + endings[Case.NOMINATIVE.ordinal()];
    }

    /**
     * <p>Вспомогательная функция, выдаёт набор грамматических характеристик для согласования разрядов в
     * числительных типа одна тысяча, пять миллионов и проч.</p>
     * @param num число, отражающее три цифры перед разрядом
     * @param d грамматические характеристики исходного числа
     * @param isThousand является ли разряд тысячей
     * @return грамматические характеристики разряда
     */
    private static Declension supplementalDeclension(int num, boolean isThousand, Declension d) {
        num %= 100; // приводим число к двузначному
        DeclensionBuilder out = new DeclensionBuilder(d.gramCase).type(Type.CARDINAL);
        if (num == 0) {
            // ноль управляет словами, ставя их в форму родительного падежа множественного числа:
            // ноль тысяч, нуля тысяч, нулю тысяч, нулём тысяч, о нуле тысяч
            out.count(Count.PLURAL).gramCase(Case.GENITIVE);
        } else if (num % 10 == 1 && num != 11) {
            // если число оканчивается на 1, то оно ставит слово в форму единственного числа
            out.count(Count.SINGULAR);
        } else if ((num % 10 == 2 && num != 12) || (num % 10 == 3 && num != 13) || (num % 10 == 4 && num != 14)) {
            // числа, оканчивающиеся на 2, 3, 4 кроме чисел 12, 13, 14
            // ставим слово в форму множественного числа
            out.count(Count.PLURAL);
            // для числительных миллион, миллиард и т.п. именительный и винительный падеж принимают форму
            // родительного падежа единственного числа: два миллиона (не *два миллионы)
            if (d.isNomAcc() && !isThousand) {
                out.count(Count.SINGULAR);
                out.gramCase(Case.GENITIVE);
            }
        }
        else {
            out.count(Count.PLURAL);
            // именительный и винительный падеж принимают форму родительного падежа множественного числа:
            // пять тысяч, миллионов (не *пять тысячи, *пять миллионы)
            if (d.isNomAcc()) {
                out.gramCase(Case.GENITIVE);
            }
        }
        return out.build();
    }

    /**
     * <p>Класс, описывающий грамматические признаки числительного:
     * {@link Gender грамматический род}, {@link Case падеж},
     * {@link Count грамматическое число} (для числительных типа один, тысяча),
     * {@link Type разряд}, {@link Animacy одушевлённость существительного, к которому относится числительное}.</p>
     * <p>Объект этого класса передаётся как аргумент в функции, выдающие числительное.</p>
     */
    public static class Declension {
        private final Gender gender;
        private final Case gramCase;
        private final Count count;
        private final Type type;
        private final Animacy animacy;

        /**
         * Выдаёт грамматический род числительного.
         * @return род
         */
        public Gender getGender() {
            return gender;
        }

        /**
         * Выдаёт падеж числительного.
         * @return падеж
         */
        public Case getGramCase() {
            return gramCase;
        }

        /**
         * Выдаёт грамматическое число (для числительных типа один, тысяча).
         * @return грамматическое число
         */
        public Count getCount() {
            return count;
        }

        /**
         * Выдаёт разряд числительного.
         * @return разряд
         */
        public Type getType() {
            return type;
        }

        /**
         * Выдаёт одушевлённость существительного, к которому относится числительное.
         * @return одушевлённость
         */
        public Animacy getAnimacy() {
            return animacy;
        }

        /**
         * Конструктор.
         * @param gender род
         * @param gramCase падеж
         * @param count грамматическое число
         * @param type разряд
         * @param animacy одушевлённость
         */
        private Declension(Gender gender, Case gramCase, Count count, Type type, Animacy animacy) {
            this.gender = gender;
            this.gramCase = gramCase;
            this.count = count;
            this.type = type;
            this.animacy = animacy;
        }

        /**
         * <p>Вспомогательная функция для сокращения условий.</p>
         * @return true, если падеж именительный или винительный
         */
        private boolean isNomAcc() {
            return gramCase == Case.NOMINATIVE || gramCase == Case.ACCUSATIVE;
        }

        /**
         * <p>Вспомогательная функция для сокращения условий
         * (для учёта одушевлённости в числительных, подобных прилагательным).</p>
         * @return true, если падеж винительный, а также мужской род или множественное число.
         */
        private boolean adjCheck() {
            return gramCase == Case.ACCUSATIVE && (gender == Gender.MASCULINE || count == Count.PLURAL);
        }
    }

    /**
     * Класс для указания грамматических признаков числительного.
     * @see Declension
     */
    public static class DeclensionBuilder {
        private Gender _gender = null;
        private Case _gramCase;
        private Count _count = null;
        private Type _type = null;
        private Animacy _animacy = null;

        /**
         * Конструктор. Обязательный параметр - падеж (как единственный общий признак у количественных и порядковых числительных).
         * @param _gramCase падеж
         */
        public DeclensionBuilder(Case _gramCase) {
            this._gramCase = _gramCase;
        }

        /**
         * Конструктор копирования, принимает аргументом объект класса {@link Declension}.
         * @param d грамматические характеристики
         */
        public DeclensionBuilder(Declension d) {
            this._gender = d.gender;
            this._gramCase = d.gramCase;
            this._count = d.count;
            this._type = d.type;
            this._animacy = d.animacy;
        }

        /**
         * Получить грамматические признаки числительного.
         * @return объект класса {@link Declension}.
         */
        public Declension build() {
            return new Declension(_gender, _gramCase, _count, _type, _animacy);
        }

        /**
         * Указать грамматический род числительного.
         * @param _gender род
         */
        public DeclensionBuilder gender(Gender _gender) {
            this._gender = _gender;
            return this;
        }

        /**
         * Указать падеж числительного.
         * @param _gramCase падеж
         */
        public DeclensionBuilder gramCase(Case _gramCase) {
            this._gramCase = _gramCase;
            return this;
        }

        /**
         * Указать грамматическое число (для числительных типа один, тысяча).
         * @param _count грамматическое число
         */
        public DeclensionBuilder count(Count _count) {
            this._count = _count;
            return this;
        }

        /**
         * Указать разряд числительного.
         * @param _type разряд
         */
        public DeclensionBuilder type(Type _type) {
            this._type = _type;
            return this;
        }

        /**
         * Указать одушевлённость существительного, к которому относится числительное.
         * @param _animacy одушевлённость
         */
        public DeclensionBuilder animacy(Animacy _animacy) {
            this._animacy = _animacy;
            return this;
        }
    }
    /**
     * <p>Дробь. Не предоставляет возможности проводить какие-либо операции, служит исключительно как "обёртка".</p>
     * <p>Поддерживаются простые (2/3, 5/2) и смешанные (1 2/3) дроби, в т.ч. и отрицательные.</p>
     */
    public static class Fraction {
        int whole, numerator, denominator; // целая часть, числитель, знаменатель

        /**
         * Геттер числителя дроби
         * @return числитель
         */
        public int getNumerator() {
            return numerator;
        }

        /**
         * Сеттер числителя дроби
         * @param numerator числитель
         */
        public void setNumerator(int numerator) {
            this.numerator = numerator;
            prepareFraction();
        }

        /**
         * Геттер знаменателя дроби
         * @return знаменатель
         */
        public int getDenominator() {
            return denominator;
        }

        /**
         * Сеттер знаменателя дроби. Выдаёт исключение, если попытаться задать значение 0.
         * @param denominator знаменатель
         * @throws ArithmeticException если знаменатель равен нулю
         */
        public void setDenominator(int denominator) {
            if (denominator == 0) throw new ArithmeticException("/ by zero"); // знаменатель не может равняться нулю
            this.denominator = denominator;
            prepareFraction();
        }

        /**
         * Геттер целой части смешанной дроби
         * @return целое
         */
        public int getWhole() {
            return whole;
        }

        /**
         * Сеттер целой части смешанной дроби
         * @param whole целое
         */
        public void setWhole(int whole) {
            this.whole = whole;
            prepareFraction();
        }

        /**
         * Приведение дроби к единому виду для корректности выполнения функций.
         */
        private void prepareFraction() {
            /* имеется целая часть и числитель / знаменатель отрицательный */
            if (whole != 0 && (numerator < 0 || denominator < 0)) {
                whole *= -1; // переносим минус на целую часть
                /* убираем минусы у числителя / знаменателя */
                if (numerator < 0) numerator *= -1;
                if (denominator < 0) denominator *= -1;
            }
            /*
             * Нет целой части и отрицательный знаменатель - переносим минус в числитель.
             * Почему это работает? Если знаменатель меньше нуля и:
             * - числитель меньше нуля - дробь становится положительной
             * - числитель больше нуля - знаки числителя и знаменателя меняются местами
             * */
            else if (denominator < 0) {
                numerator *= -1;
                denominator *= -1;
            }
        }

        /**
         * Конструктор для простой дроби
         * @param numerator числитель
         * @param denominator знаменатель
         */
        public Fraction(int numerator, int denominator) {
            setNumerator(numerator);
            setDenominator(denominator);
            setWhole(0);
        }

        /**
         * Конструктор для смешанной дроби
         * @param whole целая часть
         * @param numerator числитель
         * @param denominator знаменатель
         */
        public Fraction(int whole, int numerator, int denominator) {
            setNumerator(numerator);
            setDenominator(denominator);
            setWhole(whole);
        }
    }
}
