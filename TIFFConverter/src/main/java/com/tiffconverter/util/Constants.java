/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiffconverter.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author marcuslf
 */
public class Constants {

    private final static Map<Integer, String> fieldNames = new HashMap<>();

    public final static int IFDRECORD_BYTE_SIZE = 0XC;

    public final static int LITTLE_ENDIAN = 0x4949;
    public final static int BIG_ENDIAN = 0X4D4D;

    //TYPES
    public final static int BYTE = 1;
    public final static int ASCII = 2;
    public final static int SHORT = 3;
    public final static int LONG = 4;
    public final static int RATIONAL = 5;
    public final static int SBYTE = 6;
    public final static int UNDEFINED = 7;
    public final static int SSHORT = 8;
    public final static int SLONG = 9;
    public final static int SRATIONAL = 10;
    public final static int FLOAT = 11;
    public final static int DOUBLE = 12;

    //FIELDS
    public final static int NEWSUBFILETYPE = 0XFE;
    public final static int SUBFILETYPE = 0XFF;
    public final static int IMAGEWIDTH = 0x0100;
    public final static int IMAGELENGTH = 0x0101;
    public final static int BITSPERSAMPLE = 0x0102;
    public final static int COMPRESSION = 0x0103;
    public final static int PHOTOMETRICINTERPRETATION = 0x0106;
    public final static int THRESHHOLDING = 0x0107;
    public final static int CELLWIDTH = 0x108;
    public final static int CELLLENGTH = 0X109;
    public final static int FILLORDER = 0X10A;
    public final static int DOCUMENTNAME = 0X10D;
    public final static int IMAGEDESCRIPTION = 0X10E;
    public final static int MAKE = 0X10F;
    public final static int MODEL = 0X110;
    public final static int STRIPOFFSETS = 0X111;
    public final static int ORIENTATION = 0X112;
    public final static int SAMPLESPERPIXEL = 0X115;
    public final static int ROWSPERSTRIP = 0X116;
    public final static int STRIPBYTECOUNTS = 0X117;
    public final static int MINSAMPLEVALUE = 0X118;
    public final static int MAXSAMPLEVALUE = 0X119;
    public final static int XRESOLUTION = 0X11A;
    public final static int YRESOLUTION = 0X11B;
    public final static int PLANARCONFIGURATION = 0X11C;
    public final static int PAGENAME = 0X11D;
    public final static int XPOSITION = 0X11E;
    public final static int YPOSITION = 0X11F;
    public final static int FREEOFFSETS = 0X120;
    public final static int FREEBYTECOUNTS = 0X121;
    public final static int GRAYRESPONSEUNIT = 0X122;
    public final static int GRAYRESPONSECURVE = 0X123;
    public final static int T4OPTIONS = 0X124;
    public final static int T6OPTIONS = 0X125;
    public final static int RESOLUTIONUNIT = 0X128;
    public final static int PAGENUMBER = 0X129;
    public final static int TRANSFERFUNCTION = 0X12D;
    public final static int SOFTWARE = 0X131;
    public final static int DATETIME = 0X132;
    public final static int ARTIST = 0X13B;
    public final static int HOSTCOMPUTER = 0X13C;
    public final static int PREDICTOR = 0X13D;
    public final static int WHITEPOINT = 0X13E;
    public final static int PRIMARYCHROMATICITIES = 0X13F;
    public final static int COLORMAP = 0X140;
    public final static int HALFTONEHINTS = 0X141;
    public final static int TILEWIDTH = 0X142;
    public final static int TILELENGTH = 0X143;
    public final static int TILEOFFSETS = 0X144;
    public final static int TILEBYTECOUNTS = 0X145;
    public final static int INKSET = 0X14C;
    public final static int INKNAMES = 0X14D;
    public final static int NUMBEROFINKS = 0X14E;
    public final static int DOTRANGE = 0X150;
    public final static int TARGETPRINTER = 0X151;
    public final static int EXTRASAMPLES = 0X152;
    public final static int SAMPLEFORMAT = 0X153;
    public final static int SMINSAMPLEVALUE = 0X154;
    public final static int SMAXSAMPLEVALUE = 0X155;
    public final static int TRANSFERRANGE = 0X156;
    public final static int JPEGPROC = 0X200;
    public final static int JPEGINTERCHANGEFORMAT = 0X201;
    public final static int JPEGINTERCHANCEFORMATLENGTH = 0X202;
    public final static int JPEGRESTARTINTERVAL = 0X203;
    public final static int JPEGLOSSLESSPREDICTORS = 0X205;
    public final static int JPEGPOINTTRANSFORMS = 0X206;
    public final static int JPEGQTABLES = 0X207;
    public final static int JPEGDCTABLES = 0X208;
    public final static int JPEGACTABLES = 0X209;
    public final static int YCBCRCOEFFICIENTS = 0X211;
    public final static int YCBCRSUBSAMPLING = 0X212;
    public final static int YCBCRPOSITIONING = 0X213;
    public final static int REFERENCEBLACKWHITE = 0X214;
    public final static int COPYRIGHT = 0X8298;

