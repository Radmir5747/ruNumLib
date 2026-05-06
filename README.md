# ruNumLib

[![NO AI](https://raw.githubusercontent.com/nuxy/no-ai-badge/master/badge.svg)](https://github.com/nuxy/no-ai-badge)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Позволяет получить порядковое, количественное или собирательное числительное с учётом грамматических характеристик,
а также согласовать числительное и существительное.

## Описание
Поддерживаются порядковые, количественные целые и дробные, собирательные (_трое_, _оба/обе_ и т. д.) числительные.

Доступно преобразование простых (_две пятых_) и смешанных (_одна целая пять седьмых_) дробей, в том числе и неправильных,
а также склонение числительных полтора/полторы и полтораста.

Поддерживаются положительные и отрицательные числа типа int.

Грамматические характеристики:
* падеж - поддерживаются шесть падежей, традиционно выделяемых в "школьной" грамматике;
* род - для порядковых (_первый - первая_) и количественных (_один - одна_, _оба - обе_ и т.д.) числительных;
* грамматическое число - для порядковых (_первый - первые_) и количественных (_один - одни_ и т.д.) числительных; 
* разряд (количественное, порядковое или собирательное числительное);
* одушевлённость существительного, к которому относится числительное (вижу **одного** человека - **одних** людей).

Код библиотеки полностью задокументирован с помощью javadoc.

## Использование

### Получение числа прописью
Для получения числа прописью в функцию `getNumeral` нужно передать грамматические характеристики, а также целое 
число или [дробь](#описание-дробей). Также есть отдельные функции для получения форм числительных оба (`getBoth`),
полтора (`getOneAndAHalf`), полтораста (`get150`), они принимают лишь грамматические характеристики.

### Согласование числительного и существительного
Функция `getNumeralWithNoun` выдаёт массив, первый элемент которого - числительное, а второй - существительное. Функция
принимает в качестве аргументов целое число или дробь, [существительное](#описание-существительных) и грамматические 
характеристики: падеж, в который требуется поставить числительное и существительное, разряд числительного, 
для порядковых и дробных числительных - грамматическое число (_первый человек - первые люди_, _одна вторая конфеты - 
одна вторая конфет_).
> [!TIP]
> Если существительное употребляется только во множественном числе, то грамматическое число передавать не требуются.

### Указание грамматических характеристик
Грамматические характеристики описывает класс `Declension`.

Обязательные характеристики для всех числительных - падеж и разряд. Например, чтобы получить количественное числительное
1, следует передать разряд, падеж, род и число. Для количественного числительного 5 нужно передать лишь разряд и падеж.
При отсутствии необходимых характеристик выдаётся исключение `IllegalArgumentException`.

> [!TIP]
> Для функций `getBoth`, `getOneAndAHalf`, `get150` разряд указывать не обязательно.
> Лишние грамматические характеристики не учитываются (например, род для количественного числительного _пять_).

> [!IMPORTANT]
> При указании грамматического числа у количественного числительного 1, а также порядковых числительных требуется указать
> род, даже если числительное находится в форме множественного числа. Соответственно, если указывается род, то должно 
> быть и грамматическое число. <!-- TODO: это будет исправлено в будущих версиях -->

В винительном падеже собирательных числительных, форме мужского рода или множественного числа порядковых числительных,
числительного один, а также в количественных числительных два, три, четыре и оканчивающихся на один следует указать
одушевлённость.

Для получения экземпляра класса Declension используется метод `build()` класса `DeclensionBuilder`. Падеж указывается в 
конструкторе, остальные характеристики - при помощи вызова соответствующих функций (см. [пункт "Грамматические 
характеристики"](#грамматические-характеристики)).

> [!NOTE]
> Класс Declension неизменяемый.

Например, количественное числительное в форме именительного падежа описывается следующим образом:
```java
Declension d = new DeclensionBuilder(Case.NOMINATIVE).type(Type.CARDINAL).build();
```
Следующая строка описывает порядковое числительное в форме винительного падежа женского рода, относящееся к одушевлённому 
существительному:
```java
Declension d = new DeclensionBuilder(Case.ACCUSATIVE).type(Type.ORDINAL).animacy(Animacy.ANIMATE).gender(Gender.FEMININE).build();
```
DeclensionBuilder позволяет изменить уже заданные грамматические характеристики. В данном примере описывается 
количественное числительное в форме именительного падежа, затем именительный падеж меняется на винительный и добавляется 
одушевлённость:
```java
Declension d = new DeclensionBuilder(Case.NOMINATIVE).type(Type.CARDINAL).build();
Declension d1 = new DeclensionBuilder(d).gramCase(Case.ACCUSATIVE).animacy(Animacy.ANIMATE).build();
```
### Описание дробей
Для представления дробей служит класс `Fraction`. Десятичные дроби, представленные как числа с плавающей точкой (double)
не поддерживаются, их преобразование в объект класса Fraction остаётся на совести программиста.
> [!IMPORTANT]
> Класс Fraction служит исключительно "обёрткой" для представления дроби и не предоставляет возможности проводить 
> арифметические операции.

Поддерживаются простые и смешанные дроби. Числитель и целая часть могут быть отрицательными. Приведение дробей не 
происходит, т.е. можно представить дроби вида 2/4 и 11/5.

При объявлении простой дроби сначала указывается числитель, а затем знаменатель:
```java
Fraction fraction = new Fraction(-2, 5); // -2/5
```
У составной дроби указывается целая часть, затем числитель и знаменатель:
```java
Fraction fraction = new Fraction(9, 3, 4); // 9 3/4
```
При преобразовании дроби в числительное разряд указывать не нужно; дробные числа преобразуются в количественные 
числительные.

Числитель, знаменатель и целую часть дроби можно изменять с помощью функций `setNumerator`, `setDenominator`, `setWhole`
соответственно.
> [!TIP]
> При указании элементов дроби знаки нормализуются. Так, если в дроби с положительной целой частью задать отрицательный
> числитель, целая часть поменяет знак, а числитель станет положительным. Если задать положительный числитель и 
> отрицательный знаменатель, то они "обменяются" знаками.

### Описание существительных
Для описания существительных используется класс `Noun`. Требуется задать род и одушевлённость существительного, а также
передать падежные формы единственного и множественного числа. Если существительное употребляется только во множественном
числе (_сутки, ножницы_), то требуется указать только одушевлённость и формы множественного числа.

Имеется возможность задать отдельные падежные формы для чисел, оканчивающихся на 2, 3, 4, для этого используется функция 
`setPaucalForms`.

Функция `getCaseForm` выдаёт форму существительного в зависимости от падежа и грамматического числа.

### Примеры
Количественные числительные:
```java
String s = RussianNumeral.getNumeral(1000, // дательный падеж, множественное число
        new DeclensionBuilder(Case.DATIVE).type(Type.CARDINAL).count(Count.PLURAL).build());
System.out.println(s); // тысячам
String s1 = RussianNumeral.getNumeral(Integer.MAX_VALUE, 
        new DeclensionBuilder(Case.INSTRUMENTAL).type(Type.CARDINAL).build()); // творительный падеж
System.out.println(s1); 
//двумя миллиардами ста сорока семью миллионами четырьмястами восемьюдесятью тремя тысячами шестьюстами сорока семью
```
Порядковые числительные:
```java
String s = RussianNumeral.getNumeral(300, // именительный падеж, мужской род, множественное число
        new DeclensionBuilder(Case.NOMINATIVE).type(Type.ORDINAL).gender(Gender.MASCULINE).count(Count.PLURAL).build());
System.out.println(s); // трёхсотые
String s1 = RussianNumeral.getNumeral(20152200, // дательный падеж, мужской род, единственное число
        new DeclensionBuilder(Case.DATIVE).type(Type.ORDINAL).gender(Gender.MASCULINE).count(Count.SINGULAR).build());
System.out.println(s1); // двадцать миллионов сто пятьдесят две тысячи двухсотому
String s2 = RussianNumeral.getNumeral(2147000000, // творительныый падеж, средний род, единственное число
        new DeclensionBuilder(Case.INSTRUMENTAL).type(Type.ORDINAL).gender(Gender.NEUTER).count(Count.SINGULAR).build());
System.out.println(s2); // два миллиарда стосорокасемимиллионным
```
Образование числительных от отрицательных чисел:
```java
String s = RussianNumeral.getNumeral(-4196, new DeclensionBuilder(Case.GENITIVE).type(Type.CARDINAL).build());
System.out.println(s); // минус четырёх тысяч ста девяноста шести; количественное числительное, родительный падеж
String s1 = RussianNumeral.getNumeral(-1, // винительный падеж, порядковое числительное, мужской род, единственное число
        new DeclensionBuilder(Case.ACCUSATIVE).type(Type.ORDINAL).gender(Gender.MASCULINE).count(Count.SINGULAR)
                .animacy(Animacy.INANIMATE).build()); // относится к одушевлённому существительному
System.out.println(s1); // минус первый
```
Собирательные числительные:
```java
String s = RussianNumeral.getNumeral(5, new DeclensionBuilder(Case.PREPOSITIONAL).type(Type.COLLECTIVE).build());
System.out.println(s); // пятерых; предложный падеж
String s1 = RussianNumeral.getNumeral(10, new DeclensionBuilder(Case.NOMINATIVE).type(Type.COLLECTIVE).build());
System.out.println(s1); // десятеро; именительный падеж
```
Дроби:
```java
String s = RussianNumeral.getNumeral(new Fraction(1, 7), new DeclensionBuilder(Case.ACCUSATIVE).build());
System.out.println(s); // одну седьмую; винительный падеж
String s1 = RussianNumeral.getNumeral(new Fraction(3, 20, 100), new DeclensionBuilder(Case.INSTRUMENTAL).build());
System.out.println(s1); // тремя целыми двадцатью сотыми; творительный падеж
String s2 = RussianNumeral.getNumeral(new Fraction(-3, 2), new DeclensionBuilder(Case.NOMINATIVE).build());
System.out.println(s2); // минус три вторых; именительный падеж
```
Согласование числительного и существительного:
```java
// Количественные числительные:
Noun year = new Noun(Gender.MASCULINE, Animacy.INANIMATE, new String[]{"год", "года", "году", "год", "годом", "годе"},
        new String[]{"годы", "лет", "годам", "годы", "годами", "годах"});
String[] s = RussianNumeral.getNumeralWithNoun(1, year,
        new DeclensionBuilder(Case.NOMINATIVE).type(Type.CARDINAL).build());
String[] s0 = RussianNumeral.getNumeralWithNoun(0, year,
        new DeclensionBuilder(Case.NOMINATIVE).type(Type.CARDINAL).build());
String[] s1 = RussianNumeral.getNumeralWithNoun(2, year,
        new DeclensionBuilder(Case.NOMINATIVE).type(Type.CARDINAL).build());
String[] s2 = RussianNumeral.getNumeralWithNoun(5, year,
new DeclensionBuilder(Case.NOMINATIVE).type(Type.CARDINAL).build());
System.out.println(s[0] + " " + s[1]); // один год
System.out.println(s0[0] + " " + s0[1]); // ноль лет
System.out.println(s1[0] + " " + s1[1]); // два года
System.out.println(s2[0] + " " + s2[1]); // пять лет
Noun boy = new Noun(Gender.MASCULINE, Animacy.ANIMATE,
        new String[]{"мальчик", "мальчика", "мальчику", "мальчика", "мальчиком", "мальчике"},
        new String[]{"мальчики", "мальчиков", "мальчикам", "мальчиков", "мальчиками", "мальчиках"});
String[] s3 = RussianNumeral.getNumeralWithNoun(1, year,
        new DeclensionBuilder(Case.ACCUSATIVE).type(Type.CARDINAL).build());
String[] s4 = RussianNumeral.getNumeralWithNoun(1, boy,
        new DeclensionBuilder(Case.ACCUSATIVE).type(Type.CARDINAL).build());
String[] s5 = RussianNumeral.getNumeralWithNoun(2, year,
        new DeclensionBuilder(Case.ACCUSATIVE).type(Type.CARDINAL).build());
String[] s6 = RussianNumeral.getNumeralWithNoun(2, boy,
        new DeclensionBuilder(Case.ACCUSATIVE).type(Type.CARDINAL).build());
System.out.println(s3[0] + " " + s3[1]); // один год (винительный падеж)
System.out.println(s4[0] + " " + s4[1]); // одного мальчика (винительный падеж)
System.out.println(s5[0] + " " + s5[1]); // два года (винительный падеж)
System.out.println(s6[0] + " " + s6[1]); // двух мальчиков (винительный падеж)
String[] s7 = RussianNumeral.getNumeralWithNoun(0, year,
        new DeclensionBuilder(Case.INSTRUMENTAL).type(Type.CARDINAL).build());
System.out.println(s7[0] + " " + s7[1]); // нолём лет
// Порядковые числительные
String[] s8 = RussianNumeral.getNumeralWithNoun(1, year,
        new DeclensionBuilder(Case.ACCUSATIVE).type(Type.ORDINAL).count(Count.SINGULAR).build());
String[] s9 = RussianNumeral.getNumeralWithNoun(1, year,
new DeclensionBuilder(Case.ACCUSATIVE).type(Type.ORDINAL).count(Count.PLURAL).build());
System.out.println(s8[0] + " " + s8[1]); // первый год
System.out.println(s9[0] + " " + s9[1]); // первые годы
// Собирательные числительные
String[] s10 = RussianNumeral.getNumeralWithNoun(2, boy,
        new DeclensionBuilder(Case.NOMINATIVE).type(Type.COLLECTIVE).build());
        System.out.println(s10[0] + " " + s10[1]); // двое мальчиков
// Дроби
Noun candy = new Noun(Gender.FEMININE, Animacy.INANIMATE,
        new String[]{"конфета", "конфеты", "конфете", "конфету", "конфетой", "конфете"},
        new String[]{"конфеты", "конфет", "конфетам", "конфеты", "конфетами", "конфетах"});
Fraction half = new Fraction(1, 2);
String[] s11 = RussianNumeral.getNumeralWithNoun(half, candy, 
        new DeclensionBuilder(Case.ACCUSATIVE).count(Count.SINGULAR).build());
String[] s12 = RussianNumeral.getNumeralWithNoun(half, candy,
        new DeclensionBuilder(Case.ACCUSATIVE).count(Count.PLURAL).build());
System.out.println(s11[0] + " " + s11[1]); // одну вторую конфеты
System.out.println(s12[0] + " " + s12[1]); // одну вторую конфет
// Отдельные формы для чисел 2, 3, 4
Noun whole = new Noun(Gender.FEMININE, Animacy.INANIMATE,
        new String[]{"целая", "целой", "целой", "целую", "целой", "целой"},
        new String[]{"целые", "целых", "целым", "целые", "целыми", "целых"});
// формы для чисел 2, 3, 4
        whole.setPaucalForms(new String[]{"целых", "целых", "целым", "целых", "целыми", "целых"});
String[] s13 = RussianNumeral.getNumeralWithNoun(1, whole,
        new DeclensionBuilder(Case.NOMINATIVE).type(Type.CARDINAL).build());
String[] s14 = RussianNumeral.getNumeralWithNoun(2, whole,
        new DeclensionBuilder(Case.NOMINATIVE).type(Type.CARDINAL).build());
String[] s15 = RussianNumeral.getNumeralWithNoun(5, whole,
        new DeclensionBuilder(Case.NOMINATIVE).type(Type.CARDINAL).build());
        System.out.println(s13[0] + " " + s13[1]); // одна целая
        System.out.println(s14[0] + " " + s14[1]); // две целых (не целые!)
        System.out.println(s15[0] + " " + s15[1]); // пять целых
// согласование с pluralia tantum
Noun days = new Noun(Animacy.INANIMATE, new String[]{"сутки", "суток", "суткам", "сутки", "сутками", "сутках"});
String[] s16 = RussianNumeral.getNumeralWithNoun(1, days,
        new DeclensionBuilder(Case.NOMINATIVE).type(Type.CARDINAL).build());
String[] s17 = RussianNumeral.getNumeralWithNoun(5, days,
        new DeclensionBuilder(Case.NOMINATIVE).type(Type.CARDINAL).build());
String[] s18 = RussianNumeral.getNumeralWithNoun(1, days,
        new DeclensionBuilder(Case.NOMINATIVE).type(Type.ORDINAL).build());
System.out.println(s16[0] + " " + s16[1]); // одни сутки
System.out.println(s17[0] + " " + s17[1]); // пять суток
System.out.println(s18[0] + " " + s18[1]); // первые сутки
```
### Грамматические характеристики
`Animacy` - одушевлённость существительного, к которому относится числительное; задаётся функцией `animacy()`:
* `Animacy.ANIMATE` - одушевлённое существительное
* `Animacy.INANIMATE` - неодушевлённое существительное

`Case` - падеж; задаётся функцией `gramCase()`:
* `Case.NOMINATIVE` - именительный падеж
* `Case.GENITIVE` - родительный падеж
* `Case.DATIVE` - дательный падеж
* `Case.ACCUSATIVE` - винительный падеж
* `Case.INSTRUMENTAL` - творительный падеж
* `Case.PREPOSITIONAL` - предложный падеж

`Count` - грамматическое число; задаётся функцией `count()`:
* `Count.SINGULAR` - единственное число
* `Count.PLURAL` - множественное число

`Gender` - род; задаётся функцией `gender()`:
* `Gender.MASCULINE` - мужской род
* `Gender.FEMININE` - женский род
* `Gender.NEUTER` - средний род

`Type` - разряд числительного; задаётся функцией `type()`:
* `Type.CARDINAL` - количественное числительное
* `Type.ORDINAL` - порядковое числительное
* `Type.COLLECTIVE` - количественное собирательное числительное
## Лицензия
MIT, см. [LICENSE](LICENSE)