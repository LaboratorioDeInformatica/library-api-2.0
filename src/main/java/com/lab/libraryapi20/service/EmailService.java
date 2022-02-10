package com.lab.libraryapi20.service;

import java.util.List;

public interface EmailService {

    void sendMails(String message, List<String> emails);
}
