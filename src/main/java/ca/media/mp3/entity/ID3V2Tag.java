package ca.media.mp3.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ID3V2Tag {
  private final int[] id3V2tag;
  private final Map<String, Integer> header;
  
  public ID3V2Tag(final int[] mp3) throws IllegalArgumentException {
    if(!isAnID3V2tag(mp3)) {
      throw new IllegalArgumentException("Array does not contain an ID3 V2 tag");
    }

    int size = ((mp3[6] * 0x80 + mp3[7]) * 0x80 + mp3[8]) * 0x80 + mp3[9];
    id3V2tag = Arrays.copyOf(mp3, size);
    header = new HashMap<>();
    header.put("majorVersion", id3V2tag[3]);
    header.put("revisionNumber", id3V2tag[4]);
    header.put("flags", id3V2tag[5]);
    header.put("size", size);
  }
  
  public static boolean isAnID3V2tag(final int[] mp3) throws IllegalArgumentException {
    if(mp3 == null) {
      throw new IllegalArgumentException("Array is null");
    }

    if(mp3.length < 10) {
      throw new IllegalArgumentException("Array is too small");
    }

    return 
        (mp3[0] == 0x49 && mp3[1] == 0x44 && mp3[2] == 0x33) &&
        (mp3[3] < 0xFF && mp3[4] < 0xFF) &&
        (mp3[5] & 0xE0) == mp3[5] &&
        (mp3[6] < 0x80 && mp3[7] < 0x80 && mp3[8] < 0x80 && mp3[9] < 0x80);
  }
    
  public int majorVersion() {
    return header.get("majorVersion");
  }
  
  public int revisionNumber() {
    return header.get("revisionNumber");
  }
  
  public boolean unsynchronisation() {
    return (header.get("flags") & 0x80) == 0x80;
  }
  
  public boolean extendedHeader() {
    return (header.get("flags") & 0x40) == 0x40;
  }
  
  public boolean experimental() {
    return (header.get("flags") & 0x20) == 0x20;
  }
  
  public int flags() {
    return header.get("flags");
  }
  
  public int size() {
    return header.get("size");
  }
  
  @Override
  public String toString() {
    return 
      String.format("\"version\":%d, \"revision\":%d, \"flags\":0x%02X, \"size\":%d", 
      majorVersion(), revisionNumber(), flags(), size());
  }
  
  public Map<String, Integer> header() {
    return header;
  }
}
