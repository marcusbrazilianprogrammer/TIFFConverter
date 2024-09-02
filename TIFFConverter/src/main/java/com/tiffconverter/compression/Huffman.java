/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiffconverter.compression;

import com.tiffconverter.util.BitIterator;
import com.tiffconverter.util.BitList;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author Marcus Lessa
 */
public class Huffman {

    public static Map<Integer, String> codesWhite = new TreeMap<>();
    public static Map<Integer, String> codesBlack = new TreeMap<>();

    static {
        codesWhite.put(0, "00110101");
        codesWhite.put(1, "000111");
        codesWhite.put(2, "0111");
        codesWhite.put(3, "1000");
        codesWhite.put(4, "1011");
        codesWhite.put(5, "1100");
        codesWhite.put(6, "1110");
        codesWhite.put(7, "1111");
        codesWhite.put(8, "10011");
        codesWhite.put(9, "10100");
        codesWhite.put(10, "00111");
        codesWhite.put(11, "01000");
        codesWhite.put(12, "001000");
        codesWhite.put(13, "000011");
        codesWhite.put(14, "110100");
        codesWhite.put(15, "110101");
        codesWhite.put(16, "101010");
        codesWhite.put(17, "101011");
        codesWhite.put(18, "0100111");
        codesWhite.put(19, "0001100");
        codesWhite.put(20, "0001000");
        codesWhite.put(21, "0010111");
        codesWhite.put(22, "0000011");
        codesWhite.put(23, "0000100");
        codesWhite.put(24, "0101000");
        codesWhite.put(25, "0101011");
        codesWhite.put(26, "0010011");
        codesWhite.put(27, "0100100");
        codesWhite.put(28, "0011000");
        codesWhite.put(29, "00000010");
        codesWhite.put(30, "00000011");
        codesWhite.put(31, "00011010");
        codesWhite.put(32, "00011011");
        codesWhite.put(33, "00010010");
        codesWhite.put(34, "00010011");
        codesWhite.put(35, "00010100");
        codesWhite.put(36, "00010101");
        codesWhite.put(37, "00010110");
        codesWhite.put(38, "00010111");
        codesWhite.put(39, "00101000");
        codesWhite.put(40, "00101001");
        codesWhite.put(41, "00101010");
        codesWhite.put(42, "00101011");
        codesWhite.put(43, "00101100");
        codesWhite.put(44, "00101101");
        codesWhite.put(45, "00000100");
        codesWhite.put(46, "00000101");
        codesWhite.put(47, "00001010");
        codesWhite.put(48, "00001011");
        codesWhite.put(49, "01010010");
        codesWhite.put(50, "01010011");
        codesWhite.put(51, "01010100");
        codesWhite.put(52, "01010101");
        codesWhite.put(53, "00100100");
        codesWhite.put(54, "00100101");
        codesWhite.put(55, "01011000");
        codesWhite.put(56, "01011001");
        codesWhite.put(57, "01011010");
        codesWhite.put(58, "01011011");
        codesWhite.put(59, "01001010");
        codesWhite.put(60, "01001011");
        codesWhite.put(61, "00110010");
        codesWhite.put(62, "00110011");
        codesWhite.put(63, "00110100");
        codesWhite.put(64, "11011");
        codesWhite.put(128, "10010");
        codesWhite.put(192, "010111");
        codesWhite.put(256, "0110111");
        codesWhite.put(320, "00110110");
        codesWhite.put(384, "00110111");
        codesWhite.put(448, "01100100");
        codesWhite.put(512, "01100101");
        codesWhite.put(576, "01101000");
        codesWhite.put(640, "01100111");
        codesWhite.put(704, "011001100");
        codesWhite.put(768, "011001101");
        codesWhite.put(832, "011010010");
        codesWhite.put(896, "011010011");
        codesWhite.put(960, "011010100");
        codesWhite.put(1024, "011010101");
        codesWhite.put(1088, "011010110");
        codesWhite.put(1152, "011010111");
        codesWhite.put(1216, "011011000");
        codesWhite.put(1280, "011011001");
        codesWhite.put(1344, "011011010");
        codesWhite.put(1408, "011011011");
        codesWhite.put(1472, "010011000");
        codesWhite.put(1536, "010011001");
        codesWhite.put(1600, "010011010");
        codesWhite.put(1664, "011000");
        codesWhite.put(1728, "010011011");
        codesWhite.put(1792, "00000001000");
        codesWhite.put(1856, "00000001100");
        codesWhite.put(1920, "00000001101");
        codesWhite.put(1984, "000000010010");
        codesWhite.put(2048, "000000010011");
        codesWhite.put(2112, "000000010100");
        codesWhite.put(2176, "000000010101");
        codesWhite.put(2240, "000000010110");
        codesWhite.put(2304, "000000010111");
        codesWhite.put(2368, "000000011100");
        codesWhite.put(2432, "000000011101");
        codesWhite.put(2496, "000000011110");
        codesWhite.put(2560, "000000011111");
        codesBlack.put(0, "0000110111");
        codesBlack.put(1, "010");
        codesBlack.put(2, "11");
        codesBlack.put(3, "10");
        codesBlack.put(4, "011");
        codesBlack.put(5, "0011");
        codesBlack.put(6, "0010");
        codesBlack.put(7, "00011");
        codesBlack.put(8, "000101");
        codesBlack.put(9, "000100");
        codesBlack.put(10, "0000100");
        codesBlack.put(11, "0000101");
        codesBlack.put(12, "0000111");
        codesBlack.put(13, "00000100");
        codesBlack.put(14, "00000111");
        codesBlack.put(15, "000011000");
        codesBlack.put(16, "0000010111");
        codesBlack.put(17, "0000011000");
        codesBlack.put(18, "0000001000");
        codesBlack.put(19, "00001100111");
        codesBlack.put(20, "00001101000");
        codesBlack.put(21, "00001101100");
        codesBlack.put(22, "00000110111");
        codesBlack.put(23, "00000101000");
        codesBlack.put(24, "00000010111");
        codesBlack.put(25, "00000011000");
        codesBlack.put(26, "000011001010");
        codesBlack.put(27, "000011001011");
        codesBlack.put(28, "000011001100");
        codesBlack.put(29, "000011001101");
        codesBlack.put(30, "000001101000");
        codesBlack.put(31, "000001101001");
        codesBlack.put(32, "000001101010");
        codesBlack.put(33, "000001101011");
        codesBlack.put(34, "000011010010");
        codesBlack.put(35, "000011010011");
        codesBlack.put(36, "000011010100");
        codesBlack.put(37, "000011010101");
        codesBlack.put(38, "000011010110");
        codesBlack.put(39, "000011010111");
        codesBlack.put(40, "000001101100");
        codesBlack.put(41, "000001101101");
        codesBlack.put(42, "000011011010");
        codesBlack.put(43, "000011011011");
        codesBlack.put(44, "000001010100");
        codesBlack.put(45, "000001010101");
        codesBlack.put(46, "000001010110");
        codesBlack.put(47, "000001010111");
        codesBlack.put(48, "000001100100");
        codesBlack.put(49, "000001100101");
        codesBlack.put(50, "000001010010");
        codesBlack.put(51, "000001010011");
        codesBlack.put(52, "000000100100");
        codesBlack.put(53, "000000110111");
        codesBlack.put(54, "000000111000");
        codesBlack.put(55, "000000100111");
        codesBlack.put(56, "000000101000");
        codesBlack.put(57, "000001011000");
        codesBlack.put(58, "000001011001");
        codesBlack.put(59, "000000101011");
        codesBlack.put(60, "000000101100");
        codesBlack.put(61, "000001011010");
        codesBlack.put(62, "000001100110");
        codesBlack.put(63, "000001100111");
        codesBlack.put(64, "0000001111");
        codesBlack.put(128, "000011001000");
        codesBlack.put(192, "000011001001");
        codesBlack.put(256, "000001011011");
        codesBlack.put(320, "000000110011");
        codesBlack.put(384, "000000110100");
        codesBlack.put(448, "000000110101");
        codesBlack.put(512, "0000001101100");
        codesBlack.put(576, "0000001101101");
        codesBlack.put(640, "0000001001010");
        codesBlack.put(704, "0000001001011");
        codesBlack.put(768, "0000001001100");
        codesBlack.put(832, "0000001001101");
        codesBlack.put(896, "0000001110010");
        codesBlack.put(960, "0000001110011");
        codesBlack.put(1024, "0000001110100");
        codesBlack.put(1088, "0000001110101");
        codesBlack.put(1152, "0000001110110");
        codesBlack.put(1216, "0000001110111");
        codesBlack.put(1280, "0000001010010");
        codesBlack.put(1344, "0000001010011");
        codesBlack.put(1408, "0000001010100");
        codesBlack.put(1472, "0000001010101");
        codesBlack.put(1536, "0000001011010");
        codesBlack.put(1600, "0000001011011");
        codesBlack.put(1664, "0000001100100");
        codesBlack.put(1728, "0000001100101");
        codesBlack.put(1792, "00000001000");
        codesBlack.put(1856, "00000001100");
        codesBlack.put(1920, "00000001101");
        codesBlack.put(1984, "000000010010");
        codesBlack.put(2048, "000000010011");
        codesBlack.put(2112, "000000010100");
        codesBlack.put(2176, "000000010101");
        codesBlack.put(2240, "000000010110");
        codesBlack.put(2304, "000000010111");
        codesBlack.put(2368, "000000011100");
        codesBlack.put(2432, "000000011101");
        codesBlack.put(2496, "000000011110");
        codesBlack.put(2560, "000000011111");
    }

