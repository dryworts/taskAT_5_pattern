package ru.netology.pattern;

import com.codeborne.selenide.Condition;
import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AppCardDeliveryTaskOne {
    private String generateDate(int addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    private Faker faker;

    @BeforeEach
    void setUpAll() {
        faker = new Faker(new Locale("ru"));
    }

    @Test
    public void ShouldBeSuccessfullyCompleted() {
        open("http://localhost:9999");
        String city = faker.address().city();
        $("[data-test-id = 'city'] input").setValue(city);
        String planningDate = generateDate(4, "dd.MM.yyyy");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        $("[data-test-id='name'] input").setValue(firstName + " " + lastName);
        String phone = faker.phoneNumber().phoneNumber();
        $("[data-test-id='phone'] input").setValue(phone);
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='success-notification']").shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Успешно!\n" +
                        "Встреча успешно запланирована на " + planningDate));
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        String anotherDate = generateDate(8, "dd.MM.yyyy");
        $("[data-test-id='date'] input").setValue(anotherDate);
        $("button.button").click();


        $("[data-test-id='replan-notification'] .notification__title").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='replan-notification'] .button").click();

        $("[data-test-id='success-notification']").shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Успешно!\n" +
                        "Встреча успешно запланирована на " + anotherDate));

    }


}
