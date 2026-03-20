package com.books.bonnets.librarian.utils.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Enums {

    @Getter
    @AllArgsConstructor( access = lombok.AccessLevel.PRIVATE)//Set the enum constructor to private
    public enum Status {
        ORDERED((byte) 0),
        PAID((byte)1),
        CANCELLED((byte)2);

        //Get id of the status
        private final byte id;

        //Get the status from the id
        public static Status fromValue(byte id) {
            for (Status status : Status.values()) {
                if (status.getId() == id) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid status id: " + id);
        }
    }


}
