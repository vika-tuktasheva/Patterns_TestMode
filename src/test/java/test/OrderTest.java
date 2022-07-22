package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static data.DataGenerator.Registration.getRegisteredUser;
import static data.DataGenerator.Registration.getUser;
import static data.DataGenerator.getRandomLogin;
import static data.DataGenerator.getRandomPassword;

public class OrderTest {

    @BeforeEach
    public void openApp() {
        open("http://localhost:9999");
    }

    @Test
    public void shouldSuccessLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $(".button").click();
        $("[id=root]").should(text("Личный кабинет"));
    }

    @Test
    public void shouldFailBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login]").click();
        $(".notification__content").should(exactText("Ошибка! Пользователь заблокирован"));
    }

    @Test
    public void shouldFailNonRegisteredUser() {
        var nonRegisteredUser = getUser("active");
        $("[data-test-id=login] input").setValue(nonRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(nonRegisteredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $(".notification__content").should(exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    public void shouldFailWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $(".notification__content").should(exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    public void shouldFailWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $("[data-test-id=action-login]").click();
        $(".notification__content").should(exactText("Ошибка! Неверно указан логин или пароль"));
    }
}