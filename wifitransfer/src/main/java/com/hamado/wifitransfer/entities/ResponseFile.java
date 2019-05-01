package com.hamado.wifitransfer.entities;

public class ResponseFile extends ResponseDirectory {
        public long size;

        public ResponseFile(String name, String path, long size) {
            super(name, path);
            this.size = size;
        }
    }