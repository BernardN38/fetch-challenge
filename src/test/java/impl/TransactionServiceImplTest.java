package impl;

import com.fetchbackend.entity.Transaction;
import com.fetchbackend.entity.User;
import com.fetchbackend.payload.TransactionDto;
import com.fetchbackend.repository.TransactionRepository;
import com.fetchbackend.repository.UserRepository;
import com.fetchbackend.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TransactionServiceImplTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        User user = new User(
                1L,
                "testUser",
                "password"
        );
        user = userRepository.save(user);
        Calendar date = Calendar.getInstance();

        Transaction trans1 = new Transaction(1L, "testPayer1", 100, date, Calendar.getInstance(), user);
        date.add(Calendar.MINUTE,1);
        Transaction trans2 =       new Transaction(2L, "testPayer2", 200, date, Calendar.getInstance(), user);
        date.add(Calendar.MINUTE, 1);
        Transaction trans3=       new Transaction(3L, "testPayer3", 300, date, Calendar.getInstance(), user);
        date.add(Calendar.MINUTE, 1);
        Transaction trans4 =     new Transaction(4L, "testPayer1", 400, date, Calendar.getInstance(), user);


        transactionRepository.saveAll(List.of(trans1,trans2,trans3,trans4));
    }

    @AfterEach
    void cleanUpEach() {
//        System.out.println(transactionRepository.count());
//        transactionRepository.deleteAllInBatch(testTransactions);
    }

    @Test
    void getUserTransactions() {

        TransactionDto trans1 = new TransactionDto();
        trans1.setPayer("testPayer1");
        trans1.setPoints(100);
        TransactionDto trans2 = new TransactionDto();
        trans2.setPayer("testPayer2");
        trans2.setPoints(200);
        TransactionDto trans3 = new TransactionDto();
        trans3.setPayer("testPayer3");
        trans3.setPoints(300);
        TransactionDto trans4 = new TransactionDto();
        trans4.setPayer("testPayer1");
        trans4.setPoints(400);
        List<TransactionDto> transactionDtoList = List.of(trans1, trans2, trans3, trans4);

        //given data
        List<TransactionDto> userTransactions = transactionService.getUserTransactions(0, 10, "id", "asc", 1L).getContent();

        //check equality
        assertThat(userTransactions.get(0).getPoints()).isEqualTo(trans1.getPoints());
        assertThat(userTransactions.get(0).getPayer()).isEqualTo(trans1.getPayer());

        assertThat(userTransactions.get(1).getPoints()).isEqualTo(trans2.getPoints());
        assertThat(userTransactions.get(1).getPayer()).isEqualTo(trans2.getPayer());

        assertThat(userTransactions.get(2).getPoints()).isEqualTo(trans3.getPoints());
        assertThat(userTransactions.get(2).getPayer()).isEqualTo(trans3.getPayer());

        assertThat(userTransactions.get(3).getPoints()).isEqualTo(trans4.getPoints());
        assertThat(userTransactions.get(3).getPayer()).isEqualTo(trans4.getPayer());
    }

    @Test
    void createTransaction() {
        TransactionDto inputDto = new TransactionDto();
        inputDto.setPoints(200);
        inputDto.setPayer("testPayerCreateTransaction");

        TransactionDto transactionDto = transactionService.createTransaction(inputDto, 1L);

        assertThat(transactionDto.getPayer()).isEqualTo(inputDto.getPayer());
        assertThat(transactionDto.getPoints()).isEqualTo(inputDto.getPoints());
    }


    @Test
    void updateTransaction() {
        TransactionDto inputDto = new TransactionDto();
        inputDto.setPoints(200);
        inputDto.setPayer("testPayerCreateTransaction");
        TransactionDto transactionDto = transactionService.updateTransaction(inputDto, 1L, 5L);

        assertThat(transactionDto.getPayer()).isEqualTo(inputDto.getPayer());
        assertThat(transactionDto.getPoints()).isEqualTo(inputDto.getPoints());

    }
}