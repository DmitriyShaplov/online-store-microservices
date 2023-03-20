package ru.shaplov.billing.service.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import ru.shaplov.billing.model.Payment;
import ru.shaplov.billing.model.Transaction;
import ru.shaplov.billing.model.persistence.PaymentEntity;
import ru.shaplov.billing.persistence.PaymentRepository;
import ru.shaplov.billing.persistence.TransactionRepository;
import ru.shaplov.billing.service.HistoryService;

import java.util.List;
import java.util.UUID;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    public HistoryServiceImpl(PaymentRepository paymentRepository,
                              TransactionRepository transactionRepository,
                              ModelMapper modelMapper) {
        this.paymentRepository = paymentRepository;
        this.transactionRepository = transactionRepository;
        this.modelMapper = modelMapper;
        this.modelMapper.typeMap(PaymentEntity.class, Payment.class)
                .addMappings(mapping -> mapping.map(source -> source.getAccount().getUserId(), Payment::setUserId));
    }

    @Override
    public List<Payment> getPaymentHistory(Long userId) {
        return modelMapper.map(paymentRepository.findAllByAccountUserIdOrderByPaymentDateDesc(userId),
                new TypeToken<List<Payment>>() {
                }.getType());
    }

    @Override
    public List<Transaction> getTransactionHistory(Long userId) {
        return modelMapper.map(transactionRepository.findAllByAccountUserIdOrderByDateDesc(userId),
                new TypeToken<List<Transaction>>() {
                }.getType());
    }

    @Override
    public List<Transaction> getTransactionHistory(UUID orderId) {
        return modelMapper.map(transactionRepository.findAllByOrderIdOrderByDateDesc(orderId),
                new TypeToken<List<Transaction>>() {
                }.getType());
    }
}
