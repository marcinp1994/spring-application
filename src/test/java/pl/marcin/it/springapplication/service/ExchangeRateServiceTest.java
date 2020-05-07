package pl.marcin.it.springapplication.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ExchangeRateServiceTest {

    @Autowired
    ExchangeRateService exchangeRateService;

    @Test
    public void testBuildURL(){
    }

}