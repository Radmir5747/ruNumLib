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
        if (d.gramCase == null) throw new IllegalArgumentException("Insufficient arguments");
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
        if (d.gramCase == null) throw new IllegalArgumentException("Insufficient arguments");
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
        String res = "";
        String[][] endings = new String[2][6];
        endings[0] = new String[]{"е", "их", "им", "", "ими", "их"}; // мягкая основа
        endings[1] = new String[]{"о", "ых", "ым", "", "ыми", "ых"}; // твёрдая основа
        String[] numerals = new String[]{"дво", "тро", "четвер", "пятер", "шестер", "семер", "восьмер", "девятер", "десятер"};
        res = numerals[num - 2];
        int base = num < 4 ? 0 : 1; // двое, трое - мягкая основа
        if (d.gramCase == Case.ACCUSATIVE) { // для винительного падежа учитываем одушевлённость
            return modifyForAnimacy(res, endings[base], d);
        }
        return res + endings[base][d.gramCase.ordinal()];
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
        if (d.gender == null || d.gramCase == null)
            throw new IllegalArgumentException("Missing gender and/or grammatical case");
        String res = "об";
        String[][] endings = new String[2][6];
        endings[0] = new String[] {"а", "оих", "оим", "", "оими", "оих"};
        endings[1] = new String[] {"е", "еих", "еим", "", "еими", "еих"};
        int base = d.gender == Gender.FEMININE ? 1 : 0;
        if (d.gramCase == Case.ACCUSATIVE) { // для винительного падежа учитываем одушевлённость
            return modifyForAnimacy(res, endings[base], d);
        }
        return res + endings[base][d.gramCase.ordinal()];
    }

    /**
     * <p>Выдаёт необходимую форму числительного <i>полтора</i> в зависимости от грамматического рода и падежа.</p>
     * <p>Необходимо передать падеж и род, иначе выдаёт исключение.</p>
     * @param d грамматические характеристики числительного (род, падеж)
     * @return числительное <i>полтора</i> в нужной форме
     * @throws IllegalArgumentException если отсутствуют необходимые грамматические характеристики
     */
    public static String getOneAndAHalf(Declension d) {
        String[] numerals = new String[] {"полтора", "полторы", "полутора"};
        if (d.gramCase == null || d.gender == null)
            throw new IllegalArgumentException("Missing gender and/or grammatical case");
        if (d.gramCase == Case.NOMINATIVE || d.gramCase == Case.ACCUSATIVE) {
            if (d.gender == Gender.FEMININE) return numerals[1];
            return numerals[0];
        }
        return numerals[2];
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
     * @param d грамматические признаки (род, падеж, грамматическое число)
     * @return число прописью
     * @throws IllegalArgumentException если отсутствуют необходимые грамматические характеристики
     */
    private static String getOrdinalNumeral(int num, Declension d) {
        if (d.gender == null || d.gramCase == null || d.count == null)
            throw new IllegalArgumentException("Insufficient arguments");
        String res = "";
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
            String base = "тысяч";
            String[][] endings = new String[2][6];
            endings[0] = new String[]{"а", "и", "е", "у", "ей", "е"};
            endings[1] = new String[]{"и", "", "ам", "и", "ами", "ах"};
            return base + endings[d.count.ordinal()][d.gramCase.ordinal()];
        }
        if (num == 10e5 || num == 10e8) {
            if (d.count == null) throw new IllegalArgumentException("Missing grammatical count");
            String base = num == 10e5 ? "миллион" : "миллиард";
            String[][] endings = new String[2][6];
            endings[0] = new String[]{"", "а", "у", "", "ом", "е"};
            endings[1] = new String[]{"ы", "ов", "ам", "ы", "ами", "ах"};
            return base + endings[d.count.ordinal()][d.gramCase.ordinal()];
        }
        if (num == 1) {
            if (d.count == null || d.gender == null) throw new IllegalArgumentException("Missing grammatical count " +
                    "and/or gender");
            String base = "од";
            String[][] endings = new String[4][6];
            endings[0] = new String[]{"ин", "ного", "ному", "", "ним", "ном"};
            endings[1] = new String[]{"на", "ной", "ной", "ну", "ной", "ной"};
            endings[2] = new String[]{"но", "ного", "ному", "но", "ним", "ном"};
            endings[3] = new String[]{"ни", "них", "ним", "", "ними", "них"};
            int ending = d.count == Count.PLURAL ? 3 : d.gender.ordinal();
            if (d.gramCase == Case.ACCUSATIVE && (d.gender == Gender.MASCULINE || d.count == Count.PLURAL)) {
                return modifyForAnimacy(base, endings[ending], d);
            }
            return base + endings[ending][d.gramCase.ordinal()];
        }
        if (num > 1 && num < 5) { // 2, 3, 4
            if (num == 2 && d.gender == null) throw new IllegalArgumentException("Missing gender");
            String[] numerals = new String[]{"дв", "тр", "четыр"};
            String[][] endings = new String[4][6];
            endings[0] = new String[]{"а", "ух", "ум", "", "умя", "ух"};
            endings[1] = new String[]{"е", "ух", "ум", "", "умя", "ух"};
            endings[2] = new String[]{"и", "ёх", "ём", "", "емя", "ёх"};
            endings[3] = new String[]{"е", "ёх", "ём", "", "ьмя", "ёх"};
            int ending = num - 1;
            if (num == 2) ending = d.gender == Gender.FEMININE ? 1 : 0; // учитываем род для числ. 2
            if (d.gramCase == Case.ACCUSATIVE) {
                return modifyForAnimacy(numerals[num - 2], endings[ending], d);
            }
            return numerals[num - 2] + endings[ending][d.gramCase.ordinal()];
        }
        if ((num > 4 && num < 21) || (num == 30)) { // 5 - 20, 30
            String[] eightCases = new String[] {"восемь", "восьми", "восьми", "восемь", "восемью", "восьми"};
            // у числительного восемь учитываем беглую гласную
            if (num == 8) return eightCases[d.gramCase.ordinal()];
            String[] numerals = new String[] {"пят", "шест", "сем", "", "девят", "десят", "одиннадцат",
                    "двенадцат", "тринадцат", "четырнадцат", "пятнадцат", "шестнадцат", "семнадцат",
                    "восемнадцат", "девятнадцат", "двадцат", "тридцат"};
            String[] endings = {"ь", "и", "и", "ь", "ью", "и"};
            int base = num == 30 ? 16 : num - 5;
            return numerals[base] + endings[d.gramCase.ordinal()];
        }
        if (num == 50 || num == 60 || num == 70 || num == 80) {
            String[] endings = new String[] {"", "и", "и", "", "ью", "и"};
            return getCardinalNumeral(num / 10, d) + "десят" + endings[d.gramCase.ordinal()];
        }
        if (num == 40 || num == 90 || num == 100) {
            String[] numerals = new String[] {"сорок", "девяност", "ст"};
            String[][] endings = new String[2][6];
            endings[0] = new String[] {"", "а"};
            endings[1] = new String[] {"о", "а"};
            int numeral = 0;
            if (num == 90) numeral = 1;
            else if (num == 100) numeral = 2;
            int base = num == 40 ? 0 : 1;
            int ending = (d.gramCase == Case.NOMINATIVE || d.gramCase == Case.ACCUSATIVE) ? 0 : 1;
            return numerals[numeral] + endings[base][ending];
        }
        if (num == 200 || num == 300 || num == 400 || num == 500 || num == 600 || num == 700 || num == 800 || num == 900) {
            Declension first_d = new DeclensionBuilder(d.gramCase)
                    .animacy(Animacy.INANIMATE).gender(Gender.FEMININE).build();
            String[] endings = new String[] {"", "сот", "стам", "", "стами", "стах"};
            String[] nominativeEndings = new String[] {"сти", "ста", "сот"};
            res = getCardinalNumeral(num / 100, first_d);
            if (d.gramCase == Case.NOMINATIVE || d.gramCase == Case.ACCUSATIVE) {
                int i = 2;
                if (num == 200) i = 0;
                else if (num == 300 || num == 400) i = 1;
                return res + nominativeEndings[i];
            }
            return res + endings[d.gramCase.ordinal()];
        }
        if (num > 20 && num < 100) { // составные числительные для двузначных чисел
            // снимаем одушевлённость с числительных один-четыре
            // составное количественное числительное управляет существительным: люблю двести семьдесят три ученика
            // https://gramota.ru/spravka/vopros/322608
            return getCardinalNumeral(num / 10 * 10, d) + " " + getCardinalNumeral(num % 10,
                    new DeclensionBuilder(d).animacy(Animacy.INANIMATE).build());
        }
        if (num > 100 && num < 1000) { // составные числительные для трёхзначных чисел
            return getCardinalNumeral(num / 100 * 100, d) + " " + getCardinalNumeral(num % 100, d);
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
