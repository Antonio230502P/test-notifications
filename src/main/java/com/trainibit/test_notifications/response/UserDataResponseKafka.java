package com.trainibit.test_notifications.response;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDataResponseKafka {
    private String firstToken;
    private UUID uuid;
    private String email;
    private UUID templateUuid;
}