    static {
        fieldNames.put(NEWSUBFILETYPE, "NewSubFileType");
        fieldNames.put(SUBFILETYPE, "SubFileType");
        fieldNames.put(IMAGEWIDTH, "ImageWidth");
        fieldNames.put(IMAGELENGTH, "ImageLength");
        fieldNames.put(BITSPERSAMPLE, "BitsPerSample");
        fieldNames.put(COMPRESSION, "Compression");
        fieldNames.put(PHOTOMETRICINTERPRETATION, "PhotometricInterpretation");
        fieldNames.put(THRESHHOLDING, "ThreshHolding");
        fieldNames.put(CELLWIDTH, "CellWidth");
        fieldNames.put(CELLLENGTH, "CellLenght");
        fieldNames.put(FILLORDER, "FillOrder");
        fieldNames.put(DOCUMENTNAME, "DocumentName");
        fieldNames.put(IMAGEDESCRIPTION, "ImageDescription");
        fieldNames.put(MAKE, "Make");
        fieldNames.put(MODEL, "Model");
        fieldNames.put(STRIPOFFSETS, "StripOffsets");
        fieldNames.put(ORIENTATION, "Orientation");
        fieldNames.put(SAMPLESPERPIXEL, "SamplesPerPixel");
        fieldNames.put(ROWSPERSTRIP, "RowsPerStrip");
        fieldNames.put(STRIPBYTECOUNTS, "StripByteCounts");
        fieldNames.put(MINSAMPLEVALUE, "MinSampleValue");
        fieldNames.put(MAXSAMPLEVALUE, "MaxSampleValue");
        fieldNames.put(XRESOLUTION, "XResolution");
        fieldNames.put(YRESOLUTION, "YResolution");
        fieldNames.put(PLANARCONFIGURATION, "PlanarConfiguration");
        fieldNames.put(PAGENAME, "PageName");
        fieldNames.put(XPOSITION, "XPosition");
        fieldNames.put(YPOSITION, "YPosition");
        fieldNames.put(FREEOFFSETS, "FreeOffsets");
        fieldNames.put(FREEBYTECOUNTS, "FreeByteCounts");
        fieldNames.put(GRAYRESPONSEUNIT, "GrayResponseUnit");
        fieldNames.put(GRAYRESPONSECURVE, "GrayResponseCurve");
        fieldNames.put(T4OPTIONS, "T4Options");
        fieldNames.put(T6OPTIONS, "T6Options");
        fieldNames.put(RESOLUTIONUNIT, "ResolutionUnit");
        fieldNames.put(PAGENUMBER, "PageNumber");
        fieldNames.put(TRANSFERFUNCTION, "TranferFunction");
        fieldNames.put(SOFTWARE, "Software");
        fieldNames.put(DATETIME, "DateTime");
        fieldNames.put(ARTIST, "Artist");
        fieldNames.put(HOSTCOMPUTER, "HostComputer");
        fieldNames.put(PREDICTOR, "Predictor");
        fieldNames.put(WHITEPOINT, "WhitePoint");
        fieldNames.put(PRIMARYCHROMATICITIES, "PrimaryChromaticities");
        fieldNames.put(COLORMAP, "ColorMap");
        fieldNames.put(HALFTONEHINTS, "HalfToneHints");
        fieldNames.put(TILEWIDTH, "TileWidth");
        fieldNames.put(TILELENGTH, "TileLength");
        fieldNames.put(TILEOFFSETS, "TileOffsets");
        fieldNames.put(TILEBYTECOUNTS, "TlleByteCounts");
        fieldNames.put(INKSET, "InkSet");
        fieldNames.put(INKNAMES, "InkNames");
        fieldNames.put(NUMBEROFINKS, "NumberOfInks");
        fieldNames.put(DOTRANGE, "DotRange");
        fieldNames.put(TARGETPRINTER, "TargetPrinter");
        fieldNames.put(EXTRASAMPLES, "ExtraSamples");
        fieldNames.put(SAMPLEFORMAT, "SampleFormat");
        fieldNames.put(SMINSAMPLEVALUE, "SMinSampleValue");
        fieldNames.put(SMAXSAMPLEVALUE, "SMaxSampleValue");
        fieldNames.put(TRANSFERRANGE, "TransferRange");
        fieldNames.put(JPEGPROC, "JPEGProc");
        fieldNames.put(JPEGINTERCHANGEFORMAT, "JPEGInterchangeFormat");
        fieldNames.put(JPEGINTERCHANCEFORMATLENGTH, "JPEGInterchangeFormatLenght");
        fieldNames.put(JPEGRESTARTINTERVAL, "JPEGRestartInterval");
        fieldNames.put(JPEGLOSSLESSPREDICTORS, "JPEGLossLessPredictors");
        fieldNames.put(JPEGPOINTTRANSFORMS, "JPEGPointTransforms");
        fieldNames.put(JPEGQTABLES, "JPEGQTables");
        fieldNames.put(JPEGDCTABLES, "JPEGDCTables");
        fieldNames.put(JPEGACTABLES, "JPEGACTables");
        fieldNames.put(YCBCRCOEFFICIENTS, "YCBCRCoefficients");
        fieldNames.put(YCBCRSUBSAMPLING, "YCBCRSubSampling");
        fieldNames.put(YCBCRPOSITIONING, "YCBCRPositioning");
        fieldNames.put(REFERENCEBLACKWHITE, "ReferenceBlackWhite");
        fieldNames.put(COPYRIGHT, "Copyright");
    }

    public static String getFieldName(int field) {
        return fieldNames.get(field);
    }

}