    private static int LIMIT_TERMINATING_CODE = 63;

    public Tree createTree(Map<Integer, String> codes) {

        Tree arvore = new Tree();
        arvore.root = new Node();
        Node root = arvore.root;
        for (Integer folha : codes.keySet()) {
            String code = codes.get(folha);
            Node current = root;
            char[] paths = code.toCharArray();
            for (int i = 0; i < paths.length; i++) {
                Node nextNode = paths[i] == '0' ? current.left == null ? new Node(current, false) : current.left : current.right == null ? new Node(current, true) : current.right;
                if (i == paths.length - 1) {
                    nextNode.nodeValue = folha;
                }
                current = nextNode;
            }
        }

        return arvore;
    }

    public Tree createTree(Map<Integer, String> codesWhite, Map<Integer, String> codesBlack) {
        Tree arvore = new Tree();
        arvore.root = new Node();
        Node root = arvore.root;
        for (Integer folha : codesWhite.keySet()) {
            String code = codesWhite.get(folha);
            Node current = root;
            char[] paths = code.toCharArray();
            for (int i = 0; i < paths.length; i++) {
                Node nextNode = paths[i] == '0' ? current.left == null ? new Node(current, false) : current.left : current.right == null ? new Node(current, true) : current.right;
                if (i == paths.length - 1) {
                    nextNode.nodeValue = folha;
                    nextNode.color = false;
                }
                current = nextNode;
            }
        }

        for (Integer folha : codesBlack.keySet()) {
            String code = codesBlack.get(folha);
            Node current = root;
            char[] paths = code.toCharArray();
            for (int i = 0; i < paths.length; i++) {
                Node nextNode = paths[i] == '0' ? current.left == null ? new Node(current, false) : current.left : current.right == null ? new Node(current, true) : current.right;
                if (i == paths.length - 1) {
                    nextNode.nodeValue = folha;
                    nextNode.color = true;
                }
                current = nextNode;
            }
        }

        return arvore;
    }

