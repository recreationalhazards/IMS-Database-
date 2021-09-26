package com.example.demo.service;

import com.example.demo.persistence.dao.DeviceMetadataRepository;
import com.maxmind.geoip2.DatabaseReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ua_parser.Parser;

@Service
public class DeviceService {

    private static final String UNKNOWN = "UNKNOWN";

//    @Value("${support.email}")
    private String from;

    private DeviceMetadataRepository deviceMetadataRepository;
    private DatabaseReader databaseReader;
    private Parser parser;
    private JavaMailSender mailSender;
    private MessageSource messages;
}
