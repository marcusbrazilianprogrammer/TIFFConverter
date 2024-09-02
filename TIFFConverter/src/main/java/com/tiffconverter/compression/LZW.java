/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiffconverter.compression;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

/**
 *
 * @author Marcus Lessa
 */
public class LZW {

    class LZWSequence implements Comparable {

        List<Byte> sequence = new ArrayList<>();

        LZWSequence(List<Byte> b) {
            sequence = b;
        }

        LZWSequence() {

        }

        @Override
        public int compareTo(Object o) {
            LZWSequence sq = (LZWSequence) o;
            for (int i = 0; i < sq.sequence.size() && i < sequence.size(); i++) {
                if (sequence.get(i) == sq.sequence.get(i)) {
                    continue;
                } else {
                    return sequence.get(i) - sq.sequence.get(i);
                }
            }
            if (sq.sequence.size() != sequence.size()) {
                return sequence.size() - sq.sequence.size();
            }
            return 0;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 47 * hash + Objects.hashCode(this.sequence);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final LZWSequence other = (LZWSequence) obj;
            return this.compareTo(other) == 0;
        }

    }

    class DecompressStats {

        int codeSize;
        int bitsRead;
        
        public DecompressStats(){
            codeSize = MIN_CODE_SIZE;
            bitsRead = -1;
        }
        
    }

    private TreeMap<Integer, byte[]> decompressTable;
    private TreeMap<LZWSequence, Integer> compressTable;
    private final int CLEAR_CODE = 256;
    private final int END_OF_INFORMATION = 257;
    private final int MAX_TABLE_SIZE_DECOMPRESSION = 4094;
    private final int MIN_CODE_SIZE = 9;
    private final int MAX_CODE_SIZE = 12;
    
    public LZW(){
        decompressTable = new TreeMap<>();
    }

    private void initializeCompressTable() {

    }

    private void initializaDecompressTable() {        
        decompressTable.clear();
        for (int t = 0; t < 256; t++) {
            decompressTable.put(t, new byte[]{(byte) t});
        }
    }

    public byte[] decompress(byte[] source) {
        DecompressStats stats = new DecompressStats();
        stats.codeSize = MIN_CODE_SIZE;
        int dictSize = 258;
        int code = 0;
        int oldCode = 0;
        List<Byte> preresult = new ArrayList<>();
        do {
            code = getCode(source, stats);
            if (code == CLEAR_CODE) {
                initializaDecompressTable();
                stats.codeSize = MIN_CODE_SIZE;
                dictSize = 258;
                code = getCode(source, stats);
                if (code == END_OF_INFORMATION || code==-1) {
                    break;
                }
                if (decompressTable.containsKey(code)) {
                    byte[] value = decompressTable.get(code);
                    for (byte b : value) {
                        preresult.add(b);
                    }
                }
                oldCode = code;
            } else if (code == END_OF_INFORMATION || code==-1) {
                break;
            } else if (decompressTable.containsKey(code)) {
                for (byte b : decompressTable.get(code)) {
                    preresult.add((byte) b);
                }
                byte[] outString = new byte[decompressTable.get(oldCode).length + 1];
                System.arraycopy(decompressTable.get(oldCode), 0, outString, 0, decompressTable.get(oldCode).length);
                outString[outString.length - 1] = decompressTable.get(code)[0];
                decompressTable.put(dictSize++, outString);
                oldCode = code;
            } else {
                byte[] outString = new byte[decompressTable.get(oldCode).length + 1];
                System.arraycopy(decompressTable.get(oldCode), 0, outString, 0, decompressTable.get(oldCode).length);
                outString[outString.length - 1] = decompressTable.get(oldCode)[0];
                for (byte b : outString) {
                    preresult.add((byte) b);
                }                
                decompressTable.put(dictSize++, outString);
                oldCode = code;
            }
            if (dictSize == (int) (Math.pow(2, stats.codeSize) - 1) && dictSize < 4094) {
                stats.codeSize = stats.codeSize + 1;
            }
        } while ((stats.bitsRead / 8) < source.length);

        byte[] result = new byte[preresult.size()];
        for (int i = 0; i < preresult.size(); i++) {
            result[i] = preresult.get(i);
        }
        return result;
    }
    
    public byte[] compress(byte[] source){
        return new byte[0];
    }

    public Integer getCode(byte[] source, DecompressStats stats) {
        stats.bitsRead += stats.codeSize;
        int posByte = stats.bitsRead / 8;
        int posBit = stats.bitsRead % 8;
        int code = 0;
        try{
            code = (Byte.toUnsignedInt(source[posByte - 1]) << 8) | Byte.toUnsignedInt(source[posByte]);
            if (posByte - 2 >= 0) {
                code = (Byte.toUnsignedInt(source[posByte - 2]) << 16) | code;
            }
        }catch(Exception e){
            return -1;
        }
        code = code >> (7 - posBit);
        code = (code & (int) (Math.pow(2, stats.codeSize) - 1));
        return code;
    }
      
}
