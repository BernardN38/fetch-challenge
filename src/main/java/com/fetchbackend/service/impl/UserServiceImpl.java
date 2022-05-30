package com.fetchbackend.service.impl;

import com.fetchbackend.entity.Transaction;
import com.fetchbackend.entity.User;
import com.fetchbackend.exception.FetchApiException;
import com.fetchbackend.exception.ResourceNotFoundException;
import com.fetchbackend.payload.*;
import com.fetchbackend.repository.TransactionRepository;
import com.fetchbackend.repository.UserRepository;
import com.fetchbackend.service.UserService;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    public UserServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public UserServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Map<String, Integer> getUserPointsByPayer(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        List<Transaction> transactions = transactionRepository.findSumPointsPayerByUserId(userId);
        return transactions.stream().collect(Collectors.toMap(Transaction::getPayer, Transaction::getPoints));
    }

    @Override
    public UserResponse getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<User> users = userRepository.findAll(pageable);

        return mapToUserResponse(users);
    }
    @Override
    public UserPublicDto createUser(UserDto userDto) {
        User user = mapper.map(userDto,User.class);
        User newUser = userRepository.save(user);
        return mapper.map(newUser, UserPublicDto.class);
    }

    @Override
    public SpendResponse spendUserPoints(Long userId, int pointsToSpend) {
        //check if user has enough points for request -> throw Error with message if not
        Transaction userTransactionPoints = userRepository.getUserPointsTotal(userId).orElseThrow(() -> new FetchApiException(HttpStatus.NOT_FOUND, "User does not have any points avialible"));
        int userTotalPoints = userTransactionPoints.getPoints();
        int remainingPointsToDeduct = pointsToSpend;

        if (remainingPointsToDeduct > userTotalPoints) {
            throw new FetchApiException(HttpStatus.BAD_REQUEST, "Not enough points. Points available: " + userTotalPoints);
        }

        //find oldest points transaction for user
        Transaction oldestMatch = transactionRepository.findOldestByUserId(userId, PageRequest.of(0, 1)).getContent().get(0);

        //keep track of deductions for response
        Map<String, Integer> deductionTracker = new HashMap<String, Integer>();

        // run until points remaining is 0
        while (remainingPointsToDeduct > 0) {
            //create variable of current transaction data for code readability
            int transactionPoints = oldestMatch.getPoints();
            String transactionPayer = oldestMatch.getPayer();
            //if current transaction satisfies all points remaining
            if (transactionPoints >= remainingPointsToDeduct) {
                //add to tracker
                if (deductionTracker.containsKey(transactionPayer)) {
                    deductionTracker.put(transactionPayer, deductionTracker.get(transactionPayer) - remainingPointsToDeduct);
                } else {
                    deductionTracker.put(transactionPayer, -remainingPointsToDeduct);
                }

                //update database to reflect deduction
                oldestMatch.setPoints(transactionPoints - remainingPointsToDeduct);
                transactionRepository.save(oldestMatch);

                // update remain points
                remainingPointsToDeduct = 0;
            }
            // if current transaction partially satisfies of points remaining
            else if (transactionPoints > 0) {
                //add to tracker
                if (deductionTracker.containsKey(transactionPayer)) {
                    deductionTracker.put(transactionPayer, deductionTracker.get(transactionPayer) - transactionPoints);
                } else {
                    deductionTracker.put(transactionPayer, -transactionPoints);
                }
                //update remaining points
                remainingPointsToDeduct -= transactionPoints;

                //update database to reflect deduction
                oldestMatch.setPoints(0);
                transactionRepository.save(oldestMatch);
            }
            // if current transaction has no points available get next oldest transaction from database
            else {
                oldestMatch = transactionRepository.findNextOldest(userId, oldestMatch.getCreated(), PageRequest.of(0, 1)).getContent().get(0);
            }
        }
        //create response
        SpendResponse spendResponse = new SpendResponse();
        spendResponse.setPayerDeductions(deductionTracker);
        return spendResponse;
    }


    private List<PointsDto> mapToListPointsDto(Set<Transaction> transactions) {
        Map<String, Integer> payers = new HashMap<String, Integer>();
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


    private UserResponse mapToUserResponse(Page<User> users){
        UserResponse userResponse = new UserResponse();
        userResponse.setContent(users.stream().map(user -> {
            UserPublicDto userPublicDto = new UserPublicDto();
            userPublicDto.setUsername(user.getUsername());
            userPublicDto.setId(user.getId());
            if (transactionRepository.existsByUserId(user.getId())) {
                userPublicDto.setPointsAvailable(userRepository.getUserPointsTotal(user.getId()).get().getPoints());
            }
            return userPublicDto;
        }).toList());
        userResponse.setPageNo(users.getNumber());
        userResponse.setPageSize(users.getSize());
        userResponse.setTotalElements(users.getTotalElements());
        userResponse.setTotalPages(users.getTotalPages());
        userResponse.setLast(users.isLast());
        return userResponse;
    }
}
