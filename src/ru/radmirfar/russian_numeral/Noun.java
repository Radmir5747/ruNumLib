package ru.radmirfar.russian_numeral;

/**
 * Класс, описывающий имя существительное.
 */
public class Noun {
    /**
     * Заданы ли отдельные формы для чисел 2-4
     */
    boolean usePaucalForms;
    /**
     * Род существительного
     */
    Gender gender;
    /**
     * Одушевлённость существительного
     */
    Animacy animacy;
    /**
     * Падежные формы единственного числа
     */
    String[] singularForms;
    /**
     * Падежные формы для чисел 2-4
     */
    String[] paucalForms;
    /**
     * Падежные формы множественного числа
     */
    String[] pluralForms;
    /**
     * Существительное может быть только во множественном числе (брюки, вилы).
     */
    boolean pluraliaTantum = false;
    /**
     * Конструктор для существительных, которые употребляются как в единственном, так и во множественном числе.
     * @param gender род существительного
     * @param animacy одушевлённость существительного
     * @param singularForms массив с формами единственного числа
     * @param pluralForms массив с формами множественного числа
     */
    public Noun(Gender gender, Animacy animacy, String[] singularForms, String[] pluralForms) {
        this.gender = gender;
        this.animacy = animacy;
        this.singularForms = singularForms;
        this.pluralForms = pluralForms;
    }
    /**
     * Конструктор для существительных, которые употребляются только во множественном числе (pluralia tantum).
     * @param animacy одушевлённость существительного
     * @param singularForms массив с формами единственного числа
     * @param pluralForms массив с формами множественного числа
     */
    public Noun(Animacy animacy, String[] singularForms, String[] pluralForms) {
        this.animacy = animacy;
        this.singularForms = singularForms;
        this.pluralForms = pluralForms;
        pluraliaTantum = true;
    }

    /**
     * Задаёт отдельные падежные формы для чисел 2-4
     * @param paucalForms массив с падежными формами
     */
    public void setPaucalForms(String[] paucalForms) {
        usePaucalForms = true;
        this.paucalForms = paucalForms;
    }

    /**
     * <p>Выдаёт падежную форму существительного в зависимости от падежа и грамматического числа.</p>
     * <p>При отсутствии падежа и числа выдаёт исключение.</p>
     * @param d грамматические характеристики (падеж, грамматическое число)
     * @return падежная форма существительного
     * @throws IllegalArgumentException если отсутствует падеж или грамматическое число
     */
    public String getCaseForm(Declension d) {
        if (d.count == null) throw new IllegalArgumentException("Missing count");
        if (d.count == Count.SINGULAR) return singularForms[d.gramCase.ordinal()];
        return pluralForms[d.gramCase.ordinal()];
    }
    /**
     * Указывает, употребляется ли существительное исключительно во множественном числе (как слово <i>брюки</i>)
     * @param pluraliaTantum true, если употребляется, в ином случае - false
     */
    public void setPluraliaTantum(boolean pluraliaTantum) {
        this.pluraliaTantum = pluraliaTantum;
    }

    // заметка себе: одни очки, двое-четверо очков, пять-двадцать очков, двадцать одни очки, а дальше никак
    // https://gramota.ru/poisk?query=%D0%BD%D0%BE%D0%B6%D0%BD%D0%B8%D1%86&mode=spravka
    // и https://gramota.ru/spravka/vopros/320812#question
    // НО в косвенных падежах https://gramota.ru/spravka/vopros/259034#question
}