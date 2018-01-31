# Описание

Фреймворк для автоматизации тестирования web приложений основанный на Selenium WebDriver
Является дополнительным слоем между пользователем фреймворка и WebDriver
Полностью исключает работу с WebDriver и предоставляет интерфейс для взаимодействия с браузером и веб элементами
Включает модель работы со станицами **Page Object**

# Организация проекта 
> Монорепозиторий
>
> На текущи момент содержит 2 под-проекта
* Под-проект **commons**, который содержит общие классы не привязанные к web
* Под-проект **web**, который содержит Функционал для взаимодействия с браузером, страницей и реализации основных web элементов
> Система сборки - **gradle**
>
> Юнит-тесты **junit5**, web тестируется на PhantomJS и Chrome. На самом деле почти все тесты интеграционные
>
> Используются Idea аннотации **@Nullable** и **@NotNull**
>
> Логгер Logback

# Особенности
#### Под-проект **commons** включает все необходимые модули и зависимости для быстрого создания автотестов
* Reporter - реализация на основе Allure
* Verifier - замена ассертов, постинг сообщений в Reporter
* Waiter - ожидания, которые возвращают параметризованный WaitResult. Замена ожиданий Selenium
* PumpException - собственная реализация Исключений
* PumpMessage - Форматированные сообщения для вывода в консоль
* ConfigurationsLoader - удобная загрзка конфигурация из properties с маппингом на java объект

#### Под-проект **web** предоставляет сущности для создания тестов web на основе подхода Page Object

###### Пакет ru.mk.pump.web.browsers 
- Browser и AbstractBrowser - Интерфейс и реализация для доступа к браузеру. Изолирует работу с WebDriver от пользователя
- Browsers - менеджер браузеров
- AbstractDriverBuilder - билдер для создания экземпляра **WebDriver**. Можно расширять кол-во поддерживаемых реализаци **WebDriver**  

###### Пакет ru.mk.pump.web.elements - Доступ к элементам на web странице.
- Element и BaseElement  - Интерфейс и реализация для доступа элементам. Автоматизирует все проверки перед выполнением действия над элементом и упрощает взаимодействие.
Обеспечивает логирование и репортинг с помощью слушателей.  
  Все пользовательские элементы необходимо наследовать от **BaseElement**  
  Все пользователькие интерфейсы элементов необходимо наследовать от **Element**  
- api.annotations - Аннотации-маркеры для выбора из нескольких реализаций одного интерфейса элемента
- ActionListener и StateListener - Слушатели для статусов элементов и действий над элементами
- ElementFactory - Фабрика элементов для использования в Page Object инициализации. Расширяемая


###### Пакет ru.mk.pump.web.page - web Страница. Автоматизирует проверки статуса загрузки старницы и упрощает работу со страницей для пользователя
- ru.mk.pump.web.page.api.Page и BasePage - Интерфейс старницы и базовая реализация для наследования
- ru.mk.pump.web.page.api.PageLoader и PageLoader - Все ожидания вынесены в отдельный интерфейс с базовой реализацией. Инстанс хранится в BasePage

###### ru.mk.pump.web.component - общие сущности для возможности создания неограниченной иерархии вложенных элементов (и списков элементов) в странице
 
###### ru.mk.pump.web.common - поддержка page object
- Initializer - реализация org.openqa.selenium.support.pagefactory.FieldDecorator для иницализации элементов страницы. Без использования java.lang.reflect.Proxy (это плюс)
- api.annotations - аннотации для полей с элементами на странице, в том числе аналоги FindBy и FindBys
- ru.mk.pump.web.common.api.ImplDispatcher - Диспетчер реализаций элементов. Загрущает и хранит интефрйсы и их реализации. 
Поддерживате возможность добавления пользовательских реализаций

####Для настройки элементов используются параметры вида Map<String, Parameter<?>>
- В паттерне Page Object для передачи параметров в элемент необходимо воспользоваться аннотациями из пакета ru.mk.pump.web.common.api.annotations
- либо методом ru.mk.pump.web.elements.internal.BaseElement.withParams
- элемент сам выбирает поддерживаемые параметры и записывает их в свои поля для дальнейшего использования
- параметры описаны в ru.mk.pump.web.constants.ElementParams и в java-doc если присутствует аннотация ru.mk.pump.web.elements.internal.DocParameters

####Для настройки элементов используются параметры вида Map<String, Parameter<?>>

# Ожидает реализации
- менеджер страниц
- менеджер конфигураций фреймворка
- отдельный проект - интеграция с cucumber
- отдельный проект - параллельный раннер для cucumber и перезапуск проваленных тестов
- дополнение юнит-тестов
- поддержка сборки под Windows и Linux без установленных браузеров Chrome и PhantomJS (для юнит-тестов)

# Quick Start
####Добавить в зависимости
maven
```java
не реализовано
```

gradle
```java
не реализовано
```

