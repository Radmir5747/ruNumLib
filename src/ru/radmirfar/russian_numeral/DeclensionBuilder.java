package ru.radmirfar.russian_numeral;

/**
 * Класс для указания грамматических признаков числительного.
 * @see Declension
 */
public class DeclensionBuilder {
    private Gender _gender = null;
    private Case _gramCase;
    private Count _count = null;
    private Type _type = null;
    private Animacy _animacy = null;

    /**
     * Конструктор. Обязательный параметр - падеж (как единственный общий признак у всех разрядов числительных).
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