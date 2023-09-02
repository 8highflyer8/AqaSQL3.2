package test;


import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.SQLHelper.cleanDataBase;

public class BankLoginTest {




    @Test
    @DisplayName("Should successfully login to dashboard with exist login and password from sut test data")
    void shouldSuccessfulLogin() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var AuthInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(AuthInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());

    }

    @Test
    @DisplayName("Should get error notification if user is not exist in base")
    void ShouldGetErrorNotificationIfLoginWithRandomUserWithoutAddingToBase() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var AuthInfo = DataHelper.generateRandomUser();
        loginPage.validLogin(AuthInfo);
        loginPage.verifyErrorNotificationVisibility();
    }

    @Test
    @DisplayName("Should get error notification if login with exist in base  and active user and  random verification code")
    void shouldGetErrorNotificationIfLoginWithExistUserAndRandomVerificationCode() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var AuthInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(AuthInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
        verificationPage.verifyErrorNotificationVisibility();

    }

    @Test
    @DisplayName("Should get error notification that user blocked if login with exist login and incorrect password 3 times")
    void shouldGetErrorNotificationThatUserBlockedIfLoginWithExistLoginAndIncorrectPassword3Times() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var AuthInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.inValidLogin(AuthInfo);
        loginPage.cleanFields();
        var verificationPage2 = loginPage.inValidLogin(AuthInfo);
        loginPage.cleanFields();
        var verificationPage3 = loginPage.inValidLogin(AuthInfo);
        loginPage.cleanFields();
        var verificationPage4 = loginPage.validLogin(AuthInfo);
        var status = SQLHelper.getBlockingUser();
        Assertions.assertEquals("blocked", status);


    }

}
