/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiffconverter;

import com.tiffconverter.compression.Huffman;
import com.tiffconverter.compression.LZW;
import com.tiffconverter.compression.PackBits;
import com.tiffconverter.data.IFDField;
import com.tiffconverter.data.IFDRecord;
import com.tiffconverter.util.BitMapFile;
import com.tiffconverter.util.Constants;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marcuslf
 */
public class TIFFConverter {
    
    public static int readGroup(byte[] group, int byteOrder){
        int retorno = 0;
        for(int i=0;i<group.length;i++){
            int deslocamento = (byteOrder == Constants.LITTLE_ENDIAN) ? i*8:(group.length-(i+1))*8;            
            retorno =  (Byte.toUnsignedInt(group[i])<<(deslocamento)) | retorno;            
        }
        return retorno;
    }
    
    public static IFDRecord readIFD(byte[] data, int byteOrder){
        if(data.length%Constants.IFDRECORD_BYTE_SIZE!=0)
            throw new IllegalArgumentException("O IFD deve ser divisível por 12!");
        int fieldCount = data.length/Constants.IFDRECORD_BYTE_SIZE;
        IFDRecord record = new IFDRecord();
        for(int i=0;i<fieldCount;i++){
            int offset = i*Constants.IFDRECORD_BYTE_SIZE;
            byte[] fieldBytes = new byte[Constants.IFDRECORD_BYTE_SIZE];
            System.arraycopy(data, offset, fieldBytes, 0, fieldBytes.length);
            IFDField ifdfield = new IFDField();
            
            byte[] tag = new byte[2];
            byte[] fieldType = new byte[2];
            byte[] numberValues = new byte[4];
            byte[] valueOffset = new byte[4];
            System.arraycopy(fieldBytes, 0, tag, 0, tag.length);
            System.arraycopy(fieldBytes, 2, fieldType, 0, fieldType.length);
            System.arraycopy(fieldBytes, 4, numberValues, 0, numberValues.length);
            System.arraycopy(fieldBytes, 8, valueOffset, 0, valueOffset.length);
            
            ifdfield.fieldCode = readGroup(tag, byteOrder);
            ifdfield.fieldName = Constants.getFieldName(ifdfield.fieldCode);
            ifdfield.fieldType = readGroup(fieldType,byteOrder);
            ifdfield.numberValues = readGroup(numberValues, byteOrder);
            if(ifdfield.fieldType==Constants.SHORT && ifdfield.numberValues==1){
                ifdfield.valueOffset = readGroup(new byte[]{valueOffset[0],valueOffset[1]},byteOrder);
            }else
                ifdfield.valueOffset = readGroup(valueOffset,byteOrder);
            
            record.fields.add(ifdfield);
        }
        return record;
    }
    
    public static List<IFDRecord> getIFDs(byte[] data){
        List<IFDRecord> records = new ArrayList<IFDRecord>();
        int byteOrder = (data[0] << 8) | data[1];
        if (byteOrder != Constants.BIG_ENDIAN && byteOrder != Constants.LITTLE_ENDIAN) {
            throw new RuntimeException("Não é um arquivo TIFF");
        }
        byte[] bytes = new byte[2];
        System.arraycopy(data, 2, bytes, 0, 2);        
        int marker = readGroup(bytes, byteOrder);
        bytes = new byte[4];
        System.arraycopy(data, 4, bytes, 0, 4);
        int offset = readGroup(bytes, byteOrder);
        
        while(offset>0){
            bytes = new byte[2];
            System.arraycopy(data, offset, bytes, 0, 2);
            int numFields = readGroup(bytes,byteOrder);
            bytes = new byte[numFields*Constants.IFDRECORD_BYTE_SIZE];
            System.arraycopy(data, offset+2, bytes, 0, bytes.length);
            IFDRecord record = readIFD(bytes, byteOrder);
            bytes = new byte[4];
            System.arraycopy(data, offset+2+(numFields*Constants.IFDRECORD_BYTE_SIZE), bytes, 0, bytes.length);
            offset = readGroup(bytes,byteOrder);
            records.add(record);
            for(IFDField f : record.fields){
                setValue(f,data,byteOrder);
            }
        }
        
        return records;
    }
    
