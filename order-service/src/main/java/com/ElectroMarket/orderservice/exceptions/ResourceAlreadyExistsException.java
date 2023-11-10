package com.ElectroMarket.orderservice.exceptions;

import java.util.UUID;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String orderId)  {
        super("Order with id " + orderId + " already exists");
    }
}
