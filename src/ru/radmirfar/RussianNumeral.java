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
     * @param gender род
     * @param gramCase падеж
     * @param type разряд числительного
     * @param count грамматическое число
     * @return число прописью
     */
    public static String getNumeral(int num, Gender gender, Case gramCase, Type type, Count count, Animacy animacy) {
        if (type == Type.CARDINAL) return getCardinalNumeral(num, gender, gramCase, count, animacy);
        return getOrdinalNumeral(num, gender, gramCase, count);
    }

    /* дробные числительные бывают только количественными */
    /**
     * Выдаёт дробное количественное числительное в нужном падеже.
     * @param num десятичная дробь
     * @param gramCase падеж
     * @return число прописью
     */
    public static String getNumeral(double num, Case gramCase) {
        String res = "";
        return res;
    }

    /**
     * Выдаёт дробное количественное числительное в нужном падеже.
     * @param num простая или смешанная дробь
     * @param gramCase падеж
     * @return число прописью
     */
    public static String getNumeral(Fraction num, Case gramCase) {
        String res = "";
        return res;
    }

    /**
     * <p>Выдаёт количественное собирательное числительное прописью в нужной форме.</p>
     * <p>При передаче числа, не принадлежащего интервалу [2;10], выдаёт исключение
     * (т.к. собирательные числительные больше десяти не употребляются,
     * а меньше двух не бывают).</p>
     *
     * @param num число от 2 до 10
     * @param gramCase падеж
     * @param animacy одушевлённость существительного, к которому относится числительное
     * @throws IllegalArgumentException если число меньше 2 или больше 10
     * @return число прописью
     */
    public static String getCollectiveNumeral(int num, Case gramCase, Animacy animacy) {
        if (num < 2 || num > 10) throw new IllegalArgumentException("Only numbers from 2 to 10 are supported");
        String res = "";
        String[][] endings = new String[2][6];
        endings[0] = new String[]{"е", "их", "им", "", "ими", "их"}; // мягкая основа
        endings[1] = new String[]{"о", "ых", "ым", "", "ыми", "ых"}; // твёрдая основа
        String[] numerals = new String[]{"дво", "тро", "четвер", "пятер", "шестер", "семер", "восьмер", "девятер", "десятер"};
        res = numerals[num - 2];
        int base = num < 4 ? 0 : 1; // двое, трое - мягкая основа
        if (gramCase == Case.ACCUSATIVE) { // для винительного падежа учитываем одушевлённость
            // одушевлённое - окончание как у родительного падежа
            // неодушевлённое - как у именительного падежа
            if (animacy == Animacy.ANIMATE) return res + endings[base][Case.GENITIVE.ordinal()];
            return res + endings[base][Case.NOMINATIVE.ordinal()];
        }
        return res + endings[base][gramCase.ordinal()];
    }

    /**
     * Выдаёт необходимую форму числительного <i>оба</i> в зависимости от грамматического рода,
     * падежа и одушевлённости существительного, к которому относится числительное.
     * @param gender род
     * @param gramCase падеж
     * @param animacy одушевлённость существительного, к которому относится числительное
     * @return числительное <i>оба</i> в нужной форме
     */
    public static String getBoth(Gender gender, Case gramCase, Animacy animacy) {
        String res = "об";
        String[][] endings = new String[2][6];
        endings[0] = new String[] {"а", "оих", "оим", "", "оими", "оих"};
        endings[1] = new String[] {"е", "еих", "еим", "", "еими", "еих"};
        int base = gender == Gender.FEMININE ? 1 : 0;
        if (gramCase == Case.ACCUSATIVE) { // для винительного падежа учитываем одушевлённость
            if (animacy == Animacy.ANIMATE) return res + endings[base][Case.GENITIVE.ordinal()];
            return res + endings[base][Case.NOMINATIVE.ordinal()];
        }
        return res + endings[base][gramCase.ordinal()];
    }
    /**
     * Выдаёт порядковое числительное в нужной форме.
     * @param num число
     * @param gender род
     * @param gramCase падеж
     * @param count грамматическое число
     * @return число прописью
     */
    private static String getOrdinalNumeral(int num, Gender gender, Case gramCase, Count count) {
        String res = "";
        return res;
    }
    /**
     * Выдаёт количественное числительное в нужной форме.
     * @param num число
     * @param gender род
     * @param gramCase падеж
     * @param count грамматическое число
     * @return число прописью
     */
    private static String getCardinalNumeral(int num, Gender gender, Case gramCase, Count count, Animacy animacy) {
        String res = "";
        if (num == 1000) {
            String base = "тысяч";
            String[][] endings = new String[2][6];
            endings[0] = new String[]{"а", "и", "е", "у", "ей", "е"};
            endings[1] = new String[]{"и", "", "ам", "и", "ами", "ах"};
            return base + endings[count.ordinal()][gramCase.ordinal()];
        }
        if (num == 10e5 || num == 10e8) {
            String base = num == 10e5 ? "миллион" : "миллиард";
            String[][] endings = new String[2][6];
            endings[0] = new String[]{"", "а", "у", "", "ом", "е"};
            endings[1] = new String[]{"ы", "ов", "ам", "ы", "ами", "ах"};
            return base + endings[count.ordinal()][gramCase.ordinal()];
        }
        if (num == 1) {
            String base = "од";
            String[][] endings = new String[4][6];
            endings[0] = new String[]{"ин", "ного", "ому", "", "ним", "ном"};
            endings[1] = new String[]{"на", "ной", "ной", "ну", "ной", "ной"};
            endings[2] = new String[]{"но", "ного", "ному", "но", "ним", "ном"};
            endings[3] = new String[]{"ни", "них", "ним", "", "ними", "них"};
            int ending = count == Count.PLURAL ? 3 : gender.ordinal();
            if (gramCase == Case.ACCUSATIVE && (gender == Gender.MASCULINE || count == Count.PLURAL)) {
                if (animacy == Animacy.ANIMATE) return base + endings[ending][Case.GENITIVE.ordinal()];
                return base + endings[ending][Case.NOMINATIVE.ordinal()];
            }
            return base + endings[ending][gramCase.ordinal()];
        }
        return res;
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
