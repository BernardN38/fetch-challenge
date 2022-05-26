package com.fetchbackend.service.impl;

import com.fetchbackend.entity.Transaction;
import com.fetchbackend.entity.User;
import com.fetchbackend.exception.FetchApiException;
import com.fetchbackend.exception.ResourceNotFoundException;
import com.fetchbackend.payload.PointsDto;
import com.fetchbackend.payload.SpendResponse;
import com.fetchbackend.repository.TransactionRepository;
import com.fetchbackend.repository.UserRepository;
import com.fetchbackend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public List<PointsDto> getUserPointsByPayer(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        return mapToListPointsDto(user.getTransactions());
    }

    @Override
    public SpendResponse spendUserPoints(Long userId, int pointsToSpend) {
        //check if user has enough points for request -> throw Error with message if not
        int userTotalPoints = userRepository.getUserPointsTotal(userId);
        int remainingPointsToDeduct = pointsToSpend;
        if (remainingPointsToDeduct > userTotalPoints) {
            throw new FetchApiException(HttpStatus.BAD_REQUEST, "Not enough points. Points available: " + userTotalPoints);
        }

        //find oldest points transaction for user
        Transaction oldestMatch = transactionRepository.findOldestByUserId(userId);

        //keep track of deductions for response
        List<PointsDto> deductionTracker = new ArrayList<>();

        // run until points remaining is 0
        while (remainingPointsToDeduct > 0) {
            //create variable of current transaction points for code readability
            int transactionPoints = oldestMatch.getPoints();

            //if current transaction satisfies all points remaining
            if (transactionPoints >= remainingPointsToDeduct) {
                //add to tracker
                deductionTracker.add(new PointsDto(oldestMatch.getPayer(), -remainingPointsToDeduct));

                // update remain points
                remainingPointsToDeduct -= transactionPoints;

                //update database to reflect deduction
                oldestMatch.setPoints(transactionPoints - remainingPointsToDeduct);
                transactionRepository.save(oldestMatch);
            }
            // if current transaction partially satisfies of points remaining
            else if (transactionPoints > 0) {
                //add to tracker
                deductionTracker.add(new PointsDto(oldestMatch.getPayer(), -transactionPoints));

                //update remaining points
                remainingPointsToDeduct -= transactionPoints;

                //update database to reflect deduction
                oldestMatch.setPoints(0);
                transactionRepository.save(oldestMatch);
            }
            // if current transaction has no points available get next oldest transaction from database
            else {
                oldestMatch = transactionRepository.findNextOldest(userId, oldestMatch.getTimestamp());
            }
        }
        //create response
        SpendResponse spendResponse = new SpendResponse();
        spendResponse.setDeductions(deductionTracker);
        return spendResponse;
    }

    private boolean checkUserPointsNotExists(Long userId) {
        return !transactionRepository.existsByUserId(userId);
    }

    private List<PointsDto> mapToListPointsDto(Set<Transaction> transactions) {
        HashMap<String, Integer> payers = new HashMap<String, Integer>();
        transactions.forEach(transaction -> {
                    String key = transaction.getPayer();
                    if (payers.containsKey(key)) {
                        payers.put(key, payers.get(key) + transaction.getPoints());
                    } else {
                        payers.put(key, transaction.getPoints());
                    }
                }
        );
        List<PointsDto> response = new ArrayList<>();
        payers.forEach((k, v) -> {
            PointsDto pointsDto = new PointsDto();
            pointsDto.setPayer(k);
            pointsDto.setPoints(v);
            response.add(pointsDto);
        });
        return response;
    }
}
