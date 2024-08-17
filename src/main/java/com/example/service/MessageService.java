package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Message createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().isEmpty()) {
            throw new IllegalArgumentException("Message text cannot be blank.");
        }

        if (message.getMessageText().length() > 255) {
            throw new IllegalArgumentException("Message text cannot exceed 255 characters.");
        }

        if (!accountRepository.existsById(message.getPostedBy())) {
            throw new IllegalArgumentException("User does not exist.");
        }

        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(int messageId) {
        return messageRepository.findById(messageId);
    }

    public boolean deleteMessage(int messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return true;
        }
        return false;
    }

    public void updateMessage(int messageId, String newMessageText) {
        if (newMessageText == null || newMessageText.isEmpty()) {
            throw new IllegalArgumentException("Message text cannot be blank.");
        }

        if (newMessageText.length() > 255) {
            throw new IllegalArgumentException("Message text cannot exceed 255 characters.");
        }

        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isPresent()) {
            Message message = messageOpt.get();
            message.setMessageText(newMessageText);
            messageRepository.save(message);
        } else {
            throw new IllegalArgumentException("Message does not exist.");
        }
    }

    public List<Message> getMessagesByUser(int accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