    public static void setValue(IFDField field, byte[] data, int byteOrder){
        byte[] valueByte = null;
        switch(field.fieldCode){
            case Constants.XRESOLUTION:
            case Constants.YRESOLUTION:
            case Constants.XPOSITION:
            case Constants.YPOSITION:
                valueByte = new byte[4];
                System.arraycopy(data, field.valueOffset, valueByte, 0, 4);
                int xR = readGroup(valueByte, byteOrder);
                valueByte = new byte[4];
                System.arraycopy(data, field.valueOffset+4, valueByte, 0, 4);                
                int yR = readGroup(valueByte, byteOrder);
                field.value = (float)xR/(float)(yR==0?1:yR);
                break;
            case Constants.WHITEPOINT:
            case Constants.PRIMARYCHROMATICITIES:
            case Constants.REFERENCEBLACKWHITE:
            case Constants.YCBCRCOEFFICIENTS:
                int numbytes = 8 * field.numberValues;
                byte[] source = new byte[numbytes];
                System.arraycopy(data, field.valueOffset, source, 0,numbytes);
                float[] valores = new float[field.numberValues];
                for(int i=0;i<valores.length;i++){
                    byte[] numerador = new byte[4];
                    byte[] denominador = new byte[4];
                    System.arraycopy(source, i*8, numerador, 0, 4);
                    System.arraycopy(source, i*8+4,denominador,0, 4);
                    valores[i] = (float)readGroup(numerador, byteOrder)/(float)readGroup(denominador,byteOrder);
                }
                field.value = valores;
                break;
            case Constants.STRIPBYTECOUNTS:
            case Constants.STRIPOFFSETS:
            case Constants.COLORMAP:
            case Constants.BITSPERSAMPLE:
            case Constants.SAMPLESPERPIXEL:
            case Constants.SAMPLEFORMAT:
            case Constants.TRANSFERRANGE:
            case Constants.TRANSFERFUNCTION:
            case Constants.GRAYRESPONSECURVE:
            case Constants.GRAYRESPONSEUNIT:
                if((field.fieldType==Constants.SHORT && field.numberValues>2) || (field.fieldType==Constants.LONG && field.numberValues>1)){
                    int[] values = new int[field.numberValues];
                    int numberBytes = getSizeOfType(field.fieldType);
                    List<Integer> listaNumerosAuxiliares = new ArrayList<>();                    
                    for(int i=0;i<field.numberValues;i++){
                        byte[] byteAuxiliar = new byte[numberBytes];
                        System.arraycopy(data, field.valueOffset+i*numberBytes, byteAuxiliar, 0, numberBytes);
                        //values[i] = readGroup(byteAuxiliar, byteOrder);
                        if(field.fieldCode!=Constants.COLORMAP)
                            values[i] = readGroup(byteAuxiliar, byteOrder);
                        else
                            values[i] = readGroup(byteAuxiliar,Constants.BIG_ENDIAN);
                            
                    }
                    field.value = values;
                }else{
                    field.value = field.valueOffset;
                }
                break;
            case Constants.PAGENAME:
            case Constants.SOFTWARE:
            case Constants.DATETIME:
            case Constants.COPYRIGHT:
            case Constants.ARTIST:
            case Constants.IMAGEDESCRIPTION:
            case Constants.HOSTCOMPUTER:
            case Constants.DOCUMENTNAME:
            case Constants.TARGETPRINTER:
                if(field.numberValues>4){
                    byte[] textBytes = new byte[field.numberValues];
                    System.arraycopy(data, field.valueOffset, textBytes, 0, field.numberValues);
                    field.value = new String(textBytes);
                }
                break;
        }
    }
    