####Создать класс Страницы Web Приложения унаследованный от ru.mk.pump.web.page.BasePage  
```java
    /**
     * Страница наследуется от {@link BasePage}.
     * Все приватные поля - это элементы либо компоненты
     */
    @Title(value = "Регистрация", desc = "Страница регистрации Цифровая Ипотека")
    class RegPage extends BasePage {

        /**
         * Элемент TextArea.
         * Стандартная аннотация FindBy.
         * Аннотация {@link Title} определяет заголовок (имя) элемента и описание.
         */
        @FindBy(tagName = "h2")
        @Title(value = "Заголовок", desc = "Главный заголовок страницы")
        @Getter
        private TextArea pageTitle;

        /**
         * Элемент наследник BaseComponent. Это отдельный класс - кусок страницы, который объединяет несколько элементов.
         * Для него доступны все методы {@link Element}
         * Стандартная аннотация FindBy.
         * Аннотация {@link Title} определяет заголовок (имя) элемента (компонента) и описание.
         */
        @Title("Форма")
        @FindBy(className = "mainlayout")
        @Getter
        private RegMainForm mainForm;

        /**
         * Элемент общего интерфейса Element. Когда не важен конкретный класс элемента.
         * Стандартная аннотация FindBy.
         * Аннотация {@link Title} определяет заголовок (имя) элемента и описание.
         * Этот элемент не существует на реальной странице, но удачно будет инициализирован.
         * Ошибок не возникнет до взаимодействия.
         */
        @Title("Не существующий")
        @FindBy(className = "mainlayout11")
        @Getter
        private Element notExists;

        /**
         * Конcтруктор страницы. PUBLIC. Все конструкторы страниц, компонентов и элементов - PUBLIC!!!
         * @param browser Браузер
         */
        public RegPage(Browser browser) {
            super(browser);
            setName("Регистрация");
            /*установить url*/
            setUrl(DMUrls.REG_PAGE_URL);
            /*установить проверку с ожиданием на присутствие в DOM элемента после открытия*/
            getPageLoader().addExistsElements(pageTitle);
            /*установить проверку с ожиданием на отображение элементов после открытия*/
            getPageLoader().addDisplayedElements(pageTitle, mainForm);
        }

        /**
         * Класс компонента. Наследуется от {@link BaseComponent} - это часть страницы. Объекдиняет несколько элементов.
         * В данном случае это форма с множеством полей ввода и кнопок. Но описано только несколько.
         * Аннотация {@link Title} читается не из класса компонента, а из поля, в котором он объявлен на странице!
         * Не забываем про static для внутренних классов!
         */
        static class RegMainForm extends BaseComponent {

            /**
             * Список компонентов - это несколько зон в главной форме
             */
            @FindBy(xpath = "//div[@class='squished row form-group']")
            @Getter
            private List<RegFormZone> regFormZones;

            public RegMainForm(By avatarBy, Page page) {
                super(avatarBy, page);
            }
        }

        /**
         * Класс компонента. Наследуется от {@link BaseComponent} - это часть страницы. Объекдиняет несколько элементов.
         * В данном случае это одна из зон основной формы
         */
        static class RegFormZone extends BaseComponent {

            /**
             * Список компонентов - это несколько колонок в каждой зоне основной формы
             */
            @FindBy(xpath = "//div[contains(@class, 'column')]")
            @Getter
            private List<RegFormZoneColumn> regFormZoneColumns;

            public RegFormZone(By avatarBy, InternalElement parentElement) {
                super(avatarBy, parentElement);
            }
        }

        /**
         * Колонка в каждой из зон формы
         */
        static class RegFormZoneColumn extends BaseComponent {

            /**
             * Список элементов
             */
            @FindBy(tagName = "input")
            @Getter
            private List<Input> inputs;

            /**
             * Конечный элемент
             */
            @FindBy(className = "group-header")
            @Getter
            private TextArea header;

            @FindBy(id = "regionAutocompleteId")
            @Title(value = "Регион", desc = "Выбор региона из выпадающего списка")
            /*аннотация для определяния нескольких СТРОКОВЫХ параметров элемента*/
            @PStrings({
                /*один СТРОКОВЫЙ параметр*/
                @PString(name = "testParam1", value = "paramValue1"),
                @PString(name = "testParam2", value = "paramValue2")
            })
            /*аннотация для определяния нескольких параметров элемента типа Локатор*/
            @PFindBys({
                /*один параметр Локатор*/
                @PFindBy(name = "extraBy", value = {@FindBy(xpath = "//div")}),
                @PFindBy(name = "iddInputBy", value = {@FindBy(tagName = "input")}),
                @PFindBy(name = "iddDropDownBy", value = {@FindBy(xpath = ".")})
            })
            @Getter
            private InputDropDown inputDropDownRegions;

            public RegFormZoneColumn(By avatarBy, InternalElement parentElement) {
                super(avatarBy, parentElement);
            }
        }
    }
```

####Создать браузер
```java
Browsers browsers = new Browsers();
BrowserConfig config = new BrowserConfig(false, Size.of(true), BrowserType.CHROME);
Browser browser = browsers.newBrowser(config);
```

####Создать страницу
```java
RegPage page = new RegPage(browser);
```

####Запустить браузер и открыть страницу
```java
browser.start();
page.open();
```

####Взаимодействие с элементом
```java
/*Получить элемент Поле ввода*/
Input input = page.getMainForm().getRegFormZones().get(0).getRegFormZoneColumns().get(1).getInputs().get(2);
/*Информация об элементе - имя*/
input.info().getName();
/*Информация об элементе - описание*/
input.info().getDescription();
/*Ввод значения*/
input.type("MAX");
/*Получить текст*/
input.getText();
/*Получить элемент Выпадающий список с вводом*/
InputDropDown inputDropDown = page.getMainForm().getRegFormZones().get(2).getRegFormZoneColumns().get(1).getInputDropDownRegions();
/*Ввод текста и выбор из выпадающего списка введенного значения*/
inputDropDown.typeAndSelect("Москва");
```

>Чтобы запустить из IDE со включенным логгером :  
 ```-javaagent:./../libs/aspectjweaver.jar``` 