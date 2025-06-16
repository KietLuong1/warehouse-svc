package com.capstone.warehousesvc.dtos;

import com.capstone.warehousesvc.enums.TransactionStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionStatusDTO {
    TransactionStatus status;
}