    private static int getSizeOfType(int type){
        switch(type){
            case Constants.BYTE:               
            case Constants.ASCII:
            case Constants.UNDEFINED:
            case Constants.SBYTE:
                return 1;                
            case Constants.SHORT:
            case Constants.SSHORT:
                return 2;
            case Constants.LONG:
            case Constants.SLONG:
            case Constants.FLOAT:
                return 4;
            case Constants.RATIONAL:
            case Constants.DOUBLE:
                return 8;
        }
        return 0;
    }
        
    
    public static void transformIFDToBitmap(byte[] dataSource, IFDRecord record, String path){
        // obter os dados
        IFDField stripOffsets = record.getFieldByName("stripoffsets");
        IFDField stripByteCounts = record.getFieldByName("stripbytecounts");
        IFDField imageLenght = record.getFieldByName("imageLength");
        IFDField imageWidth = record.getFieldByName("imageWidth");
        IFDField compression = record.getFieldByName("compression");
        IFDField xResolution = record.getFieldByName("xresolution");
        IFDField yResolution = record.getFieldByName("yresolution");
        IFDField resolutionUnit = record.getFieldByName("resolutionUnit");
        IFDField rowsPerStrip = record.getFieldByName("rowsperstrip");
        IFDField photometricInterpretation = record.getFieldByName("photometricinterpretation");
        IFDField bitsPerSample = record.getFieldByName("bitspersample");
        IFDField planarConfiguration = record.getFieldByName("planarconfiguration");
        IFDField colorMap = record.getFieldByName("colorMap");
        
        byte[] data = null;
        if(stripOffsets.numberValues>1){            
            int[] counts = (int[])stripByteCounts.value;
            
            int[] offsets = (int[])stripOffsets.value;
            List<byte[]> strips = new ArrayList<>();
            for(int i=0;i<offsets.length;i++){
                byte[] strip = new byte[counts[i]];
                System.arraycopy(dataSource, offsets[i], strip, 0, strip.length);
                strips.add(decompress(strip,compression.valueOffset,imageWidth.valueOffset));
            }
            int index = 0;
            int totalBytes = 0;
            for(byte[] arr : strips){
                totalBytes += arr.length;
            }
            data = new byte[totalBytes];
            for(byte[] strip : strips){
                for(byte b : strip){
                    data[index++] = b;
                }
            }
        }else{
            data = new byte[stripByteCounts.valueOffset];
            System.arraycopy(dataSource, stripOffsets.valueOffset, data, 0, data.length);
            data = decompress(data, compression.valueOffset,imageWidth.valueOffset);
        }
        
        //organizar os dados no formato bitmap        
        byte[] inverted = null;
        int bytesPerRow = 0 ;
        int bytesPerRowBitmap = 0;
        if (planarConfiguration == null || planarConfiguration.valueOffset == 1) {
            int withPer8 = imageWidth.valueOffset / 8;
            bytesPerRow = data.length / imageLenght.valueOffset;
            //int bytesPerRowBitmap = withPer8%4==0? withPer8 : withPer8 + (4- withPer8%4);
            bytesPerRowBitmap = bytesPerRow % 4 == 0 ? bytesPerRow : bytesPerRow + (4 - bytesPerRow % 4);
            inverted = new byte[bytesPerRowBitmap * imageLenght.valueOffset];
            for (int r = imageLenght.valueOffset - 1; r >= 0; r--) {
                System.arraycopy(data, r * bytesPerRow, inverted, bytesPerRowBitmap * (imageLenght.valueOffset - 1 - r), bytesPerRow);
            }
        }else if(planarConfiguration.valueOffset==2){
            throw new RuntimeException("PlanarConfiguration=2 não implementado");
        }
        
        BitMapFile bitMapFile = new BitMapFile();
        bitMapFile.height = imageLenght.valueOffset;
        bitMapFile.width = imageWidth.valueOffset;
        bitMapFile.pixelData = inverted;
        bitMapFile.compression = 0;
        bitMapFile.imageSize = 0;
        if (resolutionUnit != null) {
            if (resolutionUnit.valueOffset == 1) {

            } else if (resolutionUnit.valueOffset == 2) {
                bitMapFile.xPixelsPerM = (int) (xResolution.valueOffset / 0.0254);
                bitMapFile.yPixelsPerM = (int) (yResolution.valueOffset / 0.0254);
            } else if (resolutionUnit.valueOffset == 3) {
                bitMapFile.xPixelsPerM = (int) (xResolution.valueOffset / 100.0);
                bitMapFile.yPixelsPerM = (int) (yResolution.valueOffset / 100.0);
            }
        }
        //obtidos os dados
        if(photometricInterpretation.valueOffset<=1 && bitsPerSample.valueOffset==1){ //bilevel image        
            bitMapFile.bitsPerPixel = (short)bitsPerSample.valueOffset;
            bitMapFile.colorsUsed = 2;
            bitMapFile.planes = 1;
            byte[][] colors = null;            
            if(photometricInterpretation.valueOffset==0){
                colors = new byte[][] {
                    { (byte)0xFF, (byte)0xFF, (byte)0xFF,0 },
                    { 0,0,0,0 }
                };
            }else{
                colors = new byte[][] {
                    { 0,0,0,0},
                    { (byte)0xFF, (byte)0xFF, (byte)0xFF,0}                    
                };
            }
            bitMapFile.colorTable = colors;
        }else if(photometricInterpretation.valueOffset<=1 && bitsPerSample.valueOffset==4){ // grayscale 16 colors
            bitMapFile.bitsPerPixel = (short)bitsPerSample.valueOffset;
            bitMapFile.colorsUsed = 16;
            bitMapFile.planes = 1;
            byte[][] colors = new byte[16][];
            for(int i=0;i<16; i++){
                colors[i] = new byte[]{(byte)((i<<4)|i),(byte)((i<<4)|i),(byte)((i<<4)|i),0};
            }
            bitMapFile.colorTable = colors;
        }
        else if(photometricInterpretation.valueOffset<=1 && bitsPerSample.valueOffset==8){ // grayscale 256 colors
            bitMapFile.bitsPerPixel = (short)bitsPerSample.valueOffset;
            bitMapFile.colorsUsed = 256;
            bitMapFile.planes = 1;
            byte[][] colors = new byte[256][];
            for(int i=0;i<256; i++){
                colors[i] = new byte[]{(byte)i,(byte)i,(byte)i,0};
            }
            bitMapFile.colorTable = colors;
        }
        else if(photometricInterpretation.valueOffset<=1 && bitsPerSample.valueOffset==16){
            bitMapFile.bitsPerPixel = (short)bitsPerSample.valueOffset;
            bitMapFile.colorsUsed = 0;
            bitMapFile.planes = 1;
        }
        else if(photometricInterpretation.valueOffset==2){ //RGB image
            bitMapFile.bitsPerPixel = 24;
            bitMapFile.colorsUsed = 0;
            bitMapFile.planes = 1;
            IFDField sampleFormat = record.getFieldByName("sampleformat");
            if(planarConfiguration.valueOffset ==1 ){
                int[] values = (int[])bitsPerSample.value;
                int sum = 0;
                for(int i : values)
                    sum += i;
                List<byte[]> pixelColors = new ArrayList<>();
                if(sum==48){
                    int newPixelLineSize = imageWidth.valueOffset*3;
                    int size = (newPixelLineSize)%4==0? newPixelLineSize : newPixelLineSize + (4-(newPixelLineSize)%4);
                    byte[] transform = new byte[size*imageLenght.valueOffset];
                    for (int i = 0; i < imageLenght.valueOffset; i++) {
                        for (int px=0;px<imageWidth.valueOffset;px++) {                                                        
                            transform[i*size + px*3] = inverted[i * bytesPerRowBitmap + px*6 + 4];
                            transform[i*size + px*3+ 1] = inverted[i * bytesPerRowBitmap + px*6 + 2];
                            transform[i*size + px*3+ 2] = inverted[i * bytesPerRowBitmap + px*6];
                        }
                    }                    
                    bitMapFile.pixelData = transform;
                }else if (sum == 24) {
                    for (int i = 0; i < imageLenght.valueOffset; i++) {
                        for (int d = 0; d < bytesPerRow; d += 3) {
                            byte intermediate = inverted[i * bytesPerRowBitmap + d];
                            inverted[i * bytesPerRowBitmap + d] = inverted[i * bytesPerRowBitmap + d + 2];
                            inverted[i * bytesPerRowBitmap + d + 2] = intermediate;
                        }
                    }
                }
                /**/
                
            }
        }else if(photometricInterpretation.valueOffset==3){ //pallette image
            bitMapFile.bitsPerPixel = (short)bitsPerSample.valueOffset;
            bitMapFile.planes = 1;
            int[] colors = (int[])colorMap.value;
            //int[][] colorsTwoBytes = new int[colors.length/3][];
            
            byte[][] newColorMap = new byte[colors.length/3][];
            for(int i=0;i<colors.length/3;i++){
                
                byte[] convertedColors = new byte[]{            
                    (byte)Math.round(255.0*(colors[(2*colors.length)/3+i]/65535.0)),
                    (byte)Math.round(255.0*(colors[colors.length/3+i]/65535.0)),
                    (byte)Math.round(255.0*(colors[i]/65535.0)),
                    0
                };
                newColorMap[i] = convertedColors;
            }
            bitMapFile.colorTable = newColorMap;
        }
       try{ 
            File f = new File(path);
            FileOutputStream fileout = new FileOutputStream(f);
            fileout.write(bitMapFile.toByteArray());
            fileout.flush();
            fileout.close();
       }catch(Exception e){
           e.printStackTrace();
       }
        
        
    }
    
    private static byte[] decompress(byte[] data, int method){
        if(method==1)
            return data;        
        else if(method==5){
            LZW lzw = new LZW();
            return lzw.decompress(data);
        }
        else if(method == 32773){
            return new PackBits().decompress(data);
        }
        return data;
    }
    
    private static byte[] decompress(byte[] data, int method, int imageWidth){
        if(method==1)
            return data;                
        else if(method==2){
            Huffman huffman = new Huffman();
            byte[] result = huffman.decompress(data, imageWidth);
            return result;
        }else if(method==3){
            
        }else if(method==4){
            
        }else if(method==5){
            LZW lzw = new LZW();
            return lzw.decompress(data);
        }
        return data;
    }
    
   
   
    
}