    public byte[] decompress(byte[] source, final int imageWidth) {
        Tree treeWhite = this.createTree(codesWhite);
        Tree treeBlack = this.createTree(codesBlack);
        BitList list = new BitList(source);
        int bitCount = 0;
        int linePixelCount = 0;
        int lineCount = -1;
        Node currentNode = treeWhite.root;
        StringBuilder resultStringBits = new StringBuilder();
        boolean currentColor = false; // false - white, true - black;
        boolean currentTypeTerminatingCode = false;
        while (bitCount < list.size()) {

            for (int i = bitCount; i < list.size(); i++) {
                currentNode = list.get(i) == 1 ? currentNode.right : currentNode.left;
                if (currentNode == null) {
                    break;
                }
                if (currentNode.nodeValue != null) {
                    break;
                }
            }

            if (currentNode != null) {
                String code = currentColor ? codesBlack.get(currentNode.nodeValue) : codesWhite.get(currentNode.nodeValue);
                bitCount += code.length();
                linePixelCount += currentNode.nodeValue;
                preencher(resultStringBits, currentNode.nodeValue, currentColor ? '1' : '0');

                if (currentNode.nodeValue > LIMIT_TERMINATING_CODE) {
                    currentNode = currentColor ? treeBlack.root : treeWhite.root;
                    currentTypeTerminatingCode = false;
                } else {
                    currentColor = !currentColor;
                    currentNode = currentColor ? treeBlack.root : treeWhite.root;
                    currentTypeTerminatingCode = true;
                }
            }

            if (linePixelCount == imageWidth && currentTypeTerminatingCode) {
                linePixelCount = 0;
                int bitpos = bitCount % 8;
                if (bitpos > 0) {
                    bitCount += (8 - bitpos);
                }
                currentNode = treeWhite.root;
                currentColor = false;
                lineCount++;
                if (imageWidth % 8 > 0) {
                    preencher(resultStringBits, 8 - imageWidth % 8, '0');
                }
            }

        }

        byte[] result = new byte[resultStringBits.length() / 8 + (resultStringBits.length() % 8 > 0 ? 1 : 0)];
        for (int i = 0; i < resultStringBits.length() / 8; i++) {
            result[i] = Integer.valueOf(resultStringBits.substring(i * 8, i * 8 + 8), 2).byteValue();
        }
        int hg = resultStringBits.length();
        if (resultStringBits.length() % 8 > 0) {
            String ag = resultStringBits.substring((resultStringBits.length() / 8) * 8);
            int lastVal = Integer.valueOf(resultStringBits.substring((resultStringBits.length() / 8) * 8, (resultStringBits.length() / 8) * 8 + resultStringBits.length() % 8), 2);
            lastVal = lastVal << (8 - resultStringBits.length() % 8);
            result[result.length - 1] = (byte) lastVal;
        }

        return result;
    }

    public static void preencher(StringBuilder builder, int num, char ch) {
        for (int i = 1; i <= num; i++) {
            builder.append(ch);
        }
    }

    public class Tree {

        Node root;
    }

    public class Node {

        Node father;
        Node left;
        Node right;
        Integer nodeValue;
        boolean color = false;

        public Node() {

        }

        public Node(Node father, boolean direction) {
            this.father = father;
            if (direction) {
                father.right = this;
            } else {
                father.left = this;
            }

        }

    }

}
