package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.By.cssSelector;

@Data
public class CardApplicationTest {
    private SelenideElement form;

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        form = $("[action]");
    }

    @Nested
    public class PositiveTestCases {
        @Test
        void shouldSubmitRequestIfDataFullyValid() {
            form.$(cssSelector("[type='text']")).sendKeys("Кристина Пелевина");
            form.$(cssSelector("[type='tel']")).sendKeys("+79061975882");
            form.$(cssSelector("[data-test-id=agreement]")).click();
            form.$(cssSelector("[type='button']")).click();
            $("[data-test-id=order-success]").shouldHave(Condition.exactText("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время."));
        }

        @Test
        void shouldSubmitRequestIfSurnameWithHyphen() {
            form.$(cssSelector("[type='text']")).sendKeys("Кристина Пелевина-Пелевина");
            form.$(cssSelector("[type='tel']")).sendKeys("+79061975882");
            form.$(cssSelector("[data-test-id=agreement]")).click();
            form.$(cssSelector("[type='button']")).click();
            $("[data-test-id=order-success]").shouldHave(Condition.exactText("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время."));
        }
    }

    @Nested
    public class NegativeTestCases {
        @Test
        void shouldNotSubmitIfFormIsEmpty() {
            form.$(cssSelector("[type='button']")).click();
            $(".input_invalid .input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
        }

        @Test
        void shouldNotSubmitIfPhoneIsEmpty() {
            form.$(cssSelector("[data-test-id=name] input")).sendKeys("Кристина Пелевина");
            form.$(cssSelector("[data-test-id=phone] input")).sendKeys("");
            form.$(cssSelector("[data-test-id=agreement]")).click();
            form.$(cssSelector("[type='button']")).click();
            $("[data-test-id=phone].input_invalid .input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
        }

        @Test
        void shouldNotSubmitIfNAmeIsEmpty() {
            form.$(cssSelector("[data-test-id=name] input")).sendKeys("");
            form.$(cssSelector("[data-test-id=phone] input")).sendKeys("+79061975882");
            form.$(cssSelector("[data-test-id=agreement]")).click();
            form.$(cssSelector("[type='button']")).click();
            $("[data-test-id=name].input_invalid .input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
        }

        @Test
        void shouldNotSubmitIfAgreementIsEmpty() {
            form.$(cssSelector("[data-test-id=name] input")).sendKeys("Кристина Пелевина");
            form.$(cssSelector("[data-test-id=phone] input")).sendKeys("+79061975882");
            form.$(cssSelector("[type='button']")).click();
            $("[data-test-id=agreement].input_invalid .checkbox__text").shouldHave(Condition.text("Я соглашаюсь с условиями обработки"));
        }

        @Test
        void shouldNotSubmitRequestIfOnlySurname() {
            form.$(cssSelector("[type='text']")).sendKeys("Пелевина");
            form.$(cssSelector("[type='tel']")).sendKeys("+79061975882");
            form.$(cssSelector("[data-test-id=agreement]")).click();
            form.$(cssSelector("[type='button']")).click();
            $("[data-test-id=name].input_invalid .input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
        }

        @Test
        void shouldNotSubmitRequestIfNameAndSurnameInLatin() {
            form.$(cssSelector("[type='text']")).sendKeys("Rhbcnbyf Gtktdbyf");
            form.$(cssSelector("[type='tel']")).sendKeys("+79061975882");
            form.$(cssSelector("[data-test-id=agreement]")).click();
            form.$(cssSelector("[type='button']")).click();
            $("[data-test-id=name].input_invalid .input__sub").shouldHave(Condition.exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        }

        @Test
        void shouldNotSubmitRequestIfPhoneIs10Numbers() {
            form.$(cssSelector("[type='text']")).sendKeys("Кристина Пелевина");
            form.$(cssSelector("[type='tel']")).sendKeys("+7906197588");
            form.$(cssSelector("[data-test-id=agreement]")).click();
            form.$(cssSelector("[type='button']")).click();
            $("[data-test-id=phone].input_invalid .input__sub").shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
        }

        @Test
        void shouldNotSubmitRequestIfPhoneIs12Numbers() {
            form.$(cssSelector("[type='text']")).sendKeys("Кристина Пелевина");
            form.$(cssSelector("[type='tel']")).sendKeys("+790619758882");
            form.$(cssSelector("[data-test-id=agreement]")).click();
            form.$(cssSelector("[type='button']")).click();
            $("[data-test-id=phone].input_invalid .input__sub").shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
        }

        @Test
        void shouldNotSubmitRequestIfNameContainsInvalidSymbols() {
            form.$(cssSelector("[type='text']")).sendKeys("++++++");
            form.$(cssSelector("[type='tel']")).sendKeys("+79061975882");
            form.$(cssSelector("[data-test-id=agreement]")).click();
            form.$(cssSelector("[type='button']")).click();
            $("[data-test-id=name].input_invalid .input__sub").shouldHave(Condition.exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        }

        @Test
        void shouldNotSubmitRequestIfPhoneContainsSymbols() {
            form.$(cssSelector("[type='text']")).sendKeys("Кристина Пелевина");
            form.$(cssSelector("[type='tel']")).sendKeys("+++++");
            form.$(cssSelector("[data-test-id=agreement]")).click();
            form.$(cssSelector("[type='button']")).click();
            $("[data-test-id=phone].input_invalid .input__sub").shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
        }
    }
}