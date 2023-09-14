package com.ElectroMarket.catalogservice.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String resource, Long id) {
        super("A " + resource + " with id " + id + " already exists.");
    }
}
