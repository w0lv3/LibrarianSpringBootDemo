package com.books.bonnets.librarian.utils.commons;

public class Enums {

    public enum Status {
        ORDERED((byte) 0),
        PAID((byte)1),
        CANCELLED((byte)2);

        private final byte id;

        Status(byte id) {
            this.id = id;
        }

        //Get id of the status
        public byte getId() {
            return id;
        }

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
