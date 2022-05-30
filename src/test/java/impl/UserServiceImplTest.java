package impl;

import com.fetchbackend.entity.Transaction;
import com.fetchbackend.entity.User;
import com.fetchbackend.payload.PointsDto;
import com.fetchbackend.payload.SpendResponse;
import com.fetchbackend.repository.TransactionRepository;
import com.fetchbackend.repository.UserRepository;
import com.fetchbackend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserServiceImpl userService;

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

    @Test
    void getUserPointsByPayer() {
        Map<String, Integer> points = userService.getUserPointsByPayer(1L);
        assertEquals(points.get("testPayer1"), 500);
        assertEquals(points.get("testPayer2"), 200);
        assertEquals(points.get("testPayer3"), 300);

    }

    @Test
    void spendUserPoints() {
        SpendResponse spendResponse = userService.spendUserPoints(1L, 800);
        System.out.println(spendResponse);
        List<PointsDto> pointsDtoList1 = List.of(new PointsDto("testPayer1", -300), new PointsDto("testPayer2", -200), new PointsDto("testPayer3", -300));
        assertThat(spendResponse.getPayerDeductions()).containsAll(pointsDtoList1);
    }

    @Test
    void spendUserPoints1() {
        SpendResponse spendResponse = userService.spendUserPoints(1L, 100);
        List<PointsDto> pointsDtoList1 = List.of(new PointsDto("testPayer1", -100));
        assertThat(spendResponse.getPayerDeductions()).containsAll(pointsDtoList1);
    }
}