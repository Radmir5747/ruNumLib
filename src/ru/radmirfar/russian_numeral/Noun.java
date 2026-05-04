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
     * Конструктор.
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
     * Задаёт отдельные падежные формы для чисел 2-4
     * @param paucalForms массив с падежными формами
     */
    public void setPaucalForms(String[] paucalForms) {
        usePaucalForms = true;
        this.paucalForms = paucalForms;
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